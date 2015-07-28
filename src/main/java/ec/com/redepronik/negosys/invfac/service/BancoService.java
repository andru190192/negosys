package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Banco;

public interface BancoService {
	@Transactional
	public String actualizar(Banco banco);

	@Transactional
	public void eliminar(Banco banco);

	@Transactional
	public String insertar(Banco banco);

	@Transactional
	public List<Banco> obtener(Boolean activo);

	@Transactional
	public Banco obtenerPorBancoId(Integer bancoId);
}