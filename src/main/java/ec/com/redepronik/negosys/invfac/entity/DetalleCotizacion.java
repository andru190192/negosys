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
@Table(name = "detallescotizaciones")
public class DetalleCotizacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer orden;
	private Cotizacion cotizacion;
	private EstadoProductoVenta estadoProductoVenta;
	private Producto producto;
	private BigDecimal precioVenta;
	private Integer cantidad;
	private BigDecimal descuento;

	public DetalleCotizacion() {
	}

	public DetalleCotizacion(Long id, Integer orden, Cotizacion cotizacion,
			EstadoProductoVenta estadoProductoVenta, Producto producto,
			BigDecimal precioVenta, Integer cantidad, BigDecimal descuento) {
		this.id = id;
		this.orden = orden;
		this.cotizacion = cotizacion;
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
		DetalleCotizacion other = (DetalleCotizacion) obj;
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

	@ManyToOne
	@JoinColumn(name = "cotizacionid", nullable = false)
	public Cotizacion getCotizacion() {
		return cotizacion;
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
	@SequenceGenerator(allocationSize = 1, name = "DETALLESCOTIZACIONES_DETALLECOTIZACIONID_GENERATOR", sequenceName = "DETALLESCOTIZACIONES_DETALLECOTIZACIONID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLESCOTIZACIONES_DETALLECOTIZACIONID_GENERATOR")
	@Column(name = "detallecotizacionid", unique = true, nullable = false)
	public Long getId() {
		return id;
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

	@ManyToOne
	@JoinColumn(name = "cotizacionid", nullable = false)
	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
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

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}