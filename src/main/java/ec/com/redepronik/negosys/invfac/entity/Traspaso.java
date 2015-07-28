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

/**
 * The persistent class for the traspaso database table.
 * 
 */
@Entity
@Table(name = "traspaso")
public class Traspaso implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Timestamp fecha;
	private String establecimiento;
	private String puntoEmision;
	private String secuencia;
	private List<DetalleTraspaso> detalleTraspasos;
	private Local local1;
	private Local local2;

	public Traspaso() {
	}

	public Traspaso(Integer id, Timestamp fecha, String establecimiento,
			String puntoEmision, String secuencia,
			List<DetalleTraspaso> detalleTraspasos, Local local1, Local local2) {
		this.id = id;
		this.fecha = fecha;
		this.establecimiento = establecimiento;
		this.puntoEmision = puntoEmision;
		this.secuencia = secuencia;
		this.detalleTraspasos = detalleTraspasos;
		this.local1 = local1;
		this.local2 = local2;
	}

	public DetalleTraspaso addDetalleTraspaso(DetalleTraspaso detalletraspaso) {
		getDetalleTraspasos().add(detalletraspaso);
		detalletraspaso.setTraspaso(this);

		return detalletraspaso;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traspaso other = (Traspaso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// bi-directional many-to-one association to Detalletraspaso
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "traspaso")
	public List<DetalleTraspaso> getDetalleTraspasos() {
		return detalleTraspasos;
	}

	@Column(length = 3, nullable = false)
	public String getEstablecimiento() {
		return establecimiento;
	}

	@Column(nullable = false)
	public Timestamp getFecha() {
		return this.fecha;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "TRASPASO_TRASPASOID_GENERATOR", sequenceName = "TRASPASO_TRASPASOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRASPASO_TRASPASOID_GENERATOR")
	@Column(name = "traspasoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	// bi-directional many-to-one association to Bodega
	@ManyToOne
	@JoinColumn(name = "localorigenid", nullable = false)
	public Local getLocal1() {
		return local1;
	}

	// bi-directional many-to-one association to Bodega
	@ManyToOne
	@JoinColumn(name = "localdestinoid", nullable = false)
	public Local getLocal2() {
		return local2;
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

	public DetalleTraspaso removeDetalleTraspaso(DetalleTraspaso detalletraspaso) {
		getDetalleTraspasos().remove(detalletraspaso);
		detalletraspaso.setTraspaso(null);

		return detalletraspaso;
	}

	public void setDetalleTraspasos(List<DetalleTraspaso> detalleTraspasos) {
		this.detalleTraspasos = detalleTraspasos;
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

	public void setLocal1(Local local1) {
		this.local1 = local1;
	}

	public void setLocal2(Local local2) {
		this.local2 = local2;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

}