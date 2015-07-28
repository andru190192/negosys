package ec.com.redepronik.negosys.wsOtros.sri.recepcion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaSolicitud", propOrder = { "estado", "comprobantes" })
public class RespuestaSolicitud {

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "comprobante" })
	public static class Comprobantes {

		protected List<Comprobante> comprobante;

		public List<Comprobante> getComprobante() {
			if (comprobante == null)
				comprobante = new ArrayList<Comprobante>();
			return this.comprobante;
		}

	}

	protected String estado;

	protected RespuestaSolicitud.Comprobantes comprobantes;

	public RespuestaSolicitud.Comprobantes getComprobantes() {
		return comprobantes;
	}

	public String getEstado() {
		return estado;
	}

	public void setComprobantes(RespuestaSolicitud.Comprobantes value) {
		this.comprobantes = value;
	}

	public void setEstado(String value) {
		this.estado = value;
	}

}
