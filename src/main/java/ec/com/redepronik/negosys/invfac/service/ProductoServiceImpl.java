package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.Utils.comprobarEan13;
import static ec.com.redepronik.negosys.utils.Utils.generarEan13;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.divide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.UtilsMath.valorConPorcentaje;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.ControlInventarioReporteDao;
import ec.com.redepronik.negosys.invfac.dao.GrupoDao;
import ec.com.redepronik.negosys.invfac.dao.PedidoStockProductoReporteDao;
import ec.com.redepronik.negosys.invfac.dao.ProductoDao;
import ec.com.redepronik.negosys.invfac.dao.StockProductoReporteDao;
import ec.com.redepronik.negosys.invfac.entity.EstadoKardex;
import ec.com.redepronik.negosys.invfac.entity.Grupo;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
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

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private PedidoStockProductoReporteDao pedidoStockProductoReporteDao;

	@Autowired
	private ControlInventarioReporteDao controlInventarioReporteDao;

	@Autowired
	private StockProductoReporteDao stockProductoReporteDao;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private GrupoDao grupoDao;

	@Autowired
	private LocalService localService;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(Producto producto,
			List<TablaPrecios> listaPrecioProductos) {
		Set<ConstraintViolation<Producto>> violationsProducto = validator
				.validate(producto);

		if (violationsProducto.size() > 0)
			for (ConstraintViolation<Producto> cv : violationsProducto)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (comprobarUnidadesVacias(producto))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TIENE UNIDADES VACIAS O CON VALOR CERO");
		else if (producto.getEan().compareTo("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESE UN CODIGO PRINCIPAL");
		else if (producto.getCodigo1().compareTo("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESE UN CODIGO AUXILIAR");
		else if (producto.getGrupo().getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO, "ESCOJA UN GRUPO");
		else if (comprobarTarifasVacias(producto))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TIENE IMPUESTOS REPETIDOS PARA EL PRODUCTO");
		else if (comprobarInclusionIva(producto))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TODO PRODUCTO DEBE CONTENER IVA");
		else if (comprobarPreciosVacias(listaPrecioProductos))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TIENE TIPOS DE PRECIOS VACIOS O NO A ESCOGIDO EL PVP");
		else {
			for (int i = 0; i < listaPrecioProductos.size(); i++) {
				if (listaPrecioProductos.get(i).getTipoPrecioProducto()
						.getPorcentajePrecioFijo()) {
					producto.getTipoPrecioProductos()
							.get(i)
							.setValor(
									listaPrecioProductos.get(i).getPorcentaje());
				} else {
					producto.getTipoPrecioProductos().get(i)
							.setValor(listaPrecioProductos.get(i).getPrecio());
				}
				producto.getTipoPrecioProductos().get(i).setOrden(i + 1);
			}
			int c = 1;
			for (ProductoUnidad pu : producto.getProductoUnidads())
				pu.setOrden(c++);

			c = 1;
			for (ProductoTarifa pt : producto.getProductosTarifas()) {
				pt.setOrden(c++);
			}

			productoDao.actualizar(producto);
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTUALIZÓ EL PRODUCTO CORRECTAMENTE", "error", true);
		}
	}

	public void actualizarKardexInicial(Producto producto, Local local,
			Integer cantidad) {

		Kardex kardex = new Kardex();
		kardex.setEstadoKardex(EstadoKardex.II);
		kardex.setNota("");
		kardex.setFecha(timestamp());
		kardex.setCantidad(cantidad);
		kardex.setPrecio(producto.getPrecio());

		productoDao.actualizar(producto);
		kardexService.insertar(kardex);
	}

	public int calcularCantidadMaximaUnidad(List<ProductoUnidad> list) {
		int unidad = 1;
		for (int i = list.size() - 1; i >= 0; i--)
			unidad *= list.get(i).getCantidad();
		return unidad;
	}

	public BigDecimal calcularPrecio(Producto producto, int precioId) {
		for (TipoPrecioProducto tpp : producto.getTipoPrecioProductos())
			if (tpp.getId().compareTo(precioId) == 0)
				return tpp.getPorcentajePrecioFijo() ? redondear(valorConPorcentaje(
						producto.getPrecio(), tpp.getValor())) : tpp.getValor();
		return obtenerPvp(producto.getPrecio(),
				producto.getTipoPrecioProductos());
	}

	public BigDecimal calcularPrecio(String ean, List<TipoPrecioProducto> list,
			Kardex k, int localId, int precioId) {
		if (k == null)
			k = kardexService.obtenerSaldoActual(ean, localId);
		if (k != null)
			for (TipoPrecioProducto tpp : list)
				if (tpp.getId().compareTo(precioId) == 0)
					return tpp.getPorcentajePrecioFijo() ? redondear(valorConPorcentaje(
							k.getPrecio(), tpp.getValor())) : tpp.getValor();
		return newBigDecimal();
	}

	public BigDecimal calcularPrecioMayorista(Producto producto) {
		for (TipoPrecioProducto tpp : producto.getTipoPrecioProductos())
			if (tpp.getNombre().contains("MAYORISTA"))
				return tpp.getPorcentajePrecioFijo() ? redondear(valorConPorcentaje(
						producto.getPrecio(), tpp.getValor())) : tpp.getValor();
		return obtenerPvp(producto.getPrecio(),
				producto.getTipoPrecioProductos());
	}

	public void calcularPrecioPorGanancia(Producto producto,
			TablaPrecios tablaPrecios) {
		if (producto.getPrecio() == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"PRIMERO INGRESE UN PRECIO COSTO");
		} else {
			BigDecimal porcentajeOriginal = tablaPrecios.getPorcentaje();
			BigDecimal precioConIva = precioConImpuestos(producto,
					tablaPrecios.getPrecio());
			BigDecimal porcentaje = divide(
					tablaPrecios.getPrecio().subtract(producto.getPrecio())
							.multiply(new BigDecimal("100.00")),
					producto.getPrecio());
			if (porcentaje.compareTo(newBigDecimal()) >= 0) {
				tablaPrecios.setPrecioIva(redondear(precioConIva));
				tablaPrecios.setPorcentaje(redondear(porcentaje));
				tablaPrecios.setPrecio(redondear(tablaPrecios.getPrecio()));
			} else {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN PRECIO > AL PRECIO COSTO");
				tablaPrecios.setPrecio(redondear(valorConPorcentaje(
						producto.getPrecio(), porcentajeOriginal)));
			}
		}
	}

	public void calcularPrecioPorIva(Producto producto,
			TablaPrecios tablaPrecios) {
		if (producto.getPrecio() == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"PRIMERO INGRESE UN PRECIO COSTO");
		} else {
			BigDecimal precioSinIva = precioSinImpuestos(producto,
					tablaPrecios.getPrecioIva());
			BigDecimal porcentaje = divide(
					precioSinIva.subtract(producto.getPrecio()).multiply(
							new BigDecimal("100.00")), producto.getPrecio());
			if (porcentaje.compareTo(newBigDecimal()) >= 0) {
				tablaPrecios.setPrecio(redondear(precioSinIva));
				tablaPrecios.setPorcentaje(redondear(porcentaje));
				tablaPrecios
						.setPrecioIva(redondear(tablaPrecios.getPrecioIva()));
			} else {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN PRECIO > AL PRECIO COSTO + IMPUESTOS");
			}
		}
	}

	public void calcularPrecioPorPorcentaje(Producto producto,
			TablaPrecios tablaPrecios) {
		if (producto.getPrecio() == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"PRIMERO INGRESE UN PRECIO COSTO");
		} else {
			BigDecimal precioSinIva = valorConPorcentaje(producto.getPrecio(),
					tablaPrecios.getPorcentaje());
			BigDecimal precioConIva = precioConImpuestos(producto, precioSinIva);
			tablaPrecios.setPrecio(redondear(precioSinIva));
			tablaPrecios.setPrecioIva(redondear(precioConIva));
			tablaPrecios.setPorcentaje(redondear(tablaPrecios.getPorcentaje()));
		}
	}

	public List<TablaPrecios> calcularPrecios(Producto producto,
			List<TablaPrecios> listaPrecios) {
		if (producto.getPrecio() != null && listaPrecios != null
				&& !listaPrecios.isEmpty()) {
			for (TablaPrecios tablaPrecios : listaPrecios) {
				if (tablaPrecios.getTipoPrecioProducto()
						.getPorcentajePrecioFijo()) {
					if (tablaPrecios.getPorcentaje() == null)
						tablaPrecios.setPorcentaje(tablaPrecios
								.getTipoPrecioProducto().getValor());
					BigDecimal precioSinIva = redondear(valorConPorcentaje(
							producto.getPrecio(), tablaPrecios.getPorcentaje()));
					BigDecimal precioConIva = redondear(precioConImpuestos(
							producto, precioSinIva));
					tablaPrecios.setPrecio(precioSinIva);
					tablaPrecios.setPrecioIva(precioConIva);
					tablaPrecios.setPorcentaje(redondear(tablaPrecios
							.getPorcentaje()));
				} else {
					if (tablaPrecios.getPrecio() == null)
						tablaPrecios.setPrecio(tablaPrecios
								.getTipoPrecioProducto().getValor());
					BigDecimal precioSinIva = redondear(tablaPrecios
							.getPrecio());
					BigDecimal precioConIva = redondear(precioConImpuestos(
							producto, precioSinIva));
					BigDecimal porcentaje = divide(
							precioSinIva.subtract(producto.getPrecio())
									.multiply(new BigDecimal("100.00")),
							producto.getPrecio());
					if (porcentaje.compareTo(new BigDecimal("0")) >= 0) {
						tablaPrecios.setPrecio(precioSinIva);
						tablaPrecios.setPrecioIva(precioConIva);
						tablaPrecios.setPorcentaje(porcentaje);
					} else {
						porcentaje = new BigDecimal("0");
						precioSinIva = redondear(valorConPorcentaje(
								producto.getPrecio(), porcentaje));
						precioConIva = redondear(precioConImpuestos(producto,
								precioSinIva));
						tablaPrecios.setPrecio(precioSinIva);
						tablaPrecios.setPrecioIva(precioConIva);
						tablaPrecios.setPorcentaje(redondear(porcentaje));
					}
				}
			}
		}
		return listaPrecios;
	}

	public Boolean comprobarDatos(String campo, String valor, String id) {
		return productoDao.comprobarIndices(Producto.class, campo, valor, id) ? true
				: false;
	}

	public boolean comprobarInclusionIva(Producto producto) {
		for (ProductoTarifa pt : producto.getProductosTarifas())
			if (pt.getTarifa().getImpuesto().getId() == 2)
				return false;
		return true;
	}

	public boolean comprobarPreciosVacias(
			List<TablaPrecios> listaPrecioProductos) {
		for (TablaPrecios tp : listaPrecioProductos)
			if (tp.getTipoPrecioProducto().getNombre() == null
					|| tp.getTipoPrecioProducto().getNombre().compareTo("") == 0)
				return true;

		boolean bn = true;
		for (TablaPrecios tp : listaPrecioProductos)
			if (tp.getTipoPrecioProducto().getPvp()) {
				bn = false;
				break;
			}
		return bn;
	}

	public boolean comprobarTarifasVacias(Producto producto) {
		boolean bn = false;

		for (int i = 0; i < producto.getProductosTarifas().size(); i++) {
			for (int j = 0; j < producto.getProductosTarifas().size(); j++) {

				if (i != j
						&& producto.getProductosTarifas().get(i).getTarifa()
								.getImpuesto().getId() == producto
								.getProductosTarifas().get(j).getTarifa()
								.getImpuesto().getId()) {
					bn = true;
					break;
				}
			}
		}
		return bn;
	}

	public boolean comprobarUnidadesVacias(Producto producto) {
		for (ProductoUnidad pu : producto.getProductoUnidads())
			if (pu.getUnidad() != null
					&& (pu.getUnidad().getNombre() == null || pu.getUnidad()
							.getNombre().compareTo("") == 0)
					|| pu.getCantidad() == 0)
				return true;
		return false;
	}

	public Long contar() {
		return (Long) productoDao.contar(Producto.class);
	}

	public String convertirUnidadString(int cantidad, List<ProductoUnidad> list) {
		String nombre = "";
		for (int i = list.size() - 1; i >= 0; i--) {
			int unidad = (i - 1 > 0) ? list.get(i).getCantidad()
					* list.get(i - 1).getCantidad() : list.get(i).getCantidad();
			if (cantidad >= unidad) {
				nombre = nombre + (nombre.compareTo("") == 0 ? "" : "+")
						+ cantidad / unidad + ""
						+ list.get(i).getUnidad().getAbreviatura();
				cantidad = cantidad % unidad;
			}
		}

		return nombre;
	}

	public void eliminar(Producto producto) {
		producto.setActivo(producto.getActivo() ? false : true);
		productoDao.actualizar(producto);
		if (producto.getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTIVÓ EL PRODUCTO " + producto.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ EL PRODUCTO " + producto.getNombre());
	}

	public void eliminarTipoPrecioProducto(TablaPrecios tablaPrecios,
			List<TablaPrecios> listaPrecioProductos) {
		if (tablaPrecios.getTipoPrecioProducto().getPvp() == true)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO SE PUEDE ELIMINAR SI ES PVP");
		else {
			listaPrecioProductos.remove(tablaPrecios);
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL TIPO DE PRECIO FUE ELIMINADO");
		}
	}

	public void eliminarUnidad(Producto producto, ProductoUnidad productoUnidad) {
		if (producto.getProductoUnidads().size() > 1) {
			String unidad = productoUnidad.getUnidad() != null ? productoUnidad
					.getUnidad().getNombre() : "VACIA";
			producto.getProductoUnidads().remove(productoUnidad);
			int o = 1;
			for (ProductoUnidad pu : producto.getProductoUnidads())
				pu.setOrden(o++);

			presentaMensaje(FacesMessage.SEVERITY_INFO, "SE ELIMINO LA UNIDAD "
					+ unidad);
		} else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"DEBE TENER AL MENOS UNA UNIDAD VÁLIDA");
	}

	public HashMap<String, Object> generarCodigo(Producto producto) {
		HashMap<String, Object> parametro = new HashMap<String, Object>();
		StreamedContent codigoBarra = null;
		if (producto != null && producto.getId() != null) {
			try {
				File codigoBarraFile = new File("dynamiccodigoBarra");
				if (comprobarEan13(producto.getEan()))
					BarcodeImageHandler.saveJPEG(BarcodeFactory
							.createEAN13(producto.getEan().substring(0,
									producto.getEan().length() - 1)),
							codigoBarraFile);
				codigoBarra = new DefaultStreamedContent(new FileInputStream(
						codigoBarraFile), "image/jpeg");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN PRODUCTO");
		parametro.put("imgCodigo", codigoBarra);
		parametro.put("codigo", producto.getEan());
		return parametro;
	}

	public void generarInventarioInicial(Producto producto,
			List<LocalProducto> listaLocalProductos) {
		if (producto.getId() != null && producto.getId() != 0
				&& producto.getKardex()) {
			producto = obtenerPorProductoId(producto.getId());
			for (LocalProducto bp : listaLocalProductos)
				if (bp.getCantidad() > 0)
					kardexService.insertarKardexInicial(producto,
							bp.getLocal(), producto.getPrecio(),
							bp.getCantidad());
		}
	}

	public String impuestoProducto(List<ProductoTarifa> list) {
		String imp = "";
		for (ProductoTarifa pt : list) {
			if (pt.getTarifa().getImpuesto() == Impuesto.IV
					&& pt.getTarifa().getIdSri() == 2)
				imp += "I+";
			else if (pt.getTarifa().getImpuesto() == Impuesto.IC)
				imp += "C+";
			else if (pt.getTarifa().getImpuesto() == Impuesto.IR)
				imp += "R+";
			else
				imp = " ";
		}
		return imp.substring(0, imp.length() - 1);
	}

	private void inicializar(Producto producto) {
		for (TipoPrecioProducto tipoPrecioProducto : producto
				.getTipoPrecioProductos())
			tipoPrecioProducto
					.setValor(redondear(tipoPrecioProducto.getValor()));

		if (producto.getPrecio() != null)
			producto.setPrecio(redondear(producto.getPrecio()));
	}

	public void insertar(Producto producto,
			List<TablaPrecios> listaPrecioProductos) {

		producto.setActivo(true);
		Set<ConstraintViolation<Producto>> violationsProducto = validator
				.validate(producto);
		if (violationsProducto.size() > 0)
			for (ConstraintViolation<Producto> cv : violationsProducto)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (producto.getPrecio().compareTo(new BigDecimal("0")) == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESE EL PRECIO COSTO");
		else if (comprobarUnidadesVacias(producto))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TIENE UNIDADES VACIAS O CON VALOR CERO");
		else if (producto.getGrupo().getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO, "ESCOJA UN GRUPO");
		else if (comprobarTarifasVacias(producto))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TIENE IMPUESTOS REPETIDOS PARA EL PRODUCTO");
		else if (comprobarInclusionIva(producto))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TODO PRODUCTO DEBE CONTENER IVA");
		else if (comprobarPreciosVacias(listaPrecioProductos))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"TIENE TIPOS DE PRECIOS VACIOS O NO A ESCOGIDO EL PVP");
		else {
			if (producto.getEan() == null
					|| producto.getEan().compareToIgnoreCase("") == 0) {

				producto.setEan(generarEan13(producto.getGrupo()));
				Grupo grupo = producto.getGrupo();
				grupo.setContador(grupo.getContador() + 1);
				grupoDao.actualizar(grupo);
				if (producto.getCodigo1() == null
						|| producto.getCodigo1().compareToIgnoreCase("") == 0)
					producto.setCodigo1(producto.getEan());
			}
			if (productoDao.comprobarIndices(Producto.class, "ean",
					producto.getEan(), String.valueOf(producto.getId())))
				presentaMensaje(FacesMessage.SEVERITY_INFO, "EL EAN YA EXISTE",
						"error", false);
			else if (productoDao.comprobarIndices(Producto.class, "codigo1",
					producto.getCodigo1(), String.valueOf(producto.getId())))
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"EL CÓDIGO 1 YA EXISTE", "error", false);
			else if (productoDao.comprobarIndices(Producto.class, "nombre",
					producto.getNombre(), String.valueOf(producto.getId())))
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"EL NOMBRE YA EXISTE", "error", false);
			else {
				producto.setTipoPrecioProductos(new ArrayList<TipoPrecioProducto>());
				for (TablaPrecios tablaPrecios : listaPrecioProductos)
					if (tablaPrecios.getTipoPrecioProducto()
							.getPorcentajePrecioFijo()) {
						tablaPrecios.getTipoPrecioProducto().setValor(
								tablaPrecios.getPorcentaje());
						producto.addTipoPrecioProducto(tablaPrecios
								.getTipoPrecioProducto());
					} else {
						tablaPrecios.getTipoPrecioProducto().setValor(
								tablaPrecios.getPrecio());
						producto.addTipoPrecioProducto(tablaPrecios
								.getTipoPrecioProducto());
					}

				int c = 1;
				for (ProductoUnidad pu : producto.getProductoUnidads())
					pu.setOrden(c++);
				c = 1;
				for (TipoPrecioProducto tp : producto.getTipoPrecioProductos())
					tp.setOrden(c++);
				c = 1;
				for (ProductoTarifa pt : producto.getProductosTarifas()) {
					pt.setOrden(c++);
					pt.setActivo(true);
				}

				productoDao.insertar(producto);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"INSERTÓ PRODUCTO: " + producto.getNombre(), "error",
						true);
			}
		}
	}

	public List<Producto> obtener() {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "order by p.nombre", new Object[] {});
		for (Producto producto : list)
			inicializar(producto);
		return list;
	}

	public List<Producto> obtenerPorDefectoFacturar() {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "inner join fetch p.productosTarifas "
						+ "where p.facturacion=true and p.activo=true "
						+ "order by p.nombre", new Object[] {});
		for (Producto producto : list)
			inicializar(producto);
		return list;
	}

	public List<Producto> obtener(String criterioBusqueda,
			Integer criterioBusquedaGrupo) {
		List<Producto> list = null;
		criterioBusqueda = criterioBusqueda.toUpperCase();

		if (criterioBusqueda.compareToIgnoreCase("") == 0
				&& criterioBusquedaGrupo == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BÚSQUEDA");
		else if (criterioBusquedaGrupo == 0 && criterioBusqueda.length() < 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MINIMO 3 CARACTERES");
		else {
			if (criterioBusquedaGrupo != 0
					&& criterioBusqueda.compareToIgnoreCase("") != 0)
				list = productoDao
						.obtenerPorHql(
								"select distinct p from Producto p inner join p.grupo g "
										+ "inner join fetch p.productoUnidads "
										+ "inner join fetch p.tipoPrecioProductos "
										+ "inner join fetch p.productosTarifas pt "
										+ "where g.id=?1 "
										+ "and (p.nombre like ?2 or p.ean like ?2 or p.codigo1 like ?2) "
										+ "order by p.nombre", new Object[] {
										criterioBusquedaGrupo,
										"%" + criterioBusqueda + "%" });
			else if (criterioBusqueda.compareToIgnoreCase("") != 0)
				list = productoDao
						.obtenerPorHql(
								"select distinct p from Producto p inner join p.grupo g "
										+ "inner join fetch p.productoUnidads "
										+ "inner join fetch p.tipoPrecioProductos "
										+ "inner join fetch p.productosTarifas pt "
										+ "where p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1 "
										+ "order by p.nombre",
								new Object[] { "%" + criterioBusqueda + "%" });
			else if (criterioBusquedaGrupo != 0)
				list = productoDao.obtenerPorHql(
						"select distinct p from Producto p inner join p.grupo g "
								+ "inner join fetch p.productoUnidads "
								+ "inner join fetch p.tipoPrecioProductos "
								+ "inner join fetch p.productosTarifas pt "
								+ "where g.id=?1 order by p.nombre",
						new Object[] { criterioBusquedaGrupo });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUN RESULTADO");
			else
				for (Producto producto : list)
					inicializar(producto);
		}
		return list;
	}

	public List<ControlInventarioReporte> obtenerControlInventario(
			Integer localId) {
		return controlInventarioReporteDao.obtenerPorHql(
				"select new ControlInventarioReporte(p.id, p.ean, p.nombre, 0, p.nombre) "
						+ "from Producto p " + "inner join p.kardexs k "
						+ "inner join k.local l "
						+ "where l.id=?1 order by p.nombre",
				new Object[] { localId });
	}

	public List<Pvp> obtenerListaPreciosConImpuestosPorProducto(
			Producto producto) {
		List<Pvp> listaPreciosConImpuestos = new ArrayList<>();
		for (TipoPrecioProducto tipoPrecioProducto : producto
				.getTipoPrecioProductos()) {
			BigDecimal precioSinImpuesto = new BigDecimal("0");
			BigDecimal precioConImpuesto = new BigDecimal("0");
			if (tipoPrecioProducto.getPorcentajePrecioFijo()) {
				precioSinImpuesto = producto.getPrecio().multiply(
						new BigDecimal("1").add(tipoPrecioProducto.getValor()
								.divide(new BigDecimal("100"))));
				precioConImpuesto = precioConImpuestos(
						producto,
						producto.getPrecio().multiply(
								new BigDecimal("1").add(tipoPrecioProducto
										.getValor().divide(
												new BigDecimal("100")))));
			} else {
				precioSinImpuesto = tipoPrecioProducto.getValor();
				precioConImpuesto = precioConImpuestos(producto,
						tipoPrecioProducto.getValor());
			}
			listaPreciosConImpuestos.add(new Pvp(tipoPrecioProducto.getId(),
					tipoPrecioProducto.getNombre(),
					redondearTotales(precioConImpuesto),
					redondear(precioSinImpuesto), tipoPrecioProducto.getPvp()));
		}
		return listaPreciosConImpuestos;
	}

	public List<String> obtenerListaString(String criterioProductoBusqueda) {
		criterioProductoBusqueda = criterioProductoBusqueda.toUpperCase();
		List<String> list = new ArrayList<String>();
		List<Producto> list1 = obtenerPorCriterioBusqueda(criterioProductoBusqueda);
		if (!list1.isEmpty())
			for (Producto p : list1)
				list.add(p.getEan() + " - " + p.getNombre());
		return list;
	}

	// public List<String> obtenerListaStringPorLocal(
	// String criterioProductoBusqueda, String establecimiento) {
	// criterioProductoBusqueda = criterioProductoBusqueda.toUpperCase();
	// List<String> list = new ArrayList<String>();
	// List<Producto> list1 = obtenerPorLocal(criterioProductoBusqueda,
	// establecimiento);
	// if (!list1.isEmpty())
	// for (Producto p : list1)
	// list.add(p.getEan() + " - " + p.getNombreimprimir());
	// return list;
	// }

	public List<String> obtenerListaStringPorLocal(
			String criterioProductoBusqueda, int localId) {
		criterioProductoBusqueda = criterioProductoBusqueda.toUpperCase();
		List<String> list = new ArrayList<String>();
		List<Producto> list1 = obtenerPorLocal(criterioProductoBusqueda, null,
				localId);
		if (!list1.isEmpty())
			for (Producto p : list1)
				list.add(p.getEan() + " - " + p.getNombre());
		return list;
	}

	public List<Producto> obtenerParaBusqueda(String criterioProductoBusqueda) {
		criterioProductoBusqueda = criterioProductoBusqueda.toUpperCase();
		List<Producto> list = new ArrayList<Producto>();
		if (criterioProductoBusqueda.length() >= 0
				&& criterioProductoBusqueda.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		else {
			list = productoDao
					.obtenerPorHql(
							"select p from Producto p where (p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1) and p.activo=true order by p.nombre",
							new Object[] { "%" + criterioProductoBusqueda + "%" });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
		}
		return list;
	}

	public Producto obtenerPorCodigo(String codigo) {
		Producto p = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "where p.codigo1=?1", new Object[] { codigo }).get(0);
		inicializar(p);
		return p;
	}

	public Producto obtenerPorCodigosAndLocal(Integer localId, String codigo) {
		Producto p = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join p.kardexs k inner join k.local l "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "where l.id=?1 "
						+ "and (p.ean like ?2 or p.codigo1 like ?2) "
						+ "and p.activo=true",
				new Object[] { localId, "%" + codigo + "%" }).get(0);
		inicializar(p);
		return p;
	}

	public List<Producto> obtenerPorCriterioBusqueda(
			String criterioProductoBusqueda) {
		criterioProductoBusqueda = criterioProductoBusqueda.toUpperCase();
		List<Producto> list = new ArrayList<Producto>();
		if (criterioProductoBusqueda.length() >= 0
				&& criterioProductoBusqueda.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		else {
			list = productoDao
					.obtenerPorHql(
							"select distinct p from Producto p "
									+ "where (p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1) "
									+ "and p.activo=true", new Object[] { "%"
									+ criterioProductoBusqueda + "%" });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUN RESULTADO");
		}
		return list;
	}

	public Producto obtenerPorEan(String ean) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "inner join fetch p.productosTarifas "
						+ "where p.ean=?1", new Object[] { ean });

		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"NO SE ENCONTRO NINGUN RESULTADO");
		else
			return list.get(0);
		return null;
	}

	public List<Producto> obtenerPorEanAndLocal(String ean, Integer localId) {
		ean = ean.toUpperCase();
		List<Producto> list = productoDao
				.obtenerPorHql(
						"select distinct p from Producto p "
								+ "inner join p.kardexs k inner join k.local l "
								+ "where (p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1) and l.id=?2  and p.activo=true",
						new Object[] { "%" + ean + "%", localId });
		for (Producto producto : list)
			inicializar(producto);
		return list;
	}

	// public void apean(Producto producto) {
	// // para almacenes patty
	// ln("*****************productoid: "
	// + producto.getId());
	// if (!comprobarEan13(producto.getEan())) {
	// producto.setEan(generarEan13(producto.getGrupo()));
	// productoDao.actualizar(producto);
	// Grupo grupo = producto.getGrupo();
	// grupo.setContador(grupo.getContador() + 1);
	// grupoDao.actualizar(grupo);
	// }
	// }
	public List<Producto> obtenerPorGrupo(Integer grupoId) {
		return productoDao.obtenerPorHql(
				"select distinct p from Producto p inner join p.grupo g "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "inner join fetch p.productosTarifas "
						+ "where g.id=?1 order by p.nombre",
				new Object[] { grupoId });

	}

	public List<Producto> obtenerPorKardexInicial(Integer localId) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join p.kardexs k inner join k.local l "
						+ "where (l.id!=?1 and p.activo=true",
				new Object[] { localId });
		for (Producto producto : list) {
			inicializar(producto);
		}
		return list;
	}

	public List<Producto> obtenerPorLocal(Integer localId) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select p from Producto p "
						+ "inner join p.kardexs k inner join k.local l "
						+ "where l.id=?1 and p.activo=true and k.activo=true "
						+ "order by p.nombre", new Object[] { localId });
		for (Producto producto : list)
			inicializar(producto);

		return list;
	}

	public List<Producto> obtenerPorLocal(String criterioProductoBusqueda,
			Integer localId) {
		criterioProductoBusqueda = criterioProductoBusqueda.toUpperCase();
		List<Producto> list = new ArrayList<Producto>();
		if (criterioProductoBusqueda.length() >= 0
				&& criterioProductoBusqueda.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		else {
			list = productoDao
					.obtenerPorHql(
							"select distinct p from Producto p "
									+ "inner join fetch p.productosTarifas pt "
									+ "inner join fetch p.tipoPrecioProductos tpp "
									+ "inner join p.kardexs k inner join k.local l "
									+ "where l.id=?1 "
									+ "and (p.nombre like ?2 or p.ean like ?2 or p.codigo1 like ?2) "
									+ "and p.activo=true", new Object[] {
									localId,
									"%" + criterioProductoBusqueda + "%" });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUN RESULTADO");
		}
		return list;
	}

	public List<Producto> obtenerPorLocal(String criterioProducto,
			Long productoId, Integer localId) {
		List<Producto> list = new ArrayList<Producto>();

		if ((criterioProducto != null && criterioProducto.length() < 3)
				&& (productoId == null || productoId == 0))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BÚSQUEDA VALIDO");
		else if (criterioProducto != null && criterioProducto.length() > 3)
			list = productoDao
					.obtenerPorHql(
							"select distinct p from Producto p "
									+ "inner join p.kardexs k inner join k.local l "
									+ "inner join fetch p.tipoPrecioProductos tpp "
									+ "inner join fetch p.productosTarifas pt "
									+ "where l.id=?1 "
									+ "and (p.nombre like ?2 or p.ean like ?2 or p.codigo1 like ?2) "
									+ "and p.activo=true",
							new Object[] { localId,
									"%" + criterioProducto.toUpperCase() + "%" });
		else if (productoId != 0)
			list = productoDao.obtenerPorHql(
					"select distinct p from Producto p "
							+ "inner join p.kardexs k inner join k.local l "
							+ "inner join fetch p.tipoPrecioProductos tpp "
							+ "inner join fetch p.productosTarifas pt "
							+ "where l.id=?1 "
							+ "and p.id=?2 and p.activo=true", new Object[] {
							localId, productoId });

		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"NO SE ENCONTRO NINGUN RESULTADO");

		return list;
	}

	public List<Producto> obtenerPorLocalAndGrupo(Integer localId,
			Integer grupoId) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "inner join p.grupo g "
						+ "inner join p.kardexs k inner join k.local l "
						+ "where l.id=?1 " + "and g.id=?2 and p.activo=true "
						+ "order by p.nombre",
				new Object[] { localId, grupoId });
		for (Producto producto : list)
			inicializar(producto);

		return list;
	}

	public List<LocalProducto> obtenerPorLocalProducto(String ean) {
		List<LocalProducto> listaLocalProductos = new ArrayList<LocalProducto>();
		List<Local> listaLocales = localService.obtener(true);
		for (Local local : listaLocales) {
			Kardex kardex = kardexService
					.obtenerSaldoActual(ean, local.getId());
			if (kardex != null)
				listaLocalProductos.add(new LocalProducto(local, kardex
						.getCantidad()));
		}
		return listaLocalProductos;
	}

	public Producto obtenerPorProductoId(Long productoId) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "inner join fetch p.tipoPrecioProductos "
						+ "inner join fetch p.productosTarifas "
						+ "where p.id=?1", new Object[] { productoId });
		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"NO SE ENCONTRO NINGUN RESULTADO");
		else
			return list.get(0);
		return null;
	}

	public List<Producto> obtenerPorProveedor(Integer proveedorid) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select distinct p from Producto p "
						+ "inner join p.detalleIngresos di "
						+ "inner join di.ingreso i "
						+ "inner join i.proveedor pr "
						+ "where pr.id=?1 order by p.nombre",
				new Object[] { proveedorid });
		return list;
	}

	public List<Producto> reporteObtenerTodos() {
		List<Producto> list = productoDao
				.obtenerPorHql(
						"select distinct p from Producto p "
								+ "inner join fetch p.productoUnidads "
								+ "inner join fetch p.tipoPrecioProductos "
								+ "where (pc.valor is not null) "
								+ "order by p.nombre", new Object[] {});
		for (Producto producto : list)
			inicializar(producto);
		return list;
	}

	public Producto obtenerUnidadesPorProductoId(Long productoId) {
		List<Producto> list = productoDao.obtenerPorHql(
				"select p from Producto p "
						+ "inner join fetch p.productoUnidads "
						+ "where p.id=?1", new Object[] { productoId });
		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"NO SE ENCONTRO NINGUN RESULTADO");
		else
			return list.get(0);
		return null;
	}

	public BigDecimal obtenerPvp(BigDecimal precioCosto,
			List<TipoPrecioProducto> list) {
		TipoPrecioProducto tpp = obtenerValorPvp(list);
		if (tpp == null)
			tpp = list.size() >= 2 ? list.get(1) : list.get(0);

		return tpp.getPorcentajePrecioFijo() ? valorConPorcentaje(precioCosto,
				tpp.getValor()) : tpp.getValor();
	}

	public BigDecimal obtenerMayorista(BigDecimal precioCosto,
			List<TipoPrecioProducto> list) {
		TipoPrecioProducto tpp = obtenerValorMayorista(list);
		if (tpp == null)
			tpp = list.size() >= 2 ? list.get(1) : list.get(0);

		return tpp.getPorcentajePrecioFijo() ? valorConPorcentaje(precioCosto,
				tpp.getValor()) : tpp.getValor();
	}

	public BigDecimal obtenerPvpConImpuestos(Producto producto) {
		BigDecimal porcetajeIva = newBigDecimal();
		BigDecimal porcetajeIce = newBigDecimal();
		BigDecimal porcetajeIrbpnr = newBigDecimal();

		for (ProductoTarifa pt : producto.getProductosTarifas()) {
			if (pt.getTarifa().getImpuesto().getId() == 1)
				porcetajeIva = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 2)
				porcetajeIce = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 3)
				porcetajeIrbpnr = pt.getTarifa().getPorcentaje();
		}
		BigDecimal pvpSinImpuesto = obtenerPvp(producto.getPrecio(),
				producto.getTipoPrecioProductos());

		BigDecimal precioConIce = pvpSinImpuesto.multiply(divide(porcetajeIce,
				100).add(new BigDecimal("1")));
		BigDecimal precioConIva = precioConIce.multiply(divide(porcetajeIva,
				100).add(new BigDecimal("1")));

		if (porcetajeIrbpnr.compareTo(new BigDecimal("0")) != 0)
			return precioConIva.add(porcetajeIrbpnr);
		else
			return precioConIva;
	}

	public BigDecimal obtenerMayoristaConImpuestos(Producto producto) {
		BigDecimal porcetajeIva = newBigDecimal();
		BigDecimal porcetajeIce = newBigDecimal();
		BigDecimal porcetajeIrbpnr = newBigDecimal();

		for (ProductoTarifa pt : producto.getProductosTarifas()) {
			if (pt.getTarifa().getImpuesto().getId() == 1)
				porcetajeIva = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 2)
				porcetajeIce = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 3)
				porcetajeIrbpnr = pt.getTarifa().getPorcentaje();
		}
		BigDecimal pvpSinImpuesto = obtenerMayorista(producto.getPrecio(),
				producto.getTipoPrecioProductos());

		BigDecimal precioConIce = pvpSinImpuesto.multiply(divide(porcetajeIce,
				100).add(new BigDecimal("1")));
		BigDecimal precioConIva = precioConIce.multiply(divide(porcetajeIva,
				100).add(new BigDecimal("1")));

		if (porcetajeIrbpnr.compareTo(new BigDecimal("0")) != 0)
			return precioConIva.add(porcetajeIrbpnr);
		else
			return precioConIva;
	}

	public Integer obtenerPvpPorProducto(List<TipoPrecioProducto> list) {
		for (TipoPrecioProducto tpp : list)
			if (tpp.getPvp())
				return tpp.getId();

		presentaMensaje(FacesMessage.SEVERITY_WARN,
				"EL PRODUCTO NO TIENE CONFIGURADO SU PVP");
		return list.get(list.size() - 1).getId();
	}

	public List<PedidoStockProductoReporte> reporteObtenerStockParaPedidoPorLocal(
			Integer localId) {
		return pedidoStockProductoReporteDao
				.obtenerPorHql(
						"select new PedidoStockProductoReporte(p.id, p.nombre, k.cantidad, p.cantidadMinima-k.cantidad ,p.cantidadMinima) "
								+ "from Producto p "
								+ "inner join p.kardexs k "
								+ "inner join k.local l "
								+ "where k.activo=true and k.cantidad<=p.cantidadMinima and l.id=?1 "
								+ "order by p.nombre", new Object[] { localId });
	}

	public List<StockProductoReporte> obtenerStockPorLocal(Integer localId) {
		return stockProductoReporteDao
				.obtenerPorHql(
						"select new StockProductoReporte(p.id, g.nombre, p.nombre, p.cantidadMinima, k.cantidad, '') "
								+ "from Producto p "
								+ "inner join p.grupo g "
								+ "inner join p.kardexs k "
								+ "inner join k.local l "
								+ "where k.activo=true and l.id=?1 "
								+ "order by p.nombre", new Object[] { localId });
	}

	public TipoPrecioProducto obtenerValorPvp(List<TipoPrecioProducto> list) {
		for (TipoPrecioProducto tpp : list)
			if (tpp.getPvp())
				return tpp;
		return null;
	}

	public TipoPrecioProducto obtenerValorMayorista(
			List<TipoPrecioProducto> list) {
		for (TipoPrecioProducto tpp : list)
			if (tpp.getOrden() == 2
					&& tpp.getNombre().compareToIgnoreCase("MAYORISTA") == 0)
				return tpp;
		return null;
	}

	public BigDecimal precioConImpuestos(Producto producto,
			BigDecimal precioReferencial) {
		BigDecimal porcetajeIva = newBigDecimal();
		BigDecimal porcetajeIce = newBigDecimal();
		BigDecimal porcetajeIrbpnr = newBigDecimal();

		for (ProductoTarifa pt : producto.getProductosTarifas()) {
			if (pt.getTarifa().getImpuesto().getId() == 1)
				porcetajeIva = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 2)
				porcetajeIce = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 3)
				porcetajeIrbpnr = pt.getTarifa().getPorcentaje();
		}
		BigDecimal precioConIce = precioReferencial.multiply(divide(
				porcetajeIce, 100).add(new BigDecimal("1")));
		BigDecimal precioConIva = precioConIce.multiply(divide(porcetajeIva,
				100).add(new BigDecimal("1")));

		if (porcetajeIrbpnr.compareTo(new BigDecimal("0")) != 0)
			return precioConIva.add(porcetajeIrbpnr);
		else
			return precioConIva;
	}

	public BigDecimal precioSinImpuestos(Producto producto,
			BigDecimal precioReferencial) {
		BigDecimal porcetajeIva = newBigDecimal();
		BigDecimal porcetajeIce = newBigDecimal();
		BigDecimal porcetajeIrbpnr = newBigDecimal();

		for (ProductoTarifa pt : producto.getProductosTarifas()) {
			if (pt.getTarifa().getImpuesto().getId() == 1)
				porcetajeIva = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 2)
				porcetajeIce = pt.getTarifa().getPorcentaje();
			else if (pt.getTarifa().getImpuesto().getId() == 3)
				porcetajeIrbpnr = pt.getTarifa().getPorcentaje();
		}

		BigDecimal precioSinIva = newBigDecimal();
		BigDecimal precioSinIrbpnr = newBigDecimal();

		if (porcetajeIrbpnr.compareTo(new BigDecimal("0")) != 0) {
			precioSinIrbpnr = precioReferencial.subtract(porcetajeIrbpnr);
			precioSinIva = divide(precioSinIrbpnr, divide(porcetajeIva, 100)
					.add(new BigDecimal("1")));
		} else
			precioSinIva = divide(precioReferencial, divide(porcetajeIva, 100)
					.add(new BigDecimal("1")));

		BigDecimal precioSinIce = divide(precioSinIva,
				divide(porcetajeIce, 100).add(new BigDecimal("1")));

		return precioSinIce;
	}

}