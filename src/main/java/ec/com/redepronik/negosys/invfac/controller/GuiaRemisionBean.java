package ec.com.redepronik.negosys.invfac.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleGuiaRemision;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.GuiaRemision;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.MotivoTraslado;
import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.invfac.service.GuiaRemisionService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Controller
@Scope("session")
public class GuiaRemisionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private GuiaRemisionService guiaRemisionService;

	@Autowired
	private FacturaService egresoService;

	@Autowired
	private LocalService localService;

	@Autowired
	private CiudadService ciudadService;

	// @Autowired
	// private GuiaRemisionReport guiaRemisionReport;

	@Autowired
	private EmpleadoService empleadoService;

	private List<Local> listaLocales;
	private List<Ciudad> listaCiudades;
	private List<MotivoTraslado> listaMotivotraslados;
	private List<PersonaCedulaNombre> listaTransportistas;

	private GuiaRemision guiaRemision;
	private Persona cliente;
	private EmpleadoCargo transportista;

	private List<Factura> listaFacturasGuiaRemision;
	private List<Factura> listaAuxFactura;
	private List<Factura> listaQuitarFacturasGuiaRemision;
	private List<DetalleGuiaRemision> listDetalleGuiaRemision;

	private List<Factura> listaFacturas;
	private String establecimiento;
	private int ciudadId;
	private String criterioBusquedaCodigo;

	private boolean bn = true;

	public GuiaRemisionBean() {
		limpiarObjetos();
	}

	public void cargarCliente() {
		guiaRemisionService.cargarCliente(listaFacturasGuiaRemision, cliente);
	}

	public void cargarDetalleGuiaRemision() {
		guiaRemisionService.cargarDetalleGuiaRemision(cliente,
				listaFacturasGuiaRemision, listaAuxFactura,
				listDetalleGuiaRemision);
	}

	public int getCiudadId() {
		return ciudadId;
	}

	public Persona getCliente() {
		return cliente;
	}

	public String getCriterioBusquedaCodigo() {
		return criterioBusquedaCodigo;
	}

	public GuiaRemision getGuiaRemision() {
		return guiaRemision;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public List<Factura> getListaFacturasGuiaRemision() {
		return listaFacturasGuiaRemision;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public MotivoTraslado[] getListaMotivoTranslado() {
		return MotivoTraslado.values();
	}

	public List<MotivoTraslado> getListaMotivotraslados() {
		return listaMotivotraslados;
	}

	public List<Factura> getListaQuitarFacturasGuiaRemision() {
		return listaQuitarFacturasGuiaRemision;
	}

	public List<PersonaCedulaNombre> getListaTransportistas() {
		return listaTransportistas;
	}

	public List<DetalleGuiaRemision> getListDetalleGuiaRemision() {
		return listDetalleGuiaRemision;
	}

	public EmpleadoCargo getTransportista() {
		return transportista;
	}

	public void imprimirGuiaRemision() {
		// guiaRemisionReport.reporteGiaRemision(guiaRemision);
		limpiarObjetos();
	}

	@PostConstruct
	public void init() {
		listaLocales = localService.obtenerPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 4);
		listaTransportistas = empleadoService.obtenerPorCargo(6);
		// listaMotivotraslados = motivoTrasladoService.obtener(true);
		listaCiudades = ciudadService.obtener(null);
	}

	public void insertar(ActionEvent actionEvent) {
		guiaRemisionService.insertar(guiaRemision, cliente, transportista,
				listDetalleGuiaRemision, listaFacturasGuiaRemision);
	}

	public boolean isBn() {
		return bn;
	}

	public void limpiarObjetos() {
		bn = true;

		listaFacturasGuiaRemision = new ArrayList<Factura>();
		listaAuxFactura = new ArrayList<Factura>();
		listaQuitarFacturasGuiaRemision = new ArrayList<Factura>();
		listDetalleGuiaRemision = new ArrayList<DetalleGuiaRemision>();
		guiaRemision = new GuiaRemision();
		transportista = new EmpleadoCargo();
		cliente = new Persona();

	}

	public void limpiarObjetosBusqueda() {
		establecimiento = "";
		ciudadId = 0;
		criterioBusquedaCodigo = new String();
		listaFacturas = new ArrayList<Factura>();
	}

	public void obtenerFacturas() {
		listaFacturas = egresoService
				.obtenerFacturasParaGuiaRemision(establecimiento, ciudadId,
						criterioBusquedaCodigo.toUpperCase());
	}

	public void quitarDetalleGuiaRemision() {
		guiaRemisionService.quitarDetalleGuiaRemision(listaAuxFactura,
				listaFacturasGuiaRemision, listaQuitarFacturasGuiaRemision,
				listDetalleGuiaRemision, cliente);
	}

	public void setBn(boolean bn) {
		this.bn = bn;
	}

	public void setCiudadId(int ciudadId) {
		this.ciudadId = ciudadId;
	}

	public void setCliente(Persona cliente) {
		this.cliente = cliente;
	}

	public void setCriterioBusquedaCodigo(String criterioBusquedaCodigo) {
		this.criterioBusquedaCodigo = criterioBusquedaCodigo;
	}

	public void setGuiaRemision(GuiaRemision guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setListaFacturasGuiaRemision(
			List<Factura> listaFacturasGuiaRemision) {
		this.listaFacturasGuiaRemision = listaFacturasGuiaRemision;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setListaMotivotraslados(
			List<MotivoTraslado> listaMotivotraslados) {
		this.listaMotivotraslados = listaMotivotraslados;
	}

	public void setListaQuitarFacturasGuiaRemision(
			List<Factura> listaQuitarFacturasGuiaRemision) {
		this.listaQuitarFacturasGuiaRemision = listaQuitarFacturasGuiaRemision;
	}

	public void setListaTransportistas(
			List<PersonaCedulaNombre> listaTransportistas) {
		this.listaTransportistas = listaTransportistas;
	}

	public void setListDetalleGuiaRemision(
			List<DetalleGuiaRemision> listDetalleGuiaRemision) {
		this.listDetalleGuiaRemision = listDetalleGuiaRemision;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setTransportista(EmpleadoCargo transportista) {
		this.transportista = transportista;
	}

}