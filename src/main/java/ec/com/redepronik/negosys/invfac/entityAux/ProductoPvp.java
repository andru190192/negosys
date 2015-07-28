package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.Producto;

public class ProductoPvp implements Serializable {

	private static final long serialVersionUID = 1L;
	private Producto producto;
	private BigDecimal pvp;

	public ProductoPvp() {

	}

	public ProductoPvp(Producto producto, BigDecimal pvp) {
		this.producto = producto;
		this.pvp = pvp;
	}

	public Producto getProducto() {
		return producto;
	}

	public BigDecimal getPvp() {
		return pvp;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setPvp(BigDecimal pvp) {
		this.pvp = pvp;
	}

}
