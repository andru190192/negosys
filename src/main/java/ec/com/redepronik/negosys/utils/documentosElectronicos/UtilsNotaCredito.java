package ec.com.redepronik.negosys.utils.documentosElectronicos;

import static ec.com.redepronik.negosys.utils.UtilsDate.date;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoString;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.impuesto;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.infoAdicional;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.infoTributaria;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.tipoIdentificacionComprador;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.totalImpuesto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ec.com.redepronik.negosys.invfac.entity.DetalleNotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Tarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.TarifaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.Impuestos;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.TotalConImpuestos;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.Detalles;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.Detalles.Detalle;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.InfoNotaCredito;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.NotaCreditoE;

public class UtilsNotaCredito {

	private static NotaCreditoE armarNotaCreditoXML(NotaCredito notaCredito,
			String cAcceso, CantidadFactura cf, Local local) {
		Persona p = UtilsNotaCredito.personaService
				.obtenerPorPersonaId(notaCredito.getFactura()
						.getClienteFactura().getPersona().getId());
		NotaCreditoE notaCreditoE = new NotaCreditoE();
		InfoTributaria infoTributaria = infoTributaria(cAcceso, local);
		infoTributaria.setEstab(notaCredito.getEstablecimiento());
		infoTributaria.setPtoEmi(notaCredito.getPuntoEmision());
		infoTributaria.setSecuencial(notaCredito.getSecuencia());
		notaCreditoE.setInfoTributaria(infoTributaria);
		notaCreditoE.setInfoNotaCredito(infoNotaCredito(notaCredito, p, cf,
				local));
		notaCreditoE.setDetalles(detalles(notaCredito.getFecha(),
				notaCredito.getDetalleNotaCredito()));
		notaCreditoE.setInfoAdicional(infoAdicional(p));

		return notaCreditoE;
	}

	private static Detalle detalle(String codigoPrincipal, String descripcion,
			int cantidad, BigDecimal precioUnitario, BigDecimal descuento,
			BigDecimal precioTotalSinImpuesto, List<Tarifa> impuestos) {

		Detalle detalle = new Detalle();
		detalle.setCodigoInterno(codigoPrincipal);
		detalle.setDescripcion(descripcion);
		detalle.setCantidad(newBigDecimal(cantidad));
		detalle.setPrecioUnitario(redondearTotales(precioUnitario));
		detalle.setDescuento(redondearTotales(descuento));
		detalle.setPrecioTotalSinImpuesto(redondearTotales(precioTotalSinImpuesto));

		BigDecimal ice = newBigDecimal();
		Impuestos i = new Impuestos();

		for (Tarifa t : impuestos)
			if (t.getImpuesto() == ec.com.redepronik.negosys.invfac.entity.Impuesto.IC)
				ice = multiplicarDivide(precioTotalSinImpuesto,
						t.getPorcentaje());

		for (Tarifa t : impuestos) {
			if (t.getImpuesto() == ec.com.redepronik.negosys.invfac.entity.Impuesto.IV)
				i.getImpuesto().add(
						impuesto(
								t.getImpuesto().getId(),
								t.getIdSri(),
								t.getPorcentaje(),
								precioTotalSinImpuesto.add(ice),
								multiplicarDivide(
										precioTotalSinImpuesto.add(ice),
										t.getPorcentaje())));
			else
				i.getImpuesto().add(
						impuesto(
								t.getImpuesto().getId(),
								t.getIdSri(),
								t.getPorcentaje(),
								precioTotalSinImpuesto,
								multiplicarDivide(precioTotalSinImpuesto,
										t.getPorcentaje())));
		}
		detalle.setImpuestos(i);
		return detalle;
	}

	private static Detalles detalles(Date fechaNotaCredito,
			List<DetalleNotaCredito> list) {
		Detalles detalles = new Detalles();
		for (DetalleNotaCredito dnc : list)
			detalles.getDetalle()
					.add(detalle(
							dnc.getProducto().getEan(),
							dnc.getProducto().getNombre(),
							dnc.getCantidad(),
							dnc.getPrecioVenta(),
							dnc.getDescuento(),
							multiplicar(dnc.getPrecioVenta(), dnc.getCantidad())
									.subtract(dnc.getDescuento()),
							tarifaService
									.obtenerPorFechaProducto(fechaNotaCredito,
											dnc.getProducto().getId())));
		return detalles;
	}

