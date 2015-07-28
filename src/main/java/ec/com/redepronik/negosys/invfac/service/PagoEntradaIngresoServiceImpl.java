package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestampCompleto;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.entity.CuotaIngreso;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entity.TipoPago;
import ec.com.redepronik.negosys.invfac.entityAux.CobroReporte;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class PagoEntradaIngresoServiceImpl implements PagoEntradaIngresoService {

	@Autowired
	private FacturaService egresoService;

	@Autowired
	private IngresoService ingresoService;

	@Autowired
	private EmpleadoService empleadoService;

	public void cobro(List<CuotaIngreso> listaCuotaIngreso,
			List<CobroReporte> listaIngresos) {

		CobroReporte cobroReporte = null;
		Ingreso ingreso = null;
		int ic = 0;
		int ie = 0;
		BigDecimal cuota = newBigDecimal();
		BigDecimal saldo = newBigDecimal();
		do {
			// OBTENER VALORES INICIALES
			if (saldo.compareTo(newBigDecimal()) <= 0) {
				cobroReporte = listaIngresos.get(ie);
				ingreso = ingresoService.obtenerPorIngresoId(cobroReporte
						.getId().longValue());
				saldo = cobroReporte.getSaldo();
			}
			if (cuota.compareTo(newBigDecimal()) == 0)
				cuota = listaCuotaIngreso.get(ic).getCuota();

			// CONSTRUIR PAGO ENTRADA
			CuotaIngreso cuotaIngresoAux = listaCuotaIngreso.get(ic);
			CuotaIngreso cuotaIngreso = new CuotaIngreso();
			cuotaIngreso.setBanco(cuotaIngresoAux.getBanco());
			cuotaIngreso.setChequeVaucher(cuotaIngresoAux.getChequeVaucher());
			cuotaIngreso.setCuentaTarjeta(cuotaIngresoAux.getCuentaTarjeta());
			cuotaIngreso.setCuota(cuotaIngresoAux.getCuota());
			cuotaIngreso.setFechaCheque(cuotaIngresoAux.getFechaCheque());
			cuotaIngreso.setFechaPago(cuotaIngresoAux.getFechaPago());
			cuotaIngreso.setFechaLimite(cuotaIngresoAux.getFechaPago());
			if (cuotaIngresoAux.getTipoPago().getId() == 3)
				cuotaIngreso.setPagado(false);
			else
				cuotaIngreso.setPagado(true);

			cuotaIngreso.setFechaGiro(cuotaIngresoAux.getFechaGiro());
			cuotaIngreso.setAnulado(false);
			cuotaIngreso.setDetalle(cuotaIngresoAux.getDetalle());
			cuotaIngreso.setTipoPago(cuotaIngresoAux.getTipoPago());
			cuotaIngreso.setCajero(empleadoService
					.obtenerEmpleadoCargoPorCedulaAndCargo(
							SecurityContextHolder.getContext()
									.getAuthentication().getName(), 4));

			// PASAR CUOTAS SEGUN CONDICION
			if (saldo.compareTo(cuota) >= 0) {
				cuotaIngreso.setCuota(cuota);
				saldo = saldo.subtract(cuota);
				cuota = newBigDecimal();
				ic++;
			} else {
				cuotaIngreso.setCuota(saldo);
				BigDecimal saldoAux = saldo;
				saldo = saldo.subtract(cuota);
				cuota = cuota.subtract(saldoAux);
				ie++;
			}

			ingreso.addCuotaingreso(cuotaIngreso);

			// DAR POR CANCELADO EL EGRESO
			if (saldo.compareTo(newBigDecimal()) <= 0) {
				ingreso.setPagado(true);
				ingreso.setFechaCierre(timestamp());
				ingresoService.actualizar(ingreso);
			}

			// GUARDAR EL EGRESO CON SUS ENTRADAS
			// if (saldo.compareTo(newBigDecimal()) <= 0
			// || (ic == listaPagoEntrada.size() && cuota
			// .compareTo(newBigDecimal()) <= 0)) {
			// // listaEgreso.add(egreso);
			//
			// }

		} while (ic <= (listaCuotaIngreso.size() - 1));
	}

	public void pagoLote(List<CobroReporte> listaIngresos, Date fechaPagoRapido) {
		Ingreso ingreso = null;

		for (CobroReporte cr : listaIngresos) {
			ingreso = ingresoService
					.obtenerPorIngresoId(cr.getId().longValue());
			if (ingreso.getCuotaIngresos() == null)
				ingreso.setCuotaIngresos(new ArrayList<CuotaIngreso>());

			// CONSTRUIR PAGO ENTRADA
			CuotaIngreso cuotaIngreso = new CuotaIngreso();
			cuotaIngreso.setCuota(cr.getSaldo());
			cuotaIngreso.setFechaPago(timestampCompleto(fechaPagoRapido));
			cuotaIngreso.setFechaLimite(cuotaIngreso.getFechaPago());
			cuotaIngreso.setPagado(true);
			cuotaIngreso.setTipoPago(TipoPago.EF);
			cuotaIngreso.setCajero(empleadoService
					.obtenerEmpleadoCargoPorCedulaAndCargo(
							SecurityContextHolder.getContext()
									.getAuthentication().getName(), 4));

			ingreso.addCuotaingreso(cuotaIngreso);
			ingreso.setPagado(true);
			ingreso.setFechaCierre(timestampCompleto(fechaPagoRapido));
			ingresoService.actualizar(ingreso);
		}
	}

}