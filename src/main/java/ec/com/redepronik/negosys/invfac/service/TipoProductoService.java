package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.TipoProducto;

public interface TipoProductoService {

	@Transactional
	public List<TipoProducto> obtener();
}