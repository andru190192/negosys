package ec.com.redepronik.negosys.invfac.entityAux;

import java.util.List;

import ec.com.redepronik.negosys.rrhh.entity.Persona;

public class CobranzaReporte {

	private Persona cliente;
	private Persona garante;
	private String nombreProducto;

	private List<CobranzaReporteAuxiliar> list;

	public CobranzaReporte() {
	}

	public Persona getCliente() {
		return cliente;
	}

	public Persona getGarante() {
		return garante;
	}

	public List<CobranzaReporteAuxiliar> getList() {
		return list;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setCliente(Persona cliente) {
		this.cliente = cliente;
	}

	public void setGarante(Persona garante) {
		this.garante = garante;
	}

	public void setList(List<CobranzaReporteAuxiliar> list) {
		this.list = list;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

}
