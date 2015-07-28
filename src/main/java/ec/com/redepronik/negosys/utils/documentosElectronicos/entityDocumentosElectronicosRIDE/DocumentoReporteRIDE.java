package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE;

public class DocumentoReporteRIDE {

	private String numeroAutorizacion;
	private String fechaAutorizacion;
	private String estado;
	private String documento;

	public DocumentoReporteRIDE() {
	}

	public DocumentoReporteRIDE(String numeroAutorizacion,
			String fechaAutorizacion, String estado, String documento) {
		this.numeroAutorizacion = numeroAutorizacion;
		this.fechaAutorizacion = fechaAutorizacion;
		this.estado = estado;
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public String getEstado() {
		return estado;
	}

	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

}
