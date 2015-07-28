package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CobroFacturaReporteAuxiliar implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private String codigo;
	private BigDecimal total;

	public CobroFacturaReporteAuxiliar() {

	}

	public CobroFacturaReporteAuxiliar(Date fecha, String codigo,
			BigDecimal total) {
		this.fecha = fecha;
		this.codigo = codigo;
		this.total = total;
	}

	public String getCodigo() {
		return codigo;
	}

	public Date getFecha() {
		return fecha;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
