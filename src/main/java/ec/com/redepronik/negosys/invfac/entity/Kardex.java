package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "kardex")
public class Kardex implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer cantidad;
	private Timestamp fecha;
	private String nota;
	private BigDecimal precio;
	private Boolean activo;
	private Local local;
	private EstadoKardex estadoKardex;
	private Producto producto;

	public Kardex() {
	}

	public Kardex(Long id, Integer cantidad, Timestamp fecha, String nota,
			BigDecimal precio, Boolean activo, Local local,
			EstadoKardex estadoKardex, Producto producto) {
		this.id = id;
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.nota = nota;
		this.precio = precio;
		this.activo = activo;
		this.local = local;
		this.estadoKardex = estadoKardex;
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
		Kardex other = (Kardex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return activo;
	}

	@Column(nullable = false)
	public Integer getCantidad() {
		return this.cantidad;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "estadokardexid", nullable = false)
	public EstadoKardex getEstadoKardex() {
		return estadoKardex;
	}

	@Column(nullable = false)
	public Timestamp getFecha() {
		return this.fecha;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "KARDEX_KARDEXID_GENERATOR", sequenceName = "KARDEX_KARDEXID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KARDEX_KARDEXID_GENERATOR")
	@Column(name = "kardexid", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	// bi-directional many-to-one association to Bodega
	@ManyToOne
	@JoinColumn(name = "localid", nullable = false)
	public Local getLocal() {
		return local;
	}

	@Column(length = 50)
	public String getNota() {
		return this.nota;
	}

	@Column(nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecio() {
		return this.precio;
	}

	// bi-directional many-to-one association to Producto
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

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public void setEstadoKardex(EstadoKardex estadoKardex) {
		this.estadoKardex = estadoKardex;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}