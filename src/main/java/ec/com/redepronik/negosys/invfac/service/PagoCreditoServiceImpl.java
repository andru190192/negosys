package ec.com.redepronik.negosys.invfac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.PagoCreditoDao;
import ec.com.redepronik.negosys.invfac.entity.PagoCredito;

@Service
public class PagoCreditoServiceImpl implements PagoCreditoService {

	@Autowired
	private PagoCreditoDao pagoCreditoDao;

	public String actualizar(PagoCredito pagocredito) {
		pagoCreditoDao.actualizar(pagocredito);
		return "SAVE";
	}

	public PagoCredito obtenerPorPagoCreditoId(Integer pagoCreditoId) {
		return pagoCreditoDao.obtenerPorId(PagoCredito.class, pagoCreditoId);
	}
}