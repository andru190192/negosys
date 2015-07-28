package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the tipoprecioproducto database table.
 * 
 */
@Entity
@Table(name = "tipoprecioproducto")
public class TipoPrecioProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nombre;
	private BigDecimal valor;
	private Integer orden;
	private Boolean pvp;
	private Boolean porcentajePrecioFijo;
	private Producto producto;

	public TipoPrecioProducto() {
	}

	public TipoPrecioProducto(Integer id, String nombre, BigDecimal valor,
			Integer orden, Boolean pvp, Producto producto,
			Boolean porcentajePrecioFijo) {
		this.id = id;
		this.nombre = nombre;
		this.valor = valor;
		this.orden = orden;
		this.pvp = pvp;
		this.producto = producto;
		this.porcentajePrecioFijo = porcentajePrecioFijo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoPrecioProducto other = (TipoPrecioProducto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "TIPOPRECIOPRODUCTO_TIPOPRECIOPRODUCTO_GENERATOR", sequenceName = "TIPOPRECIOPRODUCTO_TIPOPRECIOPRODUCTOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOPRECIOPRODUCTO_TIPOPRECIOPRODUCTO_GENERATOR")
	@Column(name = "tipoprecioproductoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	@Column(name = "porcentajepreciofijo", nullable = false)
	public Boolean getPorcentajePrecioFijo() {
		return porcentajePrecioFijo;
	}

	// bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	public Producto getProducto() {
		return this.producto;
	}

	@Column(nullable = false)
	public Boolean getPvp() {
		return pvp;
	}

	@Column(nullable = false, precision = 12, scale = 6)
	public BigDecimal getValor() {
		return this.valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setPorcentajePrecioFijo(Boolean porcentajePrecioFijo) {
		this.porcentajePrecioFijo = porcentajePrecioFijo;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setPvp(Boolean pvp) {
		this.pvp = pvp;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}