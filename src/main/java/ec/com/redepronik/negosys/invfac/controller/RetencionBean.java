package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleRetencion;
import ec.com.redepronik.negosys.invfac.entity.ImpuestoRetencion;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Retencion;
import ec.com.redepronik.negosys.invfac.entity.TarifaRetencion;
import ec.com.redepronik.negosys.invfac.entity.TipoComprobanteRetencion;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.RetencionService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;

@Controller
@Scope("session")
public class RetencionBean {

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private RetencionService retencionService;

	@Autowired
	private LocalService localService;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	private Retencion retencion;
	private String proveedor;
	private BigDecimal valorRetenido;
	private TarifaRetencion tarifa;
	private TarifaRetencion[] listaTarifas;
	private DetalleRetencion detalleRetencion;
	private List<Local> listaLocales;
	private BigDecimal totalRetenido = newBigDecimal();

	public RetencionBean() {
	}

	public BigDecimal getTotalRetenido() {
		return redondearTotales(totalRetenido);
	}

	public void setTotalRetenido(BigDecimal totalRetenido) {
		this.totalRetenido = totalRetenido;
	}

	public void cargarProveedor() {
		retencion.setProveedor(proveedorService.cargarProveedor(proveedor));
		// proveedor = proveedor.split(" - ")[2];
	}

	public DetalleRetencion getDetalleRetencion() {
		return detalleRetencion;
	}

	public TipoComprobanteRetencion[] getListaComprobantes() {
		return TipoComprobanteRetencion.values();
	}

	public ImpuestoRetencion[] getListaImpuestos() {
		return ImpuestoRetencion.values();
	}

	public TarifaRetencion[] getListaTarifas() {
		return listaTarifas;
	}

	public String getProveedor() {
		return proveedor;
	}

	public ProveedorService getProveedorService() {
		return proveedorService;
	}

	public Retencion getRetencion() {
		return retencion;
	}

	public TarifaRetencion getTarifa() {
		return tarifa;
	}

	public BigDecimal getValorRetenido() {
		return valorRetenido;
	}

	@PostConstruct
	public void init() {
		retencion = new Retencion();
		retencion.setProveedor(new Proveedor());
		retencion.getProveedor().setPersona(new Persona());
		retencion.setDetallesRetenciones(new ArrayList<DetalleRetencion>());
		detalleRetencion = new DetalleRetencion();
		listaLocales = localService.obtener(true);
	}

	public void insertar() {
		if (retencion.getProveedor() == null || proveedor.compareTo("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_WARN, "ESCOJA UN PROVEEDOR");
		else if (retencion.getEstablecimiento().compareTo("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"ESCOJA UN ESTABLECIMIENTO");
		else if (retencion.getDetallesRetenciones() == null
				|| retencion.getDetallesRetenciones().isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"INGRESE AL MENOS UN COMPROBANTE");
		else {
			Integer retencionId = retencionService.insertar(retencion).getId();
			if (retencionId != null) {
				documentosElectronicosService.generarRetencionXML(retencionId);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"INSERTO LA RETENCION # " + retencion.getId());
				limpiarRetencion();
			}

		}
	}

	public void insertarDetalle() {
		if (detalleRetencion != null) {
			if (detalleRetencion.getTipoComprobanteRetencion() == null) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"ESCOJA UN TIPO DE COMPROBANTE", "error", true);
			} else if (detalleRetencion.getNumeroComprobante().compareTo("") == 0
					|| detalleRetencion.getNumeroComprobante().length() != 15) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"INGRESE LOS 15 DIGITOS DEL COMPROBANTE", "error", true);
			} else if (detalleRetencion.getFechaEmision() == null) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"INGRESE LA FECHA DEL COMPROBANTE", "error", true);
			} else if (detalleRetencion.getImpuestoRetencion() == null) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"ESCOJA UN IMPUESTO", "error", true);
			} else if (detalleRetencion.getTarifa() == null) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"ESCOJA UNA TARIFA", "error", true);
			} else if (detalleRetencion.getPorcentajeRetencion().compareTo(
					new BigDecimal("0.00")) == 0) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"INGRESE UN PORCENTAJE", "error", true);
			} else if (detalleRetencion.getBaseImponible().compareTo(
					new BigDecimal("0.00")) == 0) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"INGRESE LA BASE IMPONIBLE", "error", true);
			} else {
				retencion.addDetalleRetencion(detalleRetencion);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"SE AGREGO CORRECTAMENTE EL COMPROBANTE "
								+ detalleRetencion.getNumeroComprobante(),
						"error", false);
				detalleRetencion = new DetalleRetencion();
				totalRetenido = totalRetenido.add(valorRetenido);
				setValorRetenido(new BigDecimal("0.00"));
			}
		}
	}

	public void limpiarRetencion() {
		retencion = new Retencion();
		retencion.setProveedor(new Proveedor());
		retencion.getProveedor().setPersona(new Persona());
		retencion.setDetallesRetenciones(new ArrayList<DetalleRetencion>());
		proveedor = "";
		detalleRetencion = new DetalleRetencion();
		totalRetenido = newBigDecimal();
	}

	public void obtenerListaTarifas() {
		listaTarifas = TarifaRetencion.obtenerPorImpuesto(detalleRetencion
				.getImpuestoRetencion());
	}

	public void obtenerPorcentaje() {
		detalleRetencion
				.setPorcentajeRetencion(redondearTotales(detalleRetencion
						.getTarifa().getPorcentaje()));
	}

	public List<String> obtenerProveedorPorBusqueda(
			String criterioProveedorBusqueda) {
		List<String> lista = proveedorService
				.obtenerListaProveedoresAutoComplete(criterioProveedorBusqueda);
		if (lista.size() == 1) {
			proveedor = lista.get(0);
			cargarProveedor();
		}
		return lista;
	}

	public void obtenerValorRetenido() {
		valorRetenido = redondearTotales(detalleRetencion.getBaseImponible()
				.multiply(detalleRetencion.getPorcentajeRetencion())
				.divide(new BigDecimal(100)));
	}

	public void cambiarPorcentaje() {
		detalleRetencion.setPorcentajeRetencion(detalleRetencion
				.getPorcentajeRetencion());
	}

	public void setDetalleRetencion(DetalleRetencion detalleRetencion) {
		this.detalleRetencion = detalleRetencion;
	}

	public void setListaTarifas(TarifaRetencion[] listaTarifas) {
		this.listaTarifas = listaTarifas;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public void setProveedorService(ProveedorService proveedorService) {
		this.proveedorService = proveedorService;
	}

	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	public void setTarifa(TarifaRetencion tarifa) {
		this.tarifa = tarifa;
	}

	public void setValorRetenido(BigDecimal valorRetenido) {
		this.valorRetenido = valorRetenido;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

}