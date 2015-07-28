package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "producto")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Boolean activo;
	private Boolean kardex;
	private Integer cantidadMinima;
	private String codigo1;
	private String ean;
	private Boolean importado;
	private Boolean facturacion;
	private String nombre;
	private BigDecimal precio;
	private List<DetalleFactura> detalleFactura;
	private List<DetalleCotizacion> detalleCotizacion;
	private List<DetallePedido> detallePedido;
	private List<DetalleBajaInventario> detalleBajaInventario;
	private List<DetalleIngreso> detalleIngresos;
	private List<DetalleTraspaso> detalleTraspasos;
	private List<Kardex> kardexs;
	private List<ProductoUnidad> productoUnidads;
	private List<TipoPrecioProducto> tipoPrecioProductos;
	private List<ProductoTarifa> productosTarifas;
	private Grupo grupo;
	private TipoProducto tipoProducto;

	public Producto() {
	}

	public Producto(Long id, Boolean activo, Boolean kardex,
			Integer cantidadMinima, String codigo1, String ean,
			Boolean importado, String nombre, BigDecimal precio,
			List<DetalleFactura> detalleFactura,
			List<DetalleCotizacion> detalleCotizacion,
			List<DetallePedido> detallePedido,
			List<DetalleBajaInventario> detalleBajaInventario,
			List<DetalleIngreso> detalleIngresos,
			List<DetalleTraspaso> detalleTraspasos, List<Kardex> kardexs,
			List<ProductoUnidad> productoUnidads,
			List<TipoPrecioProducto> tipoPrecioProductos,
			List<ProductoTarifa> productosTarifas, Grupo grupo,
			TipoProducto tipoProducto) {
		this.id = id;
		this.activo = activo;
		this.kardex = kardex;
		this.cantidadMinima = cantidadMinima;
		this.codigo1 = codigo1;
		this.ean = ean;
		this.importado = importado;
		this.nombre = nombre;
		this.nombre = nombre;
		this.precio = precio;
		this.detalleFactura = detalleFactura;
		this.detalleCotizacion = detalleCotizacion;
		this.detallePedido = detallePedido;
		this.detalleBajaInventario = detalleBajaInventario;
		this.detalleIngresos = detalleIngresos;
		this.detalleTraspasos = detalleTraspasos;
		this.kardexs = kardexs;
		this.productoUnidads = productoUnidads;
		this.tipoPrecioProductos = tipoPrecioProductos;
		this.productosTarifas = productosTarifas;
		this.grupo = grupo;
		this.tipoProducto = tipoProducto;
	}

	public DetalleBajaInventario addDetalleBajaInventario(
			DetalleBajaInventario detalleBajaInventario) {
		getDetalleBajaInventario().add(detalleBajaInventario);
		detalleBajaInventario.setProducto(this);

		return detalleBajaInventario;
	}

	public DetalleCotizacion addDetalleCotizacion(
			DetalleCotizacion detalleCotizacion) {
		getDetalleCotizacion().add(detalleCotizacion);
		detalleCotizacion.setProducto(this);

		return detalleCotizacion;
	}

	public DetalleFactura addDetalleFactura(DetalleFactura detalleFactura) {
		getDetalleFactura().add(detalleFactura);
		detalleFactura.setProducto(this);

		return detalleFactura;
	}

	public DetalleIngreso addDetalleIngreso(DetalleIngreso detalleIngreso) {
		getDetalleIngresos().add(detalleIngreso);
		detalleIngreso.setProducto(this);

		return detalleIngreso;
	}

	public DetallePedido addDetallePedido(DetallePedido detallePedido) {
		getDetallePedido().add(detallePedido);
		detallePedido.setProducto(this);

		return detallePedido;
	}

	public DetalleTraspaso addDetalleTraspaso(DetalleTraspaso detalleTraspaso) {
		getDetalleTraspasos().add(detalleTraspaso);
		detalleTraspaso.setProducto(this);

		return detalleTraspaso;
	}

	public Kardex addKardex(Kardex kardex) {
		getKardexs().add(kardex);
		kardex.setProducto(this);

		return kardex;
	}

	public ProductoTarifa addProductoTarifa(ProductoTarifa productoTarifa) {
		getProductosTarifas().add(productoTarifa);
		productoTarifa.setProducto(this);

		return productoTarifa;
	}

	public ProductoUnidad addProductoUnidad(ProductoUnidad productoUnidad) {
		getProductoUnidads().add(productoUnidad);
		productoUnidad.setProducto(this);

		return productoUnidad;
	}

	public TipoPrecioProducto addTipoPrecioProducto(
			TipoPrecioProducto tipoPrecioProductos) {
		getTipoPrecioProductos().add(tipoPrecioProductos);
		tipoPrecioProductos.setProducto(this);

		return tipoPrecioProductos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return this.activo;
	}

	@Min(value = 1, message = "INGRESE LA CANTIDAD MÍNIMA")
	@Column(name = "cantidadminima", nullable = false)
	public Integer getCantidadMinima() {
		return cantidadMinima;
	}

	// @Pattern(regexp = "[0-9]{0,13}+", message =
	// "EL CAMPO CÓDIGO1 ACEPTA DÍGITOS NUMÉRICOS")
	@Column(nullable = false, length = 25)
	public String getCodigo1() {
		return this.codigo1;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<DetalleBajaInventario> getDetalleBajaInventario() {
		return detalleBajaInventario;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<DetalleCotizacion> getDetalleCotizacion() {
		return detalleCotizacion;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<DetalleFactura> getDetalleFactura() {
		return detalleFactura;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<DetalleIngreso> getDetalleIngresos() {
		return detalleIngresos;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<DetallePedido> getDetallePedido() {
		return detallePedido;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<DetalleTraspaso> getDetalleTraspasos() {
		return detalleTraspasos;
	}

	// @Pattern(regexp = "[0-9]{0,13}+", message =
	// "EL CAMPO EAN ACEPTA DÍGITOS NUMÉRICOS")
	@Column(nullable = false, length = 25)
	public String getEan() {
		return this.ean;
	}

	@ManyToOne
	@JoinColumn(name = "grupoid", nullable = false)
	public Grupo getGrupo() {
		return this.grupo;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PRODUCTO_PRODUCTOID_GENERATOR", sequenceName = "PRODUCTO_PRODUCTOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTO_PRODUCTOID_GENERATOR")
	@Column(name = "productoid", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@Column(nullable = false)
	public Boolean getImportado() {
		return importado;
	}

	@Column(nullable = false)
	public Boolean getFacturacion() {
		return facturacion;
	}

	public void setFacturacion(Boolean facturacion) {
		this.facturacion = facturacion;
	}

	@Column(nullable = false)
	public Boolean getKardex() {
		return kardex;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	public List<Kardex> getKardexs() {
		return this.kardexs;
	}

	@Column(nullable = false)
	@Type(type = "text")
	public String getNombre() {
		return nombre;
	}

	@DecimalMin(value = "0.00", message = "MINIMO 2 DECIMALES")
	@DecimalMax(value = "9999999999.9999999999", message = "MAXIMO 10 DECIMALES")
	@Column(nullable = false, precision = 12, scale = 6)
	public BigDecimal getPrecio() {
		return this.precio;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	@IndexColumn(name = "orden", base = 1)
	public List<ProductoTarifa> getProductosTarifas() {
		return productosTarifas;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	@IndexColumn(name = "orden", base = 1)
	public List<ProductoUnidad> getProductoUnidads() {
		return productoUnidads;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "producto")
	@IndexColumn(name = "orden", base = 1)
	public List<TipoPrecioProducto> getTipoPrecioProductos() {
		return this.tipoPrecioProductos;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoproductoid", nullable = false)
	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DetalleBajaInventario removeDetalleBajaInventario(
			DetalleBajaInventario detalleBajaInventario) {
		getDetalleBajaInventario().remove(detalleBajaInventario);
		detalleBajaInventario.setProducto(null);

		return detalleBajaInventario;
	}

	public DetalleCotizacion removeDetalleCotizacion(
			DetalleCotizacion detalleCotizacion) {
		getDetalleCotizacion().remove(detalleCotizacion);
		detalleCotizacion.setProducto(null);

		return detalleCotizacion;
	}

	public DetalleFactura removeDetalleFactura(DetalleFactura detalleFactura) {
		getDetalleFactura().remove(detalleFactura);
		detalleFactura.setProducto(null);

		return detalleFactura;
	}

	public DetalleIngreso removeDetalleIngreso(DetalleIngreso detalleIngreso) {
		getDetalleIngresos().remove(detalleIngreso);
		detalleIngreso.setProducto(null);

		return detalleIngreso;
	}

	public DetallePedido removeDetallePedido(DetallePedido detallePedido) {
		getDetallePedido().remove(detallePedido);
		detallePedido.setProducto(null);

		return detallePedido;
	}

	public DetalleTraspaso removeDetalleTraspaso(DetalleTraspaso detalleTraspaso) {
		getDetalleTraspasos().remove(detalleTraspaso);
		detalleTraspaso.setProducto(null);

		return detalleTraspaso;
	}

	public Kardex removeKardex(Kardex kardex) {
		getKardexs().remove(kardex);
		kardex.setProducto(null);

		return kardex;
	}

	public ProductoTarifa removeProductoTarifa(ProductoTarifa productoTarifa) {
		getProductosTarifas().remove(productoTarifa);
		productoTarifa.setProducto(null);

		return productoTarifa;
	}

	public ProductoUnidad removeProductoUnidad(ProductoUnidad productoUnidad) {
		getProductoUnidads().remove(productoUnidad);
		productoUnidad.setProducto(null);

		return productoUnidad;
	}

	public TipoPrecioProducto removeTipoPrecioProducto(
			TipoPrecioProducto tipoPrecioProductos) {
		getTipoPrecioProductos().remove(tipoPrecioProductos);
		tipoPrecioProductos.setProducto(null);

		return tipoPrecioProductos;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setCantidadMinima(Integer cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}

	public void setCodigo1(String codigo1) {
		this.codigo1 = codigo1;
	}

	public void setDetalleBajaInventario(
			List<DetalleBajaInventario> detalleBajaInventario) {
		this.detalleBajaInventario = detalleBajaInventario;
	}

	public void setDetalleCotizacion(List<DetalleCotizacion> detalleCotizacion) {
		this.detalleCotizacion = detalleCotizacion;
	}

	public void setDetalleFactura(List<DetalleFactura> detalleFactura) {
		this.detalleFactura = detalleFactura;
	}

	public void setDetalleIngresos(List<DetalleIngreso> detalleIngresos) {
		this.detalleIngresos = detalleIngresos;
	}

	public void setDetallePedido(List<DetallePedido> detallePedido) {
		this.detallePedido = detallePedido;
	}

	public void setDetalleTraspasos(List<DetalleTraspaso> detalleTraspasos) {
		this.detalleTraspasos = detalleTraspasos;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImportado(Boolean importado) {
		this.importado = importado;
	}

	public void setKardex(Boolean kardex) {
		this.kardex = kardex;
	}

	public void setKardexs(List<Kardex> kardexs) {
		this.kardexs = kardexs;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNombreImprimir(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public void setProductosTarifas(List<ProductoTarifa> productosTarifas) {
		this.productosTarifas = productosTarifas;
	}

	public void setProductoUnidads(List<ProductoUnidad> productoUnidads) {
		this.productoUnidads = productoUnidads;
	}

	public void setTipoPrecioProductos(
			List<TipoPrecioProducto> tipoPrecioProductos) {
		this.tipoPrecioProductos = tipoPrecioProductos;
	}

	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

}