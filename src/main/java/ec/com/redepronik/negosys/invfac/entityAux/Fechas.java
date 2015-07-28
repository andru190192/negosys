package ec.com.redepronik.negosys.invfac.entityAux;

import java.util.Date;

public class Fechas {

	private Date fechaInicio;
	private Date fechaFin;

	public Fechas() {
	}

	public Fechas(Date fechaInicio, Date fechaFin) {
		super();
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
}