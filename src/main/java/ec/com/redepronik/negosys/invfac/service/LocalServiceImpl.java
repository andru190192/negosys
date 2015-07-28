package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.LocalDao;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.LocalCajero;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class LocalServiceImpl implements LocalService {

	@Autowired
	private LocalDao localDao;

	@Autowired
	private EmpleadoService empleadoService;

	private ValidatorFactory factory = Validation
			.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public void actualizar(Local local) {
		Set<ConstraintViolation<Local>> violations = validator.validate(local);
		if (violations.size() > 0)
			for (ConstraintViolation<Local> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (localDao.comprobarIndices(Local.class, "nombre",
				local.getNombre(), String.valueOf(local.getId())))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL LOCAL: " + local.getNombre() + " YA EXISTE", "error",
					false);
		else {
			localDao.actualizar(local);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "MODIFICO EL LOCAL: "
					+ local.getNombre(), "error", true);
		}
	}

	public void eliminar(Local local) {
		local.setActivo(local.getActivo() ? false : true);
		localDao.actualizar(local);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				(local.getActivo() ? "ACTIVO " : "DESACTIVO ") + "EL GRUPO: "
						+ local.getNombre());
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

	public void guardarCajeros(Local local,
			List<LocalCajero> listaLocalEmpleadoCargo) {
		if (listaLocalEmpleadoCargo.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE POR LO MENOS UN CAJERO", "error", true);
		else {
			if (local.getLocalCajero() == null)
				local.setLocalCajero(new ArrayList<LocalCajero>());

			local.setLocalCajero(listaLocalEmpleadoCargo);
			actualizar(local);
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE ACTUALIZÓ CON EXITO", "error", false);
		}
	}

	public List<Local> insertar(Local local) {
		local.setActivo(true);
		local.setNombre(local.getNombre().toUpperCase());

		Set<ConstraintViolation<Local>> violations = validator.validate(local);
		if (violations.size() > 0)
			for (ConstraintViolation<Local> cv : violations)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (localDao.comprobarIndices(Local.class, "nombre",
				local.getNombre(), String.valueOf(local.getId())))
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL LOCAL " + local.getNombre() + " YA EXISTE");
		else if (localDao.comprobarIndices(Local.class,
				"codigoEstablecimiento", local.getCodigoEstablecimiento(),
				String.valueOf(local.getId())))
			presentaMensaje(
					FacesMessage.SEVERITY_ERROR,
					"EL CÓDIGO DEL ESTABLECIMIENTO "
							+ local.getCodigoEstablecimiento() + " YA EXISTE");
		else {
			localDao.insertar(local);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTO EL LOCAL: "
					+ local.getNombre(), "error", true);
		}
		return obtenerTodos();
	}

	public void insertarCajero(Local local, EmpleadoCargo cajero,
			List<LocalCajero> listaLocalEmpleadoCargo) {
		boolean bd = false;
		if (cajero.getId() == 0 || cajero.getId() == null)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN CAJERO");
		else {
			for (LocalCajero bec : listaLocalEmpleadoCargo)
				if (bec.getEmpleadoCargo().getId() == cajero.getId()) {
					bd = true;
					break;
				}

			if (bd)
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"YA EXISTE ESTE CAJERO");
			else {
				LocalCajero lec = new LocalCajero();
				lec.setLocal(local);
				lec.setEmpleadoCargo(empleadoService
						.obtenerPorEmpleadoCargoId(cajero.getId()));
				lec.setActivo(true);
				listaLocalEmpleadoCargo.add(lec);
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"CAJERO ASOCIADO CORRECTAMENTE");
			}
			cajero = new EmpleadoCargo();
		}
	}

	public List<Local> obtener(Boolean activo) {
		return localDao.obtener(Local.class, "nombre", activo);
	}

	public List<Local> obtenerActivosPorLocalCajero() {
		List<Local> list = localDao
				.obtenerPorHql(
						"select distinct l from Local l left join fetch l.localBodeguero where l.activo=true "
								+ "order by l.nombre", new Object[] {});
		return list;
	}

	public Local obtenerMatriz() {
		Local local = localDao.obtenerPorHql(
				"select distinct l from Local l where l.matriz=true",
				new Object[] {}).get(0);
		return local;
	}

	public List<Local> obtenerPorCedulaAndCargo(String cedula, int cargo) {
		String cad = cargo == 4 ? "inner join l.localCajero lec "
				: "inner join l.localBodeguero lec ";
		List<Local> list = localDao
				.obtenerPorHql(
						"select l from Local l "
								+ cad
								+ "inner join lec.empleadoCargo ec "
								+ "inner join ec.cargo c "
								+ "inner join ec.empleado e "
								+ "inner join e.persona p "
								+ "where l.activo=true and lec.activo=true and ec.actual=true and c.activo=true and e.activo=true and p.activo=true and p.cedula=?1 and c.id=?2",
						new Object[] { cedula, cargo });
		return list;
	}

	public Local obtenerPorEstablecimiento(String establecimiento) {
		Local local = localDao.obtenerPorHql(
				"select l from Local l "
						+ "where l.codigoEstablecimiento = ?1",
				new Object[] { establecimiento }).get(0);
		return local;
	}

	public Local obtenerPorLocalId(Integer localId) {
		Local local = localDao.obtenerPorHql(
				"select distinct l from Local l "
						+ "where l.id=?1 and l.activo=true",
				new Object[] { localId }).get(0);
		return local;
	}

	public List<Local> obtenerTodos() {
		List<Local> list = localDao.obtenerPorHql(
				"select distinct l from Local l left join fetch l.localCajero "
						+ "order by l.nombre", new Object[] {});
		return list;
	}

}