package ec.com.redepronik.negosys.rrhh.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.rrhh.dao.EmpleadoCargoDao;
import ec.com.redepronik.negosys.rrhh.dao.EmpleadoDao;
import ec.com.redepronik.negosys.rrhh.dao.PersonaCedulaNombreDao;
import ec.com.redepronik.negosys.rrhh.dao.PersonaDao;
import ec.com.redepronik.negosys.rrhh.entity.Empleado;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.seguridad.entity.Rol;
import ec.com.redepronik.negosys.seguridad.entity.RolUsuario;
import ec.com.redepronik.negosys.seguridad.service.RolService;

@Service
public class EmpleadoServiceImpl implements EmpleadoService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private PersonaDao personaDao;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private RolService rolService;

	@Autowired
	private PersonaCedulaNombreDao personaCedulaNombreDao;

	@Autowired
	private EmpleadoDao empleadoDao;

	@Autowired
	private EmpleadoCargoDao empleadoCargoDao;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(Persona persona) {
		Set<ConstraintViolation<Empleado>> violationsPersona = validator
				.validate(persona.getEmpleado());

		if (violationsPersona.size() > 0)
			for (ConstraintViolation<Empleado> cv : violationsPersona)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (personaService.actualizar(persona))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "EMPLEADO ACTUALIZADO",
					"cerrar", true);
	}

	public boolean autorizacion(String login, String pass) {
		Persona persona = personaService.obtenerActivoPorCedula(login);
		boolean bn = false;
		if (persona == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "EL USUARIO NO EXISTE");
		else {
			if (personaService.generarClave(pass).compareTo(
					persona.getPassword()) == 0) {
				for (RolUsuario rolUsuario : persona.getRolUsuarios())
					if (rolUsuario.getRol().getNombre().compareTo("ADMI") == 0) {
						return true;
					}
				if (!bn)
					presentaMensaje(FacesMessage.SEVERITY_ERROR,
							"EL USUARIO NO ES ADMINISTRADOR");
			} else
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"LA CLAVE NO ES VALIDA");
		}
		return false;
	}

	public List<EmpleadoCargo> cargarCargos(
			List<EmpleadoCargo> listaEmpleadoCargos, Persona persona) {
		listaEmpleadoCargos = persona.getEmpleado().getEmpleadoCargos();
		return listaEmpleadoCargos;
	}

	public Long contar() {
		return (Long) empleadoDao.contar(Empleado.class);
	}

	public void eliminar(Persona persona) {
		persona.getEmpleado().setActivo(
				persona.getEmpleado().getActivo() ? false : true);
		personaDao.actualizar(persona);

		if (persona.getEmpleado().getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTIVÓ EL EMPLEADO: " + persona.getApellido() + " "
							+ persona.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ EL EMPLEADO: " + persona.getApellido() + " "
							+ persona.getNombre());
	}

	public void insertar(Persona persona) {
		persona.getEmpleado().setActivo(true);
		persona.getEmpleado().setFolio(String.valueOf(contar() + 1));
		persona.getEmpleado().setPersona(persona);

		Set<ConstraintViolation<Empleado>> violationsEmpleado = validator
				.validate(persona.getEmpleado());

		if (violationsEmpleado.size() > 0)
			for (ConstraintViolation<Empleado> cv : violationsEmpleado)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());

		else {
			boolean retorno = false;

			if (persona.getId() == null)
				retorno = personaService.insertar(persona);
			else
				retorno = personaService.actualizar(persona);

			if (retorno)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"EMPLEADO INSERTADO", "cerrar", true);
		}
	}

	public String insertarCargos(Persona persona) {
		actualizar(persona);
		persona = personaService.obtenerPorCedula(persona.getCedula());
		if (persona.getRolUsuarios() == null)
			persona.setRolUsuarios(new ArrayList<RolUsuario>());
		for (EmpleadoCargo ec : persona.getEmpleado().getEmpleadoCargos()) {
			Rol rol = null;
			if (ec.getCargo().getNombre().compareTo("ADMINISTRADOR") == 0)
				rol = rolService.obtenerPorNombre("ADMI");
			else if (ec.getCargo().getNombre().compareTo("SUPERVISOR/CAJA") == 0)
				rol = rolService.obtenerPorNombre("SUCA");
			else if (ec.getCargo().getNombre().compareTo("BODEGUERO") == 0)
				rol = rolService.obtenerPorNombre("BODE");
			else if (ec.getCargo().getNombre().compareTo("CAJERO") == 0)
				rol = rolService.obtenerPorNombre("CAJA");
			else if (ec.getCargo().getNombre().compareTo("VENDEDOR") == 0)
				rol = rolService.obtenerPorNombre("VEND");
			else if (ec.getCargo().getNombre().compareTo("CHOFER") == 0)
				rol = rolService.obtenerPorNombre("CHOF");
			else if (ec.getCargo().getNombre()
					.compareTo("ADMINISTRADOR/SEGURIDAD") == 0)
				rol = rolService.obtenerPorNombre("SEGU");
			else if (ec.getCargo().getNombre().compareTo("REPORTEADOR") == 0)
				rol = rolService.obtenerPorNombre("REPO");

			Boolean bn = false;

			for (RolUsuario ru : persona.getRolUsuarios()) {
				if (rol.getId() == ru.getRol().getId()) {
					bn = true;
				}
			}

			if (!bn) {
				RolUsuario rolUsuario = new RolUsuario();
				rolUsuario.setRol(rol);
				rolUsuario.setActivo(true);
				persona.addRolUsuario(rolUsuario);
			}
		}
		personaService.actualizar(persona);
		return "SAVE";
	}

	public List<Persona> obtener() {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p "
								+ "left join fetch p.empleadoCargos "
								+ "inner join p.empleado e where p.visible=true order by p.apellido, p.nombre",
						new Object[] {});
		return lista;
	}

	public List<Persona> obtener(String nombreEmpleado) {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.empleado c where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1) and p.activo=true and c.activo=true",
						new Object[] { "%" + nombreEmpleado + "%" });
		return lista;
	}

	public List<Persona> obtenerActivos() {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.empleado e where p.visible=true and p.activo=true order by p.apellido, p.nombre",
						new Object[] {});
		return lista;
	}

	public EmpleadoCargo obtenerCualquierEmpleadoCargoPorCedulaAndCargo(
			String cedula, Integer cargoId) {
		return empleadoCargoDao
				.obtenerPorHql(
						"select ec from EmpleadoCargo ec inner join ec.empleado e "
								+ "inner join e.persona p "
								+ "inner join ec.cargo c "
								+ "where p.visible=true and p.activo=true and p.cedula=?1 and c.id=?2",
						new Object[] { cedula, cargoId }).get(0);
	}

	public EmpleadoCargo obtenerEmpleadoCargoPorCedulaAndCargo(String cedula,
			Integer cargoId) {
		return empleadoCargoDao
				.obtenerPorHql(
						"select ec from EmpleadoCargo ec inner join ec.empleado e "
								+ "inner join e.persona p "
								+ "inner join ec.cargo c "
								+ "where p.activo=true and p.cedula=?1 and c.id=?2 and ec.actual=true",
						new Object[] { cedula, cargoId }).get(0);
	}

	public List<PersonaCedulaNombre> obtenerPorCargo(int cargo) {
		return personaCedulaNombreDao
				.obtenerPorHql(
						"select new PersonaCedulaNombre(ec.id, p.cedula, p.nombre, p.apellido) from Persona p "
								+ "inner join p.empleado e  "
								+ "inner join e.empleadoCargos ec "
								+ "inner join ec.cargo c "
								+ "where p.visible=true and p.activo=true and e.activo=true and ec.actual=true and c.activo=true and c.id=?1 "
								+ "order by p.apellido, p.nombre",
						new Object[] { cargo });
	}

	public Persona obtenerPorCedula(String cedula) {
		return personaService.obtenerActivoPorCedula(cedula);
	}

	public EmpleadoCargo obtenerPorEmpleadoCargoId(int empladoCargoId) {
		return empleadoCargoDao.obtenerPorId(EmpleadoCargo.class,
				empladoCargoId);
	}

	public List<Persona> obtenerTodosPorBusqueda(int criterioBusquedaCargo,
			String criterioBusquedaEmpleado, int criterioBusquedaCiudad) {
		criterioBusquedaEmpleado = criterioBusquedaEmpleado.trim();
		List<Persona> lista = null;

		if (criterioBusquedaEmpleado.compareToIgnoreCase("") == 0
				&& criterioBusquedaCiudad == 0 && criterioBusquedaCargo == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (criterioBusquedaCargo != 0
					|| criterioBusquedaEmpleado.length() >= 3
					|| criterioBusquedaCiudad != 0) {
				if (criterioBusquedaEmpleado.compareToIgnoreCase("") != 0
						&& criterioBusquedaCiudad != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join fetch p.empleado e "
											+ "left join fetch e.empleadoCargos ec "
											+ "left join ec.cargo c"
											+ "inner join p.ciudad cd "
											+ "where p.visible=true and (cd.id=?2 "
											+ "and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1)) "
											+ "order by p.apellido, p.nombre",
									new Object[] {
											"%" + criterioBusquedaEmpleado
													+ "%",
											criterioBusquedaCiudad });
				else if (criterioBusquedaCargo != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join fetch p.empleado e "
											+ "left join fetch e.empleadoCargos ec "
											+ "left join ec.cargo c "
											+ "where p.visible=true and c.id=?1 order by p.apellido, p.nombre",
									new Object[] { criterioBusquedaCargo });
				else if (criterioBusquedaEmpleado.compareToIgnoreCase("") != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join fetch p.empleado e "
											+ "left join fetch e.empleadoCargos ec "
											+ "left join ec.cargo "
											+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1) "
											+ "order by p.apellido, p.nombre",
									new Object[] { "%"
											+ criterioBusquedaEmpleado + "%" });
				else if (criterioBusquedaCiudad != 0)
					lista = personaDao.obtenerPorHql(
							"select distinct p from Persona p "
									+ "inner join fetch p.empleado e "
									+ "left join fetch e.empleadoCargos ec "
									+ "left join ec.cargo "
									+ "inner join p.ciudad cd "
									+ "where p.visible=true and cd.id=?1 "
									+ "order by p.apellido, p.nombre",
							new Object[] { criterioBusquedaCiudad });
				if (lista.isEmpty())
					presentaMensaje(FacesMessage.SEVERITY_INFO,
							"NO SE ENCONTRO NINGUNA COINCIDENCIA");

			} else
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE MINIMO 3 CARACTERES");
		}
		return lista;
	}
}