package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

import ec.com.redepronik.negosys.invfac.entity.Local;

public class LocalProducto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Local local;
	private int cantidad;

	public LocalProducto() {

	}

	public LocalProducto(Local local, int cantidad) {
		this.local = local;
		this.cantidad = cantidad;
	}

	public int getCantidad() {
		return cantidad;
	}

	public Local getLocal() {
		return local;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public void setLocal(Local local) {
		this.local = local;
	}
}