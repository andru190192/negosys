package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CierreCajaCajero implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombreCajero;
	private List<IngresoCaja> listIngresoCaja;
	private List<EgresoCaja> listEgresoCaja;
	private BigDecimal totalCajero;

	public CierreCajaCajero(String nombreCajero,
			List<IngresoCaja> listIngresoCaja, List<EgresoCaja> listEgresoCaja,
			BigDecimal totalCajero) {
		this.nombreCajero = nombreCajero;
		this.listIngresoCaja = listIngresoCaja;
		this.listEgresoCaja = listEgresoCaja;
		this.setTotalCajero(totalCajero);
	}

	public List<EgresoCaja> getListEgresoCaja() {
		return listEgresoCaja;
	}

	public List<IngresoCaja> getListIngresoCaja() {
		return listIngresoCaja;
	}

	public String getNombreCajero() {
		return nombreCajero;
	}

	public BigDecimal getTotalCajero() {
		return totalCajero;
	}

	public void setListEgresoCaja(List<EgresoCaja> listEgresoCaja) {
		this.listEgresoCaja = listEgresoCaja;
	}

	public void setListIngresoCaja(List<IngresoCaja> listIngresoCaja) {
		this.listIngresoCaja = listIngresoCaja;
	}

	public void setNombreCajero(String nombreCajero) {
		this.nombreCajero = nombreCajero;
	}

	public void setTotalCajero(BigDecimal totalCajero) {
		this.totalCajero = totalCajero;
	}

}