package ec.com.redepronik.negosys.rrhh.entity;

import java.io.Serializable;
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

import ec.com.redepronik.negosys.invfac.entity.CuotaIngreso;
import ec.com.redepronik.negosys.invfac.entity.Entrada;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Gastos;
import ec.com.redepronik.negosys.invfac.entity.GuiaRemision;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entity.LocalBodeguero;
import ec.com.redepronik.negosys.invfac.entity.LocalCajero;

@Entity
@Table(name = "empleadocargo")
public class EmpleadoCargo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean actual;
	private Date fechaCierre;
	private Date fechaInicio;
	private Cargo cargo;
	private Empleado empleado;
	private List<LocalBodeguero> localBodegueros;
	private List<LocalCajero> localCajeros;
	private List<GuiaRemision> guiaRemisiones;
	private List<Factura> facturas;
	private List<Entrada> entradas;
	private List<CuotaIngreso> cuotaIngresos;
	private List<Ingreso> ingresos;
	private List<Gastos> gastos;

	public EmpleadoCargo() {
	}

	public EmpleadoCargo(Integer id, Boolean actual, Date fechaCierre,
			Date fechaInicio, Cargo cargo, Empleado empleado,
			List<LocalBodeguero> localBodegueros,
			List<LocalCajero> localCajeros, List<GuiaRemision> guiaRemisiones,
			List<Factura> facturas, List<Entrada> entradas,
			List<CuotaIngreso> cuotaIngresos, List<Ingreso> ingresos,
			List<Gastos> gastos) {
		this.id = id;
		this.actual = actual;
		this.fechaCierre = fechaCierre;
		this.fechaInicio = fechaInicio;
		this.cargo = cargo;
		this.empleado = empleado;
		this.localBodegueros = localBodegueros;
		this.localCajeros = localCajeros;
		this.guiaRemisiones = guiaRemisiones;
		this.facturas = facturas;
		this.entradas = entradas;
		this.cuotaIngresos = cuotaIngresos;
		this.ingresos = ingresos;
		this.gastos = gastos;
	}

	public CuotaIngreso addCuotaIngreso(CuotaIngreso cuotaIngreso) {
		getCuotaIngresos().add(cuotaIngreso);
		cuotaIngreso.setCajero(this);

		return cuotaIngreso;
	}

	public Entrada addEntrada(Entrada entrada) {
		getEntradas().add(entrada);
		entrada.setCajero(this);

		return entrada;
	}

	public Factura addFactura(Factura egreso) {
		getFacturas().add(egreso);
		egreso.setCajero(this);

		return egreso;
	}

	public Gastos addGastos(Gastos gastos) {
		getGastos().add(gastos);
		gastos.setCajero(this);

		return gastos;
	}

	public GuiaRemision addGuiaremision(GuiaRemision guiaRemision) {
		getGuiaRemisiones().add(guiaRemision);
		guiaRemision.setTransportista(this);

		return guiaRemision;
	}

	public Ingreso addIngreso(Ingreso ingreso) {
		getIngresos().add(ingreso);
		ingreso.setBodeguero(this);

		return ingreso;
	}

	public LocalBodeguero addLocalBodeguero(LocalBodeguero localBodeguero) {
		getLocalBodegueros().add(localBodeguero);
		localBodeguero.setEmpleadoCargo(this);

		return localBodeguero;
	}

	public LocalCajero addLocalCajero(LocalCajero localCajero) {
		getLocalCajeros().add(localCajero);
		localCajero.setEmpleadoCargo(this);

		return localCajero;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpleadoCargo other = (EmpleadoCargo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActual() {
		return this.actual;
	}

	@ManyToOne
	@JoinColumn(name = "cargoid", nullable = false)
	public Cargo getCargo() {
		return this.cargo;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cajero")
	public List<CuotaIngreso> getCuotaIngresos() {
		return cuotaIngresos;
	}

	@ManyToOne
	@JoinColumn(name = "empleadoid", nullable = false)
	public Empleado getEmpleado() {
		return this.empleado;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cajero")
	public List<Entrada> getEntradas() {
		return entradas;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cajero")
	public List<Factura> getFacturas() {
		return facturas;
	}

	@Column(name = "fechacierre")
	@Temporal(TemporalType.DATE)
	public Date getFechaCierre() {
		return fechaCierre;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechainicio", nullable = false)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cajero")
	public List<Gastos> getGastos() {
		return gastos;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "transportista")
	public List<GuiaRemision> getGuiaRemisiones() {
		return guiaRemisiones;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "EMPLEADOCARGO_EMPLEADOCARGOID_GENERATOR", sequenceName = "EMPLEADOCARGO_EMPLEADOCARGOID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLEADOCARGO_EMPLEADOCARGOID_GENERATOR")
	@Column(name = "empleadocargoid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "bodeguero")
	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "empleadoCargo")
	public List<LocalBodeguero> getLocalBodegueros() {
		return localBodegueros;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "empleadoCargo")
	public List<LocalCajero> getLocalCajeros() {
		return localCajeros;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public LocalBodeguero removeBodegaempleadocargo(
			LocalBodeguero bodegaempleadocargo) {
		getLocalCajeros().remove(bodegaempleadocargo);
		bodegaempleadocargo.setEmpleadoCargo(null);

		return bodegaempleadocargo;
	}

	public CuotaIngreso removeCuotaingreso(CuotaIngreso cuotaingreso) {
		getIngresos().remove(cuotaingreso);
		cuotaingreso.setCajero(null);

		return cuotaingreso;
	}

	public Entrada removeEntrada(Entrada entrada) {
		getFacturas().remove(entrada);
		entrada.setCajero(null);

		return entrada;
	}

	public Factura removeFactura(Factura egreso) {
		getFacturas().remove(egreso);
		egreso.setCajero(null);

		return egreso;
	}

	public Gastos removeGastos(Gastos gastos) {
		getGastos().remove(gastos);
		gastos.setCajero(null);

		return gastos;
	}

	public GuiaRemision removeGuiaRemision(GuiaRemision guiaRemision) {
		getGuiaRemisiones().remove(guiaRemision);
		guiaRemision.setTransportista(null);

		return guiaRemision;
	}

	public Ingreso removeIngreso(Ingreso ingreso) {
		getIngresos().remove(ingreso);
		ingreso.setBodeguero(null);

		return ingreso;
	}

	public LocalCajero removeLocalCajero(LocalCajero localCajero) {
		getLocalCajeros().remove(localCajero);
		localCajero.setEmpleadoCargo(null);

		return localCajero;
	}

	public void setActual(Boolean actual) {
		this.actual = actual;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setCuotaingresos(List<CuotaIngreso> cuotaIngresos) {
		this.cuotaIngresos = cuotaIngresos;
	}

	public void setCuotaIngresos(List<CuotaIngreso> cuotaIngresos) {
		this.cuotaIngresos = cuotaIngresos;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setGastos(List<Gastos> gastos) {
		this.gastos = gastos;
	}

	public void setGuiaremisiones(List<GuiaRemision> guiaRemisiones) {
		this.guiaRemisiones = guiaRemisiones;
	}

	public void setGuiaRemisiones(List<GuiaRemision> guiaRemisiones) {
		this.guiaRemisiones = guiaRemisiones;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
	}

	public void setLocalBodegueros(List<LocalBodeguero> localBodegueros) {
		this.localBodegueros = localBodegueros;
	}

	public void setLocalCajeros(List<LocalCajero> localCajeros) {
		this.localCajeros = localCajeros;
	}

}
