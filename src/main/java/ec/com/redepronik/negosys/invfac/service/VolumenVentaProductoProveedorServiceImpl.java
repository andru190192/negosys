package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.VolumenVentaProductoProveedorDao;
import ec.com.redepronik.negosys.invfac.entityAux.VolumenVentaProductoProveedor;

@Service
public class VolumenVentaProductoProveedorServiceImpl implements
		VolumenVentaProductoProveedorService {

	@Autowired
	private VolumenVentaProductoProveedorDao volumenVentaProductoProveedorDao;

	public List<VolumenVentaProductoProveedor> obtenerPorFechasPorProveedor(
			Integer proveedorId, Date fechaInicio, Date fechaaFin) {
		return volumenVentaProductoProveedorDao
				.obtenerPorHql(
						"select distinct "
								+ "new VolumenVentaProductoProveedor("
								+ "p.id, "
								+ "p.nombre, "
								+ "(select sum(df.cantidadCuadre) "
								+ "from Producto prod "
								+ "inner join prod.detalleFactura df "
								+ "inner join df.factura f "
								+ "where f.activo=true "
								+ "and f.fechaInicio>=?2 "
								+ "and f.fechaInicio<=?3 "
								+ "and prod.id=p.id), "
								+ "(select round(sum((df.cantidadCuadre*df.precioVentaCuadre)-(df.cantidadCuadre*df.precioCosto)),2) "
								+ "from Producto prod "
								+ "inner join prod.detalleFactura df "
								+ "inner join df.factura f "
								+ "where f.activo=true "
								+ "and f.fechaInicio>=?2 "
								+ "and f.fechaInicio<=?3 "
								+ "and prod.id=p.id)) from Producto p "
								+ "inner join p.detalleIngresos di "
								+ "inner join di.ingreso i "
								+ "inner join i.proveedor pr "
								+ "where pr.id=?1 order by p.nombre",
						new Object[] { proveedorId, fechaInicio,
								dateCompleto(fechaaFin) });
	}
}