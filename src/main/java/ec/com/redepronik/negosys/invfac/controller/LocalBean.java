package ec.com.redepronik.negosys.invfac.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.LocalCajero;
import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Controller
@Scope("session")
public class LocalBean {

	@Autowired
	private LocalService localService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private EmpleadoService empleadoService;

	private List<Local> listaLocales;
	private List<Ciudad> listaCiudades;
	private List<LocalCajero> listaLocalEmpleadoCargo;
	private List<PersonaCedulaNombre> listaCajeros;
	private Local local;
	private EmpleadoCargo cajero;
	private LocalCajero encargado;

	public LocalBean() {

	}

	public void actualizar(ActionEvent actionEvent) {
		localService.actualizar(local);
	}

	public void cargarCiudades() {
		listaCiudades = ciudadService.obtenerPorProvincia(local.getProvincia());
	}

	public void cargarMostrar() {
		localService.obtenerActivosPorLocalCajero();
	}

	public void eliminar(ActionEvent actionEvent) {
		localService.eliminar(local);
	}

	public void eliminarCajero(ActionEvent actionEvent) {
		localService.eliminarCajero(encargado);
	}

	public EmpleadoCargo getCajero() {
		return cajero;
	}

	public LocalCajero getEncargado() {
		return encargado;
	}

	public List<PersonaCedulaNombre> getListaCajeros() {
		return listaCajeros;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public List<LocalCajero> getListaLocalEmpleadoCargo() {
		return listaLocalEmpleadoCargo;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public Provincia[] getListaProvincias() {
		return Provincia.values();
	}

	public Local getLocal() {
		return local;
	}

	public void guardarCajeros() {
		localService.guardarCajeros(local, listaLocalEmpleadoCargo);
	}

	@PostConstruct
	public void init() {
		limpiarObjetos();
		listaLocales = new ArrayList<Local>();
		listaLocales = localService.obtenerTodos();
		listaCajeros = empleadoService.obtenerPorCargo(4);
		// listaLocalEmpleadoCargo =
		// localService.obtenerActivosPorLocalCajero();
	}

	public void insertar(ActionEvent actionEvent) {
		// if (local.getBodegalocals() == null)
		// local.setBodegalocals(new ArrayList<Bodegalocal>());
		//
		// bodegaLocal.setBodega(localService.obtenerPorBodegaId(bodega
		// .getBodegaid()));
		// local.addBodegalocal(bodegaLocal);
		listaLocales = localService.insertar(local);
	}

	public void insertarCajero() {
		localService.insertarCajero(local, cajero, listaLocalEmpleadoCargo);
	}

	public void limpiarCajeros() {
		cajero = new EmpleadoCargo();
		listaLocalEmpleadoCargo = new ArrayList<LocalCajero>();

		if (local.getLocalCajero() != null && !local.getLocalCajero().isEmpty())
			listaLocalEmpleadoCargo = local.getLocalCajero();
	}

	public void limpiarObjetos() {
		local = new Local();
		cajero = new EmpleadoCargo();
		listaLocalEmpleadoCargo = new ArrayList<LocalCajero>();
	}

	public void setCajero(EmpleadoCargo cajero) {
		this.cajero = cajero;
	}

	public void setEncargado(LocalCajero encargado) {
		this.encargado = encargado;
	}

	public void setListaCajeros(List<PersonaCedulaNombre> listaCajeros) {
		this.listaCajeros = listaCajeros;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setListaLocalEmpleadoCargo(
			List<LocalCajero> listaLocalEmpleadoCargo) {
		this.listaLocalEmpleadoCargo = listaLocalEmpleadoCargo;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setLocal(Local Local) {
		this.local = Local;
	}
}
