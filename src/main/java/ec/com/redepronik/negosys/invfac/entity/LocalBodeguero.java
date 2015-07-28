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

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

@Entity
@Table(name = "localesbodegueros")
public class LocalBodeguero implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private EmpleadoCargo empleadoCargo;
	private Local local;

	@Column(nullable = false)
	private Boolean activo;

	public LocalBodeguero() {
	}

	public LocalBodeguero(Integer id, EmpleadoCargo empleadoCargo, Local local,
			Boolean activo) {
		this.id = id;
		this.empleadoCargo = empleadoCargo;
		this.local = local;
		this.activo = activo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalBodeguero other = (LocalBodeguero) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getActivo() {
		return activo;
	}

	// bi-directional many-to-one association to empleadoCargo
	@ManyToOne
	@JoinColumn(name = "empleadocargoid", nullable = false)
	public EmpleadoCargo getEmpleadoCargo() {
		return empleadoCargo;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "BODEGAEMPLEADOCARGO_BODEGAEMPLEADOCARGOID_GENERATOR", sequenceName = "BODEGAEMPLEADOCARGO_BODEGAEMPLEADOCARGOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BODEGAEMPLEADOCARGO_BODEGAEMPLEADOCARGOID_GENERATOR")
	@Column(name = "localbodegueroid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	// bi-directional many-to-one association to Bodega
	@ManyToOne
	@JoinColumn(name = "localid", nullable = false)
	public Local getLocal() {
		return local;
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

	public void setEmpleadoCargo(EmpleadoCargo empleadoCargo) {
		this.empleadoCargo = empleadoCargo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

}