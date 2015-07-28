package ec.com.redepronik.negosys.invfac.entity;

public enum Impuesto {

	IV(2, "IVA"), IC(3, "ICE"), IR(5, "IRBPNR");

	private final Integer id;
	private final String nombre;

	private Impuesto(Integer id, String nombre) {
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