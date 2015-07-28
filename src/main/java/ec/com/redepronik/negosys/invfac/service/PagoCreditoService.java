package ec.com.redepronik.negosys.invfac.service;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.PagoCredito;

public interface PagoCreditoService {
	@Transactional
	public String actualizar(PagoCredito pagocredito);

	@Transactional
	public PagoCredito obtenerPorPagoCreditoId(Integer pagoCreditoId);
}