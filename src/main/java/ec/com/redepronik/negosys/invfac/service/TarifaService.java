package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Tarifa;

public interface TarifaService {
	@Transactional
	public List<Tarifa> obtener();

	@Transactional
	public List<Tarifa> obtenerPorFechaProducto(Date fecha, long productoId);

	@Transactional
	public Tarifa obtenerPorFechaProducto(Date fecha, long productoId,
			Impuesto impuesto);

	@Transactional
	public List<Tarifa> obtenerPorImpuesto(Impuesto impuesto);

	@Transactional
	public Tarifa obtenerPorTarifaId(Short tarifaId);
}