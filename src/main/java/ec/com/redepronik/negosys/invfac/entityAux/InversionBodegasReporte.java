package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InversionBodegasReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer bodegaid;

	private String nombre;
	private BigDecimal preciototalcompra;
	private BigDecimal preciototalventa;

	public InversionBodegasReporte() {

	}

	public InversionBodegasReporte(Integer bodegaid, String nombre,
			BigDecimal preciototalcompra, BigDecimal preciototalventa) {
		this.bodegaid = bodegaid;
		this.nombre = nombre;
		this.preciototalcompra = preciototalcompra;
		this.preciototalventa = preciototalventa;
	}

	public Integer getBodegaid() {
		return bodegaid;
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

	public void setBodegaid(Integer bodegaid) {
		this.bodegaid = bodegaid;
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

}
