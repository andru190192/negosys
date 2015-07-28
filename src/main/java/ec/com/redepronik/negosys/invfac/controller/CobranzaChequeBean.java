package ec.com.redepronik.negosys.invfac.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.PagoCredito;
import ec.com.redepronik.negosys.invfac.entity.PagoEntrada;
import ec.com.redepronik.negosys.invfac.entityAux.ChequeReporte;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.invfac.service.PagoCreditoService;
import ec.com.redepronik.negosys.invfac.service.PagoEntradaFacturaService;

@Controller
@Scope("session")
public class CobranzaChequeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<ChequeReporte> listaCheques;
	private ChequeReporte cheque;
	private Date fechaCobro;
	private Date fechaActual;

	@Autowired
	private FacturaService egresoService;

	@Autowired
	private PagoEntradaFacturaService pagoEntradaService;

	@Autowired
	private PagoCreditoService pagoCreditoService;

	public CobranzaChequeBean() {
		listaCheques = new ArrayList<ChequeReporte>();
		fechaActual = new Date();
	}

	public void cobroCheque(ActionEvent actionEvent) {
		String[] chequeId = new String[2];
		// chequeId = cheque.getId().split("-");
		if (cheque.getTabla().compareTo("PE") == 0) {
			PagoEntrada pagoEntrada = pagoEntradaService
					.obtenerPorPagoEntradaId(Integer.parseInt(chequeId[0]));
			pagoEntrada.setFechaPago(new Timestamp(fechaCobro.getTime()));
			pagoEntrada.setPagado(true);
			pagoEntradaService.actualizar(pagoEntrada);
		} else if (cheque.getTabla().compareTo("PC") == 0) {
			PagoCredito pagoCredito = pagoCreditoService
					.obtenerPorPagoCreditoId(Integer.parseInt(chequeId[0]));
			pagoCredito.setFechaPago(new Timestamp(fechaCobro.getTime()));
			pagoCredito.setPagado(true);
			pagoCreditoService.actualizar(pagoCredito);
		}
	}

	public ChequeReporte getCheque() {
		return cheque;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public Date getFechaCobro() {
		return fechaCobro;
	}

	public List<ChequeReporte> getListaCheques() {
		return listaCheques;
	}

	public void setCheque(ChequeReporte cheque) {
		this.cheque = cheque;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public void setFechaCobro(Date fechaCobro) {
		this.fechaCobro = fechaCobro;
	}

	public void setListaCheques(List<ChequeReporte> listaCheques) {
		this.listaCheques = listaCheques;
	}
}
