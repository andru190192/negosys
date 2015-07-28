package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.moraTotal;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.entity.DetalleCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadesCreditoReporte;

@Service
public class CreditoServiceImpl implements CreditoService {

	@Autowired
	private MoraService moraService;

	public CantidadesCreditoReporte calcularCuota(
			DetalleCredito detallesCredito,
			CantidadesCreditoReporte cantidadesCreditoReporte,
			Date fechaEgreso, Date fecha) {
		cantidadesCreditoReporte
				.setSaldoCredito(redondearTotales(detallesCredito.getSaldo()));
		cantidadesCreditoReporte.setMoraCredito(newBigDecimal());
		cantidadesCreditoReporte
				.setTotalCredito(redondearTotales(detallesCredito.getSaldo()));
		if (compareTo(fecha, detallesCredito.getFechaMora()) >= 0) {
			BigDecimal mora = moraService.obtenerPorFecha(fechaEgreso)
					.getPorcentaje();
			BigDecimal moraTotal = moraTotal(fecha,
					detallesCredito.getFechaMora(), mora);
			cantidadesCreditoReporte
					.setMoraCredito(redondearTotales(multiplicarDivide(
							moraTotal, detallesCredito.getSaldo())));
			cantidadesCreditoReporte
					.setTotalCredito(redondearTotales(detallesCredito
							.getSaldo().add(
									cantidadesCreditoReporte.getMoraCredito())));
		}
		return cantidadesCreditoReporte;
	}

}