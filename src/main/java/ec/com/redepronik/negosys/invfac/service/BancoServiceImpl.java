package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.BancoDao;
import ec.com.redepronik.negosys.invfac.entity.Banco;

@Service
public class BancoServiceImpl implements BancoService {

	@Autowired
	private BancoDao bancoDao;

	// PRUEBA DE TESTEO
	public String actualizar(Banco banco) {
		if (!bancoDao.comprobarIndices(Banco.class, "nombre",
				banco.getNombre(), String.valueOf(banco.getId()))) {
			bancoDao.actualizar(banco);
			return "SAVE";
		} else
			return "EL BANCO YA EXISTE";
	}

	public void eliminar(Banco banco) {
		if (banco.getActivo())
			banco.setActivo(false);
		else
			banco.setActivo(true);
		bancoDao.actualizar(banco);
	}

	public String insertar(Banco banco) {
		if (!bancoDao.comprobarIndices(Banco.class, "nombre",
				banco.getNombre(), String.valueOf(banco.getId()))) {
			banco.setActivo(true);
			bancoDao.insertar(banco);
			return "SAVE";
		} else
			return "EL BANCO YA EXISTE";
	}

	public List<Banco> obtener(Boolean activo) {
		return bancoDao.obtener(Banco.class, "nombre", activo);
	}

	public Banco obtenerPorBancoId(Integer bancoId) {
		return bancoDao.obtenerPorId(Banco.class, bancoId);
	}
}