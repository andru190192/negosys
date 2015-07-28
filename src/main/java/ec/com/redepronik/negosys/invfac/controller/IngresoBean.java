package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleIngreso;
import ec.com.redepronik.negosys.invfac.entity.Ingreso;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.service.IngresoService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;

@Controller
@Scope("session")
public class IngresoBean {

	@Autowired
	private IngresoService ingresoService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private LocalService localService;

	@Autowired
	private EmpleadoService empleadoService;

	private Ingreso ingreso;
	private DetalleIngreso detalleIngreso;
	private String criterioProveedorBusqueda;
	private List<Persona> listaProveedorBusqueda;
	private String criterioProductoBusqueda;
	private List<Producto> listaProductoBusqueda;
	private Producto producto;
	private String criterioBusquedaRapida;
	private List<Producto> listaBusquedaRapida;
	private String proveedor;
	private List<Local> listaLocal;

	public IngresoBean() {
	}

	public void busquedaRapida() {
		listaBusquedaRapida = productoService
				.obtenerParaBusqueda(criterioBusquedaRapida);
		if (listaBusquedaRapida.size() == 1) {
			limpiarDetalleingreso();
			Producto producto = listaBusquedaRapida.get(0);
			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
					.setId(producto.getId());
			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
					.setEan(producto.getEan());
			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
					.setNombreImprimir(producto.getNombre());

			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1)
					.setPrecio(producto.getPrecio());

			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
					.setPrecio(producto.getPrecio());
			criterioBusquedaRapida = "";
			listaBusquedaRapida = new ArrayList<Producto>();
//			RequestContext.getCurrentInstance().update("formCabecera:tablaDetalleIngresos");
//			RequestContext.getCurrentInstance().update("formCabecera:txtCriterioBusqueda");
//			RequestContext.getCurrentInstance().update("mensaje");
			RequestContext.getCurrentInstance().execute(
					"document.getElementById('formCabecera:txtCriterioBusqueda').focus()");
			presentaMensaje(FacesMessage.SEVERITY_INFO, "AGREGÓ PRODUCTO "
					+ producto.getNombre());
		}
	}

	// public void cargarProducto(DetalleIngreso detalleIngreso) {
	// detalleIngreso.setProducto(productoService.obtenerPorEan(detalleIngreso
	// .getProducto().getEan().split(" - ")[0]));
	// detalleIngreso.setPrecio(detalleIngreso.getProducto().getPrecio());
	// }

	// public void cargarProveedor() {
	// ingreso.setProveedor(proveedorService.obtenerPorPersonaId(
	// ingreso.getProveedor().getPersona().getPersonaid())
	// .getProveedor());
	// }

	public void cargarProveedor() {
		ingreso.setProveedor(proveedorService.cargarProveedor(proveedor));
		proveedor = proveedor.split(" - ")[2];
	}

	// public void comprobarCantidad(DetalleIngreso detalleIngreso) {
	// if (detalleIngreso.getCantidad() < 0) {
	// presentaMensaje(FacesMessage.SEVERITY_ERROR,
	// "INGRESE UNA CANTIDAD MAYOR O IGUAL A CERO PARA CONTINUAR");
	// detalleIngreso.setCantidad(0);
	// } else if (ingreso.getDetalleIngresos()
	// .get(ingreso.getDetalleIngresos().size() - 1).getProducto()
	// .getId() != null)
	// limpiarDetalleingreso();
	// }

	public void comprobarPrecioNuevo(DetalleIngreso detalleIngreso) {
		if (detalleIngreso.getPrecio().compareTo(new BigDecimal("0.00")) <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN PRECIO MAYOR A CERO");
	}

	public void eliminarDetalle(ActionEvent actionEvent) {
		ingresoService.eliminarDetalle(ingreso, detalleIngreso);
	}

	public void eliminarDetalle(DetalleIngreso detalleIngreso) {
		ingresoService.eliminarDetalle(ingreso, detalleIngreso);
	}

	public String getCriterioBusquedaRapida() {
		return criterioBusquedaRapida;
	}

	public String getCriterioProductoBusqueda() {
		return criterioProductoBusqueda;
	}

	public String getCriterioProveedorBusqueda() {
		return criterioProveedorBusqueda;
	}

	public DetalleIngreso getDetalleIngreso() {
		return detalleIngreso;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public List<Producto> getListaBusquedaRapida() {
		return listaBusquedaRapida;
	}

	public List<Local> getListaLocal() {
		return listaLocal;
	}

	public List<Producto> getListaProductoBusqueda() {
		return listaProductoBusqueda;
	}

	public List<Persona> getListaProveedorBusqueda() {
		return listaProveedorBusqueda;
	}

	public Producto getProducto() {
		return producto;
	}

	public String getProveedor() {
		return proveedor;
	}

	@PostConstruct
	public void init() {
		listaLocal = localService.obtenerPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 3);
		// hace new a el objeto ingreso y sus dependencias
		limpiarIngreso();
		listaBusquedaRapida = new ArrayList<Producto>();
	}

	public void insertar(ActionEvent actionEvent) {
		ingreso.setBodeguero(empleadoService
				.obtenerEmpleadoCargoPorCedulaAndCargo(SecurityContextHolder
						.getContext().getAuthentication().getName(), 3));
		if (ingresoService.insertar(ingreso, listaLocal))
			limpiarIngreso();

	}

	public void insertarProductoRapido(SelectEvent event) {
		limpiarDetalleingreso();
		Producto producto = (Producto) event.getObject();
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
				.setId(producto.getId());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
				.setEan(producto.getEan());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
				.setNombreImprimir(producto.getNombre());

		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setPrecio(producto.getPrecio());

		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1).getProducto()
				.setPrecio(producto.getPrecio());

		presentaMensaje(FacesMessage.SEVERITY_INFO, "AGREGÓ PRODUCTO "
				+ producto.getNombre());
	}

	public void limpiarDetalleingreso() {
		ingreso.getDetalleIngresos().add(new DetalleIngreso());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setIngreso(ingreso);
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setProducto(new Producto());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1)
				.setLocal(new Local());
		ingreso.getDetalleIngresos()
				.get(ingreso.getDetalleIngresos().size() - 1).setCantidad(0);

		if (!listaLocal.isEmpty())
			ingreso.getDetalleIngresos()
					.get(ingreso.getDetalleIngresos().size() - 1)
					.setLocal(listaLocal.get(0));
	}

	public void limpiarIngreso() {
		ingreso = new Ingreso();
		ingreso.setTotal(new BigDecimal("0.00"));
		ingreso.setProveedor(new Proveedor());
		ingreso.getProveedor().setPersona(new Persona());
		ingreso.setDetalleIngresos(new ArrayList<DetalleIngreso>());
		ingreso.setPagado(true);
		proveedor = "";
	}

	public void limpiarObjetosBusquedaProveedor() {
		criterioProveedorBusqueda = new String();
		ingreso.getProveedor().setPersona(new Persona());
	}

	// public void obtenerProductosPorBusqueda() {
	// listaProductoBusqueda = productoService
	// .obtenerParaBusqueda(criterioProductoBusqueda.toUpperCase());
	// }

	public List<String> obtenerProductosPorBusqueda(
			String criterioProductoBusqueda) {
		return productoService.obtenerListaString(criterioProductoBusqueda);
	}

	public void obtenerProveedoresPorBusqueda() {
		listaProveedorBusqueda = proveedorService
				.obtener(criterioProveedorBusqueda.toUpperCase());
	}

	public List<String> obtenerProveedorPorBusqueda(
			String criterioProveedorBusqueda) {
		List<String> lista = proveedorService
				.obtenerListaProveedoresAutoComplete(criterioProveedorBusqueda);
		if (lista.size() == 1) {
			proveedor = lista.get(0);
			cargarProveedor();
		}
		return lista;
	}

	public void setCriterioBusquedaRapida(String criterioBusquedaRapida) {
		this.criterioBusquedaRapida = criterioBusquedaRapida;
	}

	public void setCriterioProductoBusqueda(String criterioProductoBusqueda) {
		this.criterioProductoBusqueda = criterioProductoBusqueda;
	}

	public void setCriterioProveedorBusqueda(String criterioProveedorBusqueda) {
		this.criterioProveedorBusqueda = criterioProveedorBusqueda;
	}

	public void setDetalleIngreso(DetalleIngreso detalleIngreso) {
		this.detalleIngreso = detalleIngreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public void setListaBusquedaRapida(List<Producto> listaBusquedaRapida) {
		this.listaBusquedaRapida = listaBusquedaRapida;
	}

	public void setListaLocal(List<Local> listaLocal) {
		this.listaLocal = listaLocal;
	}

	public void setListaProductoBusqueda(List<Producto> listaProductoBusqueda) {
		this.listaProductoBusqueda = listaProductoBusqueda;
	}

	public void setListaProveedorBusqueda(List<Persona> listaProveedorBusqueda) {
		this.listaProveedorBusqueda = listaProveedorBusqueda;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
}