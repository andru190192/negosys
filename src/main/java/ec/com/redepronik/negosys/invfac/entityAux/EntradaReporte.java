package ec.com.redepronik.negosys.invfac.entityAux;

import java.math.BigDecimal;
import java.util.Date;

public class EntradaReporte {

	private Date fecha;
	private BigDecimal valor;
	private String tipoPago;

	public EntradaReporte() {

	}

	public EntradaReporte(Date fecha, BigDecimal valor, String tipoPago) {
		this.fecha = fecha;
		this.valor = valor;
		this.tipoPago = tipoPago;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
