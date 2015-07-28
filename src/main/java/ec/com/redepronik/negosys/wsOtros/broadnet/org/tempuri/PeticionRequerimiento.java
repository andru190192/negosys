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
 *         &lt;element name="tipoTransaccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoProceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cajero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clave" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="proveedor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="servicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autorizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cupon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tipoTransaccion", "codigoProceso", "monto",
		"cajero", "clave", "tid", "mid", "proveedor", "servicio", "cuenta",
		"autorizacion", "referencia", "lote", "cupon" })
@XmlRootElement(name = "peticionRequerimiento")
public class PeticionRequerimiento {

	protected String tipoTransaccion;
	protected String codigoProceso;
	protected String monto;
	protected String cajero;
	protected String clave;
	protected String tid;
	protected String mid;
	protected String proveedor;
	protected String servicio;
	protected String cuenta;
	protected String autorizacion;
	protected String referencia;
	protected String lote;
	protected String cupon;

	/**
	 * Obtiene el valor de la propiedad autorizacion.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAutorizacion() {
		return autorizacion;
	}

	/**
	 * Obtiene el valor de la propiedad cajero.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCajero() {
		return cajero;
	}

	/**
	 * Obtiene el valor de la propiedad clave.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * Obtiene el valor de la propiedad codigoProceso.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodigoProceso() {
		return codigoProceso;
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
	 * Obtiene el valor de la propiedad monto.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMonto() {
		return monto;
	}

	/**
	 * Obtiene el valor de la propiedad proveedor.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProveedor() {
		return proveedor;
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
	 * Obtiene el valor de la propiedad servicio.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getServicio() {
		return servicio;
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
	 * Obtiene el valor de la propiedad tipoTransaccion.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	/**
	 * Define el valor de la propiedad autorizacion.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAutorizacion(String value) {
		this.autorizacion = value;
	}

	/**
	 * Define el valor de la propiedad cajero.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCajero(String value) {
		this.cajero = value;
	}

	/**
	 * Define el valor de la propiedad clave.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClave(String value) {
		this.clave = value;
	}

	/**
	 * Define el valor de la propiedad codigoProceso.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodigoProceso(String value) {
		this.codigoProceso = value;
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
	 * Define el valor de la propiedad monto.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMonto(String value) {
		this.monto = value;
	}

	/**
	 * Define el valor de la propiedad proveedor.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProveedor(String value) {
		this.proveedor = value;
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
	 * Define el valor de la propiedad servicio.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setServicio(String value) {
		this.servicio = value;
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

	/**
	 * Define el valor de la propiedad tipoTransaccion.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTipoTransaccion(String value) {
		this.tipoTransaccion = value;
	}

}
