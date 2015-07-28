package ec.com.redepronik.negosys.invfac.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entityAux.DocumentoElectronico;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;

@Controller
@Scope("session")
public class SubirComprobantesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private List<DocumentoElectronico> listaDocumentosAutorizados;
	private DocumentoElectronico documentoElectronico;
	private StreamedContent documento;

	public SubirComprobantesBean() {
	}

	public void subir(DocumentoElectronico documentoAut) {
		if (documentoAut.getNombreDocumento() != null
				&& documentoAut.getNombreDocumento().compareToIgnoreCase("") != 0) {
			documentosElectronicosService.subirComprobantesElectronicos(true,
					documentoAut);
			listaDocumentosAutorizados.remove(documentoAut);
		}
	}

	public void subirTodos() {
		if (listaDocumentosAutorizados != null)
			for (DocumentoElectronico docuAut : listaDocumentosAutorizados)
				documentosElectronicosService.subirComprobantesElectronicos(
						false, docuAut);
		listaDocumentosAutorizados = new ArrayList<DocumentoElectronico>();
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

	@PostConstruct
	public void init() {
		obtener();
	}

	public void obtener() {
		listaDocumentosAutorizados = documentosElectronicosService
				.documentosAutorizadosNoSubidos();
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

}