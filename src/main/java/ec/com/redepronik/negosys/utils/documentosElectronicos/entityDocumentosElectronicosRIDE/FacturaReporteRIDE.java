package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE;

import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.FacturaE;

public class FacturaReporteRIDE {

	private String numeroAutorizacion;
	private String fechaAutorizacion;
	private FacturaE factura;
	private CantidadFactura cantidadFactura;

	public FacturaReporteRIDE() {
	}

	public FacturaReporteRIDE(String numeroAutorizacion,
			String fechaAutorizacion, FacturaE factura,
			CantidadFactura cantidadFactura) {
		this.numeroAutorizacion = numeroAutorizacion;
		this.fechaAutorizacion = fechaAutorizacion;
		this.factura = factura;
		this.cantidadFactura = cantidadFactura;
	}

	public CantidadFactura getCantidadFactura() {
		return cantidadFactura;
	}

	public FacturaE getFactura() {
		return factura;
	}

	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setFactura(FacturaE factura) {
		this.factura = factura;
	}

	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

}
