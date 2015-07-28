package ec.com.redepronik.negosys.rrhh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;

public interface CiudadService {
	@Transactional
	public String actualizar(Ciudad ciudad);

	@Transactional
	public void eliminar(Ciudad ciudad);

	@Transactional
	public void insertar(Ciudad ciudad);

	@Transactional
	public List<Ciudad> obtener(Boolean activo);

	@Transactional
	public Ciudad obtenerPorCiudadId(int ciudadId);

	@Transactional
	public List<Ciudad> obtenerPorProvincia(Provincia provincia);

	@Transactional
	public List<Ciudad> obtenerPorProvincia(String provincia);
}