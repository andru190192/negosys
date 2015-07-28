package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.LocalCajero;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

public interface LocalEmpleadoCargoService {

	@Transactional
	public void actualizar(LocalCajero localempleadocargo);

	@Transactional
	public void actualizarSimple(LocalCajero localempleadocargo);

	@Transactional
	public void eliminar(LocalCajero local);

	@Transactional
	public void eliminarCajero(LocalCajero encargado);

	@Transactional
	public void guardarCajeros(LocalCajero local,
			List<LocalCajero> listaLocalempleadocargoEmpleadoCargo);

	@Transactional
	public List<LocalCajero> insertar(LocalCajero local);

	@Transactional
	public void insertarCajero(LocalCajero local, EmpleadoCargo cajero,
			List<LocalCajero> listaLocalempleadocargoEmpleadoCargo);

	@Transactional
	public List<LocalCajero> obtenerPorCajero(String cajero);

	@Transactional
	public LocalCajero obtenerPorCajeroEstablecimiento(String establecimiento,
			Integer cajeroId);

	@Transactional
	public LocalCajero obtenerPorLocalempleadocargoId(Integer localId);

	@Transactional(readOnly = true)
	public List<LocalCajero> obtenerTodos();
}