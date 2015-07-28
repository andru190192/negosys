package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

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
import ec.com.redepronik.negosys.invfac.entity.CuotaIngreso;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entity.TipoPago;
import ec.com.redepronik.negosys.invfac.entityAux.CobroReporte;
import ec.com.redepronik.negosys.invfac.service.IngresoService;
import ec.com.redepronik.negosys.invfac.service.PagoEntradaIngresoService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;

@Controller
@Scope("session")
public class PagoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private IngresoService ingresoService;

	@Autowired
	private PagoEntradaIngresoService pagoEntradaIngresoService;

	private List<CobroReporte> listaIngresos;
	private List<CobroReporte> listaIngresosSeleccionados;

	private Persona criterioBusquedaProveedor;
	private Date criterioBusquedafechaInicio;
	private Date criterioBusquedafechaFin;
	private String criterioBusquedaDocumento;

	private String auxcriterioBusquedaProveedor;
	private Date auxCriterioBusquedafechaInicio;
	private Date auxCriterioBusquedafechaFin;
	private String auxCriterioBusquedaDocumento;

	private boolean bn;

	private boolean bnBanco;
	private boolean bnEfectivo;
	private boolean bnTarjeta;
	private String chequeTarjetaVaucher;
	private String chequeTarjetaCuenta;
	private CobroReporte cobroReporte;

	private Ingreso ingreso;
	private List<CuotaIngreso> listaCuotaIngreso;
	private CuotaIngreso pagoEntrada;
	private Banco banco;
	private Date fechapago;
	private BigDecimal totalCuota;
	private BigDecimal totalTotal;
	private BigDecimal totalAbonos;
	private BigDecimal totalSaldo;

	// Cuadro de Busqueda de proveedores
	private List<Persona> listaProveedorBusqueda;
	private String criterioProveedorBusqueda;

	private Date fechaPagoRapido;

	public PagoBean() {
		listaIngresos = new ArrayList<CobroReporte>();
		banco = new Banco();
		bnBanco = false;
		bnEfectivo = false;
		bnTarjeta = false;
		listaCuotaIngreso = new ArrayList<CuotaIngreso>();
		pagoEntrada = new CuotaIngreso();
		getListaFormasPago();
		pagoEntrada.setTipoPago(TipoPago.EF);
		totalCuota = newBigDecimal();
		totalTotal = newBigDecimal();
		totalSaldo = newBigDecimal();
		totalAbonos = newBigDecimal();
		ingreso = new Ingreso();
		ingreso.setCuotaIngresos(new ArrayList<CuotaIngreso>());
		cobroReporte = new CobroReporte();

		fechaPagoRapido = new Date();

		criterioBusquedaProveedor = new Persona();
		criterioBusquedafechaInicio = null;
		criterioBusquedafechaFin = null;
		criterioBusquedaDocumento = new String();
	}

	public void cargarIngreso() {
		ingreso = ingresoService.obtenerPorIngresoId(cobroReporte.getId()
				.longValue());
	}

	public void cargarProveedor(SelectEvent event) {
		criterioBusquedaProveedor = proveedorService
				.obtenerPorPersonaId(criterioBusquedaProveedor.getId());
	}

	public void comprobarIngresosSeleccionados() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean error = false;

		if (listaIngresosSeleccionados == null
				|| listaIngresosSeleccionados.isEmpty()) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"MENSAJE DEL SISTEMA: ESCOJA POR LO MENOS UN REGISTRO");
			error = true;
		}
		context.addCallbackParam("error1", error ? true : false);
	}

	public void eliminarPagoEntrada() {
		listaCuotaIngreso.remove(pagoEntrada);
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

	public String getCriterioBusquedaDocumento() {
		return criterioBusquedaDocumento;
	}

	public Date getCriterioBusquedafechaFin() {
		return criterioBusquedafechaFin;
	}

	public Date getCriterioBusquedafechaInicio() {
		return criterioBusquedafechaInicio;
	}

	public Persona getcriterioBusquedaProveedor() {
		return criterioBusquedaProveedor;
	}

	public String getcriterioProveedorBusqueda() {
		return criterioProveedorBusqueda;
	}

	public Date getFechapago() {
		return fechapago;
	}

	public Date getFechaPagoRapido() {
		return fechaPagoRapido;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public List<CuotaIngreso> getListaCuotaIngreso() {
		return listaCuotaIngreso;
	}

	public TipoPago[] getListaFormasPago() {
		return TipoPago.values();
	}

	public List<CobroReporte> getListaIngresos() {
		return listaIngresos;
	}

	public List<CobroReporte> getListaIngresosSeleccionados() {
		return listaIngresosSeleccionados;
	}

	public List<Persona> getlistaProveedorBusqueda() {
		return listaProveedorBusqueda;
	}

	public CuotaIngreso getPagoEntrada() {
		return pagoEntrada;
	}

	public BigDecimal getTotalAbonos() {
		return redondearTotales(totalAbonos);
	}

	public BigDecimal getTotalCuota() {
		return totalCuota;
	}

	public BigDecimal getTotalSaldo() {
		return redondearTotales(totalSaldo);
	}

	public BigDecimal getTotalTotal() {
		return redondearTotales(totalTotal);
	}

	@PostConstruct
	public void init() {
		bn = true;
		listaIngresosSeleccionados = new ArrayList<CobroReporte>();
	}

	public void insertarCobro() {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean error = false;

		if (listaCuotaIngreso == null || listaCuotaIngreso.isEmpty()) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"MENSAJE DEL SISTEMA: INGRESE POR LO MENOS UN PAGO");
			error = true;
		} else {
			pagoEntradaIngresoService.cobro(listaCuotaIngreso,
					listaIngresosSeleccionados);
			recarga();
		}
		context.addCallbackParam("error", error ? true : false);
	}

	public void insertarPagoEntrada() {
		if (pagoEntrada.getTipoPago() == null
				|| pagoEntrada.getTipoPago().getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"MENSAJE DEL SISTEMA: INGRESE UN TIPO DE PAGO");
		} else if (pagoEntrada.getCuota().compareTo(new BigDecimal("0")) == 0) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"MENSAJE DEL SISTEMA: INGRESE UN MONTO");
		} else if (fechapago == null) {
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"MENSAJE DEL SISTEMA: INGRESE UNA FECHA");
		} else {
			BigDecimal auxTotalCuota = totalCuota.add(pagoEntrada.getCuota());
			if (auxTotalCuota.compareTo(totalSaldo) > 0) {
				presentaMensaje(
						FacesMessage.SEVERITY_ERROR,
						"MENSAJE DEL SISTEMA: LA SUMA DE LAS CUOTAS NO DEBE SER MAYOR AL TOTAL DEL SALDO");
			} else {
				pagoEntrada.setFechaPago(new Timestamp(fechapago.getTime()));

				totalCuota = auxTotalCuota;
				listaCuotaIngreso.add(pagoEntrada);
			}
		}
		pagoEntrada = new CuotaIngreso();
		pagoEntrada.setTipoPago(TipoPago.EF);
		fechapago = null;
		bnBanco = false;
		bnEfectivo = false;
		bnTarjeta = false;
	}

	public boolean isBn() {
		return bn;
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
		listaCuotaIngreso = new ArrayList<CuotaIngreso>();
		criterioBusquedaProveedor = new Persona();
		totalCuota = newBigDecimal();
		totalTotal = newBigDecimal();
		totalSaldo = newBigDecimal();
		totalAbonos = newBigDecimal();
	}

	public void limpiarObjetosBusquedaCliente() {
		criterioProveedorBusqueda = new String();
		listaProveedorBusqueda = new ArrayList<Persona>();
	}

	public void obtenerFacturasPendientes() {
		List<Ingreso> lista = ingresoService.obtenerIngresosNoPagados(
				criterioBusquedaProveedor.getCedula(),
				criterioBusquedaDocumento, criterioBusquedafechaInicio,
				criterioBusquedafechaFin);
		listaIngresos = new ArrayList<CobroReporte>();
		if (lista.isEmpty()) {
			bn = true;
			limpiar();
		} else {
			listaIngresos = new ArrayList<CobroReporte>();
			auxcriterioBusquedaProveedor = criterioBusquedaProveedor
					.getCedula();
			auxCriterioBusquedafechaInicio = criterioBusquedafechaInicio;
			auxCriterioBusquedafechaFin = criterioBusquedafechaFin;
			auxCriterioBusquedaDocumento = criterioBusquedaDocumento;

			criterioBusquedaProveedor = new Persona();
			criterioBusquedafechaInicio = null;
			criterioBusquedafechaFin = null;
			criterioBusquedaDocumento = new String();

			bn = false;

			totalTotal = newBigDecimal();
			totalAbonos = newBigDecimal();
			totalSaldo = newBigDecimal();

			for (Ingreso ingreso : lista) {
				CobroReporte cobroReporte = new CobroReporte();

				cobroReporte
						.setId(Integer.parseInt(ingreso.getId().toString()));
				cobroReporte
						.setNombre(ingreso.getProveedor().getPersona()
								.getApellido()
								+ " "
								+ ingreso.getProveedor().getPersona()
										.getNombre());
				cobroReporte.setEscogido(false);
				cobroReporte.setOrden(0);
				// cobroReporte.setTipoDocumento(ingreso.getTipodocumento()
				// .getNombre());
				cobroReporte.setCodigoDocumento(ingreso.getCodigoDocumento());
				cobroReporte.setFechaEmision(ingreso.getFechaFactura());
				cobroReporte.setTotal(ingresoService.calcularTotal(ingreso));
				cobroReporte.setAbono(ingresoService.calcularEntradas(ingreso));
				cobroReporte.setSaldo(redondearTotales(cobroReporte.getTotal()
						.subtract(cobroReporte.getAbono())));

				listaIngresos.add(cobroReporte);
			}
		}
	}

	public void obtenerProveedorPorBusqueda() {
		listaProveedorBusqueda = proveedorService
				.obtener(criterioProveedorBusqueda);
	}

	public void onRowSelect(SelectEvent event) {
		if (listaIngresosSeleccionados.size() > 1
				&& listaIngresosSeleccionados
						.get(0)
						.getNombre()
						.compareTo(
								listaIngresosSeleccionados.get(
										listaIngresosSeleccionados.size() - 1)
										.getNombre()) != 0) {
			listaIngresosSeleccionados
					.remove(listaIngresosSeleccionados.size() - 1);
			presentaMensaje(FacesMessage.SEVERITY_WARN,
					"SOLO PUEDE ESCOJER FACTURAS DE UN MISMO CLIENTE");
		}

		totalTotal = newBigDecimal();
		totalAbonos = newBigDecimal();
		totalSaldo = newBigDecimal();
		for (CobroReporte cr : listaIngresosSeleccionados) {
			totalTotal = totalTotal.add(cr.getTotal());
			totalAbonos = totalAbonos.add(cr.getAbono());
			totalSaldo = totalSaldo.add(cr.getSaldo());
		}

		for (CobroReporte cr1 : listaIngresos) {
			cr1.setEscogido(false);
			cr1.setOrden(0);
		}

		int orden = 1;
		for (CobroReporte cr1 : listaIngresosSeleccionados)
			cr1.setOrden(orden++);

		for (CobroReporte cr1 : listaIngresos)
			for (CobroReporte cr2 : listaIngresosSeleccionados)
				if (cr1.getId() == cr2.getId()) {
					cr1.setEscogido(true);
					cr1.setOrden(cr2.getOrden());
					break;
				}
	}

	public void onRowUnselect(UnselectEvent event) {
		totalAbonos = newBigDecimal();
		totalSaldo = newBigDecimal();
		for (CobroReporte cr : listaIngresosSeleccionados) {
			totalTotal = totalTotal.add(cr.getTotal());
			totalAbonos = totalAbonos.add(cr.getAbono());
			totalSaldo = totalSaldo.add(cr.getSaldo());
		}

		for (CobroReporte cr1 : listaIngresos) {
			cr1.setEscogido(false);
			cr1.setOrden(0);
		}

		int orden = 1;
		for (CobroReporte cr1 : listaIngresosSeleccionados) {
			cr1.setOrden(orden++);
		}

		for (CobroReporte cr1 : listaIngresos) {
			for (CobroReporte cr2 : listaIngresosSeleccionados) {
				if (cr1.getId() == cr2.getId()) {
					cr1.setEscogido(true);
					cr1.setOrden(cr2.getOrden());
					break;
				}
			}
		}
	}

	public void pagoLote() {
		pagoEntradaIngresoService.pagoLote(listaIngresosSeleccionados,
				fechaPagoRapido);
		recarga();
	}

	public void recarga() {
		// cargarCliente(null);
		List<Ingreso> lista = ingresoService.obtenerIngresosNoPagados(
				auxcriterioBusquedaProveedor, auxCriterioBusquedaDocumento,
				auxCriterioBusquedafechaInicio, auxCriterioBusquedafechaFin);
		listaIngresos = new ArrayList<CobroReporte>();
		if (lista.isEmpty()) {
			bn = true;
			limpiar();
		} else {
			bn = false;

			totalTotal = newBigDecimal();
			totalAbonos = newBigDecimal();
			totalSaldo = newBigDecimal();

			for (Ingreso ingreso : lista) {
				CobroReporte cobroReporte = new CobroReporte();

				cobroReporte
						.setId(Integer.parseInt(ingreso.getId().toString()));
				cobroReporte
						.setNombre(ingreso.getProveedor().getPersona()
								.getApellido()
								+ " "
								+ ingreso.getProveedor().getPersona()
										.getNombre());
				cobroReporte.setEscogido(false);
				cobroReporte.setOrden(0);
				// cobroReporte.setTipoDocumento(ingreso.getTipodocumento()
				// .getNombre());
				cobroReporte.setCodigoDocumento(ingreso.getCodigoDocumento());
				cobroReporte.setFechaEmision(ingreso.getFechaFactura());
				cobroReporte.setTotal(ingresoService.calcularTotal(ingreso));
				cobroReporte.setAbono(ingresoService.calcularEntradas(ingreso));
				cobroReporte.setSaldo(redondearTotales(cobroReporte.getTotal()
						.subtract(cobroReporte.getAbono())));

				listaIngresos.add(cobroReporte);
			}
		}

		listaCuotaIngreso = new ArrayList<CuotaIngreso>();
		totalCuota = new BigDecimal("0");
		listaIngresosSeleccionados = new ArrayList<CobroReporte>();
		totalTotal = newBigDecimal();
		totalAbonos = newBigDecimal();
		totalSaldo = newBigDecimal();
		fechaPagoRapido = new Date();
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void setBn(boolean bn) {
		this.bn = bn;
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

	public void setCriterioBusquedaDocumento(String criterioBusquedaDocumento) {
		this.criterioBusquedaDocumento = criterioBusquedaDocumento;
	}

	public void setCriterioBusquedafechaFin(Date criterioBusquedafechaFin) {
		this.criterioBusquedafechaFin = criterioBusquedafechaFin;
	}

	public void setCriterioBusquedafechaInicio(Date criterioBusquedafechaInicio) {
		this.criterioBusquedafechaInicio = criterioBusquedafechaInicio;
	}

	public void setcriterioBusquedaProveedor(Persona criterioBusquedaProveedor) {
		this.criterioBusquedaProveedor = criterioBusquedaProveedor;
	}

	public void setcriterioProveedorBusqueda(String criterioProveedorBusqueda) {
		this.criterioProveedorBusqueda = criterioProveedorBusqueda;
	}

	public void setFechapago(Date fechapago) {
		this.fechapago = fechapago;
	}

	public void setFechaPagoRapido(Date fechaPagoRapido) {
		this.fechaPagoRapido = fechaPagoRapido;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public void setListaCuotaIngreso(List<CuotaIngreso> listaCuotaIngreso) {
		this.listaCuotaIngreso = listaCuotaIngreso;
	}

	public void setListaIngresos(List<CobroReporte> listaIngresos) {
		this.listaIngresos = listaIngresos;
	}

	public void setListaIngresosSeleccionados(
			List<CobroReporte> listaIngresosSeleccionados) {
		this.listaIngresosSeleccionados = listaIngresosSeleccionados;
	}

	public void setlistaProveedorBusqueda(List<Persona> listaProveedorBusqueda) {
		this.listaProveedorBusqueda = listaProveedorBusqueda;
	}

	public void setPagoEntrada(CuotaIngreso pagoEntrada) {
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