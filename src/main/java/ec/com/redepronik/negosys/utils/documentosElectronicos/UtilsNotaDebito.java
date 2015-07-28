package ec.com.redepronik.negosys.utils.documentosElectronicos;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.matriz;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIGenerados;
import static ec.com.redepronik.negosys.utils.UtilsDate.date;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoString;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.iva;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotalS;
import static ec.com.redepronik.negosys.utils.UtilsXML.guardarXML;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.claveAcceso;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.crearDocumentoXML;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Tarifa;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.TarifaService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

public class UtilsNotaDebito {

	private static Document armarFacturaXML(String nombre, Factura factura,
			String cAcceso, CantidadFactura cf, Local local) {
		Map<String, Object> map = crearDocumentoXML(nombre);
		Document document = (Document) map.get("Document");
		Element raiz = (Element) map.get("Element");

		Persona p = factura.getClienteFactura().getPersona();
		raiz.appendChild(infoTributaria(document, factura, cAcceso, local));
		raiz.appendChild(infoFactura(document, factura, p, cf, local));
		raiz.appendChild(detalles(document, factura.getFechaInicio(),
				factura.getDetalleFactura()));
		raiz.appendChild(infoAdicional(document, p));
		return document;
	}

	private static Element campoAdicional(Document document, String nombre,
			String valor) {
		Element campoAdicional = document.createElement("campoAdicional");
		campoAdicional.setAttribute("nombre", nombre);
		campoAdicional.appendChild(document.createTextNode(valor));
		return campoAdicional;
	}

	private static Element detalle(Document document, String codigoPrincipal,
			String descripcion, String cantidad, String precioUnitario,
			String descuento, String precioTotalSinImpuesto,
			List<Tarifa> impuestos) {
		Element detalle = document.createElement("detalle");

		Element cp = document.createElement("codigoPrincipal");
		cp.appendChild(document.createTextNode(codigoPrincipal));
		detalle.appendChild(cp);

		Element d = document.createElement("descripcion");
		d.appendChild(document.createTextNode(descripcion));
		detalle.appendChild(d);

		Element c = document.createElement("cantidad");
		c.appendChild(document.createTextNode(cantidad));
		detalle.appendChild(c);

		Element pu = document.createElement("precioUnitario");
		pu.appendChild(document.createTextNode(precioUnitario));
		detalle.appendChild(pu);

		Element de = document.createElement("descuento");
		de.appendChild(document.createTextNode(descuento));
		detalle.appendChild(de);

		Element ptsi = document.createElement("precioTotalSinImpuesto");
		ptsi.appendChild(document.createTextNode(precioTotalSinImpuesto));
		detalle.appendChild(ptsi);

		BigDecimal ice = newBigDecimal();
		Element i = document.createElement("impuestos");
		for (Tarifa t : impuestos)
			if (t.getImpuesto() == Impuesto.IC)
				ice = multiplicarDivide(newBigDecimal(precioTotalSinImpuesto),
						t.getPorcentaje());

		for (Tarifa t : impuestos) {
			if (t.getImpuesto() == Impuesto.IV)
				i.appendChild(impuesto(document, String.valueOf(t.getImpuesto()
						.getId()), String.valueOf(t.getIdSri()), String
						.valueOf(t.getPorcentaje()),
						String.valueOf(newBigDecimal(precioTotalSinImpuesto)
								.add(ice)), String.valueOf(multiplicarDivide(
								newBigDecimal(precioTotalSinImpuesto).add(ice),
								t.getPorcentaje()))));
			else
				i.appendChild(impuesto(document, String.valueOf(t.getImpuesto()
						.getId()), String.valueOf(t.getIdSri()), String
						.valueOf(t.getPorcentaje()), precioTotalSinImpuesto,
						String.valueOf(multiplicarDivide(
								newBigDecimal(precioTotalSinImpuesto),
								t.getPorcentaje()))));
		}
		detalle.appendChild(i);

		return detalle;
	}

