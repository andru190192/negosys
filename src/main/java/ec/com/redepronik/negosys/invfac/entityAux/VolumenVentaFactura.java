package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VolumenVentaFactura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private Date fecha;
	private String codigoDocumento;
	private String cliente;
	private BigDecimal venta0;
	private BigDecimal venta12;
	private BigDecimal iva;
	private BigDecimal total;

	public VolumenVentaFactura(Date fecha, String codigoDocumento,
			String cliente, BigDecimal total) {
		this.fecha = fecha;
		this.codigoDocumento = codigoDocumento;
		this.cliente = cliente;
		this.total = total;
	}

	public VolumenVentaFactura(Integer id, Date fecha, String codigoDocumento,
			String cliente, BigDecimal venta0, BigDecimal venta12,
			BigDecimal iva) {
		this.id = id;
		this.fecha = fecha;
		this.codigoDocumento = codigoDocumento;
		this.cliente = cliente;
		this.venta0 = venta0;
		this.venta12 = venta12;
		this.iva = iva;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public String getCliente() {
		return cliente;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public Date getFecha() {
		return fecha;
	}

	public Integer getId() {
		return id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getVenta0() {
		return venta0;
	}

	public BigDecimal getVenta12() {
		return venta12;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setVenta0(BigDecimal venta0) {
		this.venta0 = venta0;
	}

	public void setVenta12(BigDecimal venta12) {
		this.venta12 = venta12;
	}

}