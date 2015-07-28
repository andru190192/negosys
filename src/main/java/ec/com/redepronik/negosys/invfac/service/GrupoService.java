package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Grupo;

public interface GrupoService {
	@Transactional
	public List<Grupo> actualizar(Grupo grupo);

	@Transactional
	public void eliminar(Grupo grupo);

	@Transactional
	public List<Grupo> insertar(Grupo grupo);

	@Transactional(readOnly = true)
	public List<Grupo> obtener();

	@Transactional(readOnly = true)
	public Grupo obtenerPorGrupoId(Integer grupoId);

	@Transactional(readOnly = true)
	public List<Grupo> reporteObtenerPorProductos();
}