	private static InfoNotaCredito infoNotaCredito(NotaCredito nc, Persona p,
			CantidadFactura cf, Local local) {
		InfoNotaCredito infoNotaCredito = new InfoNotaCredito();
		infoNotaCredito.setFechaEmision(fechaFormatoString(nc.getFecha()));
		infoNotaCredito.setDirEstablecimiento(local.getDireccion());

		if (parametro.getCodigoContribuyente() != null
				&& parametro.getCodigoContribuyente().compareToIgnoreCase("") != 0)
			infoNotaCredito.setContribuyenteEspecial(parametro
					.getCodigoContribuyente());

		infoNotaCredito
				.setObligadoContabilidad(parametro.getContabilidad() ? "SI"
						: "NO");

		infoNotaCredito
				.setTipoIdentificacionComprador(tipoIdentificacionComprador(p
						.getCedula()));
		infoNotaCredito.setRazonSocialComprador(p.getCliente()
				.getNombreComercial());
		infoNotaCredito.setIdentificacionComprador(p.getCedula());
		infoNotaCredito.setCodDocModificado("01");
		infoNotaCredito.setNumDocModificado(nc.getFactura()
				.getEstablecimiento()
				+ "-"
				+ nc.getFactura().getPuntoEmision()
				+ "-" + nc.getFactura().getSecuencia());
		infoNotaCredito.setFechaEmisionDocSustento(fechaFormatoString(nc
				.getFactura().getFechaInicio()));
		infoNotaCredito.setTotalSinImpuestos(redondearTotales(cf
				.getStSinImpuesto()));
		infoNotaCredito.setValorModificacion(redondearTotales(cf
				.getValorTotal()));
		infoNotaCredito.setMoneda("DOLAR");

		TotalConImpuestos totalConImpuestos = new TotalConImpuestos();

		BigDecimal ice = totalImpuestoIce(totalConImpuestos,
				nc.getDetalleNotaCredito(), date(nc.getFecha()));

		if (compareTo(cf.getSt0(), 0) > 0)
			totalConImpuestos.getTotalImpuesto().add(
					totalImpuesto("2", "0", cf.getSt0(), null,
							newBigDecimal("0.00")));
		if (compareTo(cf.getSt12(), 0) > 0)
			totalConImpuestos.getTotalImpuesto().add(
					totalImpuesto("2", "2", cf.getSt12().add(ice), null,
							iva(cf.getSt12().add(ice), 12)));
		if (compareTo(cf.getStNoObjetoIva(), 0) > 0)
			totalConImpuestos.getTotalImpuesto().add(
					totalImpuesto("2", "6", cf.getStNoObjetoIva(), null,
							newBigDecimal("0.00")));
		if (compareTo(cf.getStExentoIva(), 0) > 0)
			totalConImpuestos.getTotalImpuesto().add(
					totalImpuesto("2", "7", cf.getStExentoIva(), null,
							newBigDecimal("0.00")));

		if (compareTo(cf.getValorIRBPNR(), 0) > 0)
			totalConImpuestos.getTotalImpuesto().add(
					totalImpuesto("5", "5", cf.getValorIRBPNR(), null,
							cf.getValorIRBPNR()));

		infoNotaCredito.setTotalConImpuestos(totalConImpuestos);

		infoNotaCredito.setMotivo("DEVOLUCION");
		return infoNotaCredito;
	}

	public static NotaCreditoE notaCreditoXML(NotaCredito notaCredito,
			CantidadFactura cf, String claveAcceso, LocalService localService,
			TarifaService tarifaService, PersonaService personaService) {
		UtilsNotaCredito.tarifaService = tarifaService;
		UtilsNotaCredito.localService = localService;
		UtilsNotaCredito.personaService = personaService;
		Local local = UtilsNotaCredito.localService
				.obtenerPorEstablecimiento(notaCredito.getEstablecimiento());
		return armarNotaCreditoXML(notaCredito, claveAcceso, cf, local);
	}

	private static BigDecimal totalImpuestoIce(
			TotalConImpuestos totalConImpuestos, List<DetalleNotaCredito> list,
			Date fechaNotaCredito) {
		BigDecimal valorIce = newBigDecimal();
		String[][] ice = new String[list.size()][3];
		int i = 0;
		for (DetalleNotaCredito dnc : list) {
			Tarifa t = tarifaService.obtenerPorFechaProducto(fechaNotaCredito,
					dnc.getProducto().getId(),
					ec.com.redepronik.negosys.invfac.entity.Impuesto.IC);
			if (t != null) {
				ice[i][0] = String.valueOf(t.getIdSri());
				ice[i][1] = String.valueOf(multiplicar(dnc.getPrecioVenta(),
						dnc.getCantidad()));
				ice[i][2] = String.valueOf(t.getPorcentaje());
			}
			i++;
		}
		for (i = 0; i < ice.length; i++) {
			for (int j = i + 1; j < ice.length; j++) {
				if (ice[i][0] != null && ice[j][0] != null
						&& ice[i][0].compareTo(ice[j][0]) == 0) {
					ice[i][1] = String.valueOf(newBigDecimal(ice[i][1]).add(
							newBigDecimal(ice[j][1])));
					ice[j][0] = null;
					ice[j][1] = null;
				}
			}
		}

		for (i = 0; i < ice.length; i++) {
			if (ice[i][0] != null) {
				BigDecimal valor = multiplicarDivide(newBigDecimal(ice[i][1]),
						ice[i][2]);
				totalConImpuestos.getTotalImpuesto().add(
						totalImpuesto("3", ice[i][0], newBigDecimal(ice[i][1]),
								null, valor));
				valorIce = valorIce.add(valor);
			}
		}
		return valorIce;
	}

	private static TarifaService tarifaService;

	private static LocalService localService;

	private static PersonaService personaService;

}
