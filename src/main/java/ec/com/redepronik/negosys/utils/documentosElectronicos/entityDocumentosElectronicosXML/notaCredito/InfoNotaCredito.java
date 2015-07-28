package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.TotalConImpuestos;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fechaEmision", "dirEstablecimiento",
		"tipoIdentificacionComprador", "razonSocialComprador",
		"identificacionComprador", "contribuyenteEspecial",
		"obligadoContabilidad", "rise", "codDocModificado", "numDocModificado",
		"fechaEmisionDocSustento", "totalSinImpuestos", "valorModificacion",
		"moneda", "totalConImpuestos", "motivo" })
public class InfoNotaCredito {
	@XmlElement(required = true)
	protected String fechaEmision;
	protected String dirEstablecimiento;
	@XmlElement(required = true)
	protected String tipoIdentificacionComprador;
	@XmlElement(required = true)
	protected String razonSocialComprador;
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
	protected BigDecimal valorModificacion;
	protected String moneda;
	@XmlElement(required = true)
	protected TotalConImpuestos totalConImpuestos;
	@XmlElement(required = true)
	protected String motivo;

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

	public String getMoneda() {
		return this.moneda;
	}

	public String getMotivo() {
		return this.motivo;
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

	public TotalConImpuestos getTotalConImpuestos() {
		return this.totalConImpuestos;
	}

	public BigDecimal getTotalSinImpuestos() {
		return this.totalSinImpuestos;
	}

	public BigDecimal getValorModificacion() {
		return this.valorModificacion;
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

	public void setMoneda(String value) {
		this.moneda = value;
	}

	public void setMotivo(String value) {
		this.motivo = value;
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

	public void setTotalConImpuestos(TotalConImpuestos value) {
		this.totalConImpuestos = value;
	}

	public void setTotalSinImpuestos(BigDecimal value) {
		this.totalSinImpuestos = value;
	}

	public void setValorModificacion(BigDecimal value) {
		this.valorModificacion = value;
	}

}
