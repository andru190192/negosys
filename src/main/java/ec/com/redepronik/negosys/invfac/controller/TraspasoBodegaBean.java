package ec.com.redepronik.negosys.invfac.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.DetalleTraspaso;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.Traspaso;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.invfac.service.TraspasoService;

@Controller
@Scope("session")
public class TraspasoBodegaBean {

	@Autowired
	private TraspasoService traspasoService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private LocalService localService;

	private List<Local> listaLocalsOrigen;
	private List<Local> listaLocalsDestino;
	private List<Traspaso> listaTraspasos;
	private Traspaso traspaso;
	private Local localOrigen;
	private Local localDestino;
	private DetalleTraspaso detalleTraspaso;

	private List<Producto> listProductos;
	private Producto producto;
	private String nombreProducto;

	public TraspasoBodegaBean() {
		listProductos = new ArrayList<Producto>();
		producto = new Producto();

		traspaso = new Traspaso();
		localOrigen = new Local();
		localDestino = new Local();
		detalleTraspaso = new DetalleTraspaso();
	}

	public void eliminarDetalleTraspaso(ActionEvent actionEvent) {
		traspaso.removeDetalleTraspaso(detalleTraspaso);
		presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO, "ELIMINÓ",
				"EL PRODUCTO FUE ELIMINADO DE LA LISTA"));
	}

	public DetalleTraspaso getDetalleTraspaso() {
		return detalleTraspaso;
	}

	public List<Local> getListaLocalsDestino() {
		return listaLocalsDestino;
	}

	public List<Local> getListaLocalsOrigen() {
		return listaLocalsOrigen;
	}

	public List<Traspaso> getListaTraspasos() {
		if (listaTraspasos == null) {
			listaTraspasos = new ArrayList<Traspaso>();
			listaTraspasos = traspasoService.obtener();
		}
		return listaTraspasos;
	}

	public List<Producto> getListProductos() {
		return listProductos;
	}

	public Local getLocalDestino() {
		return localDestino;
	}

	public Local getLocalOrigen() {
		return localOrigen;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public Producto getProducto() {
		return producto;
	}

	public Traspaso getTraspaso() {
		return traspaso;
	}

	@PostConstruct
	public void init() {
		listaLocalsOrigen = localService.obtenerPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 3);
		listaLocalsDestino = localService.obtener(true);
	}

	public void insertarDetalle(ActionEvent actionEvent) {
		if (producto != null) {
			int b = 0;
			for (DetalleTraspaso dt : traspaso.getDetalleTraspasos()) {
				if (producto.getId() == dt.getProducto().getId()) {
					b = 1;
					break;
				}
			}
			if (b == 0) {
				DetalleTraspaso detalleTraspaso = new DetalleTraspaso();
				detalleTraspaso.setProducto(producto);
				detalleTraspaso.setCantidad(0);
				traspaso.addDetalleTraspaso(detalleTraspaso);
			} else {
				presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
						"MENSAJE DEL SISTEMA",
						"EL PRODUCTO YA EXISTE EN LA LISTA BUSQUE Y AUMENTE LA CANTIDAD"));
			}
			RequestContext.getCurrentInstance()
					.addCallbackParam("cerrar", true);
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("cerrar",
					false);
			presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"MENSAJE DEL SISTEMA",
					"EL PRODUCTO NO EXISTE EN LA BODEGA ORIGEN"));
		}
	}

	public void insertarTraspaso(ActionEvent actionEvent) {
		if (localOrigen.getId() == localDestino.getId()) {
			presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"MENSAJE DEL SISTEMA",
					"LA BODEGA ORIGEN NO PUEDE SER IGUAL A LA BODEGA DESTINO"));
			RequestContext.getCurrentInstance().addCallbackParam("cerrar",
					false);
		} else if (traspaso.getDetalleTraspasos().isEmpty()) {
			presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"MENSAJE DEL SISTEMA", "NO EXISTEN PRODUCTOS QUE TRASLADAR"));
			RequestContext.getCurrentInstance().addCallbackParam("cerrar",
					false);
		} else {
			int b = 1;
			for (DetalleTraspaso dt : traspaso.getDetalleTraspasos())
				if (dt.getCantidad() == 0) {
					b = 0;
					break;
				}

			if (b == 1) {
				traspaso.setLocal1(localService.obtenerPorLocalId(localOrigen
						.getId()));
				traspaso.setLocal2(localService.obtenerPorLocalId(localDestino
						.getId()));
				traspaso.setFecha(new Timestamp(new Date().getTime()));
				String mensaje = traspasoService.insertar(traspaso);
				if (mensaje.compareTo("SAVE") == 0) {
					presentaMensaje(new FacesMessage(
							FacesMessage.SEVERITY_INFO, "MENSAJE DEL SISTEMA",
							"EL TRASPASO SE REALIZO EXITOSAMENTE"));
				} else if (mensaje.compareTo("STOCK") == 0) {
					presentaMensaje(new FacesMessage(
							FacesMessage.SEVERITY_INFO, "MENSAJE DEL SISTEMA",
							"TRASPASO INCOMPLETO - ALGUNOS PRODUCTOS CARECEN DE STOCK SUFICIENTE"));
				} else if (mensaje.compareTo("NULL") == 0) {
					presentaMensaje(new FacesMessage(
							FacesMessage.SEVERITY_INFO, "MENSAJE DEL SISTEMA",
							"TRASPASO INCORRECTO"));
				}
				RequestContext.getCurrentInstance().addCallbackParam("cerrar",
						true);
				listaTraspasos = traspasoService.obtener();
			} else {
				presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
						"MENSAJE DEL SISTEMA",
						"EXISTEN PRODUCTOS SIN CANTIDAD A TRASLADAR"));
			}
		}
	}

	public void limpiarObjetos() {
		traspaso = new Traspaso();
		traspaso.setDetalleTraspasos(new ArrayList<DetalleTraspaso>());
		localOrigen = new Local();
		localDestino = new Local();
		detalleTraspaso = new DetalleTraspaso();
	}

	public void obtenerProducto() {
		producto = productoService.obtenerPorProductoId(producto.getId());
	}

	public void obtenerProductos() {
		if (!nombreProducto.equals(null))
			listProductos = productoService.obtenerPorEanAndLocal(
					nombreProducto.toUpperCase(), localOrigen.getId());
	}

	private void presentaMensaje(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void setDetalleTraspaso(DetalleTraspaso detalleTraspaso) {
		this.detalleTraspaso = detalleTraspaso;
	}

	public void setListaLocalsDestino(List<Local> listaLocalsDestino) {
		this.listaLocalsDestino = listaLocalsDestino;
	}

	public void setListaLocalsOrigen(List<Local> listaLocalsOrigen) {
		this.listaLocalsOrigen = listaLocalsOrigen;
	}

	public void setListaTraspasos(List<Traspaso> listaTraspasos) {
		this.listaTraspasos = listaTraspasos;
	}

	public void setListProductos(List<Producto> listProductos) {
		this.listProductos = listProductos;
	}

	public void setLocalDestino(Local bodegaDestino) {
		this.localDestino = bodegaDestino;
	}

	public void setLocalOrigen(Local bodegaOrigen) {
		this.localOrigen = bodegaOrigen;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setTraspaso(Traspaso traspaso) {
		this.traspaso = traspaso;
	}
}
