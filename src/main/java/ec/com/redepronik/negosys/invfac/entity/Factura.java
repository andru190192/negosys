package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Boolean activo;
	private String autorizacionAnulacion;
	private Cliente clienteFactura;
	private Cliente cliente;
	private BigDecimal descuento;
	private BigDecimal descuentoCuadre;
	private EmpleadoCargo vendedor;
	private EmpleadoCargo cajero;
	private NotaCredito notaCredito;
	private Timestamp fechaCierre;
	private Timestamp fechaInicio;
	private Credito credito;
	private List<DetalleFactura> detalleFactura;
	private List<Entrada> entradas;
	private GuiaRemision guiaRemision;
	private String establecimiento;
	private String puntoEmision;
	private String secuencia;

	public Factura() {
	}

	public Factura(Integer id, Boolean activo, String autorizacionAnulacion,
			Cliente clienteFactura, Cliente cliente, BigDecimal descuento,
			BigDecimal descuentoCuadre, EmpleadoCargo vendedor,
			EmpleadoCargo cajero, NotaCredito notaCredito,
			Timestamp fechaCierre, Timestamp fechaInicio, Credito credito,
			List<DetalleFactura> detalleFactura, List<Entrada> entradas,
			GuiaRemision guiaRemision, String establecimiento,
			String puntoEmision, String secuencia) {
		this.id = id;
		this.activo = activo;
		this.autorizacionAnulacion = autorizacionAnulacion;
		this.clienteFactura = clienteFactura;
		this.cliente = cliente;
		this.descuento = descuento;
		this.descuentoCuadre = descuentoCuadre;
		this.vendedor = vendedor;
		this.cajero = cajero;
		this.notaCredito = notaCredito;
		this.fechaCierre = fechaCierre;
		this.fechaInicio = fechaInicio;
		this.credito = credito;
		this.detalleFactura = detalleFactura;
		this.entradas = entradas;
		this.guiaRemision = guiaRemision;
		this.establecimiento = establecimiento;
		this.puntoEmision = puntoEmision;
		this.secuencia = secuencia;
	}

	public DetalleFactura addDetalleFactura(DetalleFactura detalleFactura) {
		getDetalleFactura().add(detalleFactura);
		detalleFactura.setFactura(this);

		return detalleFactura;
	}

	public Entrada addEntrada(Entrada entrada) {
		getEntradas().add(entrada);
		entrada.setFactura(this);

		return entrada;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Factura other = (Factura) obj;
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

	@Column(name = "`autorizacionAnulacion`", length = 10)
	public String getAutorizacionAnulacion() {
		return autorizacionAnulacion;
	}

	@ManyToOne
	@JoinColumn(name = "cajero")
	public EmpleadoCargo getCajero() {
		return cajero;
	}

	@ManyToOne
	@JoinColumn(name = "cliente")
	public Cliente getCliente() {
		return this.cliente;
	}

	@ManyToOne
	@JoinColumn(name = "`clienteFactura`")
	public Cliente getClienteFactura() {
		return this.clienteFactura;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "factura")
	public Credito getCredito() {
		return this.credito;
	}

	@Column(nullable = false, precision = 8, scale = 2)
	public BigDecimal getDescuento() {
		return descuento;
	}

	@Column(name = "`descuentoCuadre`", nullable = false, precision = 8, scale = 2)
	public BigDecimal getDescuentoCuadre() {
		return descuentoCuadre;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "factura")
	public List<DetalleFactura> getDetalleFactura() {
		return detalleFactura;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "factura")
	@IndexColumn(name = "orden", base = 1)
	public List<Entrada> getEntradas() {
		return this.entradas;
	}

	@Column(length = 3, nullable = false)
	public String getEstablecimiento() {
		return establecimiento;
	}

	@Column(name = "`fechaCierre`")
	public Timestamp getFechaCierre() {
		return fechaCierre;
	}

	@Column(name = "`fechaInicio`", nullable = false)
	public Timestamp getFechaInicio() {
		return fechaInicio;
	}

	@ManyToOne
	@JoinColumn(name = "`guiaRemision`", nullable = false)
	public GuiaRemision getGuiaRemision() {
		return guiaRemision;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "FACTURAS_ID_GENERATOR", sequenceName = "FACTURAS_ID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FACTURAS_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "factura")
	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	@Column(name = "`puntoEmision`", length = 3, nullable = false)
	public String getPuntoEmision() {
		return puntoEmision;
	}

	@Column(length = 9, nullable = false)
	public String getSecuencia() {
		return secuencia;
	}

	@ManyToOne
	@JoinColumn(name = "vendedor")
	public EmpleadoCargo getVendedor() {
		return vendedor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DetalleFactura removeDetalleFactura(DetalleFactura detalleFactura) {
		getDetalleFactura().remove(detalleFactura);
		detalleFactura.setFactura(null);

		return detalleFactura;
	}

	public Entrada removeEntrada(Entrada entrada) {
		getEntradas().remove(entrada);
		entrada.setFactura(null);

		return entrada;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setAutorizacionAnulacion(String autorizacionAnulacion) {
		this.autorizacionAnulacion = autorizacionAnulacion;
	}

	public void setCajero(EmpleadoCargo cajero) {
		this.cajero = cajero;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setClienteFactura(Cliente clienteFactura) {
		this.clienteFactura = clienteFactura;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public void setDescuentoCuadre(BigDecimal descuentoCuadre) {
		this.descuentoCuadre = descuentoCuadre;
	}

	public void setDetalleFactura(List<DetalleFactura> detalleFactura) {
		this.detalleFactura = detalleFactura;
	}

	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setFechaCierre(Timestamp fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setGuiaRemision(GuiaRemision guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	public void setVendedor(EmpleadoCargo vendedor) {
		this.vendedor = vendedor;
	}

}