package ec.com.redepronik.negosys.rrhh.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.rrhh.dao.ClienteDao;
import ec.com.redepronik.negosys.rrhh.dao.EmpleadoCargoDao;
import ec.com.redepronik.negosys.rrhh.dao.PersonaDao;
import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.seguridad.entity.RolUsuario;
import ec.com.redepronik.negosys.seguridad.service.RolService;

@Service
public class ClienteServiceImpl implements ClienteService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private PersonaDao personaDao;

	@Autowired
	private EmpleadoCargoDao empleadoCargoDao;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private RolService rolService;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(Persona persona) {
		Set<ConstraintViolation<Cliente>> violationsCliente = validator
				.validate(persona.getCliente());
		if (violationsCliente.size() > 0)
			for (ConstraintViolation<Cliente> cv : violationsCliente)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (personaService.actualizar(persona))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CLIENTE ACTUALIZADO",
					"cerrar", true);
	}

	public Cliente cargarCliente(String cliente) {
		return obtenerClientePorCedula(cliente.split(" - ")[1]);
	}

	public Long contar() {
		return (Long) clienteDao.contar(Cliente.class);
	}

	public void eliminar(Persona persona) {
		persona.getCliente().setActivo(
				persona.getCliente().getActivo() ? false : true);

		for (RolUsuario ru : persona.getRolUsuarios())
			if (ru.getRol().getNombre().compareTo("CLIE") == 0) {
				ru.setActivo(persona.getCliente().getActivo());
				break;
			}

		personaDao.actualizar(persona);
		if (persona.getCliente().getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTIVÓ AL CLIENTE: " + persona.getApellido() + " "
							+ persona.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ AL CLIENTE: " + persona.getApellido() + " "
							+ persona.getNombre());
	}

	public void eliminarAguazero(Persona persona) {
		persona.getCliente().setActivo(
				persona.getCliente().getActivo() ? false : true);

		EmpleadoCargo empleadoCargo = empleadoService
				.obtenerCualquierEmpleadoCargoPorCedulaAndCargo(
						persona.getCedula(), 5);

		empleadoCargo.setActual(empleadoCargo.getActual() ? false : true);
		empleadoCargoDao.actualizar(empleadoCargo);

		for (RolUsuario ru : persona.getRolUsuarios())
			if (ru.getRol().getNombre().compareTo("CLIE") == 0) {
				ru.setActivo(persona.getCliente().getActivo());
				break;
			}

		personaDao.actualizar(persona);
		if (persona.getCliente().getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTIVÓ AL CLIENTE: " + persona.getApellido() + " "
							+ persona.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ AL CLIENTE: " + persona.getApellido() + " "
							+ persona.getNombre());
	}

	public Persona insertar(Persona persona) {
		persona.getCliente().setActivo(true);
		persona.getCliente().setPersona(persona);
		persona.getCliente().setFolio(String.valueOf(contar() + 1));
		Set<ConstraintViolation<Cliente>> violationsCliente = validator
				.validate(persona.getCliente());
		if (violationsCliente.size() > 0)
			for (ConstraintViolation<Cliente> cv : violationsCliente)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else {
			boolean retorno = false;
			if (persona.getId() == null)
				retorno = personaService.insertar(persona);
			else
				retorno = personaService.actualizar(persona);

			if (retorno) {
				List<String> roles = new ArrayList<String>();
				roles.add("CLIE");
				personaService.insertarRoles(persona, roles);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"CLIENTE INSERTADO", "cerrar", true);
			}
		}
		return persona;
	}

	public List<Persona> obtener() {
		List<Persona> lista = personaDao.obtenerPorHql(
				"select p from Persona p " + "inner join p.cliente c "
						+ "where p.visible=true order by p.apellido, p.nombre",
				new Object[] {});
		return lista;
	}

	public List<Persona> obtener(String criterioCliente, Integer clienteId) {
		List<Persona> lista = new ArrayList<Persona>();

		if ((criterioCliente != null && criterioCliente.length() < 3)
				&& (clienteId == null || clienteId == 0))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BÚSQUEDA VALIDO");
		else if (criterioCliente != null && criterioCliente.length() > 3)
			lista = personaDao
					.obtenerPorHql(
							"select distinct p from Persona p inner join p.cliente c "
									+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1) and p.activo=true and c.activo=true",
							new Object[] { "%" + criterioCliente.toUpperCase()
									+ "%" });
		else if (clienteId != 0)
			lista = personaDao
					.obtenerPorHql(
							"select distinct p from Persona p inner join p.cliente c "
									+ "where p.visible=true and p.activo=true and c.activo=true and c.id=?1",
							new Object[] { clienteId });

		if (lista.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"NO SE ENCONTRO NINGUNA COINCIDENCIA");

		return lista;
	}

	public List<Persona> obtenerActivos() {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.cliente c where where p.visible=true and p.activo=true order by p.apellido, p.nombre",
						new Object[] {});
		return lista;
	}

	public Cliente obtenerClientePorCedula(String cedula) {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.cliente c where p.visible=true and p.cedula=?1 and p.activo=true and c.activo=true",
						new Object[] { cedula });
		if (!lista.isEmpty())
			return lista.get(0).getCliente();
		return null;
	}

	public List<Persona> obtenerClientesPorFacturasPendientes(int ciudadId,
			int vendedorId) {
		if (vendedorId == 0)
			return personaDao
					.obtenerPorHql(
							"select distinct p from Persona p "
									+ "inner join p.ciudad cd inner join p.cliente c "
									+ "inner join c.facturas f "
									+ "left join f.credito cr "
									+ "where p.visible=true and cd.id=?1 and f.activo=true and f.fechaCierre is null "
									+ "and cr.id is null "
									+ "order by p.apellido, p.nombre",
							new Object[] { ciudadId });
		else
			return personaDao
					.obtenerPorHql(
							"select distinct p from Persona p "
									+ "inner join p.ciudad cd "
									+ "inner join p.cliente c inner join c.facturas f "
									+ "inner join f.vendedor v "
									+ "left join f.credito cr "
									+ "where p.visible=true and cd.id=?1 and f.activo=true and f.fechaCierre is null "
									+ "and v.id=?2 and cr.id is null "
									+ "order by p.apellido, p.nombre",
							new Object[] { ciudadId, vendedorId });
	}

	public List<Persona> obtenerGarante(String nombreCliente) {
		return personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.cliente c where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1) and p.activo=true and c.activo=true",
						new Object[] { "%" + nombreCliente.toUpperCase() + "%" });
	}

	public List<Persona> obtenerGarantesPorCredito(int creditoId) {
		return personaDao
				.obtenerPorHql(
						"select distinct p from Persona p inner join fetch p.cliente c "
								+ "inner join fetch c.garantes g "
								+ "inner join g.credito cr where p.visible=true and cr.id=?1",
						new Object[] { creditoId });
	}

	public List<String> obtenerListaClientesAutoComplete(
			String criterioClienteBusqueda) {
		List<String> list = new ArrayList<String>();
		List<Persona> lista = obtener(criterioClienteBusqueda, 0);
		if (!lista.isEmpty())
			for (Persona p : lista)
				list.add(p.getCiudad().getNombre() + " - " + p.getCedula()
						+ " - " + p.getApellido() + " " + p.getNombre());
		return list;
	}

	public Persona obtenerPorCedula(String cedula) {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select distinct p from Persona p inner join p.cliente c "
								+ "where p.visible=true and p.cedula=?1 and p.activo=true",
						new Object[] { cedula });
		if (!lista.isEmpty())
			return lista.get(0);
		return null;
	}

	public List<Cliente> ReporteObtenerPorCiudad(int ciudadId) {
		return clienteDao
				.obtenerPorHql(
						"select distinct c from Cliente c inner join fetch c.persona p "
								+ "inner join p.ciudad cd where p.visible=true and cd.id=?1 "
								+ "order by c.nombreComercial",
						new Object[] { ciudadId });
	}

	public Persona obtenerPorClienteId(Integer clienteId) {
		List<Persona> lista = personaDao.obtenerPorHql(
				"select distinct p from Persona p inner join p.cliente c "
						+ "where p.visible=true and c.id=?1",
				new Object[] { clienteId });
		if (!lista.isEmpty())
			return lista.get(0);
		return null;
	}

	public Persona obtenerPorPersonaId(Integer personaId) {
		return personaService.obtenerPorPersonaId(personaId);
	}

	public List<Cliente> obtenerPorProveedorPorCiudad(Integer proveedorid,
			Integer ciudadid, Date fechaInicio, Date fechaFin) {
		List<Cliente> lista = clienteDao.obtenerPorHql(
				"select distinct c from Cliente c inner join c.facturas f "
						+ "inner join f.detalleFactura df "
						+ "inner join df.producto p "
						+ "inner join p.detalleIngresos di "
						+ "inner join di.ingreso i "
						+ "inner join i.proveedor pr "
						+ "inner join c.persona prs "
						+ "inner join prs.ciudad cd where prs.visible=true "
						+ "and pr.id=?1 and cd.id=?2 "
						+ "and (f.fechaInicio>=?3 and f.fechaInicio<=?4)",
				new Object[] { proveedorid, ciudadid, fechaInicio,
						dateCompleto(fechaFin) });
		return lista;
	}

	public List<Persona> obtenerTodosPorBusqueda(
			String criterioBusquedaCliente, int criterioBusquedaCiudad) {
		criterioBusquedaCliente = criterioBusquedaCliente.trim();
		List<Persona> lista = null;

		if (criterioBusquedaCliente.compareToIgnoreCase("") == 0
				&& criterioBusquedaCiudad == 0)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (criterioBusquedaCliente.length() >= 3
					|| criterioBusquedaCiudad != 0) {
				if (criterioBusquedaCliente.compareToIgnoreCase("") != 0
						&& criterioBusquedaCiudad != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join fetch p.cliente c "
											// +
											// "left join fetch c.detalleClientes "
											+ "left join fetch p.rolUsuarios "
											+ "inner join p.ciudad cd "
											+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 or c.nombreComercial like ?1) "
											+ "and cd.id=?2 "
											+ "order by p.apellido, p.nombre",
									new Object[] {
											"%" + criterioBusquedaCliente + "%",
											criterioBusquedaCiudad });
				else if (criterioBusquedaCliente.compareToIgnoreCase("") != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join fetch p.cliente c "
											// +
											// "left join fetch c.detalleClientes "
											+ "left join fetch p.rolUsuarios "
											+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 or c.nombreComercial like ?1) "
											+ "order by p.apellido, p.nombre",
									new Object[] { "%"
											+ criterioBusquedaCliente + "%" });
				else if (criterioBusquedaCiudad != 0)
					lista = personaDao.obtenerPorHql(
							"select distinct p from Persona p "
									+ "inner join fetch p.cliente c "
									// + "left join fetch c.detalleClientes "
									+ "inner join p.ciudad cd "
									+ "left join fetch p.rolUsuarios "
									+ "where p.visible=true and cd.id=?1 "
									+ "order by p.apellido, p.nombre",
							new Object[] { criterioBusquedaCiudad });
				if (lista.isEmpty())
					presentaMensaje(FacesMessage.SEVERITY_WARN,
							"NO SE ENCONTRO NINGUNA COINCIDENCIA");
			} else
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"INGRESE MINIMO 3 CARACTERES");
		}
		return lista;
	}

}