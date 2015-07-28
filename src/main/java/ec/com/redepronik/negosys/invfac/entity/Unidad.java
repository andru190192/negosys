package ec.com.redepronik.negosys.invfac.entity;

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

/**
 * The persistent class for the unidad database table.
 * 
 */
@Entity
@Table(name = "unidad")
public class Unidad implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String abreviatura;
	private Boolean activo;
	private String nombre;
	private Boolean pordefecto;
	private List<ProductoUnidad> ProductoUnidades;

	public Unidad() {
	}

	public Unidad(Integer id, String abreviatura, Boolean activo,
			String nombre, Boolean pordefecto,
			List<ProductoUnidad> ProductoUnidades) {
		this.id = id;
		this.abreviatura = abreviatura;
		this.activo = activo;
		this.nombre = nombre;
		this.pordefecto = pordefecto;
		this.ProductoUnidades = ProductoUnidades;
	}

	public ProductoUnidad addProductoUnidad(ProductoUnidad ProductoUnidad) {
		getProductoUnidades().add(ProductoUnidad);
		ProductoUnidad.setUnidad(this);

		return ProductoUnidad;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unidad other = (Unidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false, length = 5)
	public String getAbreviatura() {
		return this.abreviatura;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return this.activo;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "UNIDAD_UNIDADID_GENERATOR", sequenceName = "UNIDAD_UNIDADID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIDAD_UNIDADID_GENERATOR")
	@Column(name = "unidadid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false, length = 15)
	public String getNombre() {
		return this.nombre;
	}

	@Column(nullable = false)
	public Boolean getPordefecto() {
		return this.pordefecto;
	}

	// bi-directional many-to-one association to ProductoUnidad
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "unidad")
	public List<ProductoUnidad> getProductoUnidades() {
		return ProductoUnidades;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public ProductoUnidad removeProductoUnidad(ProductoUnidad ProductoUnidad) {
		getProductoUnidades().remove(ProductoUnidad);
		ProductoUnidad.setUnidad(null);

		return ProductoUnidad;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
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

	public void setPordefecto(Boolean pordefecto) {
		this.pordefecto = pordefecto;
	}

	public void setProductoUnidades(List<ProductoUnidad> ProductoUnidades) {
		this.ProductoUnidades = ProductoUnidades;
	}

}