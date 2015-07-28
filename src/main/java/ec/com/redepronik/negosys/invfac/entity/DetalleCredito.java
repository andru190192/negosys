package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "detallecredito")
public class DetalleCredito implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private BigDecimal cuota;
	private Date fechaLimite;
	private Date fechaMora;
	private Date fechaPago;
	private BigDecimal mora;
	private Integer orden;
	private Boolean pagado;
	private BigDecimal saldo;
	private Credito credito;
	private List<PagoCredito> pagoCreditos;

	public DetalleCredito() {
	}

	public DetalleCredito(Integer id, BigDecimal cuota, Date fechaLimite,
			Date fechaMora, Date fechaPago, BigDecimal mora, Integer orden,
			Boolean pagado, BigDecimal saldo, Credito credito,
			List<PagoCredito> pagoCreditos) {
		this.id = id;
		this.cuota = cuota;
		this.fechaLimite = fechaLimite;
		this.fechaMora = fechaMora;
		this.fechaPago = fechaPago;
		this.mora = mora;
		this.orden = orden;
		this.pagado = pagado;
		this.saldo = saldo;
		this.credito = credito;
		this.pagoCreditos = pagoCreditos;
	}

	public PagoCredito addPagocredito(PagoCredito pagoCredito) {
		getPagoCreditos().add(pagoCredito);
		pagoCredito.setDetalleCredito(this);

		return pagoCredito;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleCredito other = (DetalleCredito) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@ManyToOne
	@JoinColumn(name = "creditoid", nullable = false)
	public Credito getCredito() {
		return this.credito;
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public BigDecimal getCuota() {
		return this.cuota;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechalimite", nullable = false)
	public Date getFechaLimite() {
		return fechaLimite;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechamora", nullable = false)
	public Date getFechaMora() {
		return fechaMora;
	}

	@Column(name = "fechapago")
	@Temporal(TemporalType.DATE)
	public Date getFechaPago() {
		return fechaPago;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "DETALLECREDITO_DETALLECREDITOID_GENERATOR", sequenceName = "DETALLECREDITO_DETALLECREDITOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLECREDITO_DETALLECREDITOID_GENERATOR")
	@Column(name = "detallecreditoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(precision = 8, scale = 2)
	public BigDecimal getMora() {
		return this.mora;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return this.orden;
	}

	@Column(nullable = false)
	public Boolean getPagado() {
		return this.pagado;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "detalleCredito")
	@IndexColumn(name = "orden", base = 1)
	public List<PagoCredito> getPagoCreditos() {
		return pagoCreditos;
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public PagoCredito removePagocredito(PagoCredito pagoCredito) {
		getPagoCreditos().remove(pagoCredito);
		pagoCredito.setDetalleCredito(null);

		return pagoCredito;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public void setFechaMora(Date fechaMora) {
		this.fechaMora = fechaMora;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMora(BigDecimal mora) {
		this.mora = mora;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPagado(Boolean pagado) {
		this.pagado = pagado;
	}

	public void setPagoCreditos(List<PagoCredito> pagoCreditos) {
		this.pagoCreditos = pagoCreditos;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

}
