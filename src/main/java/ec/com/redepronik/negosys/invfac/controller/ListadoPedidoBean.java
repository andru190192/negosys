package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetallePedido;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Pedido;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.PedidoService;

@Controller
@Scope("session")
public class ListadoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacturaBean facturaBean;

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private LocalService localService;

	private List<Pedido> listaPedidos;
	private String criterioBusquedaCliente;
	private String criterioBusquedaCodigo;
	private String criterioBusquedaDetalle;
	private Date criterioBusquedaFechaDocumento;

	private List<FacturaReporte> listaPedidosDetalle;
	private Pedido pedido;
	private CantidadFactura cantidadFactura;

	public ListadoPedidoBean() {
		pedido = new Pedido();
	}

	public void convertirFactura() {
		Local local = localService.obtenerPorEstablecimiento(pedido
				.getEstablecimiento());
		List<FacturaReporte> list = new ArrayList<FacturaReporte>();
		for (DetallePedido dp : pedido.getDetallePedido())
			list.add(pedidoService.asignar(dp, local.getId()));
		facturaBean.convertirFactura(pedido.getCliente(), pedido.getVendedor(),
				local, list);
		redireccionar("factura.jsf");
	}

	public void generarListaDetalle() {
		listaPedidosDetalle = new ArrayList<FacturaReporte>();
		cantidadFactura = new CantidadFactura();
		List<DetallePedido> list = pedido.getDetallePedido();
		for (DetallePedido de : list) {
			FacturaReporte f = pedidoService.asignar(de);
			listaPedidosDetalle.add(f);
			cantidadFactura = pedidoService.calcularCantidadFactura(
					cantidadFactura, f);
		}
		pedidoService.redondearCantidadFactura(cantidadFactura);
	}

	public CantidadFactura getCantidadFactura() {
		return cantidadFactura;
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

	public List<Pedido> getListaPedidos() {
		return listaPedidos;
	}

	public List<FacturaReporte> getListaPedidosDetalle() {
		return listaPedidosDetalle;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void limpiarObjetos() {
		criterioBusquedaCliente = new String();
		criterioBusquedaCodigo = new String();
		criterioBusquedaDetalle = new String();
		criterioBusquedaFechaDocumento = null;
	}

	public void obtener() {
		listaPedidos = pedidoService.obtener(criterioBusquedaCliente,
				criterioBusquedaCodigo, criterioBusquedaDetalle,
				criterioBusquedaFechaDocumento);
		limpiarObjetos();
	}

	public void redirecionar() {
		redireccionar("pedido.jsf");
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
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

	public void setListaPedidos(List<Pedido> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}

	public void setListaPedidosDetalle(List<FacturaReporte> listaPedidosDetalle) {
		this.listaPedidosDetalle = listaPedidosDetalle;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

}