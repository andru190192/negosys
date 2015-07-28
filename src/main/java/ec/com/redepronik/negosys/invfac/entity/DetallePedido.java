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
@Table(name = "detallespedidos")
public class DetallePedido implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer orden;
	private Pedido pedido;
	private EstadoProductoVenta estadoProductoVenta;
	private Producto producto;
	private BigDecimal precioVenta;
	private Integer cantidad;
	private BigDecimal descuento;

	public DetallePedido() {
	}

	public DetallePedido(Long id, Integer orden, Pedido pedido,
			EstadoProductoVenta estadoProductoVenta, Producto producto,
			BigDecimal precioVenta, Integer cantidad, BigDecimal descuento) {
		this.id = id;
		this.orden = orden;
		this.pedido = pedido;
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
		DetallePedido other = (DetallePedido) obj;
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
	@SequenceGenerator(allocationSize = 1, name = "DETALLESPEDIDOS_DETALLEPEDIDOID_GENERATOR", sequenceName = "DETALLESPEDIDOS_DETALLEPEDIDOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLESPEDIDOS_DETALLEPEDIDOID_GENERATOR")
	@Column(name = "detallepedidoid", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	@ManyToOne
	@JoinColumn(name = "pedidoid", nullable = false)
	public Pedido getPedido() {
		return pedido;
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

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}