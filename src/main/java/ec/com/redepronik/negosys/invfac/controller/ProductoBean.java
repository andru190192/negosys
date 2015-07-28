package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.Utils.comprobarEan13;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.matriz;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaImagenProducto;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.guardarImagen;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.leerImagen;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.UtilsMath.valorConIva;
import static ec.com.redepronik.negosys.utils.UtilsMath.valorConPorcentaje;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Grupo;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entity.ProductoUnidad;
import ec.com.redepronik.negosys.invfac.entity.Tarifa;
import ec.com.redepronik.negosys.invfac.entity.TipoPrecioProducto;
import ec.com.redepronik.negosys.invfac.entity.TipoProducto;
import ec.com.redepronik.negosys.invfac.entity.Unidad;
import ec.com.redepronik.negosys.invfac.entityAux.CodigoProductoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.LocalProducto;
import ec.com.redepronik.negosys.invfac.entityAux.TablaPrecios;
import ec.com.redepronik.negosys.invfac.service.GrupoService;
import ec.com.redepronik.negosys.invfac.service.KardexService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.invfac.service.TarifaService;
import ec.com.redepronik.negosys.invfac.service.TipoProductoService;
import ec.com.redepronik.negosys.invfac.service.UnidadService;
import ec.com.redepronik.negosys.utils.service.ReporteService;

@Controller
@Scope("session")
public class ProductoBean {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	private UnidadService unidadService;

	@Autowired
	private ReporteService reporteService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private TipoProductoService tipoProductoService;

	@Autowired
	private TarifaService tarifaService;

	@Autowired
	private LocalService localService;

	private List<Producto> listaProductos;
	private List<Grupo> listaGrupos;
	private String criterioBusqueda;
	private Grupo criterioBusquedaGrupo;

	private List<TablaPrecios> listaTpp;

	private Producto producto;
	// private Iva iva;
	private Unidad unidad;
	private TipoPrecioProducto tipoPrecioProducto;
	private TablaPrecios tablaPrecios;
	private ProductoUnidad productoUnidad;
	private Integer decimales;
	// CODIGO BARRA
	private String codigoTipo;
	private String codigo;
	private StreamedContent codigoBarra;
	private boolean bnPrecio;

	private List<LocalProducto> listaBodegaProductos;
	private boolean readOnlyPrecio;

	private List<Tarifa> listaTarifas;

	// imagen producto
	private StreamedContent logo;
	private InputStream is;

	private boolean k;

	Integer numeroImpuestos;

	public ProductoBean() {
	}

	public void actualizar(ActionEvent actionEvent) {
		productoService.actualizar(producto, listaTpp);
		listaProductos = new ArrayList<Producto>();
	}

	public void adk() {
		// bien 1;servicio 2
		if (producto.getTipoProducto().getId() == 1) {
			producto.setKardex(true);
			k = false;
		} else {
			producto.setKardex(false);
			k = true;
		}
	}

	public void calcularPrecioPorGanancia(TablaPrecios tipoPrecio) {
		productoService.calcularPrecioPorGanancia(producto, tipoPrecio);
	}

	public void calcularPrecioPorIva(TablaPrecios tipoPrecio) {
		productoService.calcularPrecioPorIva(producto, tipoPrecio);
	}

	public void calcularPrecioPorPorcentaje(TablaPrecios tipoPrecio) {
		productoService.calcularPrecioPorPorcentaje(producto, tipoPrecio);
	}

	public void calcularTablaPrecios() {
		productoService.calcularPrecios(producto, listaTpp);
	}

	public void cargaEditar() {
		listaTpp = new ArrayList<TablaPrecios>();
		for (TipoPrecioProducto tipoPrecioProducto : producto
				.getTipoPrecioProductos()) {
			TablaPrecios tablaPrecios = new TablaPrecios();
			tablaPrecios.setTipoPrecioProducto(tipoPrecioProducto);
			listaTpp.add(tablaPrecios);
		}
		listaTpp = productoService.calcularPrecios(producto, listaTpp);

		readOnlyPrecio = false;
		for (Local b : localService.obtener(true)) {
			if (kardexService.obtenerSaldoActual(producto.getEan(), b.getId()) != null) {
				readOnlyPrecio = true;
				break;
			}
		}
	}

