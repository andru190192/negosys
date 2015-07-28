package ec.com.redepronik.negosys.invfac.entityAux;

import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;

public class CantidadesCreditoReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal saldoCredito;
	private BigDecimal moraCredito;
	private BigDecimal totalCredito;

	public CantidadesCreditoReporte() {
		saldoCredito = newBigDecimal();
		moraCredito = newBigDecimal();
		totalCredito = newBigDecimal();
	}

	public BigDecimal getMoraCredito() {
		return moraCredito;
	}

	public BigDecimal getSaldoCredito() {
		return saldoCredito;
	}

	public BigDecimal getTotalCredito() {
		return totalCredito;
	}

	public void setMoraCredito(BigDecimal moraCredito) {
		this.moraCredito = moraCredito;
	}

	public void setSaldoCredito(BigDecimal saldoCredito) {
		this.saldoCredito = saldoCredito;
	}

	public void setTotalCredito(BigDecimal totalCredito) {
		this.totalCredito = totalCredito;
	}

}