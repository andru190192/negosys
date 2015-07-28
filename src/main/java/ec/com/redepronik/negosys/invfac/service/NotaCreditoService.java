package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.DetalleNotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.EgresoCaja;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;

public interface NotaCreditoService {

	@Transactional
	public void actualizar(NotaCredito egreso);

	public FacturaReporte asignar(DetalleFactura de);

	public FacturaReporte asignar(DetalleNotaCredito de);

	public FacturaReporte asignar(FacturaReporte facturaReporte);

	public CantidadFactura calcularCantidad(FacturaReporte facturaReporte,
			List<FacturaReporte> listNotaEntrega);

	public CantidadFactura calcularCantidadFactura(
			CantidadFactura cantidadFactura, FacturaReporte facturaReporte);

	public void calcularImporte(FacturaReporte fr);

	public boolean cargarProductoLista(FacturaReporte facturaReporte,
			List<FacturaReporte> list, int bodegaIdNotaEntrega);

	public boolean convertirListaFacturaReporteListaFacturaDetalle(
			List<FacturaReporte> list, NotaCredito notaCredito);

	@Transactional
	public List<DetalleNotaCredito> duplicarDetalleNotaCredito(
			List<DetalleNotaCredito> detalleNotaCreditos);

	@Transactional
	public NotaCredito insertar(NotaCredito egreso, Local local);

	@Transactional
	public List<Factura> obtenerTodos();

	@Transactional
	public List<NotaCredito> obtener(Boolean activo, int tipoDocumentoId);

	@Transactional
	public List<Factura> obtener(Integer criterioBusquedaEstado,
			String criterioBusquedaCliente, String criterioBusquedaCodigo,
			String criterioBusquedaDetalle, Date criterioBusquedaFechaDocumento);

	public List<NotaCredito> obtenerDetalleNotaCreditoPorEstado(
			Date fechaInicio, Date fechaFin, int estado);

	@Transactional
	public NotaCredito obtenerPorCodigo(String codigo);

	@Transactional(readOnly = true)
	public List<NotaCredito> obtenerPorEstado(String criterioBusquedaCliente,
			String criterioBusquedaDocumento, Date criterioBusquedafechaInicio,
			Date criterioBusquedafechaFin);

	@Transactional
	public NotaCredito obtenerPorNotaCreditoId(Integer notaCreditoId);

	@Transactional
	public int posicion(FacturaReporte facturaReporte,
			List<DetalleNotaCredito> detalleNotaCreditos);

	public List<FacturaReporte> quitarProductosCantidadCero(
			List<FacturaReporte> list);

	@Transactional
	public CantidadFactura redondearCantidadFactura(
			CantidadFactura cantidadFactura);

	@Transactional
	public List<EgresoCaja> reporteEgresoCaja(int cajero, Date fecha);

	public List<DetalleNotaCredito> sumarCantidades(
			List<DetalleNotaCredito> detalleNotaCreditos);

	public CantidadFactura sumarCantidadFinal(List<FacturaReporte> list);

}
