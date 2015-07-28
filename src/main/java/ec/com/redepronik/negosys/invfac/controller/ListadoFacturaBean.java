package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.DetalleNotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.GananciaFacturaReporte;
import ec.com.redepronik.negosys.invfac.report.FacturacionReportes;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.invfac.service.NotaCreditoService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;

@Controller
@Scope("session")
public class ListadoFacturaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private NotaCreditoBean devolucionBean;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private FacturacionReportes facturaReport;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private FacturaInternaBean facturaInternaBean;

	@Autowired
	private NotaCreditoService notaCreditoService;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private List<Factura> listaFacturas;
	private String criterioBusquedaCliente;
	private String criterioBusquedaNumeroFactura;
	private Integer criterioBusquedaEstado;
	private Date criterioBusquedaFechaDocumento;

	private List<FacturaReporte> listaFacturasDetalle;
	private Factura factura;
	private String login = "";
	private String pass = "";
	private boolean bFactura;
	private String compModifica;
	private List<FacturaReporte> listaFacturaReporteDevolucion;
	private CantidadFactura cantidadFactura;
	private GananciaFacturaReporte gananciaFacturaReporte;

	public ListadoFacturaBean() {
		// criterioBusquedaFechaDocumento = new Date();
		factura = new Factura();
	}

	public void generarXML() {
		if (SecurityContextHolder.getContext().getAuthentication().getName()
				.compareTo("0123456789") == 0) {

			CantidadFactura cantidadFactura = new CantidadFactura();
			cantidadFactura.settDescuentoEgreso(factura.getDescuento());
			for (DetalleFactura de : factura.getDetalleFactura())
				cantidadFactura = facturaService.calcularCantidadFactura(
						cantidadFactura, facturaService.asignar(de));

			facturaService.redondearCantidadFactura(cantidadFactura);
			documentosElectronicosService.generarFacturaXML(factura.getId(),
					cantidadFactura);
		}
	}

	// @PostConstruct
	// public void init() {
	// for (Factura factura : facturaService.obtenerTodos()) {
	// CantidadFactura cantidadFactura = new CantidadFactura();
	// cantidadFactura.settDescuentoEgreso(factura.getDescuento());
	// for (DetalleFactura de : factura.getDetalleFactura())
	// cantidadFactura = facturaService.calcularCantidadFactura(
	// cantidadFactura, facturaService.asignar(de));
	// facturaService.redondearCantidadFactura(cantidadFactura);
	//
	// documentosElectronicosService.generarFacturaXML(factura.getId(),
	// cantidadFactura);
	// }
	// }

	public String getCriterioBusquedaNumeroFactura() {
		return criterioBusquedaNumeroFactura;
	}

	public void setCriterioBusquedaNumeroFactura(
			String criterioBusquedaNumeroFactura) {
		this.criterioBusquedaNumeroFactura = criterioBusquedaNumeroFactura;
	}

	public void calcularGanancia(Factura factura) {
		gananciaFacturaReporte = facturaService.obtenerGananciaPorFactura(
				factura.getId(), new BigDecimal("12.00"));
		gananciaFacturaReporte.setGanancia(gananciaFacturaReporte
				.getPrecioVenta()
				.subtract(gananciaFacturaReporte.getPrecioCosto())
				.subtract(gananciaFacturaReporte.getDescuentoProducto()));
	}

	public void convertirDevolucion() {
		boolean bn = empleadoService.autorizacion(login, pass);
		login = "";
		pass = "";
		if (bn) {
			devolucionBean.convertirDevolucion(factura);
			redireccionar("notaCredito.jsf");
		}
	}

	public void cuadreFactura(Factura factura) {
		facturaInternaBean.editarFacturaInterna(factura);
		redireccionar("facturaInterna.jsf");
	}

	public void eliminar() {
		Integer notaCreditoId = facturaService.eliminar(factura, login, pass)
				.getId();
		if (notaCreditoId != null) {
			NotaCredito notaCredito = notaCreditoService
					.obtenerPorNotaCreditoId(notaCreditoId);
			CantidadFactura cf = new CantidadFactura();
			for (DetalleNotaCredito de : notaCredito.getDetalleNotaCredito())
				cf = notaCreditoService.calcularCantidadFactura(cf,
						notaCreditoService.asignar(de));

			documentosElectronicosService.generarNotaCreditoXML(notaCreditoId,
					cf);
		}
		login = "";
		pass = "";
	}

	public void generarListaDetalle() {
		listaFacturasDetalle = new ArrayList<FacturaReporte>();
		cantidadFactura = new CantidadFactura();
		cantidadFactura.settDescuentoEgreso(factura.getDescuento());
		List<DetalleFactura> list = factura.getDetalleFactura();
		for (DetalleFactura de : list) {
			FacturaReporte f = facturaService.asignar(de);
			listaFacturasDetalle.add(f);
			cantidadFactura = facturaService.calcularCantidadFactura(
					cantidadFactura, f);
		}
		facturaService.redondearCantidadFactura(cantidadFactura);
	}

	public CantidadFactura getCantidadFactura() {
		return cantidadFactura;
	}

	public String getCompModifica() {
		return compModifica;
	}

	public String getCriterioBusquedaCliente() {
		return criterioBusquedaCliente;
	}

	public Integer getCriterioBusquedaEstado() {
		return criterioBusquedaEstado;
	}

	public Date getCriterioBusquedaFechaDocumento() {
		return criterioBusquedaFechaDocumento;
	}

	public Factura getFactura() {
		return factura;
	}

	public GananciaFacturaReporte getGananciaFacturaReporte() {
		return gananciaFacturaReporte;
	}

	public List<FacturaReporte> getListaFacturaReporteDevolucion() {
		return listaFacturaReporteDevolucion;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public List<FacturaReporte> getListaFacturasDetalle() {
		return listaFacturasDetalle;
	}

	public String getLogin() {
		return login;
	}

	public String getPass() {
		return pass;
	}

	public void imprimirFactura() {
		facturaReport.reporteFacturaC(factura.getId());
	}

	public boolean isBFactura() {
		return bFactura;
	}

	public void limpiarObjetos() {
		criterioBusquedaCliente = new String();
		criterioBusquedaFechaDocumento = null;
		criterioBusquedaEstado = 0;
	}

	public void obtener() {
		listaFacturas = facturaService.obtener(criterioBusquedaEstado,
				criterioBusquedaCliente, criterioBusquedaNumeroFactura,
				criterioBusquedaFechaDocumento);
		limpiarObjetos();
	}

	public void redirecionar() {
		redireccionar("factura.jsf");
	}

	public void redirecionarDI() {
		redireccionar("listadoNotaEntrega.jsf");
	}

	public void setBFactura(boolean bFactura) {
		this.bFactura = bFactura;
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setCompModifica(String compModifica) {
		this.compModifica = compModifica;
	}

	public void setCriterioBusquedaCliente(String criterioBusquedaCliente) {
		this.criterioBusquedaCliente = criterioBusquedaCliente;
	}

	public void setCriterioBusquedaEstado(Integer criterioBusquedaEstado) {
		this.criterioBusquedaEstado = criterioBusquedaEstado;
	}

	public void setCriterioBusquedaFechaDocumento(
			Date criterioBusquedaFechaDocumento) {
		this.criterioBusquedaFechaDocumento = criterioBusquedaFechaDocumento;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setGananciaFacturaReporte(
			GananciaFacturaReporte gananciaFacturaReporte) {
		this.gananciaFacturaReporte = gananciaFacturaReporte;
	}

	public void setListaFacturaReporteDevolucion(
			List<FacturaReporte> listaFacturaReporteDevolucion) {
		this.listaFacturaReporteDevolucion = listaFacturaReporteDevolucion;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setListaFacturasDetalle(
			List<FacturaReporte> listaFacturasDetalle) {
		this.listaFacturasDetalle = listaFacturasDetalle;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}