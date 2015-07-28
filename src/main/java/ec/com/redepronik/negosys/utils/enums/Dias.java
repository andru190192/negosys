package ec.com.redepronik.negosys.utils.enums;

public enum Dias {
	LUNES(1, "LUNES"), MARTES(2, "MARTES"), MIERCOLES(3, "MIERCOLES"), JUEVES(
			4, "JUEVES"), VIERNES(5, "VIERNES"), SABADO(6, "SABADO"), DOMINGO(
			7, "DOMINGO");

	private final Integer id;
	private final String nombre;

	private Dias(Integer id, String nombre) {
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
