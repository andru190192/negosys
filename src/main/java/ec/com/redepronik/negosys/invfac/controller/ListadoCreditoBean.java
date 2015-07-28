package ec.com.redepronik.negosys.invfac.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Credito;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.report.InventarioReportes;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;

@Controller
@Scope("session")
public class ListadoCreditoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacturaBean facturaBean;

	@Autowired
	private FacturaService egresoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private InventarioReportes egresoReport;

	@Autowired
	private PersonaService personaService;

	private List<Credito> listaCredito;
	private String criterioBusquedaCliente;
	private String criterioBusquedaCodigo;
	private String criterioBusquedaDetalle;
	private Date criterioBusquedaFechaDocumento;
	private List<FacturaReporte> listaEgresosDetalle;
	private Credito credito;

	public ListadoCreditoBean() {
		credito = new Credito();
	}

	public Credito getCredito() {
		return credito;
	}

	public String getCriterioBusquedaCliente() {
		return criterioBusquedaCliente;
	}

	public String getCriterioBusquedaCodigo() {
		return criterioBusquedaCodigo;
	}

	public String getCriterioBusquedaDetalle() {
		return criterioBusquedaDetalle;
	}

	public Date getCriterioBusquedaFechaDocumento() {
		return criterioBusquedaFechaDocumento;
	}

	public List<Credito> getListaCredito() {
		return listaCredito;
	}

	public List<FacturaReporte> getListaEgresosDetalle() {
		return listaEgresosDetalle;
	}

	public void limpiarObjetos() {
		criterioBusquedaCliente = new String();
		criterioBusquedaCodigo = new String();
		criterioBusquedaDetalle = new String();
		criterioBusquedaFechaDocumento = null;
	}

	public void obtener() {
		limpiarObjetos();
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}

	public void setCriterioBusquedaCliente(String criterioBusquedaCliente) {
		this.criterioBusquedaCliente = criterioBusquedaCliente;
	}

	public void setCriterioBusquedaCodigo(String criterioBusquedaCodigo) {
		this.criterioBusquedaCodigo = criterioBusquedaCodigo;
	}

	public void setCriterioBusquedaDetalle(String criterioBusquedaDetalle) {
		this.criterioBusquedaDetalle = criterioBusquedaDetalle;
	}

	public void setCriterioBusquedaFechaDocumento(
			Date criterioBusquedaFechaDocumento) {
		this.criterioBusquedaFechaDocumento = criterioBusquedaFechaDocumento;
	}

	public void setListaCredito(List<Credito> listaCredito) {
		this.listaCredito = listaCredito;
	}

	public void setListaEgresosDetalle(List<FacturaReporte> listaEgresosDetalle) {
		this.listaEgresosDetalle = listaEgresosDetalle;
	}

}