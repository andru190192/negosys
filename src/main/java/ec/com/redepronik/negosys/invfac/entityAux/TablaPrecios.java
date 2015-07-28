package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.TipoPrecioProducto;

public class TablaPrecios implements Serializable {

	private static final long serialVersionUID = 1L;

	private TipoPrecioProducto tipoPrecioProducto;

	private BigDecimal precio;

	private BigDecimal porcentaje;

	private BigDecimal precioIva;

	public TablaPrecios() {

	}

	public TablaPrecios(TipoPrecioProducto tipoPrecioProducto,
			BigDecimal precio, BigDecimal porcentaje, BigDecimal precioIva) {
		this.tipoPrecioProducto = tipoPrecioProducto;
		this.precio = precio;
		this.setPorcentaje(porcentaje);
		this.precioIva = precioIva;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public BigDecimal getPrecioIva() {
		return precioIva;
	}

	public TipoPrecioProducto getTipoPrecioProducto() {
		return tipoPrecioProducto;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public void setPrecioIva(BigDecimal precioIva) {
		this.precioIva = precioIva;
	}

	public void setTipoPrecioProducto(TipoPrecioProducto tipoPrecioProducto) {
		this.tipoPrecioProducto = tipoPrecioProducto;
	}

}
