package ec.com.redepronik.negosys.rrhh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;

public interface ProveedorService {
	@Transactional
	public void actualizar(Persona persona);

	@Transactional
	public Proveedor cargarProveedor(String proveedor);

	@Transactional
	public Long contar();

	@Transactional
	public void eliminar(Persona persona);

	@Transactional
	public void insertar(Persona persona);

	@Transactional(readOnly = true)
	public List<Persona> obtener();

	@Transactional
	public List<Persona> obtener(String nombreCliente);

	@Transactional(readOnly = true)
	public List<Persona> obtenerActivos();

	@Transactional
	public List<String> obtenerListaProveedoresAutoComplete(
			String criterioProveedorBusqueda);

	@Transactional
	public Persona obtenerPorCedula(String cedula);

	@Transactional
	public List<Proveedor> ReporteObtenerPorCiudad(int ciudadId);

	@Transactional
	public Persona obtenerPorPersonaId(Integer personaId);

	@Transactional
	public Persona obtenerPorId(Integer proveedorId);
	
	@Transactional
	public List<Persona> obtenerProveedoresPorFacturasPendientes();

	@Transactional
	public List<Persona> obtenerTodosPorBusqueda(String criterioBusqueda,
			int criterioCiudadBusqueda);

}