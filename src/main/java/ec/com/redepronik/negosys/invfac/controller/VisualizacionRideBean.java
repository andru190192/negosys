package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.convertirString;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entityAux.Anio;
import ec.com.redepronik.negosys.invfac.entityAux.DocumentoElectronico;
import ec.com.redepronik.negosys.invfac.entityAux.Mes;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;

@Controller
@Scope("session")
public class VisualizacionRideBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;
	@Autowired
	private PersonaService personaService;

	private List<DocumentoElectronico> listaDocumentosAutorizados;
	private DocumentoElectronico documentoElectronico;
	private StreamedContent documento;
	private Mes mes;
	private Anio anio;
	private String persona;
	private String cedulaRuc;

	public VisualizacionRideBean() {
	}

	public Mes[] getListaMes() {
		return Mes.values();
	}

	public Anio[] getListaAnio() {
		return Anio.values();
	}

	public List<String> obtenerPersonaPorBusqueda(String criterioPersonaBusqueda) {
		List<String> lista = personaService
				.obtenerListaPersonaAutoComplete(criterioPersonaBusqueda);
		if (lista.size() == 1) {
			persona = lista.get(0);
			cargarPersona();
		}
		return lista;
	}

	public void cargarPersona() {
		cedulaRuc = personaService.cargarPersona(persona).getCedula();
		persona = persona.split(" - ")[1];
	}

	public void descargarXML(DocumentoElectronico documentoElectronico) {
		InputStream is = new ByteArrayInputStream(
				convertirString(documentoElectronico.getDocumento()));
		documento = new DefaultStreamedContent(is, "text/xml",
				documentoElectronico.getNombreDocumento());
	}

	public void enviarCorreo(DocumentoElectronico documentoElectronico) {
		documentosElectronicosService.envioCorreo(true, documentoElectronico,
				null);
	}

	public StreamedContent getDocumento() {
		return documento;
	}

	public DocumentoElectronico getDocumentoElectronico() {
		return documentoElectronico;
	}

	public List<DocumentoElectronico> getListaDocumentosAutorizados() {
		return listaDocumentosAutorizados;
	}

	public void obtener() {
		if (cedulaRuc.compareTo("") == 0 || cedulaRuc == null)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"DEBE INGRESAR UNA CEDULA O RUC PARA REALIZAR LA BUSQUEDA");
		else if (mes == null)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"DEBE INGRESAR UN MES PARA REALIZAR LA BUSQUEDA");
		else if (anio == null)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"DEBE INGRESAR UN AÑO PARA REALIZAR LA BUSQUEDA");
		else
			listaDocumentosAutorizados = documentosElectronicosService
					.documentosAutorizados(cedulaRuc, mes, anio);
	}

	public void setDocumento(StreamedContent documento) {
		this.documento = documento;
	}

	public void setDocumentoElectronico(
			DocumentoElectronico documentoElectronico) {
		this.documentoElectronico = documentoElectronico;
	}

	public void setListaDocumentosAutorizados(
			List<DocumentoElectronico> listaDocumentosAutorizados) {
		this.listaDocumentosAutorizados = listaDocumentosAutorizados;
	}

	public void verRide(DocumentoElectronico documentoElectronico) {
		documentosElectronicosService.generarRIDE(documentoElectronico);
	}

	public Mes getMes() {
		return mes;
	}

	public void setMes(Mes mes) {
		this.mes = mes;
	}

	public Anio getAnio() {
		return anio;
	}

	public void setAnio(Anio anio) {
		this.anio = anio;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public String getCedulaRuc() {
		return cedulaRuc;
	}

	public void setCedulaRuc(String cedulaRuc) {
		this.cedulaRuc = cedulaRuc;
	}

}