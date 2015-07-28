package ec.com.redepronik.negosys.seguridad.entity;

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

import ec.com.redepronik.negosys.rrhh.entity.Persona;

@Entity
@Table(name = "rolusuario")
public class RolUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Boolean activo;
	private Rol rol;
	private Persona persona;

	public RolUsuario() {
	}

	public RolUsuario(Integer id, Boolean activo, Rol rol, Persona persona) {
		this.id = id;
		this.activo = activo;
		this.rol = rol;
		this.persona = persona;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RolUsuario other = (RolUsuario) obj;
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

	@Id
	@SequenceGenerator(allocationSize = 1, name = "ROLUSUARIO_ROLUSUARIOID_GENERATOR", sequenceName = "ROLUSUARIO_ROLUSUARIOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLUSUARIO_ROLUSUARIOID_GENERATOR")
	@Column(name = "rolusuarioid", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@ManyToOne
	@JoinColumn(name = "personaid", nullable = false)
	public Persona getPersona() {
		return persona;
	}

	@ManyToOne
	@JoinColumn(name = "rolid", nullable = false)
	public Rol getRol() {
		return this.rol;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
