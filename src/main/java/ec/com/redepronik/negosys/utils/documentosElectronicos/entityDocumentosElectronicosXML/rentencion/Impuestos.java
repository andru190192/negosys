package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "impuesto" })
public class Impuestos {
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "impuesto", propOrder = { "codigo", "codigoRetencion",
			"baseImponible", "porcentajeRetener", "valorRetenido",
			"codDocSustento", "numDocSustento", "fechaEmisionDocSustento" })
	public static class Impuesto {
		@XmlElement(required = true)
		protected String codigo;
		@XmlElement(required = true)
		protected String codigoRetencion;
		@XmlElement(required = true)
		protected BigDecimal baseImponible;
		@XmlElement(required = true)
		protected BigDecimal porcentajeRetener;
		@XmlElement(required = true)
		protected BigDecimal valorRetenido;
		@XmlElement(required = true)
		protected String codDocSustento;
		protected String numDocSustento;
		protected String fechaEmisionDocSustento;

		public BigDecimal getBaseImponible() {
			return this.baseImponible;
		}

		public String getCodDocSustento() {
			return this.codDocSustento;
		}

		public String getCodigo() {
			return this.codigo;
		}

		public String getCodigoRetencion() {
			return this.codigoRetencion;
		}

		public String getFechaEmisionDocSustento() {
			return this.fechaEmisionDocSustento;
		}

		public String getNumDocSustento() {
			return this.numDocSustento;
		}

		public BigDecimal getPorcentajeRetener() {
			return this.porcentajeRetener;
		}

		public BigDecimal getValorRetenido() {
			return this.valorRetenido;
		}

		public void setBaseImponible(BigDecimal value) {
			this.baseImponible = value;
		}

		public void setCodDocSustento(String value) {
			this.codDocSustento = value;
		}

		public void setCodigo(String value) {
			this.codigo = value;
		}

		public void setCodigoRetencion(String value) {
			this.codigoRetencion = value;
		}

		public void setFechaEmisionDocSustento(String value) {
			this.fechaEmisionDocSustento = value;
		}

		public void setNumDocSustento(String value) {
			this.numDocSustento = value;
		}

		public void setPorcentajeRetener(BigDecimal value) {
			this.porcentajeRetener = value;
		}

		public void setValorRetenido(BigDecimal value) {
			this.valorRetenido = value;
		}
	}

	@XmlElement(required = true)
	protected List<Impuesto> impuesto;

	public List<Impuesto> getImpuesto() {
		if (this.impuesto == null) {
			this.impuesto = new ArrayList<Impuesto>();
		}
		return this.impuesto;
	}

}
