package ec.com.redepronik.negosys.invfac.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Grupo;
import ec.com.redepronik.negosys.invfac.service.GrupoService;

@Controller
@Scope("session")
public class GrupoBean {

	@Autowired
	private GrupoService grupoService;

	private List<Grupo> listaGrupos;
	private Grupo grupo;

	public GrupoBean() {
	}

	public void actualizar(ActionEvent actionEvent) {
		listaGrupos = grupoService.actualizar(grupo);
	}

	public void eliminar(ActionEvent actionEvent) {
		grupoService.eliminar(grupo);
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	@PostConstruct
	public void init() {
		grupo = new Grupo();
		listaGrupos = new ArrayList<Grupo>();
		listaGrupos = grupoService.obtener();
	}

	public void insertar(ActionEvent actionEvent) {
		listaGrupos = grupoService.insertar(grupo);
	}

	public void limpiarObjetos() {
		grupo = new Grupo();
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

}
