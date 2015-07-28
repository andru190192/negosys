package ec.com.redepronik.negosys.invfac.entityAux;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonaCedulaNombre {

	@Id
	private Integer id;
	private String cedula;
	private String nombre;
	private String apellido;

	public PersonaCedulaNombre() {
	}

	public PersonaCedulaNombre(Integer vendedorId, String cedula,
			String nombre, String apellido) {
		setId(vendedorId);
		setCedula(cedula);
		setNombre(nombre);
		setApellido(apellido);
	}

	public String getApellido() {
		return apellido;
	}

	public String getCedula() {
		return cedula;
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
