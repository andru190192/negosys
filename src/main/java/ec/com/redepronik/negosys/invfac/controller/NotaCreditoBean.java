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
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.NotaCreditoService;

@Controller
@Scope("session")
public class NotaCreditoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private NotaCreditoService notaCreditoService;

	@Autowired
	private LocalService localService;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private NotaCredito notaCredito;

	private List<FacturaReporte> listaFacturaReporte;
	private CantidadFactura cantidadFactura;
	private Local local;

	public NotaCreditoBean() {

	}

	public void cambiarCantidad(FacturaReporte facturaReporte) {
		cantidadFactura = notaCreditoService.calcularCantidad(facturaReporte,
				listaFacturaReporte);
	}

	public void convertirDevolucion(Factura factura) {
		limpiarDevolucion();

		notaCredito.setFactura(factura);
		factura.setNotaCredito(notaCredito);
		local = localService.obtenerPorEstablecimiento(factura
				.getEstablecimiento());

		for (DetalleFactura df : factura.getDetalleFactura()) {
			FacturaReporte fr = notaCreditoService.asignar(df);
			fr.setCantidad(0);
			listaFacturaReporte.add(fr);
		}
	}

	public CantidadFactura getCantidadFactura() {
		return notaCreditoService.redondearCantidadFactura(cantidadFactura);
	}

	public EstadoProductoVenta[] getListaEstadoProductoVenta() {
		return EstadoProductoVenta.values();
	}

	public List<FacturaReporte> getListaFacturaReporte() {
		return listaFacturaReporte;
	}

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	@PostConstruct
	public void init() {
	}

	public void insertar(ActionEvent actionEvent) {
		if (!notaCreditoService
				.convertirListaFacturaReporteListaFacturaDetalle(
						listaFacturaReporte, notaCredito)) {

			Integer notaCreditoId = notaCreditoService.insertar(notaCredito,
					local).getId();
			if (notaCreditoId != null) {
				documentosElectronicosService.generarNotaCreditoXML(
						notaCreditoId, cantidadFactura);

				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"INSERTÓ LA DEVOLUCION: " + notaCredito.getId());
				nuevaDevolucion();
			}
		}
	}

	public void limpiarDevolucion() {
		notaCredito = new NotaCredito();
		listaFacturaReporte = new ArrayList<FacturaReporte>();
		cantidadFactura = new CantidadFactura();
		notaCredito.setFactura(new Factura());
	}

	public void nuevaDevolucion() {
		limpiarDevolucion();
		redireccionar("listadoFactura.jsf");
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setListaFacturaReporte(List<FacturaReporte> listaFacturaReporte) {
		this.listaFacturaReporte = listaFacturaReporte;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

}