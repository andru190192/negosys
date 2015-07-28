package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VolumenVentaProductoProveedor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String nombre;
	private Long cantidad;
	private BigDecimal gananciaNeta;

	public VolumenVentaProductoProveedor() {
	}

	public VolumenVentaProductoProveedor(Long id, String nombre, Long cantidad,
			BigDecimal gananciaNeta) {
		this.id = id;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.gananciaNeta = gananciaNeta;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getGananciaNeta() {
		return gananciaNeta;
	}

	public void setGananciaNeta(BigDecimal gananciaNeta) {
		this.gananciaNeta = gananciaNeta;
	}

}