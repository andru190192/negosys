package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

@Entity
@Table(name = "cuotaingreso")
public class CuotaIngreso implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean anulado;
	private String banco;
	private String chequeVaucher;
	private String cuentaTarjeta;
	private String detalle;
	private Date fechaCheque;
	private Date fechaGiro;
	private Date fechaLimite;
	private Timestamp fechaPago;
	private BigDecimal cuota;
	private Boolean pagado;
	private Ingreso ingreso;
	private TipoPago tipoPago;
	private EmpleadoCargo cajero;

	public CuotaIngreso() {
	}

	public CuotaIngreso(Integer id, Boolean anulado, String banco,
			String chequeVaucher, String cuentaTarjeta, String detalle,
			Date fechaCheque, Date fechaGiro, Date fechaLimite,
			Timestamp fechaPago, BigDecimal cuota, Boolean pagado,
			Ingreso ingreso, TipoPago tipoPago, EmpleadoCargo cajero) {
		this.id = id;
		this.anulado = anulado;
		this.banco = banco;
		this.chequeVaucher = chequeVaucher;
		this.cuentaTarjeta = cuentaTarjeta;
		this.detalle = detalle;
		this.fechaCheque = fechaCheque;
		this.fechaGiro = fechaGiro;
		this.fechaLimite = fechaLimite;
		this.fechaPago = fechaPago;
		this.cuota = cuota;
		this.pagado = pagado;
		this.ingreso = ingreso;
		this.tipoPago = tipoPago;
		this.cajero = cajero;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CuotaIngreso other = (CuotaIngreso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getAnulado() {
		return this.anulado;
	}

	@Column(length = 25)
	public String getBanco() {
		return this.banco;
	}

	@ManyToOne
	@JoinColumn(name = "cajeroid")
	public EmpleadoCargo getCajero() {
		return cajero;
	}

	@Column(name = "chequevaucher", length = 25)
	public String getChequeVaucher() {
		return chequeVaucher;
	}

	@Column(name = "cuentatarjeta", length = 25)
	public String getCuentaTarjeta() {
		return cuentaTarjeta;
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public BigDecimal getCuota() {
		return cuota;
	}

	@Column(length = 50)
	public String getDetalle() {
		return this.detalle;
	}

	@Column(name = "fechacheque")
	public Date getFechaCheque() {
		return fechaCheque;
	}

	@Column(name = "fechagiro")
	@Temporal(TemporalType.DATE)
	public Date getFechaGiro() {
		return fechaGiro;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechalimite", nullable = false)
	public Date getFechaLimite() {
		return fechaLimite;
	}

	@Column(name = "fechapago")
	public Timestamp getFechaPago() {
		return fechaPago;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "CUOTAINGRESO_CUOTAINGRESOID_GENERATOR", sequenceName = "CUOTAINGRESO_CUOTAINGRESOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUOTAINGRESO_CUOTAINGRESOID_GENERATOR")
	@Column(name = "cuotaingresoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name = "ingresoid", nullable = false)
	public Ingreso getIngreso() {
		return this.ingreso;
	}

	@Column(nullable = false)
	public Boolean getPagado() {
		return this.pagado;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tipopagoid", nullable = false)
	public TipoPago getTipoPago() {
		return tipoPago;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setAnulado(Boolean anulado) {
		this.anulado = anulado;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public void setCajero(EmpleadoCargo cajero) {
		this.cajero = cajero;
	}

	public void setChequeVaucher(String chequeVaucher) {
		this.chequeVaucher = chequeVaucher;
	}

	public void setCuentaTarjeta(String cuentaTarjeta) {
		this.cuentaTarjeta = cuentaTarjeta;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public void setFechaCheque(Date fechaCheque) {
		this.fechaCheque = fechaCheque;
	}

	public void setFechaGiro(Date fechaGiro) {
		this.fechaGiro = fechaGiro;
	}

	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public void setFechaPago(Timestamp fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public void setPagado(Boolean pagado) {
		this.pagado = pagado;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

}
