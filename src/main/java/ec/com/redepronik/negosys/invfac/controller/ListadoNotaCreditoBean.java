package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleNotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.report.InventarioReportes;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.invfac.service.NotaCreditoService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;

@Controller
@Scope("session")
public class ListadoNotaCreditoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private NotaCreditoService notaCreditoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private InventarioReportes notaCreditoReport;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private List<Factura> listaFacturas;
	private String criterioBusquedaCliente;
	private String criterioBusquedaCodigo;
	private String criterioBusquedaDetalle;
	private Date criterioBusquedaFechaDocumento;

	private List<FacturaReporte> listaNotaCreditosDetalle;

	private NotaCredito notaCredito;
	private Factura factura;

	public ListadoNotaCreditoBean() {
		notaCredito = new NotaCredito();
	}

	public void generarXML() {
		if (SecurityContextHolder.getContext().getAuthentication().getName()
				.compareTo("0123456789") == 0) {
			CantidadFactura cantidadFactura = new CantidadFactura();
			for (DetalleNotaCredito dnc : factura.getNotaCredito()
					.getDetalleNotaCredito())
				cantidadFactura = notaCreditoService.calcularCantidadFactura(
						cantidadFactura, notaCreditoService.asignar(dnc));

			notaCreditoService.redondearCantidadFactura(cantidadFactura);

			documentosElectronicosService.generarNotaCreditoXML(factura
					.getNotaCredito().getId(), cantidadFactura);
		}
	}

	public void generarListaDetalle() {
		notaCredito = factura.getNotaCredito();
		listaNotaCreditosDetalle = new ArrayList<FacturaReporte>();
		for (DetalleNotaCredito de : notaCredito.getDetalleNotaCredito())
			listaNotaCreditosDetalle.add(notaCreditoService.asignar(de));
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

	public Factura getFactura() {
		return factura;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public List<FacturaReporte> getListaNotaCreditosDetalle() {
		return listaNotaCreditosDetalle;
	}

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	// @PostConstruct
	// public void init() {
	// for (Factura factura : notaCreditoService.obtenerTodos()) {
	// CantidadFactura cantidadFactura = new CantidadFactura();
	// for (DetalleNotaCredito dnc : factura.getNotaCredito()
	// .getDetalleNotaCredito())
	// cantidadFactura = notaCreditoService.calcularCantidadFactura(
	// cantidadFactura, notaCreditoService.asignar(dnc));
	// notaCreditoService.redondearCantidadFactura(cantidadFactura);
	//
	// documentosElectronicosService.generarNotaCreditoXML(factura
	// .getNotaCredito().getId(), cantidadFactura);
	// }
	// }

	public void imprimiNotaCredito() {
		CantidadFactura cantidadFactura = new CantidadFactura();
		List<FacturaReporte> listFacturaReporte = new ArrayList<FacturaReporte>();
		for (DetalleNotaCredito detalleNotaCredito : notaCredito
				.getDetalleNotaCredito()) {
			FacturaReporte fr = notaCreditoService.asignar(detalleNotaCredito);
			cantidadFactura = notaCreditoService.calcularCantidadFactura(
					cantidadFactura, fr);
			listFacturaReporte.add(fr);
		}
	}

	public void limpiarObjetos() {
		criterioBusquedaCliente = new String();
		criterioBusquedaCodigo = new String();
		criterioBusquedaDetalle = new String();
		criterioBusquedaFechaDocumento = null;
	}

	public void obtener() {
		listaFacturas = notaCreditoService.obtener(0, criterioBusquedaCliente,
				criterioBusquedaCodigo, criterioBusquedaDetalle,
				criterioBusquedaFechaDocumento);
		if (listaFacturas == null)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO SE ENCONTRARON COINCIDENCIAS");

		limpiarObjetos();
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

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setListaNotaCreditosDetalle(
			List<FacturaReporte> listaNotaCreditosDetalle) {
		this.listaNotaCreditosDetalle = listaNotaCreditosDetalle;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}
}