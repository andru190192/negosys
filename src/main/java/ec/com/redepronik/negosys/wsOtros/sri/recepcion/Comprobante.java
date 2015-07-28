package ec.com.redepronik.negosys.wsOtros.sri.recepcion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comprobante", propOrder = { "claveAcceso", "mensajes" })
public class Comprobante {

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "mensaje" })
	public static class Mensajes {

		protected List<Mensaje> mensaje;

		public List<Mensaje> getMensaje() {
			if (mensaje == null)
				mensaje = new ArrayList<Mensaje>();
			return this.mensaje;
		}

	}

	protected String claveAcceso;

	protected Comprobante.Mensajes mensajes;

	public String getClaveAcceso() {
		return claveAcceso;
	}

	public Comprobante.Mensajes getMensajes() {
		return mensajes;
	}

	public void setClaveAcceso(String value) {
		this.claveAcceso = value;
	}

	public void setMensajes(Comprobante.Mensajes value) {
		this.mensajes = value;
	}

}
