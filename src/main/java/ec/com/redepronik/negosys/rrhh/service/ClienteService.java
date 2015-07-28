package ec.com.redepronik.negosys.rrhh.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

public interface ClienteService {

	@Transactional
	public void actualizar(Persona persona);

	@Transactional
	public Cliente cargarCliente(String cliente);

	@Transactional
	public Long contar();

	@Transactional
	public void eliminar(Persona persona);

	@Transactional
	public void eliminarAguazero(Persona persona);

	@Transactional
	public Persona insertar(Persona persona);

	@Transactional(readOnly = true)
	public List<Persona> obtener();

	@Transactional
	public List<Persona> obtener(String criterioClienteBusqueda,
			Integer clienteId);

	@Transactional(readOnly = true)
	public List<Persona> obtenerActivos();

	@Transactional
	public Cliente obtenerClientePorCedula(String cedula);

	@Transactional(readOnly = true)
	public List<Persona> obtenerClientesPorFacturasPendientes(int ciudadId,
			int vendedorId);

	@Transactional
	public List<Persona> obtenerGarante(String nombreCliente);

	@Transactional(readOnly = true)
	public List<Persona> obtenerGarantesPorCredito(int creditoId);

	@Transactional
	public List<String> obtenerListaClientesAutoComplete(
			String criterioClienteBusqueda);

	@Transactional
	public Persona obtenerPorCedula(String cedula);

	@Transactional
	public List<Cliente> ReporteObtenerPorCiudad(int ciudadId);

	@Transactional
	public Persona obtenerPorClienteId(Integer clienteId);

	@Transactional
	public Persona obtenerPorPersonaId(Integer personaId);

	@Transactional
	public List<Cliente> obtenerPorProveedorPorCiudad(Integer proveedorid,
			Integer ciudadid, Date fechaInicio, Date fechaFin);

	@Transactional
	public List<Persona> obtenerTodosPorBusqueda(
			String criterioBusquedaCliente, int criterioBusquedaCiudad);
}
