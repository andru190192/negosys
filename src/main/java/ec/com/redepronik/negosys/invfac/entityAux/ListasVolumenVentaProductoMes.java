package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.util.List;

//@Entity
public class ListasVolumenVentaProductoMes implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Id
	private Long id;
	private List<VolumenVentaProductoMes> listaLiquido;
	private List<VolumenVentaProductoMes> listaEnvase;
	private List<VolumenVentaProductoMes> listaOtros;

	public ListasVolumenVentaProductoMes() {
	}

	public ListasVolumenVentaProductoMes(Long id,
			List<VolumenVentaProductoMes> listaLiquido,
			List<VolumenVentaProductoMes> listaEnvase,
			List<VolumenVentaProductoMes> listaOtros) {
		this.id = id;
		this.listaLiquido = listaLiquido;
		this.listaEnvase = listaEnvase;
		this.listaOtros = listaOtros;
	}

	public Long getId() {
		return id;
	}

	public List<VolumenVentaProductoMes> getListaEnvase() {
		return listaEnvase;
	}

	public List<VolumenVentaProductoMes> getListaLiquido() {
		return listaLiquido;
	}

	public List<VolumenVentaProductoMes> getListaOtros() {
		return listaOtros;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setListaEnvase(List<VolumenVentaProductoMes> listaEnvase) {
		this.listaEnvase = listaEnvase;
	}

	public void setListaLiquido(List<VolumenVentaProductoMes> listaLiquido) {
		this.listaLiquido = listaLiquido;
	}

	public void setListaOtros(List<VolumenVentaProductoMes> listaOtros) {
		this.listaOtros = listaOtros;
	}

}