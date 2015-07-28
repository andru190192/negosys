package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entityAux.VolumenVentaProductoProveedor;

public interface VolumenVentaProductoProveedorService {

	@Transactional
	public List<VolumenVentaProductoProveedor> obtenerPorFechasPorProveedor(
			Integer proveedorId, Date fechaInicio, Date fechaaFin);
}
