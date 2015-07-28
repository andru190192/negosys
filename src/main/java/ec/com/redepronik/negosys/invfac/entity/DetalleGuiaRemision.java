package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "detalleguiaremision")
public class DetalleGuiaRemision implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer cantidad;
	private String descripcion;
	private GuiaRemision guiaRemision;

	public DetalleGuiaRemision() {
	}

	public DetalleGuiaRemision(Integer id, Integer cantidad,
			String descripcion, GuiaRemision guiaRemision) {
		this.id = id;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.guiaRemision = guiaRemision;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleGuiaRemision other = (DetalleGuiaRemision) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Integer getCantidad() {
		return this.cantidad;
	}

	@Column(nullable = false, length = 100)
	public String getDescripcion() {
		return this.descripcion;
	}

	@ManyToOne
	@JoinColumn(name = "guiaremisionid", nullable = false)
	public GuiaRemision getGuiaRemision() {
		return guiaRemision;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "DETALLEGUIAREMISION_DETALLEGUIAREMISIONID_GENERATOR", sequenceName = "DETALLEGUIAREMISION_DETALLEGUIAREMISIONID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLEGUIAREMISION_DETALLEGUIAREMISIONID_GENERATOR")
	@Column(name = "detalleguiaremisionid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setGuiaRemision(GuiaRemision guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
