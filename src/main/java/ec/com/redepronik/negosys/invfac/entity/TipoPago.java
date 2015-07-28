package ec.com.redepronik.negosys.invfac.entity;

public enum TipoPago {

	EF(1, "EFECTIVO"), TC(2, "TARJETA DE CRÉDITO"), CH(3, "CHEQUE"), TB(4,
			"TRANSFERENCIA BANCARIA");

	private final Integer id;
	private final String nombre;

	private TipoPago(Integer id, String nombre) {
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