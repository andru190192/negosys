package ec.com.redepronik.negosys.wsOtros.sri.recepcion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mensaje", propOrder = { "identificador", "mensaje",
		"informacionAdicional", "tipo" })
public class Mensaje {

	protected String identificador;
	protected String mensaje;
	protected String informacionAdicional;
	protected String tipo;

	public String getIdentificador() {
		return identificador;
	}

	public String getInformacionAdicional() {
		return informacionAdicional;
	}

	public String getMensaje() {
		return mensaje;
	}

	public String getTipo() {
		return tipo;
	}

	public void setIdentificador(String value) {
		this.identificador = value;
	}

	public void setInformacionAdicional(String value) {
		this.informacionAdicional = value;
	}

	public void setMensaje(String value) {
		this.mensaje = value;
	}

	public void setTipo(String value) {
		this.tipo = value;
	}

}
