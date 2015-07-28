package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.service.FacturaInternaService;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

@Controller
@Scope("session")
public class FacturaInternaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacturaInternaService facturaInternaService;

	@Autowired
	private FacturaService facturaService;

	private Factura factura;
	private Persona cliente;

	private List<FacturaReporte> listaFacturaReporte;
	private CantidadFactura cantidadFactura;

	public FacturaInternaBean() {
		limpiarFactura();
	}

	public void actualizar(ActionEvent actionEvent) {
		facturaInternaService.actualizarCuadre(listaFacturaReporte,
				factura.getDetalleFactura());
		factura.setDescuentoCuadre(cantidadFactura.gettDescuentoEgreso());
		facturaService.actualizar(factura);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"ACTUALIZO LA FACTURA INTERNA: " + factura.getId());

		nuevoCuadre();
	}

	public void cambiarCantidad(FacturaReporte facturaReporte) {
		cantidadFactura = facturaInternaService.calcularCantidad(
				facturaReporte, listaFacturaReporte);
	}

	public void cambiarDescDol(FacturaReporte facturaReporte) {
		cantidadFactura = facturaInternaService.calcularDescuentoDolares(
				facturaReporte, listaFacturaReporte);
	}

	public void cambiarImporte(FacturaReporte facturaReporte) {
		cantidadFactura = facturaInternaService.calcularImporte(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarPrecio(FacturaReporte facturaReporte) {
		cantidadFactura = facturaInternaService.calcularPrecio(facturaReporte,
				listaFacturaReporte);
	}

	public void editarFacturaInterna(Factura factura) {
		limpiarFactura();
		this.factura = facturaService.obtenerPorFacturaId(factura.getId());
		cliente = factura.getCliente().getPersona();
		if (factura.getDescuentoCuadre() != null)
			cantidadFactura.settDescuentoEgreso(factura.getDescuentoCuadre());

		for (DetalleFactura detalleFactura : factura.getDetalleFactura()) {
			FacturaReporte fr = facturaInternaService.asignar(detalleFactura);
			cantidadFactura = facturaInternaService.calcularCantidadFactura(
					cantidadFactura, fr);
			listaFacturaReporte.add(fr);
		}
	}

	public CantidadFactura getCantidadFactura() {
		return facturaInternaService.redondearCantidadFactura(cantidadFactura);
	}

	public Persona getCliente() {
		return cliente;
	}

	public Factura getFactura() {
		return factura;
	}

	public List<FacturaReporte> getListaFacturaReporte() {
		return listaFacturaReporte;
	}

	@PostConstruct
	public void init() {
	}

	public void limpiarFactura() {
		factura = new Factura();
		cliente = new Persona();
		listaFacturaReporte = new ArrayList<FacturaReporte>();
		cantidadFactura = new CantidadFactura();
	}

	public void nuevoCuadre() {
		limpiarFactura();
		redireccionar("listadoFactura.jsf");
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setCliente(Persona cliente) {
		this.cliente = cliente;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setListaFacturaReporte(List<FacturaReporte> listaFacturaReporte) {
		this.listaFacturaReporte = listaFacturaReporte;
	}

}