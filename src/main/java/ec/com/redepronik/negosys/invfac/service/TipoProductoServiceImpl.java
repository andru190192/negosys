package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.TipoProductoDao;
import ec.com.redepronik.negosys.invfac.entity.TipoProducto;

@Service
public class TipoProductoServiceImpl implements TipoProductoService {

	@Autowired
	private TipoProductoDao tipoProductoDao;

	public List<TipoProducto> obtener() {
		return tipoProductoDao.obtener(TipoProducto.class, "nombre", null);
	}
}