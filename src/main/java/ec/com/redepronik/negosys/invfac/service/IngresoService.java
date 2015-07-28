package ec.com.redepronik.negosys.invfac.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.DetalleIngreso;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entityAux.ChequeReporte;

public interface IngresoService {

	@Transactional
	public String actualizar(Ingreso ingreso);

	public BigDecimal calcularEntradas(Ingreso ingreso);

	public BigDecimal calcularTotal(Ingreso ingreso);

	@Transactional
	public void comprobar(Ingreso ingreso);

	@Transactional
	public Long contar();

	@Transactional
	public void eliminar(Ingreso ingreso);

	@Transactional
	public void eliminarDetalle(Ingreso ingreso, DetalleIngreso detalleIngreso);

	@Transactional
	public boolean insertar(Ingreso ingreso, List<Local> listaBodegas);

	@Transactional
	public boolean insertarDetalle(Ingreso ingreso,
			DetalleIngreso detalleIngreso);

	@Transactional
	public List<Ingreso> obtener(Boolean activo);

	@Transactional(readOnly = true)
	public List<Ingreso> obtener(String criterioBusquedaProveedor,
			String criterioBusquedaNumeroFactura,
			Date criterioBusquedaFechaIngreso, Date criterioBusquedaFechaFactura);

	@Transactional
	public List<ChequeReporte> obtenerChequesPorFechas(Date fechaInicio,
			Date fechaFin);

	@Transactional
	public List<Ingreso> obtenerIngresosNoPagados(
			String criterioBusquedaProveedor, String criterioBusquedaDocumento,
			Date criterioBusquedafechaInicio, Date criterioBusquedafechaFin);

	@Transactional
	public Ingreso obtenerPorIngresoId(Long ingresoId);
}