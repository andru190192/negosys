package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ControlInventarioReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String ean;
	private String nombre;
	private int stock;
	private String nombreStock;

	public ControlInventarioReporte() {
	}

	public ControlInventarioReporte(long id, String ean, String nombre,
			int stock, String nombreStock) {
		this.id = id;
		this.ean = ean;
		this.nombre = nombre;
		this.stock = stock;
		this.nombreStock = nombreStock;
	}

	public String getEan() {
		return ean;
	}

	public long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNombreStock() {
		return nombreStock;
	}

	public int getStock() {
		return stock;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNombreStock(String nombrestock) {
		this.nombreStock = nombrestock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}