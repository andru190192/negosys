package ec.com.redepronik.negosys.rrhh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.rrhh.entity.Cargo;

public interface CargoService {
	@Transactional
	public String actualizar(Cargo cargo);

	@Transactional
	public void eliminar(Cargo cargo);

	@Transactional
	public String insertar(Cargo cargo);

	@Transactional
	public List<Cargo> obtener(Boolean activo);

	@Transactional
	public Cargo obtenerPorCargoId(int cargoId);
}