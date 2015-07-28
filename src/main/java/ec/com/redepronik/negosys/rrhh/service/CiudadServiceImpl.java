package ec.com.redepronik.negosys.rrhh.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.rrhh.dao.CiudadDao;
import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;

@Service
public class CiudadServiceImpl implements CiudadService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CiudadDao ciudadDao;

	public String actualizar(Ciudad ciudad) {
		// Validar que no exista 2 ciudades = en la misma provincia
		// if (!ciudadDao.comprobarIndices(Ciudad.class, "nombre",
		// ciudad.getNombre(), String.valueOf(ciudad.getId()))) {
		ciudadDao.actualizar(ciudad);
		return "SAVE";
		// } else
		// return "EXISTE";
	}

	public void eliminar(Ciudad ciudad) {
		if (ciudad.getActivo())
			ciudad.setActivo(false);
		else
			ciudad.setActivo(true);
		ciudadDao.actualizar(ciudad);
	}

	public void insertar(Ciudad ciudad) {
		ciudad.setNombre(ciudad.getNombre().toUpperCase());
		ciudad.setActivo(true);
		ciudadDao.insertar(ciudad);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"INSERTO LA CIUDAD CON EXITO");
	}

	public List<Ciudad> obtener(Boolean activo) {
		return ciudadDao.obtener(Ciudad.class, "nombre", activo);
	}

	public Ciudad obtenerPorCiudadId(int ciudadId) {
		return ciudadDao.obtenerPorId(Ciudad.class, ciudadId);
	}

	public List<Ciudad> obtenerPorProvincia(Provincia provincia) {
		if (provincia != null && provincia.getId() != 0)
			return ciudadDao.obtenerPorHql(
					"select c from Ciudad c where c.provincia like ?1",
					new Object[] { provincia });
		else
			return null;
	}

	public List<Ciudad> obtenerPorProvincia(String provincia) {
		if (provincia != null)
			return ciudadDao
					.obtenerPorHql(
							"select c from Ciudad c inner join fetch c.provincia p where p.nombre like ?1 order by c.nombre",
							new Object[] { "%" + provincia + "%" });
		else
			return null;
	}
}