package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

@Entity
@Table(name = "entrada")
public class Entrada implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private BigDecimal cuota;
	private Date fechaLimite;
	private Date fechaMora;
	private Boolean activo;
	private Timestamp fechaPago;
	private BigDecimal mora;
	private Boolean pagado;
	private BigDecimal saldo;
	private EmpleadoCargo cajero;
	private Factura factura;
	private List<PagoEntrada> pagoEntradas;
	private Integer orden;

	public Entrada() {
	}

	public Entrada(Integer id, BigDecimal cuota, Date fechaLimite,
			Date fechaMora, Boolean activo, Timestamp fechaPago,
			BigDecimal mora, Boolean pagado, BigDecimal saldo,
			EmpleadoCargo cajero, Factura factura,
			List<PagoEntrada> pagoEntradas, Integer orden) {
		this.id = id;
		this.cuota = cuota;
		this.fechaLimite = fechaLimite;
		this.fechaMora = fechaMora;
		this.activo = activo;
		this.fechaPago = fechaPago;
		this.mora = mora;
		this.pagado = pagado;
		this.saldo = saldo;
		this.cajero = cajero;
		this.factura = factura;
		this.pagoEntradas = pagoEntradas;
		this.orden = orden;
	}

	public PagoEntrada addPagoEntrada(PagoEntrada pagoEntrada) {
		getPagoEntradas().add(pagoEntrada);
		pagoEntrada.setEntrada(this);

		return pagoEntrada;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entrada other = (Entrada) obj;
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

	@ManyToOne
	@JoinColumn(name = "cajeroid")
	public EmpleadoCargo getCajero() {
		return cajero;
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public BigDecimal getCuota() {
		return this.cuota;
	}

	@ManyToOne
	@JoinColumn(name = "facturaid", nullable = false)
	public Factura getFactura() {
		return this.factura;
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
	public Timestamp getFechaPago() {
		return fechaPago;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "ENTRADA_ENTRADAID_GENERATOR", sequenceName = "ENTRADA_ENTRADAID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTRADA_ENTRADAID_GENERATOR")
	@Column(name = "entradaid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(precision = 8, scale = 2)
	public BigDecimal getMora() {
		return this.mora;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	public Boolean getPagado() {
		return this.pagado;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "entrada")
	public List<PagoEntrada> getPagoEntradas() {
		return pagoEntradas;
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

	public PagoEntrada removePagoentrada(PagoEntrada pagoEntrada) {
		getPagoEntradas().remove(pagoEntrada);
		pagoEntrada.setEntrada(null);

		return pagoEntrada;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setCajero(EmpleadoCargo cajero) {
		this.cajero = cajero;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public void setFechaMora(Date fechaMora) {
		this.fechaMora = fechaMora;
	}

	public void setFechaPago(Timestamp fechaPago) {
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

	public void setPagoEntradas(List<PagoEntrada> pagoEntradas) {
		this.pagoEntradas = pagoEntradas;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}