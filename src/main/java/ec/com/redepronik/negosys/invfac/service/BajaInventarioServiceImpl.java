package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
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

import ec.com.redepronik.negosys.invfac.dao.BajaInventarioDao;
import ec.com.redepronik.negosys.invfac.entity.BajaInventario;
import ec.com.redepronik.negosys.invfac.entity.DetalleBajaInventario;
import ec.com.redepronik.negosys.invfac.entity.EstadoKardex;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.MotivoBaja;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.BajaInventarioReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class BajaInventarioServiceImpl implements BajaInventarioService {

	@Autowired
	private BajaInventarioDao bajaInventarioDao;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private LocalService localService;

	private boolean añadirFRLista(BajaInventarioReporte fr,
			List<BajaInventarioReporte> list, int localId, int fila) {
		Producto p = productoService.obtenerPorEan(fr.getCodigo());
		if (p != null) {
			Kardex k = kardexService.obtenerSaldoActual(p.getEan(), localId);
			if (k.getCantidad() > 0) {
				fr.setProducto(p);
				fr.setPrecioCosto(k.getPrecio());
				fr.setDescripcion(p.getNombre());
				fr.setCantidadKardex(k.getCantidad());
				fr.setMotivoBaja(MotivoBaja.CA);
				list.set(fila, fr);
				return true;
			} else
				fr.setCodigo("");
		}
		return false;
	}

	public BajaInventarioReporte asignar(
			BajaInventarioReporte bajaInventarioReporte) {
		Producto p = productoService.obtenerPorProductoId(bajaInventarioReporte
				.getProducto().getId());
		BajaInventarioReporte fr = new BajaInventarioReporte();
		if (p != null) {
			fr.setProducto(p);
			fr.setCodigo(p.getEan());
			fr.setDescripcion(p.getNombre());
			fr.setMotivoBaja(bajaInventarioReporte.getMotivoBaja());
			fr.setCantidad(bajaInventarioReporte.getCantidad());
			fr.setCantidadKardex(bajaInventarioReporte.getCantidadKardex());
			fr.setPrecioCosto(bajaInventarioReporte.getPrecioCosto());

			if (bajaInventarioReporte.getCantidad().compareTo(0) > 0)
				fr.setNombreCantidad(productoService.convertirUnidadString(
						fr.getCantidad(), p.getProductoUnidads()));

			calcularImporte(fr);
			fr.setPrecioCosto(redondear(fr.getPrecioCosto()));
			fr.setImporte(redondear(fr.getImporte()));
		}
		return fr;
	}

	public BajaInventarioReporte asignar(DetalleBajaInventario de) {
		Producto producto = productoService.obtenerPorProductoId(de
				.getProducto().getId());

		BajaInventarioReporte fr = new BajaInventarioReporte();
		fr.setProducto(producto);
		fr.setCodigo(producto.getId().toString());
		fr.setDescripcion(producto.getNombre());
		fr.setMotivoBaja(de.getMotivoBaja());
		fr.setCantidad(de.getCantidad());
		fr.setCantidadKardex(de.getCantidad());
		fr.setPrecioCosto(de.getPrecioCosto());
		fr.setNombreCantidad(productoService.convertirUnidadString(
				fr.getCantidad(), producto.getProductoUnidads()));

		calcularImporte(fr);
		fr.setPrecioCosto(redondear(fr.getPrecioCosto()));
		fr.setImporte(redondearTotales(fr.getImporte()));
		return fr;
	}

	public BigDecimal calcularCantidad(BajaInventarioReporte fr,
			List<BajaInventarioReporte> list) {
		if (fr.getCantidad() < 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD MAYOR O IGUAL QUE CERO");
			fr.setCantidad(0);
		} else if (fr.getCantidad() > fr.getCantidadKardex()) {
			presentaMensaje(
					FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD MENOR A LA EXISTENTE: "
							+ fr.getCantidadKardex());
			fr.setCantidad(0);
		}
		cambiarFRenLista(fr, list);
		return sumarCantidadFinal(list);
	}

	public void calcularImporte(BajaInventarioReporte fr) {
		if (fr.getCantidad() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA CANTIDAD");
		if (compareTo(fr.getPrecioCosto(), "0") <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN PRECIO");
		else
			fr.setImporte(multiplicar(fr.getPrecioCosto(), fr.getCantidad()));
	}

	private int cambiarFRenLista(BajaInventarioReporte fr,
			List<BajaInventarioReporte> list) {
		int idx = list.indexOf(fr);
		if (idx >= 0)
			list.set(idx, asignar(fr));
		return idx;
	}

	public void cambiarMotivoBaja(BajaInventarioReporte fr,
			List<BajaInventarioReporte> list) {
		if (fr.getCantidad() < 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD MAYOR O IGUAL QUE CERO");
			fr.setCantidad(0);
		}
		cambiarFRenLista(fr, list);
	}

	public boolean cargarProductoLista(
			BajaInventarioReporte bajaInventarioReporte,
			List<BajaInventarioReporte> list, int localId) {
		int fila = list.indexOf(bajaInventarioReporte);
		if (fila == 0)
			return añadirFRLista(bajaInventarioReporte, list, localId, fila);
		else if (list.get(fila - 1).getCantidad() != null)
			return añadirFRLista(bajaInventarioReporte, list, localId, fila);
		return false;
	}

	public boolean comprobarCantidadFinal(List<DetalleBajaInventario> list,
			int bodegaId) {
		boolean bn = true;
		for (DetalleBajaInventario de : sumarCantidades(duplicarDetalleBajaInventario(list))) {
			Producto p = productoService.obtenerPorProductoId(de.getProducto()
					.getId());
			if (p.getTipoProducto().getId() == 1) {
				int cantidad = kardexService.obtenerSaldoActual(p.getEan(),
						bodegaId).getCantidad();
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

	public boolean convertirListaBajaInventarioReporteListaBajaInventarioDetalle(
			List<BajaInventarioReporte> list, BajaInventario bajaInventario) {
		boolean bn = true;
		bajaInventario
				.setDetalleBajaInventario(new ArrayList<DetalleBajaInventario>());
		for (BajaInventarioReporte f : quitarProductosCantidadCero(list)) {
			bn = false;
			Producto producto = productoService.obtenerPorProductoId(f
					.getProducto().getId());
			DetalleBajaInventario detalleBajaInventario = new DetalleBajaInventario();
			detalleBajaInventario.setProducto(producto);
			detalleBajaInventario.setCantidad(f.getCantidad());
			detalleBajaInventario.setPrecioCosto(producto.getPrecio());
			detalleBajaInventario.setMotivoBaja(f.getMotivoBaja());

			bajaInventario.addDetalleBajaInventario(detalleBajaInventario);
		}
		if (bn)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY CANTIDADES PARA DAR DE BAJA");

		return bn;
	}

	public List<DetalleBajaInventario> duplicarDetalleBajaInventario(
			List<DetalleBajaInventario> detalleBajaInventarios) {
		List<DetalleBajaInventario> list = new ArrayList<DetalleBajaInventario>();
		if (detalleBajaInventarios != null)
			for (DetalleBajaInventario dbi : detalleBajaInventarios)
				if (dbi != null) {
					DetalleBajaInventario dBI = new DetalleBajaInventario();
					dBI.setOrden(dbi.getOrden());
					dBI.setMotivoBaja(dbi.getMotivoBaja());
					dBI.setProducto(dbi.getProducto());
					dBI.setCantidad(dbi.getCantidad());
					dBI.setPrecioCosto(dbi.getPrecioCosto());
					list.add(dBI);
				}
		return list;
	}

	private void generarKardex(
			List<DetalleBajaInventario> detalleBajaInventarios,
			int bajaInventarioId, Local local) {

		for (DetalleBajaInventario dbi : detalleBajaInventarios) {
			Kardex kardex = new Kardex();
			kardex.setEstadoKardex(EstadoKardex.BI);
			kardex.setProducto(dbi.getProducto());
			kardex.setLocal(local);
			kardex.setNota(" # " + bajaInventarioId + " x "
					+ dbi.getMotivoBaja().getNombre());
			kardex.setFecha(timestamp());
			kardex.setCantidad(dbi.getCantidad());
			kardex.setPrecio(dbi.getPrecioCosto());
			kardex.setActivo(false);

			kardexService.insertar(kardex);

			Producto producto = productoService.obtenerPorProductoId(dbi
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

	public BajaInventario insertar(BajaInventario bajaInventario) {
		bajaInventario.setFecha(timestamp());
		bajaInventario.setBodeguero(empleadoService
				.obtenerEmpleadoCargoPorCedulaAndCargo(SecurityContextHolder
						.getContext().getAuthentication().getName(), 3));
		bajaInventario.setPuntoEmision("0");
		bajaInventario.setSecuencia("0");

		int c = 1;
		for (DetalleBajaInventario de : bajaInventario
				.getDetalleBajaInventario())
			de.setOrden(c++);
		bajaInventarioDao.insertar(bajaInventario);
		Local local = localService.obtenerPorEstablecimiento(bajaInventario
				.getEstablecimiento());

		generarKardex(bajaInventario.getDetalleBajaInventario(),
				bajaInventario.getId(), local);

		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"INSERTÓ BAJA DE INVENTARIO #" + bajaInventario.getId());
		return bajaInventario;
	}

	public List<BajaInventario> obtener(Boolean activo, int tipoDocumentoId) {
		List<BajaInventario> list = bajaInventarioDao
				.obtenerPorHql(
						"select e from BajaInventario e inner join e.tipodocumento td where td.tipodocumentoid=?1 order by e.fechainicio asc",
						new Object[] { tipoDocumentoId });
		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO SE ENCONTRARON COINCIDENCIAS");
		return list;
	}

	public List<BajaInventario> obtener(String criterioBusquedaNumeroBaja,
			Date criterioBusquedaFechaDocumento) {
		List<BajaInventario> list = null;
		if (criterioBusquedaNumeroBaja.compareToIgnoreCase("") == 0
				&& criterioBusquedaFechaDocumento == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else if (!criterioBusquedaNumeroBaja.matches("[0-9]*"))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR NÚMERO");
		else if (criterioBusquedaNumeroBaja.length() >= 1
				&& criterioBusquedaNumeroBaja.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR NÚMERO");
		else {
			if (criterioBusquedaNumeroBaja.compareTo("") != 0) {
				list = bajaInventarioDao
						.obtenerPorHql(
								"select distinct bi from BajaInventario bi "
										+ "inner join fetch bi.detalleBajaInventario dbi "
										+ "inner join fetch dbi.producto p "
										+ "where bi.secuencia like ?1 order by bi.fecha",
								new Object[] { "%" + criterioBusquedaNumeroBaja
										+ "%" });
			} else if (criterioBusquedaFechaDocumento != null)
				list = bajaInventarioDao
						.obtenerPorHql(
								"select distinct bi from BajaInventario bi "
										+ "inner join fetch bi.detalleBajaInventario dbi "
										+ "inner join fetch dbi.producto p "
										+ "where bi.fecha>=?1 and bi.fecha<=?2 "
										+ "order by bi.fecha",
								new Object[] {
										criterioBusquedaFechaDocumento,
										dateCompleto(criterioBusquedaFechaDocumento) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public BajaInventario obtenerPorBajaInventarioId(Integer bajaInventarioId) {
		BajaInventario bajaInventario = (BajaInventario) bajaInventarioDao
				.obtenerPorHql(
						"select distinct bi from BajaInventario bi "
								+ "left join fetch bi.detalleBajaInventario "
								+ "where bi.id=?1",
						new Object[] { bajaInventarioId }).get(0);
		return bajaInventario;
	}

	public int posicion(BajaInventarioReporte bajaInventarioReporte,
			List<DetalleBajaInventario> detalleBajaInventarios, boolean factura) {
		for (DetalleBajaInventario detalleBajaInventario : detalleBajaInventarios)
			if ((bajaInventarioReporte.getProducto().getId()
					.compareTo(detalleBajaInventario.getProducto().getId())) == 0
					&& (bajaInventarioReporte.getCantidad()
							.compareTo(detalleBajaInventario.getCantidad())) == 0)
				return detalleBajaInventarios.indexOf(detalleBajaInventario);

		return -1;
	}

	public List<BajaInventarioReporte> quitarProductosCantidadCero(
			List<BajaInventarioReporte> list) {
		if (list.size() > 1
				&& list.get(list.size() - 1).getProducto().getId() == null)
			list.remove(list.size() - 1);
		List<BajaInventarioReporte> listBajaInventarioReporte = new ArrayList<BajaInventarioReporte>();
		for (BajaInventarioReporte f : list)
			if (f.getCantidad() != 0)
				listBajaInventarioReporte.add(f);
		return listBajaInventarioReporte;
	}

	public CantidadFactura redondearCantidadFactura(CantidadFactura cf) {
		cf.setStSinImpuesto(redondearTotales(cf.getStSinImpuesto()));
		cf.setSt12(redondearTotales(cf.getSt12()));
		cf.setSt0(redondearTotales(cf.getSt0()));
		cf.setStNoObjetoIva(redondearTotales(cf.getStNoObjetoIva()));
		cf.setStExentoIva(redondearTotales(cf.getStExentoIva()));
		cf.settDescuentoProducto(redondearTotales(cf.gettDescuentoProducto()));
		cf.settDescuento(redondearTotales(cf.gettDescuento()));
		cf.setValorIce(redondearTotales(cf.getValorIce()));
		cf.setValorIRBPNR(redondearTotales(cf.getValorIRBPNR()));
		cf.setIva12(redondearTotales(cf.getIva12()));
		cf.setPropina(redondearTotales(cf.getPropina()));
		cf.setValorTotal(redondearTotales(cf.getValorTotal()));
		return cf;
	}

	public List<DetalleBajaInventario> sumarCantidades(
			List<DetalleBajaInventario> detalleBajaInventarios) {
		detalleBajaInventarios = duplicarDetalleBajaInventario(detalleBajaInventarios);
		List<DetalleBajaInventario> detalleBajaInventariosFinal = new ArrayList<DetalleBajaInventario>();

		for (int i = 0; i < detalleBajaInventarios.size(); i++) {
			DetalleBajaInventario detalleBajaInventario = detalleBajaInventarios
					.get(i);

			for (int j = i + 1; j < detalleBajaInventarios.size(); j++) {
				DetalleBajaInventario dBajaInventario = detalleBajaInventarios
						.get(j);
				if (detalleBajaInventario.getProducto().getId()
						.compareTo(dBajaInventario.getProducto().getId()) == 0) {
					detalleBajaInventario.setCantidad(detalleBajaInventario
							.getCantidad() + dBajaInventario.getCantidad());
					detalleBajaInventarios.remove(j);
					j--;
				}
			}
			detalleBajaInventariosFinal.add(detalleBajaInventario);
		}
		return detalleBajaInventariosFinal;
	}

	public List<DetalleBajaInventario> sumarCantidadesTodo(
			List<DetalleBajaInventario> detalleBajaInventarios) {
		List<DetalleBajaInventario> detalleBajaInventariosFinal = new ArrayList<DetalleBajaInventario>();

		for (int i = 0; i < detalleBajaInventarios.size(); i++) {
			DetalleBajaInventario detalleBajaInventario = duplicarDetalleBajaInventario(
					detalleBajaInventarios.subList(i, i + 1)).get(0);

			for (int j = i + 1; j < detalleBajaInventarios.size(); j++) {
				DetalleBajaInventario dBajaInventario = detalleBajaInventarios
						.get(j);
				if (detalleBajaInventario.getProducto().getId()
						.compareTo(dBajaInventario.getProducto().getId()) == 0) {
					detalleBajaInventario.setCantidad(detalleBajaInventario
							.getCantidad() + dBajaInventario.getCantidad());
					detalleBajaInventarios.remove(j);
					j--;
				}
			}
			detalleBajaInventariosFinal.add(detalleBajaInventario);
		}
		return detalleBajaInventariosFinal;
	}

	public BigDecimal sumarCantidadFinal(List<BajaInventarioReporte> list) {
		BigDecimal total = newBigDecimal();
		if (list == null || list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "NO HAY DETALLES");
		else
			for (BajaInventarioReporte f : list)
				if (f.getCantidad() > 0)
					total = total.add(f.getImporte());
		return redondearTotales(total);
	}

}