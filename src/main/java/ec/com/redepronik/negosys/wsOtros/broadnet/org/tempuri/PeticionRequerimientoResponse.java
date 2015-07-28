package ec.com.redepronik.negosys.wsOtros.broadnet.org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Clase Java para anonymous complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que
 * haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="peticionRequerimientoResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "peticionRequerimientoResult" })
@XmlRootElement(name = "peticionRequerimientoResponse")
public class PeticionRequerimientoResponse {

	protected String peticionRequerimientoResult;

	/**
	 * Obtiene el valor de la propiedad peticionRequerimientoResult.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPeticionRequerimientoResult() {
		return peticionRequerimientoResult;
	}

	/**
	 * Define el valor de la propiedad peticionRequerimientoResult.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPeticionRequerimientoResult(String value) {
		this.peticionRequerimientoResult = value;
	}

}
