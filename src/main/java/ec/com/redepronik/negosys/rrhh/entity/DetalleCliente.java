package ec.com.redepronik.negosys.rrhh.entity;

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
@Table(name = "detallecliente")
public class DetalleCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(allocationSize = 1, name = "DETALLECLIENTE_DETALLECLIENTEID_GENERATOR", sequenceName = "DETALLECLIENTE_DETALLECLIENTEID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLECLIENTE_DETALLECLIENTEID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Integer detalleclienteid;

	@Column(nullable = false, length = 20)
	private String campo;

	@Column(nullable = false, length = 50)
	private String valor;

	// bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name = "clienteid", nullable = false)
	private Cliente cliente;

	public DetalleCliente() {
	}

	public String getCampo() {
		return campo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Integer getDetalleclienteid() {
		return detalleclienteid;
	}

	public String getValor() {
		return valor;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setDetalleclienteid(Integer detalleclienteid) {
		this.detalleclienteid = detalleclienteid;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
