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

import org.hibernate.validator.constraints.Length;

import ec.com.redepronik.negosys.invfac.entity.Gastos;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;

@Entity
@Table(name = "proveedor")
public class Proveedor implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean activo;
	private String folio;
	private String nombreComercial;
	private Persona persona;
	private List<Ingreso> ingresos;
	private List<Gastos> gastos;

	public Proveedor() {
	}

	public Proveedor(Integer id, Boolean activo, String folio,
			String nombreComercial, Persona persona, List<Ingreso> ingresos,
			List<Gastos> gastos) {
		this.id = id;
		this.activo = activo;
		this.folio = folio;
		this.nombreComercial = nombreComercial;
		this.persona = persona;
		this.ingresos = ingresos;
		this.gastos = gastos;
	}

	public Ingreso addEgreso(Ingreso ingreso) {
		getIngresos().add(ingreso);
		ingreso.setProveedor(this);

		return ingreso;
	}

	public Gastos addGastos(Gastos gastos) {
		getGastos().add(gastos);
		gastos.setProveedor(this);

		return gastos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proveedor other = (Proveedor) obj;
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

	@Column(length = 3)
	public String getFolio() {
		return this.folio;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "proveedor")
	public List<Gastos> getGastos() {
		return gastos;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PROVEEDOR_PROVEEDORID_GENERATOR", sequenceName = "PROVEEDOR_PROVEEDORID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROVEEDOR_PROVEEDORID_GENERATOR")
	@Column(name = "proveedorid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "proveedor")
	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	@Length(min = 3, max = 50, message = "EL CAMPO NOMBRE COMERCIAL ACEPTA DE 3 A 150 LETRAS")
	@Column(nullable = false, length = 50)
	public String getNombreComercial() {
		return nombreComercial;
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

	public Ingreso removeEgreso(Ingreso ingreso) {
		getIngresos().remove(ingreso);
		ingreso.setProveedor(null);

		return ingreso;
	}

	public Gastos removeGastos(Gastos gastos) {
		getGastos().remove(gastos);
		gastos.setProveedor(null);

		return gastos;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public void setGastos(List<Gastos> gastos) {
		this.gastos = gastos;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}