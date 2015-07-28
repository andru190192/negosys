package ec.com.redepronik.negosys.invfac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.ParametroDao;
import ec.com.redepronik.negosys.invfac.entity.Parametro;

@Service
public class ParametroServiceImpl implements ParametroService {

	@Autowired
	private ParametroDao parametroDao;

	public void actualizar(Parametro parametro) {
		parametroDao.actualizar(parametro);
	}

	public Parametro obtener() {
		return parametroDao.obtenerPorId(Parametro.class, 1);
	}

}
