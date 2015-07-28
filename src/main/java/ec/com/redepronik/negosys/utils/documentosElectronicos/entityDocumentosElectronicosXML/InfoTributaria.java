package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infoTributaria", propOrder = { "ambiente", "tipoEmision",
		"razonSocial", "nombreComercial", "ruc", "claveAcceso", "codDoc",
		"estab", "ptoEmi", "secuencial", "dirMatriz" })
public class InfoTributaria {
	@XmlElement(required = true)
	protected String ambiente;
	@XmlElement(required = true)
	protected String tipoEmision;
	@XmlElement(required = true)
	protected String razonSocial;
	protected String nombreComercial;
	@XmlElement(required = true)
	protected String ruc;
	@XmlElement(required = true)
	protected String claveAcceso;
	@XmlElement(required = true)
	protected String codDoc;
	@XmlElement(required = true)
	protected String estab;
	@XmlElement(required = true)
	protected String ptoEmi;
	@XmlElement(required = true)
	protected String secuencial;
	@XmlElement(required = true)
	protected String dirMatriz;

	public String getAmbiente() {
		return this.ambiente;
	}

	public String getClaveAcceso() {
		return this.claveAcceso;
	}

	public String getCodDoc() {
		return this.codDoc;
	}

	public String getDirMatriz() {
		return this.dirMatriz;
	}

	public String getEstab() {
		return this.estab;
	}

	public String getNombreComercial() {
		return this.nombreComercial;
	}

	public String getPtoEmi() {
		return this.ptoEmi;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public String getRuc() {
		return this.ruc;
	}

	public String getSecuencial() {
		return this.secuencial;
	}

	public String getTipoEmision() {
		return this.tipoEmision;
	}

	public void setAmbiente(String value) {
		this.ambiente = value;
	}

	public void setClaveAcceso(String value) {
		this.claveAcceso = value;
	}

	public void setCodDoc(String value) {
		this.codDoc = value;
	}

	public void setDirMatriz(String value) {
		this.dirMatriz = value;
	}

	public void setEstab(String value) {
		this.estab = value;
	}

	public void setNombreComercial(String value) {
		this.nombreComercial = value;
	}

	public void setPtoEmi(String value) {
		this.ptoEmi = value;
	}

	public void setRazonSocial(String value) {
		this.razonSocial = value;
	}

	public void setRuc(String value) {
		this.ruc = value;
	}

	public void setSecuencial(String value) {
		this.secuencial = value;
	}

	public void setTipoEmision(String value) {
		this.tipoEmision = value;
	}
}
