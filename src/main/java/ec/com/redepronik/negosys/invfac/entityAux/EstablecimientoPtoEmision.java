package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.LocalCajero;

@Entity
public class EstablecimientoPtoEmision implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private Local establecimiento;
	private LocalCajero ptoEmision;

	public EstablecimientoPtoEmision() {

	}

	public EstablecimientoPtoEmision(Integer id, Local establecimiento,
			LocalCajero ptoEmision) {
		this.id = id;
		this.establecimiento = establecimiento;
		this.ptoEmision = ptoEmision;
	}

	public Local getEstablecimiento() {
		return establecimiento;
	}

	public Integer getId() {
		return id;
	}

	public LocalCajero getPtoEmision() {
		return ptoEmision;
	}

	public void setEstablecimiento(Local establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPtoEmision(LocalCajero ptoEmision) {
		this.ptoEmision = ptoEmision;
	}

}