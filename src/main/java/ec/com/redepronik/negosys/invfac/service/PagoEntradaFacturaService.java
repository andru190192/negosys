package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.PagoEntrada;
import ec.com.redepronik.negosys.invfac.entityAux.CobroReporte;

public interface PagoEntradaFacturaService {
	@Transactional
	public String actualizar(PagoEntrada pagoEntrada);

	@Transactional
	public void cobro(List<PagoEntrada> listaPagoEntrada,
			List<CobroReporte> listaEgresos,
			List<CobroReporte> listaEgresosSeleccionados);

	@Transactional
	public PagoEntrada obtenerPorPagoEntradaId(Integer pagoEntradaId);

	@Transactional
	public void pagoLote(List<CobroReporte> listaEgresos,
			List<CobroReporte> listaEgresosSeleccionados, Date fechaPagoRapido);
}