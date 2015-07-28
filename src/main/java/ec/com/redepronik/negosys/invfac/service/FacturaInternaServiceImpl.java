package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.divide;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.FacturaDao;
import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;

@Service
public class FacturaInternaServiceImpl implements FacturaInternaService {

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private TarifaService tarifaService;

	public void actualizarCuadre(List<FacturaReporte> list,
			List<DetalleFactura> listDetalleFactura) {
		for (FacturaReporte f : list) {
			int i = posicion(f, listDetalleFactura);
			listDetalleFactura.get(i).setCantidadCuadre(f.getCantidadCuadre());
			listDetalleFactura.get(i).setPrecioVentaCuadre(
					f.getPrecioUnitVentaCuadre());
			listDetalleFactura.get(i).setDescuentoCuadre(
					f.getDescuentoDolaresCuadre());
		}
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
		fr.setCantidadCuadre(de.getCantidadCuadre());
		fr.setPrecioCosto(de.getPrecioCosto());
		fr.setPrecioUnitVenta(de.getPrecioVenta());
		fr.setPrecioUnitVentaCuadre(de.getPrecioVentaCuadre());
		fr.setDescuentoDolares(de.getDescuento());
		fr.setDescuentoDolaresCuadre(de.getDescuentoCuadre());
		fr.setEstado(de.getEstadoProductoVenta());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), producto.getProductoUnidads()));
		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (fr.getEstado() == EstadoProductoVenta.PR) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			if (fr.getPrecioUnitVentaCuadre() != null
					&& fr.getCantidadCuadre() != null)
				fr.setDescuentoDolaresCuadre(multiplicar(
						fr.getPrecioUnitVentaCuadre(), fr.getCantidadCuadre()));
			fr.setImpuesto("P");
			fr.setDescripcion(fr.getDescripcion() + " PROMO");
		} else if (fr.getEstado() == EstadoProductoVenta.CB) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			if (fr.getPrecioUnitVentaCuadre() != null
					&& fr.getCantidadCuadre() != null)
				fr.setDescuentoDolaresCuadre(multiplicar(
						fr.getPrecioUnitVentaCuadre(), fr.getCantidadCuadre()));
			fr.setImpuesto("C");
			fr.setDescripcion(fr.getDescripcion() + " CAMBI");
		} else if (fr.getEstado() == EstadoProductoVenta.PI) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			if (fr.getPrecioUnitVentaCuadre() != null
					&& fr.getCantidadCuadre() != null)
				fr.setDescuentoDolaresCuadre(multiplicar(
						fr.getPrecioUnitVentaCuadre(), fr.getCantidadCuadre()));
			fr.setImpuesto("PI");
			fr.setDescripcion(fr.getDescripcion() + " PRO I");
		}

		fr.setDescuentoPorcentaje(multiplicarDivide(de.getDescuento(), 100,
				multiplicar(fr.getPrecioUnitVenta(), fr.getCantidad())));

		fr.setImporte(multiplicar(fr.getPrecioUnitVentaCuadre(),
				fr.getCantidadCuadre())
				.subtract(fr.getDescuentoDolaresCuadre()));
		fr.setPrecioUnitVenta(redondear(fr.getPrecioUnitVenta()));
		fr.setPrecioUnitVentaCuadre(redondear(fr.getPrecioUnitVentaCuadre()));
		fr.setDescuentoDolares(redondear(fr.getDescuentoDolares()));
		fr.setDescuentoDolaresCuadre(redondear(fr.getDescuentoDolaresCuadre()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public FacturaReporte asignar(FacturaReporte facturaReporte) {
		Producto producto = productoService.obtenerPorProductoId(facturaReporte
				.getProducto().getId());
		FacturaReporte fr = new FacturaReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getEan());
		fr.setDescripcion(producto.getNombre());
		fr.setCantidad(facturaReporte.getCantidad());
		fr.setCantidadCuadre(facturaReporte.getCantidadCuadre());
		fr.setPrecioUnitVenta(facturaReporte.getPrecioUnitVenta());
		fr.setPrecioUnitVentaCuadre(facturaReporte.getPrecioUnitVentaCuadre());
		fr.setDescuentoDolares(facturaReporte.getDescuentoDolares());
		fr.setDescuentoDolaresCuadre(facturaReporte.getDescuentoDolaresCuadre());
		fr.setEstado(facturaReporte.getEstado());
		fr.setImpuesto(productoService.impuestoProducto(producto
				.getProductosTarifas()));

		if (facturaReporte.getCantidad().compareTo(0) > 0)
			fr.setNombreCantidad(productoService.convertirUnidadString(
					fr.getCantidad(), producto.getProductoUnidads()));

		if (fr.getEstado() == EstadoProductoVenta.PR) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setDescuentoDolaresCuadre(fr.getDescuentoDolares());
			fr.setImpuesto("P");
		} else if (fr.getEstado() == EstadoProductoVenta.CB) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setDescuentoDolaresCuadre(fr.getDescuentoDolares());
			fr.setImpuesto("C");
		} else if (fr.getEstado() == EstadoProductoVenta.PI) {
			fr.setDescuentoDolares(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			fr.setDescuentoDolaresCuadre(fr.getDescuentoDolares());
			fr.setImpuesto("PI");
		}

		fr.setImporte(multiplicar(fr.getPrecioUnitVentaCuadre(),
				fr.getCantidadCuadre()));
		fr.setPrecioUnitVentaCuadre(redondear(fr.getPrecioUnitVentaCuadre()));
		fr.setDescuentoDolaresCuadre(redondear(fr.getDescuentoDolaresCuadre()));
		fr.setImporte(redondear(fr.getImporte()));
		return fr;
	}

	public CantidadFactura calcularCantidad(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidadCuadre() < 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD MAYOR QUE CERO");
			fr.setCantidadCuadre(0);
		}
		cambiarFRenLista(fr, list);
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularCantidadFactura(CantidadFactura cf,
			FacturaReporte fr) {

		BigDecimal total = multiplicar(fr.getPrecioUnitVentaCuadre(),
				fr.getCantidadCuadre())
				.subtract(fr.getDescuentoDolaresCuadre());

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
						.add(multiplicarDivide(total, pt.getTarifa()
								.getPorcentaje())));
			if (pt.getTarifa().getImpuesto() == Impuesto.IR)
				cf.setValorIRBPNR(cf.getValorIRBPNR()
						.add(multiplicarDivide(total, pt.getTarifa()
								.getPorcentaje())));
		}
		cf.settDescuentoProducto(cf.gettDescuentoProducto().add(
				fr.getDescuentoDolaresCuadre()));
		cf.settDescuento(cf.gettDescuentoProducto().add(
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
		if (fr.getCantidadCuadre() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA APLICAR EL DESCUENTO");
			fr.setDescuentoDolaresCuadre(newBigDecimal());
		} else {
			BigDecimal imp = multiplicar(fr.getPrecioUnitVentaCuadre(),
					fr.getCantidadCuadre());
			if (compareTo(fr.getDescuentoDolaresCuadre(), "0") < 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN DESCUENTO MAYOR QUE CERO");
				fr.setDescuentoDolaresCuadre(newBigDecimal());
			} else if (compareTo(fr.getDescuentoDolaresCuadre(), imp) > 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"INGRESE UN DESCUENTO MENOR O IGUAL QUE EL IMPORTE");
				fr.setDescuentoDolaresCuadre(imp);
			}
			fr.setImporte(imp.subtract(fr.getDescuentoDolaresCuadre()));
			if (compareTo(fr.getDescuentoDolaresCuadre(), imp) == 0) {
				fr.setEstado(EstadoProductoVenta.PR);
				return calcularPromocion(fr, list);
			} else {
				fr.setEstado(EstadoProductoVenta.NR);
				return calcularPromocion(fr, list);
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
				fr.setPrecioUnitVentaCuadre(fr.getPrecioUnitVenta());
				fr.setDescuentoDolaresCuadre(newBigDecimal());
			} else
				fr.setPrecioUnitVentaCuadre(divide(fr.getImporte(),
						fr.getCantidadCuadre()));
			cambiarFRenLista(fr, list);
		}
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularPrecio(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (compareTo(fr.getPrecioUnitVenta(), "0") <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN P.UNIT. MAYOR QUE CERO");
			fr.setPrecioUnitVentaCuadre(fr.getPrecioUnitVenta());
		}
		cambiarFRenLista(fr, list);
		return sumarCantidadFinal(list);
	}

	public CantidadFactura calcularPromocion(FacturaReporte fr,
			List<FacturaReporte> list) {
		if (fr.getCantidadCuadre() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD PARA APLICAR LA PROMOCION");
			fr.setEstado(EstadoProductoVenta.NR);
		} else {
			if (fr.getEstado().getId() == 2) {
				fr.setEstado(EstadoProductoVenta.PR);
				fr.setImpuesto("P");
				fr.setDescuentoDolaresCuadre(multiplicar(
						fr.getPrecioUnitVentaCuadre(), fr.getCantidadCuadre()));
			} else {
				fr.setEstado(EstadoProductoVenta.NR);
				if (compareTo(
						fr.getDescuentoDolaresCuadre(),
						multiplicar(fr.getPrecioUnitVentaCuadre(),
								fr.getCantidadCuadre())) == 0)
					fr.setDescuentoDolaresCuadre(newBigDecimal());
			}
		}
		return sumarCantidadFinal(list);
	}

	private int cambiarFRenLista(FacturaReporte fr, List<FacturaReporte> list) {
		int idx = list.indexOf(fr);
		if (idx >= 0)
			list.set(idx, asignar(fr));
		return idx;
	}

	private int posicion(FacturaReporte facturaReporte,
			List<DetalleFactura> detalleFacturas) {
		BigDecimal precioVenta = facturaReporte.getPrecioUnitVenta();

		for (DetalleFactura detalleFactura : detalleFacturas)
			if ((facturaReporte.getProducto().getId().compareTo(detalleFactura
					.getProducto().getId())) == 0
					&& (facturaReporte.getCantidad().compareTo(detalleFactura
							.getCantidad())) == 0
					&& (compareTo(redondearTotales(precioVenta),
							redondearTotales(detalleFactura.getPrecioVenta())) == 0))
				return detalleFacturas.indexOf(detalleFactura);

		return -1;
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

	private CantidadFactura sumarCantidadFinal(List<FacturaReporte> list) {
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