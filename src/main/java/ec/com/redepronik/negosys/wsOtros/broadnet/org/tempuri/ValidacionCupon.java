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
 *         &lt;element name="iso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cupon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secuencial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "iso", "terminal", "cupon", "secuencial" })
@XmlRootElement(name = "validacionCupon")
public class ValidacionCupon {

	protected String iso;
	protected String terminal;
	protected String cupon;
	protected String secuencial;

	/**
	 * Obtiene el valor de la propiedad cupon.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCupon() {
		return cupon;
	}

	/**
	 * Obtiene el valor de la propiedad iso.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIso() {
		return iso;
	}

	/**
	 * Obtiene el valor de la propiedad secuencial.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSecuencial() {
		return secuencial;
	}

	/**
	 * Obtiene el valor de la propiedad terminal.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTerminal() {
		return terminal;
	}

	/**
	 * Define el valor de la propiedad cupon.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCupon(String value) {
		this.cupon = value;
	}

	/**
	 * Define el valor de la propiedad iso.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIso(String value) {
		this.iso = value;
	}

	/**
	 * Define el valor de la propiedad secuencial.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSecuencial(String value) {
		this.secuencial = value;
	}

	/**
	 * Define el valor de la propiedad terminal.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTerminal(String value) {
		this.terminal = value;
	}

}
