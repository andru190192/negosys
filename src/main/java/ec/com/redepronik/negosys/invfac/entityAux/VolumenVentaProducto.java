package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VolumenVentaProducto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String nombre;
	private Long cantidad;
	private String cantidadString;

	public VolumenVentaProducto() {
	}

	public VolumenVentaProducto(Long id, String nombre, Long cantidad,
			String cantidadString) {
		this.id = id;
		this.nombre = nombre;
		this.cantidad = cantidad;
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

	public String getCantidadString() {
		return cantidadString;
	}

	public void setCantidadString(String cantidadString) {
		this.cantidadString = cantidadString;
	}

}