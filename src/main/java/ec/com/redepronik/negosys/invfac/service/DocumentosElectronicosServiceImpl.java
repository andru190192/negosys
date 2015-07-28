package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.Utils.subString;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.convertir;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.convertirString;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.eliminarArchivo;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIAutorizados;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIAutorizadosNoSubidos;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIFirmados;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIFirmadosRechazados;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIFirmadosTransmitidosSinRespuesta;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRIGenerados;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaSRINoAutorizados;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getlistArchivos;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.leerArchivo;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.moverArchivo;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoString;
import static ec.com.redepronik.negosys.utils.UtilsMail.enviarCorreo;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsWebService.almacenamiento;
import static ec.com.redepronik.negosys.utils.UtilsWebService.autorizacionComprobante;
import static ec.com.redepronik.negosys.utils.UtilsWebService.validarComprobante;
import static ec.com.redepronik.negosys.utils.UtilsXML.guardarXML;
import static ec.com.redepronik.negosys.utils.UtilsXML.guardarXMLFirmado;
import static ec.com.redepronik.negosys.utils.UtilsXML.objetoTOxml;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.claveAcceso;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.convertirStringADocument;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.hilo;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.intTipoDocumento;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.obtenerCedulaCliente;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.tipoDocumento;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.tipoIdentificacionComprador;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsFactura.facturaXML;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsFacturaRide.facturaRIDE;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsFirmaDigital.firmar;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsNotaCredito.notaCreditoXML;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsNotaCreditoRide.notaCreditoRIDE;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsRetencion.retencionXML;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsRetencionRide.retencionRIDE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.NotaCredito;
import ec.com.redepronik.negosys.invfac.entity.Retencion;
import ec.com.redepronik.negosys.invfac.entityAux.Anio;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.DocumentoElectronico;
import ec.com.redepronik.negosys.invfac.entityAux.Mes;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.FacturaReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.NotaCreditoReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.RetencionReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.FacturaE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.NotaCreditoE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.ComprobanteRetencionE;
import ec.com.redepronik.negosys.utils.service.ReporteService;
import ec.com.redepronik.negosys.wsOtros.sri.autorizacion.Autorizacion;
import ec.com.redepronik.negosys.wsOtros.sri.autorizacion.RespuestaComprobante;
import ec.com.redepronik.negosys.wsOtros.sri.recepcion.Comprobante;
import ec.com.redepronik.negosys.wsOtros.sri.recepcion.RespuestaSolicitud;

