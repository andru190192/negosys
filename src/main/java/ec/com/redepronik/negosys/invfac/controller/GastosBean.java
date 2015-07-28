package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.enviarVariableVista;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestampCompleto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Gastos;
import ec.com.redepronik.negosys.invfac.entity.TipoPago;
import ec.com.redepronik.negosys.invfac.entityAux.GastosAux;
import ec.com.redepronik.negosys.invfac.service.GastoService;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;

@Controller
@Scope("session")
public class GastosBean implements Serializable {

	@Autowired
	private GastoService gastoService;

	@Autowired
	private ProveedorService proveedorService;

	private static final long serialVersionUID = 1L;
	private Date criterioBusquedaFecha;
	private String criterioBusquedaDescripcion;
	private String criterioBusquedaProveedor;
	private boolean bnFactura;
	private Date fechaGasto;
	private Gastos gastos;

	private String proveedor;

	private List<GastosAux> listaGastos;

	public GastosBean() {
	}

	public void actualizar() {
		gastos.setFecha(timestampCompleto(fechaGasto));
		gastoService.actualizar(gastos);
	}

	public void cargarEditar() {
		fechaGasto = gastos.getFecha();
	}

	public void cargarProveedor() {
		gastos.setProveedor(proveedorService.cargarProveedor(proveedor));
	}

	public void eliminar() {
		gastoService.eliminar(gastos);
	}

	public String getCriterioBusquedaDescripcion() {
		return criterioBusquedaDescripcion;
	}

	public Date getCriterioBusquedaFecha() {
		return criterioBusquedaFecha;
	}

	public String getCriterioBusquedaProveedor() {
		return criterioBusquedaProveedor;
	}

	public Date getFechaGasto() {
		return fechaGasto;
	}

	public Gastos getGastos() {
		return gastos;
	}

	public List<GastosAux> getListaGastos() {
		return listaGastos;
	}

	public TipoPago[] getListaTiposPago() {
		return TipoPago.values();
	}

	public String getProveedor() {
		return proveedor;
	}

	@PostConstruct
	public void init() {
		nuevoGasto();
	}

	public void insertar() {
		boolean bn = false;
		if (fechaGasto == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UNA FECHA");
			bn = true;
		} else if (gastos.getDescripcion().length() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UNA DESCRIPCION");
			bn = true;
		} else if (gastos.getDescripcion().length() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UNA DESCRIPCION");
			bn = true;
		} else if (gastos.getTipoPago().getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: ESCOJA UN TIPO DE PAGO");
			bn = true;
		} else if (gastos.getPrecioUnitarioCosto().compareTo(
				new BigDecimal("0.00")) == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UN PRECIO UNITARIO");
			bn = true;
		} else if (gastos.getCantidad() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UNA CANTIDAD");
			bn = true;
		} else {
			gastos.setFecha(timestampCompleto(fechaGasto));
			gastos.setDescripcion(gastos.getDescripcion().toUpperCase());
			gastoService.insertar(gastos);
			bn = false;
		}
		enviarVariableVista("errorGasto", bn);
	}

	public boolean isBnFactura() {
		return bnFactura;
	}

	public void nuevoGasto() {
		gastos = new Gastos();
		fechaGasto = new Date();
		proveedor = "";
	}

	public List<String> obtenerDescripcionPorBusqueda(
			String criterioDescripcionBusqueda) {
		List<String> lista = gastoService
				.obtenerListaDescripcionesAutoComplete(criterioDescripcionBusqueda);
		if (lista.size() == 1) {
			gastos.setDescripcion(lista.get(0));
		}
		return lista;
	}

	public void obtenerGastos() {
		listaGastos = gastoService.obtenerGastos(criterioBusquedaFecha,
				criterioBusquedaDescripcion, criterioBusquedaProveedor);
		proveedor = proveedor.split(" - ")[0];
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

	public void setBnFactura(boolean bnFactura) {
		this.bnFactura = bnFactura;
	}

	public void setCriterioBusquedaDescripcion(
			String criterioBusquedaDescripcion) {
		this.criterioBusquedaDescripcion = criterioBusquedaDescripcion;
	}

	public void setCriterioBusquedaFecha(Date criterioBusquedaFecha) {
		this.criterioBusquedaFecha = criterioBusquedaFecha;
	}

	public void setCriterioBusquedaProveedor(String criterioBusquedaProveedor) {
		this.criterioBusquedaProveedor = criterioBusquedaProveedor;
	}

	public void setFechaGasto(Date fechaGasto) {
		this.fechaGasto = fechaGasto;
	}

	public void setGastos(Gastos gastos) {
		this.gastos = gastos;
	}

	public void setListaGastos(List<GastosAux> listaGastos) {
		this.listaGastos = listaGastos;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

}