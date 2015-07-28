package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.util.List;

public class ReporteDialtor implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<VolumenVentaProductoCliente> listVolumenVentaProductoCliente;
	private List<RefrigeradorCliente> listRefrigeradorCliente;

	public ReporteDialtor(
			List<VolumenVentaProductoCliente> listVolumenVentaProductoCliente,
			List<RefrigeradorCliente> listRefrigeradorCliente) {
		this.listVolumenVentaProductoCliente = listVolumenVentaProductoCliente;
		this.listRefrigeradorCliente = listRefrigeradorCliente;
	}

	public List<RefrigeradorCliente> getListRefrigeradorCliente() {
		return listRefrigeradorCliente;
	}

	public List<VolumenVentaProductoCliente> getListVolumenVentaProductoCliente() {
		return listVolumenVentaProductoCliente;
	}

	public void setListRefrigeradorCliente(
			List<RefrigeradorCliente> listRefrigeradorCliente) {
		this.listRefrigeradorCliente = listRefrigeradorCliente;
	}

	public void setListVolumenVentaProductoCliente(
			List<VolumenVentaProductoCliente> listVolumenVentaProductoCliente) {
		this.listVolumenVentaProductoCliente = listVolumenVentaProductoCliente;
	}

}