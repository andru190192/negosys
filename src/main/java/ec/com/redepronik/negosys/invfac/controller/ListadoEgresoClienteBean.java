package ec.com.redepronik.negosys.invfac.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleCredito;
import ec.com.redepronik.negosys.invfac.entity.Entrada;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadesCreditoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadesEntradaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.NumeroCuotasReporte;
import ec.com.redepronik.negosys.invfac.service.CreditoService;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;

@Controller
@Scope("session")
public class ListadoEgresoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacturaService egresoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CreditoService creditoService;

	// //busqueda de pedido lazyData
	private List<Factura> listaEgresos;
	private String criterioBusquedaCodigo;
	private Integer tipoDocumentoId;
	// //

	private List<FacturaReporte> listaEgresosDetalle;

	private Factura egreso;

	private Entrada entrada;
	private DetalleCredito detallesCredito;
	private List<Persona> garantes;
	private Persona garante;

	private CantidadesEntradaReporte cantidadesEntradaReporte;
	private CantidadesCreditoReporte cantidadesCreditoReporte;

	private Date fechaActual;

	private NumeroCuotasReporte numeroCuotas;

	public ListadoEgresoClienteBean() {
		egreso = new Factura();

		entrada = new Entrada();
		detallesCredito = new DetalleCredito();
		garante = new Persona();

		cantidadesEntradaReporte = new CantidadesEntradaReporte();
		cantidadesCreditoReporte = new CantidadesCreditoReporte();

		fechaActual = new Date();

	}

	public void calcularCuota() {
		if (detallesCredito.getId() != null)
			cantidadesCreditoReporte = creditoService.calcularCuota(
					detallesCredito, cantidadesCreditoReporte,
					egreso.getFechaInicio(), fechaActual);
	}

	public void calcularEntrada() {
		if (entrada.getId() != null)
			cantidadesEntradaReporte = egresoService.calcularEntrada(entrada,
					cantidadesEntradaReporte, egreso.getFechaInicio(),
					fechaActual);
	}

	public void calcularNumeroCuotasEntradasCreditos() {
		// if (numeroCuotas == null) {
		// numeroCuotas = new NumeroCuotasReporte();
		// numeroCuotas = egresoService.calcularNumeroCuotasEntradasCreditos(
		// egreso, numeroCuotas, fechaActual);
		// }
	}

	public void generarListaDetalle() {
		// listaEgresosDetalle = new ArrayList<FacturaReporte>();
		// List<Detalleegreso> list = egreso.getDetalleegresos();
		// for (Detalleegreso de : list)
		// listaEgresosDetalle.add(egresoService.asignar(de, 1));
	}

	public CantidadesCreditoReporte getCantidadesCreditoReporte() {
		return cantidadesCreditoReporte;
	}

	public CantidadesEntradaReporte getCantidadesEntradaReporte() {
		return cantidadesEntradaReporte;
	}

	public String getCriterioBusquedaCodigo() {
		return criterioBusquedaCodigo;
	}

	public DetalleCredito getDetallesCredito() {
		return detallesCredito;
	}

	public Factura getEgreso() {
		return egreso;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public Persona getGarante() {
		return garante;
	}

	public List<Persona> getGarantes() {
		// if (egreso.getEgresoid() != null && egreso.getCredito() != null)
		// garantes = clienteService.obtenerGarantesPorCredito(egreso
		// .getCredito().getCreditoid());

		return garantes;
	}

	public List<Factura> getListaEgresos() {
		return listaEgresos;
	}

	public List<FacturaReporte> getListaEgresosDetalle() {
		return listaEgresosDetalle;
	}

	public NumeroCuotasReporte getNumeroCuotas() {
		return numeroCuotas;
	}

	public Integer getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	// // busqueda de pedidos lazyData
	public void obtenerEgresosClienteLazyData() {
		// listaEgresos = egresoService.obtenerEgresosPorCliente(
		// SecurityContextHolder.getContext().getAuthentication()
		// .getName().toUpperCase(),
		// criterioBusquedaCodigo.toUpperCase(), tipoDocumentoId);
	}

	public void setCantidadesCreditoReporte(
			CantidadesCreditoReporte cantidadesCreditoReporte) {
		this.cantidadesCreditoReporte = cantidadesCreditoReporte;
	}

	public void setCantidadesEntradaReporte(
			CantidadesEntradaReporte cantidadesEntradaReporte) {
		this.cantidadesEntradaReporte = cantidadesEntradaReporte;
	}

	public void setCriterioBusquedaCodigo(String criterioBusquedaCodigo) {
		this.criterioBusquedaCodigo = criterioBusquedaCodigo;
	}

	public void setDetallesCredito(DetalleCredito detallesCredito) {
		this.detallesCredito = detallesCredito;
	}

	public void setEgreso(Factura egreso) {
		this.egreso = egreso;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public void setGarante(Persona garante) {
		this.garante = garante;
	}

	public void setGarantes(List<Persona> garantes) {
		this.garantes = garantes;
	}

	public void setListaEgresos(List<Factura> listaEgresos) {
		this.listaEgresos = listaEgresos;
	}

	public void setListaEgresosDetalle(List<FacturaReporte> listaEgresosDetalle) {
		this.listaEgresosDetalle = listaEgresosDetalle;
	}

	public void setNumeroCuotas(NumeroCuotasReporte numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public void setTipoDocumentoId(Integer tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

}