package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

@Entity
@Table(name = "bajasinventarios")
public class BajaInventario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private EmpleadoCargo bodeguero;
	private Timestamp fecha;
	private String establecimiento;
	private String puntoEmision;
	private String secuencia;
	private List<DetalleBajaInventario> detalleBajaInventario;

	public BajaInventario() {
	}

	public BajaInventario(Integer id, EmpleadoCargo bodeguero, Timestamp fecha,
			String establecimiento, String puntoEmision, String secuencia,
			List<DetalleBajaInventario> detalleBajaInventario) {
		this.id = id;
		this.bodeguero = bodeguero;
		this.fecha = fecha;
		this.establecimiento = establecimiento;
		this.puntoEmision = puntoEmision;
		this.secuencia = secuencia;
		this.detalleBajaInventario = detalleBajaInventario;
	}

	public DetalleBajaInventario addDetalleBajaInventario(
			DetalleBajaInventario detalleBajaInventario) {
		getDetalleBajaInventario().add(detalleBajaInventario);
		detalleBajaInventario.setBajaInventario(this);

		return detalleBajaInventario;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BajaInventario other = (BajaInventario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@ManyToOne
	@JoinColumn(name = "bodegueroid")
	public EmpleadoCargo getBodeguero() {
		return bodeguero;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "bajaInventario")
	// @IndexColumn(name = "orden", base = 1)
	public List<DetalleBajaInventario> getDetalleBajaInventario() {
		return detalleBajaInventario;
	}

	@Column(length = 3, nullable = false)
	public String getEstablecimiento() {
		return establecimiento;
	}

	@Column(nullable = false)
	public Timestamp getFecha() {
		return fecha;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "BAJASINVENTARIOS_BAJAINVENTARIOID_GENERATOR", sequenceName = "BAJASINVENTARIOS_BAJAINVENTARIOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BAJASINVENTARIOS_BAJAINVENTARIOID_GENERATOR")
	@Column(name = "bajainventarioid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(name = "puntoemision", length = 3, nullable = false)
	public String getPuntoEmision() {
		return puntoEmision;
	}

	@Column(length = 9, nullable = false)
	public String getSecuencia() {
		return secuencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DetalleBajaInventario removeDetalleBajaInventario(
			DetalleBajaInventario detalleBajaInventario) {
		getDetalleBajaInventario().remove(detalleBajaInventario);
		detalleBajaInventario.setBajaInventario(null);

		return detalleBajaInventario;
	}

	public void setBodeguero(EmpleadoCargo bodeguero) {
		this.bodeguero = bodeguero;
	}

	public void setDetalleBajaInventario(
			List<DetalleBajaInventario> detalleBajaInventario) {
		this.detalleBajaInventario = detalleBajaInventario;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

}