package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.Gastos;

public class GastosAux implements Serializable {

	private static final long serialVersionUID = 1L;

	private Gastos gastos;
	private BigDecimal ivaPrecioUnitario;
	private BigDecimal totalPrecioUnitario;
	private BigDecimal subtotalFacturado;
	private BigDecimal iva;
	private BigDecimal totalFacturado;
	private BigDecimal valorRetencion;
	private BigDecimal valorRetencionIva;
	private BigDecimal totalPagar;

	public GastosAux() {
	}

	public GastosAux(Gastos gastos, BigDecimal ivaPrecioUnitario,
			BigDecimal totalPrecioUnitario, BigDecimal subtotalFacturado,
			BigDecimal iva, BigDecimal totalFacturado,
			BigDecimal valorRetencion, BigDecimal valorRetencionIva,
			BigDecimal totalPagar, Boolean activo) {
		this.gastos = gastos;
		this.ivaPrecioUnitario = ivaPrecioUnitario;
		this.totalPrecioUnitario = totalPrecioUnitario;
		this.subtotalFacturado = subtotalFacturado;
		this.iva = iva;
		this.totalFacturado = totalFacturado;
		this.valorRetencion = valorRetencion;
		this.valorRetencionIva = valorRetencionIva;
		this.totalPagar = totalPagar;
	}

	public Gastos getGastos() {
		return gastos;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public BigDecimal getIvaPrecioUnitario() {
		return ivaPrecioUnitario;
	}

	public BigDecimal getSubtotalFacturado() {
		return subtotalFacturado;
	}

	public BigDecimal getTotalFacturado() {
		return totalFacturado;
	}

	public BigDecimal getTotalPagar() {
		return totalPagar;
	}

	public BigDecimal getTotalPrecioUnitario() {
		return totalPrecioUnitario;
	}

	public BigDecimal getValorRetencion() {
		return valorRetencion;
	}

	public BigDecimal getValorRetencionIva() {
		return valorRetencionIva;
	}

	public void setGastos(Gastos gastos) {
		this.gastos = gastos;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public void setIvaPrecioUnitario(BigDecimal ivaPrecioUnitario) {
		this.ivaPrecioUnitario = ivaPrecioUnitario;
	}

	public void setSubtotalFacturado(BigDecimal subtotalFacturado) {
		this.subtotalFacturado = subtotalFacturado;
	}

	public void setTotalFacturado(BigDecimal totalFacturado) {
		this.totalFacturado = totalFacturado;
	}

	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}

	public void setTotalPrecioUnitario(BigDecimal totalPrecioUnitario) {
		this.totalPrecioUnitario = totalPrecioUnitario;
	}

	public void setValorRetencion(BigDecimal valorRetencion) {
		this.valorRetencion = valorRetencion;
	}

	public void setValorRetencionIva(BigDecimal valorRetencionIva) {
		this.valorRetencionIva = valorRetencionIva;
	}

}