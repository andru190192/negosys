package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;
import java.util.List;

public class ListFacturaImprimir implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<FacturaImprimir> listFacturaImprimir;

	public ListFacturaImprimir() {

	}

	public ListFacturaImprimir(List<FacturaImprimir> listFacturaImprimir) {
		this.listFacturaImprimir = listFacturaImprimir;
	}

	public List<FacturaImprimir> getListFacturaImprimir() {
		return listFacturaImprimir;
	}

	public void setListFacturaImprimir(List<FacturaImprimir> listFacturaImprimir) {
		this.listFacturaImprimir = listFacturaImprimir;
	}

}
