package ec.com.redepronik.negosys.invfac.entityAux;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cantidad {

	@Id
	private Long cantidad;

	public Cantidad() {

	}

	public Cantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
}
