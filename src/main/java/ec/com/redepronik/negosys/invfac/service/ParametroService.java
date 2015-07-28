package ec.com.redepronik.negosys.invfac.service;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Parametro;

public interface ParametroService {
	@Transactional
	public void actualizar(Parametro parametro);

	@Transactional
	public Parametro obtener();

}
