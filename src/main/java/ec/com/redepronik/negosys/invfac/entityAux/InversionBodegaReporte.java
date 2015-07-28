package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InversionBodegaReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String ean;

	private String nombre;
	private int stock;
	private BigDecimal preciounicompra;
	private BigDecimal preciototalcompra;
	private BigDecimal preciouniventa;
	private BigDecimal preciototalventa;

	public InversionBodegaReporte() {

	}

	public InversionBodegaReporte(String ean, String nombre, int stock,
			BigDecimal preciounicompra, BigDecimal preciototalcompra,
			BigDecimal preciouniventa, BigDecimal preciototalventa) {
		this.ean = ean;
		this.nombre = nombre;
		this.stock = stock;
		this.preciounicompra = preciounicompra;
		this.preciototalcompra = preciototalcompra;
		this.preciouniventa = preciouniventa;
		this.preciototalventa = preciototalventa;
	}

	public String getEan() {
		return ean;
	}

	public String getNombre() {
		return nombre;
	}

	public BigDecimal getPreciototalcompra() {
		return preciototalcompra;
	}

	public BigDecimal getPreciototalventa() {
		return preciototalventa;
	}

	public BigDecimal getPreciounicompra() {
		return preciounicompra;
	}

	public BigDecimal getPreciouniventa() {
		return preciouniventa;
	}

	public int getStock() {
		return stock;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPreciototalcompra(BigDecimal preciototalcompra) {
		this.preciototalcompra = preciototalcompra;
	}

	public void setPreciototalventa(BigDecimal preciototalventa) {
		this.preciototalventa = preciototalventa;
	}

	public void setPreciounicompra(BigDecimal preciounicompra) {
		this.preciounicompra = preciounicompra;
	}

	public void setPreciouniventa(BigDecimal preciouniventa) {
		this.preciouniventa = preciouniventa;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}
