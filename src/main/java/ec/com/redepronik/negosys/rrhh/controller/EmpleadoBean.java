package ec.com.redepronik.negosys.rrhh.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.rrhh.entity.Cargo;
import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Empleado;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;
import ec.com.redepronik.negosys.rrhh.service.CargoService;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;

@Controller
@Scope("session")
public class EmpleadoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private CargoService cargoService;

	private Persona persona;

	private List<Ciudad> listaCiudades;

	private Cargo cargo;
	private List<Cargo> listaCargos;
	private List<EmpleadoCargo> listaEmpleadoCargos;

	private List<Persona> listaPersonasEmpleados;
	private List<Ciudad> listaCiudadesBusqueda;
	private String criterioBusquedaEmpleado;
	private Integer criterioBusquedaCiudad;
	private Integer criterioBusquedaCargo;

	public EmpleadoBean() {

	}

	public void actualizar(ActionEvent actionEvent) {
		empleadoService.actualizar(persona);
	}

	public void cargarCargos() {
		listaEmpleadoCargos = new ArrayList<EmpleadoCargo>();
		listaEmpleadoCargos = persona.getEmpleado().getEmpleadoCargos();
	}

	public void cargarCiudades() {
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
		if (persona != null && persona.getEmpleado() != null) {
			limpiarObjetos();
			presentaMensaje(FacesMessage.SEVERITY_WARN, "EL EMPLEADO YA EXISTE");
		} else if (persona != null) {
			persona.setEmpleado(new Empleado());
			persona.getEmpleado().setFolio(
					String.valueOf(empleadoService.contar() + 1));
			cargarCiudades();
		} else {
			limpiarObjetos();
			persona.setCedula(cedula);
		}
	}

	public void eliminar(ActionEvent actionEvent) {
		empleadoService.eliminar(persona);
	}

	public Cargo getCargo() {
		return cargo;
	}

	public Integer getCriterioBusquedaCargo() {
		return criterioBusquedaCargo;
	}

	public Integer getCriterioBusquedaCiudad() {
		return criterioBusquedaCiudad;
	}

	public String getCriterioBusquedaEmpleado() {
		return criterioBusquedaEmpleado;
	}

	public List<Cargo> getListaCargos() {
		return listaCargos;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public List<Ciudad> getListaCiudadesBusqueda() {
		return listaCiudadesBusqueda;
	}

	public List<EmpleadoCargo> getListaEmpleadoCargos() {
		return listaEmpleadoCargos;
	}

	public List<Persona> getListaPersonasEmpleados() {
		return listaPersonasEmpleados;
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
		listaCargos = cargoService.obtener(true);
		limpiarObjetos();
	}

	public void insertar(ActionEvent actionEvent) {
		empleadoService.insertar(persona);
	}

	public void insertarCargos() {
		persona.getEmpleado().setEmpleadoCargos(listaEmpleadoCargos);
		empleadoService.insertarCargos(persona);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"SE INSERTARON LOS CARGOS EXITOSAMENTE");
	}

	public void insertarEmpleadoCargo() {
		boolean bn = false;
		if (cargo.getId() == null || cargo.getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN CARGO");
		} else {
			for (EmpleadoCargo ec : listaEmpleadoCargos) {
				if (ec.getCargo().getId() == cargo.getId()) {
					bn = true;
					break;
				}
			}

			if (bn) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"EL CARGO YA ESTÁ AGREGADO PARA ESTE EMPLEADO");
			} else {
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"CARGO INSERTADO CON ÉXITO");
				EmpleadoCargo empleadoCargo = new EmpleadoCargo();
				empleadoCargo.setCargo(cargoService.obtenerPorCargoId(cargo
						.getId()));
				empleadoCargo.setEmpleado(persona.getEmpleado());
				empleadoCargo.setFechaInicio(new Date());
				empleadoCargo.setActual(true);
				listaEmpleadoCargos.add(empleadoCargo);
			}
		}
	}

	public void limpiarObjetos() {
		persona = new Persona();
		persona.setCiudad(new Ciudad());

		persona.setEmpleado(new Empleado());
		persona.getEmpleado().setFolio(
				String.valueOf(empleadoService.contar() + 1));

		cargo = new Cargo();
	}

	public void obtenerEmpleados() {
		listaPersonasEmpleados = new ArrayList<Persona>();
		listaPersonasEmpleados = empleadoService.obtenerTodosPorBusqueda(
				criterioBusquedaCargo, criterioBusquedaEmpleado.toUpperCase(),
				criterioBusquedaCiudad);
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setCriterioBusquedaCargo(Integer criterioBusquedaCargo) {
		this.criterioBusquedaCargo = criterioBusquedaCargo;
	}

	public void setCriterioBusquedaCiudad(Integer criterioBusquedaCiudad) {
		this.criterioBusquedaCiudad = criterioBusquedaCiudad;
	}

	public void setCriterioBusquedaEmpleado(String criterioBusquedaEmpleado) {
		this.criterioBusquedaEmpleado = criterioBusquedaEmpleado;
	}

	public void setListaCargos(List<Cargo> listaCargos) {
		this.listaCargos = listaCargos;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setListaCiudadesBusqueda(List<Ciudad> listaCiudadesBusqueda) {
		this.listaCiudadesBusqueda = listaCiudadesBusqueda;
	}

	public void setListaEmpleadoCargos(List<EmpleadoCargo> listaEmpleadoCargos) {
		this.listaEmpleadoCargos = listaEmpleadoCargos;
	}

	public void setListaPersonasEmpleados(List<Persona> listaPersonasEmpleados) {
		this.listaPersonasEmpleados = listaPersonasEmpleados;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}
