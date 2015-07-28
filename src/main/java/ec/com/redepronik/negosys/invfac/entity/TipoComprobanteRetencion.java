package ec.com.redepronik.negosys.invfac.entity;

public enum TipoComprobanteRetencion {

	FA("01", "FACTURA"), ND("05", "NOTA DÉBITO");

	private final String id;
	private final String nombre;

	private TipoComprobanteRetencion(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
}