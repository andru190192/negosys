package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

public class VolumenVentaProductoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	private String producto;
	private String cliente;
	private int cantidad;

	public VolumenVentaProductoCliente(String producto, String cliente,
			int cantidad) {
		this.producto = producto;
		this.cliente = cliente;
		this.cantidad = cantidad;
	}

	public int getCantidad() {
		return cantidad;
	}

	public String getCliente() {
		return cliente;
	}

	public String getProducto() {
		return producto;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

}