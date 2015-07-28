package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

public class NumeroCuotasReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cuotasTotalCredito;
	private int cuotasPagadasCredito;
	private int cuotasPorPagarCredito;
	private int cuotasVencidasCredito;
	private int cuotasNormalesCredito;

	private int cuotasTotalEntrada;
	private int cuotasPagadasEntrada;
	private int cuotasPorPagarEntrada;
	private int cuotasVencidasEntrada;
	private int cuotasNormalesEntrada;

	public NumeroCuotasReporte() {
		cuotasTotalEntrada = 0;
		cuotasPagadasEntrada = 0;
		cuotasPorPagarEntrada = 0;
		cuotasVencidasEntrada = 0;
		cuotasNormalesEntrada = 0;

		cuotasTotalCredito = 0;
		cuotasPagadasCredito = 0;
		cuotasPorPagarCredito = 0;
		cuotasVencidasCredito = 0;
		cuotasNormalesCredito = 0;
	}

	public int getCuotasNormalesCredito() {
		return cuotasNormalesCredito;
	}

	public int getCuotasNormalesEntrada() {
		return cuotasNormalesEntrada;
	}

	public int getCuotasPagadasCredito() {
		return cuotasPagadasCredito;
	}

	public int getCuotasPagadasEntrada() {
		return cuotasPagadasEntrada;
	}

	public int getCuotasPorPagarCredito() {
		return cuotasPorPagarCredito;
	}

	public int getCuotasPorPagarEntrada() {
		return cuotasPorPagarEntrada;
	}

	public int getCuotasTotalCredito() {
		return cuotasTotalCredito;
	}

	public int getCuotasTotalEntrada() {
		return cuotasTotalEntrada;
	}

	public int getCuotasVencidasCredito() {
		return cuotasVencidasCredito;
	}

	public int getCuotasVencidasEntrada() {
		return cuotasVencidasEntrada;
	}

	public void setCuotasNormalesCredito(int cuotasNormalesCredito) {
		this.cuotasNormalesCredito = cuotasNormalesCredito;
	}

	public void setCuotasNormalesEntrada(int cuotasNormalesEntrada) {
		this.cuotasNormalesEntrada = cuotasNormalesEntrada;
	}

	public void setCuotasPagadasCredito(int cuotasPagadasCredito) {
		this.cuotasPagadasCredito = cuotasPagadasCredito;
	}

	public void setCuotasPagadasEntrada(int cuotasPagadasEntrada) {
		this.cuotasPagadasEntrada = cuotasPagadasEntrada;
	}

	public void setCuotasPorPagarCredito(int cuotasPorPagarCredito) {
		this.cuotasPorPagarCredito = cuotasPorPagarCredito;
	}

	public void setCuotasPorPagarEntrada(int cuotasPorPagarEntrada) {
		this.cuotasPorPagarEntrada = cuotasPorPagarEntrada;
	}

	public void setCuotasTotalCredito(int cuotasTotalCredito) {
		this.cuotasTotalCredito = cuotasTotalCredito;
	}

	public void setCuotasTotalEntrada(int cuotasTotalEntrada) {
		this.cuotasTotalEntrada = cuotasTotalEntrada;
	}

	public void setCuotasVencidasCredito(int cuotasVencidasCredito) {
		this.cuotasVencidasCredito = cuotasVencidasCredito;
	}

	public void setCuotasVencidasEntrada(int cuotasVencidasEntrada) {
		this.cuotasVencidasEntrada = cuotasVencidasEntrada;
	}

}