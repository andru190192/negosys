package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "detallesnotascreditos")
public class DetalleNotaCredito implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer orden;
	private NotaCredito notaCredito;
	private EstadoProductoVenta estadoProductoVenta;
	private Producto producto;
	private BigDecimal precioVenta;
	private Integer cantidad;
	private BigDecimal descuento;

	public DetalleNotaCredito() {
	}

	public DetalleNotaCredito(Long id, Integer orden, NotaCredito notaCredito,
			EstadoProductoVenta estadoProductoVenta, Producto producto,
			BigDecimal precioVenta, Integer cantidad, BigDecimal descuento) {
		this.id = id;
		this.orden = orden;
		this.notaCredito = notaCredito;
		this.estadoProductoVenta = estadoProductoVenta;
		this.producto = producto;
		this.precioVenta = precioVenta;
		this.cantidad = cantidad;
		this.descuento = descuento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleNotaCredito other = (DetalleNotaCredito) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Integer getCantidad() {
		return cantidad;
	}

	@Column(precision = 8, scale = 2)
	public BigDecimal getDescuento() {
		return descuento;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "estadoproductoventaid", nullable = false)
	public EstadoProductoVenta getEstadoProductoVenta() {
		return estadoProductoVenta;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "DETALLESNOTASCREDITOS_DETALLENOTACREDITOID_GENERATOR", sequenceName = "DETALLESNOTASCREDITOS_DETALLENOTACREDITOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLESNOTASCREDITOS_DETALLENOTACREDITOID_GENERATOR")
	@Column(name = "detallenotacreditoid", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name = "notacreditoid", nullable = false)
	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	@Column(name = "precioventa", nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	public Producto getProducto() {
		return producto;
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

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public void setEstadoProductoVenta(EstadoProductoVenta estadoProductoVenta) {
		this.estadoProductoVenta = estadoProductoVenta;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}