package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entityAux.VolumenVentaFactura;

public interface VolumenVentaFacturaService {

	@Transactional
	public List<VolumenVentaFactura> obtenerVolumenventaFacturaPorConsumidorFinal(
			int consumidorFinal, Date fechaInicio, Date fechaFin);

	@Transactional
	public List<VolumenVentaFactura> obtenerPorFechasAndVendedorAndEstadoDocumento(
			Integer vendedorId, Integer estadoDocumento, Date fechaInicio,
			Date fechaFin);
}
