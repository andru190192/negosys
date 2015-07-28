package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaDebito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "motivo" })
public class Motivos {
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "razon", "valor" })
	public static class Motivo {
		@XmlElement(required = true)
		protected String razon;
		@XmlElement(required = true)
		protected BigDecimal valor;

		public String getRazon() {
			return this.razon;
		}

		public BigDecimal getValor() {
			return this.valor;
		}

		public void setRazon(String value) {
			this.razon = value;
		}

		public void setValor(BigDecimal value) {
			this.valor = value;
		}
	}

	@XmlElement(required = true)
	protected List<Motivo> motivo;

	public List<Motivo> getMotivo() {
		if (this.motivo == null) {
			this.motivo = new ArrayList<Motivo>();
		}
		return this.motivo;
	}
}
