package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VolumenVentaProductoAnio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String nombre;
	private BigDecimal precio;
	private BigDecimal mes1;
	private BigDecimal mes2;
	private BigDecimal mes3;
	private BigDecimal mes4;
	private BigDecimal mes5;
	private BigDecimal mes6;
	private BigDecimal mes7;
	private BigDecimal mes8;
	private BigDecimal mes9;
	private BigDecimal mes10;
	private BigDecimal mes11;
	private BigDecimal mes12;

	public VolumenVentaProductoAnio() {
	}

	public VolumenVentaProductoAnio(Long id, String nombre, BigDecimal precio,
			BigDecimal mes1, BigDecimal mes2, BigDecimal mes3, BigDecimal mes4,
			BigDecimal mes5, BigDecimal mes6, BigDecimal mes7, BigDecimal mes8,
			BigDecimal mes9, BigDecimal mes10, BigDecimal mes11,
			BigDecimal mes12) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.mes1 = mes1;
		this.mes2 = mes2;
		this.mes3 = mes3;
		this.mes4 = mes4;
		this.mes5 = mes5;
		this.mes6 = mes6;
		this.mes7 = mes7;
		this.mes8 = mes8;
		this.mes9 = mes9;
		this.mes10 = mes10;
		this.mes11 = mes11;
		this.mes12 = mes12;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getMes1() {
		return mes1;
	}

	public BigDecimal getMes10() {
		return mes10;
	}

	public BigDecimal getMes11() {
		return mes11;
	}

	public BigDecimal getMes12() {
		return mes12;
	}

	public BigDecimal getMes2() {
		return mes2;
	}

	public BigDecimal getMes3() {
		return mes3;
	}

	public BigDecimal getMes4() {
		return mes4;
	}

	public BigDecimal getMes5() {
		return mes5;
	}

	public BigDecimal getMes6() {
		return mes6;
	}

	public BigDecimal getMes7() {
		return mes7;
	}

	public BigDecimal getMes8() {
		return mes8;
	}

	public BigDecimal getMes9() {
		return mes9;
	}

	public String getNombre() {
		return nombre;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMes1(BigDecimal mes1) {
		this.mes1 = mes1;
	}

	public void setMes10(BigDecimal mes10) {
		this.mes10 = mes10;
	}

	public void setMes11(BigDecimal mes11) {
		this.mes11 = mes11;
	}

	public void setMes12(BigDecimal mes12) {
		this.mes12 = mes12;
	}

	public void setMes2(BigDecimal mes2) {
		this.mes2 = mes2;
	}

	public void setMes3(BigDecimal mes3) {
		this.mes3 = mes3;
	}

	public void setMes4(BigDecimal mes4) {
		this.mes4 = mes4;
	}

	public void setMes5(BigDecimal mes5) {
		this.mes5 = mes5;
	}

	public void setMes6(BigDecimal mes6) {
		this.mes6 = mes6;
	}

	public void setMes7(BigDecimal mes7) {
		this.mes7 = mes7;
	}

	public void setMes8(BigDecimal mes8) {
		this.mes8 = mes8;
	}

	public void setMes9(BigDecimal mes9) {
		this.mes9 = mes9;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

}