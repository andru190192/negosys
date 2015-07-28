package ec.com.redepronik.negosys.invfac.entityAux;

import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.MotivoBaja;
import ec.com.redepronik.negosys.invfac.entity.Producto;

public class BajaInventarioReporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Producto producto;
	private String codigo;
	private String descripcion;
	private MotivoBaja motivoBaja;
	private Integer cantidad;
	private Integer cantidadKardex;
	private String nombreCantidad;
	private BigDecimal precioCosto;
	private BigDecimal importe;

	public BajaInventarioReporte() {
		this.producto = new Producto();
		this.setCodigo("");
		this.descripcion = "";
		this.cantidad = 0;
		this.cantidadKardex = 0;
		this.nombreCantidad = "";
		this.precioCosto = newBigDecimal();
		this.importe = newBigDecimal();
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public Integer getCantidadKardex() {
		return cantidadKardex;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public MotivoBaja getMotivoBaja() {
		return motivoBaja;
	}

	public String getNombreCantidad() {
		return nombreCantidad;
	}

	public BigDecimal getPrecioCosto() {
		return precioCosto;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public void setCantidadKardex(Integer cantidadKardex) {
		this.cantidadKardex = cantidadKardex;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public void setMotivoBaja(MotivoBaja motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public void setNombreCantidad(String nombreCantidad) {
		this.nombreCantidad = nombreCantidad;
	}

	public void setPrecioCosto(BigDecimal precioCosto) {
		this.precioCosto = precioCosto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
