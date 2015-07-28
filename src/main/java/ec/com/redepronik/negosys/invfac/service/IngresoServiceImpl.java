package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.IngresoDao;
import ec.com.redepronik.negosys.invfac.entity.CuotaIngreso;
import ec.com.redepronik.negosys.invfac.entity.DetalleIngreso;
import ec.com.redepronik.negosys.invfac.entity.EstadoKardex;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.ChequeReporte;

@Service
public class IngresoServiceImpl implements IngresoService {

	@Autowired
	private IngresoDao ingresoDao;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private LocalService localService;

	public String actualizar(Ingreso ingreso) {
		ingresoDao.actualizar(ingreso);
		return "save";
	}

	public BigDecimal calcularEntradas(Ingreso ingreso) {
		BigDecimal total = newBigDecimal();
		if (ingreso.getCuotaIngresos() != null
				&& !ingreso.getCuotaIngresos().isEmpty())
			for (CuotaIngreso ci : ingreso.getCuotaIngresos())
				total = total.add(ci.getCuota());
		return redondearTotales(total);
	}

	public BigDecimal calcularTotal(Ingreso ingreso) {
		return redondearTotales(ingreso.getTotal());
	}

	public void comprobar(Ingreso ingreso) {
		if (ingreso.getProveedor() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN PROVEEDOR", "error", false);
		else if (ingreso.getCodigoDocumento().equals(""))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE INGRESAR UN NUMERO DE DOCUMENTO", "error", false);
		else if (ingreso.getFechaFactura() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE INGRESAR LA FECHA DE LA FACTURA", "error", false);
		else if (ingreso.getDetalleIngresos() == null
				|| ingreso.getDetalleIngresos().size() == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE INGRESAR PRODUCTOS", "error", false);
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CORRECTO", "error",
					true);
	}

	private boolean comprobarDetalles(List<DetalleIngreso> detallesIngreso) {
		boolean bn = false;
		for (DetalleIngreso di : detallesIngreso) {
			bn = (di.getCantidad() <= 0 || di.getPrecio().compareTo(
					newBigDecimal("0.00")) <= 0) ? true : false;
			if (bn == false)
				break;
		}
		return bn;
	}

	public Long contar() {
		return (Long) ingresoDao.contar(Ingreso.class);
	}

	public void eliminar(Ingreso ingreso) {
		if (ingreso.getActivo())
			ingreso.setActivo(false);
		else
			ingreso.setActivo(true);
		ingresoDao.actualizar(ingreso);
	}

	public void eliminarDetalle(Ingreso ingreso, DetalleIngreso detalleIngreso) {
		ingreso.removeDetalleIngreso(detalleIngreso);
		presentaMensaje(FacesMessage.SEVERITY_INFO, "ELIMINÓ EL DETALLE: "
				+ detalleIngreso.getProducto().getEan());
	}

	private void eliminarDetalles(List<DetalleIngreso> detallesIngreso) {
		for (int i = 0; i < detallesIngreso.size(); i++) {
			DetalleIngreso di = detallesIngreso.get(i);
			if (di.getCantidad() <= 0
					|| di.getPrecio().compareTo(newBigDecimal("0.00")) <= 0) {
				detallesIngreso.remove(i);
				i--;
			}
		}
	}

	public boolean insertar(Ingreso ingreso, List<Local> listaBodegas) {
		boolean retorno = false;
		ingreso.setFechaIngreso(timestamp());
		ingreso.setActivo(true);

		if (ingreso.getProveedor() == null
				|| ingreso.getProveedor().getId() == null
				|| ingreso.getProveedor().getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN PROVEEDOR");

		else if (ingreso.getCodigoDocumento().equals(""))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE INGRESAR UN NUMERO DE DOCUMENTO");
		else if (ingreso.getFechaFactura() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE INGRESAR LA FECHA DE LA FACTURA");
		else if (ingreso.getTotal().compareTo(new BigDecimal("0")) == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE EL TOTAL DE LA FACTURA");
		else if (ingreso.getDetalleIngresos() == null
				|| ingreso.getDetalleIngresos().isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE INGRESAR PRODUCTOS");
		else if (comprobarDetalles(ingreso.getDetalleIngresos()))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO TIENE CANTIDADES A FACTURAR");
		else {
			eliminarDetalles(ingreso.getDetalleIngresos());
			int c = 1;
			for (DetalleIngreso di : ingreso.getDetalleIngresos())
				di.setOrden(c++);
			if (ingreso.getPagado())
				ingreso.setFechaCierre(timestamp());

			ingresoDao.insertar(ingreso);
			retorno = true;

			List<DetalleIngreso> detalleIngresos = ingreso.getDetalleIngresos();
			for (DetalleIngreso detalleIngreso : detalleIngresos) {
				Producto producto = productoService
						.obtenerPorProductoId(detalleIngreso.getProducto()
								.getId());

				Local local = localService.obtenerPorLocalId(detalleIngreso
						.getLocal().getId());

				Kardex kardex = new Kardex();
				kardex.setEstadoKardex(EstadoKardex.CO);
				kardex.setProducto(producto);
				kardex.setLocal(local);
				kardex.setNota("FACTURA # " + ingreso.getCodigoDocumento());
				kardex.setFecha(timestamp());
				kardex.setCantidad(detalleIngreso.getCantidad());
				kardex.setPrecio(detalleIngreso.getPrecio());
				kardex.setActivo(false);
				kardexService.insertar(kardex);

				Map<String, Object> parametro = kardexService.ponderarPrecio(
						producto, local.getId(), kardex.getPrecio(),
						kardex.getCantidad());
				BigDecimal valorNuevoPrecio = (BigDecimal) parametro
						.get("precio");
				int cantidad = (Integer) parametro.get("cantidad");

				kardexService.insertar(kardexService.generarKardexSaldo(kardex,
						cantidad, valorNuevoPrecio, true));
			}
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESO A BODEGA EXITOSO");
		}

		return retorno;
	}

	public boolean insertarDetalle(Ingreso ingreso,
			DetalleIngreso detalleIngreso) {
		if (detalleIngreso.getProducto() == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN PRODUCTO",
					"error", false);
			return false;
		} else if (detalleIngreso.getLocal() == null
				|| detalleIngreso.getLocal().getId() == null
				|| detalleIngreso.getLocal().getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UNA BODEGA",
					"error", false);
			return false;
		} else if (detalleIngreso.getCantidad() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UNA CANTIDAD", "error", false);
			return false;
		} else if (detalleIngreso.getPrecio().compareTo(newBigDecimal()) == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE EL PRECIO COSTO", "error", false);
			return false;
		} else {
			if (ingreso.getDetalleIngresos() == null)
				ingreso.setDetalleIngresos(new ArrayList<DetalleIngreso>());

			boolean bn = true;
			for (DetalleIngreso di : ingreso.getDetalleIngresos()) {
				if (di.getProducto().getId()
						.compareTo(detalleIngreso.getProducto().getId()) == 0) {
					bn = false;
					break;
				}
			}

			if (bn) {
				detalleIngreso.setLocal(localService
						.obtenerPorLocalId(detalleIngreso.getLocal().getId()));
				detalleIngreso.setProducto(productoService
						.obtenerPorProductoId(detalleIngreso.getProducto()
								.getId()));
				ingreso.addDetalleingreso(detalleIngreso);

				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"INSERTÓ EL DETALLE: "
								+ detalleIngreso.getProducto()
										.getNombre(), "error", true);
			} else
				presentaMensaje(
						FacesMessage.SEVERITY_WARN,
						"EL PRODUCTO YA ESTA REGISTRADO MODIFIQUE SU CANTIDAD O PRECIO",
						"error", true);
			return true;
		}
	}

	public void limpiarDetalleingreso(Ingreso ingreso, List<Local> listaLocal) {
		ingreso.getDetalleIngresos().add(new DetalleIngreso());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setIngreso(ingreso);
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setProducto(new Producto());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setLocal(new Local());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1).setCantidad(0);

		if (!listaLocal.isEmpty())
			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1)
					.setLocal(listaLocal.get(0));
	}

	public List<Ingreso> obtener(Boolean activo) {
		List<Ingreso> list = ingresoDao.obtener(Ingreso.class, "ingresoid",
				activo);
		return list;
	}

	public List<Ingreso> obtener(String criterioBusquedaProveedor,
			String criterioBusquedaNumeroFactura,
			Date criterioBusquedaFechaIngreso, Date criterioBusquedaFechaFactura) {
		criterioBusquedaProveedor = criterioBusquedaProveedor.toUpperCase();
		List<Ingreso> list = new ArrayList<Ingreso>();
		if (criterioBusquedaProveedor.compareToIgnoreCase("") == 0
				&& criterioBusquedaNumeroFactura.compareToIgnoreCase("") == 0
				&& criterioBusquedaFechaIngreso == null
				&& criterioBusquedaFechaFactura == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else if (criterioBusquedaProveedor.length() >= 1
				&& criterioBusquedaProveedor.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR PROVEEDORES");
		else if (criterioBusquedaNumeroFactura.length() >= 1
				&& criterioBusquedaNumeroFactura.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR NÚMERO DE DOCUMENTO");
		else if (!criterioBusquedaNumeroFactura.matches("[0-9]*"))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR NÚMERO DE DOCUMENTO");
		else {
			if (criterioBusquedaProveedor.compareTo("") != 0)
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "inner join fetch i.detalleIngresos di "
										+ "inner join di.local l "
										+ "inner join i.proveedor pd inner join pd.persona p "
										+ "where (p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "order by i.fechaFactura asc",
								new Object[] { "%" + criterioBusquedaProveedor
										+ "%" });
			else if (criterioBusquedaNumeroFactura.compareTo("") != 0)
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "inner join fetch i.detalleIngresos di "
										+ "inner join di.local l "
										+ "inner join i.proveedor pd inner join pd.persona p "
										+ "where i.codigoDocumento like ?1 "
										+ "order by i.fechaFactura asc",
								new Object[] { "%"
										+ criterioBusquedaNumeroFactura + "%" });
			else if (criterioBusquedaFechaIngreso != null)
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "inner join fetch i.detalleIngresos di "
										+ "inner join di.local l "
										+ "where i.fechaIngreso>=?1 and i.fechaIngreso<=?2 "
										+ "order by i.fechaIngreso asc",
								new Object[] {
										criterioBusquedaFechaIngreso,
										new Date(criterioBusquedaFechaIngreso
												.getTime() + 86399999) });
			else if (criterioBusquedaFechaFactura != null)
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "inner join fetch i.detalleIngresos di "
										+ "inner join di.local l "
										+ "where i.fechaFactura>=?1 and i.fechaFactura<=?2 "
										+ "order by i.fechaFactura asc",
								new Object[] {
										criterioBusquedaFechaFactura,
										new Date(criterioBusquedaFechaFactura
												.getTime() + 86399999) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public List<ChequeReporte> obtenerChequesPorFechas(Date fechaInicio,
			Date fechaFin) {
		List<ChequeReporte> list = null;
		// if (fechaInicio == null && fechaFin == null) {
		// list = (List<ChequeReporte>) ingresoDao
		// .obtenerPorSql1(
		// "select ci.cuotaingresoid||ci.chequevaucher||ci.cuentatarjeta as id, "
		// + "ci.banco as banco, "
		// + "ci.chequevaucher as numeroCheque, "
		// + "ci.cuentatarjeta as numeroCuenta, "
		// + "ci.fechacheque as fechaEmision "
		// + "from invfac.cuotaingreso ci "
		// + "inner join invfac.tipopago tp on (tp.tipopagoid=ci.tipopagoid) "
		// + "where tp.nombre='CHEQUE' and ci.pagado=false "
		// + "order by ci.fechacheque;",
		// ChequeReporte.class);
		// } else if (fechaInicio == null) {
		// list = (List<ChequeReporte>) ingresoDao
		// .obtenerPorSql1(
		// "select ci.cuotaingresoid||ci.chequevaucher||ci.cuentatarjeta as id, "
		// + "ci.banco as banco, "
		// + "ci.chequevaucher as numeroCheque, "
		// + "ci.cuentatarjeta as numeroCuenta, "
		// + "ci.fechacheque as fechaEmision "
		// + "from invfac.cuotaingreso ci "
		// + "inner join invfac.tipopago tp on (tp.tipopagoid=ci.tipopagoid) "
		// +
		// "where tp.nombre='CHEQUE' and ci.pagado=false and ci.fechacheque<='"
		// + fechaFormatoString(fechaFin)
		// + "' order by ci.fechacheque;",
		// ChequeReporte.class);
		// } else if (fechaFin == null) {
		// list = (List<ChequeReporte>) ingresoDao
		// .obtenerPorSql1(
		// "select ci.cuotaingresoid||ci.chequevaucher||ci.cuentatarjeta as id, "
		// + "ci.banco as banco, "
		// + "ci.chequevaucher as numeroCheque, "
		// + "ci.cuentatarjeta as numeroCuenta, "
		// + "ci.fechacheque as fechaEmision "
		// + "from invfac.cuotaingreso ci "
		// + "inner join invfac.tipopago tp on (tp.tipopagoid=ci.tipopagoid) "
		// +
		// "where tp.nombre='CHEQUE' and ci.pagado=false and ci.fechacheque>='"
		// + fechaFormatoString(fechaInicio)
		// + "' order by ci.fechacheque;",
		// ChequeReporte.class);
		// } else {
		// list = (List<ChequeReporte>) ingresoDao
		// .obtenerPorSql1(
		// "select ci.cuotaingresoid||ci.chequevaucher||ci.cuentatarjeta as id, "
		// + "ci.banco as banco, "
		// + "ci.chequevaucher as numeroCheque, "
		// + "ci.cuentatarjeta as numeroCuenta, "
		// + "ci.fechacheque as fechaEmision "
		// + "from invfac.cuotaingreso ci "
		// + "inner join invfac.tipopago tp on (tp.tipopagoid=ci.tipopagoid) "
		// +
		// "where tp.nombre='CHEQUE' and ci.pagado=false  and ci.fechacheque >='"
		// + fechaFormatoString(fechaInicio)
		// + "' and ci.fechacheque<='"
		// + fechaFormatoString(fechaFin)
		// + "' order by ci.fechacheque;",
		// ChequeReporte.class);
		// }
		return list;
	}

	public List<Ingreso> obtenerIngresosNoPagados(
			String criterioBusquedaProveedor, String criterioBusquedaDocumento,
			Date criterioBusquedafechaInicio, Date criterioBusquedafechaFin) {
		List<Ingreso> list = new ArrayList<Ingreso>();
		if (criterioBusquedaProveedor == null
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
			criterioBusquedaDocumento = criterioBusquedaDocumento.toLowerCase();
			if (criterioBusquedaProveedor != null
					&& criterioBusquedaProveedor.compareTo("") != 0)
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "inner join i.proveedor pr inner join pr.persona p "
										+ "left join fetch i.cuotaIngresos "
										+ "where p.cedula=?1 and i.pagado=false and i.activo=true order by i.fechaFactura asc",
								new Object[] { criterioBusquedaProveedor });
			else if (criterioBusquedaDocumento != null
					&& criterioBusquedaDocumento.compareTo("") != 0)
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "left join fetch i.cuotaIngresos "
										+ "where i.codigoDocumento like ?1 "
										+ "and i.pagado=false and i.activo=true order by i.fechaFactura asc",
								new Object[] { "%" + criterioBusquedaDocumento
										+ "%" });
			else if (criterioBusquedafechaInicio != null
					&& criterioBusquedafechaFin != null
					&& !criterioBusquedafechaInicio
							.after(criterioBusquedafechaFin))
				list = ingresoDao
						.obtenerPorHql(
								"select distinct i from Ingreso i "
										+ "left join fetch i.cuotaIngresos "
										+ "where e.fechaInicio>=?1 and e.fechaInicio<=?2 "
										+ "and i.pagado=false and i.activo=true order by i.fechaFactura asc",
								new Object[] {
										criterioBusquedafechaInicio,
										new Date(criterioBusquedafechaFin
												.getTime() + 86399999) });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO TIENE FACTURAS PENDIENTES");
		}
		return list;
	}

	public Ingreso obtenerPorIngresoId(Long ingresoId) {
		Ingreso ingreso = (Ingreso) ingresoDao.obtenerPorHql(
				"select distinct i from Ingreso i "
						+ "left join fetch i.detalleIngresos "
						+ "left join fetch i.cuotaIngresos " + "where i.id=?1",
				new Object[] { ingresoId }).get(0);

		return ingreso;
	}

}