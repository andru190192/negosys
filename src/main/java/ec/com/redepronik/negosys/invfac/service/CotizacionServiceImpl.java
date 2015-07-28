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

import ec.com.redepronik.negosys.invfac.dao.CotizacionDao;
import ec.com.redepronik.negosys.invfac.entity.Cotizacion;
import ec.com.redepronik.negosys.invfac.entity.DetalleCotizacion;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.CotizacionImprimir;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;
import ec.com.redepronik.negosys.utils.configuraciones.NumerosPorLetras;

@Service
public class CotizacionServiceImpl implements CotizacionService {

	@Autowired
	private CotizacionDao cotizacionDao;

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

	public void actualizar(Cotizacion cotizacion) {
		cotizacionDao.actualizar(cotizacion);
	}

	private boolean añadirFRLista(FacturaReporte fr, List<FacturaReporte> list,
			int localId, int fila) {
		Producto p = productoService.obtenerPorEan(fr.getCodigo());
		if (p != null) {
			int precioId = productoService.obtenerPvpPorProducto(p
					.getTipoPrecioProductos());
			Kardex k = kardexService.obtenerSaldoActual(p.getEan(), localId);

			fr.setProducto(p);
			fr.setPrecioId(precioId);
			fr.setPrecioCosto(k.getPrecio());
			fr.setCantidadKardex(k.getCantidad());
			fr.setCodigo(p.getEan());
			fr.setDescripcion(p.getNombre() + "      ");
			fr.setImpuesto(productoService.impuestoProducto(p
					.getProductosTarifas()));
			fr.setPrecioUnitVenta(productoService.calcularPrecio(p.getEan(),
					p.getTipoPrecioProductos(), k, localId, fr.getPrecioId()));

			list.set(fila, fr);
			return true;
		}
		return false;
	}

	public FacturaReporte asignar(DetalleCotizacion de) {
		Producto producto = productoService.obtenerPorProductoId(de
				.getProducto().getId());

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre());
		fr.setCantidad(de.getCantidad());
		fr.setPrecioUnitVenta(de.getPrecioVenta());
		fr.setDescuentoDolares(de.getDescuento());
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

