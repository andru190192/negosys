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

import ec.com.redepronik.negosys.rrhh.dao.PersonaDao;
import ec.com.redepronik.negosys.rrhh.dao.ProveedorDao;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;
import ec.com.redepronik.negosys.seguridad.service.RolService;

@Service
public class ProveedorServiceImpl implements ProveedorService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private PersonaDao personaDao;

	@Autowired
	private RolService rolService;

	@Autowired
	private ProveedorDao proveedorDao;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(Persona persona) {
		Set<ConstraintViolation<Proveedor>> violationsProveedor = validator
				.validate(persona.getProveedor());
		if (violationsProveedor.size() > 0)
			for (ConstraintViolation<Proveedor> cv : violationsProveedor)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (personaService.actualizar(persona))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"PROVEEDOR ACTUALIZADO", "cerrar", true);
	}

	public Proveedor cargarProveedor(String proveedor) {
		return obtenerProveedorPorCedula(proveedor.split(" - ")[0]);
	}

	public Long contar() {
		return (Long) proveedorDao.contar(Proveedor.class);
	}

	public void eliminar(Persona persona) {
		persona.getProveedor().setActivo(
				persona.getProveedor().getActivo() ? false : true);
		personaDao.actualizar(persona);

		if (persona.getProveedor().getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTIVÓ AL PROVEEDOR: " + persona.getApellido() + " "
							+ persona.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ AL PROVEEDOR: " + persona.getApellido() + " "
							+ persona.getNombre());
	}

	public void insertar(Persona persona) {
		persona.getProveedor().setActivo(true);
		persona.getProveedor().setPersona(persona);
		persona.getProveedor().setFolio(String.valueOf(contar() + 1));
		Set<ConstraintViolation<Proveedor>> violationsProveedor = validator
				.validate(persona.getProveedor());
		if (violationsProveedor.size() > 0)
			for (ConstraintViolation<Proveedor> cv : violationsProveedor)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else {
			boolean retorno = false;
			if (persona.getId() == null)
				retorno = personaService.insertar(persona);
			else
				retorno = personaService.actualizar(persona);
			if (retorno) {
				List<String> roles = new ArrayList<String>();
				roles.add("PROV");
				personaService.insertarRoles(persona, roles);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"PROVEEDOR INSERTADO", "cerrar", true);
			}
		}
	}

	public List<Persona> obtener() {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p "
								+ "inner join p.proveedor pr where p.visible=true order by p.apellido, p.nombre",
						new Object[] {});
		return lista;
	}

	public List<Persona> obtener(String criterioProveedorBusqueda) {
		criterioProveedorBusqueda = criterioProveedorBusqueda.toUpperCase();
		List<Persona> lista = null;
		if (criterioProveedorBusqueda.length() >= 0
				&& criterioProveedorBusqueda.length() < 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		else {
			lista = personaDao
					.obtenerPorHql(
							"select distinct p from Persona p inner join p.proveedor pr "
									+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 or pr.nombreComercial like ?1) and p.activo=true and pr.activo=true",
							new Object[] { "%" + criterioProveedorBusqueda
									+ "%" });
			if (lista.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
		}
		return lista;
	}

	public List<Persona> obtenerActivos() {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.proveedor pr where p.visible=true and p.activo=true order by p.apellido, p.nombre",
						new Object[] {});
		return lista;
	}

	public List<String> obtenerListaProveedoresAutoComplete(
			String criterioProveedorBusqueda) {
		List<String> list = new ArrayList<String>();
		List<Persona> lista = obtener(criterioProveedorBusqueda);
		if (!lista.isEmpty())
			for (Persona p : lista)
				list.add(p.getCedula() + " - " + p.getApellido() + " "
						+ p.getNombre() + " - "
						+ p.getProveedor().getNombreComercial());
		return list;
	}

	public Persona obtenerPorCedula(String cedula) {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.proveedor c where p.visible=true and p.cedula=?1 and p.activo=true and c.activo=true",
						new Object[] { cedula });
		if (!lista.isEmpty())
			return lista.get(0);
		return null;
	}

	public List<Proveedor> ReporteObtenerPorCiudad(int ciudadId) {
		if (ciudadId == 0)
			return proveedorDao.obtenerPorHql(
					"select distinct p from Proveedor p inner join fetch p.persona pe "
							+ "where pe.visible=true "
							+ "order by p.nombreComercial", new Object[] {});
		return proveedorDao
				.obtenerPorHql(
						"select distinct p from Proveedor p inner join fetch p.persona pe "
								+ "inner join pe.ciudad cd where pe.visible=true and cd.id=?1 "
								+ "order by p.nombreComercial",
						new Object[] { ciudadId });
	}

	public Persona obtenerPorPersonaId(Integer personaId) {
		return personaService.obtenerPorPersonaId(personaId);
	}

	public Persona obtenerPorId(Integer proveedorId) {
		return proveedorDao.obtenerPorId(Proveedor.class, proveedorId)
				.getPersona();
	}

	public List<Persona> obtenerProveedoresPorFacturasPendientes() {
		return personaDao.obtenerPorHql("select distinct p from Persona p "
				+ "inner join p.proveedor pr " + "inner join pr.ingresos i "
				+ "where p.visible=true and i.activo=true and i.pagado=false "
				+ "order by p.apellido, p.nombre", new Object[] {});
	}

	public Proveedor obtenerProveedorPorCedula(String cedula) {
		List<Persona> lista = personaDao
				.obtenerPorHql(
						"select p from Persona p inner join p.proveedor pr where p.visible=true and p.cedula=?1 and p.activo=true and pr.activo=true",
						new Object[] { cedula });
		if (!lista.isEmpty())
			return lista.get(0).getProveedor();
		return null;
	}

	public List<Persona> obtenerTodosPorBusqueda(
			String criterioBusquedaProveedor, int criterioBusquedaCiudad) {
		criterioBusquedaProveedor = criterioBusquedaProveedor.trim();
		List<Persona> lista = null;

		if (criterioBusquedaProveedor.compareToIgnoreCase("") == 0
				&& criterioBusquedaCiudad == 0)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (criterioBusquedaProveedor.length() >= 3
					|| criterioBusquedaCiudad != 0) {

				if (criterioBusquedaProveedor.compareToIgnoreCase("") != 0
						&& criterioBusquedaCiudad != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join p.proveedor pro "
											+ "inner join p.ciudad cd "
											+ "where p.visible=true and (cd.id=?2 "
											+ "and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 or pro.nombreComercial like ?1)) "
											+ "order by p.apellido, p.nombre",
									new Object[] {
											"%" + criterioBusquedaProveedor
													+ "%",
											criterioBusquedaCiudad });
				else if (criterioBusquedaProveedor.compareToIgnoreCase("") != 0)
					lista = personaDao
							.obtenerPorHql(
									"select distinct p from Persona p "
											+ "inner join p.proveedor pro "
											+ "where p.visible=true and (p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 or pro.nombreComercial like ?1) "
											+ "order by p.apellido, p.nombre",
									new Object[] { "%"
											+ criterioBusquedaProveedor + "%" });
				else if (criterioBusquedaCiudad != 0)
					lista = personaDao.obtenerPorHql(
							"select distinct p from Persona p "
									+ "inner join p.proveedor pro "
									+ "inner join p.ciudad cd "
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