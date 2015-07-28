package ec.com.redepronik.negosys.invfac.entity;

public enum ImpuestoRetencion {

	RE(1, "RENTA"), IV(2, "IVA"), SD(3, "SALIDA DIVISAS");

	private final Integer id;
	private final String nombre;

	private ImpuestoRetencion(Integer id, String nombre) {
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