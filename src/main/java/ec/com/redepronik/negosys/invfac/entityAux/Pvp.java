package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

public class Pvp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer tipoPrecioId;
	private String nombre;
	private BigDecimal precioConImpuestos;
	private BigDecimal precioSinImpuestos;
	private Boolean pvp;

	public Pvp() {

	}

	public Pvp(Integer tipoPrecioId, String nombre,
			BigDecimal precioConImpuestos, BigDecimal precioSinImpuestos,
			Boolean pvp) {
		this.tipoPrecioId = tipoPrecioId;
		this.nombre = nombre;
		this.precioConImpuestos = precioConImpuestos;
		this.precioSinImpuestos = precioSinImpuestos;
		this.pvp = pvp;
	}

	public String getNombre() {
		return nombre;
	}

	public BigDecimal getPrecioConImpuestos() {
		return precioConImpuestos;
	}

	public BigDecimal getPrecioSinImpuestos() {
		return precioSinImpuestos;
	}

	public Boolean getPvp() {
		return pvp;
	}

	public Integer getTipoPrecioId() {
		return tipoPrecioId;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecioConImpuestos(BigDecimal precioConImpuestos) {
		this.precioConImpuestos = precioConImpuestos;
	}

	public void setPrecioSinImpuestos(BigDecimal precioSinImpuestos) {
		this.precioSinImpuestos = precioSinImpuestos;
	}

	public void setPvp(Boolean pvp) {
		this.pvp = pvp;
	}

	public void setTipoPrecioId(Integer tipoPrecioId) {
		this.tipoPrecioId = tipoPrecioId;
	}

}
