package ec.com.redepronik.negosys.rrhh.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import ec.com.redepronik.negosys.invfac.entity.Cotizacion;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Garante;
import ec.com.redepronik.negosys.invfac.entity.GuiaRemision;
import ec.com.redepronik.negosys.invfac.entity.Pedido;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Boolean activo;
	private String folio;
	private String nombreComercial;
	private Persona persona;
	private List<Factura> facturas;
	private List<Cotizacion> cotizaciones;
	private List<Pedido> pedidos;
	private List<GuiaRemision> guiaRemisiones;
	private List<Garante> garantes;
	private List<DetalleCliente> detalleClientes;

	public Cliente() {
	}

	public Cliente(Integer id, Boolean activo, String folio,
			String nombreComercial, Persona persona, List<Factura> facturas,
			List<Cotizacion> cotizaciones, List<Pedido> pedidos,
			List<GuiaRemision> guiaRemisiones, List<Garante> garantes,
			List<DetalleCliente> detalleClientes) {
		this.id = id;
		this.activo = activo;
		this.folio = folio;
		this.nombreComercial = nombreComercial;
		this.persona = persona;
		this.facturas = facturas;
		this.cotizaciones = cotizaciones;
		this.pedidos = pedidos;
		this.guiaRemisiones = guiaRemisiones;
		this.garantes = garantes;
		this.detalleClientes = detalleClientes;
	}

	public Cotizacion addCotizacion(Cotizacion cotizacion) {
		getCotizaciones().add(cotizacion);
		cotizacion.setCliente(this);

		return cotizacion;
	}

	public DetalleCliente addDetalleClientes(DetalleCliente detalleClientes) {
		getDetalleClientes().add(detalleClientes);
		detalleClientes.setCliente(this);

		return detalleClientes;
	}

	public Factura addFactura(Factura factura) {
		getFacturas().add(factura);
		factura.setCliente(this);

		return factura;
	}

	public Garante addGarante(Garante garante) {
		getGarantes().add(garante);
		garante.setCliente(this);

		return garante;
	}

	public GuiaRemision addGuiaremision(GuiaRemision guiaRemisiones) {
		getGuiaRemisiones().add(guiaRemisiones);
		guiaRemisiones.setCliente(this);

		return guiaRemisiones;
	}

	public Pedido addPedido(Pedido pedido) {
		getPedidos().add(pedido);
		pedido.setCliente(this);

		return pedido;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
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

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cliente")
	public List<Cotizacion> getCotizaciones() {
		return cotizaciones;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cliente")
	public List<DetalleCliente> getDetalleClientes() {
		return detalleClientes;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cliente")
	public List<Factura> getFacturas() {
		return facturas;
	}

	@Column(length = 5)
	public String getFolio() {
		return this.folio;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cliente")
	public List<Garante> getGarantes() {
		return garantes;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cliente")
	public List<GuiaRemision> getGuiaRemisiones() {
		return guiaRemisiones;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "CLIENTE_CLIENTEID_GENERATOR", sequenceName = "CLIENTE_CLIENTEID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_CLIENTEID_GENERATOR")
	@Column(name = "clienteid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Length(min = 3, max = 50, message = "EL CAMPO NOMBRE COMERCIAL ACEPTA DE 3 A 150 LETRAS")
	@Column(name = "nombrecomercial", nullable = false, length = 50)
	public String getNombreComercial() {
		return nombreComercial;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "cliente")
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	@OneToOne
	@JoinColumn(name = "personaid", nullable = false)
	public Persona getPersona() {
		return this.persona;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DetalleCliente removeDetalleClientes(DetalleCliente detalleClientes) {
		getGarantes().remove(detalleClientes);
		detalleClientes.setCliente(null);

		return detalleClientes;
	}

	public Factura removeFactura(Factura factura) {
		getFacturas().remove(factura);
		factura.setCliente(null);

		return factura;
	}

	public GuiaRemision removeFactura(GuiaRemision guiaRemisiones) {
		getGuiaRemisiones().remove(guiaRemisiones);
		guiaRemisiones.setCliente(null);

		return guiaRemisiones;
	}

	public Garante removeGarante(Garante garante) {
		getGarantes().remove(garante);
		garante.setCliente(null);

		return garante;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setCotizaciones(List<Cotizacion> cotizaciones) {
		this.cotizaciones = cotizaciones;
	}

	public void setDetalleClientes(List<DetalleCliente> detalleClientes) {
		this.detalleClientes = detalleClientes;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public void setGarantes(List<Garante> garantes) {
		this.garantes = garantes;
	}

	public void setGuiaRemisiones(List<GuiaRemision> guiaRemisiones) {
		this.guiaRemisiones = guiaRemisiones;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}