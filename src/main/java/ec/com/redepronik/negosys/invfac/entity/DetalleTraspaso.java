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

@Entity
@Table(name = "detalletraspaso")
public class DetalleTraspaso implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cantidad;
	private Producto producto;
	private Traspaso traspaso;

	public DetalleTraspaso() {
	}

	public DetalleTraspaso(Integer id, Integer cantidad, Producto producto,
			Traspaso traspaso) {
		this.id = id;
		this.cantidad = cantidad;
		this.producto = producto;
		this.traspaso = traspaso;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleTraspaso other = (DetalleTraspaso) obj;
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
	@SequenceGenerator(allocationSize = 1, name = "DETALLETRASPASO_DETALLETRASPASOID_GENERATOR", sequenceName = "DETALLETRASPASO_DETALLETRASPASOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLETRASPASO_DETALLETRASPASOID_GENERATOR")
	@Column(name = "detalletraspasoid", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	// bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	public Producto getProducto() {
		return this.producto;
	}

	// bi-directional many-to-one association to Traspaso
	@ManyToOne
	@JoinColumn(name = "traspasoid", nullable = false)
	public Traspaso getTraspaso() {
		return this.traspaso;
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

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setTraspaso(Traspaso traspaso) {
		this.traspaso = traspaso;
	}

}