	private static Element detalles(Document document, Date fechaFactura,
			List<DetalleFactura> list) {
		Element detalles = document.createElement("detalles");
		for (DetalleFactura df : list) {
			detalles.appendChild(detalle(document, df.getProducto().getEan(),
					df.getProducto().getNombre(), String.valueOf(df
							.getCantidad()),
					String.valueOf(df.getPrecioVenta()), String.valueOf(df
							.getDescuento()), String.valueOf(multiplicar(
							df.getPrecioVenta(), df.getCantidad()).subtract(
							df.getDescuento())), tarifaService
							.obtenerPorFechaProducto(fechaFactura, df
									.getProducto().getId())));
		}
		return detalles;
	}

	public static void generarFacturaXML(Factura factura, CantidadFactura cf,
			LocalService localService, TarifaService tarifaService) {
		UtilsNotaDebito.tarifaService = tarifaService;
		UtilsNotaDebito.localService = localService;
		String claveAcceso = claveAcceso(factura.getFechaInicio(), "01",
				factura.getEstablecimiento() + factura.getPuntoEmision(),
				factura.getSecuencia());
		Local local = UtilsNotaDebito.localService
				.obtenerPorEstablecimiento(factura.getEstablecimiento());
		guardarXML(armarFacturaXML("factura", factura, claveAcceso, cf, local),
				getRutaSRIGenerados() + claveAcceso + ".xml");
	}

	private static Element impuesto(Document document, String codigo,
			String codigoPorcentaje, String tarifa, String baseImponible,
			String valor) {
		Element impuesto = document.createElement("impuesto");

		Element cod = document.createElement("codigo");
		cod.appendChild(document.createTextNode(codigo));
		impuesto.appendChild(cod);
		Element cp = document.createElement("codigoPorcentaje");
		cp.appendChild(document.createTextNode(codigoPorcentaje));
		impuesto.appendChild(cp);
		Element t = document.createElement("tarifa");
		t.appendChild(document.createTextNode(tarifa));
		impuesto.appendChild(t);
		Element bi = document.createElement("baseImponible");
		bi.appendChild(document.createTextNode(baseImponible));
		impuesto.appendChild(bi);
		Element v = document.createElement("valor");
		v.appendChild(document.createTextNode(valor));
		impuesto.appendChild(v);

		return impuesto;
	}

	private static Element infoAdicional(Document document, Persona p) {
		Element infoAdicional = document.createElement("infoAdicional");

		infoAdicional.appendChild(campoAdicional(document, "Dirección",
				p.getDireccion()));
		if (p.getEmail() != null)
			infoAdicional.appendChild(campoAdicional(document, "Email",
					p.getEmail()));

		return infoAdicional;
	}

