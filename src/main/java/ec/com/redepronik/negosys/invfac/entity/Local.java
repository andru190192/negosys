package ec.com.redepronik.negosys.invfac.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import ec.com.redepronik.negosys.rrhh.entity.Provincia;

@Entity
@Table(name = "local")
public class Local implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Boolean activo;
	private String ciudad;
	private String nombre;
	private String direccion;
	private String codigoEstablecimiento;
	private Boolean matriz;
	private Date fechaInicio;
	private Date fechaCierre;
	private Date fechaReinicio;
	private Provincia provincia;
	private String parroquia;
	private String ciudadela;
	private String numero;
	private String interseccion;
	private String referencia;
	private String piso;
	private String oficina;
	private String email;
	private String telefonoTrabajo;
	private String telefonoCelular;
	private String web;
	private Integer puntoEmisionFactura;
	private Integer secuenciaFactura;
	private Integer puntoEmisionNotaCredito;
	private Integer secuenciaNotaCredito;
	private Integer puntoEmisionNotaDebito;
	private Integer secuenciaNotaDebito;
	private Integer puntoEmisionGuiaRemision;
	private Integer secuenciaGuiaRemision;
	private Integer puntoEmisionRetencion;
	private Integer secuenciaRetencion;
	private Integer puntoEmisionTraspaso;
	private Integer secuenciaTraspaso;
	private Integer puntoEmisionBajaInventario;
	private Integer secuenciaBajaInventario;
	private Integer puntoEmisionCotizacion;
	private Integer secuenciaCotizacion;
	private Integer puntoEmisionPedido;
	private Integer secuenciaPedido;
	private List<LocalBodeguero> localBodeguero;
	private List<DetalleIngreso> detalleIngresos;
	private List<Kardex> kardexs;
	private List<Traspaso> traspasos1;
	private List<Traspaso> traspasos2;
	private List<LocalCajero> localCajero;

	public Local() {
	}

	public Local(Integer id, Boolean activo, String ciudad, String nombre,
			String direccion, String codigoEstablecimiento, Boolean matriz,
			Date fechaInicio, Date fechaCierre, Date fechaReinicio,
			Provincia provincia, String parroquia, String ciudadela,
			String numero, String interseccion, String referencia, String piso,
			String oficina, String email, String telefonoTrabajo,
			String telefonoCelular, String web, Integer puntoEmisionFactura,
			Integer secuenciaFactura, Integer puntoEmisionNotaCredito,
			Integer secuenciaNotaCredito, Integer puntoEmisionNotaDebito,
			Integer secuenciaNotaDebito, Integer puntoEmisionGuiaRemision,
			Integer secuenciaGuiaRemision, Integer puntoEmisionRetencion,
			Integer secuenciaRetencion, Integer puntoEmisionTraspaso,
			Integer secuenciaTraspaso, Integer puntoEmisionBajaInventario,
			Integer secuenciaBajaInventario, Integer puntoEmisionCotizacion,
			Integer secuenciaCotizacion, Integer puntoEmisionPedido,
			Integer secuenciaPedido, List<LocalBodeguero> localBodeguero,
			List<DetalleIngreso> detalleIngresos, List<Kardex> kardexs,
			List<Traspaso> traspasos1, List<Traspaso> traspasos2,
			List<LocalCajero> localCajero) {
		this.id = id;
		this.activo = activo;
		this.ciudad = ciudad;
		this.nombre = nombre;
		this.direccion = direccion;
		this.codigoEstablecimiento = codigoEstablecimiento;
		this.matriz = matriz;
		this.fechaInicio = fechaInicio;
		this.fechaCierre = fechaCierre;
		this.fechaReinicio = fechaReinicio;
		this.provincia = provincia;
		this.parroquia = parroquia;
		this.ciudadela = ciudadela;
		this.numero = numero;
		this.interseccion = interseccion;
		this.referencia = referencia;
		this.piso = piso;
		this.oficina = oficina;
		this.email = email;
		this.telefonoTrabajo = telefonoTrabajo;
		this.telefonoCelular = telefonoCelular;
		this.web = web;
		this.puntoEmisionFactura = puntoEmisionFactura;
		this.secuenciaFactura = secuenciaFactura;
		this.puntoEmisionNotaCredito = puntoEmisionNotaCredito;
		this.secuenciaNotaCredito = secuenciaNotaCredito;
		this.puntoEmisionNotaDebito = puntoEmisionNotaDebito;
		this.secuenciaNotaDebito = secuenciaNotaDebito;
		this.puntoEmisionGuiaRemision = puntoEmisionGuiaRemision;
		this.secuenciaGuiaRemision = secuenciaGuiaRemision;
		this.puntoEmisionRetencion = puntoEmisionRetencion;
		this.secuenciaRetencion = secuenciaRetencion;
		this.puntoEmisionTraspaso = puntoEmisionTraspaso;
		this.secuenciaTraspaso = secuenciaTraspaso;
		this.puntoEmisionBajaInventario = puntoEmisionBajaInventario;
		this.secuenciaBajaInventario = secuenciaBajaInventario;
		this.puntoEmisionCotizacion = puntoEmisionCotizacion;
		this.secuenciaCotizacion = secuenciaCotizacion;
		this.puntoEmisionPedido = puntoEmisionPedido;
		this.secuenciaPedido = secuenciaPedido;
		this.localBodeguero = localBodeguero;
		this.detalleIngresos = detalleIngresos;
		this.kardexs = kardexs;
		this.traspasos1 = traspasos1;
		this.traspasos2 = traspasos2;
		this.localCajero = localCajero;
	}

	public DetalleIngreso addDetalleIngreso(DetalleIngreso detalleIngreso) {
		getDetalleIngresos().add(detalleIngreso);
		detalleIngreso.setLocal(this);
		return detalleIngreso;
	}

	public Kardex addKardex(Kardex kardex) {
		getKardexs().add(kardex);
		kardex.setLocal(this);
		return kardex;
	}

	public LocalBodeguero addLocalBodegero(LocalBodeguero localBodegero) {
		getLocalBodeguero().add(localBodegero);
		localBodegero.setLocal(this);
		return localBodegero;
	}

	public LocalCajero addLocalCajero(LocalCajero localCajero) {
		getLocalCajero().add(localCajero);
		localCajero.setLocal(this);
		return localCajero;
	}

	public Traspaso addTraspasos1(Traspaso traspasos1) {
		getTraspasos1().add(traspasos1);
		traspasos1.setLocal1(this);

		return traspasos1;
	}

	public Traspaso addTraspasos2(Traspaso traspasos2) {
		getTraspasos2().add(traspasos2);
		traspasos2.setLocal2(this);

		return traspasos2;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Local other = (Local) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Boolean getActivo() {
		return this.activo;
	}

	@NotBlank(message = "ESCOJA LA CIUDAD DEL ESTABLECIMIENTO")
	@Column(nullable = false, length = 20)
	public String getCiudad() {
		return this.ciudad;
	}

	@Column(length = 40)
	public String getCiudadela() {
		return ciudadela;
	}

	@Pattern(regexp = "[0-9]{3,3}+", message = "EL CAMPO CÓDIGO ACEPTA 3 DÍGITOS NUMÉRICOS")
	@Column(name = "codigoestablecimiento", nullable = false, length = 3)
	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "local")
	public List<DetalleIngreso> getDetalleIngresos() {
		return detalleIngresos;
	}

	@Length(min = 5, max = 50, message = "EL CAMPO DIRECCION ACEPTA DE 5 A 50 LETRAS")
	@Column(nullable = false, length = 50)
	public String getDireccion() {
		return this.direccion;
	}

	@Column(length = 40)
	public String getEmail() {
		return email;
	}

	@Column(name = "fechacierre")
	public Date getFechaCierre() {
		return fechaCierre;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@NotNull(message = "INGRESE LA FECHA DE INCIO DEL ESTABLECIMIENTO")
	@Column(name = "fechainicio", nullable = false)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@Column(name = "fechareinicio")
	public Date getFechaReinicio() {
		return fechaReinicio;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "LOCAL_LOCALID_GENERATOR", sequenceName = "LOCAL_LOCALID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCAL_LOCALID_GENERATOR")
	@Column(name = "localid", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(length = 50)
	public String getInterseccion() {
		return interseccion;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "local")
	public List<Kardex> getKardexs() {
		return kardexs;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "local")
	public List<LocalBodeguero> getLocalBodeguero() {
		return localBodeguero;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "local")
	public List<LocalCajero> getLocalCajero() {
		return localCajero;
	}

	@Column(nullable = false)
	public Boolean getMatriz() {
		return matriz;
	}

	@Length(min = 5, max = 30, message = "EL CAMPO NOMBRE ACEPTA DE 5 A 30 LETRAS")
	@Column(nullable = false, length = 30)
	public String getNombre() {
		return nombre;
	}

	@Column(length = 10)
	public String getNumero() {
		return numero;
	}

	@Column(length = 40)
	public String getOficina() {
		return oficina;
	}

	@Column(length = 40)
	public String getParroquia() {
		return parroquia;
	}

	@Column(length = 3)
	public String getPiso() {
		return piso;
	}

	@Enumerated(EnumType.STRING)
	@NotNull(message = "INGRESE LA PROVINCIA DEL ESTABLECIMIENTO")
	@Column(name = "provincia", nullable = false)
	public Provincia getProvincia() {
		return provincia;
	}

	@Column(name = "puntoemisionbajainventario", nullable = false)
	public Integer getPuntoEmisionBajaInventario() {
		return puntoEmisionBajaInventario;
	}

	@Column(name = "puntoemisioncotizacion", nullable = false)
	public Integer getPuntoEmisionCotizacion() {
		return puntoEmisionCotizacion;
	}

	@Column(name = "puntoemisionfactura", nullable = false)
	public Integer getPuntoEmisionFactura() {
		return puntoEmisionFactura;
	}

	@Column(name = "puntoemisionguiaremision", nullable = false)
	public Integer getPuntoEmisionGuiaRemision() {
		return puntoEmisionGuiaRemision;
	}

	@Column(name = "puntoemisionnotacredito", nullable = false)
	public Integer getPuntoEmisionNotaCredito() {
		return puntoEmisionNotaCredito;
	}

	@Column(name = "puntoemisionnotadebito", nullable = false)
	public Integer getPuntoEmisionNotaDebito() {
		return puntoEmisionNotaDebito;
	}

	@Column(name = "puntoemisionpedido", nullable = false)
	public Integer getPuntoEmisionPedido() {
		return puntoEmisionPedido;
	}

	@Column(name = "puntoemisionretencion", nullable = false)
	public Integer getPuntoEmisionRetencion() {
		return puntoEmisionRetencion;
	}

	@Column(name = "puntoemisiontraspaso", nullable = false)
	public Integer getPuntoEmisionTraspaso() {
		return puntoEmisionTraspaso;
	}

	@Column(length = 50)
	public String getReferencia() {
		return referencia;
	}

	@Column(name = "secuenciabajainventario", nullable = false)
	public Integer getSecuenciaBajaInventario() {
		return secuenciaBajaInventario;
	}

	@Column(name = "secuenciacotizacion", nullable = false)
	public Integer getSecuenciaCotizacion() {
		return secuenciaCotizacion;
	}

	@Column(name = "secuenciafactura", nullable = false)
	public Integer getSecuenciaFactura() {
		return secuenciaFactura;
	}

	@Column(name = "secuenciaguiaremision", nullable = false)
	public Integer getSecuenciaGuiaRemision() {
		return secuenciaGuiaRemision;
	}

	@Column(name = "secuencianotacredito", nullable = false)
	public Integer getSecuenciaNotaCredito() {
		return secuenciaNotaCredito;
	}

	@Column(name = "secuencianotadebito", nullable = false)
	public Integer getSecuenciaNotaDebito() {
		return secuenciaNotaDebito;
	}

	@Column(name = "secuenciapedido", nullable = false)
	public Integer getSecuenciaPedido() {
		return secuenciaPedido;
	}

	@Column(name = "secuenciaretencion", nullable = false)
	public Integer getSecuenciaRetencion() {
		return secuenciaRetencion;
	}

	@Column(name = "secuenciatraspaso", nullable = false)
	public Integer getSecuenciaTraspaso() {
		return secuenciaTraspaso;
	}

	@Column(name = "telefonocelular", length = 15)
	public String getTelefonoCelular() {
		return telefonoCelular;
	}

	@Column(name = "telefonotrabajo", length = 15)
	public String getTelefonoTrabajo() {
		return telefonoTrabajo;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "local1")
	public List<Traspaso> getTraspasos1() {
		return traspasos1;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "local2")
	public List<Traspaso> getTraspasos2() {
		return traspasos2;
	}

	@Column(length = 50)
	public String getWeb() {
		return web;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DetalleIngreso removeDetalleingreso(DetalleIngreso detalleIngreso) {
		getDetalleIngresos().remove(detalleIngreso);
		detalleIngreso.setLocal(null);
		return detalleIngreso;
	}

	public Kardex removeKardex(Kardex kardex) {
		getKardexs().remove(kardex);
		kardex.setLocal(null);
		return kardex;
	}

	public LocalBodeguero removeLocalBodeguero(LocalBodeguero localBodeguero) {
		getLocalBodeguero().remove(localBodeguero);
		localBodeguero.setLocal(null);
		return localBodeguero;
	}

	public LocalCajero removeLocalCajero(LocalCajero localCajero) {
		getLocalCajero().remove(localCajero);
		localCajero.setLocal(null);
		return localCajero;
	}

	public Traspaso removeTraspasos1(Traspaso traspasos1) {
		getTraspasos1().remove(traspasos1);
		traspasos1.setLocal1(null);
		return traspasos1;
	}

	public Traspaso removeTraspasos2(Traspaso traspasos2) {
		getTraspasos2().remove(traspasos2);
		traspasos2.setLocal2(null);
		return traspasos2;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setCiudadela(String ciudadela) {
		this.ciudadela = ciudadela;
	}

	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}

	public void setDetalleIngresos(List<DetalleIngreso> detalleIngresos) {
		this.detalleIngresos = detalleIngresos;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaReinicio(Date fechaReinicio) {
		this.fechaReinicio = fechaReinicio;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInterseccion(String interseccion) {
		this.interseccion = interseccion;
	}

	public void setKardexs(List<Kardex> kardexs) {
		this.kardexs = kardexs;
	}

	public void setLocalBodeguero(List<LocalBodeguero> localBodegero) {
		this.localBodeguero = localBodegero;
	}

	public void setLocalCajero(List<LocalCajero> localCajero) {
		this.localCajero = localCajero;
	}

	public void setMatriz(Boolean matriz) {
		this.matriz = matriz;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public void setParroquia(String parroquia) {
		this.parroquia = parroquia;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public void setPuntoEmisionBajaInventario(Integer puntoEmisionBajaInventario) {
		this.puntoEmisionBajaInventario = puntoEmisionBajaInventario;
	}

	public void setPuntoEmisionCotizacion(Integer puntoEmisionCotizacion) {
		this.puntoEmisionCotizacion = puntoEmisionCotizacion;
	}

	public void setPuntoEmisionFactura(Integer puntoEmisionFactura) {
		this.puntoEmisionFactura = puntoEmisionFactura;
	}

	public void setPuntoEmisionGuiaRemision(Integer puntoEmisionGuiaRemision) {
		this.puntoEmisionGuiaRemision = puntoEmisionGuiaRemision;
	}

	public void setPuntoEmisionNotaCredito(Integer puntoEmisionNotaCredito) {
		this.puntoEmisionNotaCredito = puntoEmisionNotaCredito;
	}

	public void setPuntoEmisionNotaDebito(Integer puntoEmisionNotaDebito) {
		this.puntoEmisionNotaDebito = puntoEmisionNotaDebito;
	}

	public void setPuntoEmisionPedido(Integer puntoEmisionPedido) {
		this.puntoEmisionPedido = puntoEmisionPedido;
	}

	public void setPuntoEmisionRetencion(Integer puntoEmisionRetencion) {
		this.puntoEmisionRetencion = puntoEmisionRetencion;
	}

	public void setPuntoEmisionTraspaso(Integer puntoEmisionTraspaso) {
		this.puntoEmisionTraspaso = puntoEmisionTraspaso;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public void setSecuenciaBajaInventario(Integer secuenciaBajaInventario) {
		this.secuenciaBajaInventario = secuenciaBajaInventario;
	}

	public void setSecuenciaCotizacion(Integer secuenciaCotizacion) {
		this.secuenciaCotizacion = secuenciaCotizacion;
	}

	public void setSecuenciaFactura(Integer secuenciaFactura) {
		this.secuenciaFactura = secuenciaFactura;
	}

	public void setSecuenciaGuiaRemision(Integer secuenciaGuiaRemision) {
		this.secuenciaGuiaRemision = secuenciaGuiaRemision;
	}

	public void setSecuenciaNotaCredito(Integer secuenciaNotaCredito) {
		this.secuenciaNotaCredito = secuenciaNotaCredito;
	}

	public void setSecuenciaNotaDebito(Integer secuenciaNotaDebito) {
		this.secuenciaNotaDebito = secuenciaNotaDebito;
	}

	public void setSecuenciaPedido(Integer secuenciaPedido) {
		this.secuenciaPedido = secuenciaPedido;
	}

	public void setSecuenciaRetencion(Integer secuenciaRetencion) {
		this.secuenciaRetencion = secuenciaRetencion;
	}

	public void setSecuenciaTraspaso(Integer secuenciaTraspaso) {
		this.secuenciaTraspaso = secuenciaTraspaso;
	}

	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	public void setTelefonoTrabajo(String telefonoTrabajo) {
		this.telefonoTrabajo = telefonoTrabajo;
	}

	public void setTraspasos1(List<Traspaso> traspasos1) {
		this.traspasos1 = traspasos1;
	}

	public void setTraspasos2(List<Traspaso> traspasos2) {
		this.traspasos2 = traspasos2;
	}

	public void setWeb(String web) {
		this.web = web;
	}

}