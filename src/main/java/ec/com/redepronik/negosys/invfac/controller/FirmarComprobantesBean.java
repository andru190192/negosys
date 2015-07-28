package ec.com.redepronik.negosys.invfac.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entityAux.DocumentoElectronico;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;

@Controller
@Scope("session")
public class FirmarComprobantesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private List<DocumentoElectronico> listaDocumentosGenerados;
	private DocumentoElectronico documentoElectronico;

	public FirmarComprobantesBean() {
	}

	public void firmar(DocumentoElectronico documentoGenerado) {
		if (documentoGenerado.getNombreDocumento() != null
				&& documentoGenerado.getNombreDocumento().compareToIgnoreCase(
						"") != 0) {
			documentosElectronicosService.firmarXML(true, documentoGenerado);
			listaDocumentosGenerados.remove(documentoGenerado);
		}
	}

	public void firmarTodos() {
		if (listaDocumentosGenerados != null)
			for (DocumentoElectronico docGenerado : listaDocumentosGenerados)
				documentosElectronicosService.firmarXML(false, docGenerado);
		listaDocumentosGenerados = new ArrayList<DocumentoElectronico>();
	}

	public DocumentoElectronico getDocumentoElectronico() {
		return documentoElectronico;
	}

	public List<DocumentoElectronico> getListaDocumentosGenerados() {
		return listaDocumentosGenerados;
	}

	@PostConstruct
	public void init() {
		obtener();
	}

	public void obtener() {
		listaDocumentosGenerados = documentosElectronicosService
				.documentosGenerados();
	}

	public void setDocumentoElectronico(
			DocumentoElectronico documentoElectronico) {
		this.documentoElectronico = documentoElectronico;
	}

	public void setListaDocumentosGenerados(
			List<DocumentoElectronico> listaDocumentosGenerados) {
		this.listaDocumentosGenerados = listaDocumentosGenerados;
	}

}