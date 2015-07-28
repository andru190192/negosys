package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "detalleingreso")
public class DetalleIngreso implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer cantidad;
	private BigDecimal precio;
	private Integer orden;
	private Local local;
	private Ingreso ingreso;
	private Producto producto;

	public DetalleIngreso() {
	}

	public DetalleIngreso(Long id, Integer cantidad, BigDecimal precio,
			Integer orden, Local local, Ingreso ingreso, Producto producto) {
		this.id = id;
		this.cantidad = cantidad;
		this.precio = precio;
		this.orden = orden;
		this.local = local;
		this.ingreso = ingreso;
		this.producto = producto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleIngreso other = (DetalleIngreso) obj;
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
	@SequenceGenerator(allocationSize = 1, name = "DETALLEINGRESO_DETALLESINGRESOSID_GENERATOR", sequenceName = "DETALLEINGRESO_DETALLESINGRESOSID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLEINGRESO_DETALLESINGRESOSID_GENERATOR")
	@Column(name = "detallesingresosid", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name = "ingresoid", nullable = false)
	public Ingreso getIngreso() {
		return this.ingreso;
	}

	@ManyToOne
	@JoinColumn(name = "localid", nullable = false)
	public Local getLocal() {
		return local;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	@Column(nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecio() {
		return this.precio;
	}

	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	public Producto getProducto() {
		return this.producto;
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
