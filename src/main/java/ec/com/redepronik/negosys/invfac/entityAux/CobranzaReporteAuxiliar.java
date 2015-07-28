package ec.com.redepronik.negosys.invfac.entityAux;

import java.math.BigDecimal;

public class CobranzaReporteAuxiliar {

	private String fechaLimite;
	private BigDecimal saldo;
	private BigDecimal mora;
	private BigDecimal subTotal;

	public CobranzaReporteAuxiliar() {
	}

	public String getFechaLimite() {
		return fechaLimite;
	}

	public BigDecimal getMora() {
		return mora;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setFechaLimite(String fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public void setMora(BigDecimal mora) {
		this.mora = mora;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

}
