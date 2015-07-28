package ec.com.redepronik.negosys.seguridad.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.seguridad.dao.RolDao;
import ec.com.redepronik.negosys.seguridad.entity.Rol;

@Service
public class RolServiceImpl implements RolService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private RolDao rolDao;

	public List<Rol> obtener() {
		return rolDao.obtener(Rol.class, "nombre", null);
	}

	public Rol obtenerPorNombre(String nombre) {
		return rolDao.obtenerPorAtributo(Rol.class, "nombre", nombre, null);
	}

	public Rol obtenerPorRolId(int rolId) {
		return rolDao.obtenerPorId(Rol.class, rolId);
	}

}