		fr.setDescuentoPorcentaje(multiplicarDivide(de.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public FacturaReporte asignar(DetalleCotizacion de, int localId) {
		Producto p = productoService.obtenerPorProductoId(de.getProducto()
				.getId());
		Kardex k = kardexService.obtenerSaldoActual(p.getEan(), localId);

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(p);
		fr.setCodigo(p.getEan());
		fr.setDescripcion(p.getNombre());
		fr.setCantidad(de.getCantidad());
		fr.setCantidadKardex(k.getCantidad());
		fr.setPrecioUnitVenta(de.getPrecioVenta());
		fr.setDescuentoDolares(de.getDescuento());
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

		fr.setDescuentoPorcentaje(multiplicarDivide(de.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public FacturaReporte asignar(FacturaReporte facturaReporte) {
		Producto p = productoService.obtenerPorProductoId(facturaReporte
				.getProducto().getId());
		FacturaReporte fr = new FacturaReporte();
		if (p != null) {
			fr.setProducto(p);
			fr.setCodigo(p.getEan());
			fr.setDescripcion(p.getNombre() + "      ");
			fr.setPrecioId(facturaReporte.getPrecioId());
			fr.setCantidad(facturaReporte.getCantidad());
			fr.setCantidadCuadre(facturaReporte.getCantidadCuadre());
			fr.setCantidadKardex(facturaReporte.getCantidadKardex());
			fr.setPrecioUnitVenta(facturaReporte.getPrecioUnitVenta());
			fr.setPrecioUnitVentaCuadre(facturaReporte
					.getPrecioUnitVentaCuadre());
			fr.setPrecioCosto(facturaReporte.getPrecioCosto());
			fr.setDescuentoDolares(facturaReporte.getDescuentoDolares());
			fr.setDescuentoDolaresCuadre(facturaReporte
					.getDescuentoDolaresCuadre());
			fr.setDescuentoPorcentaje(facturaReporte.getDescuentoPorcentaje());
			fr.setEstado(facturaReporte.getEstado());
			fr.setImpuesto(productoService.impuestoProducto(p
					.getProductosTarifas()));

			if (facturaReporte.getCantidad().compareTo(0) > 0)
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

			calcularImporte(fr);
			fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
			fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
			fr.setImporte(redondear(fr.getImporte()));
		}
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
				fr.setEstado(EstadoProductoVenta.NR);
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
			List<FacturaReporte> list, int localId) {
		int fila = list.indexOf(facturaReporte);
		facturaReporte.setCodigo(facturaReporte.getCodigo().split(" - ")[0]);
		if (fila == 0)
			return añadirFRLista(facturaReporte, list, localId, fila);
		else if (list.get(fila - 1).getCantidad() != null)
			return añadirFRLista(facturaReporte, list, localId, fila);
		return false;
	}

	public boolean convertirListaFacturaReporteListaCotizacionDetalle(
			List<FacturaReporte> list, Cotizacion cotizacion) {
		boolean bn = true;
		cotizacion.setDetalleCotizacion(new ArrayList<DetalleCotizacion>());
		for (FacturaReporte f : quitarProductosCantidadCero(list)) {
			bn = false;
			Producto producto = productoService.obtenerPorProductoId(f
					.getProducto().getId());
			DetalleCotizacion detalleCotizacion = new DetalleCotizacion();
			detalleCotizacion.setProducto(producto);
			detalleCotizacion.setCantidad(f.getCantidad());

			detalleCotizacion.setPrecioVenta(f.getPrecioUnitVenta());
			detalleCotizacion.setDescuento(f.getDescuentoDolares());

			detalleCotizacion.setEstadoProductoVenta(f.getEstado());
			cotizacion.addDetalleCotizacion(detalleCotizacion);
		}
		if (bn)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY CANTIDADES A FACTURAR");

		return bn;
	}

	public List<DetalleCotizacion> duplicarDetalleCotizacion(
			List<DetalleCotizacion> detalleCotizacions) {
		List<DetalleCotizacion> list = new ArrayList<DetalleCotizacion>();
		if (detalleCotizacions != null)
			for (DetalleCotizacion de : detalleCotizacions) {
				if (de != null) {
					DetalleCotizacion dCotizacion = new DetalleCotizacion();
					dCotizacion.setOrden(de.getOrden());
					dCotizacion.setEstadoProductoVenta(de
							.getEstadoProductoVenta());
					dCotizacion.setProducto(de.getProducto());
					dCotizacion.setCantidad(de.getCantidad());
					dCotizacion.setDescuento(de.getDescuento());

					list.add(dCotizacion);
				}
			}

		return list;
	}

	public void eliminar(Cotizacion cotizacion, String login, String pass) {
		if (empleadoService.autorizacion(login, pass)) {
			cotizacion.setActivo(false);
			cotizacionDao.actualizar(cotizacion);
		}
	}

	public CantidadFactura eliminarDetalle(FacturaReporte fr,
			List<FacturaReporte> list) {
		presentaMensaje(FacesMessage.SEVERITY_INFO, "ELIMINÓ EL PRODUCTO: "
				+ fr.getDescripcion());
		list.remove(fr);
		return sumarCantidadFinal(list);
	}

	public boolean imprimirCotizacion(int cotizacionId,
			List<CotizacionImprimir> list) {
		if (cotizacionId == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY DATOS PARA IMPRIMIR");
		else {
			Cotizacion cotizacion = obtenerPorCotizacionId(cotizacionId);
			if (cotizacion == null) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE ENCONTRO LA FACTURA");
			} else if (cotizacion.getId() == null || cotizacion.getId() == 0)
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE ENCONTRO LA FACTURA");
			else {
				Persona cliente = clienteService.obtenerPorClienteId(cotizacion
						.getCliente().getId());

				CantidadFactura cantidadFactura = new CantidadFactura();
				cantidadFactura.settDescuentoEgreso(newBigDecimal());
				List<FacturaReporte> listFacturaReporte = new ArrayList<FacturaReporte>();
				for (DetalleCotizacion dc : cotizacion.getDetalleCotizacion()) {
					FacturaReporte fr = asignar(dc);
					cantidadFactura = calcularCantidadFactura(cantidadFactura,
							fr);
					listFacturaReporte.add(fr);
				}

				String telefono = "";
				if (cliente.getTelefono() != null)
					telefono = cliente.getTelefono();

				list.add(new CotizacionImprimir(cotizacion, cliente, telefono,
						NumerosPorLetras.convertNumberToLetter(cantidadFactura
								.getValorTotal()),
						redondearCantidadFactura(cantidadFactura),
						listFacturaReporte));
				return true;
			}
		}
		return false;
	}

	public Cotizacion insertar(Cotizacion cotizacion) {
		cotizacion.setCajero(empleadoService
				.obtenerEmpleadoCargoPorCedulaAndCargo(SecurityContextHolder
						.getContext().getAuthentication().getName(), 4));
		cotizacion.setFecha(timestamp());
		cotizacion.setActivo(true);
		cotizacion.setPuntoEmision("0");
		cotizacion.setSecuencia("0");

		int c = 1;
		for (DetalleCotizacion de : cotizacion.getDetalleCotizacion())
			de.setOrden(c++);

		cotizacionDao.insertar(cotizacion);

		presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTÓ COTIZACION #"
				+ cotizacion.getId());
		return cotizacion;
	}

	public List<Cotizacion> obtener(String criterioBusquedaCliente,
			String criterioBusquedaCodigo, String criterioBusquedaDetalle,
			Date criterioBusquedaFechaDocumento) {
		List<Cotizacion> list = null;
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
				list = cotizacionDao
						.obtenerPorHql(
								"select distinct e from Cotizacion e "
										+ "inner join fetch e.detalleCotizacion "
										+ "inner join e.cliente c inner join c.persona p "
										+ "where p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1 "
										+ "order by e.fecha",
								new Object[] { "%" + criterioBusquedaCliente
										+ "%" });
			} else if (criterioBusquedaCodigo.compareTo("") != 0) {
				list = cotizacionDao
						.obtenerPorHql("select distinct e from Cotizacion e "
								+ "inner join fetch e.detalleCotizacion "
								+ "where e.cotizacionid=?1 or e.secuencia=?1 "
								+ "order by e.fecha", new Object[] { Integer
								.parseInt(criterioBusquedaCodigo) });
			} else if (criterioBusquedaDetalle.compareTo("") != 0) {
				list = new ArrayList<Cotizacion>();
				List<Cotizacion> list1 = cotizacionDao
						.obtenerPorHql(
								"select distinct c from Cotizacion c "
										+ "inner join fetch c.detalleCotizacion dc "
										+ "inner join fetch dc.producto p "
										+ "where p.nombre like ?1 or p.ean like ?1 or p.codigo1 like ?1 "
										+ "order by c.fecha",
								new Object[] { criterioBusquedaDetalle });
				for (Cotizacion cotizacion : list1) {
					cotizacionDao.evict(cotizacion);
					list.add(obtenerPorCotizacionId(cotizacion.getId()));
				}
			} else if (criterioBusquedaFechaDocumento != null)
				list = cotizacionDao.obtenerPorHql(
						"select distinct e from Cotizacion e "
								+ "inner join fetch e.detalleCotizacion de "
								+ "inner join fetch de.producto p "
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

	public Cotizacion obtenerPorCotizacionId(Integer cotizacionId) {
		Cotizacion cotizacion = (Cotizacion) cotizacionDao.obtenerPorHql(
				"select distinct c from Cotizacion c "
						+ "left join fetch c.detalleCotizacion "
						+ "where c.cotizacionid=?1",
				new Object[] { cotizacionId }).get(0);
		return cotizacion;
	}

	public int posicion(FacturaReporte facturaReporte,
			List<DetalleCotizacion> detalleCotizacions) {
		for (DetalleCotizacion detalleCotizacion : detalleCotizacions)
			if ((facturaReporte.getProducto().getId()
					.compareTo(detalleCotizacion.getProducto().getId())) == 0
					&& (facturaReporte.getCantidad()
							.compareTo(detalleCotizacion.getCantidad())) == 0
					&& (facturaReporte.getPrecioUnitVenta().compareTo(
							redondear(detalleCotizacion.getPrecioVenta())) == 0))
				return detalleCotizacions.indexOf(detalleCotizacion);

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