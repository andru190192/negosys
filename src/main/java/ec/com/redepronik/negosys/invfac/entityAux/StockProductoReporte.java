package ec.com.redepronik.negosys.invfac.entityAux;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StockProductoReporte {

	@Id
	private Long id;
	private String grupo;
	private String nombre;
	private Integer cantidadMinima;
	private Integer stock;
	private String stockString;

	public StockProductoReporte() {

	}

	public StockProductoReporte(Long id, String grupo, String nombre,
			Integer cantidadMinima, Integer stock, String stockString) {
		this.id = id;
		this.grupo = grupo;
		this.nombre = nombre;
		this.cantidadMinima = cantidadMinima;
		this.stock = stock;
		this.stockString = stockString;
	}

	public Integer getCantidadMinima() {
		return cantidadMinima;
	}

	public String getGrupo() {
		return grupo;
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

	public String getStockString() {
		return stockString;
	}

	public void setCantidadMinima(Integer cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
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

	public void setStockString(String stockString) {
		this.stockString = stockString;
	}

}