package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.divide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.UtilsMath.valorConPorcentaje;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Credito;
import ec.com.redepronik.negosys.invfac.entity.DetalleCredito;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Garante;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;

@Controller
@Scope("session")
public class CreditoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private ClienteService clienteService;

	// /
	private List<Factura> listaFacturas;
	private String criterioBusquedaCliente;
	private String criterioBusquedaCodigo;
	// /

	// /
	private List<Persona> listaGarantesBusqueda;
	private String criterioBusqueda;
	// /

	private Factura factura;
	private Credito credito;
	private Date fechaInicio;
	private BigDecimal montoCredito;

	private List<Persona> listaGarantes;
	private Persona garante;

	private boolean bnCredito;
	private boolean bnGuardar;

	public CreditoBean() {
		limpiarObjetos();
	}

	public void actualizar(ActionEvent actionEvent) {
		if (listaGarantes != null && !listaGarantes.isEmpty()) {
			credito.setGarantes(new ArrayList<Garante>());
			int c = 1;
			for (Persona p : listaGarantes) {
				Garante g = new Garante();
				g.setCliente(p.getCliente());
				g.setOrden(c++);
				credito.addGarante(g);
			}
		}

		credito.setFactura(factura);
		factura.setCredito(credito);

		facturaService.actualizar(factura);
		presentaMensaje(
				FacesMessage.SEVERITY_INFO,
				"INSERTO CREDITO PARA LA FACTURA: "
						+ factura.getEstablecimiento() + "-"
						+ factura.getPuntoEmision() + "-"
						+ factura.getSecuencia());

		limpiarObjetos();
	}

	public void calcularCredito() {
		if (!comprobarCredito()) {
			credito.setMonto(redondearTotales(valorConPorcentaje(montoCredito,
					credito.getInteres())));

			BigDecimal mensual = redondearTotales(divide(credito.getMonto(),
					credito.getMeses()));
			BigDecimal suma = new BigDecimal("0");
			List<BigDecimal> list = new ArrayList<BigDecimal>();
			for (int i = 1; i <= credito.getMeses(); i++) {
				list.add(mensual);
				suma = suma.add(mensual);
			}
			BigDecimal centavos = new BigDecimal("0");
			if (suma.compareTo(credito.getMonto()) > 0) {
				centavos = redondearTotales(suma.subtract(credito.getMonto()));
				list.set(0, list.get(0).subtract(centavos));
			} else if (suma.compareTo(credito.getMonto()) < 0) {
				centavos = redondearTotales(credito.getMonto().subtract(suma));
				list.set(0, list.get(0).add(centavos));
			}

			Calendar calendar = new GregorianCalendar();
			calendar.setTime(fechaInicio);

			Calendar c = new GregorianCalendar();
			c.setTime(fechaInicio);

			credito.setDetalleCreditos(new ArrayList<DetalleCredito>());
			for (int i = 1; i <= credito.getMeses(); i++) {
				DetalleCredito detallescredito = new DetalleCredito();
				detallescredito.setOrden(i);
				calendar.add(Calendar.DATE, 30);
				detallescredito.setFechaLimite(calendar.getTime());
				detallescredito.setCuota(list.get(i - 1));
				detallescredito.setSaldo(detallescredito.getCuota());
				c.setTime(detallescredito.getFechaLimite());
				c.add(Calendar.DATE, 16);
				detallescredito.setFechaMora(c.getTime());
				detallescredito.setPagado(false);
				credito.addDetalleCredito(detallescredito);
			}

			credito.setPagado(false);

			bnGuardar = false;
		}
	}

	public void cancelarCredito() {
		credito = new Credito();
		fechaInicio = new Date();
		bnGuardar = true;
	}

	public void cargarFactura(SelectEvent event) {
		credito = new Credito();
		fechaInicio = new Date();

		listaGarantes = new ArrayList<Persona>();
		garante = new Persona();

		bnCredito = true;
		bnGuardar = true;

		factura = facturaService.obtenerPorFacturaId(factura.getId());
		montoCredito = montoCredito();
		bnCredito = false;
	}

	private boolean comprobarCredito() {
		if (credito.getMeses() <= 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"LOS MESES NO PUEDEN SER MENORES QUE 1");
			return true;
		} else if (credito.getInteres().compareTo(new BigDecimal("0")) < 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"EL INTERES NO PUEDE SER MENOR QUE CERO");
			return true;
		} else if (fechaInicio == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"TIENE QUE TENER UNA FECHA DE COMIENZO");
			return true;
		}
		return false;
	}

	public Credito getCredito() {
		return credito;
	}

	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public String getCriterioBusquedaCliente() {
		return criterioBusquedaCliente;
	}

	public String getCriterioBusquedaCodigo() {
		return criterioBusquedaCodigo;
	}

	public Factura getFactura() {
		return factura;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Persona getGarante() {
		return garante;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public List<Persona> getListaGarantes() {
		return listaGarantes;
	}

	public List<Persona> getListaGarantesBusqueda() {
		return listaGarantesBusqueda;
	}

	public BigDecimal getMontoCredito() {
		return montoCredito;
	}

	public boolean isBnCredito() {
		return bnCredito;
	}

	public boolean isBnGuardar() {
		return bnGuardar;
	}

	public void limpiarObjetos() {
		factura = new Factura();
		credito = new Credito();
		fechaInicio = new Date();
		montoCredito = newBigDecimal();

		listaGarantes = new ArrayList<Persona>();
		garante = new Persona();

		bnCredito = true;
		bnGuardar = true;
	}

	public void limpiarObjetosBusqueda() {
		criterioBusquedaCliente = new String();
		criterioBusquedaCodigo = new String();
		listaFacturas = new ArrayList<Factura>();
	}

	public void limpiarObjetosBusquedaGarante() {
		criterioBusqueda = new String();
		listaGarantesBusqueda = new ArrayList<Persona>();
	}

	private BigDecimal montoCredito() {
		BigDecimal totalFactura = facturaService.calcularTotal(factura);
		BigDecimal abonos = facturaService.calcularEntradas(factura);
		return redondearTotales(totalFactura.subtract(abonos));
	}

	public void obtener() {
		listaFacturas = facturaService.obtenerFacturasParaCredito(
				criterioBusquedaCliente, criterioBusquedaCodigo);
	}

	public void obtenerGarante(SelectEvent event) {
		listaGarantes.add(clienteService.obtenerPorPersonaId(garante.getId()));
	}

	public void obtenerGarantesProBusqueda() {
		if (criterioBusqueda.length() >= 0 && criterioBusqueda.length() <= 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 3 CARACTERES");
		else {
			listaGarantesBusqueda = clienteService.obtener(
					criterioBusqueda.toUpperCase(), 0);
			if (listaGarantesBusqueda.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
			criterioBusqueda = new String();
		}
	}

	public void setBnCredito(boolean bnCredito) {
		this.bnCredito = bnCredito;
	}

	public void setBnGuardar(boolean bnGuardar) {
		this.bnGuardar = bnGuardar;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public void setCriterioBusquedaCliente(String criterioBusquedaCliente) {
		this.criterioBusquedaCliente = criterioBusquedaCliente;
	}

	public void setCriterioBusquedaCodigo(String criterioBusquedaCodigo) {
		this.criterioBusquedaCodigo = criterioBusquedaCodigo;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setGarante(Persona garante) {
		this.garante = garante;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setListaGarantes(List<Persona> listaGarantes) {
		this.listaGarantes = listaGarantes;
	}

	public void setListaGarantesBusqueda(List<Persona> listaGarantesBusqueda) {
		this.listaGarantesBusqueda = listaGarantesBusqueda;
	}

	public void setMontoCredito(BigDecimal montoCredito) {
		this.montoCredito = montoCredito;
	}

}