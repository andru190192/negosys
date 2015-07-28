package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Unidad;

public interface UnidadService {
	@Transactional
	public void actualizar(Unidad unidad);

	@Transactional
	public void eliminar(Unidad unidad);

	@Transactional
	public List<Unidad> insertar(Unidad unidad);

	@Transactional
	public List<Unidad> obtener(Boolean activo);

	@Transactional
	public Unidad obtenerPorUnidadId(Integer unidadId);
}