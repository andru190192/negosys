package ec.com.redepronik.negosys.invfac.entity;

public enum EstadoKardex {

	II(1, "INVENTARIO INICIAL"), VE(2, "VENTA"), DV(3, "DEVOLUCIÓN EN VENTA"), CO(
			4, "COMPRA"), DC(5, "DEVOLUCIÓN EN COMPRA"), SA(6, "SALDO"), ST(7,
			"SALIDA POR TRASPASO"), ET(8, "ENTRADA POR TRASPASO"), BI(9,
			"BAJA DE INVENTARIO");

	private final Integer id;
	private final String nombre;

	private EstadoKardex(Integer id, String nombre) {
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
