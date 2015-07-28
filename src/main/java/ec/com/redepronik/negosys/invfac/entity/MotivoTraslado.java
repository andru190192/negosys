package ec.com.redepronik.negosys.invfac.entity;

public enum MotivoTraslado {

	CP(1, "COMPRA"), VT(2, "VENTA"), CG(3, "CONSIGNACION");

	private final Integer id;
	private final String nombre;

	private MotivoTraslado(Integer id, String nombre) {
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
