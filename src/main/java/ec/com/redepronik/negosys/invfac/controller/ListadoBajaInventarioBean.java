package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.BajaInventario;
import ec.com.redepronik.negosys.invfac.entity.DetalleBajaInventario;
import ec.com.redepronik.negosys.invfac.entityAux.BajaInventarioReporte;
import ec.com.redepronik.negosys.invfac.report.InventarioReportes;
import ec.com.redepronik.negosys.invfac.service.BajaInventarioService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;

@Controller
@Scope("session")
public class ListadoBajaInventarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BajaInventarioService bajaInventarioService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private InventarioReportes egresoReport;

	private List<BajaInventario> listaBajaInventarios;
	private String criterioBusquedaNumeroBaja;
	private Date criterioBusquedaFechaDocumento;

	private List<BajaInventarioReporte> listaBajaInventariosDetalle;
	private BajaInventario bajaInventario;
	private BigDecimal total;

	public ListadoBajaInventarioBean() {
		bajaInventario = new BajaInventario();
	}

	public void generarListaDetalle() {
		listaBajaInventariosDetalle = new ArrayList<BajaInventarioReporte>();
		total = newBigDecimal();
		for (DetalleBajaInventario de : bajaInventario
				.getDetalleBajaInventario())
			listaBajaInventariosDetalle.add(bajaInventarioService.asignar(de));
		total = bajaInventarioService
				.sumarCantidadFinal(listaBajaInventariosDetalle);
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BajaInventario getBajaInventario() {
		return bajaInventario;
	}

	public Date getCriterioBusquedaFechaDocumento() {
		return criterioBusquedaFechaDocumento;
	}

	public List<BajaInventario> getListaBajaInventarios() {
		return listaBajaInventarios;
	}

	public List<BajaInventarioReporte> getListaBajaInventariosDetalle() {
		return listaBajaInventariosDetalle;
	}

	public void obtenerBajaInvetario() {
		listaBajaInventarios = bajaInventarioService.obtener(
				criterioBusquedaNumeroBaja, criterioBusquedaFechaDocumento);
	}

	public void redirecionarBajaInventario() {
		redireccionar("bajaInventario.jsf");
	}

	public void setBajaInventario(BajaInventario bajaInventario) {
		this.bajaInventario = bajaInventario;
	}

	public String getCriterioBusquedaNumeroBaja() {
		return criterioBusquedaNumeroBaja;
	}

	public void setCriterioBusquedaNumeroBaja(String criterioBusquedaNumeroBaja) {
		this.criterioBusquedaNumeroBaja = criterioBusquedaNumeroBaja;
	}

	public void setCriterioBusquedaFechaDocumento(
			Date criterioBusquedaFechaDocumento) {
		this.criterioBusquedaFechaDocumento = criterioBusquedaFechaDocumento;
	}

	public void setListaBajaInventarios(
			List<BajaInventario> listaBajaInventarios) {
		this.listaBajaInventarios = listaBajaInventarios;
	}

	public void setListaBajaInventariosDetalle(
			List<BajaInventarioReporte> listaBajaInventariosDetalle) {
		this.listaBajaInventariosDetalle = listaBajaInventariosDetalle;
	}

}