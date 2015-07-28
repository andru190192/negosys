package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mora")
public class Mora implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Date fechaFin;
	private Date fechaInicio;
	private BigDecimal porcentaje;

	public Mora() {
	}

	public Mora(Integer id, Date fechaFin, Date fechaInicio,
			BigDecimal porcentaje) {
		this.id = id;
		this.fechaFin = fechaFin;
		this.fechaInicio = fechaInicio;
		this.porcentaje = porcentaje;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mora other = (Mora) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechafin", nullable = false)
	public Date getFechaFin() {
		return fechaFin;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechainicio", nullable = false)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "MORA_MORAID_GENERATOR", sequenceName = "MORA_MORAID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MORA_MORAID_GENERATOR")
	@Column(name = "moraid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false, precision = 5, scale = 2)
	public BigDecimal getPorcentaje() {
		return this.porcentaje;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

}