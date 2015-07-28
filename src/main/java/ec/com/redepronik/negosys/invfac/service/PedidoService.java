package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.DetallePedido;
import ec.com.redepronik.negosys.invfac.entity.Pedido;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;

public interface PedidoService {

	@Transactional
	public void actualizar(Pedido pedido);

	public FacturaReporte asignar(DetallePedido dp);

	public FacturaReporte asignar(DetallePedido dp, int bodegaId);

	public FacturaReporte asignar(FacturaReporte facturaReporte);

	public CantidadFactura calcularCantidad(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura calcularCantidadFactura(
			CantidadFactura cantidadFactura, FacturaReporte facturaReporte);

	public CantidadFactura calcularDescuentoDolares(FacturaReporte fr,
			List<FacturaReporte> list);

	public CantidadFactura calcularDescuentoPorcentaje(FacturaReporte fr,
			List<FacturaReporte> list);

	public void calcularImporte(FacturaReporte fr);

	public CantidadFactura calcularImporte(FacturaReporte fr,
			List<FacturaReporte> list);

	public CantidadFactura calcularPrecio(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura calcularTipoPrecio(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura cambiarEstado(FacturaReporte fr,
			List<FacturaReporte> list);

	public boolean cargarProductoLista(FacturaReporte facturaReporte,
			List<FacturaReporte> list, int bodegaId);

	public boolean convertirListaFacturaReporteListaPedidoDetalle(
			List<FacturaReporte> list, Pedido pedido);

	@Transactional
	public List<DetallePedido> duplicarDetallePedido(
			List<DetallePedido> detallePedidos);

	@Transactional
	public void eliminar(Pedido pedido, String login, String pass);

	@Transactional
	public Pedido insertar(Pedido pedido);

	@Transactional
	public List<Pedido> obtener(String criterioBusquedaCliente,
			String criterioBusquedaCodigo, String criterioBusquedaDetalle,
			Date criterioBusquedaFechaDocumento);

	@Transactional
	public List<Pedido> obtenerDetallePedidoPorEstado(Date fechaInicio,
			Date fechaFin, int estado);

	@Transactional
	public List<Pedido> obtenerPedidosPorCliente(
			String criterioBusquedaCliente, String criterioBusquedaCodigo,
			Integer tipoDocumentoId);

	@Transactional
	public Pedido obtenerPorPedidoId(Integer pedidoId);

	@Transactional
	public int posicion(FacturaReporte facturaReporte,
			List<DetallePedido> detallePedidos);

	public List<FacturaReporte> quitarProductosCantidadCero(
			List<FacturaReporte> list);

	@Transactional
	public CantidadFactura redondearCantidadFactura(
			CantidadFactura cantidadFactura);

}
