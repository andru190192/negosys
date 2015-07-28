package ec.com.redepronik.negosys.invfac.entityAux;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import ec.com.redepronik.negosys.invfac.entity.ImpuestoRetencion;
import ec.com.redepronik.negosys.invfac.entity.TarifaRetencion;

@Entity
public class Retenciones {

	@Id
	private Integer id;
	private String numeroRetencion;
	private Date fechaRetencion;
	private String proveedor;
	private String numeroComprobanteRetenido;
	private Date fechaComprobanteRetenido;
	private ImpuestoRetencion impuesto;
	private TarifaRetencion tarifa;
	private BigDecimal baseImponible;
	private BigDecimal porcentajeRetencion;
	private BigDecimal subTotal;

	public Retenciones() {

	}

	public Retenciones(Integer id, String numeroRetencion, Date fechaRetencion,
			String proveedor, String numeroComprobanteRetenido,
			Date fechaComprobanteRetenido, ImpuestoRetencion impuesto,
			TarifaRetencion tarifa, BigDecimal baseImponible,
			BigDecimal porcentajeRetencion, BigDecimal subTotal) {
		this.id = id;
		this.numeroRetencion = numeroRetencion;
		this.fechaRetencion = fechaRetencion;
		this.proveedor = proveedor;
		this.numeroComprobanteRetenido = numeroComprobanteRetenido;
		this.fechaComprobanteRetenido = fechaComprobanteRetenido;
		this.impuesto = impuesto;
		this.tarifa = tarifa;
		this.baseImponible = baseImponible;
		this.porcentajeRetencion = porcentajeRetencion;
		this.subTotal = subTotal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroRetencion() {
		return numeroRetencion;
	}

	public void setNumeroRetencion(String numeroRetencion) {
		this.numeroRetencion = numeroRetencion;
	}

	public Date getFechaRetencion() {
		return fechaRetencion;
	}

	public void setFechaRetencion(Date fechaRetencion) {
		this.fechaRetencion = fechaRetencion;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getNumeroComprobanteRetenido() {
		return numeroComprobanteRetenido;
	}

	public void setNumeroComprobanteRetenido(String numeroComprobanteRetenido) {
		this.numeroComprobanteRetenido = numeroComprobanteRetenido;
	}

	public Date getFechaComprobanteRetenido() {
		return fechaComprobanteRetenido;
	}

	public void setFechaComprobanteRetenido(Date fechaComprobanteRetenido) {
		this.fechaComprobanteRetenido = fechaComprobanteRetenido;
	}

	public ImpuestoRetencion getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(ImpuestoRetencion impuesto) {
		this.impuesto = impuesto;
	}

	public TarifaRetencion getTarifa() {
		return tarifa;
	}

	public void setTarifa(TarifaRetencion tarifa) {
		this.tarifa = tarifa;
	}

	public BigDecimal getBaseImponible() {
		return baseImponible;
	}

	public void setBaseImponible(BigDecimal baseImponible) {
		this.baseImponible = baseImponible;
	}

	public BigDecimal getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

}
