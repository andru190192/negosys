package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.guiaRemision;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "destinatario" })
public class Destinatarios {
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "destinatario", propOrder = { "identificacionDestinatario",
			"razonSocialDestinatario", "dirDestinatario", "motivoTraslado",
			"docAduaneroUnico", "codEstabDestino", "ruta", "codDocSustento",
			"numDocSustento", "numAutDocSustento", "fechaEmisionDocSustento",
			"detalles" })
	public static class Destinatario {
		protected String identificacionDestinatario;
		@XmlElement(required = true)
		protected String razonSocialDestinatario;
		@XmlElement(required = true)
		protected String dirDestinatario;
		@XmlElement(required = true)
		protected String motivoTraslado;
		protected String docAduaneroUnico;
		protected String codEstabDestino;
		protected String ruta;
		protected String codDocSustento;
		protected String numDocSustento;
		protected String numAutDocSustento;
		protected String fechaEmisionDocSustento;
		@XmlElement(required = true)
		protected Detalles detalles;

		public String getCodDocSustento() {
			return this.codDocSustento;
		}

		public String getCodEstabDestino() {
			return this.codEstabDestino;
		}

		public Detalles getDetalles() {
			return this.detalles;
		}

		public String getDirDestinatario() {
			return this.dirDestinatario;
		}

		public String getDocAduaneroUnico() {
			return this.docAduaneroUnico;
		}

		public String getFechaEmisionDocSustento() {
			return this.fechaEmisionDocSustento;
		}

		public String getIdentificacionDestinatario() {
			return this.identificacionDestinatario;
		}

		public String getMotivoTraslado() {
			return this.motivoTraslado;
		}

		public String getNumAutDocSustento() {
			return this.numAutDocSustento;
		}

		public String getNumDocSustento() {
			return this.numDocSustento;
		}

		public String getRazonSocialDestinatario() {
			return this.razonSocialDestinatario;
		}

		public String getRuta() {
			return this.ruta;
		}

		public void setCodDocSustento(String value) {
			this.codDocSustento = value;
		}

		public void setCodEstabDestino(String value) {
			this.codEstabDestino = value;
		}

		public void setDetalles(Detalles value) {
			this.detalles = value;
		}

		public void setDirDestinatario(String value) {
			this.dirDestinatario = value;
		}

		public void setDocAduaneroUnico(String value) {
			this.docAduaneroUnico = value;
		}

		public void setFechaEmisionDocSustento(String value) {
			this.fechaEmisionDocSustento = value;
		}

		public void setIdentificacionDestinatario(String value) {
			this.identificacionDestinatario = value;
		}

		public void setMotivoTraslado(String value) {
			this.motivoTraslado = value;
		}

		public void setNumAutDocSustento(String value) {
			this.numAutDocSustento = value;
		}

		public void setNumDocSustento(String value) {
			this.numDocSustento = value;
		}

		public void setRazonSocialDestinatario(String value) {
			this.razonSocialDestinatario = value;
		}

		public void setRuta(String value) {
			this.ruta = value;
		}

	}

	@XmlElement(required = true)
	protected List<Destinatario> destinatario;

	public List<Destinatario> getDestinatario() {
		if (this.destinatario == null) {
			this.destinatario = new ArrayList<Destinatario>();
		}
		return this.destinatario;
	}
}