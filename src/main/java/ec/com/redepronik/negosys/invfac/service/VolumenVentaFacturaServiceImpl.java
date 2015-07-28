package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.VolumenVentaFacturaDao;
import ec.com.redepronik.negosys.invfac.entityAux.VolumenVentaFactura;

@Service
public class VolumenVentaFacturaServiceImpl implements
		VolumenVentaFacturaService {

	@Autowired
	private VolumenVentaFacturaDao volumenVentaFacturaDao;

	public List<VolumenVentaFactura> obtenerVolumenventaFacturaPorConsumidorFinal(
			int consumidorFinal, Date fechaInicio, Date fechaaFin) {
		return volumenVentaFacturaDao
				.obtenerPorHql(
						"select new VolumenVentaFactura("
								+ "f.id, "
								+ "f.fechaInicio, "
								+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
								+ "p.cedula, "
								+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
								+ "from Factura f1 "
								+ "inner join f1.detalleFactura df "
								+ "inner join df.producto pr "
								+ "inner join pr.productosTarifas pt "
								+ "inner join pt.tarifa t "
								+ "where f1.id=f.id "
								+ "and t.id=1 "
								+ "and df.estadoProductoVenta='NR'), "
								+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
								+ "from Factura f1 "
								+ "inner join f1.detalleFactura df "
								+ "inner join df.producto pr "
								+ "inner join pr.productosTarifas pt "
								+ "inner join pt.tarifa t "
								+ "where f1.id=f.id "
								+ "and t.id=2 "
								+ "and df.estadoProductoVenta='NR'), "
								+ "(select coalesce(round(sum(df.precioVentaCuadre*?1*df.cantidad),2), cast(0.0 as big_decimal)) "
								+ "from Factura f1 "
								+ "inner join f1.detalleFactura df "
								+ "inner join df.producto pr "
								+ "inner join pr.productosTarifas pt "
								+ "inner join pt.tarifa t "
								+ "where f1.id=f.id "
								+ "and t.id=2 "
								+ "and df.estadoProductoVenta='NR')) "
								+ "from Factura f "
								+ "inner join f.clienteFactura cf "
								+ "inner join cf.persona p "
								+ "where f.activo=true and f.fechaInicio>=?2 "
								+ "and f.fechaInicio<=?3 "
								+ ((consumidorFinal == 0) ? "and p.cedula='9999999999999'"
										: (consumidorFinal == 1) ? "and p.cedula!='9999999999999'"
												: " ")
								+ " order by f.fechaInicio", new Object[] {
								new BigDecimal("0.12"), fechaInicio,
								dateCompleto(fechaaFin) });
	}

	public List<VolumenVentaFactura> obtenerPorFechasAndVendedorAndEstadoDocumento(
			Integer vendedorId, Integer estadoDocumento, Date fechaInicio,
			Date fechaFin) {
		List<VolumenVentaFactura> list = null;

		if (fechaInicio != null && fechaFin != null) {
			if (vendedorId == 0 && estadoDocumento == 0)
				list = volumenVentaFacturaDao
						.obtenerPorHql(
								"select new VolumenVentaFactura("
										+ "f.id, "
										+ "f.fechaInicio, "
										+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
										+ "p.cedula, "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=1 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=2 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*?1*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id " + "and t.id=2 "
										+ "and df.estadoProductoVenta='NR')) "
										+ "from Factura f "
										+ "inner join f.clienteFactura cf "
										+ "inner join cf.persona p "
										+ "inner join f.vendedor v "
										+ "where f.activo=true "
										+ "and f.fechaInicio>=?2 "
										+ "and f.fechaInicio<=?3 "
										+ "order by f.fechaInicio",
								new Object[] { new BigDecimal("0.12"),
										fechaInicio, dateCompleto(fechaFin) });
			else if (vendedorId == 0 && estadoDocumento == 1)
				list = volumenVentaFacturaDao
						.obtenerPorHql(
								"select new VolumenVentaFactura("
										+ "f.id, "
										+ "f.fechaInicio, "
										+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
										+ "p.cedula, "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=1 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=2 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*?1*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id " + "and t.id=2 "
										+ "and df.estadoProductoVenta='NR')) "
										+ "from Factura f "
										+ "inner join f.clienteFactura cf "
										+ "inner join cf.persona p "
										+ "inner join f.vendedor v "
										+ "where f.fechaCierre is not null "
										+ "and f.activo=true "
										+ "and f.fechaCierre>=?2 "
										+ "and f.fechaCierre<=?3 "
										+ "order by f.fechaInicio",
								new Object[] { new BigDecimal("0.12"),
										fechaInicio, dateCompleto(fechaFin) });
			else if (vendedorId == 0 && estadoDocumento == 2)
				list = volumenVentaFacturaDao
						.obtenerPorHql(
								"select new VolumenVentaFactura("
										+ "f.id, "
										+ "f.fechaInicio, "
										+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
										+ "p.cedula, "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=1 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=2 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*?1*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id " + "and t.id=2 "
										+ "and df.estadoProductoVenta='NR')) "
										+ "from Factura f "
										+ "inner join f.clienteFactura cf "
										+ "inner join cf.persona p "
										+ "inner join f.vendedor v "
										+ "where f.fechaCierre is null "
										+ "and f.activo=true "
										+ "and f.fechaInicio>=?2 "
										+ "and f.fechaInicio<=?3 "
										+ "order by f.fechaInicio",
								new Object[] { new BigDecimal("0.12"),
										fechaInicio, dateCompleto(fechaFin) });
			else if (vendedorId != 0 && estadoDocumento == 0)
				list = volumenVentaFacturaDao
						.obtenerPorHql(
								"select new VolumenVentaFactura("
										+ "f.id, "
										+ "f.fechaInicio, "
										+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
										+ "p.cedula, "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=1 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=2 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*cast(0.12 as big_decimal)*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id " + "and t.id=2 "
										+ "and df.estadoProductoVenta='NR')) "
										+ "from Factura f "
										+ "inner join f.clienteFactura cf "
										+ "inner join cf.persona p "
										+ "inner join f.vendedor v "
										+ "where v.id=?1 "
										+ "and f.activo=true "
										+ "and f.fechaInicio>=?2 "
										+ "and f.fechaInicio<=?3 "
										+ "order by f.fechaInicio",
								new Object[] { vendedorId, fechaInicio,
										dateCompleto(fechaFin) });
			else if (vendedorId != 0 && estadoDocumento == 1)
				list = volumenVentaFacturaDao
						.obtenerPorHql(
								"select new VolumenVentaFactura("
										+ "f.id, "
										+ "f.fechaInicio, "
										+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
										+ "p.cedula, "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=1 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=2 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*cast(0.12 as big_decimal)*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id " + "and t.id=2 "
										+ "and df.estadoProductoVenta='NR')) "
										+ "from Factura f "
										+ "inner join f.clienteFactura cf "
										+ "inner join cf.persona p "
										+ "inner join f.vendedor v "
										+ "where v.id=?1 "
										+ "and f.fechaCierre is not null "
										+ "and f.activo=true "
										+ "and f.fechaCierre>=?2 "
										+ "and f.fechaCierre<=?3 "
										+ "order by f.fechaInicio",
								new Object[] { vendedorId, fechaInicio,
										dateCompleto(fechaFin) });
			else if (vendedorId != 0 && estadoDocumento == 2)
				list = volumenVentaFacturaDao
						.obtenerPorHql(
								"select new VolumenVentaFactura("
										+ "f.id, "
										+ "f.fechaInicio, "
										+ "f.establecimiento||'-'||f.puntoEmision||'-'||f.secuencia, "
										+ "p.cedula, "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=1 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id "
										+ "and t.id=2 "
										+ "and df.estadoProductoVenta='NR'), "
										+ "(select coalesce(round(sum(df.precioVentaCuadre*cast(0.12 as big_decimal)*df.cantidad),2), cast(0.0 as big_decimal)) "
										+ "from Factura f1 "
										+ "inner join f1.detalleFactura df "
										+ "inner join df.producto pr "
										+ "inner join pr.productosTarifas pt "
										+ "inner join pt.tarifa t "
										+ "where f1.id=f.id " + "and t.id=2 "
										+ "and df.estadoProductoVenta='NR')) "
										+ "from Factura f "
										+ "inner join f.clienteFactura cf "
										+ "inner join cf.persona p "
										+ "inner join f.vendedor v "
										+ "where v.id=?1 "
										+ "and f.fechaCierre is null "
										+ "and f.activo=true "
										+ "and f.fechaInicio>=?2 "
										+ "and f.fechaInicio<=?3 "
										+ "order by f.fechaInicio",
								new Object[] { vendedorId, fechaInicio,
										dateCompleto(fechaFin) });
		}
		return list;
	}
}