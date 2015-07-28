package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.MoraDao;
import ec.com.redepronik.negosys.invfac.entity.Mora;

@Service
public class MoraServiceImpl implements MoraService {

	@Autowired
	private MoraDao moraDao;

	public void actualizar(Mora mora) {
		moraDao.actualizar(mora);
	}

	public void insertar(Mora mora) {
		moraDao.insertar(mora);
	}

	public List<Mora> obtener() {
		return moraDao.obtener(Mora.class, "fechainicio", null);
	}

	public Mora obtenerPorFecha(Date fecha) {
		List<Mora> mora = null;
		mora = moraDao
				.obtenerPorHql(
						"select m from Mora m where m.fechaInicio<=?1 and m.fechaFin>=?1",
						new Object[] { fecha });
		if (mora != null)
			if (mora.size() != 0)
				return mora.get(0);

		return null;
	}

}
