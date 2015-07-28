package ec.com.redepronik.negosys.seguridad.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "rol")
public class Rol implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nombre;
	private String descripcion;
	private List<RolMenu> rolMenus;
	private List<RolUsuario> rolUsuarios;

	public Rol() {
	}

	public Rol(Integer id, String nombre, String descripcion,
			List<RolMenu> rolMenus, List<RolUsuario> rolUsuarios) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.rolMenus = rolMenus;
		this.rolUsuarios = rolUsuarios;
	}

	public RolMenu addRolMenus(RolMenu rolMenus) {
		getRolMenus().add(rolMenus);
		rolMenus.setRol(this);

		return rolMenus;
	}

	public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().add(rolUsuario);
		rolUsuario.setRol(this);

		return rolUsuario;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rol other = (Rol) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(length = 100)
	public String getDescripcion() {
		return descripcion;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "ROL_ROLID_GENERATOR", sequenceName = "ROL_ROLID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROL_ROLID_GENERATOR")
	@Column(name = "rolid", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@Column(nullable = false, length = 20)
	public String getNombre() {
		return this.nombre;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "rol")
	public List<RolMenu> getRolMenus() {
		return this.rolMenus;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "rol")
	public List<RolUsuario> getRolUsuarios() {
		return this.rolUsuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public RolMenu removeRolMenus(RolMenu rolMenus) {
		getRolMenus().remove(rolMenus);
		rolMenus.setRol(null);

		return rolMenus;
	}

	public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().remove(rolUsuario);
		rolUsuario.setRol(null);

		return rolUsuario;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setRolMenus(List<RolMenu> rolMenus) {
		this.rolMenus = rolMenus;
	}

	public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
		this.rolUsuarios = rolUsuarios;
	}

}