package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Cotizacion;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.report.InventarioReportes;
import ec.com.redepronik.negosys.invfac.service.CotizacionService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Controller
@Scope("session")
public class CotizacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CotizacionService cotizacionService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private LocalService localService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private InventarioReportes cotizacionReport;

	private List<Local> listaLocales;
	private String nombreVendedor;

	private Cotizacion cotizacion;

	private List<FacturaReporte> listaFacturaReporte;
	private CantidadFactura cantidadFactura;

	private boolean bn = true;
	private boolean bnLocal;

	private Local local;
	private Producto producto;
	private String criterioBusquedaRapida;
	private List<Producto> listaBusquedaRapida;

	private int cotizacionId;

	private String cliente;

	public CotizacionBean() {

	}

	public void busquedaRapida() {
		listaBusquedaRapida = productoService.obtenerPorLocal(
				criterioBusquedaRapida, null, local.getId());
	}

	public void cambiarCantidad(FacturaReporte facturaReporte) {
		int fila = listaFacturaReporte.indexOf(facturaReporte);
		cantidadFactura = cotizacionService.calcularCantidad(facturaReporte,
				listaFacturaReporte);
		if (fila == listaFacturaReporte.size() - 1)
			listaFacturaReporte.add(new FacturaReporte());
	}

	public void cambiarDescDol(FacturaReporte facturaReporte) {
		cantidadFactura = cotizacionService.calcularDescuentoDolares(
				facturaReporte, listaFacturaReporte);
	}

	public void cambiarDescPor(FacturaReporte facturaReporte) {
		cantidadFactura = cotizacionService.calcularDescuentoPorcentaje(
				facturaReporte, listaFacturaReporte);
	}

	public void cambiarEstado(FacturaReporte facturaReporte) {
		cantidadFactura = cotizacionService.cambiarEstado(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarImporte(FacturaReporte facturaReporte) {
		cantidadFactura = cotizacionService.calcularImporte(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarPrecio(FacturaReporte facturaReporte) {
		cantidadFactura = cotizacionService.calcularPrecio(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarTipoPrecio(FacturaReporte facturaReporte) {
		cantidadFactura = cotizacionService.calcularTipoPrecio(facturaReporte,
				listaFacturaReporte);
	}

	public void cancelarTodo() {
		limpiarCotizacion();
		listaFacturaReporte = new ArrayList<FacturaReporte>();
		listaFacturaReporte.add(new FacturaReporte());
		cantidadFactura = new CantidadFactura();
	}

	public void cargarCliente() {
		cotizacion.setCliente(clienteService.cargarCliente(cliente));
	}

	public void cargarCliente(SelectEvent event) {
		Cliente c = clienteService.obtenerPorPersonaId(
				cotizacion.getCliente().getPersona().getId()).getCliente();
		cotizacion.setCliente(c);
	}

	public void cargarProducto(FacturaReporte facturaReporte) {
		if (cotizacionService.cargarProductoLista(facturaReporte,
				listaFacturaReporte, local.getId())) {
			listaFacturaReporte.get(listaFacturaReporte.size() - 1)
					.setCantidad(1);
			cambiarCantidad(listaFacturaReporte
					.get(listaFacturaReporte.size() - 1));
		}
	}

	public boolean comprobarLocal() {
		if (cotizacion.getEstablecimiento() == null
				|| cotizacion.getEstablecimiento().compareToIgnoreCase("") == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: DEBE ESCOJER UN LOCAL");
			return false;
		}
		return true;
	}

	public boolean comprobarTodo() {
		if (cotizacion.getCliente().getPersona() == null) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UN CLIENTE");
			return true;
		} else if (cotizacion.getEstablecimiento() == null
				|| cotizacion.getEstablecimiento().compareToIgnoreCase("") == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: ESCOJA UN LOCAL");
			return true;
		} else if (cotizacion.getVendedor().getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: INGRESE UN VENDEDOR");
			return true;
		} else if (cotizacionService
				.convertirListaFacturaReporteListaCotizacionDetalle(
						listaFacturaReporte, cotizacion))
			return true;

		return false;
	}

	public CantidadFactura getCantidadFactura() {
		return cotizacionService.redondearCantidadFactura(cantidadFactura);
	}

	public String getCliente() {
		return cliente;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public String getCriterioBusquedaRapida() {
		return criterioBusquedaRapida;
	}

	public List<Producto> getListaBusquedaRapida() {
		return listaBusquedaRapida;
	}

	public EstadoProductoVenta[] getListaEstadoProductoVenta() {
		return EstadoProductoVenta.values();
	}

	public List<FacturaReporte> getListaFacturaReporte() {
		return listaFacturaReporte;
	}

	public List<Local> getListaLocales() {

		return listaLocales;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public Producto getProducto() {
		return producto;
	}

	public void imprimirCotizacion() {
		// if (cotizacionReport.reporteCotizacion(cotizacionId))
		// cancelarTodo();
	}

	@PostConstruct
	public void init() {
		listaLocales = localService.obtenerPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 4);
		cancelarTodo();
		bn = true;
	}

	public void insertar(ActionEvent actionEvent) {
		if (!comprobarTodo()) {
			bn = false;

			cotizacionId = cotizacionService.insertar(cotizacion).getId();
			if (cotizacionId != 0) {
				bn = false;
				cancelarTodo();
			}
		}
	}

	public void insertarProductoRapido(SelectEvent event) {
		Producto producto = (Producto) event.getObject();
		listaFacturaReporte.get(listaFacturaReporte.size() - 1).setCodigo(
				producto.getEan());
		cotizacionService.cargarProductoLista(
				listaFacturaReporte.get(listaFacturaReporte.size() - 1),
				listaFacturaReporte, local.getId());
		listaFacturaReporte.add(new FacturaReporte());

		presentaMensaje(FacesMessage.SEVERITY_INFO, "AGREGÓ PRODUCTO "
				+ producto.getNombre());
	}

	public boolean isBn() {
		return bn;
	}

	public boolean isBnLocal() {
		return bnLocal;
	}

	private void limpiarCotizacion() {
		cotizacion = new Cotizacion();
		cotizacion.setCliente(new Cliente());
		setCliente("");

		String cedula = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		Persona persona = empleadoService.obtenerPorCedula(cedula);
		nombreVendedor = persona.getApellido().concat(" ")
				.concat(persona.getNombre());
		cotizacion.setVendedor(empleadoService
				.obtenerEmpleadoCargoPorCedulaAndCargo(cedula, 5));

		if (listaLocales.size() == 1) {
			cotizacion.setEstablecimiento(listaLocales.get(0)
					.getCodigoEstablecimiento());
			local = localService.obtenerPorEstablecimiento(cotizacion
					.getEstablecimiento());
			setBnLocal(false);
		} else {
			setBnLocal(true);
		}

	}

	public void nuevoCotizacion() {
		cancelarTodo();
		bn = true;
		cotizacionId = 0;
	}

	public List<String> obtenerClientePorBusqueda(String criterioClienteBusqueda) {
		List<String> lista = clienteService
				.obtenerListaClientesAutoComplete(criterioClienteBusqueda);
		if (lista.size() == 1) {
			cliente = (lista.get(0));
			cargarCliente();
		}
		return lista;
	}

	public List<String> obtenerProductosPorBusqueda(
			String criterioProductoBusqueda) {
		if (comprobarLocal())
			return productoService.obtenerListaStringPorLocal(
					criterioProductoBusqueda, local.getId());
		return new ArrayList<String>();
	}

	public void setBn(boolean bn) {
		this.bn = bn;
	}

	public void setBnLocal(boolean bnLocal) {
		this.bnLocal = bnLocal;
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public void setCriterioBusquedaRapida(String criterioBusquedaRapida) {
		this.criterioBusquedaRapida = criterioBusquedaRapida;
	}

	public void setListaBusquedaRapida(List<Producto> listaBusquedaRapida) {
		this.listaBusquedaRapida = listaBusquedaRapida;
	}

	public void setListaFacturaReporte(List<FacturaReporte> listaFacturaReporte) {
		this.listaFacturaReporte = listaFacturaReporte;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}