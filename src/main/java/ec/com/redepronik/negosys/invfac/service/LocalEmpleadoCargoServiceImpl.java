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

import ec.com.redepronik.negosys.invfac.dao.LocalEmpleadoCargoDao;
import ec.com.redepronik.negosys.invfac.entity.LocalCajero;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class LocalEmpleadoCargoServiceImpl implements LocalEmpleadoCargoService {

	@Autowired
	private LocalEmpleadoCargoDao localEmpleadoCargoDao;

	@Autowired
	private EmpleadoService empleadoService;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(LocalCajero localempleadocargo) {
		Set<ConstraintViolation<LocalCajero>> violations = validator
				.validate(localempleadocargo);
		if (violations.size() > 0)
			for (ConstraintViolation<LocalCajero> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else {
			localEmpleadoCargoDao.actualizar(localempleadocargo);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "MODIFICO EL CAJERO: "
					+ localempleadocargo.getEmpleadoCargo().getEmpleado()
							.getPersona().getApellido()
					+ " "
					+ localempleadocargo.getEmpleadoCargo().getEmpleado()
							.getPersona().getNombre(), "error", true);
		}
	}

	public void actualizarSimple(LocalCajero localempleadocargo) {
		localEmpleadoCargoDao.actualizar(localempleadocargo);
	}

	public void eliminar(LocalCajero local) {
		local.setActivo(local.getActivo() ? false : true);
		localEmpleadoCargoDao.actualizar(local);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				(local.getActivo() ? "ACTIVO " : "DESACTIVO ")
						+ "EL CAJERO: "
						+ local.getEmpleadoCargo().getEmpleado().getPersona()
								.getApellido()
						+ " "
						+ local.getEmpleadoCargo().getEmpleado().getPersona()
								.getNombre());
	}

	public void eliminarCajero(LocalCajero encargado) {
		encargado.setActivo(encargado.getActivo() ? false : true);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				(encargado.getActivo() ? "ACTIVO " : "DESACTIVO ")
						+ "EL CAJERO: "
						+ encargado.getEmpleadoCargo().getEmpleado()
								.getPersona().getApellido()
						+ " "
						+ encargado.getEmpleadoCargo().getEmpleado()
								.getPersona().getNombre());
	}

	public void guardarCajeros(LocalCajero local,
			List<LocalCajero> listaLocalempleadocargoEmpleadoCargo) {
		if (listaLocalempleadocargoEmpleadoCargo.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE POR LO MENOS UN CAJERO", "error", true);
		else {
			actualizar(local);
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTUALIZÓ CON EXITO", "error", false);
		}
	}

	public List<LocalCajero> insertar(LocalCajero local) {
		local.setActivo(true);

		Set<ConstraintViolation<LocalCajero>> violations = validator
				.validate(local);
		if (violations.size() > 0)
			for (ConstraintViolation<LocalCajero> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else {
			localEmpleadoCargoDao.insertar(local);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTO EL CAJERO: "
					+ local.getEmpleadoCargo().getEmpleado().getPersona()
							.getApellido()
					+ " "
					+ local.getEmpleadoCargo().getEmpleado().getPersona()
							.getNombre(), "error", true);
		}
		return obtenerTodos();
	}

	public void insertarCajero(LocalCajero local, EmpleadoCargo cajero,
			List<LocalCajero> listaLocalempleadocargoEmpleadoCargo) {
		boolean bd = false;
		if (cajero.getId() == 0 || cajero.getId() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN CAJERO");
		else {
			for (LocalCajero bec : listaLocalempleadocargoEmpleadoCargo)
				if (bec.getEmpleadoCargo().getId() == cajero.getId()) {
					bd = true;
					break;
				}

			if (bd)
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"YA EXISTE ESTE CAJERO");
			else {
				LocalCajero lec = new LocalCajero();
				lec.setEmpleadoCargo(empleadoService
						.obtenerPorEmpleadoCargoId(cajero.getId()));
				lec.setActivo(true);
				listaLocalempleadocargoEmpleadoCargo.add(lec);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"CAJERO ASOCIADO CORRECTAMENTE");
			}
			cajero = new EmpleadoCargo();
		}
	}

	public List<LocalCajero> obtenerPorCajero(String cajero) {
		List<LocalCajero> list = localEmpleadoCargoDao
				.obtenerPorHql(
						"select l from Localempleadocargo l "
								+ "inner join l.bodegalocals bl "
								+ "inner join bl.bodega b "
								+ "inner join l.localempleadocargos lec "
								+ "inner join lec.empleadocargo ec "
								+ "inner join ec.cargo c "
								+ "inner join ec.empleado e "
								+ "inner join e.persona p "
								+ "where b.activo=true and l.activo=true and lec.activo=true and ec.actual=true and c.activo=true and e.activo=true and p.activo=true and c.cargoid=4 and p.cedula=?1",
						new Object[] { cajero });
		return list;
	}

	public LocalCajero obtenerPorCajeroEstablecimiento(String establecimiento,
			Integer cajeroId) {
		LocalCajero list = localEmpleadoCargoDao
				.obtenerPorHql(
						"select lec from Localempleadocargo lec "
								+ "inner join lec.local l "
								+ "inner join lec.empleadocargo ec "
								+ "where l.activo=true and lec.activo=true and "
								+ "l.codigoestablecimiento=?1 and ec.empleadocargoid=?2",
						new Object[] { establecimiento, cajeroId }).get(0);
		return list;
	}

	public LocalCajero obtenerPorLocalempleadocargoId(Integer localId) {
		LocalCajero local = localEmpleadoCargoDao.obtenerPorHql(
				"select distinct l from Localempleadocargo l "
						+ "inner join fetch l.bodegalocals "
						+ "where l.localid=?1 and l.activo=true",
				new Object[] { localId }).get(0);
		return local;
	}

	public List<LocalCajero> obtenerTodos() {
		List<LocalCajero> list = localEmpleadoCargoDao
				.obtenerPorHql(
						"select distinct l from Localempleadocargo l left join fetch l.localempleadocargos "
								+ "left join fetch l.bodegalocals "
								+ "order by l.nombre", new Object[] {});
		return list;
	}
}