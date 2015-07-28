package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;

@Entity
@Table(name = "retenciones")
public class Retencion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Proveedor proveedor;
	private EmpleadoCargo cajero;
	private Timestamp fechaEmision;
	private String establecimiento;
	private String puntoEmision;
	private String secuencia;
	private Boolean activo;
	private List<DetalleRetencion> detallesRetenciones;

	public Retencion() {
	}

	public Retencion(Integer id, Proveedor proveedor, EmpleadoCargo cajero,
			Timestamp fechaEmision, String establecimiento,
			String puntoEmision, String secuencia, Boolean activo,
			List<DetalleRetencion> detallesRetenciones) {
		this.id = id;
		this.proveedor = proveedor;
		this.cajero = cajero;
		this.fechaEmision = fechaEmision;
		this.establecimiento = establecimiento;
		this.puntoEmision = puntoEmision;
		this.secuencia = secuencia;
		this.activo = activo;
		this.detallesRetenciones = detallesRetenciones;
	}

	public DetalleRetencion addDetalleRetencion(
			DetalleRetencion detalleRetencion) {
		getDetallesRetenciones().add(detalleRetencion);
		detalleRetencion.setRetencion(this);

		return detalleRetencion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Retencion other = (Retencion) obj;
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

	@ManyToOne
	@JoinColumn(name = "cajero", nullable = false)
	public EmpleadoCargo getCajero() {
		return cajero;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "retencion")
	// @IndexColumn(name = "orden", base = 1)
	public List<DetalleRetencion> getDetallesRetenciones() {
		return detallesRetenciones;
	}

	@Column(length = 3, nullable = false)
	public String getEstablecimiento() {
		return establecimiento;
	}

	@Column(name = "fechaemision", nullable = false)
	public Timestamp getFechaEmision() {
		return fechaEmision;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "RETENCIONES_ID_GENERATOR", sequenceName = "RETENCIONES_ID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RETENCIONES_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name = "proveedor")
	public Proveedor getProveedor() {
		return proveedor;
	}

	@Column(name = "puntoemision", length = 3, nullable = false)
	public String getPuntoEmision() {
		return puntoEmision;
	}

	@Column(length = 9, nullable = false)
	public String getSecuencia() {
		return secuencia;
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

	public DetalleRetencion removeDetalleRetencion(
			DetalleRetencion detalleRetencion) {
		getDetallesRetenciones().remove(detalleRetencion);
		detalleRetencion.setRetencion(null);

		return detalleRetencion;
	}

	public void setCajero(EmpleadoCargo cajero) {
		this.cajero = cajero;
	}

	public void setDetallesRetenciones(
			List<DetalleRetencion> detallesRetenciones) {
		this.detallesRetenciones = detallesRetenciones;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setFechaEmision(Timestamp fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
}