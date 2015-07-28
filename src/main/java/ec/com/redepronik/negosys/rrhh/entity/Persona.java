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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import ec.com.redepronik.negosys.seguridad.entity.Bitacora;
import ec.com.redepronik.negosys.seguridad.entity.RolUsuario;

@Entity
@Table(name = "persona")
public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean activo;
	private Boolean visible;
	private String apellido;
	private String cedula;
	private String direccion;
	private String nombre;
	private String referencia;
	private Cliente cliente;
	private Empleado empleado;
	private Ciudad ciudad;
	private Proveedor proveedor;
	private String password;
	private String email;
	private String telefono;
	private List<Bitacora> bitacoras;
	private List<RolUsuario> rolUsuarios;

	public Persona() {
	}

	public Persona(Integer id, Boolean activo, Boolean visible,
			String apellido, String cedula, String direccion, String nombre,
			String referencia, Cliente cliente, Empleado empleado,
			Ciudad ciudad, Proveedor proveedor, String password, String email,
			String telefono, List<Bitacora> bitacoras,
			List<RolUsuario> rolUsuarios) {
		this.id = id;
		this.activo = activo;
		this.visible = visible;
		this.apellido = apellido;
		this.cedula = cedula;
		this.direccion = direccion;
		this.nombre = nombre;
		this.referencia = referencia;
		this.cliente = cliente;
		this.empleado = empleado;
		this.ciudad = ciudad;
		this.proveedor = proveedor;
		this.password = password;
		this.email = email;
		this.telefono = telefono;
		this.bitacoras = bitacoras;
		this.rolUsuarios = rolUsuarios;
	}

	public Bitacora addBitacora(Bitacora bitacora) {
		getBitacoras().add(bitacora);
		bitacora.setPersona(this);

		return bitacora;
	}

	public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().add(rolUsuario);
		rolUsuario.setPersona(this);

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
		Persona other = (Persona) obj;
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

	@Pattern(regexp = "[A-Za-z ñÑ]{3,25}", message = "EL CAMPO APELLIDO ACEPTA DE 3 A 50 LETRAS")
	@Column(nullable = false, length = 25)
	public String getApellido() {
		return this.apellido;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "persona")
	public List<Bitacora> getBitacoras() {
		return bitacoras;
	}

	@Pattern(regexp = "[0-9]{10}+|[0-9]{13}+", message = "EL CAMPO CÉDULA ACEPTA DE 10 A 13 DÍGITOS NUMÉRICOS")
	@Column(nullable = false, length = 13)
	public String getCedula() {
		return this.cedula;
	}

	@ManyToOne
	@JoinColumn(name = "ciudadid", nullable = false)
	public Ciudad getCiudad() {
		return this.ciudad;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "persona")
	public Cliente getCliente() {
		return this.cliente;
	}

	@Length(min = 5, max = 100, message = "EL CAMPO DIRECCION ACEPTA DE 5 A 500 LETRAS")
	@Column(length = 100)
	public String getDireccion() {
		return this.direccion;
	}

	@Email(message = "INGRESE UN EMAIL VALIDO")
	public String getEmail() {
		return email;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "persona")
	public Empleado getEmpleado() {
		return this.empleado;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PERSONA_personaId_GENERATOR", sequenceName = "PERSONA_personaId_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONA_personaId_GENERATOR")
	@Column(name = "personaid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Pattern(regexp = "[A-Za-z ñÑ]{3,25}", message = "EL CAMPO NOMBRE ACEPTA DE 3 A 50 LETRAS")
	@Column(nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	public String getPassword() {
		return password;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "persona")
	public Proveedor getProveedor() {
		return this.proveedor;
	}

	@Column(length = 100)
	public String getReferencia() {
		return this.referencia;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "persona")
	public List<RolUsuario> getRolUsuarios() {
		return rolUsuarios;
	}

	public String getTelefono() {
		return telefono;
	}

	@Column(nullable = false)
	public Boolean getVisible() {
		return visible;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DetalleCliente removeAdicional(DetalleCliente detallecliente) {
		getCliente().getDetalleClientes().remove(detallecliente);
		detallecliente.setCliente(null);
		return detallecliente;
	}

	public Bitacora removeBitacora(Bitacora bitacora) {
		getBitacoras().remove(bitacora);
		bitacora.setPersona(null);

		return bitacora;
	}

	public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().remove(rolUsuario);
		rolUsuario.setPersona(null);

		return rolUsuario;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setBitacoras(List<Bitacora> bitacoras) {
		this.bitacoras = bitacoras;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
		this.rolUsuarios = rolUsuarios;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}