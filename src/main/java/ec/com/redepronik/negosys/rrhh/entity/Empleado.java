package ec.com.redepronik.negosys.rrhh.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "empleado")
public class Empleado implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean activo;
	private String folio;
	private Persona persona;
	private List<EmpleadoCargo> empleadoCargos;

	public Empleado() {
	}

	public Empleado(Integer id, Boolean activo, String folio, Persona persona,
			List<EmpleadoCargo> empleadoCargos) {
		this.id = id;
		this.activo = activo;
		this.folio = folio;
		this.persona = persona;
		this.empleadoCargos = empleadoCargos;
	}

	public EmpleadoCargo addEmpleadoCargo(EmpleadoCargo empleadoCargo) {
		getEmpleadoCargos().add(empleadoCargo);
		empleadoCargo.setEmpleado(this);

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
		Empleado other = (Empleado) obj;
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

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "empleado")
	public List<EmpleadoCargo> getEmpleadoCargos() {
		return this.empleadoCargos;
	}

	@Column(length = 3)
	public String getFolio() {
		return this.folio;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "EMPLEADO_EMPLEADOID_GENERATOR", sequenceName = "EMPLEADO_EMPLEADOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLEADO_EMPLEADOID_GENERATOR")
	@Column(name = "empleadoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@OneToOne
	@JoinColumn(name = "personaid", nullable = false)
	public Persona getPersona() {
		return this.persona;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public EmpleadoCargo removeEmpleadoCargo(EmpleadoCargo empleadoCargo) {
		getEmpleadoCargos().remove(empleadoCargo);
		empleadoCargo.setEmpleado(null);

		return empleadoCargo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setEmpleadoCargos(List<EmpleadoCargo> empleadoCargos) {
		this.empleadoCargos = empleadoCargos;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}