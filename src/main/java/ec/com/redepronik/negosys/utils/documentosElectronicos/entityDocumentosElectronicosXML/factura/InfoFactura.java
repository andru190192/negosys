package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.TotalConImpuestos;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fechaEmision", "dirEstablecimiento",
		"contribuyenteEspecial", "obligadoContabilidad",
		"tipoIdentificacionComprador", "guiaRemision", "razonSocialComprador",
		"identificacionComprador", "totalSinImpuestos", "totalDescuento",
		"totalConImpuestos", "propina", "importeTotal", "moneda" })
public class InfoFactura {
	@XmlElement(required = true)
	protected String fechaEmision;
	@XmlElement(required = true)
	protected String dirEstablecimiento;
	protected String contribuyenteEspecial;
	protected String obligadoContabilidad;
	@XmlElement(required = true)
	protected String tipoIdentificacionComprador;
	protected String guiaRemision;
	@XmlElement(required = true)
	protected String razonSocialComprador;
	@XmlElement(required = true)
	protected String identificacionComprador;
	@XmlElement(required = true)
	protected BigDecimal totalSinImpuestos;
	@XmlElement(required = true)
	protected BigDecimal totalDescuento;
	@XmlElement(required = true)
	protected TotalConImpuestos totalConImpuestos;
	@XmlElement(required = true)
	protected BigDecimal propina;
	@XmlElement(required = true)
	protected BigDecimal importeTotal;
	protected String moneda;

	public String getContribuyenteEspecial() {
		return this.contribuyenteEspecial;
	}

	public String getDirEstablecimiento() {
		return this.dirEstablecimiento;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public String getGuiaRemision() {
		return this.guiaRemision;
	}

	public String getIdentificacionComprador() {
		return this.identificacionComprador;
	}

	public BigDecimal getImporteTotal() {
		return this.importeTotal;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public String getObligadoContabilidad() {
		return this.obligadoContabilidad;
	}

	public BigDecimal getPropina() {
		return this.propina;
	}

	public String getRazonSocialComprador() {
		return this.razonSocialComprador;
	}

	public String getTipoIdentificacionComprador() {
		return this.tipoIdentificacionComprador;
	}

	public TotalConImpuestos getTotalConImpuestos() {
		return this.totalConImpuestos;
	}

	public BigDecimal getTotalDescuento() {
		return this.totalDescuento;
	}

	public BigDecimal getTotalSinImpuestos() {
		return this.totalSinImpuestos;
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

	public void setGuiaRemision(String value) {
		this.guiaRemision = value;
	}

	public void setIdentificacionComprador(String value) {
		this.identificacionComprador = value;
	}

	public void setImporteTotal(BigDecimal value) {
		this.importeTotal = value;
	}

	public void setMoneda(String value) {
		this.moneda = value;
	}

	public void setObligadoContabilidad(String value) {
		this.obligadoContabilidad = value;
	}

	public void setPropina(BigDecimal value) {
		this.propina = value;
	}

	public void setRazonSocialComprador(String value) {
		this.razonSocialComprador = value;
	}

	public void setTipoIdentificacionComprador(String value) {
		this.tipoIdentificacionComprador = value;
	}

	public void setTotalConImpuestos(TotalConImpuestos value) {
		this.totalConImpuestos = value;
	}

	public void setTotalDescuento(BigDecimal value) {
		this.totalDescuento = value;
	}

	public void setTotalSinImpuestos(BigDecimal value) {
		this.totalSinImpuestos = value;
	}

}
