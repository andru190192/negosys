package ec.com.redepronik.negosys.invfac.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.KardexReporte;

public interface KardexService {

	@Transactional
	public String actualizar(Kardex kardex);

	@Transactional
	public void actualizarNotaKardex(String codigoDocumentoAnterior,
			String codigoDocumentoNuevo);

	@Transactional
	public Boolean comprobarKardexInicial(Kardex kardex);

	@Transactional
	public List<KardexReporte> generarkardex(Producto producto,
			Date fechaInicio, Date fechaFinal, Local local);

	@Transactional
	public Kardex generarKardexSaldo(Kardex nuevo, int cantidad,
			BigDecimal precio, boolean ingreso);

	@Transactional
	public void insertar(Kardex kardex);

	@Transactional
	public boolean insertarKardexInicial(Producto producto, Local local,
			BigDecimal precio, Integer cantidad);

	public List<KardexReporte> invertirKardex(List<KardexReporte> list);

	@Transactional
	public Kardex obtenerSaldoActual(String ean);

	@Transactional
	public Kardex obtenerSaldoActual(String ean, Integer localId);

	@Transactional
	public Map<String, Object> ponderarPrecio(Producto producto, int bodegaId,
			BigDecimal precio, int cantidad);

}