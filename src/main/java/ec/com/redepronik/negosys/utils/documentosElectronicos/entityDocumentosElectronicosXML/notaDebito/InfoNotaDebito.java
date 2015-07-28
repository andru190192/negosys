package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaDebito;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.Impuestos;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fechaEmision", "dirEstablecimiento",
		"tipoIdentificacionComprador", "razonSocialComprador",
		"identificacionComprador", "contribuyenteEspecial",
		"obligadoContabilidad", "rise", "codDocModificado", "numDocModificado",
		"fechaEmisionDocSustento", "totalSinImpuestos", "impuestos",
		"valorTotal" })
public class InfoNotaDebito {
	@XmlElement(required = true)
	protected String fechaEmision;
	protected String dirEstablecimiento;
	@XmlElement(required = true)
	protected String tipoIdentificacionComprador;
	protected String razonSocialComprador;
	@XmlElement(required = true)
	protected String identificacionComprador;
	protected String contribuyenteEspecial;
	protected String obligadoContabilidad;
	protected String rise;
	@XmlElement(required = true)
	protected String codDocModificado;
	@XmlElement(required = true)
	protected String numDocModificado;
	@XmlElement(required = true)
	protected String fechaEmisionDocSustento;
	@XmlElement(required = true)
	protected BigDecimal totalSinImpuestos;
	@XmlElement(required = true)
	protected Impuestos impuestos;
	@XmlElement(required = true)
	protected BigDecimal valorTotal;

	public String getCodDocModificado() {
		return this.codDocModificado;
	}

	public String getContribuyenteEspecial() {
		return this.contribuyenteEspecial;
	}

	public String getDirEstablecimiento() {
		return this.dirEstablecimiento;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public String getFechaEmisionDocSustento() {
		return this.fechaEmisionDocSustento;
	}

	public String getIdentificacionComprador() {
		return this.identificacionComprador;
	}

	public Impuestos getImpuestos() {
		return this.impuestos;
	}

	public String getNumDocModificado() {
		return this.numDocModificado;
	}

	public String getObligadoContabilidad() {
		return this.obligadoContabilidad;
	}

	public String getRazonSocialComprador() {
		return this.razonSocialComprador;
	}

	public String getRise() {
		return this.rise;
	}

	public String getTipoIdentificacionComprador() {
		return this.tipoIdentificacionComprador;
	}

	public BigDecimal getTotalSinImpuestos() {
		return this.totalSinImpuestos;
	}

	public BigDecimal getValorTotal() {
		return this.valorTotal;
	}

	public void setCodDocModificado(String value) {
		this.codDocModificado = value;
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

	public void setFechaEmisionDocSustento(String value) {
		this.fechaEmisionDocSustento = value;
	}

	public void setIdentificacionComprador(String value) {
		this.identificacionComprador = value;
	}

	public void setImpuestos(Impuestos value) {
		this.impuestos = value;
	}

	public void setNumDocModificado(String value) {
		this.numDocModificado = value;
	}

	public void setObligadoContabilidad(String value) {
		this.obligadoContabilidad = value;
	}

	public void setRazonSocialComprador(String value) {
		this.razonSocialComprador = value;
	}

	public void setRise(String value) {
		this.rise = value;
	}

	public void setTipoIdentificacionComprador(String value) {
		this.tipoIdentificacionComprador = value;
	}

	public void setTotalSinImpuestos(BigDecimal value) {
		this.totalSinImpuestos = value;
	}

	public void setValorTotal(BigDecimal value) {
		this.valorTotal = value;
	}

}
