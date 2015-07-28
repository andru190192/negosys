package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.TarifaDao;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Tarifa;

@Service
public class TarifaServiceImpl implements TarifaService {

	@Autowired
	private TarifaDao tarifaDao;

	public List<Tarifa> obtener() {
		return tarifaDao
				.obtenerPorHql(
						"Select t from Tarifa t where t.fechaInicio<=?1 and t.fechaFin>=?1",
						new Object[] { new Date() });
	}

	public List<Tarifa> obtenerPorFechaProducto(Date fecha, long productoId) {
		return tarifaDao.obtenerPorHql(
				"select t from Tarifa t inner join t.productosTarifas pt "
						+ "inner join pt.producto p "
						+ "where t.fechaInicio<=?1 and "
						+ "t.fechaFin>=?1 and p.id=?2  ", new Object[] { fecha,
						productoId });
	}

	public Tarifa obtenerPorFechaProducto(Date fecha, long productoId,
			Impuesto impuesto) {
		List<Tarifa> list = (List<Tarifa>) tarifaDao
				.obtenerPorHql(
						"select t from Tarifa t inner join t.productosTarifas pt inner join pt.producto p "
								+ "where t.fechaInicio<=?1 and t.fechaFin>=?2 "
								+ "and p.id=?3 and t.impuesto like ?4",
						new Object[] { fecha, dateCompleto(fecha), productoId,
								impuesto });

		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public List<Tarifa> obtenerPorImpuesto(Impuesto impuesto) {
		Date hoy = new Date();
		return tarifaDao.obtenerPorHql("Select t from Tarifa t "
				+ "where t.impuesto like ?1 "
				+ "and t.fechaInicio<=?2 and t.fechaFin>=?3", new Object[] {
				impuesto, hoy, new Date(hoy.getTime() + 86399999) });
	}

	public Tarifa obtenerPorTarifaId(Short tarifaId) {
		return tarifaDao.obtenerPorHql("Select t from Tarifa t where t.id=?1",
				new Object[] { tarifaId }).get(0);
	}
}