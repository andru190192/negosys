package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;

public class GananciaFab implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal totalIngreso;
	private BigDecimal totalEgreso;

	public GananciaFab(BigDecimal totalIngreso, BigDecimal totalEgreso) {
		this.totalIngreso = totalIngreso;
		this.totalEgreso = totalEgreso;
	}

	public BigDecimal getTotalEgreso() {
		return totalEgreso;
	}

	public BigDecimal getTotalIngreso() {
		return totalIngreso;
	}

	public void setTotalEgreso(BigDecimal totalEgreso) {
		this.totalEgreso = totalEgreso;
	}

	public void setTotalIngreso(BigDecimal totalIngreso) {
		this.totalIngreso = totalIngreso;
	}

}