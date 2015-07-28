package ec.com.redepronik.negosys.wsOtros.sri.autorizacion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaLote", propOrder = { "claveAccesoLoteConsultada",
		"numeroComprobantesLote", "autorizaciones" })
public class RespuestaLote {

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "autorizacion" })
	public static class Autorizaciones {
		protected List<Autorizacion> autorizacion;

		public List<Autorizacion> getAutorizacion() {
			if (autorizacion == null)
				autorizacion = new ArrayList<Autorizacion>();
			return this.autorizacion;
		}

	}

	protected String claveAccesoLoteConsultada;
	protected String numeroComprobantesLote;

	protected RespuestaLote.Autorizaciones autorizaciones;

	public RespuestaLote.Autorizaciones getAutorizaciones() {
		return autorizaciones;
	}

	public String getClaveAccesoLoteConsultada() {
		return claveAccesoLoteConsultada;
	}

	public String getNumeroComprobantesLote() {
		return numeroComprobantesLote;
	}

	public void setAutorizaciones(RespuestaLote.Autorizaciones value) {
		this.autorizaciones = value;
	}

	public void setClaveAccesoLoteConsultada(String value) {
		this.claveAccesoLoteConsultada = value;
	}

	public void setNumeroComprobantesLote(String value) {
		this.numeroComprobantesLote = value;
	}

}
