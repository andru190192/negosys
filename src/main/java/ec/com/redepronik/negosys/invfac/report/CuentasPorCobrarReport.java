package ec.com.redepronik.negosys.invfac.report;

import static ec.com.redepronik.negosys.utils.Utils.comprobarCedula;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsDate.diasMora;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoString;
import static ec.com.redepronik.negosys.utils.UtilsMath.moraTotal;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleCredito;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entityAux.CentralRiesgoReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CobranzaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CobranzaReporteAuxiliar;
import ec.com.redepronik.negosys.invfac.entityAux.CobroFacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CobroFacturaReporteAuxiliar;
import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.invfac.service.MoraService;
import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.utils.service.ReporteService;

@Controller
@Scope("session")
public class CuentasPorCobrarReport {

	@Autowired
	private ReporteService reporteService;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private FacturaService egresoService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private MoraService moraService;

	@Autowired
	private EmpleadoService empleadoService;

	private List<Ciudad> listaCiudades;

	private List<PersonaCedulaNombre> listaVendedores;
	private int vendedorId;

	private Provincia provincia;
	private Ciudad ciudad;
	private Date fechaInicio;
	private Date fechaFin;

	public CuentasPorCobrarReport() {
		ciudad = new Ciudad();
		listaCiudades = new ArrayList<Ciudad>();
	}

	public void cargarCiudades() {
		if (provincia.getId() != 0) {
			listaCiudades = new ArrayList<Ciudad>();
			listaCiudades = ciudadService.obtenerPorProvincia(provincia);
		}
	}

	private String comprobarCed(String cedulaRuc) {
		if (comprobarCedula(cedulaRuc)) {
			if (cedulaRuc.length() == 10)
				return "C";
			else if (cedulaRuc.length() == 13)
				return "R";
		}
		return "NO TIENE";
	}

	private int diasVencido(Date fechaCorte, Factura egreso) {
		List<DetalleCredito> list2 = egreso.getCredito().getDetalleCreditos();
		for (DetalleCredito detalleCredito : list2)
			if (!detalleCredito.getPagado())
				if (detalleCredito.getFechaLimite().compareTo(fechaCorte) < 0)
					return diasMora(fechaCorte, detalleCredito.getFechaLimite())
							.intValue();

		return 0;
	}

