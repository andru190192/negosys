package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "detallesretenciones")
public class DetalleRetencion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Retencion retencion;
	private TipoComprobanteRetencion tipoComprobanteRetencion;
	private String numeroComprobante;
	private String autorizacionSri;
	private Date fechaEmision;
	private ImpuestoRetencion impuestoRetencion;
	private TarifaRetencion tarifa;
	private BigDecimal baseImponible;
	private BigDecimal porcentajeRetencion;

	public DetalleRetencion() {
	}

	public DetalleRetencion(Integer id, Retencion retencion,
			TipoComprobanteRetencion tipoComprobanteRetencion,
			String numeroComprobante, String autorizacionSri, Date fechaEmision,
			ImpuestoRetencion impuestoRetencion, TarifaRetencion tarifa,
			BigDecimal baseImponible, BigDecimal porcentajeRetencion) {
		this.id = id;
		this.retencion = retencion;
		this.tipoComprobanteRetencion = tipoComprobanteRetencion;
		this.numeroComprobante = numeroComprobante;
		this.autorizacionSri=autorizacionSri;
		this.fechaEmision = fechaEmision;
		this.impuestoRetencion = impuestoRetencion;
		this.tarifa = tarifa;
		this.baseImponible = baseImponible;
		this.porcentajeRetencion = porcentajeRetencion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleRetencion other = (DetalleRetencion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(name = "baseimponible", precision = 8, scale = 2, nullable = false)
	public BigDecimal getBaseImponible() {
		return baseImponible;
	}

	@Column(name = "fechaemision", nullable = false)
	public Date getFechaEmision() {
		return fechaEmision;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "DETALLESRETENCIONES_ID_GENERATOR", sequenceName = "DETALLESRETENCIONES_ID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLESRETENCIONES_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "impuesto", nullable = false)
	public ImpuestoRetencion getImpuestoRetencion() {
		return impuestoRetencion;
	}

	@Column(name = "numerocomprobante", length = 15, nullable = false)
	public String getNumeroComprobante() {
		return numeroComprobante;
	}

	@Column(name = "autorizacionsri", length = 40)
	public String getAutorizacionSri() {
		return autorizacionSri;
	}

	public void setAutorizacionSri(String autorizacionSri) {
		this.autorizacionSri = autorizacionSri;
	}

	@Column(name = "porcentajeretencion", precision = 5, scale = 2, nullable = false)
	public BigDecimal getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	@ManyToOne
	@JoinColumn(name = "retencion", nullable = false)
	public Retencion getRetencion() {
		return retencion;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tarifa", nullable = false)
	public TarifaRetencion getTarifa() {
		return tarifa;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tipocomprobante", nullable = false)
	public TipoComprobanteRetencion getTipoComprobanteRetencion() {
		return tipoComprobanteRetencion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setBaseImponible(BigDecimal baseImponible) {
		this.baseImponible = baseImponible;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setImpuestoRetencion(ImpuestoRetencion impuestoRetencion) {
		this.impuestoRetencion = impuestoRetencion;
	}

	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	public void setTarifa(TarifaRetencion tarifa) {
		this.tarifa = tarifa;
	}

	public void setTipoComprobanteRetencion(
			TipoComprobanteRetencion tipoComprobanteRetencion) {
		this.tipoComprobanteRetencion = tipoComprobanteRetencion;
	}
}