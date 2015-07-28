package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class KardexReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private String detalle;
	private Integer cantidadEntrada;
	private BigDecimal precioEntrada;
	private BigDecimal totalEntrada;
	private Integer cantidadSalida;
	private BigDecimal precioSalida;
	private BigDecimal totalSalida;
	private Integer cantidadSaldo;
	private BigDecimal precioSaldo;
	private BigDecimal totalSaldo;
	private String nombreBodega;

	public KardexReporte() {
	}

	public Integer getCantidadEntrada() {
		return cantidadEntrada;
	}

	public Integer getCantidadSaldo() {
		return cantidadSaldo;
	}

	public Integer getCantidadSalida() {
		return cantidadSalida;
	}

	public String getDetalle() {
		return detalle;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getNombreBodega() {
		return nombreBodega;
	}

	public BigDecimal getPrecioEntrada() {
		return precioEntrada;
	}

	public BigDecimal getPrecioSaldo() {
		return precioSaldo;
	}

	public BigDecimal getPrecioSalida() {
		return precioSalida;
	}

	public BigDecimal getTotalEntrada() {
		return totalEntrada;
	}

	public BigDecimal getTotalSaldo() {
		return totalSaldo;
	}

	public BigDecimal getTotalSalida() {
		return totalSalida;
	}

	public void setCantidadEntrada(Integer cantidadEntrada) {
		this.cantidadEntrada = cantidadEntrada;
	}

	public void setCantidadSaldo(Integer cantidadSaldo) {
		this.cantidadSaldo = cantidadSaldo;
	}

	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setNombreBodega(String nombreBodega) {
		this.nombreBodega = nombreBodega;
	}

	public void setPrecioEntrada(BigDecimal precioEntrada) {
		this.precioEntrada = precioEntrada;
	}

	public void setPrecioSaldo(BigDecimal precioSaldo) {
		this.precioSaldo = precioSaldo;
	}

	public void setPrecioSalida(BigDecimal precioSalida) {
		this.precioSalida = precioSalida;
	}

	public void setTotalEntrada(BigDecimal totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public void setTotalSaldo(BigDecimal totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public void setTotalSalida(BigDecimal totalSalida) {
		this.totalSalida = totalSalida;
	}
}
