package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.InteresVenta;

public interface InteresVentaService {
	@Transactional
	public void actualizar(InteresVenta interesVenta);

	@Transactional
	public void insertar(InteresVenta interesVenta);

	@Transactional
	public List<InteresVenta> obtener();

	@Transactional
	public InteresVenta obtenerPorFecha(Date fecha);

}
