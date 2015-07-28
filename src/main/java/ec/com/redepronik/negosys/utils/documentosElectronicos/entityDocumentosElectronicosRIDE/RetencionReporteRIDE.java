package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.ComprobanteRetencionE;

public class RetencionReporteRIDE {

	private String numeroAutorizacion;
	private String fechaAutorizacion;
	private ComprobanteRetencionE retencion;

	public RetencionReporteRIDE() {
	}

	public RetencionReporteRIDE(String numeroAutorizacion,
			String fechaAutorizacion, ComprobanteRetencionE retencion) {
		this.numeroAutorizacion = numeroAutorizacion;
		this.fechaAutorizacion = fechaAutorizacion;
		this.retencion = retencion;

	}

	public ComprobanteRetencionE getRetencion() {
		return retencion;
	}

	public void setRetencion(ComprobanteRetencionE retencion) {
		this.retencion = retencion;
	}

	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

}
