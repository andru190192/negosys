package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaDebito;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoAdicional;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "infoTributaria", "infoNotaDebito",
		"motivos", "infoAdicional" })
@XmlRootElement(name = "notaDebito")
public class NotaDebitoE {
	@XmlElement(required = true)
	protected InfoTributaria infoTributaria;
	@XmlElement(required = true)
	protected InfoNotaDebito infoNotaDebito;
	@XmlElement(required = true)
	protected Motivos motivos;
	protected InfoAdicional infoAdicional;
	@XmlAttribute
	protected String id = "comprobante";
	@XmlAttribute
	protected String version = "1.0.0";

	public String getId() {
		return this.id;
	}

	public InfoAdicional getInfoAdicional() {
		return this.infoAdicional;
	}

	public InfoNotaDebito getInfoNotaDebito() {
		return this.infoNotaDebito;
	}

	public InfoTributaria getInfoTributaria() {
		return this.infoTributaria;
	}

	public Motivos getMotivos() {
		return this.motivos;
	}

	public String getVersion() {
		return this.version;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setInfoAdicional(InfoAdicional value) {
		this.infoAdicional = value;
	}

	public void setInfoNotaDebito(InfoNotaDebito value) {
		this.infoNotaDebito = value;
	}

	public void setInfoTributaria(InfoTributaria value) {
		this.infoTributaria = value;
	}

	public void setMotivos(Motivos value) {
		this.motivos = value;
	}

	public void setVersion(String value) {
		this.version = value;
	}

}
