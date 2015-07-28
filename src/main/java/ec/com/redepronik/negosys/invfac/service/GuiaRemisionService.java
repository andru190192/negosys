package ec.com.redepronik.negosys.invfac.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entity.DetalleGuiaRemision;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.GuiaRemision;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

public interface GuiaRemisionService {
	@Transactional
	public void cargarCliente(List<Factura> listaFacturasGuiaRemision,
			Persona cliente);

	@Transactional
	public void cargarDetalleGuiaRemision(Persona cliente,
			List<Factura> listaFacturasGuiaRemision,
			List<Factura> listaAuxFactura,
			List<DetalleGuiaRemision> listDetalleGuiaRemision);

	@Transactional
	public boolean comprobarGuiaRemision(GuiaRemision guiaRemision,
			EmpleadoCargo transportista,
			List<DetalleGuiaRemision> listDetalleGuiaRemision);

	@Transactional
	public void insertar(GuiaRemision guiaRemision, Integer egresoId,
			EmpleadoCargo transportista);

	@Transactional
	public void insertar(GuiaRemision guiaRemision, Persona cliente,
			EmpleadoCargo transportista,
			List<DetalleGuiaRemision> listDetalleGuiaRemision,
			List<Factura> listaFacturasGuiaRemision);

	@Transactional
	public List<GuiaRemision> obtener(String criterioBusquedaCliente,
			String criterioBusquedaCodigo);

	@Transactional
	public GuiaRemision obtenerPorGuiaRemisionId(Long guiaRemisionId);

	@Transactional
	public void quitarDetalleGuiaRemision(List<Factura> listaAuxFactura,
			List<Factura> listaFacturasGuiaRemision,
			List<Factura> listaQuitarFacturasGuiaRemision,
			List<DetalleGuiaRemision> listDetalleGuiaRemision, Persona cliente);

}