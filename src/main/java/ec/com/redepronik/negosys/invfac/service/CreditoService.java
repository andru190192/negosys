package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.DetalleCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadesCreditoReporte;

public interface CreditoService {

	@Transactional
	public CantidadesCreditoReporte calcularCuota(
			DetalleCredito detallesCredito,
			CantidadesCreditoReporte cantidadesCreditoReporte,
			Date fechaEgreso, Date fecha);

}