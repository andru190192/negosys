package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.RetencionesDao;
import ec.com.redepronik.negosys.invfac.entityAux.Retenciones;

@Service
public class RetencionesServiceImpl implements RetencionesService {

	@Autowired
	RetencionesDao retencionesDao;

	public List<Retenciones> obtener(Integer proveedorId, Date fechaInicio,
			Date fechaFin) {
		List<Retenciones> lista = null;
		if (proveedorId == 0)
			lista = retencionesDao
					.obtenerPorHql(
							"select new Retenciones( "
									+ "dr.id, "
									+ "r.establecimiento||'-'||r.puntoEmision||'-'||r.secuencia, "
									+ "r.fechaEmision, "
									+ "p.nombreComercial, "
									+ "substring(dr.numeroComprobante,1,3)||'-'||substring(dr.numeroComprobante,4,3)||'-'||substring(dr.numeroComprobante,7,9), "
									+ "dr.fechaEmision, "
									+ "dr.impuestoRetencion, "
									+ "dr.tarifa, "
									+ "round(dr.baseImponible,2), "
									+ "round(dr.porcentajeRetencion,2), "
									+ "round(dr.baseImponible*dr.porcentajeRetencion/100,2)) "
									+ "from Retencion r "
									+ "inner join r.proveedor p "
									+ "inner join r.detallesRetenciones dr "
									+ "where r.activo=true "
									+ "and r.fechaEmision>=?1 and r.fechaEmision<=?2 "
									+ "order by r.fechaEmision", new Object[] {
									fechaInicio, dateCompleto(fechaFin) });
		else
			lista = retencionesDao
					.obtenerPorHql(
							"select new Retenciones("
									+ "dr.id, "
									+ "r.establecimiento||'-'||r.puntoEmision||'-'||r.secuencia, "
									+ "r.fechaEmision, "
									+ "p.nombreComercial, "
									+ "substring(dr.numeroComprobante,1,3)||'-'||substring(dr.numeroComprobante,4,3)||'-'||substring(dr.numeroComprobante,7,9), "
									+ "dr.fechaEmision, "
									+ "dr.impuestoRetencion, "
									+ "dr.tarifa, "
									+ "round(dr.baseImponible,2), "
									+ "round(dr.porcentajeRetencion,2), "
									+ "round(dr.baseImponible * dr.porcentajeRetencion / 100,2)) "
									+ "from Retencion r "
									+ "inner join r.proveedor p "
									+ "inner join r.detallesRetenciones dr "
									+ "where r.activo=true "
									+ "and p.id=?1 "
									+ "and r.fechaEmision>=?2 and r.fechaEmision<=?3 "
									+ "order by r.fechaEmision", new Object[] {
									proveedorId, fechaInicio,
									dateCompleto(fechaFin) });
		return lista;
	}
}