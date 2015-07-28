package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.LocalCajero;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

public interface LocalService {
	@Transactional
	public void actualizar(Local local);

	@Transactional
	public void eliminar(Local local);

	@Transactional
	public void eliminarCajero(LocalCajero encargado);

	@Transactional
	public void guardarCajeros(Local local,
			List<LocalCajero> listaLocalEmpleadoCargo);

	@Transactional
	public List<Local> insertar(Local local);

	@Transactional
	public void insertarCajero(Local local, EmpleadoCargo cajero,
			List<LocalCajero> listaLocalEmpleadoCargo);

	@Transactional
	public List<Local> obtener(Boolean activo);

	@Transactional
	public List<Local> obtenerActivosPorLocalCajero();

	@Transactional
	public Local obtenerMatriz();

	@Transactional
	public List<Local> obtenerPorCedulaAndCargo(String cedula, int cargo);

	@Transactional
	public Local obtenerPorEstablecimiento(String establecimiento);

	@Transactional
	public Local obtenerPorLocalId(Integer localId);

	@Transactional(readOnly = true)
	public List<Local> obtenerTodos();
}