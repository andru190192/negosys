package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.util.List;

public class CobroFacturaReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private String folio;
	private String nombres;
	private String direccion;
	private String referencia;
	private List<CobroFacturaReporteAuxiliar> listaFactura;

	public CobroFacturaReporte() {

	}

	public CobroFacturaReporte(String folio, String nombres, String direccion,
			String referencia, List<CobroFacturaReporteAuxiliar> listaFactura) {
		this.folio = folio;
		this.nombres = nombres;
		this.direccion = direccion;
		this.referencia = referencia;
		this.listaFactura = listaFactura;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getFolio() {
		return folio;
	}

	public List<CobroFacturaReporteAuxiliar> getListaFactura() {
		return listaFactura;
	}

	public String getNombres() {
		return nombres;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public void setListaFactura(List<CobroFacturaReporteAuxiliar> listaFactura) {
		this.listaFactura = listaFactura;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

}
