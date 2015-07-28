package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.Utils.invertirPalabra;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.ProductoTarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;

@Service
public class FacturaCajeroServiceImpl implements FacturaCajeroService {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private TarifaService tarifaService;

	private boolean añadirFRLista(FacturaReporte fr, int bodegaId, int fila) {
		Producto p = productoService.obtenerPorEan(fr.getCodigo());
		if (p != null) {
			int precioId = productoService.obtenerPvpPorProducto(p
					.getTipoPrecioProductos());
			Kardex k = kardexService.obtenerSaldoActual(p.getEan(), bodegaId);

			if (k != null) {
				fr.setProducto(p);
				fr.setCodigo(p.getEan());
				fr.setDescripcion(p.getNombre() + "      ");
				fr.setPrecioId(precioId);
				fr.setCantidadKardex(k.getCantidad());
				fr.setPrecioUnitVenta(redondear(productoService.calcularPrecio(
						p.getEan(), p.getTipoPrecioProductos(), k, bodegaId,
						fr.getPrecioId())));
				fr.setPrecioCosto(k.getPrecio());
				fr.setImpuesto(productoService.impuestoProducto(p
						.getProductosTarifas()));
				return true;
			}
		} else
			fr.setCodigo("");
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"EL PRODUCTO NO TIENE KARDEX");
		return false;
	}

	public CantidadFactura calcularCantidad(CantidadFactura cantidadFactura,
			FacturaReporte fr, boolean bn) {
		if (bn)
			if (fr.getCantidad() > fr.getCantidadKardex()) {
				presentaMensaje(
						FacesMessage.SEVERITY_ERROR,
						"INGRESE UNA CANTIDAD MENOR A LA EXISTENTE: "
								+ fr.getCantidadKardex());
				fr.setCantidad(0);
			}
		calcularImporte(fr);
		return calcularCantidadFactura(cantidadFactura, fr);
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

	public void calcularImporte(FacturaReporte fr) {
		if (fr.getCantidad() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA CANTIDAD");
		if (compareTo(fr.getPrecioUnitVenta(), "0") <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN PRECIO");
		else {
			BigDecimal imp = redondear(multiplicar(fr.getPrecioUnitVenta(),
					fr.getCantidad()));
			if (fr.getEstado() == EstadoProductoVenta.PI)
				fr.setDescuentoDolares(imp);
			fr.setImporte(redondear(imp.subtract(fr.getDescuentoDolares())));
		}
	}

	public boolean cargarProductoLista(FacturaReporte facturaReporte,
			List<FacturaReporte> list, int bodegaId) {
		int fila = list.indexOf(facturaReporte);
		if (fila == 0)
			return añadirFRLista(facturaReporte, bodegaId, fila);
		else if (list.get(fila - 1).getCantidad() != null)
			return añadirFRLista(facturaReporte, bodegaId, fila);
		return false;
	}

	public int contarRegistrosActivos(List<FacturaReporte> facturaReporte) {
		int registrosActivos = 0;
		for (FacturaReporte fr : facturaReporte)
			if (fr.getCantidad() > 0)
				registrosActivos++;
		return registrosActivos;
	}

	public void precioMayorista(FacturaReporte fr) {
		Producto producto = productoService.obtenerPorProductoId(fr
				.getProducto().getId());
		fr.setPrecioUnitVenta(productoService.calcularPrecioMayorista(producto));
	}

	public void promocion(FacturaReporte fr) {
		fr.setEstado(EstadoProductoVenta.PI);
		fr.setImpuesto("PI");
		String aux = invertirPalabra(fr.getDescripcion());
		fr.setDescripcion(invertirPalabra(aux.substring(6, aux.length()))
				+ " PRO I");
	}

	public CantidadFactura redondearCantidadFactura(CantidadFactura cfc) {
		CantidadFactura cfp = new CantidadFactura();
		cfp.setStSinImpuesto(redondearTotales(cfc.getStSinImpuesto()));
		cfp.setSt12(redondearTotales(cfc.getSt12()));
		cfp.setSt0(redondearTotales(cfc.getSt0()));
		cfp.setStNoObjetoIva(redondearTotales(cfc.getStNoObjetoIva()));
		cfp.setStExentoIva(redondearTotales(cfc.getStExentoIva()));
		cfp.settDescuentoProducto(redondearTotales(cfc.gettDescuentoProducto()));
		cfp.settDescuentoEgreso(redondearTotales(cfc.gettDescuentoEgreso()));
		cfp.settDescuento(redondearTotales(cfc.gettDescuento()));
		cfp.setValorIce(redondearTotales(cfc.getValorIce()));
		cfp.setValorIRBPNR(redondearTotales(cfc.getValorIRBPNR()));
		cfp.setIva12(redondearTotales(cfc.getIva12()));
		cfp.setPropina(redondearTotales(cfc.getPropina()));
		cfp.setValorTotal(redondearTotales(cfc.getValorTotal()));
		return cfp;
	}

}