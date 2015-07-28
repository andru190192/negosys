package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.guiaRemision;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "dirEstablecimiento", "dirPartida",
		"razonSocialTransportista", "tipoIdentificacionTransportista",
		"rucTransportista", "rise", "obligadoContabilidad",
		"contribuyenteEspecial", "fechaIniTransporte", "fechaFinTransporte",
		"placa" })
public class InfoGuiaRemision {
	protected String dirEstablecimiento;
	@XmlElement(required = true)
	protected String dirPartida;
	@XmlElement(required = true)
	protected String razonSocialTransportista;
	@XmlElement(required = true)
	protected String tipoIdentificacionTransportista;
	@XmlElement(required = true)
	protected String rucTransportista;
	protected String rise;
	protected String obligadoContabilidad;
	protected String contribuyenteEspecial;
	@XmlElement(required = true)
	protected String fechaIniTransporte;
	@XmlElement(required = true)
	protected String fechaFinTransporte;
	@XmlElement(required = true)
	protected String placa;

	public String getContribuyenteEspecial() {
		return this.contribuyenteEspecial;
	}

	public String getDirEstablecimiento() {
		return this.dirEstablecimiento;
	}

	public String getDirPartida() {
		return this.dirPartida;
	}

	public String getFechaFinTransporte() {
		return this.fechaFinTransporte;
	}

	public String getFechaIniTransporte() {
		return this.fechaIniTransporte;
	}

	public String getObligadoContabilidad() {
		return this.obligadoContabilidad;
	}

	public String getPlaca() {
		return this.placa;
	}

	public String getRazonSocialTransportista() {
		return this.razonSocialTransportista;
	}

	public String getRise() {
		return this.rise;
	}

	public String getRucTransportista() {
		return this.rucTransportista;
	}

	public String getTipoIdentificacionTransportista() {
		return this.tipoIdentificacionTransportista;
	}

	public void setContribuyenteEspecial(String value) {
		this.contribuyenteEspecial = value;
	}

	public void setDirEstablecimiento(String value) {
		this.dirEstablecimiento = value;
	}

	public void setDirPartida(String value) {
		this.dirPartida = value;
	}

	public void setFechaFinTransporte(String value) {
		this.fechaFinTransporte = value;
	}

	public void setFechaIniTransporte(String value) {
		this.fechaIniTransporte = value;
	}

	public void setObligadoContabilidad(String value) {
		this.obligadoContabilidad = value;
	}

	public void setPlaca(String value) {
		this.placa = value;
	}

	public void setRazonSocialTransportista(String value) {
		this.razonSocialTransportista = value;
	}

	public void setRise(String value) {
		this.rise = value;
	}

	public void setRucTransportista(String value) {
		this.rucTransportista = value;
	}

	public void setTipoIdentificacionTransportista(String value) {
		this.tipoIdentificacionTransportista = value;
	}
}
