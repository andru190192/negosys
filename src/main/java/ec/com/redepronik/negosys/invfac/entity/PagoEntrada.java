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

@Entity
@Table(name = "pagoentrada")
public class PagoEntrada implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Boolean anulado;
	private String banco;
	private String chequeVaucher;
	private String cuentaTarjeta;
	private BigDecimal cuota;
	private Boolean activo;
	private String detalle;
	private Date fechaCheque;
	private Date fechaGiro;
	private Timestamp fechaPago;
	private Boolean pagado;
	private Entrada entrada;
	private TipoPago tipoPago;

	public PagoEntrada() {
	}

	public PagoEntrada(Integer id, Boolean anulado, String banco,
			String chequeVaucher, String cuentaTarjeta, BigDecimal cuota,
			Boolean activo, String detalle, Date fechaCheque, Date fechaGiro,
			Timestamp fechaPago, Boolean pagado, Entrada entrada,
			TipoPago tipoPago) {
		this.id = id;
		this.anulado = anulado;
		this.banco = banco;
		this.chequeVaucher = chequeVaucher;
		this.cuentaTarjeta = cuentaTarjeta;
		this.cuota = cuota;
		this.activo = activo;
		this.detalle = detalle;
		this.fechaCheque = fechaCheque;
		this.fechaGiro = fechaGiro;
		this.fechaPago = fechaPago;
		this.pagado = pagado;
		this.entrada = entrada;
		this.tipoPago = tipoPago;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagoEntrada other = (PagoEntrada) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return activo;
	}

	public Boolean getAnulado() {
		return this.anulado;
	}

	@Column(length = 25)
	public String getBanco() {
		return this.banco;
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
		return this.cuota;
	}

	@Column(length = 50)
	public String getDetalle() {
		return this.detalle;
	}

	@ManyToOne
	@JoinColumn(name = "entradaid", nullable = false)
	public Entrada getEntrada() {
		return this.entrada;
	}

	@Column(name = "fechacheque")
	@Temporal(TemporalType.DATE)
	public Date getFechaCheque() {
		return fechaCheque;
	}

	@Column(name = "fechagiro")
	@Temporal(TemporalType.DATE)
	public Date getFechaGiro() {
		return fechaGiro;
	}

	@Column(name = "fechapago")
	public Timestamp getFechaPago() {
		return fechaPago;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PAGOENTRADA_PAGOENTRADAID_GENERATOR", sequenceName = "PAGOENTRADA_PAGOENTRADAID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOENTRADA_PAGOENTRADAID_GENERATOR")
	@Column(name = "pagoentradaid", unique = true, nullable = false)
	public Integer getId() {
		return id;
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

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setAnulado(Boolean anulado) {
		this.anulado = anulado;
	}

	public void setBanco(String banco) {
		this.banco = banco;
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

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public void setFechaCheque(Date fechaCheque) {
		this.fechaCheque = fechaCheque;
	}

	public void setFechaGiro(Date fechaGiro) {
		this.fechaGiro = fechaGiro;
	}

	public void setFechaPago(Timestamp fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPagado(Boolean pagado) {
		this.pagado = pagado;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

}
