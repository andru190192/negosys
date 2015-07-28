package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fechaEmision", "dirEstablecimiento",
		"contribuyenteEspecial", "obligadoContabilidad",
		"tipoIdentificacionSujetoRetenido", "razonSocialSujetoRetenido",
		"identificacionSujetoRetenido", "periodoFiscal" })
public class InfoCompRetencion {
	@XmlElement(required = true)
	protected String fechaEmision;
	protected String dirEstablecimiento;
	protected String contribuyenteEspecial;
	protected String obligadoContabilidad;
	@XmlElement(required = true)
	protected String tipoIdentificacionSujetoRetenido;
	@XmlElement(required = true)
	protected String razonSocialSujetoRetenido;
	@XmlElement(required = true)
	protected String identificacionSujetoRetenido;
	@XmlElement(required = true)
	protected String periodoFiscal;

	public String getContribuyenteEspecial() {
		return this.contribuyenteEspecial;
	}

	public String getDirEstablecimiento() {
		return this.dirEstablecimiento;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public String getIdentificacionSujetoRetenido() {
		return this.identificacionSujetoRetenido;
	}

	public String getObligadoContabilidad() {
		return this.obligadoContabilidad;
	}

	public String getPeriodoFiscal() {
		return this.periodoFiscal;
	}

	public String getRazonSocialSujetoRetenido() {
		return this.razonSocialSujetoRetenido;
	}

	public String getTipoIdentificacionSujetoRetenido() {
		return this.tipoIdentificacionSujetoRetenido;
	}

	public void setContribuyenteEspecial(String value) {
		this.contribuyenteEspecial = value;
	}

	public void setDirEstablecimiento(String value) {
		this.dirEstablecimiento = value;
	}

	public void setFechaEmision(String value) {
		this.fechaEmision = value;
	}

	public void setIdentificacionSujetoRetenido(String value) {
		this.identificacionSujetoRetenido = value;
	}

	public void setObligadoContabilidad(String value) {
		this.obligadoContabilidad = value;
	}

	public void setPeriodoFiscal(String value) {
		this.periodoFiscal = value;
	}

	public void setRazonSocialSujetoRetenido(String value) {
		this.razonSocialSujetoRetenido = value;
	}

	public void setTipoIdentificacionSujetoRetenido(String value) {
		this.tipoIdentificacionSujetoRetenido = value;
	}
}
