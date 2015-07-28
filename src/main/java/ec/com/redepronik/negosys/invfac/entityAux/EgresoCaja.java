package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EgresoCaja implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private Date fecha;
	private String codigoDocumentoNC;
	private String codigoDocumentoF;
	private String cliente;
	private BigDecimal totalFactura;
	private BigDecimal pago;

	public EgresoCaja() {
	}

	public EgresoCaja(Integer id, Date fecha, String codigoDocumentoNC,
			String codigoDocumentoF, String cliente, BigDecimal totalFactura,
			BigDecimal pago) {
		this.id = id;
		this.fecha = fecha;
		this.codigoDocumentoNC = codigoDocumentoNC;
		this.codigoDocumentoF = codigoDocumentoF;
		this.cliente = cliente;
		this.totalFactura = totalFactura;
		this.pago = pago;
	}

	public String getCliente() {
		return cliente;
	}

	public String getCodigoDocumentoF() {
		return codigoDocumentoF;
	}

	public String getCodigoDocumentoNC() {
		return codigoDocumentoNC;
	}

	public Date getFecha() {
		return fecha;
	}

	public Integer getId() {
		return id;
	}

	public BigDecimal getPago() {
		return pago;
	}

	public BigDecimal getTotalFactura() {
		return totalFactura;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setCodigoDocumentoF(String codigoDocumentoF) {
		this.codigoDocumentoF = codigoDocumentoF;
	}

	public void setCodigoDocumentoNC(String codigoDocumentoNC) {
		this.codigoDocumentoNC = codigoDocumentoNC;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPago(BigDecimal pago) {
		this.pago = pago;
	}

	public void setTotalFactura(BigDecimal totalFactura) {
		this.totalFactura = totalFactura;
	}

}