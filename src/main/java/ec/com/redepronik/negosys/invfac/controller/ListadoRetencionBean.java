package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleRetencion;
import ec.com.redepronik.negosys.invfac.entity.Retencion;
import ec.com.redepronik.negosys.invfac.entity.TipoComprobanteRetencion;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.invfac.service.RetencionService;

@Controller
@Scope("session")
public class ListadoRetencionBean {

	@Autowired
	private RetencionService retencionService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private LocalService localService;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private List<Retencion> listaRetenciones;
	private String criterioBusquedaProveedor;
	private String criterioBusquedaNumeroComprobante;
	private String criterioBusquedaNumeroRetencion;
	private Date criterioBusquedaFechaIngreso;
	private Date criterioBusquedaFechaRetencion;
	private DetalleRetencion detalleRetencion;

	private Retencion retencion;

	public ListadoRetencionBean() {

	}

	public void generarXML() {
		if (SecurityContextHolder.getContext().getAuthentication().getName()
				.compareTo("0123456789") == 0) {
			documentosElectronicosService
					.generarRetencionXML(retencion.getId());
		}
	}

	@PostConstruct
	public void init() {
		retencion = new Retencion();
		detalleRetencion = new DetalleRetencion();

		// for (Retencion retencion : retencionService.obtener())
		// documentosElectronicosService
		// .generarRetencionXML(retencion.getId());
	}

	public Date getCriterioBusquedaFechaIngreso() {
		return criterioBusquedaFechaIngreso;
	}

	public void limpiarObjetos() {
		criterioBusquedaProveedor = new String();
		criterioBusquedaNumeroComprobante = new String();
		criterioBusquedaFechaIngreso = new Date();
	}

	public void obtener() {
		listaRetenciones = retencionService
				.obtener(criterioBusquedaProveedor,
						criterioBusquedaNumeroComprobante,
						criterioBusquedaFechaIngreso,criterioBusquedaNumeroRetencion,criterioBusquedaFechaRetencion);
	}

	public void redirecionar() {
		redireccionar("retencion.jsf");
	}

	public String getCriterioBusquedaProveedor() {
		return criterioBusquedaProveedor;
	}

	public void setCriterioBusquedaProveedor(String criterioBusquedaProveedor) {
		this.criterioBusquedaProveedor = criterioBusquedaProveedor;
	}

	public void setCriterioBusquedaFechaIngreso(
			Date criterioBusquedaFechaIngreso) {
		this.criterioBusquedaFechaIngreso = criterioBusquedaFechaIngreso;
	}

	public RetencionService getRetencionService() {
		return retencionService;
	}

	public void setRetencionService(RetencionService retencionService) {
		this.retencionService = retencionService;
	}

	public ProductoService getProductoService() {
		return productoService;
	}

	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}

	public LocalService getLocalService() {
		return localService;
	}

	public void setLocalService(LocalService localService) {
		this.localService = localService;
	}

	public List<Retencion> getListaRetenciones() {
		return listaRetenciones;
	}

	public void setListaRetenciones(List<Retencion> listaRetenciones) {
		this.listaRetenciones = listaRetenciones;
	}

	public String getCriterioBusquedaNumeroComprobante() {
		return criterioBusquedaNumeroComprobante;
	}

	public void setCriterioBusquedaNumeroComprobante(
			String criterioBusquedaNumeroComprobante) {
		this.criterioBusquedaNumeroComprobante = criterioBusquedaNumeroComprobante;
	}

	public Retencion getRetencion() {
		return retencion;
	}

	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	public DetalleRetencion getDetalleRetencion() {
		return detalleRetencion;
	}

	public void setDetalleRetencion(DetalleRetencion detalleRetencion) {
		this.detalleRetencion = detalleRetencion;
	}

	public TipoComprobanteRetencion[] getListaComprobantes() {
		return TipoComprobanteRetencion.values();
	}

	public String getCriterioBusquedaNumeroRetencion() {
		return criterioBusquedaNumeroRetencion;
	}

	public void setCriterioBusquedaNumeroRetencion(
			String criterioBusquedaNumeroRetencion) {
		this.criterioBusquedaNumeroRetencion = criterioBusquedaNumeroRetencion;
	}

	public Date getCriterioBusquedaFechaRetencion() {
		return criterioBusquedaFechaRetencion;
	}

	public void setCriterioBusquedaFechaRetencion(
			Date criterioBusquedaFechaRetencion) {
		this.criterioBusquedaFechaRetencion = criterioBusquedaFechaRetencion;
	}

}
