package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML;

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
	@XmlType(name = "impuesto", propOrder = { "codigo", "codigoPorcentaje",
			"tarifa", "baseImponible", "valor" })
	public static class Impuesto {
		@XmlElement(required = true)
		protected String codigo;
		@XmlElement(required = true)
		protected String codigoPorcentaje;
		protected BigDecimal tarifa;
		@XmlElement(required = true)
		protected BigDecimal baseImponible;
		@XmlElement(required = true)
		protected BigDecimal valor;

		public BigDecimal getBaseImponible() {
			return this.baseImponible;
		}

		public String getCodigo() {
			return this.codigo;
		}

		public String getCodigoPorcentaje() {
			return this.codigoPorcentaje;
		}

		public BigDecimal getTarifa() {
			return this.tarifa;
		}

		public BigDecimal getValor() {
			return this.valor;
		}

		public void setBaseImponible(BigDecimal value) {
			this.baseImponible = value;
		}

		public void setCodigo(String value) {
			this.codigo = value;
		}

		public void setCodigoPorcentaje(String value) {
			this.codigoPorcentaje = value;
		}

		public void setTarifa(BigDecimal value) {
			this.tarifa = value;
		}

		public void setValor(BigDecimal value) {
			this.valor = value;
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