package ec.com.redepronik.negosys.rrhh.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;

@Controller
@Scope("session")
public class ProveedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private CiudadService ciudadService;

	private Persona persona;

	private List<Ciudad> listaCiudades;

	private List<Ciudad> listaCiudadesBusqueda;
	private List<Persona> listaPersonasProveedores;
	private String criterioBusquedaProveedor;
	private Integer criterioBusquedaCiudad;

	public ProveedorBean() {

	}

	public void actualizar(ActionEvent actionEvent) {
		proveedorService.actualizar(persona);
		listaPersonasProveedores = new ArrayList<Persona>();
	}

	public void cargarCiudades() {
		listaCiudades = new ArrayList<>();
		listaCiudades = ciudadService.obtenerPorProvincia(persona.getCiudad()
				.getProvincia());
	}

	public void cargarEditar() {
		cargarCiudades();
	}

	public void cargarInsertar() {
		limpiarObjetos();
	}

	public void comprobarPersona() {
		String cedula = persona.getCedula().trim();
		persona = personaService.obtenerPorCedula(cedula);
		if (persona != null && persona.getProveedor() != null) {
			limpiarObjetos();
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL PROVEEDOR YA EXISTE");
		} else if (persona != null) {
			persona.setProveedor(new Proveedor());
			persona.getProveedor().setFolio(
					String.valueOf(proveedorService.contar() + 1));
			persona.getProveedor().setNombreComercial(
					persona.getApellido().concat(" ")
							.concat(persona.getNombre()));
			cargarCiudades();
		} else {
			limpiarObjetos();
			persona.setCedula(cedula);
		}
	}

	public void eliminar(ActionEvent actionEvent) {
		proveedorService.eliminar(persona);
	}

	public Integer getCriterioBusquedaCiudad() {
		return criterioBusquedaCiudad;
	}

	public String getCriterioBusquedaProveedor() {
		return criterioBusquedaProveedor;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public List<Ciudad> getListaCiudadesBusqueda() {
		return listaCiudadesBusqueda;
	}

	public List<Persona> getListaPersonasProveedores() {
		return listaPersonasProveedores;
	}

	public Provincia[] getListaProvincias() {
		return Provincia.values();
	}

	public Persona getPersona() {
		return persona;
	}

	@PostConstruct
	public void init() {
		listaCiudadesBusqueda = ciudadService.obtener(null);
		limpiarObjetos();
	}

	public void insertar(ActionEvent actionEvent) {
		proveedorService.insertar(persona);
	}

	public void limpiarObjetos() {
		persona = new Persona();
		persona.setEmail("");
		persona.setTelefono("");

		persona.setCiudad(new Ciudad());

		persona.setProveedor(new Proveedor());

		persona.getProveedor().setFolio(
				String.valueOf(proveedorService.contar() + 1));
	}

	public void obtenerProveedores() {
		listaPersonasProveedores = new ArrayList<Persona>();
		listaPersonasProveedores = proveedorService
				.obtenerTodosPorBusqueda(
						criterioBusquedaProveedor.toUpperCase(),
						criterioBusquedaCiudad);
	}

	public void setCriterioBusquedaCiudad(Integer criterioBusquedaCiudad) {
		this.criterioBusquedaCiudad = criterioBusquedaCiudad;
	}

	public void setCriterioBusquedaProveedor(String criterioBusquedaProveedor) {
		this.criterioBusquedaProveedor = criterioBusquedaProveedor;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setListaCiudadesBusqueda(List<Ciudad> listaCiudadesBusqueda) {
		this.listaCiudadesBusqueda = listaCiudadesBusqueda;
	}

	public void setListaPersonasProveedores(
			List<Persona> listaPersonasProveedores) {
		this.listaPersonasProveedores = listaPersonasProveedores;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
}
