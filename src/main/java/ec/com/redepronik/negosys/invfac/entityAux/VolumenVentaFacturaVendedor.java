package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class VolumenVentaFacturaVendedor implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombreVendedor;
	private BigDecimal total;
	private List<VolumenVentaFactura> listVolumenVentaFactura;

	public VolumenVentaFacturaVendedor(String nombreVendedor, BigDecimal total,
			List<VolumenVentaFactura> listVolumenVentaFactura) {
		this.nombreVendedor = nombreVendedor;
		this.total = total;
		this.listVolumenVentaFactura = listVolumenVentaFactura;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<VolumenVentaFactura> getListVolumenVentaFactura() {
		return listVolumenVentaFactura;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setListVolumenVentaFactura(
			List<VolumenVentaFactura> listVolumenVentaFactura) {
		this.listVolumenVentaFactura = listVolumenVentaFactura;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

}