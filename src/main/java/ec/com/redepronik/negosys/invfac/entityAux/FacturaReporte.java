package ec.com.redepronik.negosys.invfac.entityAux;

import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Producto;

public class FacturaReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Producto producto;
	private String codigo;
	private Integer precioId;
	private String impuesto;
	private String descripcion;
	private Integer cantidad;
	private Integer cantidadKardex;
	private Integer cantidadCuadre;
	private String nombreCantidad;
	private EstadoProductoVenta estado;
	private BigDecimal precioCosto;
	private BigDecimal precioUnitVenta;
	private BigDecimal precioUnitVentaCuadre;
	private BigDecimal precioUnitVentaImpuesto;
	private BigDecimal descuentoDolares;
	private BigDecimal descuentoDolaresCuadre;
	private BigDecimal descuentoPorcentaje;
	private BigDecimal importe;
	private BigDecimal importeImpuesto;

	public FacturaReporte() {
		this.producto = new Producto();
		this.precioId = 0;
		this.setCodigo("");
		this.impuesto = "";
		this.descripcion = "";
		this.cantidad = 0;
		this.cantidadKardex = 0;
		this.cantidadCuadre = 0;
		this.nombreCantidad = "";
		this.estado = EstadoProductoVenta.NR;
		this.precioUnitVenta = newBigDecimal();
		this.precioCosto = newBigDecimal();
		this.precioUnitVentaCuadre = newBigDecimal();
		this.precioUnitVentaImpuesto = newBigDecimal();
		this.descuentoDolares = newBigDecimal();
		this.descuentoDolaresCuadre = newBigDecimal();
		this.descuentoPorcentaje = newBigDecimal();
		this.importe = newBigDecimal();
		this.importeImpuesto = newBigDecimal();
	}

	public FacturaReporte(String codigo) {
		this.codigo = codigo;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public Integer getCantidadCuadre() {
		return cantidadCuadre;
	}

	public Integer getCantidadKardex() {
		return cantidadKardex;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public BigDecimal getDescuentoDolares() {
		return descuentoDolares;
	}

	public BigDecimal getDescuentoDolaresCuadre() {
		return descuentoDolaresCuadre;
	}

	public BigDecimal getDescuentoPorcentaje() {
		return descuentoPorcentaje;
	}

	public EstadoProductoVenta getEstado() {
		return estado;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public BigDecimal getImporteImpuesto() {
		return importeImpuesto;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public String getNombreCantidad() {
		return nombreCantidad;
	}

	public BigDecimal getPrecioCosto() {
		return precioCosto;
	}

	public Integer getPrecioId() {
		return precioId;
	}

	public BigDecimal getPrecioUnitVenta() {
		return precioUnitVenta;
	}

	public BigDecimal getPrecioUnitVentaCuadre() {
		return precioUnitVentaCuadre;
	}

	public BigDecimal getPrecioUnitVentaImpuesto() {
		return precioUnitVentaImpuesto;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public void setCantidadCuadre(Integer cantidadCuadre) {
		this.cantidadCuadre = cantidadCuadre;
	}

	public void setCantidadKardex(Integer cantidadKardex) {
		this.cantidadKardex = cantidadKardex;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setDescuentoDolares(BigDecimal descuentoDolares) {
		this.descuentoDolares = descuentoDolares;
	}

	public void setDescuentoDolaresCuadre(BigDecimal descuentoDolaresCuadre) {
		this.descuentoDolaresCuadre = descuentoDolaresCuadre;
	}

	public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) {
		this.descuentoPorcentaje = descuentoPorcentaje;
	}

	public void setEstado(EstadoProductoVenta estado) {
		this.estado = estado;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public void setImporteImpuesto(BigDecimal importeImpuesto) {
		this.importeImpuesto = importeImpuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public void setNombreCantidad(String nombreCantidad) {
		this.nombreCantidad = nombreCantidad;
	}

	public void setPrecioCosto(BigDecimal precioCosto) {
		this.precioCosto = precioCosto;
	}

	public void setPrecioId(Integer precioId) {
		this.precioId = precioId;
	}

	public void setPrecioUnitVenta(BigDecimal precioUnitVenta) {
		this.precioUnitVenta = precioUnitVenta;
	}

	public void setPrecioUnitVentaCuadre(BigDecimal precioUnitVentaCuadre) {
		this.precioUnitVentaCuadre = precioUnitVentaCuadre;
	}

	public void setPrecioUnitVentaImpuesto(BigDecimal precioUnitVentaImpuesto) {
		this.precioUnitVentaImpuesto = precioUnitVentaImpuesto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
