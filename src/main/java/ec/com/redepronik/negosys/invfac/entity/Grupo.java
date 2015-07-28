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

@Entity
@Table(name = "grupo")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Boolean activo;
	private String nombre;
	private Integer contador;
	private List<Producto> productos;

	public Grupo() {
	}

	public Grupo(Integer id, Boolean activo, String nombre, Integer contador,
			List<Producto> productos) {
		this.id = id;
		this.activo = activo;
		this.nombre = nombre;
		this.contador = contador;
		this.productos = productos;
	}

	public Producto addProducto(Producto producto) {
		getProductos().add(producto);
		producto.setGrupo(this);

		return producto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grupo other = (Grupo) obj;
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

	@Column(nullable = false)
	public Integer getContador() {
		return contador;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "GRUPO_GRUPOID_GENERATOR", sequenceName = "GRUPO_GRUPOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_GRUPOID_GENERATOR")
	@Column(name = "grupoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "grupo")
	public List<Producto> getProductos() {
		return this.productos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Producto removeProducto(Producto producto) {
		getProductos().remove(producto);
		producto.setGrupo(null);

		return producto;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setContador(Integer contador) {
		this.contador = contador;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

}