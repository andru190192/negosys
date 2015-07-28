package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Mora;

public interface MoraService {
	@Transactional
	public void actualizar(Mora mora);

	@Transactional
	public void insertar(Mora mora);

	@Transactional
	public List<Mora> obtener();

	@Transactional
	public Mora obtenerPorFecha(Date fecha);

}
