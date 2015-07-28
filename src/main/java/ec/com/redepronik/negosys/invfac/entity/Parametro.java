package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "parametro")
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String codigoContribuyente;
	private Boolean contabilidad;
	private String direccion;
	private String nombreComercial;
	private Integer precisionDecimal;
	private String razonSocial;
	private String ruc;
	private Short esperaAutorizacion;
	private Boolean tipoEmision;
	private Boolean facturacionElectronica;
	private Boolean tipoAmbiente;
	private String ipProxy;
	private String puertoProxy;
	private String usuarioProxy;
	private String claveProxy;
	private Integer tipoRedondeo;
	private String autorizacionSri;
	private String cenResolucion;
	private Date fechaInicioAutorizacion;
	private Date fechaFinAutorizacion;
	private Date fechaCorte;
	private String userAwsS3;
	private String passAwsS3;
	private String bucketAwsS3;
	private String email;
	private String passEmail;
	private String passToken;
	private TipoServidor tipoServidor;
	private String wsBucket;

	public Parametro() {
	}

	public Parametro(Integer id, String codigoContribuyente,
			Boolean contabilidad, String direccion, String nombreComercial,
			Integer precisionDecimal, String razonSocial, String ruc,
			Short esperaAutorizacion, Boolean tipoEmision,
			Boolean facturacionElectronica, Boolean tipoAmbiente,
			String ipProxy, String puertoProxy, String usuarioProxy,
			String claveProxy, Integer tipoRedondeo, String autorizacionSri,
			String cenResolucion, Date fechaInicioAutorizacion,
			Date fechaFinAutorizacion) {
		this.id = id;
		this.codigoContribuyente = codigoContribuyente;
		this.contabilidad = contabilidad;
		this.direccion = direccion;
		this.nombreComercial = nombreComercial;
		this.precisionDecimal = precisionDecimal;
		this.razonSocial = razonSocial;
		this.ruc = ruc;
		this.esperaAutorizacion = esperaAutorizacion;
		this.tipoEmision = tipoEmision;
		this.facturacionElectronica = facturacionElectronica;
		this.tipoAmbiente = tipoAmbiente;
		this.ipProxy = ipProxy;
		this.puertoProxy = puertoProxy;
		this.usuarioProxy = usuarioProxy;
		this.claveProxy = claveProxy;
		this.tipoRedondeo = tipoRedondeo;
		this.autorizacionSri = autorizacionSri;
		this.cenResolucion = cenResolucion;
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
		this.fechaFinAutorizacion = fechaFinAutorizacion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parametro other = (Parametro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(name = "autorizacionsri", length = 15)
	public String getAutorizacionSri() {
		return autorizacionSri;
	}

	@Column(name = "bucketawss3", length = 25)
	public String getBucketAwsS3() {
		return bucketAwsS3;
	}

	@Column(name = "cenresolucion", length = 15)
	public String getCenResolucion() {
		return cenResolucion;
	}

	@Column(name = "claveproxy", length = 15)
	public String getClaveProxy() {
		return claveProxy;
	}

	@Column(name = "codigocontribuyente", length = 20)
	public String getCodigoContribuyente() {
		return codigoContribuyente;
	}

	@Column(nullable = false)
	public Boolean getContabilidad() {
		return this.contabilidad;
	}

	@Column(nullable = false, length = 100)
	public String getDireccion() {
		return this.direccion;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return email;
	}

	@Column(name = "wsbucket", length = 50)
	public String getWsBucket() {
		return wsBucket;
	}

	public void setWsBucket(String wsBucket) {
		this.wsBucket = wsBucket;
	}

	@Column(name = "esperaautorizacion", nullable = false)
	public Short getEsperaAutorizacion() {
		return esperaAutorizacion;
	}

	@Column(name = "facturacionelectronica", nullable = false)
	public Boolean getFacturacionElectronica() {
		return facturacionElectronica;
	}

	@Column(name = "fechafinautorizacion")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinAutorizacion() {
		return fechaFinAutorizacion;
	}

	@Column(name = "fechainicioautorizacion")
	@Temporal(TemporalType.DATE)
	public Date getFechaInicioAutorizacion() {
		return fechaInicioAutorizacion;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "PARAMETRO_PARAMETERID_GENERATOR", sequenceName = "PARAMETRO_PARAMETERID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETRO_PARAMETERID_GENERATOR")
	@Column(name = "parameterid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	@Column(name = "`fechaCorte`")
	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	@Column(name = "ipproxy", length = 15)
	public String getIpProxy() {
		return ipProxy;
	}

	@Column(name = "nombrecomercial", nullable = false, length = 50)
	public String getNombreComercial() {
		return nombreComercial;
	}

	@Column(name = "passawss3", length = 50)
	public String getPassAwsS3() {
		return passAwsS3;
	}

	@Column(name = "passemail", length = 15)
	public String getPassEmail() {
		return passEmail;
	}

	@Column(name = "passtoken", length = 15)
	public String getPassToken() {
		return passToken;
	}

	@Column(name = "precisiondecimal", nullable = false)
	public Integer getPrecisionDecimal() {
		return precisionDecimal;
	}

	@Column(name = "puertoproxy", length = 5)
	public String getPuertoProxy() {
		return puertoProxy;
	}

	@Column(name = "razonsocial", nullable = false, length = 50)
	public String getRazonSocial() {
		return razonSocial;
	}

	@Column(nullable = false, length = 13)
	public String getRuc() {
		return this.ruc;
	}

	@Column(name = "tipoambiente", nullable = false)
	public Boolean getTipoAmbiente() {
		return tipoAmbiente;
	}

	@Column(name = "tipoemision", nullable = false)
	public Boolean getTipoEmision() {
		return tipoEmision;
	}

	@Column(name = "tiporedondeo", nullable = false)
	public Integer getTipoRedondeo() {
		return tipoRedondeo;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "tiposervidoremail")
	public TipoServidor getTipoServidor() {
		return tipoServidor;
	}

	@Column(name = "userawss3", length = 30)
	public String getUserAwsS3() {
		return userAwsS3;
	}

	@Column(name = "usuarioproxy", length = 15)
	public String getUsuarioProxy() {
		return usuarioProxy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setAutorizacionSri(String autorizacionSri) {
		this.autorizacionSri = autorizacionSri;
	}

	public void setBucketAwsS3(String bucketAwsS3) {
		this.bucketAwsS3 = bucketAwsS3;
	}

	public void setCenResolucion(String cenResolucion) {
		this.cenResolucion = cenResolucion;
	}

	public void setClaveProxy(String claveProxy) {
		this.claveProxy = claveProxy;
	}

	public void setCodigoContribuyente(String codigoContribuyente) {
		this.codigoContribuyente = codigoContribuyente;
	}

	public void setContabilidad(Boolean contabilidad) {
		this.contabilidad = contabilidad;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEsperaAutorizacion(Short esperaAutorizacion) {
		this.esperaAutorizacion = esperaAutorizacion;
	}

	public void setFacturacionElectronica(Boolean facturacionElectronica) {
		this.facturacionElectronica = facturacionElectronica;
	}

	public void setFechaFinAutorizacion(Date fechaFinAutorizacion) {
		this.fechaFinAutorizacion = fechaFinAutorizacion;
	}

	public void setFechaInicioAutorizacion(Date fechaInicioAutorizacion) {
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIpProxy(String ipProxy) {
		this.ipProxy = ipProxy;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public void setPassAwsS3(String passAwsS3) {
		this.passAwsS3 = passAwsS3;
	}

	public void setPassEmail(String passEmail) {
		this.passEmail = passEmail;
	}

	public void setPassToken(String passToken) {
		this.passToken = passToken;
	}

	public void setPrecisionDecimal(Integer precisionDecimal) {
		this.precisionDecimal = precisionDecimal;
	}

	public void setPuertoProxy(String puertoProxy) {
		this.puertoProxy = puertoProxy;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public void setTipoAmbiente(Boolean tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

	public void setTipoEmision(Boolean tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	public void setTipoRedondeo(Integer tipoRedondeo) {
		this.tipoRedondeo = tipoRedondeo;
	}

	public void setTipoServidor(TipoServidor tipoServidor) {
		this.tipoServidor = tipoServidor;
	}

	public void setUserAwsS3(String userAwsS3) {
		this.userAwsS3 = userAwsS3;
	}

	public void setUsuarioProxy(String usuarioProxy) {
		this.usuarioProxy = usuarioProxy;
	}

}