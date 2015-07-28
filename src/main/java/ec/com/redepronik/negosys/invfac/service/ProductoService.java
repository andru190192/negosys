package ec.com.redepronik.negosys.invfac.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entity.ProductoUnidad;
import ec.com.redepronik.negosys.invfac.entity.TipoPrecioProducto;
import ec.com.redepronik.negosys.invfac.entityAux.ControlInventarioReporte;
import ec.com.redepronik.negosys.invfac.entityAux.LocalProducto;
import ec.com.redepronik.negosys.invfac.entityAux.PedidoStockProductoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.Pvp;
import ec.com.redepronik.negosys.invfac.entityAux.StockProductoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.TablaPrecios;

public interface ProductoService {

	@Transactional
	public void actualizar(Producto producto,
			List<TablaPrecios> listaPrecioProductos);

	@Transactional
	public void actualizarKardexInicial(Producto producto, Local local,
			Integer cantidad);

	public int calcularCantidadMaximaUnidad(List<ProductoUnidad> list);

	public BigDecimal calcularPrecio(Producto producto, int precioId);

	public BigDecimal calcularPrecio(String ean, List<TipoPrecioProducto> list,
			Kardex k, int localId, int precioId);

	public BigDecimal calcularPrecioMayorista(Producto producto);

	public void calcularPrecioPorGanancia(Producto producto,
			TablaPrecios tablaPrecios);

	public void calcularPrecioPorIva(Producto producto,
			TablaPrecios tablaPrecios);

	public void calcularPrecioPorPorcentaje(Producto producto,
			TablaPrecios tablaPrecios);

	public List<TablaPrecios> calcularPrecios(Producto producto,
			List<TablaPrecios> listaPrecios);

	@Transactional
	public Boolean comprobarDatos(String campo, String valor, String id);

	public boolean comprobarPreciosVacias(
			List<TablaPrecios> listaPrecioProductos);

	public boolean comprobarUnidadesVacias(Producto producto);

	public Long contar();

	public String convertirUnidadString(int cantidad, List<ProductoUnidad> list);

	@Transactional
	public void eliminar(Producto producto);

	public void eliminarTipoPrecioProducto(TablaPrecios tablaPrecios,
			List<TablaPrecios> listaPrecioProductos);

	@Transactional(readOnly = true)
	public void eliminarUnidad(Producto producto, ProductoUnidad productoUnidad);

	public HashMap<String, Object> generarCodigo(Producto producto);

	@Transactional
	public void generarInventarioInicial(Producto producto,
			List<LocalProducto> listaLocalProductos);

	public String impuestoProducto(List<ProductoTarifa> list);

	@Transactional
	public void insertar(Producto producto,
			List<TablaPrecios> listaPrecioProductos);

	@Transactional
	public List<Producto> obtener();

	@Transactional
	public List<Producto> obtenerPorDefectoFacturar();

	@Transactional(readOnly = true)
	public List<Producto> obtener(String criterioBusqueda, Integer grupoId);

	@Transactional(readOnly = true)
	public List<ControlInventarioReporte> obtenerControlInventario(
			Integer localId);

	// @Transactional
	// public void apean(Producto producto);

	@Transactional
	public List<Pvp> obtenerListaPreciosConImpuestosPorProducto(
			Producto producto);

	@Transactional(readOnly = true)
	public List<String> obtenerListaString(String criterioProductoBusqueda);

	@Transactional(readOnly = true)
	public List<String> obtenerListaStringPorLocal(
			String criterioProductoBusqueda, int localId);

	// @Transactional(readOnly = true)
	// public List<String> obtenerListaStringPorLocal(
	// String criterioProductoBusqueda, String establecimiento);

	@Transactional(readOnly = true)
	public List<Producto> obtenerParaBusqueda(String nombreProducto);

	@Transactional(readOnly = true)
	public Producto obtenerPorCodigo(String codigo);

	@Transactional(readOnly = true)
	public Producto obtenerPorCodigosAndLocal(Integer localId, String codigo);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorCriterioBusqueda(
			String criterioProductoBusqueda);

	@Transactional(readOnly = true)
	public Producto obtenerPorEan(String ean);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorEanAndLocal(String ean, Integer localId);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorGrupo(Integer grupoId);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorKardexInicial(Integer localId);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorLocal(Integer localId);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorLocal(String criterioProductoBusqueda,
			Integer localId);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorLocal(String criterioProducto,
			Long productoId, Integer localId);

	@Transactional(readOnly = true)
	public List<Producto> obtenerPorLocalAndGrupo(Integer localId,
			Integer grupoId);

	@Transactional(readOnly = true)
	public List<LocalProducto> obtenerPorLocalProducto(String ean);

	@Transactional(readOnly = true)
	public Producto obtenerPorProductoId(Long productoId);

	@Transactional
	public List<Producto> obtenerPorProveedor(Integer proveedorid);

	@Transactional(readOnly = true)
	public List<Producto> reporteObtenerTodos();

	public BigDecimal obtenerPvp(BigDecimal precioCosto,
			List<TipoPrecioProducto> list);

	public BigDecimal obtenerPvpConImpuestos(Producto producto);

	public BigDecimal obtenerMayoristaConImpuestos(Producto producto);

	public Integer obtenerPvpPorProducto(List<TipoPrecioProducto> list);

	@Transactional(readOnly = true)
	public List<PedidoStockProductoReporte> reporteObtenerStockParaPedidoPorLocal(
			Integer localId);

	@Transactional(readOnly = true)
	public List<StockProductoReporte> obtenerStockPorLocal(Integer localId);

	@Transactional(readOnly = true)
	public Producto obtenerUnidadesPorProductoId(Long productoId);

	public TipoPrecioProducto obtenerValorPvp(List<TipoPrecioProducto> list);

	public BigDecimal precioConImpuestos(Producto producto,
			BigDecimal precioReferencial);
}