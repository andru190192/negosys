package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;

@Entity
@Table(name = "ingreso")
public class Ingreso implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Boolean activo;
	private String codigoDocumento;
	private Date fechaFactura;
	private Timestamp fechaIngreso;
	private Timestamp fechaCierre;
	private Boolean pagado;
	private EmpleadoCargo bodeguero;
	private Proveedor proveedor;
	private BigDecimal total;
	private List<CuotaIngreso> cuotaIngresos;
	private List<DetalleIngreso> detalleIngresos;

	public Ingreso() {
	}

	public Ingreso(Long id, Boolean activo, String codigoDocumento,
			Date fechaFactura, Timestamp fechaIngreso, Timestamp fechaCierre,
			Boolean pagado, EmpleadoCargo bodeguero, Proveedor proveedor,
			BigDecimal total, List<CuotaIngreso> cuotaIngresos,
			List<DetalleIngreso> detalleIngresos) {
		this.id = id;
		this.activo = activo;
		this.codigoDocumento = codigoDocumento;
		this.fechaFactura = fechaFactura;
		this.fechaIngreso = fechaIngreso;
		this.fechaCierre = fechaCierre;
		this.pagado = pagado;
		this.bodeguero = bodeguero;
		this.proveedor = proveedor;
		this.total = total;
		this.cuotaIngresos = cuotaIngresos;
		this.detalleIngresos = detalleIngresos;
	}

	public CuotaIngreso addCuotaingreso(CuotaIngreso cuotaingreso) {
		getCuotaIngresos().add(cuotaingreso);
		cuotaingreso.setIngreso(this);

		return cuotaingreso;
	}

	public DetalleIngreso addDetalleingreso(DetalleIngreso detalleingreso) {
		getDetalleIngresos().add(detalleingreso);
		detalleingreso.setIngreso(this);

		return detalleingreso;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingreso other = (Ingreso) obj;
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

	// bi-directional many-to-one association to empleadoCargo
	@ManyToOne
	@JoinColumn(name = "bodegueroid")
	public EmpleadoCargo getBodeguero() {
		return bodeguero;
	}

	@Column(name = "codigodocumento", nullable = false, length = 25)
	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	// bi-directional many-to-one association to Cuotaingreso
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "ingreso")
	public List<CuotaIngreso> getCuotaIngresos() {
		return cuotaIngresos;
	}

	// bi-directional many-to-one association to Detalleingreso
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "ingreso")
	// @IndexColumn(name = "orden", base = 1)
	public List<DetalleIngreso> getDetalleIngresos() {
		return detalleIngresos;
	}

	@Column(name = "fechacierre")
	public Timestamp getFechaCierre() {
		return fechaCierre;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechafactura", nullable = false)
	public Date getFechaFactura() {
		return fechaFactura;
	}

	@Column(name = "fechaingreso", nullable = false)
	public Timestamp getFechaIngreso() {
		return fechaIngreso;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "INGRESO_INGRESOID_GENERATOR", sequenceName = "INGRESO_INGRESOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INGRESO_INGRESOID_GENERATOR")
	@Column(name = "ingresoid", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@Column(nullable = false)
	public Boolean getPagado() {
		return this.pagado;
	}

	// bi-directional many-to-one association to proveedor
	@ManyToOne
	@JoinColumn(name = "proveedorid", nullable = false)
	public Proveedor getProveedor() {
		return this.proveedor;
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public BigDecimal getTotal() {
		return this.total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public CuotaIngreso removeCuotaIngreso(CuotaIngreso cuotaIngreso) {
		getCuotaIngresos().remove(cuotaIngreso);
		cuotaIngreso.setIngreso(null);

		return cuotaIngreso;
	}

	public DetalleIngreso removeDetalleIngreso(DetalleIngreso detalleIngreso) {
		getDetalleIngresos().remove(detalleIngreso);
		detalleIngreso.setIngreso(null);

		return detalleIngreso;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setBodeguero(EmpleadoCargo bodeguero) {
		this.bodeguero = bodeguero;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public void setCuotaIngresos(List<CuotaIngreso> cuotaIngresos) {
		this.cuotaIngresos = cuotaIngresos;
	}

	public void setDetalleIngresos(List<DetalleIngreso> detalleIngresos) {
		this.detalleIngresos = detalleIngresos;
	}

	public void setFechaCierre(Timestamp fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public void setFechaIngreso(Timestamp fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPagado(Boolean pagado) {
		this.pagado = pagado;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}