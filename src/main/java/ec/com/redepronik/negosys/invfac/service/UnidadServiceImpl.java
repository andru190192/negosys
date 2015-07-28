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

import ec.com.redepronik.negosys.invfac.dao.UnidadDao;
import ec.com.redepronik.negosys.invfac.entity.Unidad;

@Service
public class UnidadServiceImpl implements UnidadService {

	@Autowired
	private UnidadDao unidadDao;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(Unidad unidad) {
		unidad.setNombre(unidad.getNombre().toUpperCase());
		unidad.setAbreviatura(unidad.getAbreviatura().toUpperCase());
		Set<ConstraintViolation<Unidad>> violations = validator
				.validate(unidad);
		if (violations.size() > 0)
			for (ConstraintViolation<Unidad> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (unidadDao.comprobarIndicesMinuscula(Unidad.class, "nombre",
				unidad.getNombre(), String.valueOf(unidad.getId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "EL NOMBRE YA EXISTE");
		else if (unidadDao.comprobarIndices(Unidad.class, "abreviatura",
				unidad.getAbreviatura(), String.valueOf(unidad.getId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA ABREVIATURA YA EXISTE");
		else {
			unidadDao.actualizar(unidad);
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA UNIDAD SE GUARDO CON EXITO", "error", true);
		}
	}

	public void eliminar(Unidad unidad) {
		unidad.setActivo(unidad.getActivo() ? false : true);
		unidadDao.actualizar(unidad);
		if (unidad.getActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO, "SE ACTIVÓ LA UNIDAD: "
					+ unidad.getNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE DESACTIVÓ LA UNIDAD: " + unidad.getNombre());
	}

	public List<Unidad> insertar(Unidad unidad) {
		unidad.setNombre(unidad.getNombre().toUpperCase());
		unidad.setAbreviatura(unidad.getAbreviatura().toUpperCase());
		unidad.setActivo(true);
		unidad.setPordefecto(false);

		Set<ConstraintViolation<Unidad>> violations = validator
				.validate(unidad);
		if (violations.size() > 0)
			for (ConstraintViolation<Unidad> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (unidadDao.comprobarIndices(Unidad.class, "nombre",
				unidad.getNombre(), String.valueOf(unidad.getId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"EL NOMBRE DE LA UNIDAD YA EXISTE");
		else if (unidadDao.comprobarIndices(Unidad.class, "abreviatura",
				unidad.getAbreviatura(), String.valueOf(unidad.getId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA ABREVIATURA YA EXISTE");
		else {
			unidadDao.insertar(unidad);
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA UNIDAD SE GUARDO CON EXITO", "error", true);
		}
		return obtener(null);
	}

	public List<Unidad> obtener(Boolean activo) {
		return unidadDao.obtener(Unidad.class, "nombre", activo);
	}

	public Unidad obtenerPorUnidadId(Integer unidadId) {
		return unidadDao.obtenerPorId(Unidad.class, unidadId);
	}
}