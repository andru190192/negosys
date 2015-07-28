package ec.com.redepronik.negosys.invfac.entityAux;

import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;

public class CantidadFactura implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal stSinImpuesto;
	private BigDecimal st12;
	private BigDecimal st0;
	private BigDecimal stNoObjetoIva;
	private BigDecimal stExentoIva;
	private BigDecimal tDescuentoProducto;
	private BigDecimal tDescuentoEgreso;
	private BigDecimal tDescuento;
	private BigDecimal valorIce;
	private BigDecimal valorIRBPNR;
	private BigDecimal iva12;
	private BigDecimal propina;
	private BigDecimal valorTotal;

	public CantidadFactura() {
		this.stSinImpuesto = newBigDecimal();
		this.st12 = newBigDecimal();
		this.st0 = newBigDecimal();
		this.stNoObjetoIva = newBigDecimal();
		this.stExentoIva = newBigDecimal();
		this.tDescuentoProducto = newBigDecimal();
		this.tDescuentoEgreso = newBigDecimal();
		this.tDescuento = newBigDecimal();
		this.valorIce = newBigDecimal();
		this.valorIRBPNR = newBigDecimal();
		this.iva12 = newBigDecimal();
		this.propina = newBigDecimal();
		this.valorTotal = newBigDecimal();
	}

	public CantidadFactura(BigDecimal stSinImpuesto, BigDecimal st12,
			BigDecimal st0, BigDecimal stNoObjetoIva, BigDecimal stExentoIva,
			BigDecimal tDescuentoProducto, BigDecimal tDescuentoEgreso,
			BigDecimal tDescuento, BigDecimal valorIce, BigDecimal valorIRBPNR,
			BigDecimal iva12, BigDecimal propina, BigDecimal valorTotal) {
		this.stSinImpuesto = stSinImpuesto;
		this.st12 = st12;
		this.st0 = st0;
		this.stNoObjetoIva = stNoObjetoIva;
		this.stExentoIva = stExentoIva;
		this.tDescuentoProducto = tDescuentoProducto;
		this.tDescuentoEgreso = tDescuentoEgreso;
		this.tDescuento = tDescuento;
		this.valorIce = valorIce;
		this.valorIRBPNR = valorIRBPNR;
		this.iva12 = iva12;
		this.propina = propina;
		this.valorTotal = valorTotal;
	}

	public BigDecimal getIva12() {
		return iva12;
	}

	public BigDecimal getPropina() {
		return propina;
	}

	public BigDecimal getSt0() {
		return st0;
	}

	public BigDecimal getSt12() {
		return st12;
	}

	public BigDecimal getStExentoIva() {
		return stExentoIva;
	}

	public BigDecimal getStNoObjetoIva() {
		return stNoObjetoIva;
	}

	public BigDecimal getStSinImpuesto() {
		return stSinImpuesto;
	}

	public BigDecimal gettDescuento() {
		return tDescuento;
	}

	public BigDecimal gettDescuentoEgreso() {
		return tDescuentoEgreso;
	}

	public BigDecimal gettDescuentoProducto() {
		return tDescuentoProducto;
	}

	public BigDecimal getValorIce() {
		return valorIce;
	}

	public BigDecimal getValorIRBPNR() {
		return valorIRBPNR;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setIva12(BigDecimal iva12) {
		this.iva12 = iva12;
	}

	public void setPropina(BigDecimal propina) {
		this.propina = propina;
	}

	public void setSt0(BigDecimal st0) {
		this.st0 = st0;
	}

	public void setSt12(BigDecimal st12) {
		this.st12 = st12;
	}

	public void setStExentoIva(BigDecimal stExentoIva) {
		this.stExentoIva = stExentoIva;
	}

	public void setStNoObjetoIva(BigDecimal stNoObjetoIva) {
		this.stNoObjetoIva = stNoObjetoIva;
	}

	public void setStSinImpuesto(BigDecimal stSinImpuesto) {
		this.stSinImpuesto = stSinImpuesto;
	}

	public void settDescuento(BigDecimal tDescuento) {
		this.tDescuento = tDescuento;
	}

	public void settDescuentoEgreso(BigDecimal tDescuentoEgreso) {
		this.tDescuentoEgreso = tDescuentoEgreso;
	}

	public void settDescuentoProducto(BigDecimal tDescuentoProducto) {
		this.tDescuentoProducto = tDescuentoProducto;
	}

	public void setValorIce(BigDecimal valorIce) {
		this.valorIce = valorIce;
	}

	public void setValorIRBPNR(BigDecimal valorIRBPNR) {
		this.valorIRBPNR = valorIRBPNR;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

}