	public void cargaMostrar() {
		listaTpp = new ArrayList<TablaPrecios>();
		for (TipoPrecioProducto tipoPrecioProducto : producto
				.getTipoPrecioProductos()) {
			TablaPrecios tablaPrecios = new TablaPrecios();
			tablaPrecios.setTipoPrecioProducto(tipoPrecioProducto);
			listaTpp.add(tablaPrecios);
		}
		listaTpp = productoService.calcularPrecios(producto, listaTpp);

		listaBodegaProductos = new ArrayList<LocalProducto>();
		listaBodegaProductos = productoService.obtenerPorLocalProducto(producto
				.getEan());
	}

	public void cargarInsertar() {
		producto = new Producto();
		producto.setGrupo(new Grupo());
		producto.setTipoProducto(TipoProducto.BN);
		producto.setKardex(true);

		producto.setProductoUnidads(new ArrayList<ProductoUnidad>());
		ProductoUnidad productoUnidad = new ProductoUnidad();
		productoUnidad.setOrden(1);
		productoUnidad.setUnidad(unidadService.obtenerPorUnidadId(1));
		productoUnidad.setPadre(0);
		productoUnidad.setCantidad(1);
		producto.addProductoUnidad(productoUnidad);

		producto.setProductosTarifas(new ArrayList<ProductoTarifa>());
		insertarFilaImpuestoTarifa();
		producto.getProductosTarifas().get(0)
				.setTarifa(tarifaService.obtenerPorTarifaId((short) 2));

		listaTpp = new ArrayList<TablaPrecios>();
		TipoPrecioProducto tppC = new TipoPrecioProducto();
		tppC.setNombre("COSTO");
		tppC.setValor(newBigDecimal());
		tppC.setPvp(false);
		tppC.setPorcentajePrecioFijo(true);
		TablaPrecios tablaPreciosC = new TablaPrecios(tppC, newBigDecimal(),
				newBigDecimal(), newBigDecimal());
		listaTpp.add(tablaPreciosC);

		TipoPrecioProducto tppM = new TipoPrecioProducto();
		tppM.setNombre("MAYORISTA");
		tppM.setValor(newBigDecimal());
		tppM.setPvp(false);
		tppM.setPorcentajePrecioFijo(true);
		TablaPrecios tablaPreciosM = new TablaPrecios(tppM, newBigDecimal(),
				newBigDecimal(), newBigDecimal());
		listaTpp.add(tablaPreciosM);

		TipoPrecioProducto tppA = new TipoPrecioProducto();
		tppA.setNombre("AFILIADO");
		tppA.setValor(newBigDecimal());
		tppA.setPvp(false);
		tppA.setPorcentajePrecioFijo(true);
		TablaPrecios tablaPreciosA = new TablaPrecios(tppA, newBigDecimal(),
				newBigDecimal(), newBigDecimal());
		listaTpp.add(tablaPreciosA);

		TipoPrecioProducto tppN = new TipoPrecioProducto();
		tppN.setNombre("NORMAL");
		tppN.setValor(newBigDecimal());
		tppN.setPvp(true);
		tppN.setPorcentajePrecioFijo(true);
		TablaPrecios tablaPreciosN = new TablaPrecios(tppN, newBigDecimal(),
				newBigDecimal(), newBigDecimal());
		listaTpp.add(tablaPreciosN);

		listaBodegaProductos = new ArrayList<LocalProducto>();

		for (Local ba : localService.obtener(true))
			listaBodegaProductos.add(new LocalProducto(ba, 0));
	}

