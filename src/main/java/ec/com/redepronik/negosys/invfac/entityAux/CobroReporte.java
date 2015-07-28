package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CobroReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nombre;
	private Integer orden;
	private Boolean escogido;
	private String tipoDocumento;
	private String codigoDocumento;
	private Date fechaEmision;
	private BigDecimal total;
	private BigDecimal abono;
	private BigDecimal saldo;

	public CobroReporte() {

	}

	public CobroReporte(Integer id, String nombre, Integer orden,
			Boolean escogido, String tipoDocumento, String codigoDocumento,
			Date fechaEmision, BigDecimal total, BigDecimal abono,
			BigDecimal saldo) {
		this.id = id;
		this.nombre = nombre;
		this.orden = orden;
		this.escogido = escogido;
		this.tipoDocumento = tipoDocumento;
		this.codigoDocumento = codigoDocumento;
		this.fechaEmision = fechaEmision;
		this.total = total;
		this.abono = abono;
		this.saldo = saldo;
	}

	public BigDecimal getAbono() {
		return abono;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public Boolean getEscogido() {
		return escogido;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getOrden() {
		return orden;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public void setEscogido(Boolean escogido) {
		this.escogido = escogido;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
