package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.RetencionDao;
import ec.com.redepronik.negosys.invfac.entity.Retencion;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class RetencionServiceImpl implements RetencionService {

	@Autowired
	private RetencionDao retencionDao;

	@Autowired
	private EmpleadoService empleadoService;

	public String actualizar(Retencion retencion) {
		retencionDao.actualizar(retencion);
		return "SAVE";
	}

	public Retencion insertar(Retencion retencion) {
		retencion.setActivo(true);
		String cedula = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		retencion.setCajero(empleadoService
				.obtenerEmpleadoCargoPorCedulaAndCargo(cedula, 4));
		retencion.setPuntoEmision("0");
		retencion.setSecuencia("0");
		retencion.setFechaEmision(timestamp());
		retencionDao.insertar(retencion);
		return retencion;
	}

	public List<Retencion> obtener() {
		return retencionDao.obtenerPorHql("select distinct r from Retencion r "
				+ "inner join fetch r.detallesRetenciones dr", new Object[] {});
	}

	public Retencion obtenerPorId(Integer id) {
		List<Retencion> list = retencionDao.obtenerPorHql(
				"select distinct r from Retencion r "
						+ "inner join fetch r.detallesRetenciones dr "
						+ "where r.id=?1 order by r.fechaEmision",
				new Object[] { id });
		if (list == null || list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	public List<Retencion> obtener(String criterioBusquedaProveedor,
			String criterioBusquedaNumeroComprobante,
			Date criterioBusquedaFechaIngreso,
			String criterioBusquedaNumeroRetencion,
			Date criterioBusquedaFechaRetencion) {
		criterioBusquedaProveedor = criterioBusquedaProveedor.toUpperCase();
		List<Retencion> list = new ArrayList<Retencion>();
		if (criterioBusquedaProveedor.compareToIgnoreCase("") == 0
				&& criterioBusquedaNumeroComprobante.compareToIgnoreCase("") == 0
				&& criterioBusquedaFechaIngreso == null
				&& criterioBusquedaNumeroRetencion.compareToIgnoreCase("") == 0
				&& criterioBusquedaFechaRetencion == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else if (criterioBusquedaProveedor.length() >= 1
				&& criterioBusquedaProveedor.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR PROVEEDORES");
		else if (criterioBusquedaNumeroComprobante.length() >= 1
				&& criterioBusquedaNumeroComprobante.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES PARA LA BÚSQUEDA POR NÚMERO DE COMPROBANTES");
		else if (!criterioBusquedaNumeroComprobante.matches("[0-9]*"))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR NÚMERO DE COMPROBANTES");
		else if (!criterioBusquedaNumeroRetencion.matches("[0-9]*"))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR NÚMERO RETENCIÓN");
		else {
			if (criterioBusquedaProveedor.compareTo("") != 0)
				list = retencionDao
						.obtenerPorHql(
								"select distinct r from Retencion r "
										+ "inner join fetch r.detallesRetenciones dr "
										+ "inner join r.proveedor pd inner join pd.persona p "
										+ "where (p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "order by r.fechaEmision",
								new Object[] { "%" + criterioBusquedaProveedor
										+ "%" });
			else if (criterioBusquedaNumeroComprobante.compareTo("") != 0)
				list = retencionDao
						.obtenerPorHql(
								"select distinct r from Retencion r "
										+ "inner join fetch r.detallesRetenciones dr "
										+ "inner join r.proveedor pd inner join pd.persona p "
										+ "where dr.numeroComprobante like ?1 "
										+ "order by r.fechaEmision",
								new Object[] { "%"
										+ criterioBusquedaNumeroComprobante
										+ "%" });
			else if (criterioBusquedaFechaIngreso != null)
				list = retencionDao
						.obtenerPorHql(
								"select distinct r from Retencion r "
										+ "inner join fetch r.detallesRetenciones dr "
										+ "inner join r.proveedor pd inner join pd.persona p "
										+ "where dr.fechaEmision>=?1 and dr.fechaEmision<=?2 "
										+ "order by r.fechaEmision",
								new Object[] {
										criterioBusquedaFechaIngreso,
										new Date(criterioBusquedaFechaIngreso
												.getTime() + 86399999) });
			else if (criterioBusquedaNumeroRetencion.compareTo("") != 0)
				list = retencionDao
						.obtenerPorHql(
								"select distinct r from Retencion r "
										+ "inner join fetch r.detallesRetenciones dr "
										+ "inner join r.proveedor pd inner join pd.persona p "
										+ "where r.secuencia like ?1 "
										+ "order by r.fechaEmision",
								new Object[] { String.format(
										"%09d",
										Integer.parseInt(criterioBusquedaNumeroRetencion)) });
			else if (criterioBusquedaFechaRetencion != null)
				list = retencionDao
						.obtenerPorHql(
								"select distinct r from Retencion r "
										+ "inner join fetch r.detallesRetenciones dr "
										+ "inner join r.proveedor pd inner join pd.persona p "
										+ "where r.fechaEmision>=?1 and r.fechaEmision<=?2 "
										+ "order by r.fechaEmision",
								new Object[] {
										criterioBusquedaFechaRetencion,
										new Date(criterioBusquedaFechaRetencion
												.getTime() + 86399999) });
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return list;
	}
}