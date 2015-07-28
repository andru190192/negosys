package ec.com.redepronik.negosys.wsOtros.sri.autorizacion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autorizacion", propOrder = { "estado", "numeroAutorizacion",
		"fechaAutorizacion", "ambiente", "comprobante", "mensajes" })
public class Autorizacion {

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

	protected String estado;
	protected String numeroAutorizacion;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar fechaAutorizacion;
	protected String ambiente;
	protected String comprobante;

	protected Autorizacion.Mensajes mensajes;

	public String getAmbiente() {
		return ambiente;
	}

	public String getComprobante() {
		return comprobante;
	}

	public String getEstado() {
		return estado;
	}

	public XMLGregorianCalendar getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public Autorizacion.Mensajes getMensajes() {
		return mensajes;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setAmbiente(String value) {
		this.ambiente = value;
	}

	public void setComprobante(String value) {
		this.comprobante = value;
	}

	public void setEstado(String value) {
		this.estado = value;
	}

	public void setFechaAutorizacion(XMLGregorianCalendar value) {
		this.fechaAutorizacion = value;
	}

	public void setMensajes(Autorizacion.Mensajes value) {
		this.mensajes = value;
	}

	public void setNumeroAutorizacion(String value) {
		this.numeroAutorizacion = value;
	}

}
