package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VolumenVentaProductoMes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String nombre;
	private BigDecimal precio;
	private BigDecimal s1;
	private BigDecimal s2;
	private BigDecimal s3;
	private BigDecimal s4;
	private BigDecimal s5;
	private BigDecimal s6;

	public VolumenVentaProductoMes() {
	}

	public VolumenVentaProductoMes(Long id, String nombre, BigDecimal precio,
			BigDecimal s1, BigDecimal s2, BigDecimal s3, BigDecimal s4,
			BigDecimal s5, BigDecimal s6) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
		this.s5 = s5;
		this.setS6(s6);
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

	public BigDecimal getS1() {
		return s1;
	}

	public BigDecimal getS2() {
		return s2;
	}

	public BigDecimal getS3() {
		return s3;
	}

	public BigDecimal getS4() {
		return s4;
	}

	public BigDecimal getS5() {
		return s5;
	}

	public BigDecimal getS6() {
		return s6;
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

	public void setS1(BigDecimal s1) {
		this.s1 = s1;
	}

	public void setS2(BigDecimal s2) {
		this.s2 = s2;
	}

	public void setS3(BigDecimal s3) {
		this.s3 = s3;
	}

	public void setS4(BigDecimal s4) {
		this.s4 = s4;
	}

	public void setS5(BigDecimal s5) {
		this.s5 = s5;
	}

	public void setS6(BigDecimal s6) {
		this.s6 = s6;
	}

}