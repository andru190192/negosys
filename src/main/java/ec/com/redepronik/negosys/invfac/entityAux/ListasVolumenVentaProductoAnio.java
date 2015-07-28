package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.util.List;

//@Entity
public class ListasVolumenVentaProductoAnio implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Id
	private Long id;
	private List<VolumenVentaProductoAnio> listaLiquido;
	private List<VolumenVentaProductoAnio> listaEnvase;
	private List<VolumenVentaProductoAnio> listaOtros;

	public ListasVolumenVentaProductoAnio() {
	}

	public ListasVolumenVentaProductoAnio(Long id,
			List<VolumenVentaProductoAnio> listaLiquido,
			List<VolumenVentaProductoAnio> listaEnvase,
			List<VolumenVentaProductoAnio> listaOtros) {
		this.id = id;
		this.listaLiquido = listaLiquido;
		this.listaEnvase = listaEnvase;
		this.listaOtros = listaOtros;
	}

	public Long getId() {
		return id;
	}

	public List<VolumenVentaProductoAnio> getListaEnvase() {
		return listaEnvase;
	}

	public List<VolumenVentaProductoAnio> getListaLiquido() {
		return listaLiquido;
	}

	public List<VolumenVentaProductoAnio> getListaOtros() {
		return listaOtros;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setListaEnvase(List<VolumenVentaProductoAnio> listaEnvase) {
		this.listaEnvase = listaEnvase;
	}

	public void setListaLiquido(List<VolumenVentaProductoAnio> listaLiquido) {
		this.listaLiquido = listaLiquido;
	}

	public void setListaOtros(List<VolumenVentaProductoAnio> listaOtros) {
		this.listaOtros = listaOtros;
	}

}