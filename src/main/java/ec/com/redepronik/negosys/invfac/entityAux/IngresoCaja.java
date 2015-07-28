package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import ec.com.redepronik.negosys.invfac.entity.TipoPago;

@Entity
public class IngresoCaja implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Date fechaFactura;
	private Date fechaPago;
	private String codigoDocumento;
	private String cliente;
	private BigDecimal totalFactura;
	private BigDecimal pago;
	private TipoPago tipoPago;

	public IngresoCaja() {
	}

	public IngresoCaja(Integer id, Date fechaFactura, Date fechaPago,
			String codigoDocumento, String cliente, BigDecimal totalFactura,
			BigDecimal pago, TipoPago tipoPago) {
		this.id = id;
		this.fechaFactura = fechaFactura;
		this.fechaPago = fechaPago;
		this.codigoDocumento = codigoDocumento;
		this.cliente = cliente;
		this.totalFactura = totalFactura;
		this.pago = pago;
		this.tipoPago = tipoPago;
	}

	public String getCliente() {
		return cliente;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public Integer getId() {
		return id;
	}

	public BigDecimal getPago() {
		return pago;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public BigDecimal getTotalFactura() {
		return totalFactura;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPago(BigDecimal pago) {
		this.pago = pago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public void setTotalFactura(BigDecimal totalFactura) {
		this.totalFactura = totalFactura;
	}

}