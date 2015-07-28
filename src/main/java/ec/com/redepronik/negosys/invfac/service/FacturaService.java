package ec.com.redepronik.negosys.invfac.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.Entrada;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadesEntradaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaImprimir;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.GananciaFacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.IngresoCaja;
import ec.com.redepronik.negosys.invfac.entityAux.NumeroCuotasReporte;
import ec.com.redepronik.negosys.invfac.entityAux.Pvp;
import ec.com.redepronik.negosys.invfac.entityAux.VolumenVentaProducto;
import ec.com.redepronik.negosys.rrhh.entity.Ciudad;

public interface FacturaService {

	@Transactional
	public void actualizar(Factura egreso);

	public FacturaReporte asignar(DetalleFactura de);

	public FacturaReporte asignar(FacturaReporte facturaReporte);

	public CantidadFactura calcularCantidad(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura calcularCantidadFactura(
			CantidadFactura cantidadFactura, FacturaReporte facturaReporte);

	public CantidadFactura calcularDescuentoDolares(FacturaReporte fr,
			List<FacturaReporte> list);

	public CantidadFactura calcularDescuentoPorcentaje(FacturaReporte fr,
			List<FacturaReporte> list);

	@Transactional
	public CantidadesEntradaReporte calcularEntrada(Entrada entrada,
			CantidadesEntradaReporte cantidadesEntradaReporte,
			Date fechaFactura, Date fecha);

	@Transactional
	public BigDecimal calcularEntradas(Factura egreso);

	public void calcularImporte(FacturaReporte fr);

	public CantidadFactura calcularImporte(FacturaReporte fr,
			List<FacturaReporte> list);

	@Transactional
	public NumeroCuotasReporte calcularNumeroCuotasEntradasCreditos(
			Factura factura, NumeroCuotasReporte numeroCuotas, Date fecha);

	public CantidadFactura calcularPrecio(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura calcularTipoPrecio(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	@Transactional
	public BigDecimal calcularTotal(Factura egreso);

	@Transactional
	public BigDecimal calcularTotal(String codigoDocumento);

	public void cambiarDescuentoFactura(CantidadFactura cf);

	public CantidadFactura cambiarEstado(FacturaReporte fr,
			List<FacturaReporte> list);

	public List<FacturaReporte> cargarListaFacturaReporteFab();

	public CantidadFactura cargarProductoLista(String codigoEspecial,
			Integer cantidad, String ean, List<FacturaReporte> lista,
			Integer bodegaId, Pvp pvp);

	public boolean comprobarCantidadFinal(List<DetalleFactura> list,
			String codigoEstablecimiento);

	public boolean comprobarDetallesFactura(List<FacturaReporte> lista);

	public int contarRegistrosActivos(List<FacturaReporte> facturaReporte);

	public boolean convertirListaFacturaReporteListaFacturaDetalle(
			List<FacturaReporte> list, Factura egreso);

	@Transactional
	public List<DetalleFactura> duplicarDetalleFactura(
			List<DetalleFactura> detalleFacturas);

	@Transactional
	public Factura duplicarFactura(Factura egreso, int tipoDocumentoId);

	@Transactional
	public NotaCredito eliminar(Factura egreso, String login, String pass);

	public CantidadFactura eliminarDetalle(FacturaReporte fr,
			List<FacturaReporte> list);

	@Transactional
	public void eliminarFab(Factura egreso, String login, String pass);

	@Transactional
	public boolean imprimirFactura(int facturaId, List<FacturaImprimir> list);

	// @Transactional
	// public Integer insertar(Factura egreso);

	@Transactional
	public Factura insertar(Factura egreso,
			List<FacturaReporte> listaFacturaReporte);

	@Transactional
	public Factura insertarFacturaCajero(Factura factura,
			List<FacturaReporte> listaFacturaReporte);

	@Transactional
	public Integer insertarFab(Factura egreso, List<FacturaReporte> list);

	@Transactional
	public List<Factura> obtener(Integer criterioBusquedaEstado,
			String criterioBusquedaCliente,
			String criterioBusquedaNumeroFactura,
			Date criterioBusquedaFechaDocumento);

	@Transactional
	public Long obtenerCantidadVendidaPorProveedorCliente(Integer clienteid,
			Long productoid, Date fechainicio, Date fechaFin);

	@Transactional
	public List<Factura> obtenerEntregaProducto(Date fechaInicio,
			Date fechaFin, Local local, Ciudad ciudad);

	@Transactional
	public List<Factura> obtenerFacturasParaCredito(
			String criterioBusquedaCliente, String criterioBusquedaCodigo);

	@Transactional
	public List<Factura> obtenerFacturasParaGuiaRemision(
			String establecimiento, int ciudadId, String criterioBusquedaCodigo);

	@Transactional
	public List<Factura> obtenerFacturasPorCliente(
			String criterioBusquedaCliente, String criterioBusquedaCodigo,
			Integer tipoDocumentoId);

	@Transactional
	public List<GananciaFacturaReporte> obtenerGananciaBrutaPorFechas(
			Date fechaInicio, Date fechaFin);

	@Transactional
	public List<GananciaFacturaReporte> obtenerGananciaNetaPorFechas(
			Date fechaInicio, Date fechaFin);

	@Transactional
	public GananciaFacturaReporte obtenerGananciaPorFactura(Integer egresoId,
			BigDecimal iva);

	@Transactional
	public List<Factura> obtenerNoPagadosPorCiudad(int ciudadId);

	@Transactional
	public Factura obtenerPorCodigo(String codigo);

	@Transactional
	public List<Factura> obtenerPorCredito(String cedulaRuc,
			Integer criterioBusquedaEstado, String criterioBusquedaCodigo);

	@Transactional
	public List<Factura> obtenerPorCreditoAlDiaAndFechaLimite(Date fechaLimite);

	@Transactional
	public List<Factura> obtenerPorCreditoVencidoAndFechaLimite(Date fechaLimite);

	@Transactional(readOnly = true)
	public List<Factura> obtenerPorEstado(String criterioBusquedaCliente,
			String criterioBusquedaNumeroDocumento, Date criterioBusquedafechaInicio,
			Date criterioBusquedafechaFin);

	@Transactional(readOnly = true)
	public Factura obtenerPorFacturaId(Integer egresoId);

	@Transactional
	public List<Factura> obtenerTodos();

	@Transactional
	public List<Factura> obtenerPorFechasAndVendedorAndEstadoDocumento(
			Date fechaInicio, Date fechaFin, int vendedorId, int estadoDocumento);

	@Transactional
	public List<VolumenVentaProducto> obtenerVolumenVentaProducto(
			Date fechaInicio, Date fechaFin, int ordenarId,
			EstadoProductoVenta estadoProductoVenta);

	@Transactional
	public int posicion(FacturaReporte facturaReporte,
			List<DetalleFactura> detalleFacturas);

	public List<FacturaReporte> quitarProductosCantidadCero(
			List<FacturaReporte> list);

	@Transactional
	public CantidadFactura redondearCantidadFactura(
			CantidadFactura cantidadFactura);

	@Transactional
	public List<IngresoCaja> reporteIngreso(Date fechaInicio, Date fechaFin);

	@Transactional
	public List<IngresoCaja> reporteIngresoCaja(int cajero, Date fecha);

	@Transactional
	public List<DetalleFactura> sumarCantidades(
			List<DetalleFactura> detalleFacturas);

	public List<DetalleFactura> sumarCantidadesTodo(
			List<DetalleFactura> detalleFacturas);

	public CantidadFactura sumarCantidadFinal(List<FacturaReporte> list);

}