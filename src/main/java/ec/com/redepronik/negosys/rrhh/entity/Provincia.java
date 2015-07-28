package ec.com.redepronik.negosys.rrhh.entity;

public enum Provincia {

	AZ(1, "AZUAY"), BO(2, "BOLIVAR"), CA(3, "CAÑAR"), CR(4, "CARCHI"), CH(5,
			"CHIMBORAZO"), CO(6, "COTOPAXI"), EL(7, "EL ORO"), ES(8,
			"ESMERALDAS"), GA(9, "GALAPAGOS"), GU(10, "GUAYAS"), IM(11,
			"IMBABURA"), LO(12, "LOJA"), LS(13, "LOS RIOS"), MA(14, "MANABÍ"), MO(
			15, "MORONA SANTIAGO"), NA(16, "NAPO"), OR(17, "ORELLANA"), PA(18,
			"PASTAZA"), PI(19, "PICHINCHA"), SA(20, "SANTA ELENA"), SN(21,
			"SANTO DOMINGO DE LOS TSACHILAS"), SU(22, "SUCUMBIOS"), TU(23,
			"TUNGURAHUA"), ZA(24, "ZAMORA CHINCHIPE");

	private final Integer id;
	private final String nombre;

	private Provincia(Integer id, String nombre) {
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