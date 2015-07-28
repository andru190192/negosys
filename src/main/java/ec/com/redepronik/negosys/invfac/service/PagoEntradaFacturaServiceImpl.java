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

import ec.com.redepronik.negosys.invfac.dao.PagoEntradaDao;
import ec.com.redepronik.negosys.invfac.entity.Entrada;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.PagoEntrada;
import ec.com.redepronik.negosys.invfac.entity.TipoPago;
import ec.com.redepronik.negosys.invfac.entityAux.CobroReporte;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class PagoEntradaFacturaServiceImpl implements PagoEntradaFacturaService {

	@Autowired
	private PagoEntradaDao pagoEntradaDao;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private EmpleadoService empleadoService;

	public String actualizar(PagoEntrada pagoEntrada) {
		pagoEntradaDao.actualizar(pagoEntrada);
		return "SAVE";
	}

	public void cobro(List<PagoEntrada> listaPagoEntrada,
			List<CobroReporte> listaFacturas,
			List<CobroReporte> listaFacturasSeleccionados) {

		CobroReporte cobroReporte = null;
		Factura factura = null;
		int ic = 0;
		int ie = 0;
		BigDecimal cuota = newBigDecimal();
		BigDecimal saldo = newBigDecimal();
		do {
			// OBTENER VALORES INICIALES
			if (saldo.compareTo(newBigDecimal()) <= 0) {
				cobroReporte = listaFacturasSeleccionados.get(ie);
				factura = facturaService.obtenerPorFacturaId(cobroReporte
						.getId());
				saldo = cobroReporte.getSaldo();
			}
			if (cuota.compareTo(newBigDecimal()) == 0)
				cuota = listaPagoEntrada.get(ic).getCuota();

			// CONSTRUIR PAGO ENTRADA
			PagoEntrada pagoEntradaAux = listaPagoEntrada.get(ic);
			PagoEntrada pagoEntrada = new PagoEntrada();
			pagoEntrada.setBanco(pagoEntradaAux.getBanco());
			pagoEntrada.setChequeVaucher(pagoEntradaAux.getChequeVaucher());
			pagoEntrada.setCuentaTarjeta(pagoEntradaAux.getCuentaTarjeta());
			pagoEntrada.setCuota(pagoEntradaAux.getCuota());
			pagoEntrada.setFechaCheque(pagoEntradaAux.getFechaCheque());
			pagoEntrada.setFechaPago(pagoEntradaAux.getFechaPago());
			if (pagoEntradaAux.getTipoPago().getId() == 3)
				pagoEntrada.setPagado(false);
			else
				pagoEntrada.setPagado(true);

			pagoEntrada.setFechaGiro(pagoEntradaAux.getFechaGiro());
			pagoEntrada.setAnulado(false);
			pagoEntrada.setDetalle(pagoEntradaAux.getDetalle());
			pagoEntrada.setTipoPago(pagoEntradaAux.getTipoPago());
			pagoEntrada.setActivo(true);

			// CONSTRUIR ENTRADA
			Entrada entrada = new Entrada();
			entrada.setCajero(empleadoService
					.obtenerEmpleadoCargoPorCedulaAndCargo(
							SecurityContextHolder.getContext()
									.getAuthentication().getName(), 4));
			entrada.setFechaLimite(pagoEntrada.getFechaPago());
			entrada.setFechaMora(pagoEntrada.getFechaPago());
			entrada.setFechaPago(pagoEntrada.getFechaPago());
			if (pagoEntradaAux.getTipoPago().getId() == 3)
				entrada.setPagado(false);
			else
				entrada.setPagado(true);

			entrada.setSaldo(newBigDecimal());
			entrada.setActivo(true);

			// PASAR CUOTAS SEGUN CONDICION
			if (saldo.compareTo(cuota) >= 0) {
				entrada.setCuota(cuota);
				pagoEntrada.setCuota(cuota);
				saldo = saldo.subtract(cuota);
				cuota = newBigDecimal();
				ic++;
			} else {
				entrada.setCuota(saldo);
				pagoEntrada.setCuota(saldo);
				BigDecimal saldoAux = saldo;
				saldo = saldo.subtract(cuota);
				cuota = cuota.subtract(saldoAux);
				ie++;
			}

			// AGREGAR PAGOENTRADA->ENTRADA->EGRESO
			entrada.setPagoEntradas(new ArrayList<PagoEntrada>());
			entrada.addPagoEntrada(pagoEntrada);
			factura.addEntrada(entrada);

			int c = 1;
			List<Entrada> list = factura.getEntradas();
			if (list != null && !list.isEmpty())
				for (Entrada e : list)
					e.setOrden(c++);

			// DAR POR CANCELADO EL EGRESO
			if (saldo.compareTo(newBigDecimal()) <= 0) {
				factura.setFechaCierre(timestamp());
				Factura e = facturaService.obtenerPorFacturaId(factura.getId());
				e.setFechaCierre(timestamp());
				facturaService.actualizar(e);
				listaFacturas.remove(cobroReporte);
			} else {
				cobroReporte.setEscogido(false);
				cobroReporte.setOrden(0);
				cobroReporte.setTotal(facturaService.calcularTotal(factura));
				cobroReporte.setAbono(facturaService.calcularEntradas(factura));
				cobroReporte.setSaldo(cobroReporte.getTotal().subtract(
						cobroReporte.getAbono()));
			}

			// GUARDAR EL EGRESO CON SUS ENTRADAS
			// if (saldo.compareTo(newBigDecimal()) <= 0
			// || (ic == listaPagoEntrada.size() && cuota
			// .compareTo(newBigDecimal()) <= 0)) {
			// // listaFactura.add(factura);
			//
			// }

		} while (ic <= (listaPagoEntrada.size() - 1));
	}

	public PagoEntrada obtenerPorPagoEntradaId(Integer pagoEntradaId) {
		return pagoEntradaDao.obtenerPorId(PagoEntrada.class, pagoEntradaId);
	}

	public void pagoLote(List<CobroReporte> listaFacturas,
			List<CobroReporte> listaFacturasSeleccionados, Date fechaPagoRapido) {
		Factura factura = null;

		for (CobroReporte cr : listaFacturasSeleccionados) {
			factura = facturaService.obtenerPorFacturaId(cr.getId());
			if (factura.getEntradas() == null)
				factura.setEntradas(new ArrayList<Entrada>());

			// CONSTRUIR PAGO ENTRADA
			PagoEntrada pagoEntrada = new PagoEntrada();
			pagoEntrada.setCuota(cr.getSaldo());
			pagoEntrada.setFechaPago(timestampCompleto(fechaPagoRapido));
			pagoEntrada.setPagado(true);
			pagoEntrada.setTipoPago(TipoPago.EF);
			pagoEntrada.setActivo(true);

			// CONSTRUIR ENTRADA
			Entrada entrada = new Entrada();
			entrada.setPagoEntradas(new ArrayList<PagoEntrada>());
			entrada.addPagoEntrada(pagoEntrada);
			entrada.setFechaLimite(pagoEntrada.getFechaPago());
			entrada.setFechaMora(pagoEntrada.getFechaPago());
			entrada.setFechaPago(pagoEntrada.getFechaPago());
			entrada.setPagado(true);
			entrada.setSaldo(newBigDecimal());
			entrada.setCuota(cr.getSaldo());
			entrada.setCajero(empleadoService
					.obtenerEmpleadoCargoPorCedulaAndCargo(
							SecurityContextHolder.getContext()
									.getAuthentication().getName(), 4));
			entrada.setActivo(true);

			factura.addEntrada(entrada);
			factura.setFechaCierre(timestampCompleto(fechaPagoRapido));
			int c = 1;
			List<Entrada> list = factura.getEntradas();
			if (list != null && !list.isEmpty())
				for (Entrada e : list)
					e.setOrden(c++);
			facturaService.actualizar(factura);
			listaFacturas.remove(cr);
		}
	}
}