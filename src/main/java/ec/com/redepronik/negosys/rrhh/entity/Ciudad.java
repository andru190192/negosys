package ec.com.redepronik.negosys.rrhh.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ciudad")
public class Ciudad implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean activo;
	private String nombre;
	private Provincia provincia;
	private List<Persona> personas;

	public Ciudad() {
	}

	public Ciudad(Integer id, Boolean activo, String nombre,
			Provincia provincia, List<Persona> personas) {
		this.id = id;
		this.activo = activo;
		this.nombre = nombre;
		this.provincia = provincia;
		this.personas = personas;
	}

	public Persona addPersona(Persona persona) {
		getPersonas().add(persona);
		persona.setCiudad(this);

		return persona;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ciudad other = (Ciudad) obj;
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

	@Id
	@SequenceGenerator(allocationSize = 1, name = "CIUDAD_CIUDADID_GENERATOR", sequenceName = "CIUDAD_CIUDADID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CIUDAD_CIUDADID_GENERATOR")
	@Column(name = "ciudadid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "ciudad")
	public List<Persona> getPersonas() {
		return this.personas;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "provinciaid", nullable = false)
	public Provincia getProvincia() {
		return this.provincia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setCiudad(null);

		return persona;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}