package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the productounidad database table.
 * 
 */
@Entity
@Table(name = "productounidad")
public class ProductoUnidad implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cantidad;
	private Integer orden;
	private Integer padre;
	private Producto producto;
	private Unidad unidad;

	public ProductoUnidad() {
	}

	public ProductoUnidad(Integer id, Integer cantidad, Integer orden,
			Integer padre, Producto producto, Unidad unidad) {
		this.id = id;
		this.cantidad = cantidad;
		this.orden = orden;
		this.padre = padre;
		this.producto = producto;
		this.unidad = unidad;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductoUnidad other = (ProductoUnidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Integer getCantidad() {
		return this.cantidad;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PRODUCTOUNIDAD_PRODUCTOUNIDADID_GENERATOR", sequenceName = "PRODUCTOUNIDAD_PRODUCTOUNIDADID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTOUNIDAD_PRODUCTOUNIDADID_GENERATOR")
	@Column(name = "productounidadid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return this.orden;
	}

	@Column(nullable = false)
	public Integer getPadre() {
		return this.padre;
	}

	// bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	public Producto getProducto() {
		return this.producto;
	}

	// bi-directional many-to-one association to Unidad
	@ManyToOne
	@JoinColumn(name = "unidadid", nullable = false)
	public Unidad getUnidad() {
		return this.unidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPadre(Integer padre) {
		this.padre = padre;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

}
