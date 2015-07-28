package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE;

import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.NotaCreditoE;

public class NotaCreditoReporteRIDE {

	private String numeroAutorizacion;
	private String fechaAutorizacion;
	private NotaCreditoE notaCredito;
	private CantidadFactura cantidadFactura;

	public NotaCreditoReporteRIDE() {
	}

	public NotaCreditoReporteRIDE(String numeroAutorizacion,
			String fechaAutorizacion, NotaCreditoE notaCredito,
			CantidadFactura cantidadFactura) {
		this.numeroAutorizacion = numeroAutorizacion;
		this.fechaAutorizacion = fechaAutorizacion;
		this.notaCredito = notaCredito;
		this.cantidadFactura = cantidadFactura;
	}

	public CantidadFactura getCantidadFactura() {
		return cantidadFactura;
	}

	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public NotaCreditoE getNotaCredito() {
		return notaCredito;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public void setNotaCredito(NotaCreditoE notaCredito) {
		this.notaCredito = notaCredito;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

}
