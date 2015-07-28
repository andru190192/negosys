package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;

@Entity
@Table(name = "guiaremision")
public class GuiaRemision implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String aduana;
	private Cliente cliente;
	private EmpleadoCargo transportista;
	private Date fechaInicio;
	private Date fechaTerminacion;
	private String placa;
	private String puntoLlegada;
	private String puntoPartida;
	private String razonSocialDestinatario;
	private List<DetalleGuiaRemision> detalleGuiaRemisions;
	private List<Factura> facturas;
	private MotivoTraslado motivoTraslado;
	private String establecimiento;
	private String puntoEmision;
	private String secuencia;

	public GuiaRemision() {
	}

	public GuiaRemision(Integer id, String aduana, Cliente cliente,
			EmpleadoCargo transportista, Date fechaInicio,
			Date fechaTerminacion, String placa, String puntoLlegada,
			String puntoPartida, String razonSocialDestinatario,
			List<DetalleGuiaRemision> detalleGuiaRemisions,
			List<Factura> facturas, MotivoTraslado motivoTraslado,
			String establecimiento, String puntoEmision, String secuencia) {
		this.id = id;
		this.aduana = aduana;
		this.cliente = cliente;
		this.transportista = transportista;
		this.fechaInicio = fechaInicio;
		this.fechaTerminacion = fechaTerminacion;
		this.placa = placa;
		this.puntoLlegada = puntoLlegada;
		this.puntoPartida = puntoPartida;
		this.razonSocialDestinatario = razonSocialDestinatario;
		this.detalleGuiaRemisions = detalleGuiaRemisions;
		this.facturas = facturas;
		this.motivoTraslado = motivoTraslado;
		this.establecimiento = establecimiento;
		this.puntoEmision = puntoEmision;
		this.secuencia = secuencia;
	}

	public DetalleGuiaRemision addDetalleGuiaRemision(
			DetalleGuiaRemision detalleGuiaRemision) {
		getDetalleGuiaRemisions().add(detalleGuiaRemision);
		detalleGuiaRemision.setGuiaRemision(this);

		return detalleGuiaRemision;
	}

	public Factura addFactura(Factura factura) {
		getFacturas().add(factura);
		factura.setGuiaRemision(this);

		return factura;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuiaRemision other = (GuiaRemision) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(length = 25)
	public String getAduana() {
		return this.aduana;
	}

	@ManyToOne
	@JoinColumn(name = "clienteid")
	public Cliente getCliente() {
		return cliente;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "guiaRemision")
	public List<DetalleGuiaRemision> getDetalleGuiaRemisions() {
		return detalleGuiaRemisions;
	}

	@Column(nullable = false, length = 3)
	public String getEstablecimiento() {
		return establecimiento;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "guiaRemision")
	public List<Factura> getFacturas() {
		return facturas;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechainicio", nullable = false)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaterminacion", nullable = false)
	public Date getFechaTerminacion() {
		return fechaTerminacion;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "GUIAREMISION_GUIAREMISIONID_GENERATOR", sequenceName = "GUIAREMISION_GUIAREMISIONID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUIAREMISION_GUIAREMISIONID_GENERATOR")
	@Column(name = "guiaremisionid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "motivotrasladoid", nullable = false)
	public MotivoTraslado getMotivoTraslado() {
		return motivoTraslado;
	}

	@Column(length = 10)
	public String getPlaca() {
		return this.placa;
	}

	@Column(name = "puntoemision", nullable = false, length = 3)
	public String getPuntoEmision() {
		return puntoEmision;
	}

	@Column(name = "puntollegada", nullable = false, length = 20)
	public String getPuntoLlegada() {
		return puntoLlegada;
	}

	@Column(name = "puntopartida", nullable = false, length = 20)
	public String getPuntoPartida() {
		return puntoPartida;
	}

	@Column(name = "razonsocialdestinatario", nullable = false, length = 50)
	public String getRazonSocialDestinatario() {
		return razonSocialDestinatario;
	}

	@Column(nullable = false, length = 9)
	public String getSecuencia() {
		return secuencia;
	}

	@ManyToOne
	@JoinColumn(name = "transportistaid", nullable = false)
	public EmpleadoCargo getTransportista() {
		return transportista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Factura removeFactura(Factura factura) {
		getFacturas().remove(factura);
		factura.setGuiaRemision(null);

		return factura;
	}

	public void setAduana(String aduana) {
		this.aduana = aduana;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setDetalleGuiaRemisions(
			List<DetalleGuiaRemision> detalleGuiaRemisions) {
		this.detalleGuiaRemisions = detalleGuiaRemisions;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaTerminacion(Date fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMotivoTraslado(MotivoTraslado motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public void setPuntoLlegada(String puntoLlegada) {
		this.puntoLlegada = puntoLlegada;
	}

	public void setPuntoPartida(String puntoPartida) {
		this.puntoPartida = puntoPartida;
	}

	public void setRazonSocialDestinatario(String razonSocialDestinatario) {
		this.razonSocialDestinatario = razonSocialDestinatario;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	public void setTransportista(EmpleadoCargo transportista) {
		this.transportista = transportista;
	}

}