@Service
public class DocumentosElectronicosServiceImpl implements
		DocumentosElectronicosService {

	private static String obtieneComprobantes(RespuestaSolicitud rs) {
		StringBuilder mensaje = new StringBuilder();
		mensaje.append("<ns2:respuestaSolicitud xmlns:ns2=\"http://ec.gob.sri.ws.recepcion\">");
		mensaje.append("\n<estado>" + rs.getEstado() + "</estado>");
		mensaje.append("\n<comprobantes>");
		for (Comprobante c : rs.getComprobantes().getComprobante()) {
			mensaje.append("\n<comprobante>");
			mensaje.append("\n<claveAcceso>" + c.getClaveAcceso()
					+ "</claveAcceso>");
			mensaje.append("\n<mensajes>");
			mensaje.append(obtieneMensajesComprobante(c));
			mensaje.append("\n</mensajes>");
			mensaje.append("\n</comprobante>");

		}
		mensaje.append("\n</comprobantes>");
		mensaje.append("\n</ns2:respuestaSolicitud>");
		return mensaje.toString();
	}

	private static String obtieneMensajesComprobante(Comprobante c) {
		StringBuilder mensaje = new StringBuilder();
		for (ec.com.redepronik.negosys.wsOtros.sri.recepcion.Mensaje m : c
				.getMensajes().getMensaje()) {
			mensaje.append("\n<mensaje>");
			mensaje.append("\n<identificador>" + m.getIdentificador()
					+ "</identificador>");
			mensaje.append("\n<mensaje>" + m.getMensaje() + "</mensaje>");
			mensaje.append("\n<informacionAdicional>"
					+ m.getInformacionAdicional() + "</informacionAdicional>");
			mensaje.append("\n<tipo>" + m.getTipo() + "</tipo>");
			mensaje.append("\n</mensaje>");
		}
		return mensaje.toString();
	}

	private static String obtienerMensajesAutorizacion(Autorizacion aut) {
		StringBuilder mensaje = new StringBuilder();
		mensaje.append("\n<mensajes>\n<mensaje>");
		for (ec.com.redepronik.negosys.wsOtros.sri.autorizacion.Mensaje m : aut
				.getMensajes().getMensaje()) {
			mensaje.append("\n<mensaje>");
			mensaje.append("\n<identificador>" + m.getIdentificador()
					+ "</identificador>");
			mensaje.append("\n<mensaje>" + m.getMensaje() + "</mensaje>");
			if (m.getInformacionAdicional() != null)
				mensaje.append("\n<informacionAdicional>"
						+ m.getInformacionAdicional()
						+ "</informacionAdicional>");
			mensaje.append("\n<tipo>" + m.getTipo() + "</tipo>");
			mensaje.append("\n</mensaje>");
		}
		mensaje.append("\n</mensaje>\n</mensajes>");
		return mensaje.toString();
	}

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private NotaCreditoService notaCreditoService;

	@Autowired
	private RetencionService retencionService;

	@Autowired
	private TarifaService tarifaService;

	@Autowired
	private LocalService localService;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private ReporteService reporteService;

	public void autorizarXML(boolean presentaMensaje, boolean normal,
			DocumentoElectronico docElec) {
		String ruta = normal ? getRutaSRIFirmados()
				: getRutaSRIFirmadosTransmitidosSinRespuesta();
		RespuestaComprobante rc = null;
		for (int i = 0; i < 5; i++) {
			rc = autorizacionComprobante(subString(
					docElec.getNombreDocumento(), ".xml"));
			if (rc != null
					&& !rc.getAutorizaciones().getAutorizacion().isEmpty())
				break;
			hilo();
		}
		if (!rc.getAutorizaciones().getAutorizacion().isEmpty()) {
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<autorizacion>");
			for (Autorizacion aut : rc.getAutorizaciones().getAutorizacion()) {
				if (aut.getEstado().equals("AUTORIZADO")) {
					xml.append("<estado>" + aut.getEstado() + "</estado>");
					xml.append("<numeroAutorizacion>"
							+ aut.getNumeroAutorizacion()
							+ "</numeroAutorizacion>");
					xml.append("<fechaAutorizacion class=\"fechaAutorizacion\">"
							+ aut.getFechaAutorizacion()
							+ "</fechaAutorizacion>");
					xml.append("<comprobante><![CDATA[" + aut.getComprobante()
							+ "]]></comprobante>");
					xml.append(obtienerMensajesAutorizacion(aut));
					xml.append("\n</autorizacion>");

					guardarXML(
							convertirStringADocument(xml.toString()),
							getRutaSRIAutorizadosNoSubidos()
									+ obtenerCedulaCliente(aut.getComprobante())
									+ "-" + docElec.getNombreDocumento());
					eliminarArchivo(ruta + docElec.getNombreDocumento());
					if (presentaMensaje)
						presentaMensaje(FacesMessage.SEVERITY_INFO,
								"SE AUTORIZO CORRECTAMENTE EL XML");
					break;
				} else if (aut.getEstado().equals("NO AUTORIZADO")) {
					xml.append("<estado>" + aut.getEstado() + "</estado>");
					xml.append("<fechaAutorizacion class=\"fechaAutorizacion\">"
							+ aut.getFechaAutorizacion()
							+ "</fechaAutorizacion>");
					xml.append("<comprobante><![CDATA[" + aut.getComprobante()
							+ "]]></comprobante>");
					xml.append(obtienerMensajesAutorizacion(aut));
					xml.append("\n</autorizacion>");

					guardarXML(
							convertirStringADocument(xml.toString()),
							getRutaSRINoAutorizados()
									+ docElec.getNombreDocumento());
					eliminarArchivo(ruta + docElec.getNombreDocumento());
					if (presentaMensaje)
						presentaMensaje(FacesMessage.SEVERITY_INFO,
								"NO SE AUTORIZO EL XML");
					break;
				}
			}
		} else if (rc.getAutorizaciones().getAutorizacion().isEmpty()) {
			moverArchivo(
					ruta + docElec.getNombreDocumento(),
					getRutaSRIFirmadosTransmitidosSinRespuesta()
							+ docElec.getNombreDocumento());
			if (presentaMensaje)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"EL XML SE ENVIO PERO NO RECIBIO NINGUNA RESPUESTA");
		}
	}

	public void enviarAutorizarXML(boolean presentaMensaje,
			DocumentoElectronico docElec) {
		if (parametro.getFacturacionElectronica()) {
			byte[] xml = convertirString(docElec.getDocumento());
			RespuestaSolicitud rs = null;
			for (int i = 0; i < 5; i++) {
				rs = validarComprobante(xml);
				if (rs != null && rs.getEstado().compareToIgnoreCase("") != 0)
					break;
				hilo();
			}
			if (rs.getEstado().compareToIgnoreCase("") != 0) {
				if (rs.getEstado().compareToIgnoreCase("RECIBIDA") == 0)
					autorizarXML(presentaMensaje, true, docElec);
				else if (rs.getEstado().compareToIgnoreCase("DEVUELTA") == 0) {
					StringBuilder sb = new StringBuilder(docElec.getDocumento());
					sb = sb.reverse();
					if (String.valueOf(sb.charAt(0)).compareToIgnoreCase(" ") == 0)
						sb.deleteCharAt(0);
					sb.insert(
							intTipoDocumento(docElec.getNombreDocumento()) + 4,
							new StringBuilder(obtieneComprobantes(rs))
									.reverse());
					sb = sb.reverse();
					guardarXML(
							convertirStringADocument(sb.toString()),
							getRutaSRIFirmadosRechazados()
									+ docElec.getNombreDocumento());
					eliminarArchivo(getRutaSRIFirmados()
							+ docElec.getNombreDocumento());
					if (presentaMensaje)
						presentaMensaje(FacesMessage.SEVERITY_INFO,
								"EL XML HA SIDO DEVUELTO");
				}
			} else if (rs.getEstado().compareToIgnoreCase("") == 0)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"ERROR DE TRANSMISION");
		}
	}

	public List<DocumentoElectronico> documentosFirmados() {
		List<DocumentoElectronico> list = new ArrayList<DocumentoElectronico>();
		for (String s : getlistArchivos(getRutaSRIFirmados()))
			list.add(new DocumentoElectronico(s, tipoDocumento(s),
					leerArchivo(getRutaSRIFirmados() + s)));
		return list;
	}

	public List<DocumentoElectronico> documentosGenerados() {
		List<DocumentoElectronico> list = new ArrayList<DocumentoElectronico>();
		for (String s : getlistArchivos(getRutaSRIGenerados()))
			list.add(new DocumentoElectronico(s, tipoDocumento(s),
					leerArchivo(getRutaSRIGenerados() + s)));
		return list;
	}

	public List<DocumentoElectronico> documentosTransmitidosSinRespuesta() {
		List<DocumentoElectronico> list = new ArrayList<DocumentoElectronico>();
		for (String s : getlistArchivos(getRutaSRIFirmadosTransmitidosSinRespuesta()))
			list.add(new DocumentoElectronico(s, tipoDocumento(s),
					leerArchivo(getRutaSRIFirmadosTransmitidosSinRespuesta()
							+ s)));
		return list;
	}

	public void firmarXML(boolean presentaMensaje, DocumentoElectronico docElec) {
		if (parametro.getFacturacionElectronica()) {
			guardarXMLFirmado(
					firmar(parametro.getPassToken(), docElec.getDocumento()),
					getRutaSRIFirmados() + docElec.getNombreDocumento());
			eliminarArchivo(getRutaSRIGenerados()
					+ docElec.getNombreDocumento());
			if (presentaMensaje)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"SE FIRMO CORRECTAMENTE EL XML");
		}
	}

	public void subirComprobantesElectronicos(boolean presentaMensaje,
			DocumentoElectronico docElec) {
		if (parametro.getFacturacionElectronica()) {
			Map<String, Object> map = generarFileRIDE(docElec);
			envioCorreo(presentaMensaje, docElec, map);
			almacenamiento(
					subString(docElec.getNombreDocumento(), ".xml"),
					convertir(docElec.getNombreDocumento(),
							docElec.getDocumento()), (File) map.get("ride"));
			moverArchivo(
					getRutaSRIAutorizadosNoSubidos()
							+ docElec.getNombreDocumento(),
					getRutaSRIAutorizados() + docElec.getNombreDocumento());
			if (presentaMensaje)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"SE SUBIO CORRECTAMENTE EL XML");
		}
	}

	public String generarFacturaXML(int facturaId, CantidadFactura cf) {
		if (parametro.getFacturacionElectronica()) {
			Factura factura = facturaService.obtenerPorFacturaId(facturaId);
			String claveAcceso = claveAcceso(factura.getFechaInicio(), "01",
					factura.getEstablecimiento() + factura.getPuntoEmision(),
					factura.getSecuencia());
			objetoTOxml(
					FacturaE.class,
					getRutaSRIGenerados() + claveAcceso + ".xml",
					facturaXML(factura, cf, claveAcceso, localService,
							tarifaService, personaService));
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE GENERO CORRECTAMENTE EL XML");
			return claveAcceso + ".xml";
		}
		return "";
	}

	public String generarRetencionXML(int retencionId) {
		if (parametro.getFacturacionElectronica()) {
			Retencion retencion = retencionService.obtenerPorId(retencionId);
			String claveAcceso = claveAcceso(
					retencion.getFechaEmision(),
					"07",
					retencion.getEstablecimiento()
							+ retencion.getPuntoEmision(),
					retencion.getSecuencia());
			objetoTOxml(
					ComprobanteRetencionE.class,
					getRutaSRIGenerados() + claveAcceso + ".xml",
					retencionXML(retencion, claveAcceso, localService,
							personaService));
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE GENERO CORRECTAMENTE EL XML");
			return claveAcceso + ".xml";
		}
		return "";
	}

	public String generarNotaCreditoXML(int notaCreditoId, CantidadFactura cf) {
		if (parametro.getFacturacionElectronica()) {
			NotaCredito notaCredito = notaCreditoService
					.obtenerPorNotaCreditoId(notaCreditoId);
			String claveAcceso = claveAcceso(
					notaCredito.getFecha(),
					"04",
					notaCredito.getEstablecimiento()
							+ notaCredito.getPuntoEmision(),
					notaCredito.getSecuencia());
			objetoTOxml(
					NotaCreditoE.class,
					getRutaSRIGenerados() + claveAcceso + ".xml",
					notaCreditoXML(notaCredito, cf, claveAcceso, localService,
							tarifaService, personaService));
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"SE GENERO CORRECTAMENTE EL XML");
			return claveAcceso + ".xml";
		}
		return "";
	}

	public List<DocumentoElectronico> documentosAutorizados(String cedulaRuc,
			Mes mes, Anio anio) {
		String fec = mes.getId() + anio.getId();
		List<DocumentoElectronico> list = new ArrayList<DocumentoElectronico>();
		for (String s : getlistArchivos(getRutaSRIAutorizados())) {
			if (s.startsWith(cedulaRuc)
					&& fec.compareToIgnoreCase(s.split("-")[1].substring(2, 8)) == 0)
				list.add(new DocumentoElectronico(s,
						tipoDocumento(s.split("-")[1]),
						leerArchivo(getRutaSRIAutorizados() + s)));
		}
		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO HAY DATOS A MOSTRAR");

		return list;
	}

	public List<DocumentoElectronico> documentosAutorizadosNoSubidos() {
		List<DocumentoElectronico> list = new ArrayList<DocumentoElectronico>();
		for (String s : getlistArchivos(getRutaSRIAutorizadosNoSubidos()))
			list.add(new DocumentoElectronico(s,
					tipoDocumento(s.split("-")[1]),
					leerArchivo(getRutaSRIAutorizadosNoSubidos() + s)));
		return list;
	}

	public void envioCorreo(boolean presentaMensaje,
			DocumentoElectronico documentoElectronico, Map<String, Object> map) {
		String[] split = documentoElectronico.getNombreDocumento().split("-");
		Persona p = personaService.obtenerPorCedula(split[0]);
		if (p != null && p.getEmail() != null
				&& p.getEmail().compareToIgnoreCase("") != 0) {
			if (map == null)
				map = generarFileRIDE(documentoElectronico);
			InfoTributaria infoTributaria = (InfoTributaria) map
					.get("infoTributaria");
			File xml = convertir(documentoElectronico.getNombreDocumento(),
					documentoElectronico.getDocumento());

			boolean envioCorreoCorrecto = enviarCorreo(
					infoTributaria.getNombreComercial(),
					p,
					infoTributaria.getEstab() + "-"
							+ infoTributaria.getPtoEmi() + "-"
							+ infoTributaria.getSecuencial(),
					fechaFormatoString(split[1]), xml, (File) map.get("ride"));
			if (presentaMensaje && envioCorreoCorrecto)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"SE ENVIO AL CORREO SATISFACTORIAMENTE");
		}
	}

	public Map<String, Object> generarFileRIDE(
			DocumentoElectronico documentoElectronico) {
		Map<String, Object> map = new HashMap<String, Object>();
		File ride = null;
		InfoTributaria infoTributaria = null;
		if (documentoElectronico.getTipoDocumento().compareToIgnoreCase(
				"FACTURA") == 0) {
			List<FacturaReporteRIDE> list = imprimirRideFactura(documentoElectronico
					.getDocumento());
			if (list != null) {
				infoTributaria = list.get(0).getFactura().getInfoTributaria();
				ride = reporteService.generarReporteRIDECorreo(list,
						"FacturaE", infoTributaria);
			}
		} else if (documentoElectronico.getTipoDocumento().compareToIgnoreCase(
				"NOTA DE CRÉDITO") == 0) {
			List<NotaCreditoReporteRIDE> list = imprimirRideNotaCredito(documentoElectronico
					.getDocumento());
			if (list != null) {
				infoTributaria = list.get(0).getNotaCredito()
						.getInfoTributaria();
				ride = reporteService.generarReporteRIDECorreo(list,
						"NotaCreditoE", infoTributaria);
			}
		} else if (documentoElectronico.getTipoDocumento().compareToIgnoreCase(
				"COMPROBANTE DE RETENCIÓN") == 0) {
			List<RetencionReporteRIDE> list = imprimirRideRetencion(documentoElectronico
					.getDocumento());
			if (list != null) {
				infoTributaria = list.get(0).getRetencion().getInfoTributaria();
				ride = reporteService.generarReporteRIDECorreo(list,
						"comprobanteRetencionE", infoTributaria);
			}
		}
		map.put("ride", ride);
		map.put("infoTributaria", infoTributaria);
		return map;
	}

	public void generarRIDE(DocumentoElectronico documentoElectronico) {
		if (documentoElectronico != null) {
			if (documentoElectronico.getTipoDocumento().compareToIgnoreCase(
					"FACTURA") == 0) {
				List<FacturaReporteRIDE> list = imprimirRideFactura(documentoElectronico
						.getDocumento());
				if (list != null)
					reporteService.generarReporteRIDE(list, "FacturaE", list
							.get(0).getFactura().getInfoTributaria());
			} else if (documentoElectronico.getTipoDocumento()
					.compareToIgnoreCase("NOTA DE CRÉDITO") == 0) {
				List<NotaCreditoReporteRIDE> list = imprimirRideNotaCredito(documentoElectronico
						.getDocumento());
				if (list != null)
					reporteService.generarReporteRIDE(list, "NotaCreditoE",
							list.get(0).getNotaCredito().getInfoTributaria());
			} else if (documentoElectronico.getTipoDocumento()
					.compareToIgnoreCase("COMPROBANTE DE RETENCIÓN") == 0) {
				List<RetencionReporteRIDE> list = imprimirRideRetencion(documentoElectronico
						.getDocumento());
				if (list != null)
					reporteService.generarReporteRIDE(list,
							"comprobanteRetencionE", list.get(0).getRetencion()
									.getInfoTributaria());
			}
		}
	}

	public List<FacturaReporteRIDE> imprimirRideFactura(String xml) {
		FacturaReporteRIDE facturaReporteRIDE = facturaRIDE(xml);
		if (facturaReporteRIDE == null) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"EL DOCUMENTO NO ES VALIDO NO SE PUDO GENERAR EL RIDE");
			return null;
		}
		List<FacturaReporteRIDE> list = new ArrayList<FacturaReporteRIDE>();
		list.add(facturaReporteRIDE);
		return list;
	}

	public List<NotaCreditoReporteRIDE> imprimirRideNotaCredito(String xml) {
		NotaCreditoReporteRIDE notaCreditoReporteRIDE = notaCreditoRIDE(xml);
		if (notaCreditoReporteRIDE == null) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"EL DOCUMENTO NO ES VALIDO NO SE PUDO GENERAR EL RIDE");
			return null;
		}
		List<NotaCreditoReporteRIDE> list = new ArrayList<NotaCreditoReporteRIDE>();
		list.add(notaCreditoReporteRIDE);
		return list;
	}

	public List<RetencionReporteRIDE> imprimirRideRetencion(String xml) {
		RetencionReporteRIDE retencionReporteRIDE = retencionRIDE(xml);
		if (retencionReporteRIDE == null) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"EL DOCUMENTO NO ES VALIDO NO SE PUDO GENERAR EL RIDE");
			return null;
		}
		List<RetencionReporteRIDE> list = new ArrayList<RetencionReporteRIDE>();
		list.add(retencionReporteRIDE);
		return list;
	}

	public void cambiarConsumidorFinal() {
		List<DocumentoElectronico> listDocu = documentosGenerados();
		String[][] cedulas = new String[4][2];
		cedulas[0][0] = "0704868462001";
		cedulas[0][1] = "CONZA APOLO FRANKLIN";

		cedulas[1][0] = "0701224537";
		cedulas[1][1] = "CONZA FRANKLIN";

		cedulas[2][0] = "0704997360";
		cedulas[2][1] = "GUACHISACA ANDRES";

		cedulas[3][0] = "0706246857";
		cedulas[3][1] = "CONZA ADRIAN";

		int r = 0;
		int c = 0;
		String cedula = "";
		String nombre = "";
		for (int i = 0; i < listDocu.size(); i++) {
			if (r <= 0) {
				r = (int) (Math.random() * 10) + 1;
				cedula = cedulas[c][0];
				nombre = cedulas[c][1];
				c++;
				if (c == 4)
					c = 0;
			}
			r--;
			DocumentoElectronico docElec = listDocu.get(i);
			if (docElec.getTipoDocumento().compareToIgnoreCase("FACTURA") == 0) {
				List<FacturaReporteRIDE> list = imprimirRideFactura(docElec
						.getDocumento());
				FacturaE fe = list.get(0).getFactura();

				fe.getInfoFactura().setTipoIdentificacionComprador(
						tipoIdentificacionComprador(cedula));
				fe.getInfoFactura().setRazonSocialComprador(nombre);
				fe.getInfoFactura().setIdentificacionComprador(cedula);

				objetoTOxml(FacturaE.class,
						getRutaSRIFirmados() + docElec.getNombreDocumento(), fe);
			}
		}
	}
}