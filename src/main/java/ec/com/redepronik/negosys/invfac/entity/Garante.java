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

import ec.com.redepronik.negosys.rrhh.entity.Cliente;

@Entity
@Table(name = "garante")
public class Garante implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Cliente cliente;
	private Integer orden;
	private Credito credito;

	public Garante() {
	}

	public Garante(Integer id, Cliente cliente, Integer orden, Credito credito) {
		this.id = id;
		this.cliente = cliente;
		this.orden = orden;
		this.credito = credito;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Garante other = (Garante) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// bi-directional many-to-one association to empleadoCargo
	@ManyToOne
	@JoinColumn(name = "clienteid", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}

	// bi-directional many-to-one association to Credito
	@ManyToOne
	@JoinColumn(name = "creditoid", nullable = false)
	public Credito getCredito() {
		return this.credito;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "GARANTE_GARANTEID_GENERATOR", sequenceName = "GARANTE_GARANTEID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GARANTE_GARANTEID_GENERATOR")
	@Column(name = "garanteid", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@Column(nullable = false)
	public Integer getOrden() {
		return orden;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

}