	private static Element infoFactura(Document document, Factura f, Persona p,
			CantidadFactura cf, Local local) {
		Element infoFactura = document.createElement("infoFactura");

		Element fechaEmision = document.createElement("fechaEmision");
		fechaEmision.appendChild(document.createTextNode(fechaFormatoString(f
				.getFechaInicio())));
		infoFactura.appendChild(fechaEmision);

		Element dirEstablecimiento = document
				.createElement("dirEstablecimiento");
		dirEstablecimiento.appendChild(document.createTextNode(local
				.getDireccion()));
		infoFactura.appendChild(dirEstablecimiento);

		Element contribuyenteEspecial = document
				.createElement("contribuyenteEspecial");
		contribuyenteEspecial.appendChild(document.createTextNode(parametro
				.getCodigoContribuyente()));
		infoFactura.appendChild(contribuyenteEspecial);

		Element obligadoContabilidad = document
				.createElement("obligadoContabilidad");
		obligadoContabilidad.appendChild(document.createTextNode(parametro
				.getContabilidad() ? "SI" : "NO"));
		infoFactura.appendChild(obligadoContabilidad);

		String tic = "";
		if (p.getCedula().length() == 13) {
			if (p.getCedula().compareToIgnoreCase("9999999999999") == 0)
				tic = "07";
			else
				tic = "04";
		} else if (p.getCedula().length() == 10)
			tic = "05";
		Element tipoIdentificacionComprador = document
				.createElement("tipoIdentificacionComprador");
		tipoIdentificacionComprador.appendChild(document.createTextNode(tic));
		infoFactura.appendChild(tipoIdentificacionComprador);

		Element razonSocialComprador = document
				.createElement("razonSocialComprador");
		razonSocialComprador.appendChild(document.createTextNode(p
				.getApellido() + " " + p.getNombre()));
		infoFactura.appendChild(razonSocialComprador);

		Element identificacionComprador = document
				.createElement("identificacionComprador");
		identificacionComprador.appendChild(document.createTextNode(p
				.getCedula()));
		infoFactura.appendChild(identificacionComprador);

		Element totalSinImpuestos = document.createElement("totalSinImpuestos");
		totalSinImpuestos.appendChild(document
				.createTextNode(redondearTotalS(cf.getStSinImpuesto())));
		infoFactura.appendChild(totalSinImpuestos);

		Element totalDescuento = document.createElement("totalDescuento");
		totalDescuento.appendChild(document.createTextNode(redondearTotalS(cf
				.gettDescuento())));
		infoFactura.appendChild(totalDescuento);

		Element totalConImpuestos = document.createElement("totalConImpuestos");

		BigDecimal ice = totalImpuesto(document, totalConImpuestos,
				f.getDetalleFactura(), date(f.getFechaInicio()));

		if (compareTo(cf.getSt0(), 0) > 0)
			totalConImpuestos.appendChild(totalImpuesto(document, "2", "0",
					"0.00", redondearTotalS(cf.getSt0()), "0.00"));
		if (compareTo(cf.getSt12(), 0) > 0)
			totalConImpuestos.appendChild(totalImpuesto(document, "2", "2",
					"0.00", redondearTotalS(cf.getSt12().add(ice)),
					redondearTotalS(iva(cf.getSt12().add(ice), 12))));
		if (compareTo(cf.getStNoObjetoIva(), 0) > 0)
			totalConImpuestos.appendChild(totalImpuesto(document, "2", "6",
					"0.00", redondearTotalS(cf.getStNoObjetoIva()), "0.00"));
		if (compareTo(cf.getStExentoIva(), 0) > 0)
			totalConImpuestos.appendChild(totalImpuesto(document, "2", "7",
					"0.00", redondearTotalS(cf.getStExentoIva()), "0.00"));

		if (compareTo(cf.getValorIRBPNR(), 0) > 0)
			totalConImpuestos.appendChild(totalImpuesto(document, "5", "5", "",
					redondearTotalS(cf.getValorIRBPNR()),
					redondearTotalS(cf.getValorIRBPNR())));

		infoFactura.appendChild(totalConImpuestos);

		Element propina = document.createElement("propina");
		propina.appendChild(document.createTextNode(redondearTotalS(cf
				.getPropina())));
		infoFactura.appendChild(propina);

		Element importeTotal = document.createElement("importeTotal");
		importeTotal.appendChild(document.createTextNode(redondearTotalS(cf
				.getValorTotal())));
		infoFactura.appendChild(importeTotal);

		Element moneda = document.createElement("moneda");
		moneda.appendChild(document.createTextNode("DOLAR"));
		infoFactura.appendChild(moneda);

		return infoFactura;
	}

