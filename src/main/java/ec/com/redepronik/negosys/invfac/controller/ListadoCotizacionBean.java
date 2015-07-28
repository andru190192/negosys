package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Cotizacion;
import ec.com.redepronik.negosys.invfac.entity.DetalleCotizacion;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.service.CotizacionService;
import ec.com.redepronik.negosys.invfac.service.LocalService;

@Controller
@Scope("session")
public class ListadoCotizacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacturaBean facturaBean;

	@Autowired
	private CotizacionService cotizacionService;

	@Autowired
	private LocalService localService;

	private List<Cotizacion> listaCotizaciones;
	private String criterioBusquedaCliente;
	private String criterioBusquedaCodigo;
	private String criterioBusquedaDetalle;
	private Date criterioBusquedaFechaDocumento;
	private List<FacturaReporte> listaCotizacionesDetalle;
	private Cotizacion egreso;
	private CantidadFactura cantidadFactura;

	public ListadoCotizacionBean() {
		egreso = new Cotizacion();
	}

	public void convertirFactura() {
		Local local = localService.obtenerPorEstablecimiento(egreso
				.getEstablecimiento());
		List<FacturaReporte> list = new ArrayList<FacturaReporte>();
		for (DetalleCotizacion dc : egreso.getDetalleCotizacion())
			list.add(cotizacionService.asignar(dc, local.getId()));
		facturaBean.convertirFactura(egreso.getCliente(), egreso.getVendedor(),
				local, list);
		redireccionar("factura.jsf");
	}

	public void generarListaDetalle() {
		listaCotizacionesDetalle = new ArrayList<FacturaReporte>();
		cantidadFactura = new CantidadFactura();
		for (DetalleCotizacion dc : egreso.getDetalleCotizacion()) {
			FacturaReporte f = cotizacionService.asignar(dc);
			listaCotizacionesDetalle.add(f);
			cantidadFactura = cotizacionService.calcularCantidadFactura(
					cantidadFactura, f);
		}
		cotizacionService.redondearCantidadFactura(cantidadFactura);
	}

	public CantidadFactura getCantidadFactura() {
		return cantidadFactura;
	}

	public Cotizacion getCotizacion() {
		return egreso;
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

	public List<Cotizacion> getlistaCotizaciones() {
		return listaCotizaciones;
	}

	public List<FacturaReporte> getlistaCotizacionesDetalle() {
		return listaCotizacionesDetalle;
	}

	public void limpiarObjetos() {
		criterioBusquedaCliente = new String();
		criterioBusquedaCodigo = new String();
		criterioBusquedaDetalle = new String();
		criterioBusquedaFechaDocumento = null;
	}

	public void obtener() {
		listaCotizaciones = cotizacionService.obtener(criterioBusquedaCliente,
				criterioBusquedaCodigo, criterioBusquedaDetalle,
				criterioBusquedaFechaDocumento);
		limpiarObjetos();
	}

	public void redirecionar() {
		redireccionar("cotizacion.jsf");
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setCotizacion(Cotizacion egreso) {
		this.egreso = egreso;
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

	public void setlistaCotizaciones(List<Cotizacion> listaCotizaciones) {
		this.listaCotizaciones = listaCotizaciones;
	}

	public void setlistaCotizacionesDetalle(
			List<FacturaReporte> listaCotizacionesDetalle) {
		this.listaCotizacionesDetalle = listaCotizacionesDetalle;
	}
}