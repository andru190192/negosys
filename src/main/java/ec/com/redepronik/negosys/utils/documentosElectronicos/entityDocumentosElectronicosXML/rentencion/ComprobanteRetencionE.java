package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoAdicional;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "infoTributaria", "infoCompRetencion",
		"impuestos", "infoAdicional" })
@XmlRootElement(name = "comprobanteRetencion")
public class ComprobanteRetencionE {
	@XmlElement(required = true)
	protected InfoTributaria infoTributaria;
	@XmlElement(required = true)
	protected InfoCompRetencion infoCompRetencion;
	@XmlElement(required = true)
	protected Impuestos impuestos;
	protected InfoAdicional infoAdicional;
	@XmlAttribute
	protected String id = "comprobante";
	@XmlAttribute
	protected String version = "1.0.0";

	public String getId() {
		return this.id;
	}

	public Impuestos getImpuestos() {
		return this.impuestos;
	}

	public InfoAdicional getInfoAdicional() {
		return this.infoAdicional;
	}

	public InfoCompRetencion getInfoCompRetencion() {
		return this.infoCompRetencion;
	}

	public InfoTributaria getInfoTributaria() {
		return this.infoTributaria;
	}

	public String getVersion() {
		return this.version;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setImpuestos(Impuestos value) {
		this.impuestos = value;
	}

	public void setInfoAdicional(InfoAdicional value) {
		this.infoAdicional = value;
	}

	public void setInfoCompRetencion(InfoCompRetencion value) {
		this.infoCompRetencion = value;
	}

	public void setInfoTributaria(InfoTributaria value) {
		this.infoTributaria = value;
	}

	public void setVersion(String value) {
		this.version = value;
	}

}