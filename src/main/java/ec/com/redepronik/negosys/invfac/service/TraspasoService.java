package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Traspaso;

public interface TraspasoService {
	@Transactional
	public String insertar(Traspaso traspaso);

	@Transactional
	public List<Traspaso> obtener();
}