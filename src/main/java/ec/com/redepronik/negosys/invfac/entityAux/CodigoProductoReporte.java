package ec.com.redepronik.negosys.invfac.entityAux;

public class CodigoProductoReporte {

	private String codigo;
	private String pvp;
	private String nombre;
	private String nombreComercial;
	private String paginaWeb;

	public CodigoProductoReporte(String codigo, String pvp, String nombre,
			String nombreComercial, String paginaWeb) {
		this.codigo = codigo;
		this.pvp = pvp;
		this.nombre = nombre;
		this.nombreComercial = nombreComercial;
		this.paginaWeb = paginaWeb;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public String getPvp() {
		return pvp;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public void setPvp(String pvp) {
		this.pvp = pvp;
	}

}
