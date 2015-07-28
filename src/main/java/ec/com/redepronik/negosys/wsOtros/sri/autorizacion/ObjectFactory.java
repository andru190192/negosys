package ec.com.redepronik.negosys.wsOtros.sri.autorizacion;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName _AutorizacionComprobanteLote_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion", "autorizacionComprobanteLote");
	private final static QName _RespuestaAutorizacion_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion", "RespuestaAutorizacion");
	private final static QName _AutorizacionComprobanteResponse_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion",
			"autorizacionComprobanteResponse");
	private final static QName _AutorizacionComprobanteLoteResponse_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion",
			"autorizacionComprobanteLoteResponse");
	private final static QName _AutorizacionComprobante_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion", "autorizacionComprobante");
	private final static QName _Autorizacion_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion", "autorizacion");
	private final static QName _Mensaje_QNAME = new QName(
			"http://ec.gob.sri.ws.autorizacion", "mensaje");

	public ObjectFactory() {
	}

	public Autorizacion createAutorizacion() {
		return new Autorizacion();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacion")
	public JAXBElement<Autorizacion> createAutorizacion(Autorizacion value) {
		return new JAXBElement<Autorizacion>(_Autorizacion_QNAME,
				Autorizacion.class, null, value);
	}

	public AutorizacionComprobante createAutorizacionComprobante() {
		return new AutorizacionComprobante();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobante")
	public JAXBElement<AutorizacionComprobante> createAutorizacionComprobante(
			AutorizacionComprobante value) {
		return new JAXBElement<AutorizacionComprobante>(
				_AutorizacionComprobante_QNAME, AutorizacionComprobante.class,
				null, value);
	}

	public AutorizacionComprobanteLote createAutorizacionComprobanteLote() {
		return new AutorizacionComprobanteLote();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobanteLote")
	public JAXBElement<AutorizacionComprobanteLote> createAutorizacionComprobanteLote(
			AutorizacionComprobanteLote value) {
		return new JAXBElement<AutorizacionComprobanteLote>(
				_AutorizacionComprobanteLote_QNAME,
				AutorizacionComprobanteLote.class, null, value);
	}

	public AutorizacionComprobanteLoteResponse createAutorizacionComprobanteLoteResponse() {
		return new AutorizacionComprobanteLoteResponse();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobanteLoteResponse")
	public JAXBElement<AutorizacionComprobanteLoteResponse> createAutorizacionComprobanteLoteResponse(
			AutorizacionComprobanteLoteResponse value) {
		return new JAXBElement<AutorizacionComprobanteLoteResponse>(
				_AutorizacionComprobanteLoteResponse_QNAME,
				AutorizacionComprobanteLoteResponse.class, null, value);
	}

	public AutorizacionComprobanteResponse createAutorizacionComprobanteResponse() {
		return new AutorizacionComprobanteResponse();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobanteResponse")
	public JAXBElement<AutorizacionComprobanteResponse> createAutorizacionComprobanteResponse(
			AutorizacionComprobanteResponse value) {
		return new JAXBElement<AutorizacionComprobanteResponse>(
				_AutorizacionComprobanteResponse_QNAME,
				AutorizacionComprobanteResponse.class, null, value);
	}

	public Autorizacion.Mensajes createAutorizacionMensajes() {
		return new Autorizacion.Mensajes();
	}

	public Mensaje createMensaje() {
		return new Mensaje();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "mensaje")
	public JAXBElement<Mensaje> createMensaje(Mensaje value) {
		return new JAXBElement<Mensaje>(_Mensaje_QNAME, Mensaje.class, null,
				value);
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "RespuestaAutorizacion")
	public JAXBElement<Object> createRespuestaAutorizacion(Object value) {
		return new JAXBElement<Object>(_RespuestaAutorizacion_QNAME,
				Object.class, null, value);
	}

	public RespuestaComprobante createRespuestaComprobante() {
		return new RespuestaComprobante();
	}

	public RespuestaComprobante.Autorizaciones createRespuestaComprobanteAutorizaciones() {
		return new RespuestaComprobante.Autorizaciones();
	}

	public RespuestaLote createRespuestaLote() {
		return new RespuestaLote();
	}

	public RespuestaLote.Autorizaciones createRespuestaLoteAutorizaciones() {
		return new RespuestaLote.Autorizaciones();
	}

}
