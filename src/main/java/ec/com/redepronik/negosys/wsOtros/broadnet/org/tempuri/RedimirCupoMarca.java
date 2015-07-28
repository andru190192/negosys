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
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cupon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idproducto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cantidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tid", "mid", "cuenta", "referencia", "lote",
		"cupon", "idproducto", "cantidad" })
@XmlRootElement(name = "redimirCupoMarca")
public class RedimirCupoMarca {

	protected String tid;
	protected String mid;
	protected String cuenta;
	protected String referencia;
	protected String lote;
	protected String cupon;
	protected String idproducto;
	protected String cantidad;

	/**
	 * Obtiene el valor de la propiedad cantidad.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * Obtiene el valor de la propiedad cuenta.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCuenta() {
		return cuenta;
	}

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
	 * Obtiene el valor de la propiedad idproducto.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdproducto() {
		return idproducto;
	}

	/**
	 * Obtiene el valor de la propiedad lote.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLote() {
		return lote;
	}

	/**
	 * Obtiene el valor de la propiedad mid.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * Obtiene el valor de la propiedad referencia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getReferencia() {
		return referencia;
	}

	/**
	 * Obtiene el valor de la propiedad tid.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * Define el valor de la propiedad cantidad.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCantidad(String value) {
		this.cantidad = value;
	}

	/**
	 * Define el valor de la propiedad cuenta.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCuenta(String value) {
		this.cuenta = value;
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
	 * Define el valor de la propiedad idproducto.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdproducto(String value) {
		this.idproducto = value;
	}

	/**
	 * Define el valor de la propiedad lote.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLote(String value) {
		this.lote = value;
	}

	/**
	 * Define el valor de la propiedad mid.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMid(String value) {
		this.mid = value;
	}

	/**
	 * Define el valor de la propiedad referencia.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setReferencia(String value) {
		this.referencia = value;
	}

	/**
	 * Define el valor de la propiedad tid.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTid(String value) {
		this.tid = value;
	}

}
