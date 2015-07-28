package ec.com.redepronik.negosys.seguridad.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.seguridad.entity.Bitacora;
import ec.com.redepronik.negosys.seguridad.service.BitacoraService;

@Controller
@Scope("session")
public class BitacoraBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	BitacoraService bitacoraService;

	@Autowired
	PersonaService personaService;

	private Persona persona;
	private String criterio;
	private Date fechaInicio;

	List<Bitacora> listaBitacora;
	List<Persona> listaPersonas;

	public BitacoraBean() {
	}

	public void buscarUsuario() {
		listaPersonas = personaService.obtenerTodosPorBusqueda(criterio, 0);
	}

	public void cargarUsuario(SelectEvent event) {
		persona = personaService.obtenerPorPersonaId(persona.getId());
	}

	public void consultar() {
		listaBitacora = new ArrayList<Bitacora>();
		listaBitacora = bitacoraService.obtener(persona.getId(), fechaInicio);
	}

	public String getCriterio() {
		return criterio;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public List<Bitacora> getListaBitacora() {
		return listaBitacora;
	}

	public List<Persona> getListaPersonas() {
		return listaPersonas;
	}

	public Persona getPersona() {
		return persona;
	}

	@PostConstruct
	public void init() {
		persona = new Persona();
		fechaInicio = new Date();
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setListaBitacora(List<Bitacora> listaBitacora) {
		this.listaBitacora = listaBitacora;
	}

	public void setListaPersonas(List<Persona> listaPersonas) {
		this.listaPersonas = listaPersonas;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}