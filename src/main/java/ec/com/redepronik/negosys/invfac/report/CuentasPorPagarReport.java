package ec.com.redepronik.negosys.invfac.report;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entityAux.ChequeReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CobroFacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CobroFacturaReporteAuxiliar;
import ec.com.redepronik.negosys.invfac.service.IngresoService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;
import ec.com.redepronik.negosys.utils.service.ReporteService;

@Controller
public class CuentasPorPagarReport {

	@Autowired
	private ReporteService reporteService;

	@Autowired
	private IngresoService ingresoService;

	@Autowired
	private ProveedorService proveedorService;

	private Date fechaInicio;
	private Date fechaFin;

	public CuentasPorPagarReport() {
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void reporteChequesPorPagar(ActionEvent actionEvent) {
		List<ChequeReporte> list = ingresoService.obtenerChequesPorFechas(
				fechaInicio, fechaFin);
		Map<String, Object> parametro = new HashMap<String, Object>();
		reporteService.generarReportePDF(list, parametro, "ChequesPorPagar");
	}

	public void reportePagoFactura(ActionEvent actionEvent) {
		boolean bn = false;
		List<CobroFacturaReporte> list = new ArrayList<CobroFacturaReporte>();
		BigDecimal totalTotal = newBigDecimal();
		for (Persona persona : proveedorService
				.obtenerProveedoresPorFacturasPendientes()) {
			bn = true;
			List<CobroFacturaReporteAuxiliar> list1 = new ArrayList<CobroFacturaReporteAuxiliar>();
			for (Ingreso ingreso : ingresoService.obtenerIngresosNoPagados(
					persona.getCedula(), "", null, null)) {
				BigDecimal tIngreso = ingresoService.calcularTotal(ingreso);
				BigDecimal aIngreso = ingresoService.calcularEntradas(ingreso);
				BigDecimal total = redondearTotales(tIngreso.subtract(aIngreso));

				totalTotal = totalTotal.add(total);
				list1.add(new CobroFacturaReporteAuxiliar(ingreso
						.getFechaFactura(), ingreso.getCodigoDocumento(), total));
			}
			list.add(new CobroFacturaReporte(persona.getProveedor().getFolio(),
					persona.getCedula() + " - " + persona.getApellido() + " "
							+ persona.getNombre(), persona.getDireccion(),
					persona.getReferencia(), list1));
		}
		if (bn) {
			Map<String, Object> parametro = new HashMap<String, Object>();
			parametro.put("total", redondearTotales(totalTotal));
			reporteService.generarReportePDF(list, parametro, "PagoFactura");
		} else
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO TIENE FACTURAS POR PAGAR");
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

}