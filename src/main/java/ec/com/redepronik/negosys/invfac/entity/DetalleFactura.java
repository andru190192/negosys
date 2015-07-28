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
@Table(name = "`detallesFacturas`")
public class DetalleFactura implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer orden;
	private Factura factura;
	private EstadoProductoVenta estadoProductoVenta;
	private Producto producto;
	private BigDecimal precioCosto;
	private BigDecimal precioVenta;
	private BigDecimal precioVentaCuadre;
	private Integer cantidad;
	private Integer cantidadCuadre;
	private BigDecimal descuento;
	private BigDecimal descuentoCuadre;

	public DetalleFactura() {
	}

	public DetalleFactura(Long id, Integer orden, Factura factura,
			EstadoProductoVenta estadoProductoVenta, Producto producto,
			BigDecimal precioCosto, BigDecimal precioVenta,
			BigDecimal precioVentaCuadre, Integer cantidad,
			Integer cantidadCuadre, BigDecimal descuento,
			BigDecimal descuentoCuadre) {
		this.id = id;
		this.orden = orden;
		this.factura = factura;
		this.estadoProductoVenta = estadoProductoVenta;
		this.producto = producto;
		this.precioCosto = precioCosto;
		this.precioVenta = precioVenta;
		this.precioVentaCuadre = precioVentaCuadre;
		this.cantidad = cantidad;
		this.cantidadCuadre = cantidadCuadre;
		this.descuento = descuento;
		this.descuentoCuadre = descuentoCuadre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleFactura other = (DetalleFactura) obj;
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

	@Column(name = "`cantidadCuadre`", nullable = false)
	public Integer getCantidadCuadre() {
		return cantidadCuadre;
	}

	@Column(precision = 8, scale = 2)
	public BigDecimal getDescuento() {
		return descuento;
	}

	@Column(name = "`descuentoCuadre`", precision = 8, scale = 2)
	public BigDecimal getDescuentoCuadre() {
		return descuentoCuadre;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "`estadoProductoVenta`", nullable = false)
	public EstadoProductoVenta getEstadoProductoVenta() {
		return estadoProductoVenta;
	}

	@ManyToOne
	@JoinColumn(name = "factura", nullable = false)
	public Factura getFactura() {
		return factura;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "DETALLESFACTURAS_ID_GENERATOR", sequenceName = "`detallesFacturas_id_seq`")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLESFACTURAS_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	@Column(name = "`precioCosto`", nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecioCosto() {
		return precioCosto;
	}

	@Column(name = "`precioVenta`", nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	@Column(name = "`precioVentaCuadre`", nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecioVentaCuadre() {
		return precioVentaCuadre;
	}

	@ManyToOne
	@JoinColumn(name = "producto", nullable = false)
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

	public void setCantidadCuadre(Integer cantidadCuadre) {
		this.cantidadCuadre = cantidadCuadre;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public void setDescuentoCuadre(BigDecimal descuentoCuadre) {
		this.descuentoCuadre = descuentoCuadre;
	}

	public void setEstadoProductoVenta(EstadoProductoVenta estadoProductoVenta) {
		this.estadoProductoVenta = estadoProductoVenta;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPrecioCosto(BigDecimal precioCosto) {
		this.precioCosto = precioCosto;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public void setPrecioVentaCuadre(BigDecimal precioVentaCuadre) {
		this.precioVentaCuadre = precioVentaCuadre;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}