	@SuppressWarnings("static-access")
	private Date fechaCorte() {
		Calendar cal = Calendar.getInstance();
		int anio = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH);
		int dia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (mes == 0) {
			mes = 11;
			anio--;
		} else {
			mes--;
			cal.set(anio, mes, Calendar.DAY_OF_MONTH);
			dia = cal.getActualMaximum(cal.DAY_OF_MONTH);
		}
		cal.set(anio, mes, dia);
		return cal.getTime();
	}

	private List<CentralRiesgoReporte> generarCentralRiesgo() {
		Date FEC_CORTE_SALDO = fechaCorte();
		List<CentralRiesgoReporte> list = new ArrayList<CentralRiesgoReporte>();

		List<Factura> listaEgreso = egresoService
				.obtenerPorCreditoAlDiaAndFechaLimite(FEC_CORTE_SALDO);

		List<Factura> listaEgresoVencido = egresoService
				.obtenerPorCreditoVencidoAndFechaLimite(FEC_CORTE_SALDO);

		if (listaEgreso != null && !listaEgreso.isEmpty()) {

			for (int i = 0; i < listaEgreso.size(); i++)
				for (DetalleCredito detalleCredito : listaEgreso.get(i)
						.getCredito().getDetalleCreditos())
					if (detalleCredito.getFechaLimite().compareTo(
							FEC_CORTE_SALDO) > 0) {
						listaEgreso.remove(i);
						break;
					}

			int morososEnviar = listaEgreso.size() * 28 / 72;
			for (int i = 0; i <= morososEnviar; i++)
				listaEgreso.add(listaEgresoVencido.get(i));

			for (Factura egreso : listaEgreso) {
				CentralRiesgoReporte centralRiesgoReporteCliente = new CentralRiesgoReporte();
				Persona cliente = personaService.obtenerPorPersonaId(egreso
						.getClienteFactura().getPersona().getId());

				String VAL_OPERACION = totalFactura(egreso);
				String VAL_TOTAL = totalTotal(egreso, FEC_CORTE_SALDO);
				String VAL_VENCIDO = totalVencido(egreso, FEC_CORTE_SALDO);
				String VAL_XVENCER = totalXVencer(egreso, FEC_CORTE_SALDO);
				int NUM_DIAS_VENCIDO = diasVencido(FEC_CORTE_SALDO, egreso);

				centralRiesgoReporteCliente.setCOD_TIPO_ID(comprobarCed(cliente
						.getCedula()));
				centralRiesgoReporteCliente.setCOD_ID_SUJETO(cliente
						.getCedula());
				centralRiesgoReporteCliente.setNOM_SUJETO(cliente.getNombre()
						+ " " + cliente.getApellido());
				centralRiesgoReporteCliente
						.setDIRECCION(cliente.getDireccion());
				centralRiesgoReporteCliente.setCIUDAD(cliente.getCiudad()
						.getNombre());
				if (cliente.getTelefono() != null)
					centralRiesgoReporteCliente.setTELEFONO(cliente
							.getTelefono());
				else
					centralRiesgoReporteCliente.setTELEFONO("0994387691");

				// //////////////////////////////
				centralRiesgoReporteCliente.setACREEDOR("ALMACENES PATTY");
				centralRiesgoReporteCliente
						.setFEC_CORTE_SALDO(fechaFormatoString(FEC_CORTE_SALDO));
				centralRiesgoReporteCliente.setTIPO_DEUDOR("TITULAR");
				centralRiesgoReporteCliente.setNUM_OPERACION(String
						.valueOf(egreso.getId()));
				centralRiesgoReporteCliente
						.setFEC_CONCESION(fechaFormatoString(egreso
								.getFechaInicio()));
				centralRiesgoReporteCliente.setVAL_OPERACION(VAL_OPERACION);
				centralRiesgoReporteCliente.setVAL_TOTAL(VAL_TOTAL);
				centralRiesgoReporteCliente.setVAL_XVENCER(VAL_XVENCER);
				centralRiesgoReporteCliente.setVAL_VENCIDO(VAL_VENCIDO);
				centralRiesgoReporteCliente.setVAL_DEM_JUDICIAL("0,00");
				centralRiesgoReporteCliente.setVAL_CART_CASTIGADA("0,00");
				centralRiesgoReporteCliente
						.setNUM_DIAS_VENCIDO(NUM_DIAS_VENCIDO);

				list.add(centralRiesgoReporteCliente);

				List<Persona> listGarantes = clienteService
						.obtenerGarantesPorCredito(egreso.getCredito().getId());
				if (!listGarantes.isEmpty()) {
					CentralRiesgoReporte centralRiesgoReporteGarante = new CentralRiesgoReporte();
					Persona garante = listGarantes.get(0);

					centralRiesgoReporteGarante
							.setCOD_TIPO_ID(comprobarCed(garante.getCedula()));
					centralRiesgoReporteGarante.setCOD_ID_SUJETO(garante
							.getCedula());
					centralRiesgoReporteGarante.setNOM_SUJETO(garante
							.getNombre() + " " + garante.getApellido());
					centralRiesgoReporteGarante.setDIRECCION(garante
							.getDireccion());
					centralRiesgoReporteGarante.setCIUDAD(garante.getCiudad()
							.getNombre());
					if (garante.getTelefono() != null) {
						centralRiesgoReporteGarante.setTELEFONO(garante
								.getTelefono());
						if (list.get(list.indexOf(centralRiesgoReporteCliente))
								.getTELEFONO().equalsIgnoreCase("0994387691"))
							list.get(list.indexOf(centralRiesgoReporteCliente))
									.setTELEFONO(garante.getTelefono());
					} else
						centralRiesgoReporteGarante.setTELEFONO("0994387691");

					// //////////////////////////////
					centralRiesgoReporteGarante.setACREEDOR("ALMACENES PATTY");
					centralRiesgoReporteGarante
							.setFEC_CORTE_SALDO(fechaFormatoString(FEC_CORTE_SALDO));
					centralRiesgoReporteGarante.setTIPO_DEUDOR("GARANTE");
					centralRiesgoReporteGarante.setNUM_OPERACION(String
							.valueOf(egreso.getId()));
					centralRiesgoReporteGarante
							.setFEC_CONCESION(fechaFormatoString(egreso
									.getFechaInicio()));
					centralRiesgoReporteGarante.setVAL_OPERACION(VAL_OPERACION);
					centralRiesgoReporteGarante.setVAL_TOTAL(VAL_TOTAL);
					centralRiesgoReporteGarante.setVAL_XVENCER(VAL_XVENCER);
					centralRiesgoReporteGarante.setVAL_VENCIDO(VAL_VENCIDO);
					centralRiesgoReporteGarante.setVAL_DEM_JUDICIAL("0,00");
					centralRiesgoReporteGarante.setVAL_CART_CASTIGADA("0,00");
					centralRiesgoReporteGarante
							.setNUM_DIAS_VENCIDO(NUM_DIAS_VENCIDO);

					list.add(centralRiesgoReporteGarante);
				}

			}
		}
		return list;
	}

	private List<CobranzaReporte> generarCobranza() {
		List<CobranzaReporte> list = new ArrayList<CobranzaReporte>();
		for (Factura egreso : egresoService.obtenerNoPagadosPorCiudad(ciudad
				.getId())) {
			CobranzaReporte cr = new CobranzaReporte();
			cr.setCliente(personaService.obtenerPorPersonaId(egreso
					.getCliente().getPersona().getId()));
			List<Persona> listaGarantes = clienteService
					.obtenerGarantesPorCredito(egreso.getCredito().getId());
			if (listaGarantes != null && !listaGarantes.isEmpty())
				cr.setGarante(personaService.obtenerPorPersonaId(listaGarantes
						.get(0).getId()));

			if (!egreso.getDetalleFactura().isEmpty())
				cr.setNombreProducto(egreso.getDetalleFactura().get(0)
						.getProducto().getNombre());

			cr.setList(new ArrayList<CobranzaReporteAuxiliar>());
			BigDecimal mora = moraService.obtenerPorFecha(
					egreso.getFechaInicio()).getPorcentaje();
			for (DetalleCredito detalleCredito : egreso.getCredito()
					.getDetalleCreditos())
				if (!detalleCredito.getPagado()) {
					CobranzaReporteAuxiliar cra = new CobranzaReporteAuxiliar();
					if (compareTo(detalleCredito.getFechaLimite(), new Date()) < 0) {
						if (compareTo(new Date(), detalleCredito.getFechaMora()) >= 0) {
							BigDecimal moraTotal = moraTotal(new Date(),
									detalleCredito.getFechaMora(), mora);
							cra.setMora(multiplicarDivide(moraTotal,
									detalleCredito.getSaldo()));
							cra.setSubTotal(detalleCredito.getSaldo().add(
									cra.getMora()));
						} else {
							cra.setMora(newBigDecimal());
							cra.setSubTotal(detalleCredito.getSaldo());
						}
						cra.setFechaLimite(fechaFormatoString(detalleCredito
								.getFechaLimite()));
						cra.setSaldo(detalleCredito.getSaldo());

						cra.setSaldo(redondearTotales(cra.getSaldo()));
						cra.setMora(redondearTotales(cra.getMora()));
						cra.setSubTotal(redondearTotales(cra.getSubTotal()));
						cr.getList().add(cra);
					}
				}
			list.add(cr);
		}
		return list;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public Provincia[] getListaProvincias() {
		return Provincia.values();
	}

	public List<PersonaCedulaNombre> getListaVendedores() {
		return listaVendedores;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public int getVendedorId() {
		return vendedorId;
	}

	@PostConstruct
	public void init() {
		listaVendedores = empleadoService.obtenerPorCargo(5);
	}

	public void reporteCentralRiesgo(ActionEvent actionEvent) {
		List<CentralRiesgoReporte> list = generarCentralRiesgo();
		Map<String, Object> parametro = new HashMap<String, Object>();
		reporteService.generarReporteXLS(list, parametro, "DATOS");
	}

	public void reporteCobranza(ActionEvent actionEvent) {
		List<CobranzaReporte> list = generarCobranza();
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("CIUDAD", ciudadService
				.obtenerPorCiudadId(ciudad.getId()).getNombre());
		reporteService.generarReportePDF(list, parametro, "CobranzaCredito");
	}

	public void reporteCobranzaFactura(ActionEvent actionEvent) {
		boolean bn = false;
		List<CobroFacturaReporte> list = new ArrayList<CobroFacturaReporte>();
		BigDecimal sumTotal = newBigDecimal();
		for (Persona persona : clienteService
				.obtenerClientesPorFacturasPendientes(ciudad.getId(),
						vendedorId)) {
			bn = true;
			List<CobroFacturaReporteAuxiliar> list1 = new ArrayList<CobroFacturaReporteAuxiliar>();
			for (Factura e : egresoService.obtenerPorEstado(
					persona.getCedula(), null, null, null)) {
				BigDecimal total = redondearTotales(egresoService
						.calcularTotal(e).subtract(
								egresoService.calcularEntradas(e)));

				list1.add(new CobroFacturaReporteAuxiliar(e.getFechaInicio(),
						String.valueOf(e.getId()), total));
				sumTotal = sumTotal.add(total);
			}
			list.add(new CobroFacturaReporte(persona.getCliente().getFolio(),
					persona.getCliente().getNombreComercial(), persona
							.getDireccion(), persona.getReferencia(), list1));
		}
		if (bn) {
			String nombre = "";
			if (vendedorId != 0) {
				Persona p = empleadoService
						.obtenerPorEmpleadoCargoId(vendedorId).getEmpleado()
						.getPersona();
				nombre = p.getApellido() + " " + p.getNombre();
			}
			Map<String, Object> parametro = new HashMap<String, Object>();
			parametro.put("cobrador", nombre);
			parametro.put("ciudad",
					ciudadService.obtenerPorCiudadId(ciudad.getId())
							.getNombre());
			parametro.put("total", sumTotal);
			reporteService
					.generarReportePDF(list, parametro, "CobranzaFactura");
		} else
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO TIENE FACTURAS POR COBRAR");
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setListaVendedores(List<PersonaCedulaNombre> listaVendedores) {
		this.listaVendedores = listaVendedores;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public void setVendedorId(int vendedorId) {
		this.vendedorId = vendedorId;
	}

	private String totalFactura(Factura egreso) {
		return redondear(egreso.getCredito().getMonto()).toString().replace(
				'.', ',');
	}

	private String totalTotal(Factura egreso, Date fechaCorte) {
		BigDecimal val = newBigDecimal();
		for (DetalleCredito detalleCredito : egreso.getCredito()
				.getDetalleCreditos())
			if (!detalleCredito.getPagado())
				val = val.add(detalleCredito.getCuota());
		return redondear(val).toString().replace('.', ',');
	}

	private String totalVencido(Factura egreso, Date fechaCorte) {
		BigDecimal val = newBigDecimal();
		for (DetalleCredito detalleCredito : egreso.getCredito()
				.getDetalleCreditos())
			if (!detalleCredito.getPagado())
				if (compareTo(detalleCredito.getFechaLimite(), fechaCorte) < 0)
					val = val.add(detalleCredito.getCuota());
		return redondear(val).toString().replace('.', ',');
	}

	private String totalXVencer(Factura egreso, Date fechaCorte) {
		BigDecimal val = newBigDecimal();
		for (DetalleCredito detalleCredito : egreso.getCredito()
				.getDetalleCreditos())
			if (!detalleCredito.getPagado())
				if (compareTo(detalleCredito.getFechaLimite(), fechaCorte) >= 0)
					val = val.add(detalleCredito.getCuota());
		return redondear(val).toString().replace('.', ',');
	}

}