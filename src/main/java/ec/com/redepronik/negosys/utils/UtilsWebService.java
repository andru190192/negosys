package ec.com.redepronik.negosys.utils;

import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaCertificadoSeguridadSRIProduccion;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaCertificadoSeguridadSRIPruebas;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoCorte;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ec.com.redepronik.negosys.wsOtros.sri.autorizacion.AutorizacionComprobantes;
import ec.com.redepronik.negosys.wsOtros.sri.autorizacion.AutorizacionComprobantesService;
import ec.com.redepronik.negosys.wsOtros.sri.autorizacion.RespuestaComprobante;
import ec.com.redepronik.negosys.wsOtros.sri.autorizacion.RespuestaLote;
import ec.com.redepronik.negosys.wsOtros.sri.recepcion.RecepcionComprobantes;
import ec.com.redepronik.negosys.wsOtros.sri.recepcion.RecepcionComprobantesService;
import ec.com.redepronik.negosys.wsOtros.sri.recepcion.RespuestaSolicitud;

public class UtilsWebService {

	private static String produccion = "https://cel.sri.gob.ec";
	private static String prueba = "https://celcer.sri.gob.ec";
	private static String recepcion = "/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
	private static String autorizacion = "/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
	private static boolean ambiente = parametro.getTipoAmbiente();
	private static String redepronikWS = "http://" + parametro.getWsBucket() + "/redepronikWS/";

	public static void almacenamiento(String nombreArchivo, File xml, File ride) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("userAwsS3", parametro.getUserAwsS3());
		map.add("passAwsS3", parametro.getPassAwsS3());
		map.add("bucketAwsS3", parametro.getBucketAwsS3());
		map.add("nombreArchivo", nombreArchivo);
		map.add("xml", new FileSystemResource(xml));
		map.add("ride", new FileSystemResource(ride));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "multipart/form-data");
		headers.set("Accept", "text/plain");

		restTemplate.exchange(redepronikWS + "almacenamiento", HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(map, headers),
				String.class);
	}

	public static Date fechaCorte(String ruc) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> map = new HashMap<String, String>();
		map.put("ruc", ruc);
		String respuesta = restTemplate.getForObject(redepronikWS + "fechaCorte?ruc={ruc}", String.class, map);
		return fechaFormatoCorte(respuesta);
	}

	public static RespuestaComprobante autorizacionComprobante(String claveAccesoComprobante) {
		try {
			certificadoSeguridadSRI();
			AutorizacionComprobantesService service = new AutorizacionComprobantesService(new URL((ambiente ? produccion : prueba) + autorizacion),
					new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));
			AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 9999);
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 9999);
			return port.autorizacionComprobante(claveAccesoComprobante);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RespuestaLote autorizacionComprobanteLote(String claveAccesoLote) {
		try {
			certificadoSeguridadSRI();
			AutorizacionComprobantesService service = new AutorizacionComprobantesService(new URL((ambiente ? produccion : prueba) + autorizacion),
					new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));
			AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 9999);
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 9999);
			return port.autorizacionComprobanteLote(claveAccesoLote);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void certificadoSeguridadSRI() {
		String rutaCertificado = ambiente ? getRutaCertificadoSeguridadSRIProduccion() : getRutaCertificadoSeguridadSRIPruebas();
		System.setProperty("javax.net.ssl.keyStore", rutaCertificado);
		System.setProperty("javax.net.ssl.keyStorePassword", "certificadoSeguridadSRI");
		System.setProperty("javax.net.ssl.trustStore", rutaCertificado);
		System.setProperty("javax.net.ssl.trustStorePassword", "certificadoSeguridadSRI");
	}

	public static RespuestaSolicitud validarComprobante(byte[] xml) {
		try {
			certificadoSeguridadSRI();
			RecepcionComprobantesService service = new RecepcionComprobantesService(new URL((ambiente ? produccion : prueba) + recepcion), new QName(
					"http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService"));
			RecepcionComprobantes port = service.getRecepcionComprobantesPort();
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 9999);
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 9999);
			return port.validarComprobante(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
