package ec.com.redepronik.negosys.invfac.entityAux;

public class PrecioProductoReporte {

	private Long productoId;
	private String nombre;
	private String preciosConImpuestos;
	private String PreciosSinImpuestos;

	public PrecioProductoReporte() {

	}

	public PrecioProductoReporte(Long productoId, String nombre,
			String preciosConImpuestos, String preciosSinImpuestos) {
		this.productoId = productoId;
		this.nombre = nombre;
		this.preciosConImpuestos = preciosConImpuestos;
		PreciosSinImpuestos = preciosSinImpuestos;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPreciosConImpuestos() {
		return preciosConImpuestos;
	}

	public String getPreciosSinImpuestos() {
		return PreciosSinImpuestos;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPreciosConImpuestos(String preciosConImpuestos) {
		this.preciosConImpuestos = preciosConImpuestos;
	}

	public void setPreciosSinImpuestos(String preciosSinImpuestos) {
		PreciosSinImpuestos = preciosSinImpuestos;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

}
