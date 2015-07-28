package ec.com.redepronik.negosys.rrhh.entity;

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
@Table(name = "cargo")
public class Cargo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean activo;
	private String nombre;
	private Boolean porDefecto;
	private List<EmpleadoCargo> empleadoCargos;

	public Cargo() {
	}

	public Cargo(Integer id, Boolean activo, String nombre, Boolean porDefecto,
			List<EmpleadoCargo> empleadoCargos) {
		this.id = id;
		this.activo = activo;
		this.nombre = nombre;
		this.porDefecto = porDefecto;
		this.empleadoCargos = empleadoCargos;
	}

	public EmpleadoCargo addEmpleadocargo(EmpleadoCargo empleadoCargo) {
		getEmpleadoCargos().add(empleadoCargo);
		empleadoCargo.setCargo(this);

		return empleadoCargo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cargo other = (Cargo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return this.activo;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cargo")
	public List<EmpleadoCargo> getEmpleadoCargos() {
		return this.empleadoCargos;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "CARGO_CARGOID_GENERATOR", sequenceName = "CARGO_CARGOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARGO_CARGOID_GENERATOR")
	@Column(name = "cargoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	@Column(name = "pordefecto", nullable = false)
	public Boolean getPorDefecto() {
		return porDefecto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public EmpleadoCargo removeEmpleadocargo(EmpleadoCargo empleadoCargo) {
		getEmpleadoCargos().remove(empleadoCargo);
		empleadoCargo.setCargo(null);

		return empleadoCargo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setEmpleadoCargos(List<EmpleadoCargo> empleadoCargos) {
		this.empleadoCargos = empleadoCargos;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPorDefecto(Boolean porDefecto) {
		this.porDefecto = porDefecto;
	}

}