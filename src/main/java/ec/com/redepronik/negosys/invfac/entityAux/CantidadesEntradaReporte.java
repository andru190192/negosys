package ec.com.redepronik.negosys.invfac.entityAux;

import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;

public class CantidadesEntradaReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal saldoEntrada;
	private BigDecimal moraEntrada;
	private BigDecimal totalEntrada;

	public CantidadesEntradaReporte() {
		saldoEntrada = newBigDecimal();
		moraEntrada = newBigDecimal();
		totalEntrada = newBigDecimal();
	}

	public BigDecimal getMoraEntrada() {
		return moraEntrada;
	}

	public BigDecimal getSaldoEntrada() {
		return saldoEntrada;
	}

	public BigDecimal getTotalEntrada() {
		return totalEntrada;
	}

	public void setMoraEntrada(BigDecimal moraEntrada) {
		this.moraEntrada = moraEntrada;
	}

	public void setSaldoEntrada(BigDecimal saldoEntrada) {
		this.saldoEntrada = saldoEntrada;
	}

	public void setTotalEntrada(BigDecimal totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

}