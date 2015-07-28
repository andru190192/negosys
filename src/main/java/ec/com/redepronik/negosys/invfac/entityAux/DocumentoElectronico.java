package ec.com.redepronik.negosys.invfac.entityAux;

import java.io.Serializable;

public class DocumentoElectronico implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombreDocumento;
	private String tipoDocumento;
	private String documento;

	public DocumentoElectronico() {
	}

	public DocumentoElectronico(String nombreDocumento, String tipoDocumento,
			String documento) {
		this.nombreDocumento = nombreDocumento;
		this.tipoDocumento = tipoDocumento;
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNombreDocumento() {
		return nombreDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
