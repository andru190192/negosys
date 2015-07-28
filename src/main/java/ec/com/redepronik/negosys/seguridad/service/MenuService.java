package ec.com.redepronik.negosys.seguridad.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.seguridad.entity.Menu;

public interface MenuService {
	@Transactional
	public String actualizar(Menu menu);

	@Transactional
	public void eliminar(int menuId);

	@Transactional
	public String insertar(Menu menu);

	@Transactional
	public List<Menu> obtener();

	@Transactional
	public List<Menu> obtenerPorUsuario(String cedulaRuc);
}