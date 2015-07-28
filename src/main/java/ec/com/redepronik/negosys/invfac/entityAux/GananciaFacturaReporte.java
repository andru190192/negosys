package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GananciaFacturaReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String codigoDocumento;
	private Date fechaFactura;
	private Date fechaPago;
	private BigDecimal precioVenta;
	private BigDecimal precioCosto;
	private BigDecimal descuentoProducto;
	private BigDecimal ganancia;

	public GananciaFacturaReporte() {

	}

	public GananciaFacturaReporte(String codigoDocumento, Date fechaFactura,
			Date fechaPago, BigDecimal precioVenta, BigDecimal precioCosto,
			BigDecimal descuentoProducto, BigDecimal ganancia) {
		this.codigoDocumento = codigoDocumento;
		this.fechaFactura = fechaFactura;
		this.fechaPago = fechaPago;
		this.precioVenta = precioVenta;
		this.precioCosto = precioCosto;
		this.descuentoProducto = descuentoProducto;
		this.ganancia = ganancia;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public BigDecimal getDescuentoProducto() {
		return descuentoProducto;
	}

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public BigDecimal getGanancia() {
		return ganancia;
	}

	public BigDecimal getPrecioCosto() {
		return precioCosto;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public void setDescuentoProducto(BigDecimal descuentoProducto) {
		this.descuentoProducto = descuentoProducto;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setGanancia(BigDecimal ganancia) {
		this.ganancia = ganancia;
	}

	public void setPrecioCosto(BigDecimal precioCosto) {
		this.precioCosto = precioCosto;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

}
