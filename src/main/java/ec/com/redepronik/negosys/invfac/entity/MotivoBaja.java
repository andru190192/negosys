package ec.com.redepronik.negosys.invfac.entity;

public enum MotivoBaja {
	CA(1, "CADUCIDAD"), DT(2, "DESTRUCCIÓN"), CI(3, "CONSUMO INTERNO"), DV(4,
			"DEVOLUCIÓN");

	private final Integer id;
	private final String nombre;

	private MotivoBaja(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
}
