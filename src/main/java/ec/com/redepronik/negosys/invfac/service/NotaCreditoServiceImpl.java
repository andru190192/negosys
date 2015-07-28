package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.EgresoCajaDao;
import ec.com.redepronik.negosys.invfac.dao.FacturaDao;
import ec.com.redepronik.negosys.invfac.dao.NotaCreditoDao;
import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.DetalleNotaCredito;
import ec.com.redepronik.negosys.invfac.entity.EstadoKardex;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.EgresoCaja;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class NotaCreditoServiceImpl implements NotaCreditoService {

	@Autowired
	private NotaCreditoDao notaCreditoDao;

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private FacturaInternaService facturaInternaService;

	@Autowired
	private EgresoCajaDao egresoCajaDao;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private LocalService localService;

	@Autowired
	private LocalEmpleadoCargoService localEmpleadoCargoService;

	@Autowired
	private TarifaService tarifaService;

	public void actualizar(NotaCredito egreso) {
		notaCreditoDao.actualizar(egreso);
	}

	private void actualizarCantidadCuadre(NotaCredito notaCredito,
			Factura factura) {

		for (DetalleNotaCredito dnc : notaCredito.getDetalleNotaCredito()) {
			int i = posicionCuadre(dnc, factura.getDetalleFactura());
			int cantidad = factura.getDetalleFactura().get(i)
					.getCantidadCuadre()
					- dnc.getCantidad();
			if (cantidad < 0)
				cantidad = 0;
			factura.getDetalleFactura().get(i).setCantidadCuadre(cantidad);
		}
	}

	private boolean añadirFRLista(FacturaReporte fr, List<FacturaReporte> list,
			int bodegaId, int fila) {
		Producto p = productoService.obtenerPorEan(fr.getCodigo());
		if (p != null) {
			int precioId = productoService.obtenerPvpPorProducto(p
					.getTipoPrecioProductos());
			Kardex k = kardexService.obtenerSaldoActual(p.getEan(), bodegaId);

			fr.setProducto(p);
			fr.setPrecioId(precioId);
			fr.setPrecioCosto(k.getPrecio());
			fr.setCantidadKardex(k.getCantidad());
			fr.setCodigo(p.getEan());
			fr.setDescripcion(p.getNombre() + "      ");
			fr.setImpuesto(productoService.impuestoProducto(p
					.getProductosTarifas()));

			fr.setPrecioUnitVenta(productoService.calcularPrecio(p.getEan(),
					p.getTipoPrecioProductos(), k, bodegaId, fr.getPrecioId()));

			list.set(fila, fr);
			return true;
		}
		return false;
	}

	public FacturaReporte asignar(DetalleFactura de) {
		Producto producto = productoService.obtenerPorProductoId(de
				.getProducto().getId());

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre());
		fr.setCantidad(de.getCantidad());
		fr.setCantidadKardex(de.getCantidad());
		fr.setPrecioCosto(de.getPrecioCosto());
		fr.setPrecioUnitVenta(de.getPrecioVenta());
		fr.setDescuentoDolares(de.getDescuento());
		fr.setEstado(de.getEstadoProductoVenta());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), producto.getProductoUnidads()));
		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (fr.getEstado() == EstadoProductoVenta.PR) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("P");
		} else if (fr.getEstado() == EstadoProductoVenta.CB) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("C");
		} else if (fr.getEstado() == EstadoProductoVenta.PI) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("PI");
		}

		fr.setDescuentoPorcentaje(multiplicarDivide(de.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public FacturaReporte asignar(DetalleNotaCredito dnc) {
		Producto producto = productoService.obtenerPorProductoId(dnc
				.getProducto().getId());

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre());
		fr.setCantidad(dnc.getCantidad());
		fr.setCantidadKardex(dnc.getCantidad());
		fr.setPrecioUnitVenta(dnc.getPrecioVenta());
		fr.setDescuentoDolares(dnc.getDescuento());
		fr.setEstado(dnc.getEstadoProductoVenta());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), producto.getProductoUnidads()));
		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (fr.getEstado() == EstadoProductoVenta.PR) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("P");
		} else if (fr.getEstado() == EstadoProductoVenta.CB) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("C");
		} else if (fr.getEstado() == EstadoProductoVenta.PI) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("PI");
		}

		fr.setDescuentoPorcentaje(multiplicarDivide(dnc.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public FacturaReporte asignar(FacturaReporte facturaReporte) {
		Producto producto = productoService.obtenerPorProductoId(facturaReporte
				.getProducto().getId());
		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre() + "      ");
		fr.setPrecioId(facturaReporte.getPrecioId());
		fr.setCantidad(facturaReporte.getCantidad());
		fr.setCantidadCuadre(facturaReporte.getCantidadCuadre());
		fr.setCantidadKardex(facturaReporte.getCantidadKardex());
		fr.setPrecioUnitVenta(facturaReporte.getPrecioUnitVenta());
		fr.setPrecioUnitVentaCuadre(facturaReporte.getPrecioUnitVentaCuadre());
		fr.setPrecioCosto(facturaReporte.getPrecioCosto());
		fr.setDescuentoDolares(facturaReporte.getDescuentoDolares());
		fr.setDescuentoDolaresCuadre(facturaReporte.getDescuentoDolaresCuadre());
		fr.setDescuentoPorcentaje(facturaReporte.getDescuentoPorcentaje());
		fr.setEstado(facturaReporte.getEstado());

		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (facturaReporte.getCantidad().compareTo(0) > 0)
			fr.setNombreCantidad(productoService.convertirUnidadString(
					fr.getCantidad(), producto.getProductoUnidads()));

		if (fr.getEstado() == EstadoProductoVenta.PR) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("P");
		} else if (fr.getEstado() == EstadoProductoVenta.CB) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("C");
		} else if (fr.getEstado() == EstadoProductoVenta.PI) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("PI");
		}

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public CantidadFactura calcularCantidad(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidad() < 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD MAYOR O IGUAL QUE CERO");
			fr.setCantidad(0);
		} else if (fr.getEstado().getId() != 3)
			if (fr.getCantidad() > fr.getCantidadKardex()) {
				presentaMensaje(
						FacesMessage.SEVERITY_ERROR,
						"INGRESE UNA CANTIDAD MENOR A LA EXISTENTE: "
								+ fr.getCantidadKardex());
				fr.setCantidad(0);
			}
		cambiarFRenLista(fr, list);
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularCantidadFactura(CantidadFactura cf,
			FacturaReporte fr) {
		BigDecimal total = fr.getImporte();
		BigDecimal tAux = multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad());
		cf.setStSinImpuesto(cf.getStSinImpuesto().add(total));
		for (ProductoTarifa pt : fr.getProducto().getProductosTarifas()) {
			if (pt.getTarifa().getIdSri() == 2)
				cf.setSt12(cf.getSt12().add(total));
			else if (pt.getTarifa().getIdSri() == 0)
				cf.setSt0(cf.getSt0().add(total));
			else if (pt.getTarifa().getIdSri() == 6)
				cf.setStNoObjetoIva(cf.getStNoObjetoIva().add(total));
			else if (pt.getTarifa().getIdSri() == 7)
				cf.setStExentoIva(cf.getStExentoIva().add(total));
			if (pt.getTarifa().getImpuesto() == Impuesto.IC)
				cf.setValorIce(cf.getValorIce()
						.add(multiplicarDivide(tAux, pt.getTarifa()
								.getPorcentaje())));
			if (pt.getTarifa().getImpuesto() == Impuesto.IR)
				cf.setValorIRBPNR(cf.getValorIRBPNR().add(
						pt.getTarifa().getPorcentaje()));
		}
		cf.settDescuentoProducto(cf.gettDescuentoProducto().add(
				fr.getDescuentoDolares()));
		cf.settDescuento(cf.gettDescuentoProducto().add(
				cf.gettDescuentoEgreso()));

		BigDecimal iva = tarifaService.obtenerPorTarifaId((short) 2)
				.getPorcentaje();
		cf.setIva12(iva(cf.getSt12().add(cf.getValorIce()), iva));

		cf.setValorTotal(cf.getStSinImpuesto().add(cf.getValorIce())
				.add(cf.getValorIRBPNR()).add(cf.getIva12())
				.add(cf.getPropina()));

		cf.setValorTotal(redondearTotales(cf.getValorTotal()));

		return cf;
	}

	public void calcularImporte(FacturaReporte fr) {
		if (fr.getCantidad() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA CANTIDAD");
		if (compareTo(fr.getPrecioUnitVenta(), "0") <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN PRECIO");
		else
			fr.setImporte(multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad()));
	}

	private int cambiarFRenLista(FacturaReporte fr, List<FacturaReporte> list) {
		int idx = list.indexOf(fr);
		if (idx >= 0)
			list.set(idx, asignar(fr));
		return idx;
	}

	public boolean cargarProductoLista(FacturaReporte facturaReporte,
			List<FacturaReporte> list, int bodegaId) {
		int fila = list.indexOf(facturaReporte);
		facturaReporte.setCodigo(facturaReporte.getCodigo().split(" - ")[0]);
		if (fila == 0)
			return añadirFRLista(facturaReporte, list, bodegaId, fila);
		else if (list.get(fila - 1).getCantidad() != null)
			return añadirFRLista(facturaReporte, list, bodegaId, fila);
		return false;
	}

	public boolean convertirListaFacturaReporteListaFacturaDetalle(
			List<FacturaReporte> list, NotaCredito notaCredito) {
		boolean bn = true;
		notaCredito.setDetalleNotaCredito(new ArrayList<DetalleNotaCredito>());
		for (FacturaReporte f : quitarProductosCantidadCero(list)) {
			bn = false;
			Producto producto = productoService.obtenerPorProductoId(f
					.getProducto().getId());
			DetalleNotaCredito dnc = new DetalleNotaCredito();
			dnc.setProducto(producto);
			dnc.setCantidad(f.getCantidad());
			dnc.setPrecioVenta(f.getPrecioUnitVenta());
			dnc.setDescuento(f.getDescuentoDolares());

			notaCredito.addDetalleNotaCredito(dnc);
		}
		if (bn)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY CANTIDADES A FACTURAR");

		return bn;
	}

	public List<DetalleNotaCredito> duplicarDetalleNotaCredito(
			List<DetalleNotaCredito> detalleNotaCreditos) {
		List<DetalleNotaCredito> list = new ArrayList<DetalleNotaCredito>();
		if (detalleNotaCreditos != null)
			for (DetalleNotaCredito de : detalleNotaCreditos) {
				if (de != null) {
					DetalleNotaCredito dNotaCredito = new DetalleNotaCredito();
					dNotaCredito.setOrden(de.getOrden());
					dNotaCredito.setCantidad(de.getCantidad());
					dNotaCredito.setPrecioVenta(de.getPrecioVenta());
					dNotaCredito.setDescuento(de.getDescuento());
					dNotaCredito.setProducto(de.getProducto());
					list.add(dNotaCredito);
				}
			}

		return list;
	}

	public NotaCredito duplicarNotaCredito(NotaCredito egreso,
			int tipoDocumentoId) {
		NotaCredito nc = new NotaCredito();
		nc.setCajero(egreso.getCajero());

		// nc.setDetalleNotaCreditos(new ArrayList<DetalleNotaCredito>());
		// for (DetalleNotaCredito detalleNotaCredito :
		// duplicarDetalleNotaCredito(egreso
		// .getDetalleNotaCreditos()))
		// nc.addDetalleNotaCredito(detalleNotaCredito);
		//
		// nc.setLocal(egreso.getLocal());
		// nc.setTipodocumento(tipoDocumentoService.obtenerPorId(tipoDocumentoId));
		//

		return nc;
	}

	private void generarKardexNotaCredito(
			List<DetalleNotaCredito> detalleNotaCreditos, int notaCreditoId,
			Local local) {
		for (DetalleNotaCredito de : sumarCantidades(detalleNotaCreditos)) {
			if (de.getProducto().getKardex()) {
				Kardex kardex = new Kardex();
				kardex.setEstadoKardex(EstadoKardex.DV);
				kardex.setProducto(de.getProducto());
				kardex.setLocal(local);
				kardex.setNota("NOTA CREDITO # " + notaCreditoId);
				kardex.setFecha(timestamp());
				kardex.setCantidad(de.getCantidad() * -1);
				kardex.setPrecio(de.getProducto().getPrecio());
				kardex.setActivo(false);

				kardexService.insertar(kardex);

				Producto producto = productoService.obtenerPorProductoId(de
						.getProducto().getId());
				Map<String, Object> parametro = kardexService.ponderarPrecio(
						producto, local.getId(), kardex.getPrecio(),
						kardex.getCantidad());
				BigDecimal valorNuevoPrecio = (BigDecimal) parametro
						.get("precio");
				int cantidad = (Integer) parametro.get("cantidad");
				kardexService.insertar(kardexService.generarKardexSaldo(kardex,
						cantidad, valorNuevoPrecio, false));
			}
		}
	}

	public NotaCredito insertar(NotaCredito notaCredito, Local local) {
		notaCredito.setFecha(timestamp());
		notaCredito.setCajero(notaCredito.getFactura().getCajero());
		notaCredito.setEstablecimiento(notaCredito.getFactura()
				.getEstablecimiento());

		notaCredito.setPuntoEmision("0");
		notaCredito.setSecuencia("0");

		int c = 1;
		for (DetalleNotaCredito de : notaCredito.getDetalleNotaCredito())
			de.setOrden(c++);

		notaCreditoDao.insertar(notaCredito);
		// actualizar aqui la cantidad cuadre
		actualizarCantidadCuadre(notaCredito, notaCredito.getFactura());
		facturaDao.actualizar(notaCredito.getFactura());

		generarKardexNotaCredito(
				duplicarDetalleNotaCredito(notaCredito.getDetalleNotaCredito()),
				notaCredito.getId(), local);

		presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTÓ NOTA DE CREDITO #"
				+ notaCredito.getId());
		return notaCredito;
	}

	public List<NotaCredito> obtener(Boolean activo, int tipoDocumentoId) {
		List<NotaCredito> list = notaCreditoDao
				.obtenerPorHql(
						"select nc from NotaCredito nc inner join nc.tipodocumento td where td.tipodocumentoid=?1 order by nc.fechainicio asc",
						new Object[] { tipoDocumentoId });
		return list;
	}

	public List<Factura> obtener(Integer criterioBusquedaEstado,
			String criterioBusquedaCliente, String criterioBusquedaCodigo,
			String criterioBusquedaDetalle, Date criterioBusquedaFechaDocumento) {
		List<Factura> list = null;
		if (criterioBusquedaEstado == 0
				&& criterioBusquedaCliente.compareToIgnoreCase("") == 0
				&& criterioBusquedaCodigo.compareToIgnoreCase("") == 0
				&& criterioBusquedaDetalle.compareToIgnoreCase("") == 0
				&& criterioBusquedaFechaDocumento == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else if (criterioBusquedaCliente.length() >= 1
				&& criterioBusquedaCliente.length() <= 4)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 4 CARACTERES PARA LA BÚSQUEDA POR CLIENTES");
		else if (!criterioBusquedaCodigo.matches("[0-9]*"))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR DOCUMENTOS");
		else if (criterioBusquedaCodigo.compareToIgnoreCase("") != 0
				&& criterioBusquedaCodigo.charAt(0) == '0')
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO SE PERMITE CEROS AL INICIO PARA LA BÚSQUEDA POR DOCUMENTOS");
		else if (criterioBusquedaDetalle.length() >= 1
				&& criterioBusquedaDetalle.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR PRODUCTOS");
		else {
			criterioBusquedaCliente = criterioBusquedaCliente.toUpperCase();
			criterioBusquedaCodigo = criterioBusquedaCodigo.toUpperCase();
			criterioBusquedaDetalle = criterioBusquedaDetalle.toUpperCase();

			if (criterioBusquedaCliente.compareTo("") != 0) {
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join fetch f.notaCredito nc "
										+ "inner join fetch nc.detalleNotaCredito "
										+ "inner join f.cliente c inner join c.persona p "
										+ "where (p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "order by nc.fecha",
								new Object[] { "%" + criterioBusquedaCliente
										+ "%" });
			} else if (criterioBusquedaCodigo.compareTo("") != 0) {
				list = facturaDao
						.obtenerPorHql("select distinct f from Factura f "
								+ "inner join fetch f.notaCredito nc "
								+ "inner join fetch nc.detalleNotaCredito "
								+ "where nc.notacreditoid=?1 "
								+ "order by nc.fecha", new Object[] { Integer
								.parseInt(criterioBusquedaCodigo) });
			} else if (criterioBusquedaDetalle.compareTo("") != 0) {
				list = new ArrayList<Factura>();
				List<Factura> list1 = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join fetch f.notaCredito nc "
										+ "inner join fetch nc.detalleNotaCredito dnc "
										+ "inner join fetch dnc.producto p "
										+ "where (p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1) "
										+ "order by nc.fecha",
								new Object[] { "%" + criterioBusquedaDetalle
										+ "%" });
				for (Factura egreso : list1) {
					facturaDao.evict(egreso);
					list.add(facturaDao.obtenerPorId(Factura.class,
							egreso.getId()));
				}
			} else if (criterioBusquedaFechaDocumento != null)
				list = facturaDao.obtenerPorHql(
						"select distinct f from Factura f "
								+ "inner join fetch f.notaCredito nc "
								+ "inner join fetch nc.detalleNotaCredito dnc "
								+ "inner join fetch dnc.producto p "
								+ "where nc.fecha>=?1 and nc.fecha<=?2 "
								+ "order by nc.fecha",
						new Object[] {
								criterioBusquedaFechaDocumento,
								new Date(criterioBusquedaFechaDocumento
										.getTime() + 86399999) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public List<Factura> obtenerTodos() {
		return facturaDao.obtenerPorHql("select distinct f from Factura f "
				+ "inner join fetch f.notaCredito nc "
				+ "inner join fetch nc.detalleNotaCredito "
				+ "order by nc.fecha", new Object[] {});
	}

	public List<NotaCredito> obtenerDetalleNotaCreditoPorEstado(
			Date fechaInicio, Date fechaFin, int estado) {
		List<NotaCredito> list = new ArrayList<NotaCredito>();
		if (fechaInicio == null && fechaFin == null && estado == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			list = notaCreditoDao
					.obtenerPorHql(
							"select distinct nc from NotaCredito nc "
									+ "inner join nc.tipodocumento td "
									+ "inner join fetch nc.detalleegresos de "
									+ "inner join fetch de.estadoProductoNotaCredito epe "
									+ "where (td.tipodocumentoid=1 or td.tipodocumentoid=8) "
									+ "and nc.fechainicio>=?1 and nc.fechainicio<=?2 "
									+ "and epe.estadoproductoegresoid=?3 "
									+ "order by nc.fechainicio asc",
							new Object[] { fechaInicio, dateCompleto(fechaFin),
									(short) estado });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public NotaCredito obtenerPorCodigo(String codigo) {
		NotaCredito egreso = null;
		List<NotaCredito> lista = notaCreditoDao.obtenerPorHql(
				"select nc from NotaCredito nc where nc.codigodocumento=?1",
				new Object[] { codigo });
		if (!lista.isEmpty()) {
			egreso = lista.get(0);
		}
		return egreso;
	}

	public List<NotaCredito> obtenerPorEstado(String criterioBusquedaCliente,
			String criterioBusquedaDocumento, Date criterioBusquedafechaInicio,
			Date criterioBusquedafechaFin) {
		List<NotaCredito> list = new ArrayList<NotaCredito>();
		if (criterioBusquedaCliente == null
				&& criterioBusquedafechaInicio == null
				&& criterioBusquedafechaFin == null
				&& (criterioBusquedaDocumento == null || criterioBusquedaDocumento
						.compareTo("") == 0))
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"INGRESE ALGÚN CRITERIO DE BÚSQUEDA");
		else if (criterioBusquedafechaInicio != null
				&& criterioBusquedafechaFin != null
				&& criterioBusquedafechaInicio.after(criterioBusquedafechaFin))
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"LA FECHA DE INICIO DEBE SER MENOR A LA FECHA FINAL");
		else {
			if (criterioBusquedaCliente != null
					&& criterioBusquedaCliente.compareTo("") != 0)
				list = notaCreditoDao
						.obtenerPorHql(
								"select distinct nc from NotaCredito nc "
										+ "inner join nc.tipodocumento td "
										+ "inner join nc.cliente c inner join c.persona p "
										+ "where p.cedula=?1 and "
										+ "(td.tipodocumentoid=1 or td.tipodocumentoid=8)"
										+ " and nc.pagado=false and nc.activo=true order by nc.fechainicio asc",
								new Object[] { criterioBusquedaCliente });
			else if (criterioBusquedaDocumento != null
					&& criterioBusquedaDocumento.compareTo("") != 0)
				list = notaCreditoDao
						.obtenerPorHql(
								"select distinct nc from NotaCredito nc "
										+ "inner join nc.tipodocumento td "
										+ "inner join nc.cliente c inner join c.persona p "
										+ "where nc.codigodocumento like ?1 and "
										+ "(td.tipodocumentoid=1 or td.tipodocumentoid=8)"
										+ " and nc.pagado=false and nc.activo=true order by nc.fechainicio asc",
								new Object[] { "%" + criterioBusquedaDocumento
										+ "%" });
			else if (criterioBusquedafechaInicio != null
					&& criterioBusquedafechaFin != null
					&& !criterioBusquedafechaInicio
							.after(criterioBusquedafechaFin))
				list = notaCreditoDao
						.obtenerPorHql(
								"select distinct nc from NotaCredito nc "
										+ "inner join nc.tipodocumento td "
										+ "inner join nc.cliente c inner join c.persona p "
										+ "where nc.fechainicio>=?1 and nc.fechainicio<=?2 and "
										+ "(td.tipodocumentoid=1 or td.tipodocumentoid=8)"
										+ " and nc.pagado=false and nc.activo=true "
										+ "order by nc.fechainicio asc",
								new Object[] {
										criterioBusquedafechaInicio,
										new Date(criterioBusquedafechaFin
												.getTime() + 86399999) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"EL CLIENTE NO TIENE FACTURAS PENDIENTES");
		}
		return list;
	}

	public NotaCredito obtenerPorNotaCreditoId(Integer notaCreditoId) {
		return (NotaCredito) facturaDao
				.obtenerPorHql(
						"select distinct f from Factura f "
								+ "inner join fetch f.notaCredito nc "
								+ "inner join fetch nc.detalleNotaCredito "
								+ "where nc.id=?1",
						new Object[] { notaCreditoId }).get(0).getNotaCredito();
	}

	public int posicion(FacturaReporte facturaReporte,
			List<DetalleNotaCredito> detalleNotaCreditos) {
		BigDecimal precioVenta = facturaReporte.getPrecioUnitVenta();
		for (DetalleNotaCredito detalleNotaCredito : detalleNotaCreditos)
			if ((facturaReporte.getProducto().getId()
					.compareTo(detalleNotaCredito.getProducto().getId())) == 0
					&& (facturaReporte.getCantidad()
							.compareTo(detalleNotaCredito.getCantidad())) == 0
					&& (precioVenta.compareTo(redondear(detalleNotaCredito
							.getPrecioVenta())) == 0))
				return detalleNotaCreditos.indexOf(detalleNotaCredito);

		return -1;
	}

	private int posicionCuadre(DetalleNotaCredito dnc,
			List<DetalleFactura> detalleFacturas) {
		for (DetalleFactura df : detalleFacturas)
			if ((dnc.getProducto().getId().compareTo(df.getProducto().getId())) == 0
					&& (dnc.getCantidad().compareTo(df.getCantidad())) >= 0)
				return detalleFacturas.indexOf(df);

		for (DetalleFactura df : detalleFacturas)
			if (dnc.getProducto().getId().compareTo(df.getProducto().getId()) == 0)
				return detalleFacturas.indexOf(df);

		return -1;
	}

	public List<FacturaReporte> quitarProductosCantidadCero(
			List<FacturaReporte> list) {
		if (list.size() > 1
				&& list.get(list.size() - 1).getProducto().getId() == null)
			list.remove(list.size() - 1);
		List<FacturaReporte> listFacturaReporte = new ArrayList<FacturaReporte>();
		for (FacturaReporte f : list)
			if (f.getCantidad() != 0)
				listFacturaReporte.add(f);
		return listFacturaReporte;
	}

	public CantidadFactura redondearCantidadFactura(CantidadFactura cf) {
		cf.setStSinImpuesto(redondearTotales(cf.getStSinImpuesto()));
		cf.setSt12(redondearTotales(cf.getSt12()));
		cf.setSt0(redondearTotales(cf.getSt0()));
		cf.setStNoObjetoIva(redondearTotales(cf.getStNoObjetoIva()));
		cf.setStExentoIva(redondearTotales(cf.getStExentoIva()));
		cf.settDescuentoProducto(redondearTotales(cf.gettDescuentoProducto()));
		cf.settDescuentoEgreso(redondearTotales(cf.gettDescuentoEgreso()));
		cf.settDescuento(redondearTotales(cf.gettDescuento()));
		cf.setValorIce(redondearTotales(cf.getValorIce()));
		cf.setValorIRBPNR(redondearTotales(cf.getValorIRBPNR()));
		cf.setIva12(redondearTotales(cf.getIva12()));
		cf.setPropina(redondearTotales(cf.getPropina()));
		cf.setValorTotal(redondearTotales(cf.getValorTotal()));
		return cf;
	}

	public List<EgresoCaja> reporteEgresoCaja(int cajero, Date fecha) {
		if (fecha == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA FECHA");
		else
			return egresoCajaDao
					.obtenerPorHql(
							"select new EgresoCaja(en.id, nc.fecha, nc.establecimiento||'-'||nc.puntoEmision||'-'||nc.secuencia, "
									+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, p.apellido||' '||p.nombre, en.cuota, en.cuota) "
									+ "from Factura f "
									+ "inner join f.notaCredito nc "
									+ "inner join f.cliente c "
									+ "inner join c.persona p "
									+ "inner join f.entradas en "
									+ "inner join nc.cajero ca "
									+ "where ca.id=?1 "
									+ "and nc.fecha>=?2 and nc.fecha<=?3 order by nc.fecha",
							new Object[] { cajero, fecha, dateCompleto(fecha) });
		return new ArrayList<EgresoCaja>();
	}

	public List<DetalleNotaCredito> sumarCantidades(
			List<DetalleNotaCredito> detalleNotaCreditos) {
		List<DetalleNotaCredito> detalleNotaCreditosFinal = new ArrayList<DetalleNotaCredito>();

		for (int i = 0; i < detalleNotaCreditos.size(); i++) {
			DetalleNotaCredito detalleNotaCredito = duplicarDetalleNotaCredito(
					detalleNotaCreditos.subList(i, i + 1)).get(0);

			for (int j = i + 1; j < detalleNotaCreditos.size(); j++) {
				DetalleNotaCredito dNotaCredito = detalleNotaCreditos.get(j);
				if (detalleNotaCredito.getProducto().getId()
						.compareTo(dNotaCredito.getProducto().getId()) == 0) {
					detalleNotaCredito.setCantidad(detalleNotaCredito
							.getCantidad() + dNotaCredito.getCantidad());
					detalleNotaCreditos.remove(j);
					j--;
				}
			}
			detalleNotaCreditosFinal.add(detalleNotaCredito);
		}
		return detalleNotaCreditosFinal;
	}

	public CantidadFactura sumarCantidadFinal(List<FacturaReporte> list) {
		CantidadFactura cantidadFactura = new CantidadFactura();
		if (list == null || list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "NO HAY DETALLES");
		else
			for (FacturaReporte f : list)
				if (f.getCantidad() > 0)
					cantidadFactura = calcularCantidadFactura(cantidadFactura,
							f);

		return redondearCantidadFactura(cantidadFactura);
	}

}