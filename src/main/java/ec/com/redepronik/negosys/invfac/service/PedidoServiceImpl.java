package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.Utils.invertirPalabra;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.divide;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.PedidoDao;
import ec.com.redepronik.negosys.invfac.entity.DetallePedido;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Pedido;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	private PedidoDao pedidoDao;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private LocalService localService;

	@Autowired
	private TarifaService tarifaService;

	public void actualizar(Pedido pedido) {
		pedidoDao.actualizar(pedido);
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

	public FacturaReporte asignar(DetallePedido dp) {
		Producto producto = productoService.obtenerPorProductoId(dp
				.getProducto().getId());

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre());
		fr.setCantidad(dp.getCantidad());
		fr.setPrecioUnitVenta(dp.getPrecioVenta());
		fr.setDescuentoDolares(dp.getDescuento());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), producto.getProductoUnidads()));
		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (fr.getEstado().getId() == 2) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("P");
		} else if (fr.getEstado().getId() == 3) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("C");
		} else if (fr.getEstado().getId() == 4) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("PI");
		}

		fr.setDescuentoPorcentaje(multiplicarDivide(dp.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public FacturaReporte asignar(DetallePedido dp, int bodegaId) {
		Producto p = productoService.obtenerPorProductoId(dp.getProducto()
				.getId());
		Kardex k = kardexService.obtenerSaldoActual(p.getEan(), bodegaId);

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(p);
		fr.setCodigo(p.getEan());
		fr.setDescripcion(p.getNombre());
		fr.setCantidad(dp.getCantidad());
		fr.setCantidadKardex(k.getCantidad());
		fr.setPrecioUnitVenta(dp.getPrecioVenta());
		fr.setDescuentoDolares(dp.getDescuento());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), p.getProductoUnidads()));

		if (fr.getEstado().getId() == 2) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("P");
		} else if (fr.getEstado().getId() == 3) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("C");
		} else if (fr.getEstado().getId() == 4) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("PI");
		}

		fr.setDescuentoPorcentaje(multiplicarDivide(dp.getDescuento(), 100,
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

		if (fr.getEstado().getId() == 2) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("P");
		} else if (fr.getEstado().getId() == 3) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setImpuesto("C");
		} else if (fr.getEstado().getId() == 4) {
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
			if (pt.getTarifa().getImpuesto().getId() == 2)
				cf.setValorIce(cf.getValorIce()
						.add(multiplicarDivide(tAux, pt.getTarifa()
								.getPorcentaje())));
			if (pt.getTarifa().getImpuesto().getId() == 3)
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

	public CantidadFactura calcularDescuentoDolares(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidad() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA APLICAR EL DESCUENTO");
			fr.setDescuentoDolares(newBigDecimal());
		} else {
			BigDecimal imp = multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad());
			if (compareTo(fr.getDescuentoDolares(), "0") < 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN DESCUENTO MAYOR QUE CERO");
				fr.setDescuentoDolares(newBigDecimal());
			} else if (compareTo(fr.getDescuentoDolares(), imp) > 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN DESCUENTO MENOR O IGUAL QUE EL IMPORTE");
				fr.setDescuentoDolares(imp);
			}
			fr.setImporte(imp.subtract(fr.getDescuentoDolares()));
			if (compareTo(fr.getDescuentoDolares(), imp) == 0) {
				fr.setEstado(EstadoProductoVenta.PR);
				return cambiarEstado(fr, list);
			} else {
				fr.setEstado(EstadoProductoVenta.NR);
				return cambiarEstado(fr, list);
			}
		}
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularDescuentoPorcentaje(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidad() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA APLICAR EL DESCUENTO");
			fr.setDescuentoPorcentaje(newBigDecimal());
		} else {
			BigDecimal imp = multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad());
			if (compareTo(fr.getDescuentoPorcentaje(), "0") < 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN DESCUENTO MAYOR QUE CERO");
				fr.setDescuentoPorcentaje(newBigDecimal());
			} else if (compareTo(fr.getDescuentoPorcentaje(), "100") > 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN DESCUENTO MENOR O IGUAL AL 100%");
				fr.setDescuentoPorcentaje(newBigDecimal(100));
			}
			fr.setDescuentoDolares(multiplicarDivide(imp,
					fr.getDescuentoPorcentaje(), 100));
			fr.setImporte(imp.subtract(fr.getDescuentoDolares()));
			if (compareTo(fr.getDescuentoPorcentaje(), "100") == 0) {
				fr.setEstado(EstadoProductoVenta.PR);
				return cambiarEstado(fr, list);
			} else {
				fr.setEstado(EstadoProductoVenta.NR);
				return cambiarEstado(fr, list);
			}
		}
		return sumarCantidadFinal(list);
	}

	public void calcularImporte(FacturaReporte fr) {
		if (fr.getCantidad() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA CANTIDAD");
		if (compareTo(fr.getPrecioUnitVenta(), "0") <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN PRECIO");
		else
			fr.setImporte(multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad()));
	}

	public CantidadFactura calcularImporte(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidad() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA CAMBIAR EL IMPORTE");
			fr.setImporte(newBigDecimal());
		} else {
			if (compareTo(fr.getImporte(), "0") <= 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN IMPORTE MAYOR QUE CERO");
				fr.setPrecioUnitVenta(fr.getPrecioCosto());
				fr.setDescuentoDolares(newBigDecimal());
				fr.setDescuentoPorcentaje(newBigDecimal());
			} else {
				fr.setPrecioUnitVenta(divide(fr.getImporte(), fr.getCantidad()));
				if (compareTo(fr.getPrecioUnitVenta(), fr.getPrecioCosto()) < 0)
					presentaMensaje(FacesMessage.SEVERITY_WARN,
							"ESTA FACTURANDO CON UN P.UNIT. MENOR QUE EL PRECIO COSTO");
			}
			cambiarFRenLista(fr, list);
		}
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularPrecio(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (compareTo(fr.getPrecioUnitVenta(), "0") <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN P.UNIT. MAYOR QUE CERO");
			fr.setPrecioUnitVenta(fr.getPrecioCosto());
		} else if (compareTo(fr.getPrecioUnitVenta(), fr.getPrecioCosto()) < 0)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"ESTA FACTURANDO CON UN P.UNIT. MENOR QUE EL PRECIO COSTO");
		cambiarFRenLista(fr, list);
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularTipoPrecio(FacturaReporte fr,
			List<FacturaReporte> list) {
		Producto producto = productoService.obtenerPorProductoId(fr
				.getProducto().getId());
		fr.setPrecioUnitVenta(productoService.calcularPrecio(producto,
				fr.getPrecioId()));
		if (fr.getCantidad() != null) {
			calcularImporte(fr);
			cambiarFRenLista(fr, list);
		}
		return sumarCantidadFinal(list);
	}

	public CantidadFactura cambiarEstado(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidad() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA CAMBIAR EL ESTADO");
			fr.setEstado(EstadoProductoVenta.NR);
		} else {
			if (fr.getEstado().getId() == 1) {
				fr.setEstado(EstadoProductoVenta.NR);
				fr.setImpuesto(productoService.impuestoProducto(fr
						.getProducto().getProductosTarifas()));
				if (compareTo(fr.getDescuentoDolares(),
						multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())) == 0)
					fr.setDescuentoDolares(newBigDecimal());

				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ "      ");
			} else if (fr.getEstado().getId() == 2) {
				fr.setEstado(EstadoProductoVenta.PR);
				fr.setImpuesto("P");
				fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
						fr.getCantidad()));
				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ " PROMO");
			} else if (fr.getEstado().getId() == 3) {
				fr.setEstado(EstadoProductoVenta.CB);
				fr.setImpuesto("C");
				fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
						fr.getCantidad()));
				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ " CAMBI");
			} else if (fr.getEstado().getId() == 4) {
				fr.setEstado(EstadoProductoVenta.PI);
				fr.setImpuesto("PI");
				fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
						fr.getCantidad()));
				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ " PRO I");
			}
			BigDecimal imp = multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad());
			fr.setImporte(imp.subtract(fr.getDescuentoDolares()));
			fr.setDescuentoPorcentaje(multiplicarDivide(
					fr.getDescuentoDolares(), 100, imp));
		}
		return sumarCantidadFinal(list);
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

	public boolean convertirListaFacturaReporteListaPedidoDetalle(
			List<FacturaReporte> list, Pedido pedido) {
		boolean bn = true;
		pedido.setDetallePedido(new ArrayList<DetallePedido>());
		for (FacturaReporte f : quitarProductosCantidadCero(list)) {
			bn = false;
			Producto producto = productoService.obtenerPorProductoId(f
					.getProducto().getId());
			DetallePedido detallePedido = new DetallePedido();
			detallePedido.setProducto(producto);
			detallePedido.setCantidad(f.getCantidad());
			detallePedido.setPrecioVenta(f.getPrecioUnitVenta());
			detallePedido.setDescuento(f.getDescuentoDolares());
			detallePedido.setEstadoProductoVenta(f.getEstado());
			pedido.addDetallePedido(detallePedido);
		}
		if (bn)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY CANTIDADES A FACTURAR");

		return bn;
	}

	public List<DetallePedido> duplicarDetallePedido(
			List<DetallePedido> detallePedidos) {
		List<DetallePedido> list = new ArrayList<DetallePedido>();
		if (detallePedidos != null)
			for (DetallePedido de : detallePedidos) {
				if (de != null) {
					DetallePedido dPedido = new DetallePedido();
					dPedido.setOrden(de.getOrden());
					dPedido.setEstadoProductoVenta(de.getEstadoProductoVenta());
					dPedido.setProducto(de.getProducto());
					dPedido.setCantidad(de.getCantidad());
					dPedido.setDescuento(de.getDescuento());

					list.add(dPedido);
				}
			}

		return list;
	}

	public void eliminar(Pedido pedido, String login, String pass) {
		if (empleadoService.autorizacion(login, pass)) {
			pedido.setActivo(false);
			pedidoDao.actualizar(pedido);
		}
	}

	public Pedido insertar(Pedido pedido) {
		pedido.setCajero(empleadoService.obtenerEmpleadoCargoPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 4));
		pedido.setFecha(timestamp());
		pedido.setActivo(true);
		pedido.setPuntoEmision("0");
		pedido.setSecuencia("0");

		int c = 1;
		for (DetallePedido de : pedido.getDetallePedido())
			de.setOrden(c++);

		pedidoDao.insertar(pedido);

		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"INSERTÓ PEDIDO #" + pedido.getId());
		return pedido;
	}

	public List<Pedido> obtener(String criterioBusquedaCliente,
			String criterioBusquedaCodigo, String criterioBusquedaDetalle,
			Date criterioBusquedaFechaDocumento) {
		List<Pedido> list = null;
		if (criterioBusquedaCliente.compareToIgnoreCase("") == 0
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
				list = pedidoDao
						.obtenerPorHql(
								"select distinct e from Pedido e "
										+ "inner join fetch e.detallePedido dp "
										+ "inner join e.cliente c inner join c.persona p "
										+ "where (p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "order by e.fecha",
								new Object[] { "%" + criterioBusquedaCliente
										+ "%" });
			} else if (criterioBusquedaCodigo.compareTo("") != 0) {
				list = pedidoDao
						.obtenerPorHql(
								"select distinct e from Pedido e "
										+ "inner join fetch e.detallePedido dp "
										+ "where e.pedidoid=?1 or e.secuencia=?1 order by e.fecha",
								new Object[] { Integer
										.parseInt(criterioBusquedaCodigo) });
			} else if (criterioBusquedaDetalle.compareTo("") != 0) {
				list = new ArrayList<Pedido>();
				List<Pedido> list1 = pedidoDao
						.obtenerPorHql(
								"select distinct e from Pedido e "
										+ "inner join fetch e.detallePedido dp "
										+ "inner join fetch dp.producto p "
										+ "where (p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1) "
										+ "order by e.fecha",
								new Object[] { "%" + criterioBusquedaDetalle
										+ "%" });
				for (Pedido pedido : list1) {
					pedidoDao.evict(pedido);
					list.add(obtenerPorPedidoId(pedido.getId()));
				}
			} else if (criterioBusquedaFechaDocumento != null)
				list = pedidoDao.obtenerPorHql(
						"select distinct e from Pedido e "
								+ "inner join fetch e.detallePedido dp "
								+ "inner join fetch dp.producto p "
								+ "where e.fecha>=?1 and e.fecha<=?2 "
								+ "order by e.fecha", new Object[] {
								criterioBusquedaFechaDocumento,
								dateCompleto(criterioBusquedaFechaDocumento) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public List<Pedido> obtenerDetallePedidoPorEstado(Date fechaInicio,
			Date fechaFin, int estado) {
		List<Pedido> list = new ArrayList<Pedido>();
		if (fechaInicio == null && fechaFin == null && estado == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			list = pedidoDao.obtenerPorHql("select distinct e from Pedido e "
					+ "inner join e.tipodocumento td "
					+ "inner join fetch e.detallepedidos de "
					+ "inner join fetch de.estadoProductoPedido epe "
					+ "where (td.tipodocumentoid=1 or td.tipodocumentoid=8) "
					+ "and e.fechainicio>=?1 and e.fechainicio<=?2 "
					+ "and epe.estadoproductopedidoid=?3 "
					+ "order by e.fechainicio asc", new Object[] { fechaInicio,
					dateCompleto(fechaFin), (short) estado });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public List<Pedido> obtenerPedidosPorCliente(
			String criterioBusquedaCliente, String criterioBusquedaCodigo,
			Integer tipoDocumentoId) {
		List<Pedido> list = null;
		if (tipoDocumentoId == 0 && criterioBusquedaCodigo.compareTo("") == 0)
			list = pedidoDao.obtenerPorHql("select distinct e from Pedido e "
					+ "inner join e.tipodocumento td "
					+ "inner join fetch e.detallepedidos "
					+ "inner join e.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 and (td.tipodocumentoid=1 "
					+ "or td.tipodocumentoid=2 or td.tipodocumentoid=7 "
					+ "or td.tipodocumentoid=8) order by e.fechainicio asc",
					new Object[] { criterioBusquedaCliente });
		else if (tipoDocumentoId != 0
				&& criterioBusquedaCodigo.compareTo("") != 0)
			list = pedidoDao.obtenerPorHql("select distinct e from Pedido e "
					+ "inner join e.tipodocumento td "
					+ "inner join fetch e.detallepedidos "
					+ "inner join e.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 and td.tipodocumentoid=?2 "
					+ "and e.codigodocumento like ?3 "
					+ "order by e.fechainicio asc", new Object[] {
					criterioBusquedaCliente, tipoDocumentoId,
					"%" + criterioBusquedaCodigo + "%" });
		else if (tipoDocumentoId != 0)
			list = pedidoDao.obtenerPorHql("select distinct e from Pedido e "
					+ "inner join e.tipodocumento td "
					+ "inner join fetch e.detallepedidos "
					+ "inner join e.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 and td.tipodocumentoid=?2 "
					+ "order by e.fechainicio asc", new Object[] {
					criterioBusquedaCliente, tipoDocumentoId });
		else if (criterioBusquedaCodigo.compareTo("") != 0)
			list = pedidoDao.obtenerPorHql("select distinct e from Pedido e "
					+ "inner join e.tipodocumento td "
					+ "inner join fetch e.detallepedidos "
					+ "inner join e.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 and e.codigodocumento like ?2 "
					+ "order by e.fechainicio asc",
					new Object[] { criterioBusquedaCliente,
							"%" + criterioBusquedaCodigo + "%" });

		return list;
	}

	public List<Pedido> obtenerPorEstado(String criterioBusquedaCliente,
			String criterioBusquedaDocumento, Date criterioBusquedafechaInicio,
			Date criterioBusquedafechaFin) {
		List<Pedido> list = new ArrayList<Pedido>();
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
				list = pedidoDao
						.obtenerPorHql(
								"select distinct e from Pedido e "
										+ "inner join e.tipodocumento td "
										+ "inner join e.cliente c inner join c.persona p "
										+ "where p.cedula=?1 and "
										+ "(td.tipodocumentoid=1 or td.tipodocumentoid=8)"
										+ " and e.pagado=false and e.activo=true order by e.fechainicio asc",
								new Object[] { criterioBusquedaCliente });
			else if (criterioBusquedaDocumento != null
					&& criterioBusquedaDocumento.compareTo("") != 0)
				list = pedidoDao
						.obtenerPorHql(
								"select distinct e from Pedido e "
										+ "inner join e.tipodocumento td "
										+ "inner join e.cliente c inner join c.persona p "
										+ "where e.codigodocumento like ?1 and "
										+ "(td.tipodocumentoid=1 or td.tipodocumentoid=8)"
										+ " and e.pagado=false and e.activo=true order by e.fechainicio asc",
								new Object[] { "%" + criterioBusquedaDocumento
										+ "%" });
			else if (criterioBusquedafechaInicio != null
					&& criterioBusquedafechaFin != null
					&& !criterioBusquedafechaInicio
							.after(criterioBusquedafechaFin))
				list = pedidoDao
						.obtenerPorHql(
								"select distinct e from Pedido e "
										+ "inner join e.tipodocumento td "
										+ "inner join e.cliente c inner join c.persona p "
										+ "where e.fechainicio>=?1 and e.fechainicio<=?2 and "
										+ "(td.tipodocumentoid=1 or td.tipodocumentoid=8)"
										+ " and e.pagado=false and e.activo=true "
										+ "order by e.fechainicio asc",
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

	public Pedido obtenerPorPedidoId(Integer pedidoId) {
		Pedido pedido = (Pedido) pedidoDao.obtenerPorHql(
				"select distinct p from Pedido p "
						+ "left join fetch p.detallePedido "
						+ "where p.pedidoid=?1", new Object[] { pedidoId })
				.get(0);
		return pedido;
	}

	public int posicion(FacturaReporte facturaReporte,
			List<DetallePedido> detallePedidos) {
		for (DetallePedido detallePedido : detallePedidos)
			if ((facturaReporte.getProducto().getId().compareTo(detallePedido
					.getProducto().getId())) == 0
					&& (facturaReporte.getCantidad().compareTo(detallePedido
							.getCantidad())) == 0
					&& (facturaReporte.getPrecioUnitVenta().compareTo(
							redondear(detallePedido.getPrecioVenta())) == 0))
				return detallePedidos.indexOf(detallePedido);

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