package ec.com.redepronik.negosys.invfac.report;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.UtilsMath.valorConPorcentaje;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Grupo;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoUnidad;
import ec.com.redepronik.negosys.invfac.entity.TipoPrecioProducto;
import ec.com.redepronik.negosys.invfac.entityAux.InversionBodegaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.InversionBodegasReporte;
import ec.com.redepronik.negosys.invfac.entityAux.PedidoStockProductoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.PrecioGrupoProductoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.PrecioProductoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.Retenciones;
import ec.com.redepronik.negosys.invfac.entityAux.StockProductoReporte;
import ec.com.redepronik.negosys.invfac.service.GrupoService;
import ec.com.redepronik.negosys.invfac.service.KardexService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.invfac.service.RetencionesService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;
import ec.com.redepronik.negosys.utils.service.ReporteService;

@Controller
@Scope("session")
public class InventarioReportes {

	@Autowired
	private ReporteService reporteService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private LocalService localService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private RetencionesService retencionesService;

	@Autowired
	private ProveedorService proveedorService;

	private Integer localId;
	private List<Local> listaLocales;
	private List<Persona> listaProveedores;
	private Date fechaInicioRetenciones;
	private Date fechaFinRetenciones;
	private Integer proveedorId;

	public InventarioReportes() {

	}

	public Integer getProveedorId() {
		return proveedorId;
	}

	public void setProveedorId(Integer proveedorId) {
		this.proveedorId = proveedorId;
	}

	public List<Persona> getListaProveedores() {
		return listaProveedores;
	}

	public void setListaProveedores(List<Persona> listaProveedores) {
		this.listaProveedores = listaProveedores;
	}

	public Date getFechaInicioRetenciones() {
		return fechaInicioRetenciones;
	}

	public void setFechaInicioRetenciones(Date fechaInicioRetenciones) {
		this.fechaInicioRetenciones = fechaInicioRetenciones;
	}

	public Date getFechaFinRetenciones() {
		return fechaFinRetenciones;
	}

