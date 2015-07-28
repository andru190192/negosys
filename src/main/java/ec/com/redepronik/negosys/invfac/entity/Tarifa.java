package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tarifas")
public class Tarifa implements Serializable {
	private static final long serialVersionUID = 1L;

	private Short id;
	private Short idSri;
	private String nombre;
	private Date fechaFin;
	private Date fechaInicio;
	private BigDecimal porcentaje;
	private Impuesto impuesto;
	private List<ProductoTarifa> productosTarifas;

	public Tarifa() {
	}

	public Tarifa(Short id, Short idSri, String nombre, Date fechaFin,
			Date fechaInicio, BigDecimal porcentaje, Impuesto impuesto,
			List<ProductoTarifa> productosTarifas) {
		this.id = id;
		this.idSri = idSri;
		this.nombre = nombre;
		this.fechaFin = fechaFin;
		this.fechaInicio = fechaInicio;
		this.porcentaje = porcentaje;
		this.impuesto = impuesto;
		this.productosTarifas = productosTarifas;
	}

	public ProductoTarifa addProductoTarifa(ProductoTarifa productoTarifa) {
		getProductosTarifas().add(productoTarifa);
		productoTarifa.setTarifa(this);

		return productoTarifa;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tarifa other = (Tarifa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechafin", nullable = false)
	public Date getFechaFin() {
		return fechaFin;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechainicio", nullable = false)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "TARIFAS_TARIFAID_GENERATOR", sequenceName = "TARIFAS_TARIFAID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TARIFAS_TARIFAID_GENERATOR")
	@Column(name = "tarifaid", unique = true, nullable = false)
	public Short getId() {
		return id;
	}

	@Column(name = "id_sri", nullable = false)
	public Short getIdSri() {
		return idSri;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "impuestoid", nullable = false)
	public Impuesto getImpuesto() {
		return impuesto;
	}

	@Column(nullable = false, length = 250)
	public String getNombre() {
		return nombre;
	}

	@Column(nullable = false, precision = 5, scale = 2)
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "tarifa")
	public List<ProductoTarifa> getProductosTarifas() {
		return productosTarifas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public ProductoTarifa removeProductoTarifa(ProductoTarifa productoTarifa) {
		getProductosTarifas().remove(productoTarifa);
		productoTarifa.setTarifa(null);

		return productoTarifa;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setIdSri(Short idSri) {
		this.idSri = idSri;
	}

	public void setImpuesto(Impuesto impuesto) {
		this.impuesto = impuesto;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public void setProductosTarifas(List<ProductoTarifa> productosTarifas) {
		this.productosTarifas = productosTarifas;
	}

}