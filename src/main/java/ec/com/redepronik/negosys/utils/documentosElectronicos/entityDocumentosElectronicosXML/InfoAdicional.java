package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "campoAdicional" })
public class InfoAdicional {
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "value" })
	public static class CampoAdicional {
		@XmlValue
		protected String value;
		@XmlAttribute
		protected String nombre;

		public String getNombre() {
			return this.nombre;
		}

		public String getValue() {
			return this.value;
		}

		public void setNombre(String value) {
			this.nombre = value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@XmlElement(required = true)
	protected List<CampoAdicional> campoAdicional;

	public List<CampoAdicional> getCampoAdicional() {
		if (this.campoAdicional == null) {
			this.campoAdicional = new ArrayList<CampoAdicional>();
		}
		return this.campoAdicional;
	}
}
