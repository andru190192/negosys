package ec.com.redepronik.negosys.invfac.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Unidad;
import ec.com.redepronik.negosys.invfac.service.UnidadService;

@Controller
@Scope("session")
public class UnidadBean {

	@Autowired
	private UnidadService unidadService;

	private List<Unidad> listaUnidades;
	private Unidad unidad;

	public UnidadBean() {
	}

	public void actualizar(ActionEvent actionEvent) {
		unidadService.actualizar(unidad);
	}

	public void eliminar(ActionEvent actionEvent) {
		unidadService.eliminar(unidad);
	}

	public List<Unidad> getListaUnidades() {
		return listaUnidades;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	@PostConstruct
	public void init() {
		unidad = new Unidad();
		listaUnidades = new ArrayList<Unidad>();
		listaUnidades = unidadService.obtener(null);
	}

	public void insertar(ActionEvent actionEvent) {
		listaUnidades = unidadService.insertar(unidad);
	}

	public void limpiar() {
		unidad = new Unidad();
	}

	public void setListaUnidades(List<Unidad> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}

	public void setUnidad(Unidad Unidad) {
		this.unidad = Unidad;
	}
}
