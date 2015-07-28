package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.GuiaRemision;
import ec.com.redepronik.negosys.invfac.service.GuiaRemisionService;

@Controller
@Scope("session")
public class ListadoGuiaRemisionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private GuiaRemisionService guiaRemisionService;

	// @Autowired
	// private GuiaRemisionReport guiaRemisionReport;

	private List<GuiaRemision> listaGuiaRemision;
	private String criterioBusquedaCliente;
	private String criterioBusquedaCodigo;
	private GuiaRemision guiaRemision;

	public ListadoGuiaRemisionBean() {
		guiaRemision = new GuiaRemision();
	}

	public String getCriterioBusquedaCliente() {
		return criterioBusquedaCliente;
	}

	public String getCriterioBusquedaCodigo() {
		return criterioBusquedaCodigo;
	}

	public GuiaRemision getGuiaRemision() {
		return guiaRemision;
	}

	public List<GuiaRemision> getListaGuiaRemision() {
		return listaGuiaRemision;
	}

	public void imprimirGuiaRemision() {
		// guiaRemisionReport.reporteGiaRemision(guiaRemision);
	}

	public void limpiarObjetos() {
		criterioBusquedaCliente = new String();
		criterioBusquedaCodigo = new String();
	}

	public void obtener() {
		if (criterioBusquedaCliente.compareToIgnoreCase("") == 0
				&& criterioBusquedaCodigo.compareToIgnoreCase("") == 0)
			presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"MENSAJE DEL SISTEMA", "INGRESE UN CRITERIO DE BUSQUEDA"));
		else {
			if (criterioBusquedaCliente.length() >= 1
					&& criterioBusquedaCliente.length() <= 4) {
				presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"MENSAJE DEL SISTEMA",
						"INGRESE MAS DE 4 CARACTERES PARA LA BÚSQUEDA POR CLIENTES"));
			} else if (!criterioBusquedaCodigo.matches("[0-9]*")) {
				presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"MENSAJE DEL SISTEMA",
						"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR DOCUMENTOS"));
			} else if (criterioBusquedaCodigo.compareToIgnoreCase("") != 0
					&& criterioBusquedaCodigo.charAt(0) == '0') {
				presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"MENSAJE DEL SISTEMA",
						"NO SE PERMITE CEROS AL INICIO PARA LA BÚSQUEDA POR DOCUMENTOS"));
			} else {
				listaGuiaRemision = guiaRemisionService.obtener(
						criterioBusquedaCliente.toUpperCase(),
						criterioBusquedaCodigo.toUpperCase());
				if (listaGuiaRemision.isEmpty()) {
					presentaMensaje(new FacesMessage(
							FacesMessage.SEVERITY_INFO, "MENSAJE DEL SISTEMA",
							"NO SE ENCONTRARON COINCIDENCIAS"));
				}
				limpiarObjetos();
			}
		}
	}

	private void presentaMensaje(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void redirecionar() {
		redireccionar("guiaRemision.jsf");
	}

	public void setCriterioBusquedaCliente(String criterioBusquedaCliente) {
		this.criterioBusquedaCliente = criterioBusquedaCliente;
	}

	public void setCriterioBusquedaCodigo(String criterioBusquedaCodigo) {
		this.criterioBusquedaCodigo = criterioBusquedaCodigo;
	}

	public void setGuiaRemision(GuiaRemision guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public void setListaGuiaRemision(List<GuiaRemision> listaGuiaRemision) {
		this.listaGuiaRemision = listaGuiaRemision;
	}

}