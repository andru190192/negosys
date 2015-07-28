package ec.com.redepronik.negosys.invfac.entityAux;

import java.math.BigDecimal;
import java.util.Date;

public class EntradaAntiguaReporte {

	private Date fecha;

	private Date fechaPago;

	private BigDecimal cuota;

	private BigDecimal mora;

	private BigDecimal cuotaMora;

	private BigDecimal totalCuota;

	private String tipoPago;

	private boolean pagado;

	public BigDecimal getCuota() {
		return cuota;
	}

	public BigDecimal getCuotaMora() {
		return cuotaMora;
	}

	public Date getFecha() {
		return fecha;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public BigDecimal getMora() {
		return mora;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public BigDecimal getTotalCuota() {
		return totalCuota;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public void setCuotaMora(BigDecimal cuotaMora) {
		this.cuotaMora = cuotaMora;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setMora(BigDecimal mora) {
		this.mora = mora;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public void setTotalCuota(BigDecimal totalCuota) {
		this.totalCuota = totalCuota;
	}

}
