package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;

public interface FacturaInternaService {

	public void actualizarCuadre(List<FacturaReporte> list,
			List<DetalleFactura> listDetalleFactura);

	public FacturaReporte asignar(DetalleFactura de);

	public CantidadFactura calcularCantidad(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura calcularCantidadFactura(
			CantidadFactura cantidadFactura, FacturaReporte fr);

	public CantidadFactura calcularDescuentoDolares(FacturaReporte fr,
			List<FacturaReporte> list);

	public CantidadFactura calcularImporte(FacturaReporte fr,
			List<FacturaReporte> list);

	public CantidadFactura calcularPrecio(FacturaReporte facturaReporte,
			List<FacturaReporte> list);

	public CantidadFactura redondearCantidadFactura(CantidadFactura cf);

}