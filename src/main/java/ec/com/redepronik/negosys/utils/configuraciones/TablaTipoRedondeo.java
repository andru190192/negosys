package ec.com.redepronik.negosys.utils.configuraciones;

public class TablaTipoRedondeo {

	private String entrada;
	private String UP;
	private String DOWN;
	private String CEILING;
	private String FLOOR;
	private String HALF_UP;
	private String HALF_DOWN;
	private String HALF_EVEN;
	private String UNNECESSARY;

	public TablaTipoRedondeo(String entrada, String uP, String dOWN,
			String cEILING, String fLOOR, String hALF_UP, String hALF_DOWN,
			String hALF_EVEN, String uNNECESSARY) {
		this.entrada = entrada;
		UP = uP;
		DOWN = dOWN;
		CEILING = cEILING;
		FLOOR = fLOOR;
		HALF_UP = hALF_UP;
		HALF_DOWN = hALF_DOWN;
		HALF_EVEN = hALF_EVEN;
		UNNECESSARY = uNNECESSARY;
	}

	public String getCEILING() {
		return CEILING;
	}

	public String getDOWN() {
		return DOWN;
	}

	public String getEntrada() {
		return entrada;
	}

	public String getFLOOR() {
		return FLOOR;
	}

	public String getHALF_DOWN() {
		return HALF_DOWN;
	}

	public String getHALF_EVEN() {
		return HALF_EVEN;
	}

	public String getHALF_UP() {
		return HALF_UP;
	}

	public String getUNNECESSARY() {
		return UNNECESSARY;
	}

	public String getUP() {
		return UP;
	}

	public void setCEILING(String cEILING) {
		CEILING = cEILING;
	}

	public void setDOWN(String dOWN) {
		DOWN = dOWN;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public void setFLOOR(String fLOOR) {
		FLOOR = fLOOR;
	}

	public void setHALF_DOWN(String hALF_DOWN) {
		HALF_DOWN = hALF_DOWN;
	}

	public void setHALF_EVEN(String hALF_EVEN) {
		HALF_EVEN = hALF_EVEN;
	}

	public void setHALF_UP(String hALF_UP) {
		HALF_UP = hALF_UP;
	}

	public void setUNNECESSARY(String uNNECESSARY) {
		UNNECESSARY = uNNECESSARY;
	}

	public void setUP(String uP) {
		UP = uP;
	}

}
