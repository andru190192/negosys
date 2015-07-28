package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.GuiaRemisionDao;
import ec.com.redepronik.negosys.invfac.entity.DetalleFactura;
import ec.com.redepronik.negosys.invfac.entity.DetalleGuiaRemision;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.GuiaRemision;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Service
public class GuiaRemisionServiceImpl implements GuiaRemisionService {

	@Autowired
	private GuiaRemisionDao guiaRemisionDao;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private LocalService localService;

	public void cargarCliente(List<Factura> listaFacturasGuiaRemision,
			Persona cliente) {
		cliente = listaFacturasGuiaRemision.size() == 1 ? listaFacturasGuiaRemision
				.get(0).getCliente().getPersona()
				: new Persona();
	}

	public void cargarDetalleGuiaRemision(Persona cliente,
			List<Factura> listaFacturasGuiaRemision,
			List<Factura> listaAuxFactura,
			List<DetalleGuiaRemision> listDetalleGuiaRemision) {
		if (listaFacturasGuiaRemision.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"DEBE ESCOJER UNA FACTURA", "errorListaFactura", true);
		else {
			List<DetalleFactura> listDetalleFactura = new ArrayList<DetalleFactura>();
			if (listaAuxFactura.size() != listaFacturasGuiaRemision.size())
				listaAuxFactura.addAll(listaFacturasGuiaRemision);
			listaFacturasGuiaRemision.clear();
			listaFacturasGuiaRemision.addAll(listaAuxFactura);
			cargarCliente(listaFacturasGuiaRemision, cliente);
			for (Factura egreso : listaFacturasGuiaRemision)
				listDetalleFactura.addAll(facturaService
						.duplicarDetalleFactura(egreso.getDetalleFactura()));

			List<DetalleFactura> listDetalleFacturaFinal = facturaService
					.sumarCantidades(listDetalleFactura);
			for (DetalleFactura de : listDetalleFacturaFinal) {
				DetalleGuiaRemision dgr = new DetalleGuiaRemision();
				dgr.setDescripcion(de.getProducto().getEan() + " - "
						+ de.getProducto().getNombre());
				dgr.setCantidad(de.getCantidad());
				listDetalleGuiaRemision.add(dgr);
			}
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CORRECTO",
					"errorListaFactura", false);
		}
	}

	public boolean comprobarGuiaRemision(GuiaRemision guiaRemision,
			EmpleadoCargo transportista,
			List<DetalleGuiaRemision> listDetalleGuiaRemision) {
		if (transportista.getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN TRANSPORTISTA", "error", true);
			return true;
		} else if (guiaRemision.getPlaca().compareTo("") == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UNA PLACA",
					"error", true);
			return true;
		} else if (guiaRemision.getMotivoTraslado().getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN MOTIVO DE TRANSLADO", "error", true);
			return true;
		} else if (listDetalleGuiaRemision.isEmpty()) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE FACTURAS PARA LA GUIA DE REMISION", "error", true);
			return true;
		}
		return false;
	}

	public void insertar(GuiaRemision guiaRemision, Integer egresoId,
			EmpleadoCargo transportista) {
		if (guiaRemision.getMotivoTraslado().getId() != 0
				&& transportista.getId() != 0
				&& guiaRemision.getPlaca() != null) {
			Factura egreso = facturaService.obtenerPorFacturaId(egresoId);
			Persona cliente = egreso.getCliente().getPersona();
			guiaRemision.setCliente(cliente.getCliente());
			guiaRemision.setRazonSocialDestinatario(cliente.getCliente()
					.getNombreComercial());
			guiaRemision.setTransportista(empleadoService
					.obtenerPorEmpleadoCargoId(transportista.getId()));
			guiaRemision.setFechaInicio(new Date());
			guiaRemision.setFechaTerminacion(new Date());
			guiaRemision.setPuntoLlegada(cliente.getCiudad().getNombre());
			String ciudad = localService.obtenerPorEstablecimiento(
					egreso.getEstablecimiento()).getCiudad();
			guiaRemision.setPuntoPartida(ciudad);

			guiaRemision.setFacturas(new ArrayList<Factura>());
			guiaRemision.addFactura(egreso);

			// guiaRemision.setMotivoTraslado(motivoTrasladoService
			// .obtenerPorMotivoTrasladoId(guiaRemision
			// .getMotivoTraslado().getId()));

			guiaRemisionDao.insertar(guiaRemision);
		}
	}

	public void insertar(GuiaRemision guiaRemision, Persona cliente,
			EmpleadoCargo transportista,
			List<DetalleGuiaRemision> listDetalleGuiaRemision,
			List<Factura> listaFacturasGuiaRemision) {
		if (!comprobarGuiaRemision(guiaRemision, transportista,
				listDetalleGuiaRemision)) {
			if (cliente.getId() == null) {
				cliente = listaFacturasGuiaRemision.get(0).getCliente()
						.getPersona();
				guiaRemision.setRazonSocialDestinatario("");
			} else {
				guiaRemision.setCliente(cliente.getCliente());
				guiaRemision.setRazonSocialDestinatario(cliente.getCliente()
						.getNombreComercial());
			}

			guiaRemision.setTransportista(empleadoService
					.obtenerPorEmpleadoCargoId(transportista.getId()));
			guiaRemision.setPuntoLlegada(cliente.getCiudad().getNombre());
			String ciudad = localService.obtenerPorEstablecimiento(
					listaFacturasGuiaRemision.get(0).getEstablecimiento())
					.getCiudad();
			guiaRemision.setPuntoPartida(ciudad);

			guiaRemision
					.setDetalleGuiaRemisions(new ArrayList<DetalleGuiaRemision>());
			for (DetalleGuiaRemision dgr : listDetalleGuiaRemision)
				guiaRemision.addDetalleGuiaRemision(dgr);

			guiaRemision.setFacturas(new ArrayList<Factura>());
			for (Factura e : listaFacturasGuiaRemision)
				guiaRemision.addFactura(e);

			// guiaRemision.setMotivoTraslado(motivoTrasladoService
			// .obtenerPorMotivoTrasladoId(guiaRemision
			// .getMotivoTraslado().getId()));

			guiaRemision.setFechaInicio(new Date());
			guiaRemision.setFechaTerminacion(new Date());

			guiaRemisionDao.insertar(guiaRemision);

			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INSERTÓ GUIA REMISION: " + guiaRemision.getId());
		}
	}

	public List<GuiaRemision> obtener(String criterioBusquedaCliente,
			String criterioBusquedaCodigo) {
		List<GuiaRemision> lista = null;
		criterioBusquedaCliente = criterioBusquedaCliente.toUpperCase();
		criterioBusquedaCodigo = criterioBusquedaCodigo.toUpperCase();

		if (criterioBusquedaCliente.compareToIgnoreCase("") == 0
				&& criterioBusquedaCodigo.compareToIgnoreCase("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN CRITERIO DE BUSQUEDA");
		else if (criterioBusquedaCliente.length() >= 1
				&& criterioBusquedaCliente.length() <= 4) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE MAS DE 4 CARACTERES PARA LA BÚSQUEDA POR CLIENTES");
		} else if (!criterioBusquedaCodigo.matches("[0-9]*")) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE SOLO NÚMEROS PARA LA BÚSQUEDA POR DOCUMENTOS");
		} else if (criterioBusquedaCodigo.compareToIgnoreCase("") != 0
				&& criterioBusquedaCodigo.charAt(0) == '0') {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO SE PERMITE CEROS AL INICIO PARA LA BÚSQUEDA POR DOCUMENTOS");
		} else {
			if (criterioBusquedaCliente.compareTo("") != 0
					&& criterioBusquedaCodigo.compareTo("") != 0)
				lista = guiaRemisionDao
						.obtenerPorHql(
								"select distinct gr from Guiaremision gr "
										+ "inner join fetch gr.detalleguiaremisions "
										+ "inner join gr.cliente c inner join c.persona p "
										+ "where ((p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "and gr.codigodocumento like ?2) "
										+ "order by gr.fechainicio asc",
								new Object[] {
										"%" + criterioBusquedaCliente + "%",
										"%" + criterioBusquedaCodigo + "%" });
			else if (criterioBusquedaCliente.compareTo("") != 0)
				lista = guiaRemisionDao
						.obtenerPorHql(
								"select distinct gr from Guiaremision gr "
										+ "inner join fetch gr.detalleguiaremisions "
										+ "inner join gr.cliente c inner join c.persona p "
										+ "where ((p.cedula like ?1 or p.apellido like ?1 or p.nombre like ?1) "
										+ "order by gr.fechainicio asc",
								new Object[] { "%" + criterioBusquedaCliente
										+ "%" });
			else if (criterioBusquedaCodigo.compareTo("") != 0)
				lista = guiaRemisionDao
						.obtenerPorHql(
								"select distinct gr from Guiaremision gr "
										+ "inner join fetch gr.detalleguiaremisions "
										+ "inner join gr.cliente c inner join c.persona p "
										+ "where gr.codigodocumento like ?1 "
										+ "order by gr.fechainicio asc",
								new Object[] { "%" + criterioBusquedaCodigo
										+ "%" });
			if (lista.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRARON COINCIDENCIAS");
		}
		return lista;
	}

	public GuiaRemision obtenerPorGuiaRemisionId(Long guiaRemisionId) {
		return (GuiaRemision) guiaRemisionDao.obtenerPorHql(
				"select distinct gr from Guiaremision gr "
						+ "inner join fetch gr.detalleguiaremisions "
						+ "where gr.guiaremisionid=?1",
				new Object[] { guiaRemisionId }).get(0);
	}

	public void quitarDetalleGuiaRemision(List<Factura> listaAuxFactura,
			List<Factura> listaFacturasGuiaRemision,
			List<Factura> listaQuitarFacturasGuiaRemision,
			List<DetalleGuiaRemision> listDetalleGuiaRemision, Persona cliente) {
		if (listaFacturasGuiaRemision.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"NO HAY FACTURAS PARA LA GUIA DE REMISION",
					"errorQuitarListaFactura", true);
		else {
			if (listaQuitarFacturasGuiaRemision.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"DEBE ESCOJER UNA FACTURA", "errorQuitarListaFactura",
						true);
			else {
				for (Factura e : listaQuitarFacturasGuiaRemision)
					listaFacturasGuiaRemision.remove(e);

				listaAuxFactura.clear();
				listaAuxFactura.addAll(listaFacturasGuiaRemision);

				listDetalleGuiaRemision = new ArrayList<DetalleGuiaRemision>();
				cargarDetalleGuiaRemision(cliente, listaFacturasGuiaRemision,
						listaAuxFactura, listDetalleGuiaRemision);
				presentaMensaje(FacesMessage.SEVERITY_INFO, "CORRECTO",
						"errorQuitarListaFactura", false);
			}
		}
	}

}