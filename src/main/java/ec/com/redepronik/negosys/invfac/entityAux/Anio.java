package ec.com.redepronik.negosys.invfac.entityAux;

public enum Anio {

	a2015("2015"), a2016("2016");

	private final String id;

	private Anio(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}