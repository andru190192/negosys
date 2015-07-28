package ec.com.redepronik.negosys.invfac.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.BajaInventario;
import ec.com.redepronik.negosys.invfac.entity.DetalleBajaInventario;
import ec.com.redepronik.negosys.invfac.entityAux.BajaInventarioReporte;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;

public interface BajaInventarioService {

	public BajaInventarioReporte asignar(
			BajaInventarioReporte bajaInventarioReporte);

	public BajaInventarioReporte asignar(DetalleBajaInventario de);

	public BigDecimal calcularCantidad(
			BajaInventarioReporte bajaInventarioReporte,
			List<BajaInventarioReporte> list);

	public void calcularImporte(BajaInventarioReporte fr);

	public void cambiarMotivoBaja(BajaInventarioReporte bajaInventarioReporte,
			List<BajaInventarioReporte> list);

	public boolean cargarProductoLista(
			BajaInventarioReporte bajaInventarioReporte,
			List<BajaInventarioReporte> list, int localId);

	public boolean convertirListaBajaInventarioReporteListaBajaInventarioDetalle(
			List<BajaInventarioReporte> list, BajaInventario bajaInventario);

	@Transactional
	public BajaInventario insertar(BajaInventario bajaInventario);

	@Transactional
	public List<BajaInventario> obtener(Boolean activo, int tipoDocumentoId);

	@Transactional
	public List<BajaInventario> obtener(String criterioBusquedaNumeroBaja,
			Date criterioBusquedaFechaDocumento);

	@Transactional(readOnly = true)
	public BajaInventario obtenerPorBajaInventarioId(Integer bajaInventarioId);

	public List<BajaInventarioReporte> quitarProductosCantidadCero(
			List<BajaInventarioReporte> list);

	@Transactional
	public CantidadFactura redondearCantidadFactura(
			CantidadFactura cantidadFactura);

	@Transactional
	public List<DetalleBajaInventario> sumarCantidades(
			List<DetalleBajaInventario> detalleBajaInventarios);

	public List<DetalleBajaInventario> sumarCantidadesTodo(
			List<DetalleBajaInventario> detalleBajaInventarios);

	public BigDecimal sumarCantidadFinal(List<BajaInventarioReporte> list);
}
