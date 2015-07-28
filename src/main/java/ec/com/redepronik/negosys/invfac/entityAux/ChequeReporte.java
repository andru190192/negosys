package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChequeReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String banco;
	private String numeroCuenta;
	private String numeroCheque;
	private Date fechaGiro;
	private Date fechaCobro;
	private boolean anulado;
	private String detalle;
	private BigDecimal cuota;
	private Date fechaPago;
	private boolean pagado;
	private String tabla;

	public ChequeReporte() {
	}

	public ChequeReporte(Integer id, String banco, String numeroCuenta,
			String numeroCheque, Date fechaGiro, Date fechaCobro,
			boolean anulado, String detalle, BigDecimal cuota, Date fechaPago,
			boolean pagado, String tabla) {
		this.id = id;
		this.banco = banco;
		this.numeroCuenta = numeroCuenta;
		this.numeroCheque = numeroCheque;
		this.fechaGiro = fechaGiro;
		this.fechaCobro = fechaCobro;
		this.anulado = anulado;
		this.detalle = detalle;
		this.cuota = cuota;
		this.fechaPago = fechaPago;
		this.pagado = pagado;
		this.tabla = tabla;
	}

	public String getBanco() {
		return banco;
	}

	public BigDecimal getCuota() {
		return cuota;
	}

	public String getDetalle() {
		return detalle;
	}

	public Date getFechaCobro() {
		return fechaCobro;
	}

	public Date getFechaGiro() {
		return fechaGiro;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public Integer getId() {
		return id;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public String getTabla() {
		return tabla;
	}

	public boolean isAnulado() {
		return anulado;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public void setFechaCobro(Date fechaCobro) {
		this.fechaCobro = fechaCobro;
	}

	public void setFechaGiro(Date fechaGiro) {
		this.fechaGiro = fechaGiro;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

}