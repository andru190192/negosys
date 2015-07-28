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

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Tarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.TarifaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.Impuestos;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.TotalConImpuestos;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.Detalles;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.Detalles.Detalle;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.FacturaE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.InfoFactura;

public class UtilsFactura {

	private static FacturaE armarFacturaXML(Factura factura, String cAcceso,
			CantidadFactura cf, Local local) {
		Persona p = UtilsFactura.personaService.obtenerPorPersonaId(factura
				.getClienteFactura().getPersona().getId());

		FacturaE facturaE = new FacturaE();
		InfoTributaria infoTributaria = infoTributaria(cAcceso, local);
		infoTributaria.setEstab(factura.getEstablecimiento());
		infoTributaria.setPtoEmi(factura.getPuntoEmision());
		infoTributaria.setSecuencial(factura.getSecuencia());
		facturaE.setInfoTributaria(infoTributaria);
		facturaE.setInfoFactura(infoFactura(factura, p, cf, local));
		facturaE.setDetalles(detalles(factura.getFechaInicio(),
				factura.getDetalleFactura()));
		facturaE.setInfoAdicional(infoAdicional(p));

		return facturaE;
	}

	private static Detalle detalle(String codigoPrincipal, String descripcion,
			int cantidad, BigDecimal precioUnitario, BigDecimal descuento,
			BigDecimal precioTotalSinImpuesto, List<Tarifa> impuestos) {

		Detalle detalle = new Detalle();
		detalle.setCodigoPrincipal(codigoPrincipal);
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

	private static Detalles detalles(Date fechaFactura,
			List<DetalleFactura> list) {
		Detalles detalles = new Detalles();
		for (DetalleFactura df : list)
			detalles.getDetalle().add(
					detalle(df.getProducto().getEan(), df.getProducto()
							.getNombre(), df.getCantidad(),
							df.getPrecioVenta(), df.getDescuento(),
							multiplicar(df.getPrecioVenta(), df.getCantidad())
									.subtract(df.getDescuento()), tarifaService
									.obtenerPorFechaProducto(fechaFactura, df
											.getProducto().getId())));
		return detalles;
	}

	public static FacturaE facturaXML(Factura factura, CantidadFactura cf,
			String claveAcceso, LocalService localService,
			TarifaService tarifaService, PersonaService personaService) {
		UtilsFactura.tarifaService = tarifaService;
		UtilsFactura.localService = localService;
		UtilsFactura.personaService = personaService;
		Local local = UtilsFactura.localService
				.obtenerPorEstablecimiento(factura.getEstablecimiento());
		return armarFacturaXML(factura, claveAcceso, cf, local);
	}

	private static InfoFactura infoFactura(Factura f, Persona p,
			CantidadFactura cf, Local local) {
		InfoFactura infoFactura = new InfoFactura();
		infoFactura.setFechaEmision(fechaFormatoString(f.getFechaInicio()));
		infoFactura.setDirEstablecimiento(local.getDireccion());

		if (parametro.getCodigoContribuyente() != null
				&& parametro.getCodigoContribuyente().compareToIgnoreCase("") != 0)
			infoFactura.setContribuyenteEspecial(parametro
					.getCodigoContribuyente());

		infoFactura.setObligadoContabilidad(parametro.getContabilidad() ? "SI"
				: "NO");

		infoFactura
				.setTipoIdentificacionComprador(tipoIdentificacionComprador(p
						.getCedula()));
		infoFactura
				.setRazonSocialComprador(p.getCliente().getNombreComercial());
		infoFactura.setIdentificacionComprador(p.getCedula());
		infoFactura
				.setTotalSinImpuestos(redondearTotales(cf.getStSinImpuesto()));
		infoFactura.setTotalDescuento(redondearTotales(cf.gettDescuento()));

		TotalConImpuestos totalConImpuestos = new TotalConImpuestos();

		BigDecimal ice = totalImpuestoIce(totalConImpuestos,
				f.getDetalleFactura(), date(f.getFechaInicio()));

		if (compareTo(cf.getSt0(), 0) > 0)
			totalConImpuestos.getTotalImpuesto().add(
					totalImpuesto("2", "0", cf.getSt0(), newBigDecimal("0.00"),
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

		infoFactura.setTotalConImpuestos(totalConImpuestos);

		infoFactura.setPropina(redondearTotales(cf.getPropina()));
		infoFactura.setImporteTotal(redondearTotales(cf.getValorTotal()));
		infoFactura.setMoneda("DOLAR");
		return infoFactura;
	}

	private static BigDecimal totalImpuestoIce(
			TotalConImpuestos totalConImpuestos, List<DetalleFactura> list,
			Date fechaFactura) {
		BigDecimal valorIce = newBigDecimal();
		String[][] ice = new String[list.size()][3];
		int i = 0;
		for (DetalleFactura de : list) {
			Tarifa t = tarifaService.obtenerPorFechaProducto(fechaFactura, de
					.getProducto().getId(),
					ec.com.redepronik.negosys.invfac.entity.Impuesto.IC);
			if (t != null) {
				ice[i][0] = String.valueOf(t.getIdSri());
				ice[i][1] = String.valueOf(multiplicar(de.getPrecioVenta(),
						de.getCantidad()));
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
