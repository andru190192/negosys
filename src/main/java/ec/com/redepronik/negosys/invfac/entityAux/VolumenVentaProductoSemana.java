package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VolumenVentaProductoSemana implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String nombre;
	private BigDecimal precio;
	private BigDecimal d1;
	private BigDecimal d2;
	private BigDecimal d3;
	private BigDecimal d4;
	private BigDecimal d5;
	private BigDecimal d6;

	public VolumenVentaProductoSemana() {
	}

	public VolumenVentaProductoSemana(Long id, String nombre,
			BigDecimal precio, BigDecimal d1, BigDecimal d2, BigDecimal d3,
			BigDecimal d4, BigDecimal d5, BigDecimal d6) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
		this.d4 = d4;
		this.d5 = d5;
		this.d6 = d6;
	}

	public BigDecimal getD1() {
		return d1;
	}

	public BigDecimal getD2() {
		return d2;
	}

	public BigDecimal getD3() {
		return d3;
	}

	public BigDecimal getD4() {
		return d4;
	}

	public BigDecimal getD5() {
		return d5;
	}

	public BigDecimal getD6() {
		return d6;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setD1(BigDecimal d1) {
		this.d1 = d1;
	}

	public void setD2(BigDecimal d2) {
		this.d2 = d2;
	}

	public void setD3(BigDecimal d3) {
		this.d3 = d3;
	}

	public void setD4(BigDecimal d4) {
		this.d4 = d4;
	}

	public void setD5(BigDecimal d5) {
		this.d5 = d5;
	}

	public void setD6(BigDecimal d6) {
		this.d6 = d6;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

}