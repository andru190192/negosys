package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "productostarifas")
public class ProductoTarifa implements Serializable {
	private static final long serialVersionUID = 1L;

	private Short id;
	private Integer orden;
	private Boolean activo;
	private Tarifa tarifa;
	private Producto producto;

	public ProductoTarifa() {
	}

	public ProductoTarifa(Short id, Integer orden, Boolean activo,
			Tarifa tarifa, Producto producto) {
		this.id = id;
		this.orden = orden;
		this.activo = activo;
		this.tarifa = tarifa;
		this.producto = producto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductoTarifa other = (ProductoTarifa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return activo;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PRODUCTOSTARIFAS_PRODUCTOTARIFAID_GENERATOR", sequenceName = "PRODUCTOSTARIFAS_PRODUCTOTARIFAID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTOSTARIFAS_PRODUCTOTARIFAID_GENERATOR")
	@Column(name = "productotarifaid", unique = true, nullable = false)
	public Short getId() {
		return id;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	// bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	public Producto getProducto() {
		return producto;
	}

	@ManyToOne
	@JoinColumn(name = "tarifaid", nullable = false)
	public Tarifa getTarifa() {
		return tarifa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setTarifa(Tarifa tarifa) {
		this.tarifa = tarifa;
	}

}