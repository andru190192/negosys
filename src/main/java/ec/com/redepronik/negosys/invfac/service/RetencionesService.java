package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entityAux.Retenciones;

public interface RetencionesService {

	@Transactional
	public List<Retenciones> obtener(Integer proveedorId, Date fechaInicio,
			Date fechaFin);
}
