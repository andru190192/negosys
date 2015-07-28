package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.Utils.invertirPalabra;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.divide;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.moraTotal;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.UtilsMath.valorSinIva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.CantidadDao;
import ec.com.redepronik.negosys.invfac.dao.FacturaDao;
import ec.com.redepronik.negosys.invfac.dao.GananciaFacturaReporteDao;
import ec.com.redepronik.negosys.invfac.dao.IngresoCajaDao;
import ec.com.redepronik.negosys.invfac.dao.VolumenVentaProductoDao;
import ec.com.redepronik.negosys.invfac.entity.DetalleCredito;
import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.Entrada;
import ec.com.redepronik.negosys.invfac.entity.EstadoKardex;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entity.PagoCredito;
import ec.com.redepronik.negosys.invfac.entity.PagoEntrada;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entity.TipoPrecioProducto;
import ec.com.redepronik.negosys.invfac.entity.TipoProducto;
import ec.com.redepronik.negosys.invfac.entityAux.Cantidad;
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
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;
import ec.com.redepronik.negosys.utils.configuraciones.NumerosPorLetras;

@Service
public class FacturaServiceImpl implements FacturaService {

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private GananciaFacturaReporteDao gananciaFacturaReporteDao;

	@Autowired
	private VolumenVentaProductoDao volumenVentaProductoDao;

	@Autowired
	private CantidadDao cantidadDao;

	@Autowired
	private IngresoCajaDao ingresoCajaDao;

	@Autowired
	private FacturaInternaService facturaInternaService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private NotaCreditoService notaCreditoService;

	@Autowired
	private MoraService moraService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private LocalService localService;

	@Autowired
	private LocalEmpleadoCargoService localEmpleadoCargoService;

	@Autowired
	private TarifaService tarifaService;

	public void actualizar(Factura egreso) {
		facturaDao.actualizar(egreso);
	}

