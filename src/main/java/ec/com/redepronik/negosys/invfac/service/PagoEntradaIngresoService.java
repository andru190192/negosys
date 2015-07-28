package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.CuotaIngreso;
import ec.com.redepronik.negosys.invfac.entityAux.CobroReporte;

public interface PagoEntradaIngresoService {

	@Transactional
	public void cobro(List<CuotaIngreso> listaCuotaIngreso,
			List<CobroReporte> listaIngresos);

	@Transactional
	public void pagoLote(List<CobroReporte> listaIngresos, Date fechaPagoRapido);

}