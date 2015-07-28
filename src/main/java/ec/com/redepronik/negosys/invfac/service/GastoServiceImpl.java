package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateFin;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateInicio;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.EgresoCajaDao;
import ec.com.redepronik.negosys.invfac.dao.GastoDao;
import ec.com.redepronik.negosys.invfac.entity.Gastos;
import ec.com.redepronik.negosys.invfac.entityAux.EgresoCaja;
import ec.com.redepronik.negosys.invfac.entityAux.GastosAux;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class GastoServiceImpl implements GastoService {

	@Autowired
	private GastoDao gastoDao;

	@Autowired
	private EgresoCajaDao egresoCajaDao;

	@Autowired
	private EmpleadoService empleadoService;

	public void actualizar(Gastos gasto) {
		gastoDao.actualizar(gasto);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"ACTUALIZO " + gasto.getDescripcion());
	}

	public GastosAux converirGastosGastosAux(Gastos g) {
		GastosAux ga = new GastosAux();
		ga.setGastos(g);
		ga.setIvaPrecioUnitario(redondear(g.getIva() ? iva(
				g.getPrecioUnitarioCosto(), 12) : newBigDecimal()));
		ga.setTotalPrecioUnitario(redondearTotales(g.getPrecioUnitarioCosto()
				.add(ga.getIvaPrecioUnitario())));
		ga.setSubtotalFacturado(redondearTotales(multiplicar(
				g.getPrecioUnitarioCosto(), g.getCantidad())));
		ga.setIva(redondearTotales(g.getIva() ? iva(ga.getSubtotalFacturado(),
				12) : newBigDecimal()));
		ga.setTotalFacturado(redondearTotales(ga.getSubtotalFacturado().add(
				ga.getIva())));
		ga.setValorRetencion(redondearTotales(multiplicarDivide(
				ga.getSubtotalFacturado(), g.getPorcentajeRetencionFuente())));
		ga.setValorRetencionIva(redondearTotales(multiplicarDivide(ga.getIva(),
				g.getPorcentajeRetencionIva())));
		ga.setTotalPagar(redondearTotales(ga.getTotalFacturado()
				.subtract(ga.getValorRetencion())
				.subtract(ga.getValorRetencionIva())));
		return ga;
	}

	public void eliminar(Gastos gasto) {
		gasto.setActivo(gasto.getActivo() ? false : true);
		gastoDao.actualizar(gasto);

		if (gasto.getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO, "SE ACTIVÓ EL GASTO: "
					+ gasto.getDescripcion());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ EL GASTO: " + gasto.getDescripcion());
	}

	public void insertar(Gastos gasto) {
		String cedula = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		gasto.setCajero(empleadoService.obtenerEmpleadoCargoPorCedulaAndCargo(
				cedula, 4));
		gasto.setActivo(true);
		if (gasto.getPorcentajeRetencionFuente() == null)
			gasto.setPorcentajeRetencionFuente(newBigDecimal());
		if (gasto.getPorcentajeRetencionIva() == null)
			gasto.setPorcentajeRetencionIva(newBigDecimal());
		gastoDao.insertar(gasto);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"INSERTÓ " + gasto.getDescripcion());
	}

	public List<Gastos> obtener(Date fechaInicio, Date fechaFin) {
		List<Gastos> list = new ArrayList<Gastos>();
		if (fechaInicio == null && fechaFin == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			list = gastoDao
					.obtenerPorHql(
							"Select g from Gastos g where g.activo=true and g.fecha>=?1 and g.fecha<=?2 order by g.fecha",
							new Object[] { fechaInicio, dateCompleto(fechaFin) });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public List<Gastos> obtener(Date criterioBusquedaFecha,
			String criterioBusquedaDescripcion, String criterioBusquedaProveedor) {
		List<Gastos> list = new ArrayList<Gastos>();
		if (criterioBusquedaFecha == null
				&& criterioBusquedaDescripcion.compareToIgnoreCase("") == 0
				&& criterioBusquedaProveedor.compareToIgnoreCase("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			criterioBusquedaDescripcion = criterioBusquedaDescripcion
					.toUpperCase();
			criterioBusquedaProveedor = criterioBusquedaProveedor.toUpperCase();
			if (criterioBusquedaFecha != null)
				list = gastoDao
						.obtenerPorHql(
								"Select g from Gastos g where g.fecha>=?1 and g.fecha<=?2 order by g.fecha",
								new Object[] { criterioBusquedaFecha,
										dateCompleto(criterioBusquedaFecha) });
			else if (criterioBusquedaDescripcion.compareToIgnoreCase("") != 0)
				list = gastoDao
						.obtenerPorHql(
								"Select g from Gastos g where g.descripcion like ?1 order by g.fecha",
								new Object[] { "%"
										+ criterioBusquedaDescripcion + "%" });
			else if (criterioBusquedaProveedor.compareToIgnoreCase("") != 0)
				list = gastoDao.obtenerPorHql("Select g from Gastos g "
						+ "inner join g.proveedor pr "
						+ "where pr.nombrecomercial like ?1 order by g.fecha",
						new Object[] { "%" + criterioBusquedaProveedor + "%" });

			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}

	public List<GastosAux> obtenerGastos(Date fechaInicio, Date fechaFin) {
		List<GastosAux> list = new ArrayList<GastosAux>();
		for (Gastos g : obtener(fechaInicio, fechaFin))
			list.add(converirGastosGastosAux(g));
		return list;
	}

	public List<GastosAux> obtenerGastos(Date criterioBusquedaFecha,
			String criterioBusquedaDescripcion, String criterioBusquedaProveedor) {
		List<GastosAux> list = new ArrayList<GastosAux>();
		for (Gastos g : obtener(criterioBusquedaFecha,
				criterioBusquedaDescripcion, criterioBusquedaProveedor))
			list.add(converirGastosGastosAux(g));
		return list;
	}

	public List<String> obtenerListaDescripcionesAutoComplete(
			String criterioProveedorBusqueda) {
		List<String> list = new ArrayList<String>();
		List<Gastos> lista = obtener(null, criterioProveedorBusqueda, "");
		for (Gastos gastos : lista)
			list.add(gastos.getDescripcion());
		return list;
	}

	public Gastos obtenerPorGastosId(Integer gastoId) {
		return gastoDao.obtenerPorId(Gastos.class, gastoId);
	}

	public List<EgresoCaja> reporteEgreso(Date fechaInicio, Date fechaFin) {
		if (fechaInicio == null && fechaFin == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA FECHA");
		else
			return egresoCajaDao
					.obtenerPorHql(
							"select new EgresoCaja(g.gastoid, g.fecha, g.descripcion, g.descripcion, pr.nombrecomercial, 0, 0) "
									+ "from Gastos g "
									+ "left join g.proveedor pr "
									+ "where g.activo=true and g.fecha>=?1 and g.fecha<=?2 "
									+ "order by g.fecha;",
							new Object[] { dateInicio(fechaInicio),
									dateFin(fechaFin) });
		return new ArrayList<EgresoCaja>();
	}

	public List<EgresoCaja> reporteEgresoCaja(int cajero, Date fecha) {
		if (fecha == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA FECHA");
		else
			return egresoCajaDao
					.obtenerPorHql(
							"select new EgresoCaja(g.gastoid, g.fecha, g.descripcion, g.descripcion, pr.nombrecomercial, 0, 0) "
									+ "from Gastos g "
									+ "left join g.proveedor pr "
									+ "inner join g.empleadocargo ec "
									+ "where g.activo=true and g.tipopagoid=1 and ec.empleadocargoid=?1 "
									+ "and g.fecha>=?2 and g.fecha<=?3 order by g.fecha;",
							new Object[] { cajero, dateInicio(fecha),
									dateFin(fecha) });
		return new ArrayList<EgresoCaja>();
	}
}