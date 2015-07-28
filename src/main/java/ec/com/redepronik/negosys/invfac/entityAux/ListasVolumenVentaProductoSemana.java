package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.util.List;

//@Entity
public class ListasVolumenVentaProductoSemana implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Id
	private Long id;
	private List<VolumenVentaProductoSemana> listaLiquido;
	private List<VolumenVentaProductoSemana> listaEnvase;
	private List<VolumenVentaProductoSemana> listaOtros;

	public ListasVolumenVentaProductoSemana() {
	}

	public ListasVolumenVentaProductoSemana(Long id,
			List<VolumenVentaProductoSemana> listaLiquido,
			List<VolumenVentaProductoSemana> listaEnvase,
			List<VolumenVentaProductoSemana> listaOtros) {
		this.id = id;
		this.listaLiquido = listaLiquido;
		this.listaEnvase = listaEnvase;
		this.listaOtros = listaOtros;
	}

	public Long getId() {
		return id;
	}

	public List<VolumenVentaProductoSemana> getListaEnvase() {
		return listaEnvase;
	}

	public List<VolumenVentaProductoSemana> getListaLiquido() {
		return listaLiquido;
	}

	public List<VolumenVentaProductoSemana> getListaOtros() {
		return listaOtros;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setListaEnvase(List<VolumenVentaProductoSemana> listaEnvase) {
		this.listaEnvase = listaEnvase;
	}

	public void setListaLiquido(List<VolumenVentaProductoSemana> listaLiquido) {
		this.listaLiquido = listaLiquido;
	}

	public void setListaOtros(List<VolumenVentaProductoSemana> listaOtros) {
		this.listaOtros = listaOtros;
	}

}