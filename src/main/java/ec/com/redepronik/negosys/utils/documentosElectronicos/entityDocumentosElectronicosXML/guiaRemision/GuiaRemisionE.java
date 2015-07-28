package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.guiaRemision;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoAdicional;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "infoTributaria", "infoGuiaRemision",
		"destinatarios", "infoAdicional" })
@XmlRootElement(name = "guiaRemision")
public class GuiaRemisionE {
	@XmlElement(required = true)
	protected InfoTributaria infoTributaria;
	@XmlElement(required = true)
	protected InfoGuiaRemision infoGuiaRemision;
	@XmlElement(required = true)
	protected Destinatarios destinatarios;
	protected InfoAdicional infoAdicional;
	@XmlAttribute
	protected String id = "comprobante";
	@XmlAttribute
	protected String version = "1.0.0";

	public Destinatarios getDestinatarios() {
		return this.destinatarios;
	}

	public String getId() {
		return this.id;
	}

	public InfoAdicional getInfoAdicional() {
		return this.infoAdicional;
	}

	public InfoGuiaRemision getInfoGuiaRemision() {
		return this.infoGuiaRemision;
	}

	public InfoTributaria getInfoTributaria() {
		return this.infoTributaria;
	}

	public String getVersion() {
		return this.version;
	}

	public void setDestinatarios(Destinatarios value) {
		this.destinatarios = value;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setInfoAdicional(InfoAdicional value) {
		this.infoAdicional = value;
	}

	public void setInfoGuiaRemision(InfoGuiaRemision value) {
		this.infoGuiaRemision = value;
	}

	public void setInfoTributaria(InfoTributaria value) {
		this.infoTributaria = value;
	}

	public void setVersion(String value) {
		this.version = value;
	}

}
