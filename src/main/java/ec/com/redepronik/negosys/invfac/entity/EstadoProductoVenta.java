package ec.com.redepronik.negosys.invfac.entity;

public enum EstadoProductoVenta {

	NR(1, "NORMAL"), PR(2, "PROMOCIÓN"), CB(3, "CAMBIO"), PI(4,
			"PROMOCIÓN INTERNA");

	private final Integer id;
	private final String nombre;

	private EstadoProductoVenta(Integer id, String nombre) {
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