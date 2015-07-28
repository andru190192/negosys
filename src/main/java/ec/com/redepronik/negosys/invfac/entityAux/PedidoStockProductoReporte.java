package ec.com.redepronik.negosys.invfac.entityAux;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PedidoStockProductoReporte {

	@Id
	private Long id;
	private String nombre;
	private Integer stock;
	private Integer diferencia;
	private Integer cantidadMinima;

	public PedidoStockProductoReporte() {
	}

	public PedidoStockProductoReporte(Long id, String nombre, Integer stock,
			Integer diferencia, Integer cantidadMinima) {
		this.id = id;
		this.nombre = nombre;
		this.stock = stock;
		this.diferencia = diferencia;
		this.cantidadMinima = cantidadMinima;
	}

	public Integer getCantidadMinima() {
		return cantidadMinima;
	}

	public Integer getDiferencia() {
		return diferencia;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getStock() {
		return stock;
	}

	public void setCantidadMinima(Integer cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}

	public void setDiferencia(Integer diferencia) {
		this.diferencia = diferencia;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

}
