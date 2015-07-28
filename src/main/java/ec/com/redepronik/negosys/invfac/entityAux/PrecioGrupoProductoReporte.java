package ec.com.redepronik.negosys.invfac.entityAux;

import java.util.List;

public class PrecioGrupoProductoReporte {

	private String nombreGrupo;
	private List<PrecioProductoReporte> listaPrecioProductoReporte;

	public PrecioGrupoProductoReporte() {
	}

	public PrecioGrupoProductoReporte(String nombreGrupo,
			List<PrecioProductoReporte> listaPrecioProductoReporte) {
		this.nombreGrupo = nombreGrupo;
		this.listaPrecioProductoReporte = listaPrecioProductoReporte;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public List<PrecioProductoReporte> getPrecioProductoReporte() {
		return listaPrecioProductoReporte;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public void setPrecioProductoReporte(
			List<PrecioProductoReporte> listaPrecioProductoReporte) {
		this.listaPrecioProductoReporte = listaPrecioProductoReporte;
	}

}
