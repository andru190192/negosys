package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.service.IngresoService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;

@Controller
@Scope("session")
public class ListadoIngresoBean {

	@Autowired
	private IngresoService ingresoService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private LocalService localService;

	private List<Ingreso> listaIngresos;
	private String criterioBusquedaProveedor;
	private String criterioBusquedaNumeroFactura;
	private Date criterioBusquedaFechaIngreso;
	private Date criterioBusquedaFechaFactura;

	private Ingreso ingreso;

	public ListadoIngresoBean() {
		ingreso = new Ingreso();
	}

	public Date getCriterioBusquedaFechaFactura() {
		return criterioBusquedaFechaFactura;
	}

	public Date getCriterioBusquedaFechaIngreso() {
		return criterioBusquedaFechaIngreso;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public List<Ingreso> getListaIngresos() {
		return listaIngresos;
	}

	public void limpiarObjetos() {
		criterioBusquedaProveedor = new String();
		criterioBusquedaNumeroFactura = new String();
		criterioBusquedaFechaIngreso = new Date();
		criterioBusquedaFechaFactura = new Date();
	}

	public void obtener() {
		listaIngresos = ingresoService.obtener(criterioBusquedaProveedor,
				criterioBusquedaNumeroFactura, criterioBusquedaFechaIngreso,
				criterioBusquedaFechaFactura);
	}

	public void redirecionar() {
		redireccionar("ingreso.jsf");
	}

	public String getCriterioBusquedaProveedor() {
		return criterioBusquedaProveedor;
	}

	public void setCriterioBusquedaProveedor(String criterioBusquedaProveedor) {
		this.criterioBusquedaProveedor = criterioBusquedaProveedor;
	}

	public String getCriterioBusquedaNumeroFactura() {
		return criterioBusquedaNumeroFactura;
	}

	public void setCriterioBusquedaNumeroFactura(
			String criterioBusquedaNumeroFactura) {
		this.criterioBusquedaNumeroFactura = criterioBusquedaNumeroFactura;
	}

	public void setCriterioBusquedaFechaFactura(
			Date criterioBusquedaFechaFactura) {
		this.criterioBusquedaFechaFactura = criterioBusquedaFechaFactura;
	}

	public void setCriterioBusquedaFechaIngreso(
			Date criterioBusquedaFechaIngreso) {
		this.criterioBusquedaFechaIngreso = criterioBusquedaFechaIngreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public void setListaIngresos(List<Ingreso> listaIngresos) {
		this.listaIngresos = listaIngresos;
	}

}
