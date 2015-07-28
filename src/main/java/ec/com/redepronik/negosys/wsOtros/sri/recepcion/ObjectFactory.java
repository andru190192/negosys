package ec.com.redepronik.negosys.wsOtros.sri.recepcion;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName _ValidarComprobante_QNAME = new QName(
			"http://ec.gob.sri.ws.recepcion", "validarComprobante");
	private final static QName _ValidarComprobanteResponse_QNAME = new QName(
			"http://ec.gob.sri.ws.recepcion", "validarComprobanteResponse");
	private final static QName _Mensaje_QNAME = new QName(
			"http://ec.gob.sri.ws.recepcion", "mensaje");
	private final static QName _RespuestaSolicitud_QNAME = new QName(
			"http://ec.gob.sri.ws.recepcion", "RespuestaSolicitud");
	private final static QName _Comprobante_QNAME = new QName(
			"http://ec.gob.sri.ws.recepcion", "comprobante");

	public ObjectFactory() {
	}

	public Comprobante createComprobante() {
		return new Comprobante();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "comprobante")
	public JAXBElement<Comprobante> createComprobante(Comprobante value) {
		return new JAXBElement<Comprobante>(_Comprobante_QNAME,
				Comprobante.class, null, value);
	}

	public Comprobante.Mensajes createComprobanteMensajes() {
		return new Comprobante.Mensajes();
	}

	public Mensaje createMensaje() {
		return new Mensaje();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "mensaje")
	public JAXBElement<Mensaje> createMensaje(Mensaje value) {
		return new JAXBElement<Mensaje>(_Mensaje_QNAME, Mensaje.class, null,
				value);
	}

	public RespuestaSolicitud createRespuestaSolicitud() {
		return new RespuestaSolicitud();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "RespuestaSolicitud")
	public JAXBElement<RespuestaSolicitud> createRespuestaSolicitud(
			RespuestaSolicitud value) {
		return new JAXBElement<RespuestaSolicitud>(_RespuestaSolicitud_QNAME,
				RespuestaSolicitud.class, null, value);
	}

	public RespuestaSolicitud.Comprobantes createRespuestaSolicitudComprobantes() {
		return new RespuestaSolicitud.Comprobantes();
	}

	public ValidarComprobante createValidarComprobante() {
		return new ValidarComprobante();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "validarComprobante")
	public JAXBElement<ValidarComprobante> createValidarComprobante(
			ValidarComprobante value) {
		return new JAXBElement<ValidarComprobante>(_ValidarComprobante_QNAME,
				ValidarComprobante.class, null, value);
	}

	public ValidarComprobanteResponse createValidarComprobanteResponse() {
		return new ValidarComprobanteResponse();
	}

	@XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "validarComprobanteResponse")
	public JAXBElement<ValidarComprobanteResponse> createValidarComprobanteResponse(
			ValidarComprobanteResponse value) {
		return new JAXBElement<ValidarComprobanteResponse>(
				_ValidarComprobanteResponse_QNAME,
				ValidarComprobanteResponse.class, null, value);
	}

}
