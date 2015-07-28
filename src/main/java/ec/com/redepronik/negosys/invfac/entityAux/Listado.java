package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

public class Listado implements Serializable {

	private static final long serialVersionUID = 1L;
	private Factura egreso;
	private Persona persona;

	public Factura getEgreso() {
		return egreso;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setEgreso(Factura egreso) {
		this.egreso = egreso;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}