	public void setFechaFinRetenciones(Date fechaFinRetenciones) {
		this.fechaFinRetenciones = fechaFinRetenciones;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public Integer getLocalId() {
		return localId;
	}

	@PostConstruct
	public void init() {
		listaLocales = localService.obtener(true);
		listaProveedores = proveedorService.obtener();
	}

	public InversionBodegaReporte calculaInversion(Producto producto) {
		Kardex kardex = kardexService.obtenerSaldoActual(producto.getEan(),
				localId);
		if (kardex != null && kardex.getCantidad() > 0) {
			int cantidad = kardex.getCantidad();

			TipoPrecioProducto tpp = productoService.obtenerValorPvp(producto
					.getTipoPrecioProductos());

			if (tpp == null)
				tpp = producto.getTipoPrecioProductos().size() >= 2 ? producto
						.getTipoPrecioProductos().get(1) : producto
						.getTipoPrecioProductos().get(0);

			BigDecimal preciouniventa = tpp.getPorcentajePrecioFijo() ? valorConPorcentaje(
					kardex.getPrecio(), tpp.getValor()) : tpp.getValor();

			return new InversionBodegaReporte(producto.getEan(),
					producto.getNombre(), cantidad, kardex.getPrecio(),
					multiplicar(kardex.getPrecio(), cantidad), preciouniventa,
					multiplicar(preciouniventa, cantidad));
		} else
			return null;
	}

	@PreAuthorize("hasRole('READ')")
	public void reporteConsolidadoInversionBodegas(ActionEvent actionEvent) {
		List<InversionBodegasReporte> list = new ArrayList<InversionBodegasReporte>();
		for (Local local : listaLocales) {
			BigDecimal preciototalcompra = newBigDecimal();
			BigDecimal preciototalventa = newBigDecimal();

			for (Producto producto : productoService.obtenerPorLocal(local
					.getId())) {
				InversionBodegaReporte ibr = calculaInversion(producto);
				if (ibr != null) {
					preciototalcompra = preciototalcompra.add(ibr
							.getPreciototalcompra());
					preciototalventa = preciototalventa.add(ibr
							.getPreciototalventa());
				}
			}

			list.add(new InversionBodegasReporte(localId, local.getNombre(),
					preciototalcompra, preciototalventa));
		}
		if (list == null || list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY NADA QUE MOSTRAR");
		else {
			Map<String, Object> parametros = new HashMap<String, Object>();
			reporteService.generarReportePDF(list, parametros,
					"InversionBodegas");
		}
	}

	@PreAuthorize("hasRole('READ')")
	public void reporteInversionBodegaPorProducto(ActionEvent actionEvent) {
		if (localId != 0) {
			List<InversionBodegaReporte> lista = new ArrayList<InversionBodegaReporte>();
			for (Producto producto : productoService.obtenerPorLocal(localId)) {
				InversionBodegaReporte ibr = calculaInversion(producto);
				if (ibr != null)
					lista.add(ibr);
			}
			if (lista == null || lista.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO HAY INFORMACION PARA MOSTRAR");
			else {
				Local local = localService.obtenerPorLocalId(localId);
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("bodega", local.getNombre());
				reporteService.generarReportePDF(lista, parametros,
						"InversionBodegaPorProducto");
			}
		}
		localId = 0;
	}

	public void reporteListaPrecioProducto(ActionEvent actionEvent) {
		List<PrecioGrupoProductoReporte> lista = new ArrayList<PrecioGrupoProductoReporte>();
		for (Grupo grupo : grupoService.obtener()) {
			List<PrecioProductoReporte> listaPreciosProductos = new ArrayList<PrecioProductoReporte>();
			for (Producto producto : productoService.obtenerPorGrupo(grupo
					.getId())) {
				BigDecimal pvp = redondearTotales(productoService
						.obtenerPvpConImpuestos(producto));
				String preciosConImpuestos = "";
				int contador = 1;
				for (ProductoUnidad pu : producto.getProductoUnidads()) {
					Integer can = 1;
					for (int i = producto.getProductoUnidads().indexOf(pu); i >= 0; i--)
						can = can
								* producto.getProductoUnidads().get(i)
										.getCantidad();

					preciosConImpuestos = preciosConImpuestos
							+ pu.getUnidad().getAbreviatura() + "-"
							+ redondearTotales(multiplicar(pvp, can));
					if (contador < producto.getProductoUnidads().size()) {
						preciosConImpuestos = preciosConImpuestos + " || ";
						contador++;
					}
				}
				listaPreciosProductos.add(new PrecioProductoReporte(producto
						.getId(), producto.getNombre(), preciosConImpuestos,
						null));
			}
			lista.add(new PrecioGrupoProductoReporte(grupo.getNombre(),
					listaPreciosProductos));
		}

		if (lista == null || lista.isEmpty()) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO HAY INFORMACION PARA MOSTRAR");
		} else {
			Map<String, Object> parametros = new HashMap<String, Object>();
			reporteService.generarReportePDF(lista, parametros,
					"ListaPrecioProducto");
		}
	}

	public void reporteRetenciones(ActionEvent actionEvent) {
		if (fechaInicioRetenciones == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA FECHA DE INICIO");
		else if (fechaInicioRetenciones == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA FECHA DE CORTE");
		else if (fechaInicioRetenciones.compareTo(fechaFinRetenciones) > 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"LA FECHA DE INICIO NO PUEDE SER MAYOR A LA FECHA DE CORTE");
		else {
			List<Retenciones> lista = retencionesService.obtener(proveedorId,
					fechaInicioRetenciones, fechaFinRetenciones);
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("fechaInicio", fechaInicioRetenciones);
			parametros.put("fechaFin", fechaFinRetenciones);
			reporteService.generarReportePDF(lista, parametros, "Retencion");
		}
	}

	public void reportePedidoStockProducto(ActionEvent actionEvent) {
		if (localId == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA BODEGA");
		else {
			Local local = localService.obtenerPorLocalId(localId);
			List<PedidoStockProductoReporte> lista = productoService
					.reporteObtenerStockParaPedidoPorLocal(localId);

			if (lista == null || lista.isEmpty()) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO HAY DATOS QUE MOSTRAR");
			} else {
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("bodega", local.getNombre());

				reporteService.generarReportePDF(lista, parametros,
						"PedidoStockProducto");
			}
		}
		localId = 0;
	}

	public void reporteProductoGrupo(ActionEvent actionEvent) {
		List<Grupo> list = grupoService.reporteObtenerPorProductos();
		Map<String, Object> parametros = new HashMap<String, Object>();
		reporteService.generarReportePDF(list, parametros, "ProductoGrupo");
	}

	public void reporteStockProductosPorBodega(ActionEvent actionEvent) {
		if (localId == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA BODEGA");
		} else {
			List<StockProductoReporte> lista = productoService
					.obtenerStockPorLocal(localId);
			if (lista == null || lista.isEmpty()) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO HAY DATOS QUE MOSTRAR");
			} else {
				for (StockProductoReporte stockProductoReporte : lista) {
					List<ProductoUnidad> listaUnidades = productoService
							.obtenerUnidadesPorProductoId(
									stockProductoReporte.getId())
							.getProductoUnidads();
					String stockString = productoService.convertirUnidadString(
							stockProductoReporte.getStock(), listaUnidades);

					stockProductoReporte.setStockString("("
							+ stockProductoReporte.getStock()
							+ listaUnidades.get(0).getUnidad().getAbreviatura()
							+ ") " + stockString);
				}
				Local local = localService.obtenerPorLocalId(localId);
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("bodega", local.getNombre());
				reporteService.generarReportePDF(lista, parametros,
						"StockProductoBodega");
			}
		}
		localId = 0;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setLocalId(Integer localId) {
		this.localId = localId;
	}

}