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
import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.DetalleCliente;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;

@Controller
@Scope("session")
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CiudadService ciudadService;

	private List<Persona> listaPersonasClientes;
	private Persona persona;
	private List<Ciudad> listaCiudadesBusqueda;
	private String criterioBusquedaCliente;
	private Integer criterioBusquedaCiudad;
	private List<Ciudad> listaCiudades;

	public ClienteBean() {

	}

	public void actualizar(ActionEvent actionEvent) {
		clienteService.actualizar(persona);
		listaPersonasClientes = new ArrayList<Persona>();
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
		if (persona != null && persona.getCliente() != null) {
			limpiarObjetos();
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "EL CLIENTE YA EXISTE");
		} else if (persona != null) {
			persona.setCliente(new Cliente());
			persona.getCliente().setFolio(
					String.valueOf(clienteService.contar() + 1));
			persona.getCliente().setNombreComercial(
					persona.getApellido().concat(" ")
							.concat(persona.getNombre()));
			cargarCiudades();
		} else {
			limpiarObjetos();
			persona.setCedula(cedula);
		}
	}

	public void eliminar(ActionEvent actionEvent) {
		clienteService.eliminar(persona);
	}

	public void eliminarAdicional(DetalleCliente detallecliente) {
		personaService.eliminarAdiconal(persona, detallecliente);
	}

	public Integer getCriterioBusquedaCiudad() {
		return criterioBusquedaCiudad;
	}

	public String getCriterioBusquedaCliente() {
		return criterioBusquedaCliente;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public List<Ciudad> getListaCiudadesBusqueda() {
		return listaCiudadesBusqueda;
	}

	public List<Persona> getListaPersonasClientes() {
		return listaPersonasClientes;
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
		clienteService.insertar(persona);
	}

	public void insertarFilaAdicional() {
		personaService.insertarFilaAdicional(persona);
	}

	public void limpiarObjetos() {
		persona = new Persona();
		persona.setEmail("");
		persona.setTelefono("");
		;

		persona.setCiudad(new Ciudad());

		persona.setCliente(new Cliente());

		persona.getCliente()
				.setDetalleClientes(new ArrayList<DetalleCliente>());

		persona.getCliente().setFolio(
				String.valueOf(clienteService.contar() + 1));
	}

	public void obtenerClientes() {
		listaPersonasClientes = new ArrayList<Persona>();
		listaPersonasClientes = clienteService.obtenerTodosPorBusqueda(
				criterioBusquedaCliente.toUpperCase(), criterioBusquedaCiudad);
	}

	public void setCriterioBusquedaCiudad(Integer criterioBusquedaCiudad) {
		this.criterioBusquedaCiudad = criterioBusquedaCiudad;
	}

	public void setCriterioBusquedaCliente(String criterioBusquedaCliente) {
		this.criterioBusquedaCliente = criterioBusquedaCliente;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setListaCiudadesBusqueda(List<Ciudad> listaCiudadesBusqueda) {
		this.listaCiudadesBusqueda = listaCiudadesBusqueda;
	}

	public void setListaPersonasClientes(List<Persona> listaPersonasClientes) {
		this.listaPersonasClientes = listaPersonasClientes;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
}