	private static Element infoTributaria(Document document, Factura f,
			String cAcceso, Local local) {
		Element infoTributaria = document.createElement("infoTributaria");

		Element ambiente = document.createElement("ambiente");
		ambiente.appendChild(document.createTextNode(parametro
				.getTipoAmbiente() ? "1" : "0"));
		infoTributaria.appendChild(ambiente);

		Element tipoEmision = document.createElement("tipoEmision");
		tipoEmision.appendChild(document.createTextNode(parametro
				.getTipoEmision() ? "1" : "0"));
		infoTributaria.appendChild(tipoEmision);

		Element razonSocial = document.createElement("razonSocial");
		razonSocial.appendChild(document.createTextNode(parametro
				.getRazonSocial()));
		infoTributaria.appendChild(razonSocial);

		Element nombreComercial = document.createElement("nombreComercial");
		nombreComercial.appendChild(document.createTextNode(local.getNombre()));
		infoTributaria.appendChild(nombreComercial);

		Element ruc = document.createElement("ruc");
		ruc.appendChild(document.createTextNode(parametro.getRuc()));
		infoTributaria.appendChild(ruc);

		Element claveAcceso = document.createElement("claveAcceso");
		claveAcceso.appendChild(document.createTextNode(cAcceso));
		infoTributaria.appendChild(claveAcceso);

		Element codDoc = document.createElement("codDoc");
		codDoc.appendChild(document.createTextNode("01"));
		infoTributaria.appendChild(codDoc);

		Element estab = document.createElement("estab");
		estab.appendChild(document.createTextNode(f.getEstablecimiento()));
		infoTributaria.appendChild(estab);

		Element ptoEmi = document.createElement("ptoEmi");
		ptoEmi.appendChild(document.createTextNode(f.getPuntoEmision()));
		infoTributaria.appendChild(ptoEmi);

		Element secuencial = document.createElement("secuencial");
		secuencial.appendChild(document.createTextNode(f.getSecuencia()));
		infoTributaria.appendChild(secuencial);

		Element dirMatriz = document.createElement("dirMatriz");
		dirMatriz.appendChild(document.createTextNode(matriz.getDireccion()));
		infoTributaria.appendChild(dirMatriz);

		return infoTributaria;
	}

	private static BigDecimal totalImpuesto(Document document,
			Element totalConImpuestos, List<DetalleFactura> list,
			Date fechaFactura) {
		BigDecimal valorIce = newBigDecimal();
		String[][] ice = new String[list.size()][3];
		int i = 0;
		for (DetalleFactura de : list) {
			Tarifa t = tarifaService.obtenerPorFechaProducto(fechaFactura, de
					.getProducto().getId(), Impuesto.IC);
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
				Element totalImpuesto = document.createElement("totalImpuesto");

				Element cod = document.createElement("codigo");
				cod.appendChild(document.createTextNode("3"));
				totalImpuesto.appendChild(cod);
				Element cp = document.createElement("codigoPorcentaje");
				cp.appendChild(document.createTextNode(ice[i][0]));
				totalImpuesto.appendChild(cp);
				Element bi = document.createElement("baseImponible");
				bi.appendChild(document.createTextNode(ice[i][1]));
				totalImpuesto.appendChild(bi);
				Element v = document.createElement("valor");
				v.appendChild(document.createTextNode(String.valueOf(valor)));
				totalImpuesto.appendChild(v);

				totalConImpuestos.appendChild(totalImpuesto);
				valorIce = valorIce.add(valor);
			}
		}
		return valorIce;
	}

	private static Element totalImpuesto(Document document, String codigo,
			String codigoPorcentaje, String descuentoAdicional,
			String baseImponible, String valor) {

		Element totalImpuesto = document.createElement("totalImpuesto");

		Element cod = document.createElement("codigo");
		cod.appendChild(document.createTextNode(codigo));
		totalImpuesto.appendChild(cod);
		Element cp = document.createElement("codigoPorcentaje");
		cp.appendChild(document.createTextNode(codigoPorcentaje));
		totalImpuesto.appendChild(cp);
		if (codigo.compareToIgnoreCase("2") == 0) {
			Element da = document.createElement("descuentoAdicional");
			da.appendChild(document.createTextNode(descuentoAdicional));
			totalImpuesto.appendChild(da);
		}
		Element bi = document.createElement("baseImponible");
		bi.appendChild(document.createTextNode(baseImponible));
		totalImpuesto.appendChild(bi);
		Element v = document.createElement("valor");
		v.appendChild(document.createTextNode(valor));
		totalImpuesto.appendChild(v);

		return totalImpuesto;
	}

	private static TarifaService tarifaService;

	private static LocalService localService;

}
