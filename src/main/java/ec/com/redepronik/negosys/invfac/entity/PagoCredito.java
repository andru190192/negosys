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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "pagocredito")
public class PagoCredito implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean anulado;
	private String banco;
	private String chequeVaucher;
	private String cuentaTarjeta;
	private BigDecimal cuota;
	private String detalle;
	private Integer orden;
	private Date fechaCheque;
	private Date fechaGiro;
	private Date fechaPago;
	private Boolean pagado;
	private DetalleCredito detalleCredito;
	private TipoPago tipoPago;

	public PagoCredito() {
	}

	public PagoCredito(Integer id, Boolean anulado, String banco,
			String chequeVaucher, String cuentaTarjeta, BigDecimal cuota,
			String detalle, Integer orden, Date fechaCheque, Date fechaGiro,
			Date fechaPago, Boolean pagado, DetalleCredito detalleCredito,
			TipoPago tipoPago) {
		this.id = id;
		this.anulado = anulado;
		this.banco = banco;
		this.chequeVaucher = chequeVaucher;
		this.cuentaTarjeta = cuentaTarjeta;
		this.cuota = cuota;
		this.detalle = detalle;
		this.orden = orden;
		this.fechaCheque = fechaCheque;
		this.fechaGiro = fechaGiro;
		this.fechaPago = fechaPago;
		this.pagado = pagado;
		this.detalleCredito = detalleCredito;
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
		PagoCredito other = (PagoCredito) obj;
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
	@JoinColumn(name = "detallecreditoid", nullable = false)
	public DetalleCredito getDetalleCredito() {
		return detalleCredito;
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
	@Temporal(TemporalType.DATE)
	public Date getFechaPago() {
		return fechaPago;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PAGOCREDITO_PAGOCREDITOID_GENERATOR", sequenceName = "PAGOCREDITO_PAGOCREDITOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOCREDITO_PAGOCREDITOID_GENERATOR")
	@Column(name = "pagocreditoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
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

	public void setDetalleCredito(DetalleCredito detalleCredito) {
		this.detalleCredito = detalleCredito;
	}

	public void setFechaCheque(Date fechaCheque) {
		this.fechaCheque = fechaCheque;
	}

	public void setFechaGiro(Date fechaGiro) {
		this.fechaGiro = fechaGiro;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPagado(Boolean pagado) {
		this.pagado = pagado;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

}
