package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

public class RefrigeradorCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	private String campo;
	private String cliente;
	private String valor;

	public RefrigeradorCliente(String campo, String cliente, String valor) {
		this.campo = campo;
		this.cliente = cliente;
		this.valor = valor;
	}

	public String getCampo() {
		return campo;
	}

	public String getCliente() {
		return cliente;
	}

	public String getValor() {
		return valor;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}