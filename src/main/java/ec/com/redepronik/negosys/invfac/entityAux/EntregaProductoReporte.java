package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntregaProductoReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String ean;
	private String nombre;
	private String cantidadString;

	public EntregaProductoReporte() {

	}

	public EntregaProductoReporte(String ean, String nombre,
			String cantidadString) {
		this.ean = ean;
		this.nombre = nombre;
		this.cantidadString = cantidadString;
	}

	public String getEan() {
		return ean;
	}

	public String getNombre() {
		return nombre;
	}

	public void setEan(String ean) {
		this.ean = ean;
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
