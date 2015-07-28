package ec.com.redepronik.negosys.utils.enums;

public enum Meses {
	ENERO(1, "ENERO"), FEBRERO(2, "FEBRERO"), MARZO(3, "MARZO"), ABRIL(4,
			"ABRIL"), MAYO(5, "MAYO"), JUNIO(6, "JUNIO"), JULIO(7, "JULIO"), AGOSTO(
			8, "AGOSTO"), SEPTIEMBRE(9, "SEPTIEMBRE"), OCTUBRE(10, "OCTUBRE"), NOVIEMBRE(
			11, "NOVIEMBRE"), DICIEMBRE(12, "DICIEMBRE");

	private final Integer id;
	private final String nombre;

	private Meses(Integer id, String nombre) {
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
