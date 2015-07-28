package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.GrupoDao;
import ec.com.redepronik.negosys.invfac.entity.Grupo;

@Service
public class GrupoServiceImpl implements GrupoService {

	@Autowired
	private GrupoDao grupoDao;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public List<Grupo> actualizar(Grupo grupo) {
		grupo.setNombre(grupo.getNombre().toUpperCase());
		Set<ConstraintViolation<Grupo>> violations = validator.validate(grupo);
		if (violations.size() > 0)
			for (ConstraintViolation<Grupo> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (grupoDao.comprobarIndicesMinuscula(Grupo.class, "nombre",
				grupo.getNombre(), String.valueOf(grupo.getId())))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL GRUPO: " + grupo.getNombre() + " YA EXISTE", "error",
					false);
		else {
			grupoDao.actualizar(grupo);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "MODIFICO EL GRUPO: "
					+ grupo.getNombre(), "error", true);
		}
		return obtener();
	}

	public void eliminar(Grupo grupo) {
		grupo.setActivo(grupo.getActivo() ? false : true);
		grupoDao.actualizar(grupo);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				(grupo.getActivo() ? "ACTIVO " : "DESACTIVO ") + "EL GRUPO: "
						+ grupo.getNombre());
	}

	public List<Grupo> insertar(Grupo grupo) {
		grupo.setNombre(grupo.getNombre().toUpperCase());
		grupo.setActivo(true);
		grupo.setContador(0);
		Set<ConstraintViolation<Grupo>> violations = validator.validate(grupo);
		if (violations.size() > 0)
			for (ConstraintViolation<Grupo> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (grupoDao.comprobarIndices(Grupo.class, "nombre",
				grupo.getNombre(), String.valueOf(grupo.getId())))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL GRUPO: " + grupo.getNombre() + " YA EXISTE", "error",
					true);
		else {
			grupoDao.insertar(grupo);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTO EL GRUPO: "
					+ grupo.getNombre(), "error", true);
		}
		return obtener();
	}

	public List<Grupo> obtener() {
		List<Grupo> lista = grupoDao.obtenerPorHql(
				"select g from Grupo g where g.activo=true order by g.nombre",
				new Object[] {});
		return lista;
	}

	public Grupo obtenerPorGrupoId(Integer grupoId) {
		Grupo grupo = grupoDao
				.obtenerPorHql(
						"select g from Grupo g where g.activo=true and g.id=?1",
						new Object[] { grupoId }).get(0);
		return grupo;
	}

	public List<Grupo> reporteObtenerPorProductos() {
		List<Grupo> lista = grupoDao
				.obtenerPorHql(
						"select distinct g from Grupo g "
								+ "left join fetch g.productos p "
								+ "where g.activo=true and p.activo=true order by g.nombre",
						new Object[] {});
		return lista;
	}
}