	public void insertarUnidad(ProductoUnidad productoUnidad) {
		System.out.println(productoUnidad.getUnidad().getNombre());
		System.out.println(productoUnidad.getUnidad().getId());
		int fila = 0;
		for (int i = 0; i < producto.getProductoUnidads().size(); i++) {
			if (producto.getProductoUnidads().get(i).getUnidad().getId() == productoUnidad
					.getUnidad().getId())
				fila = i;
		}

		System.out.println("*** " + fila);

		boolean bn = false;
		for (int i = 0; i < producto.getProductoUnidads().size(); i++) {
			if (i != fila
					&& producto.getProductoUnidads().get(i).getUnidad().getId() == productoUnidad
							.getUnidad().getId())
				bn = true;
		}

		System.out.println("*** " + bn);

		if (bn) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA UNIDAD YA EXISTE");
			producto.getProductoUnidads().get(fila).getUnidad().setId(0);
		} else if (productoUnidad.getUnidad().getId() != null
				&& productoUnidad.getUnidad().getId() != 0) {
			Unidad u = unidadService.obtenerPorUnidadId(productoUnidad
					.getUnidad().getId());
			System.out.println(u.getNombre());
			productoUnidad.setUnidad(u);
		}
	}

	public void copiar_Codigo1() {
		producto.setCodigo1(producto.getEan());
	}

	public void eliminar(ActionEvent actionEvent) {
		productoService.eliminar(producto);
	}

	public void eliminarImpuesto(ProductoTarifa productoTarifa) {
		if (producto.getProductosTarifas().size() > 1) {
			producto.removeProductoTarifa(productoTarifa);
			calcularTablaPrecios();
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"EL IMPUESTO HA SIDO ELIMINADO CON EXITO");
		} else {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO PUEDE ELIMINAR EL IMPUESTO, DEBE DEJAR UN IMPUESTO");
		}
	}

	public void eliminarTipoPrecioProducto(TablaPrecios tablaPrecios) {
		productoService.eliminarTipoPrecioProducto(tablaPrecios, listaTpp);
	}

	public void eliminarUnidad(ProductoUnidad unidad) {
		productoService.eliminarUnidad(producto, unidad);
	}

	public void cargarGrupoProducto() {
		producto.setGrupo(grupoService.obtenerPorGrupoId(producto.getGrupo()
				.getId()));
	}

	// CODIGO DE BARRAS
	public void generarCodigo() {
		HashMap<String, Object> parametro = productoService
				.generarCodigo(producto);
		codigoBarra = (StreamedContent) parametro.get("imgCodigo");
		codigo = (String) parametro.get("codigo");
	}

	public String getCodigo() {
		return codigo;
	}

	public StreamedContent getCodigoBarra() {
		return codigoBarra;
	}

	public String getCodigoTipo() {
		return codigoTipo;
	}

	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public Grupo getCriterioBusquedaGrupo() {
		return criterioBusquedaGrupo;
	}

	public Integer getDecimales() {
		return decimales;
	}

	public List<LocalProducto> getListaBodegaProductos() {
		return listaBodegaProductos;
	}

	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public List<Tarifa> getListaTarifas() {
		return listaTarifas;
	}

	public TipoProducto[] getListaTipoProducto() {
		return TipoProducto.values();
	}

	public List<TablaPrecios> getlistaTpp() {
		return listaTpp;
	}

	public StreamedContent getLogo() {
		is = leerImagen(getRutaImagenProducto() + producto.getEan() + ".png");
		if (is != null)
			setLogo(new DefaultStreamedContent(is, "image/png"));
		return logo;
	}

	public Producto getProducto() {
		return producto;
	}

	public ProductoUnidad getProductoUnidad() {
		return productoUnidad;
	}

	public TablaPrecios getTablaPrecios() {
		return tablaPrecios;
	}

	public TipoPrecioProducto getTipoPrecioProducto() {
		return tipoPrecioProducto;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	@PostConstruct
	private void init() {
		limpiarObjetos();
		criterioBusquedaGrupo = new Grupo();
		listaGrupos = grupoService.obtener();
		decimales = parametro.getPrecisionDecimal();
		codigoTipo = "ean";
		numeroImpuestos = Impuesto.values().length;
		listaTarifas = tarifaService.obtener();
	}

	public void insertar(ActionEvent actionEvent) {
		productoService.insertar(producto, listaTpp);
		productoService
				.generarInventarioInicial(producto, listaBodegaProductos);
	}

	public void insertarFilaImpuestoTarifa() {
		if (producto.getProductosTarifas().size() == numeroImpuestos) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"YA NO HAY MAS IMPUESTO APLICABLES AL PRODUCTO");
		} else {
			producto.addProductoTarifa(new ProductoTarifa());
			producto.getProductosTarifas()
					.get(producto.getProductosTarifas().size() - 1)
					.setTarifa(new Tarifa());
			producto.getProductosTarifas()
					.get(producto.getProductosTarifas().size() - 1)
					.setActivo(true);

			if (producto.getProductosTarifas().size() == 1)
				producto.getProductosTarifas()
						.get(0)
						.setTarifa(
								tarifaService.obtenerPorImpuesto(Impuesto.IV)
										.get(0));
			else if (producto.getProductosTarifas().size() == 2)
				producto.getProductosTarifas()
						.get(1)
						.setTarifa(
								tarifaService.obtenerPorImpuesto(Impuesto.IC)
										.get(0));
			else if (producto.getProductosTarifas().size() == 3)
				producto.getProductosTarifas()
						.get(2)
						.setTarifa(
								tarifaService.obtenerPorImpuesto(Impuesto.IR)
										.get(0));
		}
	}

	public void insertarFilaTipoPrecio() {
		TablaPrecios tp = new TablaPrecios();
		tp.setTipoPrecioProducto(new TipoPrecioProducto(null, "PRECIO "
				+ (listaTpp.size() + 1), newBigDecimal(), listaTpp.size() + 1,
				false, producto, false));
		tp.setPorcentaje(newBigDecimal());
		tp.setPrecio(redondear(valorConPorcentaje(producto.getPrecio(),
				tp.getPorcentaje())));
		tp.setPrecioIva(redondear(valorConIva(tp.getPrecio())));

		listaTpp.add(tp);
		if (producto.getTipoPrecioProductos() == null)
			producto.setTipoPrecioProductos(new ArrayList<TipoPrecioProducto>());

		producto.addTipoPrecioProducto(tp.getTipoPrecioProducto());
		calcularTablaPrecios();
	}

	public void insertarFilaUnidad() {
		producto.addProductoUnidad(new ProductoUnidad(null, 0, producto
				.getProductoUnidads().size() + 1, 0, null, new Unidad()));
	}

	public boolean isBnPrecio() {
		return bnPrecio;
	}

	public boolean isK() {
		return k;
	}

	public boolean isReadOnlyPrecio() {
		return readOnlyPrecio;
	}

	public void limpiarBusqueda() {
		listaProductos = new ArrayList<Producto>();
	}

	public void limpiarObjetos() {
		producto = new Producto();
		producto.setGrupo(new Grupo());
		producto.setTipoProducto(TipoProducto.BN);

		unidad = new Unidad();
		productoUnidad = new ProductoUnidad();

		tipoPrecioProducto = new TipoPrecioProducto();
		listaTpp = new ArrayList<TablaPrecios>();
	}

	public void obtenerImpuestoTarifa(ProductoTarifa productoTarifa) {

		productoTarifa.setTarifa(tarifaService
				.obtenerPorTarifaId(productoTarifa.getTarifa().getId()));

		boolean bn = false;
		int posicion = producto.getProductosTarifas().indexOf(productoTarifa);
		for (int i = 0; i < producto.getProductosTarifas().size(); i++) {
			if ((i != posicion)
					&& productoTarifa.getTarifa().getImpuesto().getId() == producto
							.getProductosTarifas().get(i).getTarifa()
							.getImpuesto().getId()) {
				bn = true;
				break;
			}
		}
		if (bn) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL PRODUCTO YA TIENE AGREGADO UN IMPUESTO DE ESTE TIPO");
			productoTarifa.getTarifa().setId((short) 0);
			productoTarifa.getTarifa().setPorcentaje(newBigDecimal());
		} else
			calcularTablaPrecios();
	}

	public void obtenerProducto(Producto producto) {
		this.producto = productoService.obtenerPorProductoId(producto.getId());
		generarCodigo();
	}

	public void obtenerProductos() {
		listaProductos = productoService.obtener(
				criterioBusqueda.toUpperCase(), criterioBusquedaGrupo.getId());
	}

	public void productoUnidad() {
		if (!producto.getProductoUnidads().isEmpty()) {
			int size = producto.getProductoUnidads().size() - 1;
			if (producto.getProductoUnidads().get(size).getUnidad() == null)
				producto.getProductoUnidads().remove(size);
		}
	}

	public void pvp() {
		int posicion = listaTpp.indexOf(tablaPrecios);
		for (TablaPrecios tp : listaTpp)
			tp.getTipoPrecioProducto().setPvp(false);
		listaTpp.get(posicion).getTipoPrecioProducto().setPvp(true);
	}

	public void reporteCodigoProductoPersonalizado() {
		if (producto == null || producto.getId() == null
				|| producto.getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN PRODUCTO");
		else {
			if (!comprobarEan13(codigo))
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"EL PRODUCTO NO TIENE UN EAN13 VALIDO");
			else {
				List<CodigoProductoReporte> lista = new ArrayList<CodigoProductoReporte>();
				if (bnPrecio)
					lista.add(new CodigoProductoReporte(
							codigo,
							"$ "
									+ String.valueOf(redondearTotales(productoService.obtenerPvp(
											producto.getPrecio(),
											producto.getTipoPrecioProductos()))),
							producto.getNombre(), parametro
									.getNombreComercial(), matriz.getWeb()));
				else
					lista.add(new CodigoProductoReporte(codigo, null, producto
							.getNombre(), parametro.getNombreComercial(),
							matriz.getWeb()));
				Map<String, Object> parametros = new HashMap<String, Object>();
				reporteService.generarReportePDFSencillo(lista, parametros,
						"CodigoBarra");
			}
		}
	}

	public void reporteCodigoProductoSimple() {
		if (producto == null || producto.getId() == null
				|| producto.getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN PRODUCTO");
		else {
			if (!comprobarEan13(codigo))
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"EL PRODUCTO NO TIENE UN EAN13 VALIDO");
			else {
				List<CodigoProductoReporte> lista = new ArrayList<CodigoProductoReporte>();
				if (bnPrecio)
					lista.add(new CodigoProductoReporte(
							codigo,
							"$ "
									+ String.valueOf(redondearTotales(productoService.obtenerPvp(
											producto.getPrecio(),
											producto.getTipoPrecioProductos()))),
							producto.getNombre(), parametro
									.getNombreComercial(), matriz.getWeb()));
				else
					lista.add(new CodigoProductoReporte(codigo, null, producto
							.getNombre(), parametro.getNombreComercial(),
							matriz.getWeb()));
				Map<String, Object> parametros = new HashMap<String, Object>();
				reporteService.generarReportePDFSencillo(lista, parametros,
						"CodigoBarraSimple");
			}
		}
	}

	public void setBnPrecio(boolean bnPrecio) {
		this.bnPrecio = bnPrecio;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setCodigoBarra(StreamedContent codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public void setCodigoTipo(String codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public void setCriterioBusquedaGrupo(Grupo criterioBusquedaGrupo) {
		this.criterioBusquedaGrupo = criterioBusquedaGrupo;
	}

	public void setDecimales(Integer decimales) {
		this.decimales = decimales;
	}

	public void setK(boolean k) {
		this.k = k;
	}

	public void setListaBodegaProductos(List<LocalProducto> listaBodegaProductos) {
		this.listaBodegaProductos = listaBodegaProductos;
	}

	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public void setListaProductos(List<Producto> listaProductosTabla) {
		this.listaProductos = listaProductosTabla;
	}

	public void setListaTarifas(List<Tarifa> listaTarifas) {
		this.listaTarifas = listaTarifas;
	}

	public void setlistaTpp(List<TablaPrecios> listaTpp) {
		this.listaTpp = listaTpp;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setProductoUnidad(ProductoUnidad productoUnidad) {
		this.productoUnidad = productoUnidad;
	}

	public void setReadOnlyPrecio(boolean readOnlyPrecio) {
		this.readOnlyPrecio = readOnlyPrecio;
	}

	public void setTablaPrecios(TablaPrecios tablaPrecios) {
		this.tablaPrecios = tablaPrecios;
	}

	public void setTipoPrecioProducto(TipoPrecioProducto tipoPrecioProducto) {
		this.tipoPrecioProducto = tipoPrecioProducto;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public void subirImagen(FileUploadEvent event) {
		try {
			guardarImagen(getRutaImagenProducto(),
					leerImagen(event.getFile().getInputstream(), null),
					producto.getEan(), 300, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