	public FacturaReporte asignar(DetalleFactura de) {
		Producto producto = productoService.obtenerPorProductoId(de
				.getProducto().getId());

		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getId().toString());
		fr.setDescripcion(producto.getNombre());
		fr.setCantidad(de.getCantidad());
		fr.setCantidadKardex(de.getCantidad());
		fr.setCantidadCuadre(de.getCantidadCuadre());
		fr.setPrecioCosto(de.getPrecioCosto());
		fr.setPrecioUnitVenta(de.getPrecioVenta());
		fr.setPrecioUnitVentaCuadre(de.getPrecioVentaCuadre());
		fr.setDescuentoDolares(de.getDescuento());
		fr.setDescuentoDolaresCuadre(de.getDescuentoCuadre());
		fr.setEstado(de.getEstadoProductoVenta());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), producto.getProductoUnidads()));

		if (fr.getEstado() == EstadoProductoVenta.PR) {
			fr.setImpuesto("P");
			// fr.setDescripcion(fr.getDescripcion() + " PROMO");
		} else if (fr.getEstado() == EstadoProductoVenta.CB) {
			fr.setImpuesto("B");
			// fr.setDescripcion(fr.getDescripcion() + " CAMBI");
		} else if (fr.getEstado() == EstadoProductoVenta.PI) {
			fr.setImpuesto("PI");
			// fr.setDescripcion(fr.getDescripcion() + " PRO I");
		} else
			fr.setImpuesto(productoService.impuestoProducto(producto
					.getProductosTarifas()));

		if (fr.getEstado() != EstadoProductoVenta.NR) {
			fr.setDescuentoDolares(redondearTotales(multiplicar(
					fr.getPrecioUnitVenta(), fr.getCantidad())));
			if (fr.getPrecioUnitVentaCuadre() != null
					&& fr.getCantidadCuadre() != null) {
				fr.setDescuentoDolaresCuadre(redondearTotales(multiplicar(
						fr.getPrecioUnitVentaCuadre(), fr.getCantidadCuadre())));
				fr.setPrecioUnitVentaCuadre(redondear(fr
						.getPrecioUnitVentaCuadre()));
			}
		}

		fr.setDescuentoPorcentaje(multiplicarDivide(de.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		fr.setImporte(redondearTotales(multiplicar(fr.getPrecioUnitVenta(),
				fr.getCantidad()).subtract(fr.getDescuentoDolares())));
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		return fr;
	}

	public FacturaReporte asignar(FacturaReporte fr) {
		Producto producto = productoService.obtenerPorProductoId(fr
				.getProducto().getId());
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre() + "      ");
		fr.setPrecioUnitVentaImpuesto(redondearTotales(productoService
				.precioConImpuestos(producto, fr.getPrecioUnitVenta())));

		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (fr.getCantidad().compareTo(0) > 0)
			fr.setNombreCantidad(productoService.convertirUnidadString(
					fr.getCantidad(), producto.getProductoUnidads()));

		if (fr.getEstado() == EstadoProductoVenta.PR)
			fr.setImpuesto("P");
		else if (fr.getEstado() == EstadoProductoVenta.CB)
			fr.setImpuesto("C");
		else if (fr.getEstado() == EstadoProductoVenta.PI)
			fr.setImpuesto("PI");

		if (fr.getEstado() != EstadoProductoVenta.NR)
			fr.setDescuentoDolares(redondear(multiplicar(
					fr.getPrecioUnitVenta(), fr.getCantidad())));

		calcularImporte(fr);
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setImporteImpuesto(redondearTotales(productoService
				.precioConImpuestos(producto, fr.getImporte())));
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
		cf.settDescuentoEgreso(cf.gettDescuentoProducto().add(
				cf.gettDescuentoEgreso()));

		BigDecimal iva = tarifaService.obtenerPorTarifaId((short) 2)
				.getPorcentaje();
		cf.setIva12(iva(cf.getSt12().add(cf.getValorIce()), iva));

		cf.setValorTotal(cf.getStSinImpuesto().add(cf.getValorIce())
				.add(cf.getValorIRBPNR()).add(cf.getIva12())
				.add(cf.getPropina()));

		cf.setValorTotal(cf.getValorTotal());

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

	public CantidadesEntradaReporte calcularEntrada(Entrada entrada,
			CantidadesEntradaReporte cantidadesEntradaReporte,
			Date fechaFactura, Date fecha) {
		cantidadesEntradaReporte.setSaldoEntrada(redondearTotales(entrada
				.getSaldo()));
		cantidadesEntradaReporte.setMoraEntrada(newBigDecimal());
		cantidadesEntradaReporte.setTotalEntrada(redondearTotales(entrada
				.getSaldo()));
		if (compareTo(fecha, entrada.getFechaMora()) >= 0) {
			BigDecimal mora = moraService.obtenerPorFecha(fechaFactura)
					.getPorcentaje();
			BigDecimal moraTotal = moraTotal(fecha, entrada.getFechaMora(),
					mora);
			cantidadesEntradaReporte.setMoraEntrada(multiplicarDivide(
					moraTotal, entrada.getSaldo()));
			cantidadesEntradaReporte
					.setTotalEntrada(redondearTotales(entrada.getSaldo().add(
							cantidadesEntradaReporte.getMoraEntrada())));
		}
		return cantidadesEntradaReporte;
	}

	public BigDecimal calcularEntradas(Factura egreso) {
		BigDecimal total = newBigDecimal();
		if (egreso.getEntradas() != null && !egreso.getEntradas().isEmpty())
			for (Entrada entrada : egreso.getEntradas())
				total = total.add(entrada.getCuota());

		return total;
	}

	public void calcularImporte(FacturaReporte fr) {
		if (fr.getCantidad() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA CANTIDAD");
		if (compareTo(fr.getPrecioUnitVenta(), "0") <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN PRECIO");
		else
			fr.setImporte(redondear(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad())));
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

	public NumeroCuotasReporte calcularNumeroCuotasEntradasCreditos(
			Factura factura, NumeroCuotasReporte numeroCuotas, Date fecha) {

		if (factura.getEntradas() != null)
			for (Entrada f : factura.getEntradas()) {
				if (f.getPagado())
					numeroCuotas.setCuotasPagadasEntrada(numeroCuotas
							.getCuotasPagadasEntrada() + 1);
				else {
					if (compareTo(f.getFechaLimite(), fecha) < 0)
						numeroCuotas.setCuotasVencidasEntrada(numeroCuotas
								.getCuotasVencidasEntrada() + 1);
					else
						numeroCuotas.setCuotasNormalesEntrada(numeroCuotas
								.getCuotasNormalesEntrada() + 1);
				}
			}
		numeroCuotas.setCuotasPorPagarEntrada(numeroCuotas
				.getCuotasVencidasEntrada()
				+ numeroCuotas.getCuotasNormalesEntrada());
		numeroCuotas.setCuotasTotalEntrada(numeroCuotas
				.getCuotasPagadasEntrada()
				+ numeroCuotas.getCuotasPorPagarEntrada());

		if (factura.getCredito() != null
				&& factura.getCredito().getDetalleCreditos() != null)
			for (DetalleCredito dc : factura.getCredito().getDetalleCreditos()) {
				if (dc.getPagado())
					numeroCuotas.setCuotasPagadasCredito(numeroCuotas
							.getCuotasPagadasCredito() + 1);
				else {
					if (compareTo(dc.getFechaLimite(), fecha) < 0)
						numeroCuotas.setCuotasVencidasCredito(numeroCuotas
								.getCuotasVencidasCredito() + 1);
					else
						numeroCuotas.setCuotasNormalesCredito(numeroCuotas
								.getCuotasNormalesCredito() + 1);
				}
			}
		numeroCuotas.setCuotasPorPagarCredito(numeroCuotas
				.getCuotasVencidasCredito()
				+ numeroCuotas.getCuotasNormalesCredito());
		numeroCuotas.setCuotasTotalCredito(numeroCuotas
				.getCuotasPagadasCredito()
				+ numeroCuotas.getCuotasPorPagarCredito());

		return numeroCuotas;
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
		// fr.setIva(fr.getEstado() == 2 ? "P" : producto.getIva() ? "I" : "");
		if (fr.getCantidad() != null) {
			calcularImporte(fr);
			cambiarFRenLista(fr, list);
		}
		return sumarCantidadFinal(list);
	}

	public BigDecimal calcularTotal(Factura factura) {
		factura = obtenerPorFacturaId(factura.getId());
		if (factura != null) {
			CantidadFactura cantidadFactura = new CantidadFactura();
			cantidadFactura.settDescuentoEgreso(factura.getDescuentoCuadre());
			for (DetalleFactura detalleFactura : factura.getDetalleFactura())
				cantidadFactura = facturaInternaService
						.calcularCantidadFactura(cantidadFactura,
								asignar(detalleFactura));
			return redondearTotales(cantidadFactura.getValorTotal());
		}
		return newBigDecimal("0.00");
	}

	public BigDecimal calcularTotal(String codigoDocumento) {
		Factura egreso = obtenerPorCodigo(codigoDocumento);
		if (egreso == null)
			return newBigDecimal();
		else {
			CantidadFactura cantidadFactura = new CantidadFactura();
			cantidadFactura.settDescuentoEgreso(egreso.getDescuentoCuadre());
			for (DetalleFactura detalleFactura : egreso.getDetalleFactura()) {
				cantidadFactura = facturaInternaService
						.calcularCantidadFactura(cantidadFactura,
								asignar(detalleFactura));
			}
			return redondearTotales(cantidadFactura.getValorTotal());
		}
	}

	public void cambiarDescuentoFactura(CantidadFactura cf) {
		if (cf.gettDescuentoEgreso().compareTo(newBigDecimal()) < 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN DESCUENTO MAYOR O IGUAL A CERO");
			cf.settDescuentoEgreso(newBigDecimal());
		} else if (cf.gettDescuentoEgreso().compareTo(cf.getValorTotal()) > 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN DESCUENTO MENOR O IGUAL AL TOTAL");
			cf.settDescuentoEgreso(cf.getValorTotal());
		}
		// cf.setTotal(redondear(cf.getSubTotal().add(cf.getIva())));
	}

	public CantidadFactura cambiarEstado(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidad() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA CAMBIAR EL ESTADO");
			fr.setEstado(EstadoProductoVenta.NR);
		} else {
			if (fr.getEstado() == EstadoProductoVenta.NR) {
				fr.setEstado(EstadoProductoVenta.NR);
				fr.setImpuesto(productoService.impuestoProducto(fr
						.getProducto().getProductosTarifas()));
				if (compareTo(fr.getDescuentoDolares(),
						multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())) == 0)
					fr.setDescuentoDolares(newBigDecimal());

				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ "      ");
			} else if (fr.getEstado() == EstadoProductoVenta.PR) {
				fr.setEstado(EstadoProductoVenta.PR);
				fr.setImpuesto("P");
				fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
						fr.getCantidad()));
				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ " PROMO");
			} else if (fr.getEstado() == EstadoProductoVenta.CB) {
				fr.setEstado(EstadoProductoVenta.CB);
				fr.setImpuesto("C");
				fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
						fr.getCantidad()));
				String aux = invertirPalabra(fr.getDescripcion());
				fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
						+ " CAMBI");
			} else if (fr.getEstado() == EstadoProductoVenta.PI) {
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
			fr.setImporteImpuesto(redondearTotales(productoService
					.precioConImpuestos(fr.getProducto(), fr.getImporte())));
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

	public List<FacturaReporte> cargarListaFacturaReporteFab() {
		List<FacturaReporte> listaFacturaReporte = new ArrayList<FacturaReporte>();
		for (Producto p : productoService.obtener()) {
			listaFacturaReporte.add(convertirProductoFR(p));
			if (p.getEan().compareToIgnoreCase("0001") == 0) {
				FacturaReporte facturaReporte = convertirProductoFR(p);
				facturaReporte.setPrecioUnitVenta(valorSinIva(facturaReporte
						.getPrecioUnitVenta()));
				facturaReporte.setImpuesto("P");
				facturaReporte.setEstado(EstadoProductoVenta.PR);
				facturaReporte.setDescripcion(p.getNombre() + " PROMO");
				listaFacturaReporte.add(facturaReporte);
			}
		}
		return listaFacturaReporte;
	}

	public CantidadFactura cargarProductoLista(String codigoEspecial,
			Integer cantidad, String ean, List<FacturaReporte> lista,
			Integer bodegaId, Pvp pvp) {
		Producto p = productoService.obtenerPorEan(ean);
		Kardex k = kardexService.obtenerSaldoActual(p.getEan(), bodegaId);
		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(p);
		fr.setCantidad(cantidad);
		fr.setCantidadCuadre(cantidad);

		if (codigoEspecial.toUpperCase().compareTo("N") == 0) {
			fr.setEstado(EstadoProductoVenta.NR);
			fr.setImpuesto(productoService.impuestoProducto(p
					.getProductosTarifas()));
		} else if (codigoEspecial.toUpperCase().compareTo("P") == 0) {
			fr.setEstado(EstadoProductoVenta.PR);
			fr.setImpuesto("P");
		} else if (codigoEspecial.toUpperCase().compareTo("I") == 0) {
			fr.setEstado(EstadoProductoVenta.PI);
			fr.setImpuesto("PI");
		} else if (codigoEspecial.toUpperCase().compareTo("C") == 0) {
			fr.setEstado(EstadoProductoVenta.CB);
			fr.setImpuesto("C");
		}

		fr.setPrecioId(pvp.getTipoPrecioId());
		fr.setPrecioCosto(k.getPrecio());
		fr.setPrecioUnitVenta(pvp.getPrecioSinImpuestos());
		fr.setPrecioUnitVentaImpuesto(pvp.getPrecioConImpuestos());
		fr.setCantidadKardex(k.getCantidad());
		fr.setCodigo(p.getEan());
		fr.setDescripcion(p.getNombre() + "      ");
		lista.add(fr);
		return cambiarEstado(fr, lista);
	}

	public boolean comprobarCantidadFinal(List<DetalleFactura> list,
			String codigoEstablecimiento) {
		boolean bn = true;
		for (DetalleFactura de : sumarCantidades(duplicarDetalleFactura(list))) {
			Integer localId = localService.obtenerPorEstablecimiento(
					codigoEstablecimiento).getId();
			Producto p = productoService.obtenerPorProductoId(de.getProducto()
					.getId());
			if (p.getTipoProducto() == TipoProducto.BN) {
				int cantidad = kardexService.obtenerSaldoActual(p.getEan(),
						localId).getCantidad();
				if (de.getCantidad() > cantidad) {
					bn = false;
					presentaMensaje(
							FacesMessage.SEVERITY_ERROR,
							"LA SUMA DE LAS CANTIDADES DEL PRODUCTO: "
									+ p.getEan() + " ES MAYOR A: " + cantidad);
				}
			}
		}
		return bn;
	}

	public boolean comprobarDetallesFactura(List<FacturaReporte> lista) {
		for (FacturaReporte f : lista)
			if (f.getCantidad() > 0)
				return true;
		return false;
	}

	public int contarRegistrosActivos(List<FacturaReporte> facturaReporte) {
		int registrosActivos = 0;
		for (FacturaReporte fr : facturaReporte)
			if (fr.getCantidad() > 0)
				registrosActivos++;
		return registrosActivos;
	}

	public boolean convertirListaFacturaReporteListaFacturaDetalle(
			List<FacturaReporte> list, Factura egreso) {
		boolean bn = true;
		egreso.setDetalleFactura(new ArrayList<DetalleFactura>());
		for (FacturaReporte f : quitarProductosCantidadCero(list)) {
			bn = false;
			Producto producto = productoService.obtenerPorProductoId(f
					.getProducto().getId());
			DetalleFactura detalleFactura = new DetalleFactura();
			detalleFactura.setProducto(producto);
			detalleFactura.setCantidad(f.getCantidad());
			detalleFactura.setPrecioCosto(producto.getPrecio());
			detalleFactura.setPrecioVenta(f.getPrecioUnitVenta());
			detalleFactura.setDescuento(f.getDescuentoDolares());

			detalleFactura.setEstadoProductoVenta(f.getEstado());
			egreso.addDetalleFactura(detalleFactura);
		}
		if (bn)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY CANTIDADES A FACTURAR");

		return bn;
	}

	public boolean convertirListaFacturaReporteListaFacturaDetalleFacturaCajero(
			List<FacturaReporte> list, Factura egreso) {
		boolean bn = true;
		List<DetalleFactura> list1 = new ArrayList<DetalleFactura>();
		for (FacturaReporte f : quitarProductosCantidadCero(list)) {
			bn = false;
			Producto producto = productoService.obtenerPorProductoId(f
					.getProducto().getId());
			DetalleFactura detalleFactura = new DetalleFactura();
			detalleFactura.setProducto(producto);
			detalleFactura.setCantidad(f.getCantidad());
			detalleFactura.setPrecioCosto(producto.getPrecio());
			detalleFactura.setPrecioVenta(f.getPrecioUnitVenta());
			detalleFactura.setDescuento(f.getDescuentoDolares());

			detalleFactura.setEstadoProductoVenta(f.getEstado());
			list1.add(detalleFactura);

		}
		egreso.setDetalleFactura(new ArrayList<DetalleFactura>());
		for (DetalleFactura detalleFactura : sumarCantidades(list1))
			if (detalleFactura.getCantidad() > 0)
				egreso.addDetalleFactura(detalleFactura);

		if (bn)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY CANTIDADES A FACTURAR");

		return bn;
	}

	private FacturaReporte convertirProductoFR(Producto p) {
		FacturaReporte facturaReporte = new FacturaReporte();
		TipoPrecioProducto tpp = productoService.obtenerValorPvp(p
				.getTipoPrecioProductos());
		facturaReporte.setProducto(p);
		facturaReporte.setCodigo(p.getEan());
		facturaReporte.setDescripcion(p.getNombre());
		facturaReporte.setCantidad(0);
		facturaReporte.setPrecioId(tpp.getId());
		facturaReporte.setPrecioUnitVenta(tpp.getValor());

		facturaReporte.setDescuentoDolares(newBigDecimal());
		facturaReporte.setDescuentoPorcentaje(newBigDecimal());
		return facturaReporte;
	}

	public List<DetalleFactura> duplicarDetalleFactura(
			List<DetalleFactura> detalleFacturas) {
		List<DetalleFactura> list = new ArrayList<DetalleFactura>();
		if (detalleFacturas != null)
			for (DetalleFactura de : detalleFacturas) {
				if (de != null) {
					DetalleFactura dFactura = new DetalleFactura();
					dFactura.setOrden(de.getOrden());
					dFactura.setCantidad(de.getCantidad());
					dFactura.setPrecioCosto(de.getPrecioCosto());
					dFactura.setPrecioVenta(de.getPrecioVenta());
					dFactura.setDescuento(de.getDescuento());
					dFactura.setEstadoProductoVenta(de.getEstadoProductoVenta());
					dFactura.setProducto(de.getProducto());
					list.add(dFactura);
				}
			}

		return list;
	}

	public Factura duplicarFactura(Factura egreso, int tipoDocumentoId) {
		Factura f = new Factura();
		f.setActivo(egreso.getActivo());
		f.setAutorizacionAnulacion(egreso.getAutorizacionAnulacion());
		f.setCajero(egreso.getCajero());
		f.setClienteFactura(egreso.getCliente());
		f.setCliente(egreso.getCliente());
		f.setDescuento(egreso.getDescuento());
		// f.setDevolucion(egreso.getDevolucion());
		f.setVendedor(egreso.getVendedor());
		f.setFechaCierre(egreso.getFechaCierre());
		f.setFechaInicio(egreso.getFechaInicio());

		// if (egreso.getCredito() != null) {
		// Credito credito = new Credito();
		// credito.setInteres(egreso.getCredito().getInteres());
		// credito.setMeses(egreso.getCredito().getMeses());
		// credito.setMonto(egreso.getCredito().getMonto());
		// credito.setPagado(egreso.getCredito().getPagado());
		//
		// if (egreso.getCredito().getDetallecreditos() != null) {
		// credito.setDetallecreditos(new ArrayList<Detallecredito>());
		// for (Detallecredito detalleCredito : egreso.getCredito()
		// .getDetallecreditos()) {
		// Detallecredito dCredito = new Detallecredito();
		// dCredito.setCuota(detalleCredito.getCuota());
		// dCredito.setFechalimite(detalleCredito.getFechalimite());
		// dCredito.setFechamora(detalleCredito.getFechamora());
		// dCredito.setFechapago(detalleCredito.getFechapago());
		// dCredito.setMora(detalleCredito.getMora());
		// dCredito.setOrden(detalleCredito.getOrden());
		// dCredito.setPagado(detalleCredito.getPagado());
		// dCredito.setSaldo(detalleCredito.getSaldo());
		//
		// if (detalleCredito.getPagocreditos() != null) {
		// dCredito.setPagocreditos(new ArrayList<Pagocredito>());
		// for (Pagocredito pagoCredito : detalleCredito
		// .getPagocreditos()) {
		// Pagocredito pCredito = new Pagocredito();
		// pCredito.setBanco(pagoCredito.getBanco());
		// pCredito.setChequevaucher(pagoCredito
		// .getChequevaucher());
		// pCredito.setCuentatarjeta(pagoCredito
		// .getCuentatarjeta());
		// pCredito.setCuota(pagoCredito.getCuota());
		// pCredito.setFechacheque(pagoCredito
		// .getFechacheque());
		// pCredito.setFechapago(pagoCredito.getFechapago());
		// pCredito.setPagado(pagoCredito.getPagado());
		// pCredito.setFechagiro(pagoCredito.getFechagiro());
		// pCredito.setAnulado(pagoCredito.getAnulado());
		// pCredito.setDetalle(pagoCredito.getDetalle());
		// pCredito.setTipopago(pagoCredito.getTipopago());
		// dCredito.addPagocredito(pCredito);
		// }
		// }
		// credito.addDetallecredito(dCredito);
		// }
		// }
		//
		// if (egreso.getCredito().getGarantes() != null) {
		// credito.setGarantes(new ArrayList<Garante>());
		// List<Persona> listaGarantes = clienteService
		// .obtenerGarantesPorCredito(egreso.getCredito()
		// .getCreditoid());
		// for (Persona p : listaGarantes) {
		// Garante g = new Garante();
		// g.setCliente(p.getCliente());
		// credito.addGarante(g);
		// }
		// }
		//
		// credito.setFactura(f);
		// f.setCredito(credito);
		// }
		//
		// f.setDetalleFacturas(new ArrayList<DetalleFactura>());
		// for (DetalleFactura detalleFactura : duplicarDetalleFactura(egreso
		// .getDetalleFacturas()))
		// f.addDetalleFactura(detalleFactura);
		//
		// f.setLocal(egreso.getLocal());
		// f.setTipodocumento(tipoDocumentoService.obtenerPorId(tipoDocumentoId));

		if (egreso.getEntradas() != null) {
			f.setEntradas(new ArrayList<Entrada>());
			for (Entrada entrada : egreso.getEntradas()) {
				Entrada en = new Entrada();
				en.setCuota(entrada.getCuota());
				en.setFechaLimite(entrada.getFechaLimite());
				en.setFechaMora(entrada.getFechaMora());
				en.setFechaPago(entrada.getFechaPago());
				en.setMora(entrada.getMora());
				en.setPagado(entrada.getPagado());
				en.setSaldo(entrada.getSaldo());
				if (entrada.getPagoEntradas() != null) {
					en.setPagoEntradas(new ArrayList<PagoEntrada>());
					for (PagoEntrada pagoEntrada : entrada.getPagoEntradas()) {
						PagoEntrada pEntrada = new PagoEntrada();
						pEntrada.setBanco(pagoEntrada.getBanco());
						pEntrada.setChequeVaucher(pagoEntrada
								.getChequeVaucher());
						pEntrada.setCuentaTarjeta(pagoEntrada
								.getCuentaTarjeta());
						pEntrada.setCuota(pagoEntrada.getCuota());
						pEntrada.setFechaCheque(pagoEntrada.getFechaCheque());
						pEntrada.setFechaPago(pagoEntrada.getFechaPago());
						pEntrada.setPagado(pagoEntrada.getPagado());
						pEntrada.setFechaGiro(pagoEntrada.getFechaGiro());
						pEntrada.setAnulado(pagoEntrada.getAnulado());
						pEntrada.setDetalle(pagoEntrada.getDetalle());
						pEntrada.setTipoPago(pagoEntrada.getTipoPago());
						en.addPagoEntrada(pEntrada);
					}
				}
				f.addEntrada(en);
			}
		}
		// f.setGuiaremision(egreso.getGuiaremision());

		return f;
	}

	public NotaCredito eliminar(Factura factura, String login, String pass) {
		boolean bn = empleadoService.autorizacion(login, pass);

		Local local = localService.obtenerPorEstablecimiento(factura
				.getEstablecimiento());

		if (factura.getEntradas() != null)
			for (Entrada f : factura.getEntradas()) {
				f.setActivo(false);
				for (PagoEntrada pe : f.getPagoEntradas())
					pe.setActivo(false);
			}

		for (DetalleFactura de : factura.getDetalleFactura())
			if (kardexService.obtenerSaldoActual(de.getProducto().getEan(),
					local.getId()) == null) {
				bn = false;
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO HAY COMO ANULAR, EL PRODUCTO NO TIENE KARDEX");
			}

		if (bn) {
			factura.setActivo(false);
			factura.setAutorizacionAnulacion(login);

			NotaCredito notaCredito = new NotaCredito();
			notaCredito.setFactura(factura);
			factura.setNotaCredito(notaCredito);

			List<FacturaReporte> listaFacturaReporte = new ArrayList<FacturaReporte>();
			for (DetalleFactura df : duplicarDetalleFactura(factura
					.getDetalleFactura()))
				listaFacturaReporte.add(notaCreditoService.asignar(df));

			notaCreditoService.convertirListaFacturaReporteListaFacturaDetalle(
					listaFacturaReporte, notaCredito);

			notaCreditoService.insertar(notaCredito, local);

			presentaMensaje(
					FacesMessage.SEVERITY_INFO,
					"ANULO CORRECTAMENTE LA FACTURA "
							+ factura.getEstablecimiento() + "-"
							+ factura.getPuntoEmision() + "-"
							+ factura.getSecuencia());

			return notaCredito;
		}
		return null;
	}

	public CantidadFactura eliminarDetalle(FacturaReporte fr,
			List<FacturaReporte> list) {
		presentaMensaje(FacesMessage.SEVERITY_INFO, "ELIMINÓ EL PRODUCTO: "
				+ fr.getDescripcion());
		list.remove(fr);
		return sumarCantidadFinal(list);
	}

	public void eliminarFab(Factura egreso, String login, String pass) {
		boolean bn = empleadoService.autorizacion(login, pass);
		if (bn) {
			egreso.setActivo(false);
			egreso.setAutorizacionAnulacion(login);
			facturaDao.actualizar(egreso);

			presentaMensaje(FacesMessage.SEVERITY_INFO, "ANULO CORRECTAMENTE");
		}
	}

	private void generarKardexFactura(List<DetalleFactura> detalleFacturas,
			int facturaId, Local local) {
		List<DetalleFactura> detalleFacturasFinal = sumarCantidades(detalleFacturas);

		for (DetalleFactura de : detalleFacturasFinal) {
			if (de.getEstadoProductoVenta().getId() != 3
					&& de.getProducto().getKardex()) {
				Kardex kardex = new Kardex();

				kardex.setEstadoKardex(EstadoKardex.VE);
				kardex.setProducto(de.getProducto());
				kardex.setLocal(local);
				kardex.setNota("FACTURA # " + facturaId);
				kardex.setFecha(timestamp());
				kardex.setCantidad(de.getCantidad());
				kardex.setPrecio(de.getPrecioCosto());
				kardex.setActivo(false);

				kardexService.insertar(kardex);

				Producto producto = productoService.obtenerPorProductoId(de
						.getProducto().getId());
				Kardex kardexSaldo = kardexService.obtenerSaldoActual(
						producto.getEan(), local.getId());
				int cantidad = 0;
				if (kardexSaldo != null) {
					cantidad = kardexSaldo.getCantidad();
					kardexSaldo.setActivo(false);
					kardexService.actualizar(kardexSaldo);
				}
				kardexService.insertar(kardexService.generarKardexSaldo(kardex,
						cantidad, kardex.getPrecio(), false));
			}
		}
	}

	public boolean imprimirFactura(int facturaId, List<FacturaImprimir> list) {
		if (facturaId == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY DATOS PARA IMPRIMIR");
		else {
			Factura factura = obtenerPorFacturaId(facturaId);
			if (factura == null) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE ENCONTRO LA FACTURA");
			} else if (factura.getId() == null || factura.getId() == 0)
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE ENCONTRO LA FACTURA");
			else {
				CantidadFactura cf = new CantidadFactura();
				cf.settDescuentoEgreso(factura.getDescuento());
				List<FacturaReporte> listFacturaReporte = new ArrayList<FacturaReporte>();
				for (DetalleFactura df : factura.getDetalleFactura()) {
					FacturaReporte fr = asignar(df);
					cf = calcularCantidadFactura(cf, fr);
					listFacturaReporte.add(fr);
				}

				Persona cliente = factura.getClienteFactura().getPersona();
				Persona vendedor = factura.getVendedor().getEmpleado()
						.getPersona();

				list.add(new FacturaImprimir(factura, cliente, vendedor,
						NumerosPorLetras.convertNumberToLetter(cf
								.getValorTotal()),
						redondearCantidadFactura(cf), listFacturaReporte));
				return true;
			}
		}
		return false;
	}

	private void inicializar(Factura egreso) {
		if (egreso.getEntradas() != null)
			for (Entrada entrada : egreso.getEntradas()) {
				entrada.setCuota(redondearTotales(entrada.getCuota()));
				for (PagoEntrada pagosentrada : entrada.getPagoEntradas())
					pagosentrada.setCuota(redondearTotales(pagosentrada
							.getCuota()));
			}

		if (egreso.getCredito() != null
				&& egreso.getCredito().getDetalleCreditos() != null) {
			while (egreso.getCredito().getDetalleCreditos().contains(null))
				egreso.getCredito().getDetalleCreditos().remove(null);

			for (DetalleCredito detallescredito : egreso.getCredito()
					.getDetalleCreditos()) {
				detallescredito.setCuota(redondearTotales(detallescredito
						.getCuota()));
				for (PagoCredito pagoscredito : detallescredito
						.getPagoCreditos())
					pagoscredito.setCuota(redondearTotales(pagoscredito
							.getCuota()));
			}
		}
	}

	// public Integer insertar(Factura egreso) {
	// facturaDao.insertar(egreso);
	// return egreso.getId();
	// }

	public Factura insertar(Factura factura,
			List<FacturaReporte> listaFacturaReporte) {
		convertirListaFacturaReporteListaFacturaDetalle(listaFacturaReporte,
				factura);
		if (comprobarCantidadFinal(factura.getDetalleFactura(),
				factura.getEstablecimiento())) {
			Local local = localService.obtenerPorEstablecimiento(factura
					.getEstablecimiento());
			factura.setActivo(true);
			factura.setFechaInicio(timestamp());
			String cedula = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			factura.setCajero(empleadoService
					.obtenerEmpleadoCargoPorCedulaAndCargo(cedula, 4));

			factura.setPuntoEmision("0");
			factura.setSecuencia("0");

			if (factura.getDescuento() == null)
				factura.setDescuento(newBigDecimal());

			int c = 1;
			for (DetalleFactura de : factura.getDetalleFactura()) {
				de.setOrden(c++);
				if (de.getDescuento() == null)
					de.setDescuento(newBigDecimal());
			}

			c = 1;
			List<Entrada> list = factura.getEntradas();
			if (list != null && !list.isEmpty())
				for (Entrada f : list) {
					f.setCajero(factura.getCajero());
					f.setOrden(c++);
				}

			factura.setDescuentoCuadre(factura.getDescuento());
			for (DetalleFactura de : factura.getDetalleFactura()) {
				de.setCantidadCuadre(de.getCantidad());
				de.setPrecioVentaCuadre(de.getPrecioVenta());
				de.setDescuentoCuadre(de.getDescuento());
			}

			facturaDao.insertar(factura);

			List<DetalleFactura> detalleFacturas = duplicarDetalleFactura(factura
					.getDetalleFactura());

			generarKardexFactura(detalleFacturas, factura.getId(), local);

			presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTÓ FACTURA #"
					+ factura.getId());
		}
		return factura;

	}

	public Factura insertarFacturaCajero(Factura factura,
			List<FacturaReporte> listaFacturaReporte) {
		convertirListaFacturaReporteListaFacturaDetalleFacturaCajero(
				listaFacturaReporte, factura);
		if (comprobarCantidadFinal(factura.getDetalleFactura(),
				factura.getEstablecimiento())) {
			Local local = localService.obtenerPorEstablecimiento(factura
					.getEstablecimiento());
			factura.setActivo(true);
			factura.setFechaInicio(timestamp());
			String cedula = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			factura.setCajero(empleadoService
					.obtenerEmpleadoCargoPorCedulaAndCargo(cedula, 4));

			factura.setPuntoEmision("0");
			factura.setSecuencia("0");

			if (factura.getDescuento() == null)
				factura.setDescuento(newBigDecimal());

			int c = 1;
			for (DetalleFactura de : factura.getDetalleFactura()) {
				de.setOrden(c++);
				if (de.getDescuento() == null)
					de.setDescuento(newBigDecimal());
			}

			c = 1;
			List<Entrada> list = factura.getEntradas();
			if (list != null && !list.isEmpty())
				for (Entrada f : list) {
					f.setCajero(factura.getCajero());
					f.setOrden(c++);
				}

			factura.setDescuentoCuadre(factura.getDescuento());
			for (DetalleFactura de : factura.getDetalleFactura()) {
				de.setCantidadCuadre(de.getCantidad());
				de.setPrecioVentaCuadre(de.getPrecioVenta());
				de.setDescuentoCuadre(de.getDescuento());
			}

			facturaDao.insertar(factura);

			List<DetalleFactura> detalleFacturas = duplicarDetalleFactura(factura
					.getDetalleFactura());

			generarKardexFactura(detalleFacturas, factura.getId(), local);

			presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTÓ FACTURA #"
					+ factura.getId());
		}
		return factura;
	}

	public Integer insertarFab(Factura egreso, List<FacturaReporte> list) {
		egreso.setActivo(true);
		// egreso.setDevolucion(false);
		if (egreso.getFechaInicio() == null)
			egreso.setFechaInicio(timestamp());
		String cedula = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		egreso.setCajero(empleadoService.obtenerEmpleadoCargoPorCedulaAndCargo(
				cedula, 4));
		egreso.setVendedor(empleadoService
				.obtenerCualquierEmpleadoCargoPorCedulaAndCargo(egreso
						.getCliente().getPersona().getCedula(), 5));
		egreso.setDescuento(newBigDecimal());

		int c = 1;
		for (DetalleFactura de : egreso.getDetalleFactura()) {
			de.setOrden(c++);
			if (de.getDescuento() == null)
				de.setDescuento(newBigDecimal());
		}

		c = 1;
		List<Entrada> list1 = egreso.getEntradas();
		if (list1 != null && !list1.isEmpty())
			for (Entrada f : list1)
				f.setOrden(c++);

		egreso.setDescuentoCuadre(egreso.getDescuento());
		for (DetalleFactura de : egreso.getDetalleFactura()) {
			de.setCantidadCuadre(de.getCantidad());
			de.setPrecioVentaCuadre(de.getPrecioVenta());
			de.setDescuentoCuadre(de.getDescuento());
		}

		facturaDao.insertar(egreso);
		presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTÓ FACTURA: "
				+ egreso.getId(), "error5", false);
		return egreso.getId();
	}

	public List<Factura> obtener(Integer criterioBusquedaEstado,
			String criterioBusquedaCliente,
			String criterioBusquedaNumeroFactura,
			Date criterioBusquedaFechaDocumento) {
		List<Factura> list = null;
		if (criterioBusquedaEstado == 0
				&& criterioBusquedaCliente.compareToIgnoreCase("") == 0
				&& criterioBusquedaNumeroFactura.compareToIgnoreCase("") == 0
				&& criterioBusquedaFechaDocumento == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else if (criterioBusquedaEstado != 0
				&& criterioBusquedaFechaDocumento == null)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SI ESCOJIÓ UN ESTADO, ESCOJA UN DIA");
		else if (criterioBusquedaCliente.length() >= 1
				&& criterioBusquedaCliente.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR CLIENTES");
		else if (!criterioBusquedaNumeroFactura.matches("[0-9]*"))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR NÚMERO FACTURA");
		else {
			criterioBusquedaCliente = criterioBusquedaCliente.toUpperCase();

			if (criterioBusquedaEstado != 0
					&& criterioBusquedaFechaDocumento != null) {
				String consulta = null;
				if (criterioBusquedaEstado == 1)
					consulta = "f.fechaCierre!=null and f.activo=true";
				else if (criterioBusquedaEstado == 2)
					consulta = "f.fechaCierre=null and f.activo=true";
				else if (criterioBusquedaEstado == 3)
					consulta = "f.activo=false";

				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join fetch f.detalleFactura df "
										+ "inner join f.cliente c inner join c.persona p "
										+ "where f.fechaInicio>=?1 and f.fechaInicio<=?2 and "
										+ consulta + " order by f.fechaInicio",
								new Object[] {
										criterioBusquedaFechaDocumento,
										new Date(criterioBusquedaFechaDocumento
												.getTime() + 86399999) });
			} else if (criterioBusquedaCliente.compareTo("") != 0) {
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join fetch f.detalleFactura df "
										+ "inner join f.cliente c inner join c.persona p "
										+ "where (p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "order by f.fechaInicio",
								new Object[] { "%" + criterioBusquedaCliente
										+ "%" });
			} else if (criterioBusquedaNumeroFactura.compareTo("") != 0) {
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join fetch f.detalleFactura df "
										+ "where f.secuencia like ?1 "
										+ "order by f.secuencia",
								new Object[] { String.format(
										"%09d",
										Integer.parseInt(criterioBusquedaNumeroFactura)) });
			} else if (criterioBusquedaFechaDocumento != null)
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join fetch f.detalleFactura df "
										+ "inner join fetch df.producto p "
										+ "where f.fechaInicio>=?1 and f.fechaInicio<=?2 "
										+ "order by f.fechaInicio",
								new Object[] {
										criterioBusquedaFechaDocumento,
										new Date(criterioBusquedaFechaDocumento
												.getTime() + 86399999) });

			if (list != null && list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
			else
				for (Factura egreso : list)
					inicializar(egreso);
		}
		return list;
	}

	public Long obtenerCantidadVendidaPorProveedorCliente(Integer clienteId,
			Long productoId, Date fechaInicio, Date fechaFin) {
		List<Cantidad> list = cantidadDao
				.obtenerPorHql(
						"select new Cantidad(sum(df.cantidadCuadre)) from DetalleFactura df "
								+ "inner join df.factura f "
								+ "inner join f.cliente c "
								+ "inner join df.producto p "
								+ "where (f.fechaInicio>=?1 "
								+ "and f.fechaInicio<=?2) and c.id=?3 "
								+ "and p.id=?4", new Object[] { fechaInicio,
								dateCompleto(fechaFin), clienteId, productoId });
		if (!list.isEmpty())
			return list.get(0) != null && list.get(0).getCantidad() != null ? list
					.get(0).getCantidad() : 0L;
		else
			return 0L;
	}

	public List<Factura> obtenerEntregaProducto(Date fechaInicio,
			Date fechaFin, Local local, Ciudad ciudad) {
		return facturaDao.obtenerPorHql(
				"select distinct f from Factura f "
						+ "inner join fetch f.detalleFactura "
						+ "inner join f.cliente cl inner join cl.persona pe "
						+ "inner join pe.ciudad cd where f.activo=true "
						+ "and f.fechaInicio >=?1 and f.fechaInicio <=?2 "
						+ "and f.establecimiento=?3 and cd.id=?4 "
						+ "order by f.fechaInicio",
				new Object[] { fechaInicio,
						new Date(fechaFin.getTime() + 86399999),
						local.getCodigoEstablecimiento(), ciudad.getId() });
	}

	public List<Factura> obtenerFacturasParaCredito(
			String criterioBusquedaCliente, String criterioBusquedaCodigo) {
		if (criterioBusquedaCliente.compareToIgnoreCase("") == 0
				&& criterioBusquedaCodigo.compareToIgnoreCase("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (criterioBusquedaCliente.length() >= 1
					&& criterioBusquedaCliente.length() <= 4)
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE MAS DE 4 CARACTERES PARA LA BÚSQUEDA POR CLIENTES");
			else if (!criterioBusquedaCodigo.matches("[0-9]*"))
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR DOCUMENTOS");
			else if (criterioBusquedaCodigo.compareToIgnoreCase("") != 0
					&& String.valueOf(criterioBusquedaCodigo).charAt(0) == '0')
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE PERMITE CEROS AL INICIO PARA LA BÚSQUEDA POR DOCUMENTOS");
			else {
				List<Factura> list = null;
				criterioBusquedaCliente = criterioBusquedaCliente.toUpperCase();
				if (criterioBusquedaCliente.compareTo("") != 0
						&& criterioBusquedaCodigo.compareTo("") != 0)
					list = facturaDao
							.obtenerPorHql(
									"select distinct f from Factura f "
											+ "left join f.credito cr "
											+ "inner join fetch f.detalleFactura "
											+ "inner join f.cliente c inner join c.persona p "
											+ "where cr.id is null "
											+ "and ((p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
											+ "and f.secuencia like ?2) and f.fechaCierre is null and f.activo=true "
											+ "order by f.fechaInicio asc",
									new Object[] {
											"%" + criterioBusquedaCliente + "%",
											"%" + criterioBusquedaCodigo + "%" });
				else if (criterioBusquedaCliente.compareTo("") != 0)
					list = facturaDao
							.obtenerPorHql(
									"select distinct f from Factura f "
											+ "left join f.credito cr "
											+ "inner join fetch f.detalleFactura "
											+ "inner join f.cliente c inner join c.persona p "
											+ "where cr.id is null "
											+ "and (p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
											+ "and f.fechaCierre is null and f.activo=true "
											+ "order by f.fechaInicio asc",
									new Object[] { "%"
											+ criterioBusquedaCliente + "%" });
				else if (criterioBusquedaCodigo.compareTo("") != 0)
					list = facturaDao
							.obtenerPorHql(
									"select distinct f from Factura f "
											+ "left join f.credito cr "
											+ "inner join fetch f.detalleFactura "
											+ "where cr.id is null and f.secuencia like ?1 "
											+ "and f.fechaCierre is null and f.activo=true "
											+ "order by f.fechaInicio asc",
									new Object[] { "%" + criterioBusquedaCodigo
											+ "%" });
				criterioBusquedaCliente = new String();
				criterioBusquedaCodigo = new String();
				for (Factura egreso : list)
					inicializar(egreso);
				return list;
			}
		}
		return new ArrayList<Factura>();
	}

	public List<Factura> obtenerFacturasParaGuiaRemision(
			String establecimiento, int ciudadId, String criterioBusquedaCodigo) {
		List<Factura> list = null;
		if (establecimiento.compareToIgnoreCase("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UN LOCAL");
		else if (ciudadId == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA CIUDAD");
		else {
			if (criterioBusquedaCodigo.compareTo("") == 0)
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "left join f.guiaRemision gr "
										+ "inner join fetch f.detalleFactura "
										+ "inner join f.cliente c inner join c.persona p "
										+ "inner join p.ciudad cd "
										+ "where gr.id is null "
										+ "and f.establecimiento=?1 "
										+ "and cd.id=?2 order by f.fechaInicio asc",
								new Object[] { establecimiento, ciudadId });
			else
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "left join f.guiaRemision gr "
										+ "inner join fetch f.detalleFactura "
										+ "inner join f.cliente c inner join c.persona p "
										+ "inner join p.ciudad cd "
										+ "where gr.id is null "
										+ "and f.establecimiento=?1  and cd.id=?2 "
										+ "and f.secuencia=?3"
										+ "order by f.fechaInicio asc",
								new Object[] { establecimiento, ciudadId,
										"%" + criterioBusquedaCodigo + "%" });
		}
		for (Factura egreso : list)
			inicializar(egreso);
		return list;
	}

	public List<Factura> obtenerFacturasPorCliente(
			String criterioBusquedaCliente, String criterioBusquedaCodigo,
			Integer tipoDocumentoId) {
		List<Factura> list = null;
		if (tipoDocumentoId == 0 && criterioBusquedaCodigo.compareTo("") == 0)
			list = facturaDao.obtenerPorHql("select distinct f from Factura f "
					+ "inner join fetch f.detalleFactura "
					+ "inner join f.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 order by f.fechaInicio asc",
					new Object[] { criterioBusquedaCliente });
		else if (tipoDocumentoId != 0
				&& criterioBusquedaCodigo.compareTo("") != 0)
			list = facturaDao.obtenerPorHql("select distinct f from Factura f "
					+ "inner join fetch f.detalleFactura "
					+ "inner join f.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 and f.id=?2 "
					+ "and f.codigoDocumento like ?3 "
					+ "order by f.fechaInicio asc", new Object[] {
					criterioBusquedaCliente, tipoDocumentoId,
					"%" + criterioBusquedaCodigo + "%" });
		else if (tipoDocumentoId != 0)
			list = facturaDao.obtenerPorHql("select distinct f from Factura f "
					+ "inner join fetch f.detalleFactura "
					+ "inner join f.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 and f.id=?2 "
					+ "order by f.fechaInicio asc", new Object[] {
					criterioBusquedaCliente, tipoDocumentoId });
		else if (criterioBusquedaCodigo.compareTo("") != 0)
			list = facturaDao.obtenerPorHql("select distinct f from Factura f "
					+ "inner join fetch f.detalleFactura "
					+ "inner join f.cliente c inner join c.persona p "
					+ "where  p.cedula=?1 " + "order by f.fechaInicio asc",
					new Object[] { criterioBusquedaCliente,
							"%" + criterioBusquedaCodigo + "%" });

		for (Factura egreso : list)
			inicializar(egreso);
		return list;
	}

	public List<GananciaFacturaReporte> obtenerGananciaBrutaPorFechas(
			Date fechaInicio, Date fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"ESCOJA LAS DOS FECHAS PARA GENERAR EL REPORTE");
		else if (fechaInicio.after(fechaFin))
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"LA FECHA DE INICIO DEBE SER MENOR A LA FECHA FINAL");
		else {
			List<GananciaFacturaReporte> list = new ArrayList<GananciaFacturaReporte>();
			List<Factura> listFactura = facturaDao.obtenerPorHql(
					"select distinct f from Factura f "
							+ "inner join fetch f.detalleFactura df "
							+ "where f.fechaCierre is not null "
							+ "and f.fechaCierre>=?1 and f.fechaCierre<=?2 "
							+ "and f.activo=true "
							+ "and (df.estadoProductoVenta=?3 "
							+ "or df.estadoProductoVenta=?4)", new Object[] {
							fechaInicio,
							new Date(fechaFin.getTime() + 86399999),
							EstadoProductoVenta.NR, EstadoProductoVenta.PI });
			for (Factura f : listFactura) {
				BigDecimal precioVenta = newBigDecimal();
				BigDecimal precioCosto = newBigDecimal();
				BigDecimal descuentoProducto = newBigDecimal();
				for (DetalleFactura df : f.getDetalleFactura()) {
					precioVenta = precioVenta.add(productoService
							.precioConImpuestos(
									df.getProducto(),
									multiplicar(df.getPrecioVentaCuadre(),
											df.getCantidadCuadre())));
					precioCosto = precioCosto.add(productoService
							.precioConImpuestos(
									df.getProducto(),
									multiplicar(df.getPrecioCosto(),
											df.getCantidadCuadre())));
					descuentoProducto = descuentoProducto.add(productoService
							.precioConImpuestos(df.getProducto(),
									df.getDescuentoCuadre()));
				}
				list.add(new GananciaFacturaReporte(f.getEstablecimiento()
						+ "-" + f.getPuntoEmision() + "-" + f.getSecuencia(), f
						.getFechaInicio(), f.getFechaCierre(),
						redondearTotales(precioVenta),
						redondearTotales(precioCosto),
						redondearTotales(descuentoProducto),
						redondearTotales(precioVenta.subtract(precioCosto)
								.subtract(descuentoProducto))));
			}
			return list;
		}
		return null;
	}

	public List<GananciaFacturaReporte> obtenerGananciaNetaPorFechas(
			Date fechaInicio, Date fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"ESCOJA LAS DOS FECHAS PARA GENERAR EL REPORTE");
		else if (fechaInicio.after(fechaFin))
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"LA FECHA DE INICIO DEBE SER MENOR A LA FECHA FINAL");
		else
			return gananciaFacturaReporteDao
					.obtenerPorHql(
							"select new GananciaFacturaReporte( "
									+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
									+ "f.fechaInicio, f.fechaCierre, "
									+ "round(sum(df.precioVentaCuadre*df.cantidadCuadre),2), "
									+ "round(sum(df.precioCosto*df.cantidadCuadre),2), "
									+ "round(sum(df.descuentoCuadre),2), "
									+ "round(sum(((df.precioVentaCuadre-df.precioCosto)*df.cantidadCuadre)-df.descuentoCuadre),2)) "
									+ "from Factura f "
									+ "inner join f.detalleFactura df "
									+ "where f.fechaCierre is not null "
									+ "and f.fechaCierre>=?1 and f.fechaCierre<=?2 and f.activo=true "
									+ "and (df.estadoProductoVenta=?3 or df.estadoProductoVenta=?4)"
									+ "group by f.id "
									+ "order by f.fechaCierre", new Object[] {
									fechaInicio, dateCompleto(fechaFin),
									EstadoProductoVenta.NR,
									EstadoProductoVenta.PI });
		return null;
	}

	public GananciaFacturaReporte obtenerGananciaPorFactura(Integer egresoId,
			BigDecimal iva) {
		iva = divide(iva, "100").add(new BigDecimal("1"));
		return (GananciaFacturaReporte) gananciaFacturaReporteDao
				.obtenerPorHql(
						"select new GananciaFacturaReporte(f.id, f.fechaInicio, f.fechaCierre, "
								+ "round(sum(case when (de.iva=true) then de.precioVentaCuadre*?1 "
								+ "*de.cantidadCuadre else de.precioVentaCuadre*de.cantidadCuadre end),2), "
								+ "round(sum(case when (de.iva=true) then de.precioCosto*?1 "
								+ "*de.cantidadCuadre else de.precioCosto*de.cantidadCuadre end),2), "
								+ "round(sum(de.descuentoCuadre),2), "
								+ "round(f.descuentoCuadre,2), 0)) "
								+ "from Factura f "
								+ "inner f.detalleFactura de "
								+ "inner join f.cliente cl "
								+ "inner join cl.persona p "
								+ "where f.pagado=true and f.id=?2 "
								+ "and de.estadoProductoVenta=1 "
								+ "group by f.codigoDocumento, f.id "
								+ "order by f.fechaInicio asc",
						new Object[] { iva, egresoId }).get(0);
	}

	public List<Factura> obtenerNoPagadosPorCiudad(int ciudadId) {
		List<Factura> list = facturaDao.obtenerPorHql(
				"select distinct f from Factura f "
						+ "inner join fetch f.detalleFactura df "
						+ "inner join fetch f.credito c "
						+ "inner join fetch c.detalleCreditos dc "
						+ "left join fetch dc.pagoCreditos pc "
						+ "inner join f.cliente cl inner join cl.persona p "
						+ "inner join p.ciudad cd "
						+ "where dc.pagado=false and dc.fechaLimite<?1 "
						+ "and cd.id=?2 order by f.fechaInicio", new Object[] {
						dateCompleto(new Date()), ciudadId });
		for (Factura egreso : list)
			inicializar(egreso);
		return list;
	}

	public Factura obtenerPorCodigo(String codigo) {
		String[] numDocumento = codigo.split("-");
		Factura egreso = null;
		List<Factura> lista = facturaDao
				.obtenerPorHql(
						"select f from Factura f where f.establecimiento like ?1 and f.puntoEmision like ?2 and f.secuencia like ?3",
						new Object[] { numDocumento[0], numDocumento[1],
								numDocumento[2] });
		if (!lista.isEmpty()) {
			egreso = lista.get(0);
			inicializar(egreso);
		}
		return egreso;
	}

	public List<Factura> obtenerPorCredito(String cedulaRuc,
			Integer criterioBusquedaEstado, String criterioBusquedaCodigo) {
		List<Factura> list = new ArrayList<Factura>();
		if ((cedulaRuc == null || cedulaRuc.compareTo("") == 0)
				&& criterioBusquedaEstado == 0
				&& criterioBusquedaCodigo.compareTo("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (cedulaRuc != null && cedulaRuc.compareTo("") != 0) {
				list = facturaDao.obtenerPorHql(
						"select distinct f from Factura f "
								+ "inner join f.cliente c "
								+ "inner join c.persona p "
								+ "left join fetch f.entradas en "
								+ "left join fetch en.pagoEntradas pe "
								+ "inner join fetch f.credito cr "
								+ "left join fetch cr.garantes g "
								+ "inner join fetch cr.detalleCreditos dc "
								+ "left join fetch dc.pagoCreditos pc "
								+ "where p.cedula=?1 "
								+ "order by f.fechaInicio asc",
						new Object[] { cedulaRuc });
			} else if (criterioBusquedaEstado != 0) {
				list = facturaDao.obtenerPorHql(
						"select distinct f from Factura f "
								+ "inner join f.cliente c "
								+ "inner join c.persona p "
								+ "left join fetch f.entradas en "
								+ "left join fetch en.pagoEntradas pe "
								+ "inner join fetch f.credito cr "
								+ "left join fetch cr.garantes g "
								+ "inner join fetch cr.detalleCreditos dc "
								+ "left join fetch dc.pagoCreditos pc "
								+ "where cr.pagado=?1 "
								+ "order by f.fechaInicio asc",
						new Object[] { criterioBusquedaEstado == 1 ? true
								: false });
			} else if (criterioBusquedaCodigo.compareTo("") != 0) {
				list = facturaDao.obtenerPorHql(
						"select distinct f from Factura f "
								+ "inner join f.cliente c "
								+ "inner join c.persona p "
								+ "left join fetch f.entradas en "
								+ "left join fetch en.pagoEntradas pe "
								+ "inner join fetch f.credito cr "
								+ "inner join fetch cr.detalleCreditos dc "
								+ "left join fetch dc.pagoCreditos pc "
								+ "left join fetch cr.garantes g "
								+ "where f.secuencia like ?1 "
								+ "order by f.fechaInicio asc",
						new Object[] { "%" + criterioBusquedaCodigo + "%" });
				/*
				 * list = facturaDao.obtenerPorHql(
				 * "select distinct f from Factura f " +
				 * "inner join f.tipodocumento td " + "inner join f.cliente c "
				 * + "inner join c.persona p " +
				 * "left join fetch f.entradas en " +
				 * "left join fetch en.pagoentradas pe " +
				 * "inner join fetch f.credito cr " +
				 * "left join fetch cr.garantes g " +
				 * "inner join fetch cr.detallecreditos dc " +
				 * "left join fetch dc.pagocreditos pc " +
				 * "where cr.pagado=false and f.codigodocumento like ?1 " +
				 * "order by f.fechainicio asc", new Object[] { "%" +
				 * criterioBusquedaCodigo + "%" });
				 */
			}
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"NO SE ENCONTRÓ NINGUNA COINCIDENCIA");
		}
		return list;
	}

	public List<Factura> obtenerPorCreditoAlDiaAndFechaLimite(Date fechaLimite) {
		List<Factura> list = facturaDao
				.obtenerPorHql(
						"select distinct f from Factura f inner join f.credito c "
								+ "where c.pagado=false and true=all(select dc.pagado from DetalleCredito dc "
								+ "where dc.credito.id=c.id and dc.fechaLimite<?1) "
								+ "order by f.id", new Object[] { fechaLimite });
		if (!list.isEmpty()) {
			for (Factura egreso : list)
				inicializar(egreso);
			return list;
		} else
			return null;
	}

	public List<Factura> obtenerPorCreditoVencidoAndFechaLimite(Date fechaLimite) {
		List<Factura> list = facturaDao.obtenerPorHql(
				"select f from Factura f inner join f.credito c "
						+ "inner join c.detalleCreditos dc "
						+ "where f.activo=true and dc.pagado=false and dc.fechaLimite<?1 "
						+ "group by f.id order by sum(dc.cuota) desc",
				new Object[] { fechaLimite });
		if (!list.isEmpty()) {
			for (Factura egreso : list)
				inicializar(egreso);
			return list;
		} else
			return null;
	}

	public List<Factura> obtenerPorEstado(String criterioBusquedaCliente,
			String criterioBusquedaNumeroDocumento,
			Date criterioBusquedafechaInicio, Date criterioBusquedafechaFin) {
		List<Factura> list = new ArrayList<Factura>();
		if (criterioBusquedaCliente == null
				&& criterioBusquedafechaInicio == null
				&& criterioBusquedafechaFin == null
				&& (criterioBusquedaNumeroDocumento == null || criterioBusquedaNumeroDocumento
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
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join f.cliente c inner join c.persona p "
										+ "where p.cedula=?1 "
										+ "and f.fechaCierre is null and f.activo=true order by f.fechaInicio",
								new Object[] { criterioBusquedaCliente });
			else if (criterioBusquedaNumeroDocumento != null
					&& criterioBusquedaNumeroDocumento.compareTo("") != 0)
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join f.cliente c inner join c.persona p "
										+ "where f.secuencia like ?1 "
										+ "and f.fechaCierre is null and f.activo=true order by f.secuencia",
								new Object[] { String.format(
										"%09d",
										Integer.parseInt(criterioBusquedaNumeroDocumento)) });
			else if (criterioBusquedafechaInicio != null
					&& criterioBusquedafechaFin != null
					&& !criterioBusquedafechaInicio
							.after(criterioBusquedafechaFin))
				list = facturaDao
						.obtenerPorHql(
								"select distinct f from Factura f "
										+ "inner join f.cliente c inner join c.persona p "
										+ "where f.fechaInicio>=?1 and f.fechaInicio<=?2 "
										+ "and f.fechaCierre is null and f.activo=true "
										+ "order by f.fechaInicio",
								new Object[] { criterioBusquedafechaInicio,
										dateCompleto(criterioBusquedafechaFin) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"EL CLIENTE NO TIENE FACTURAS PENDIENTES");
			else
				for (Factura egreso : list)
					inicializar(egreso);
		}
		return list;
	}

	public Factura obtenerPorFacturaId(Integer facturaId) {
		List<Factura> list = facturaDao.obtenerPorHql(
				"select f from Factura f "
						+ "inner join fetch f.detalleFactura "
						+ "where f.id=?1", new Object[] { facturaId });
		if (list != null && !list.isEmpty()) {
			Factura f = list.get(0);
			inicializar(f);
			return f;
		}
		return null;
	}

	public List<Factura> obtenerTodos() {
		return (List<Factura>) facturaDao.obtenerPorHql(
				"select distinct f from Factura f "
						+ "inner join fetch f.detalleFactura df "
						+ "where f.activo=true order by f.fechaInicio",
				new Object[] {});
	}

	public List<Factura> obtenerPorFechasAndVendedorAndEstadoDocumento(
			Date fechaInicio, Date fechaFin, int vendedorId, int estadoDocumento) {
		List<Factura> list = null;
		if (fechaInicio != null && fechaFin != null) {
			if (vendedorId == 0 && estadoDocumento == 0)
				list = facturaDao.obtenerPorHql("select f from Factura f "
						+ "inner join f.vendedor v "
						+ "where f.fechaInicio >= ?1 and f.fechaInicio <= ?2 "
						+ "and f.activo=true order by f.fechaInicio asc",
						new Object[] { fechaInicio, dateCompleto(fechaFin) });
			else if (vendedorId == 0 && estadoDocumento == 1)
				list = facturaDao.obtenerPorHql("select f from Factura f "
						+ "inner join f.vendedor v "
						+ "where f.fechaCierre is not null and "
						+ "f.activo=true and "
						+ "f.fechaCierre >= ?1 and f.fechaCierre <= ?2 "
						+ "order by f.fechaInicio asc", new Object[] {
						fechaInicio, dateCompleto(fechaFin) });
			else if (vendedorId == 0 && estadoDocumento == 2)
				list = facturaDao.obtenerPorHql("select f from Factura f "
						+ "inner join f.vendedor v "
						+ "where f.fechaCierre is null and "
						+ "f.activo=true and "
						+ "f.fechaInicio >= ?1 and f.fechaInicio <= ?2 "
						+ "order by f.fechaInicio asc", new Object[] {
						fechaInicio, dateCompleto(fechaFin) });
			else if (vendedorId != 0 && estadoDocumento == 0)
				list = facturaDao.obtenerPorHql("select f from Factura f "
						+ "inner join f.vendedor v "
						+ "where f.fechaInicio >= ?1 and f.fechaInicio <= ?2 "
						+ "and f.activo=true and v.id=?3 "
						+ "order by f.fechaInicio asc", new Object[] {
						fechaInicio, dateCompleto(fechaFin), vendedorId });
			else if (vendedorId != 0 && estadoDocumento == 1)
				list = facturaDao.obtenerPorHql("select f from Factura f "
						+ "inner join f.vendedor v "
						+ "where f.fechaCierre is not null and "
						+ "f.activo=true and "
						+ "f.fechaCierre >= ?1 and f.fechaCierre <= ?2 "
						+ "and v.id=?3 " + "order by f.fechaInicio asc",
						new Object[] { fechaInicio, dateCompleto(fechaFin),
								vendedorId });
			else if (vendedorId != 0 && estadoDocumento == 2)
				list = facturaDao.obtenerPorHql("select f from Factura f "
						+ "inner join f.vendedor v "
						+ "where f.fechaCierre is null and "
						+ "f.activo=true and "
						+ "f.fechaInicio >= ?1 and f.fechaInicio <= ?2 "
						+ "and v.id=?3 " + "order by f.fechaInicio asc",
						new Object[] { fechaInicio, dateCompleto(fechaFin),
								vendedorId });

			for (Factura egreso : list)
				inicializar(egreso);
		}
		return list;
	}

	public List<VolumenVentaProducto> obtenerVolumenVentaProducto(
			Date fechaInicio, Date fechaFin, int ordenarId,
			EstadoProductoVenta estadoProductoVenta) {
		List<VolumenVentaProducto> list = new ArrayList<VolumenVentaProducto>();
		if (fechaInicio == null && fechaFin == null
				&& estadoProductoVenta == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			list = volumenVentaProductoDao.obtenerPorHql(
					"select new VolumenVentaProducto(p.id, p.nombre, sum(df.cantidad), p.nombre) "
							+ "from DetalleFactura df "
							+ "inner join df.factura f "
							+ "inner join df.producto p "
							+ "where f.activo=true "
							+ "and f.fechaInicio>=?1 "
							+ "and f.fechaInicio<=?2 "
							+ (estadoProductoVenta == null ? ""
									: "and df.estadoProductoVenta=?3 ")
							+ "group by p.id, p.nombre "
							+ "order by "
							+ (ordenarId == 1 ? "p.nombre"
									: "sum(df.cantidad) desc"), new Object[] {
							fechaInicio,
							dateCompleto(fechaFin),
							estadoProductoVenta == null ? null
									: estadoProductoVenta });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public int posicion(FacturaReporte facturaReporte,
			List<DetalleFactura> detalleFacturas) {
		BigDecimal precioVenta = facturaReporte.getPrecioUnitVenta();

		for (DetalleFactura detalleFactura : detalleFacturas)
			if ((facturaReporte.getProducto().getId().compareTo(detalleFactura
					.getProducto().getId())) == 0
					&& (facturaReporte.getCantidad().compareTo(detalleFactura
							.getCantidad())) == 0
					&& (precioVenta.compareTo(redondear(detalleFactura
							.getPrecioVenta())) == 0))
				return detalleFacturas.indexOf(detalleFactura);

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

	public List<IngresoCaja> reporteIngreso(Date fechaInicio, Date fechaFin) {
		if (fechaInicio == null && fechaFin == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA FECHA");
		else
			return null;
		// (List<IngresoCaja>) facturaDao
		// .obtenerPorSql1(
		// "select en.entradaid as id, f.fechainicio as fechaFactura, pe.fechapago as fechaPago, "
		// +
		// "f.establecimiento||'-'||f.puntoemision||'-'||f.secuencia as codigoDocumento, "
		// + "p.apellido||' '||p.nombre as cliente, "
		// + "0 as totalFactura, "
		// + "en.cuota as pago, "
		// + "pe.tipopagoid as tipoPago "
		// + "from invfac.facturas f "
		// + "inner join rrhh.cliente c on (c.clienteid=f.clienteid) "
		// + "inner join rrhh.persona p on (p.personaid=c.personaid) "
		// + "inner join invfac.entrada en on (en.facturaid=f.facturaid) "
		// +
		// "inner join invfac.pagoentrada pe on (pe.detalleentradaid=en.entradaid) "
		// + "where f.activo=true and "
		// + "en.fechapago>='"
		// + dateInicio(fechaInicio)
		// + "' and en.fechapago<='"
		// + dateFin(fechaFin)
		// + "' order by en.fechapago;",
		// IngresoCaja.class);
		return new ArrayList<IngresoCaja>();
	}

	public List<IngresoCaja> reporteIngresoCaja(int cajero, Date fecha) {
		if (fecha == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA FECHA");
		else
			return ingresoCajaDao
					.obtenerPorHql(
							"select new IngresoCaja(en.id, f.fechaInicio, pe.fechaPago, "
									+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
									+ "p.apellido||' '||p.nombre, "
									+ "en.cuota, en.cuota, pe.tipoPago) "
									+ "from Factura f "
									+ "inner join f.clienteFactura c "
									+ "inner join c.persona p "
									+ "inner join f.entradas en "
									+ "inner join en.pagoEntradas pe "
									+ "inner join en.cajero ca "
									+ "where f.activo=true and ca.id=?1 "
									+ "and en.fechaPago>=?2 "
									+ "and en.fechaPago<=?3 "
									+ "order by en.fechaPago", new Object[] {
									cajero, fecha, dateCompleto(fecha) });
		return new ArrayList<IngresoCaja>();
	}

	public List<DetalleFactura> sumarCantidades(
			List<DetalleFactura> detalleFacturas) {
		List<DetalleFactura> detalleFacturasFinal = new ArrayList<DetalleFactura>();

		for (int i = 0; i < detalleFacturas.size(); i++)
			if (detalleFacturas.get(i).getEstadoProductoVenta() != EstadoProductoVenta.CB) {
				DetalleFactura detalleFactura = duplicarDetalleFactura(
						detalleFacturas.subList(i, i + 1)).get(0);

				for (int j = i + 1; j < detalleFacturas.size(); j++) {
					DetalleFactura dFactura = detalleFacturas.get(j);
					if (detalleFactura.getProducto().getId()
							.compareTo(dFactura.getProducto().getId()) == 0) {
						detalleFactura.setCantidad(detalleFactura.getCantidad()
								+ dFactura.getCantidad());

						detalleFacturas.remove(j);
						j--;
					}
				}
				detalleFacturasFinal.add(detalleFactura);
			}
		return detalleFacturasFinal;
	}

	public List<DetalleFactura> sumarCantidadesTodo(
			List<DetalleFactura> detalleFacturas) {
		List<DetalleFactura> detalleFacturasFinal = new ArrayList<DetalleFactura>();

		for (int i = 0; i < detalleFacturas.size(); i++) {
			DetalleFactura detalleFactura = duplicarDetalleFactura(
					detalleFacturas.subList(i, i + 1)).get(0);

			for (int j = i + 1; j < detalleFacturas.size(); j++) {
				DetalleFactura dFactura = detalleFacturas.get(j);
				if (detalleFactura.getProducto().getId()
						.compareTo(dFactura.getProducto().getId()) == 0) {
					detalleFactura.setCantidad(detalleFactura.getCantidad()
							+ dFactura.getCantidad());
					detalleFacturas.remove(j);
					j--;
				}
			}
			detalleFacturasFinal.add(detalleFactura);
		}
		return detalleFacturasFinal;
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