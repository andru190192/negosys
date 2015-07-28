package ec.com.redepronik.negosys.rrhh.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

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
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.rrhh.dao.PersonaDao;
import ec.com.redepronik.negosys.rrhh.entity.DetalleCliente;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.seguridad.entity.Rol;
import ec.com.redepronik.negosys.seguridad.entity.RolUsuario;
import ec.com.redepronik.negosys.seguridad.service.RolService;

@Service
public class PersonaServiceImpl implements PersonaService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaDao personaDao;

	@Autowired
	private RolService rolService;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public boolean actualizar(Persona persona) {
		boolean retorno = false;

		Set<ConstraintViolation<Persona>> violations = validator
				.validate(persona);
		if (violations.size() > 0)
			for (ConstraintViolation<Persona> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (persona.getCiudad().getId() == null
				|| persona.getCiudad().getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO, "ESCOJA UNA CIUDAD");
		// } else if (persona.getEmailPersonas().size() == 0) {
		// presentaMensaje(FacesMessage.SEVERITY_INFO,
		// "MENSAJE DEL SISTEMA", "DEBE INGRESAR AL MENOS UN EMAIL"));
		// } else if (persona.getTelefonoPersonas().size() == 0) {
		// presentaMensaje(FacesMessage.SEVERITY_INFO,
		// "MENSAJE DEL SISTEMA",
		// "DEBE INGRESAR AL MENOS UN TELÉFONO"));
		else if (personaDao.comprobarIndices(Persona.class, "cedula",
				persona.getCedula(), String.valueOf(persona.getId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA CÉDULA YA EXISTE",
					"cerrar", false);
		else {
			persona.setPassword(generarClave(persona.getCedula()));
			personaDao.actualizar(persona);
			retorno = true;
		}
		return retorno;
	}

	public void cambiarClave(String claveActual, String clave1, String clave2) {
		Persona persona = obtenerActivoPorCedula(SecurityContextHolder
				.getContext().getAuthentication().getName());
		if (claveActual.length() == 0 || clave1.length() == 0
				|| clave2.length() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESE TODOS LOS DATOS REQUERIDOS");
		} else if (clave1.length() < 8 || clave2.length() < 8) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA NUEVA CONTRASEÑA DEBE TENER UNA LONGITUD MINIMA DE 8 CARACTERES");
		} else if (!compararClave(persona.getPassword(),
				generarClave(claveActual))) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA CONTRASEÑA ACTUAL ES INCORRECTA");
		} else if (!compararClave(clave1, clave2)) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LAS CONTRASEÑAS NUEVAS NO COINCIDEN");
		} else if (compararClave(clave1, persona.getCedula())) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA CONTRASEÑA NO PUEDE SER IGUAL AL USUARIO");
		} else {
			persona.setPassword(generarClave(clave1));
			personaDao.actualizar(persona);
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"CAMBIO DE PASSWORD EXITOSO");
			redireccionar("../../../negosys/templates/home.jsf");
		}
	}

	public boolean compararClave(String clave1, String clave2) {
		return clave1.compareTo(clave2) == 0 ? true : false;
	}

	public boolean comprobarRol(String cedula, String rol) {
		List<Persona> usuario = null;
		usuario = personaDao.obtenerPorHql("select distinct p from Persona p "
				+ "left join fetch p.rolUsuarios ru "
				+ "left join fetch ru.rol r "
				+ "where p.cedula=?1 and r.nombre=?2", new Object[] { cedula,
				rol });

		if (usuario.size() == 0)
			return false;
		else
			return true;
	}

	public Long contar() {
		return (Long) personaDao.contar(Persona.class);
	}

	public void eliminar(Persona persona) {
		persona.setActivo(persona.getActivo() ? false : true);
		personaDao.actualizar(persona);

		if (persona.getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTIVO A LA PERSONA: " + persona.getApellido() + " "
							+ persona.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVO A LA PERSONA: " + persona.getApellido() + " "
							+ persona.getNombre());
	}

	public void eliminarAdiconal(Persona persona, DetalleCliente detallecliente) {
		if (persona.getCliente().getDetalleClientes().size() > 1) {
			String campo = detallecliente.getCampo();
			persona.removeAdicional(detallecliente);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "SE ELIMINO EL CAMPO "
					+ campo);
		} else {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"DEBE TENER AL MENOS UN CAMPO ADICIONAL");
		}
	}

	public String generarClave(String clave) {
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
		return shaPasswordEncoder.encodePassword(clave, null);
	}

	public boolean insertar(Persona persona) {
		boolean retorno = false;

		Set<ConstraintViolation<Persona>> violationsPersona = validator
				.validate(persona);
		if (violationsPersona.size() > 0)
			for (ConstraintViolation<Persona> cv : violationsPersona)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());

		else if (persona.getCiudad().getId() == null
				|| persona.getCiudad().getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO, "ESCOJA UNA CIUDAD");
		// } else if (persona.getEmailPersonas().size() == 0) {
		// presentaMensaje(FacesMessage.SEVERITY_INFO,
		// "MENSAJE DEL SISTEMA", "DEBE INGRESAR AL MENOS UN EMAIL"));
		// } else if (persona.getTelefonoPersonas().size() == 0) {
		// presentaMensaje(FacesMessage.SEVERITY_INFO,
		// "MENSAJE DEL SISTEMA",
		// "DEBE INGRESAR AL MENOS UN TELÉFONO"));
		else if (personaDao.comprobarIndices(Persona.class, "cedula",
				persona.getCedula(), String.valueOf(persona.getId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA CÉDULA YA EXISTE",
					"cerrar", false);
		else {
			persona.setActivo(true);
			persona.setVisible(true);
			persona.setPassword(generarClave(persona.getCedula()));
			personaDao.insertar(persona);
			retorno = true;
		}
		return retorno;
	}

	public void insertarFilaAdicional(Persona persona) {
		if (persona.getCedula().length() == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESE PRIMERO UNA CÉDULA VÁLIDA");
		else
			persona.getCliente().getDetalleClientes().add(new DetalleCliente());
	}

	public String insertarRoles(Persona persona, List<String> roles) {
		// Usuario usuario = persona.getUsuario();
		// if (usuario == null) {
		// usuario = new Usuario();
		// usuario.setPersona(persona);
		// usuario.setLogin(persona.getCedula());
		// usuario.setPassword(generarClave(persona.getCedula()));
		// usuario.setEnabled(true);
		//
		// usuario.setRolusuarios(new ArrayList<Rolusuario>());
		// for (String r : roles) {
		// Rolusuario rolUsuario = new Rolusuario();
		// Rol rol = rolService.obtenerPorNombre(r);
		// rolUsuario.setRol(rol);
		// rolUsuario.setActivo(true);
		// usuario.addRolusuario(rolUsuario);
		// }
		// usuarioDao.insertar(usuario);
		// } else {
		if (persona.getRolUsuarios() == null)
			persona.setRolUsuarios(new ArrayList<RolUsuario>());

		for (String r : roles) {
			RolUsuario rolUsuario = new RolUsuario();
			Rol rol = rolService.obtenerPorNombre(r);
			rolUsuario.setRol(rol);
			rolUsuario.setActivo(true);
			persona.addRolUsuario(rolUsuario);
		}
		personaDao.actualizar(persona);
		// }
		return "SAVE";
	}

	public List<Persona> obtener(Boolean activo) {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p where p.visible=true and order by p.apellido, p.nombre",
						new Object[] {});
		return lista;
	}

	public Persona obtenerActivoPorCedula(String cedula) {
		List<Persona> persona = personaDao.obtenerPorHql(
				"select p from Persona p where p.cedula=?1 and p.activo=true",
				new Object[] { cedula });
		if (persona != null && persona.size() == 1)
			return persona.get(0);
		return null;
	}

	public PersonaCedulaNombre obtenerPersonaCedulaNombre(String cedula) {
		// return (PersonaCedulaNombre) personaDao
		// .obtenerPorSql1(
		// "select p.personaid as id, p.cedula as cedula, "
		// + "p.apellido||' '||p.nombre as nombres "
		// + "from rrhh.persona p where p.visible=true and p.cedula='"
		// + cedula + "'", PersonaCedulaNombre.class).get(
		// 0);
		return (PersonaCedulaNombre) personaDao
				.obtenerPorHql(
						"select p.personaid as id, p.cedula as cedula, "
								+ "p.apellido||' '||p.nombre as nombres "
								+ "from rrhh.persona p where p.visible=true and p.cedula='"
								+ cedula + "'", new Object[] {});
	}

	public Persona obtenerPorCedula(String cedula) {
		List<Persona> persona = personaDao
				.obtenerPorHql(
						"select p from Persona p "
								+ "where p.visible=true and p.cedula=?1 and p.activo=true",
						new Object[] { cedula });
		if (persona != null)
			if (persona.size() != 0)
				return persona.get(0);

		return null;
	}

	public Persona obtenerPorPersonaId(Integer personaId) {
		Persona persona = personaDao.obtenerPorHql(
				"select p from Persona p "
						+ "where p.visible=true and p.id=?1 and p.activo=true",
				new Object[] { personaId }).get(0);
		return persona;
	}

	public List<Persona> obtenerTodosPorBusqueda(
			String criterioBusquedaPersona, int criterioBusquedaCiudad) {
		List<Persona> lista = null;
		criterioBusquedaPersona = criterioBusquedaPersona.toUpperCase();

		if (criterioBusquedaPersona.compareToIgnoreCase("") == 0
				&& criterioBusquedaCiudad == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (criterioBusquedaPersona.length() >= 3
					|| criterioBusquedaCiudad != 0) {
				if (criterioBusquedaPersona.compareToIgnoreCase("") != 0
						&& criterioBusquedaCiudad != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join p.ciudad cd "
											+ "where p.visible=true and (cd.ciudadid=?2 "
											+ "and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 )) "
											+ "order by p.apellido, p.nombre",
									new Object[] {
											"%" + criterioBusquedaPersona + "%",
											criterioBusquedaCiudad });
				else if (criterioBusquedaPersona.compareToIgnoreCase("") != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 ) "
											+ "order by p.apellido, p.nombre",
									new Object[] { "%"
											+ criterioBusquedaPersona + "%" });
				else if (criterioBusquedaCiudad != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join p.ciudad cd "
											+ "where p.visible=true and cd.ciudadid=?1 "
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

	public List<String> obtenerListaPersonaAutoComplete(
			String criterioPersonaBusqueda) {
		List<String> list = new ArrayList<String>();
		List<Persona> lista = obtenerPersonas(criterioPersonaBusqueda);
		if (!lista.isEmpty())
			for (Persona p : lista)
				list.add(p.getCedula() + " - " + p.getApellido() + " "
						+ p.getNombre());
		return list;
	}

	public List<Persona> obtenerPersonas(String criterioPersonaBusqueda) {
		criterioPersonaBusqueda = criterioPersonaBusqueda.toUpperCase();
		List<Persona> lista = null;
		if (criterioPersonaBusqueda.length() >= 0
				&& criterioPersonaBusqueda.length() < 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		else {
			lista = personaDao
					.obtenerPorHql(
							"select distinct p from Persona p "
									+ "where p.visible=true and "
									+ "(p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 ) "
									+ "and p.activo=true", new Object[] { "%"
									+ criterioPersonaBusqueda + "%" });
			if (lista.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
		}
		return lista;
	}

	public Persona cargarPersona(String persona) {
		return obtenerPorCedula(persona.split(" - ")[0]);
	}

}
