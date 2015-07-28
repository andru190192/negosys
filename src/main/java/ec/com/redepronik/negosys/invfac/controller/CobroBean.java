package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Banco;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.PagoEntrada;
import ec.com.redepronik.negosys.invfac.entity.TipoPago;
import ec.com.redepronik.negosys.invfac.entityAux.CobroReporte;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.invfac.service.PagoEntradaFacturaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;

@Controller
@Scope("session")
public class CobroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private PagoEntradaFacturaService pagoEntradaService;

	private List<CobroReporte> listaFacturas;
	private List<CobroReporte> listaFacturasSeleccionados;

	private Persona criterioBusquedaCliente;
	private Date criterioBusquedafechaInicio;
	private Date criterioBusquedafechaFin;
	private String criterioBusquedaDocumento;

	private boolean bnBanco;
	private boolean bnEfectivo;
	private boolean bnTarjeta;
	private String chequeTarjetaVaucher;
	private String chequeTarjetaCuenta;
	private CobroReporte cobroReporte;

	private Factura factura;
	private List<PagoEntrada> listaPagoEntrada;
	private PagoEntrada pagoEntrada;
	private Banco banco;
	private Date fechapago;
	private BigDecimal totalCuota;
	private BigDecimal totalTotal;

	private BigDecimal totalAbonos;
	private BigDecimal totalSaldo;

	// Cuadro de Busqueda de clientes
	private List<Persona> listaClienteBusqueda;
	private String criterioClienteBusqueda;

	private Date fechaPagoRapido;

	public CobroBean() {
		listaFacturas = new ArrayList<CobroReporte>();
		banco = new Banco();
		bnBanco = false;
		bnEfectivo = false;
		bnTarjeta = false;
		listaPagoEntrada = new ArrayList<PagoEntrada>();
		pagoEntrada = new PagoEntrada();
		pagoEntrada.setTipoPago(TipoPago.EF);
		totalCuota = newBigDecimal();
		totalTotal = newBigDecimal();
		totalSaldo = newBigDecimal();
		totalAbonos = newBigDecimal();
		factura = new Factura();
		cobroReporte = new CobroReporte();

		fechaPagoRapido = new Date();
		fechapago = new Date();

		criterioBusquedaCliente = new Persona();
		criterioBusquedafechaInicio = null;
		criterioBusquedafechaFin = null;
		criterioBusquedaDocumento = new String();
	}

	public void cargarCliente(SelectEvent event) {
		criterioBusquedaCliente = clienteService
				.obtenerPorPersonaId(criterioBusquedaCliente.getId());
	}

	public void cargarEgreso() {
		factura = facturaService.obtenerPorCodigo(cobroReporte
				.getCodigoDocumento());
	}

	public void cargarFactura() {
		factura = facturaService.obtenerPorCodigo(cobroReporte
				.getCodigoDocumento());
	}

	public void comprobarFacturasSeleccionados() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean error = false;

		if (listaFacturasSeleccionados == null
				|| listaFacturasSeleccionados.isEmpty()) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"ESCOJA POR LO MENOS UNA FACTURA");
			error = true;
		}
		context.addCallbackParam("error1", error ? true : false);
	}

	public void eliminarPagoEntrada() {
		listaPagoEntrada.remove(pagoEntrada);
		totalCuota = totalCuota.subtract(pagoEntrada.getCuota());
	}

	public Banco getBanco() {
		return banco;
	}

	public String getChequeTarjetaCuenta() {
		return chequeTarjetaCuenta;
	}

	public String getChequeTarjetaVaucher() {
		return chequeTarjetaVaucher;
	}

	public CobroReporte getCobroReporte() {
		return cobroReporte;
	}

	public Persona getCriterioBusquedaCliente() {
		return criterioBusquedaCliente;
	}

	public String getCriterioBusquedaDocumento() {
		return criterioBusquedaDocumento;
	}

	public Date getCriterioBusquedafechaFin() {
		return criterioBusquedafechaFin;
	}

	public Date getCriterioBusquedafechaInicio() {
		return criterioBusquedafechaInicio;
	}

	public String getCriterioClienteBusqueda() {
		return criterioClienteBusqueda;
	}

	public Factura getFactura() {
		return factura;
	}

	public Date getFechapago() {
		return fechapago;
	}

	public Date getFechaPagoRapido() {
		return fechaPagoRapido;
	}

	public List<Persona> getListaClienteBusqueda() {
		return listaClienteBusqueda;
	}

	public List<CobroReporte> getListaFacturas() {
		return listaFacturas;
	}

	public List<CobroReporte> getListaFacturasSeleccionados() {
		return listaFacturasSeleccionados;
	}

	public List<PagoEntrada> getListaPagoEntrada() {
		return listaPagoEntrada;
	}

	public TipoPago[] getListaTiposPago() {
		return TipoPago.values();
	}

	public PagoEntrada getPagoEntrada() {
		return pagoEntrada;
	}

	public BigDecimal getTotalAbonos() {
		return totalAbonos;
	}

	public BigDecimal getTotalCuota() {
		return totalCuota;
	}

	public BigDecimal getTotalSaldo() {
		return totalSaldo;
	}

	public BigDecimal getTotalTotal() {
		return totalTotal;
	}

	@PostConstruct
	public void init() {
		listaFacturasSeleccionados = new ArrayList<CobroReporte>();
	}

	public void insertarCobro() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean error = false;

		if (listaPagoEntrada == null || listaPagoEntrada.isEmpty()) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"INGRESE POR LO MENOS UN PAGO");
			error = true;
		} else {
			pagoEntradaService.cobro(listaPagoEntrada, listaFacturas,
					listaFacturasSeleccionados);
			listaPagoEntrada = new ArrayList<PagoEntrada>();
			totalCuota = new BigDecimal("0");
			listaFacturasSeleccionados = new ArrayList<CobroReporte>();
			totalTotal = newBigDecimal();
			totalAbonos = newBigDecimal();
			totalSaldo = newBigDecimal();
			fechapago = new Date();
		}
		context.addCallbackParam("error", error ? true : false);
	}

	public void insertarPagoEntrada() {
		if (pagoEntrada.getTipoPago() == null
				|| pagoEntrada.getTipoPago().getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"INGRESE UN TIPO DE PAGO");
		} else if (pagoEntrada.getCuota().compareTo(new BigDecimal("0")) == 0) {
			presentaMensaje(FacesMessage.SEVERITY_WARN, "INGRESE UN MONTO");
		} else if (fechapago == null) {
			presentaMensaje(FacesMessage.SEVERITY_WARN, "INGRESE UNA FECHA");
		} else {
			BigDecimal auxTotalCuota = totalCuota.add(pagoEntrada.getCuota());
			if (auxTotalCuota.compareTo(totalSaldo) > 0) {
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"LA SUMA DE LAS CUOTAS NO DEBE SER MAYOR AL TOTAL DEL SALDO");
			} else {
				pagoEntrada.setFechaPago(new Timestamp(fechapago.getTime()));

				totalCuota = auxTotalCuota;
				listaPagoEntrada.add(pagoEntrada);

				pagoEntrada = new PagoEntrada();
				bnBanco = false;
				bnEfectivo = false;
				bnTarjeta = false;
				pagoEntrada.setTipoPago(TipoPago.EF);
			}
		}
	}

	public boolean isBnBanco() {
		return bnBanco;
	}

	public boolean isBnEfectivo() {
		return bnEfectivo;
	}

	public boolean isBnTarjeta() {
		return bnTarjeta;
	}

	public void limpiar() {
		listaPagoEntrada = new ArrayList<PagoEntrada>();
		criterioBusquedaCliente = new Persona();
		totalCuota = newBigDecimal();
		totalTotal = newBigDecimal();
		totalSaldo = newBigDecimal();
		totalAbonos = newBigDecimal();
	}

	public void limpiarObjetosBusquedaCliente() {
		criterioClienteBusqueda = new String();
		listaClienteBusqueda = new ArrayList<Persona>();
	}

	public void obtenerClientesPorBusqueda() {
		if (criterioClienteBusqueda.length() >= 0
				&& criterioClienteBusqueda.length() <= 3) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		} else {
			listaClienteBusqueda = clienteService.obtener(
					criterioClienteBusqueda.toUpperCase(), 0);
			if (listaClienteBusqueda.isEmpty()) {
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
			}
			criterioClienteBusqueda = new String();
		}
	}

	public void obtenerFacturasPendientes() {
		List<Factura> lista = facturaService.obtenerPorEstado(
				criterioBusquedaCliente.getCedula(), criterioBusquedaDocumento,
				criterioBusquedafechaInicio, criterioBusquedafechaFin);

		if (lista.isEmpty()) {
			limpiar();
		} else {
			listaFacturas = new ArrayList<CobroReporte>();
			criterioBusquedaCliente = new Persona();
			criterioBusquedafechaInicio = null;
			criterioBusquedafechaFin = null;
			criterioBusquedaDocumento = new String();

			totalTotal = newBigDecimal();
			totalAbonos = newBigDecimal();
			totalSaldo = newBigDecimal();

			for (Factura factura : lista) {
				CobroReporte cobroReporte = new CobroReporte();

				cobroReporte.setId(factura.getId());
				cobroReporte.setNombre(factura.getCliente().getPersona()
						.getApellido()
						+ " " + factura.getCliente().getPersona().getNombre());
				cobroReporte.setCodigoDocumento(factura.getEstablecimiento()
						.concat("-").concat(factura.getPuntoEmision())
						.concat("-").concat(factura.getSecuencia()));
				cobroReporte.setFechaEmision(factura.getFechaInicio());
				cobroReporte.setEscogido(false);
				cobroReporte.setOrden(0);
				cobroReporte.setTotal(facturaService.calcularTotal(factura));
				cobroReporte.setAbono(facturaService.calcularEntradas(factura));
				cobroReporte.setSaldo(cobroReporte.getTotal().subtract(
						cobroReporte.getAbono()));

				listaFacturas.add(cobroReporte);
			}
		}
	}

	public void onRowSelect(SelectEvent event) {
		if (listaFacturasSeleccionados.size() > 1
				&& listaFacturasSeleccionados
						.get(0)
						.getNombre()
						.compareTo(
								listaFacturasSeleccionados.get(
										listaFacturasSeleccionados.size() - 1)
										.getNombre()) != 0) {
			listaFacturasSeleccionados
					.remove(listaFacturasSeleccionados.size() - 1);
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"SOLO PUEDE ESCOJER FACTURAS DE UN MISMO CLIENTE");
		}

		totalTotal = newBigDecimal();
		totalAbonos = newBigDecimal();
		totalSaldo = newBigDecimal();
		for (CobroReporte cr : listaFacturasSeleccionados) {
			totalTotal = totalTotal.add(cr.getTotal());
			totalAbonos = totalAbonos.add(cr.getAbono());
			totalSaldo = totalSaldo.add(cr.getSaldo());
		}

		for (CobroReporte cr1 : listaFacturas) {
			cr1.setEscogido(false);
			cr1.setOrden(0);
		}

		int orden = 1;
		for (CobroReporte cr1 : listaFacturasSeleccionados) {
			cr1.setOrden(orden++);
		}

		for (CobroReporte cr1 : listaFacturas) {
			for (CobroReporte cr2 : listaFacturasSeleccionados) {
				if (cr1.getId() == cr2.getId()) {
					cr1.setEscogido(true);
					cr1.setOrden(cr2.getOrden());
					break;
				}
			}
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		totalAbonos = newBigDecimal();
		totalSaldo = newBigDecimal();
		for (CobroReporte cr : listaFacturasSeleccionados) {
			totalTotal = totalTotal.add(cr.getTotal());
			totalAbonos = totalAbonos.add(cr.getAbono());
			totalSaldo = totalSaldo.add(cr.getSaldo());
		}

		for (CobroReporte cr1 : listaFacturas) {
			cr1.setEscogido(false);
			cr1.setOrden(0);
		}

		int orden = 1;
		for (CobroReporte cr1 : listaFacturasSeleccionados) {
			cr1.setOrden(orden++);
		}

		for (CobroReporte cr1 : listaFacturas) {
			for (CobroReporte cr2 : listaFacturasSeleccionados) {
				if (cr1.getId() == cr2.getId()) {
					cr1.setEscogido(true);
					cr1.setOrden(cr2.getOrden());
					break;
				}
			}
		}
	}

	public void pagoLote() {
		pagoEntradaService.pagoLote(listaFacturas, listaFacturasSeleccionados,
				fechaPagoRapido);
		listaPagoEntrada = new ArrayList<PagoEntrada>();
		totalCuota = new BigDecimal("0");
		listaFacturasSeleccionados = new ArrayList<CobroReporte>();
		totalTotal = newBigDecimal();
		totalAbonos = newBigDecimal();
		totalSaldo = newBigDecimal();
		fechaPagoRapido = new Date();
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void setBnBanco(boolean bnBanco) {
		this.bnBanco = bnBanco;
	}

	public void setBnEfectivo(boolean bnEfectivo) {
		this.bnEfectivo = bnEfectivo;
	}

	public void setBnTarjeta(boolean bnTarjeta) {
		this.bnTarjeta = bnTarjeta;
	}

	public void setChequeTarjetaCuenta(String chequeTarjetaCuenta) {
		this.chequeTarjetaCuenta = chequeTarjetaCuenta;
	}

	public void setChequeTarjetaVaucher(String chequeTarjetaVaucher) {
		this.chequeTarjetaVaucher = chequeTarjetaVaucher;
	}

	public void setCobroReporte(CobroReporte cobroReporte) {
		this.cobroReporte = cobroReporte;
	}

	public void setCriterioBusquedaCliente(Persona criterioBusquedaCliente) {
		this.criterioBusquedaCliente = criterioBusquedaCliente;
	}

	public void setCriterioBusquedaDocumento(String criterioBusquedaDocumento) {
		this.criterioBusquedaDocumento = criterioBusquedaDocumento;
	}

	public void setCriterioBusquedafechaFin(Date criterioBusquedafechaFin) {
		this.criterioBusquedafechaFin = criterioBusquedafechaFin;
	}

	public void setCriterioBusquedafechaInicio(Date criterioBusquedafechaInicio) {
		this.criterioBusquedafechaInicio = criterioBusquedafechaInicio;
	}

	public void setCriterioClienteBusqueda(String criterioClienteBusqueda) {
		this.criterioClienteBusqueda = criterioClienteBusqueda;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setFechapago(Date fechapago) {
		this.fechapago = fechapago;
	}

	public void setFechaPagoRapido(Date fechaPagoRapido) {
		this.fechaPagoRapido = fechaPagoRapido;
	}

	public void setListaClienteBusqueda(List<Persona> listaClienteBusqueda) {
		this.listaClienteBusqueda = listaClienteBusqueda;
	}

	public void setListaFacturas(List<CobroReporte> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setListaFacturasSeleccionados(
			List<CobroReporte> listaFacturasSeleccionados) {
		this.listaFacturasSeleccionados = listaFacturasSeleccionados;
	}

	public void setListaPagoEntrada(List<PagoEntrada> listaPagoEntrada) {
		this.listaPagoEntrada = listaPagoEntrada;
	}

	public void setPagoEntrada(PagoEntrada pagoEntrada) {
		this.pagoEntrada = pagoEntrada;
	}

	public void setTotalAbonos(BigDecimal totalAbonos) {
		this.totalAbonos = totalAbonos;
	}

	public void setTotalCuota(BigDecimal totalCuota) {
		this.totalCuota = totalCuota;
	}

	public void setTotalSaldo(BigDecimal totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public void setTotalTotal(BigDecimal totalTotal) {
		this.totalTotal = totalTotal;
	}

	public void tipoPago() {
		if (pagoEntrada.getTipoPago() != null
				&& pagoEntrada.getTipoPago().getId() != 0) {
			if (pagoEntrada.getTipoPago().getId() == 1) {
				bnEfectivo = true;
				bnTarjeta = false;
				bnBanco = false;
			} else if (pagoEntrada.getTipoPago().getId() == 2) {
				bnEfectivo = false;
				bnTarjeta = true;
				bnBanco = false;
				chequeTarjetaCuenta = "# TARJETA";
				chequeTarjetaVaucher = "# VAUCHER";
			} else if (pagoEntrada.getTipoPago().getId() == 3) {
				bnEfectivo = false;
				bnTarjeta = true;
				bnBanco = true;
				chequeTarjetaCuenta = "# CUENTA";
				chequeTarjetaVaucher = "# CHEQUE";
			}
		}
	}
}