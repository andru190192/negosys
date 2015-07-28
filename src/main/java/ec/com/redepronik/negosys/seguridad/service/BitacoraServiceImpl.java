package ec.com.redepronik.negosys.seguridad.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.seguridad.dao.BitacoraDao;
import ec.com.redepronik.negosys.seguridad.entity.Bitacora;

@Service
public class BitacoraServiceImpl implements BitacoraService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BitacoraDao bitacoraDao;

	public List<Bitacora> obtener(Integer personaId, Date fechaInicio) {
		List<Bitacora> lista = null;

		if (personaId == null || personaId == 0)
			lista = bitacoraDao.obtenerPorHql("select b from Bitacora b "
					+ "inner join b.persona p "
					+ "where b.fecha>=?1 and b.fecha<=?2 "
					+ "order by b.fecha desc", new Object[] { fechaInicio,
					new Date(fechaInicio.getTime() + 86399999) });
		else
			lista = bitacoraDao.obtenerPorHql("select b from Bitacora b "
					+ "inner join b.persona p "
					+ "where p.id=?1 and b.fecha>=?2 and b.fecha<=?3 "
					+ "order by b.fecha desc", new Object[] { personaId,
					fechaInicio, new Date(fechaInicio.getTime() + 86399999) });

		return lista;
	}
}