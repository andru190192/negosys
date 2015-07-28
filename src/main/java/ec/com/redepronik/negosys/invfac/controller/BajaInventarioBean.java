package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.enviarVariableVista;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.io.Serializable;
import java.math.BigDecimal;
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

import ec.com.redepronik.negosys.invfac.entity.BajaInventario;
import ec.com.redepronik.negosys.invfac.entity.DetalleBajaInventario;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.MotivoBaja;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.BajaInventarioReporte;
import ec.com.redepronik.negosys.invfac.service.BajaInventarioService;
import ec.com.redepronik.negosys.invfac.service.KardexService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Controller
@Scope("session")
public class BajaInventarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BajaInventarioService bajaInventarioService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private LocalService localService;

	@Autowired
	private EmpleadoService empleadoService;

	private List<Local> listaLocales;
	private BajaInventario bajaInventario;
	private List<BajaInventarioReporte> listaBajaInventarioReporte;
	private BigDecimal total;
	private Local local;
	private boolean bnLocal;
	private String login = "";
	private String pass = "";
	private Producto producto;
	private String criterioBusquedaRapida;
	private List<Producto> listaBusquedaRapida;

	public BajaInventarioBean() {
	}

	public void busquedaRapida() {
		listaBusquedaRapida = productoService.obtenerPorLocal(
				criterioBusquedaRapida, null, local.getId());
	}

	public void cambiarCantidad(BajaInventarioReporte bajaInventarioReporte) {
		int fila = listaBajaInventarioReporte.indexOf(bajaInventarioReporte);
		total = bajaInventarioService.calcularCantidad(bajaInventarioReporte,
				listaBajaInventarioReporte);
		if (fila == listaBajaInventarioReporte.size() - 1)
			listaBajaInventarioReporte.add(new BajaInventarioReporte());
	}

	public void cambiarMotivoBaja(BajaInventarioReporte bajaInventarioReporte) {
		bajaInventarioService.cambiarMotivoBaja(bajaInventarioReporte,
				listaBajaInventarioReporte);
	}

	public boolean comprobarLocal() {
		if (local.getId() == null || local.getId() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"ERROR: DEBE ESCOJER UN LOCAL");
			return false;
		}
		return true;
	}

	// public void cargarProducto(BajaInventarioReporte bajaInventarioReporte) {
	// if (bajaInventarioService.cargarProductoLista(bajaInventarioReporte,
	// listaBajaInventarioReporte, local.getId())) {
	// BajaInventarioReporte fr = listaBajaInventarioReporte
	// .get(listaBajaInventarioReporte.size() - 1);
	// fr.setCantidad(1);
	// fr.setMotivoBaja(MotivoBaja.CA);
	// cambiarCantidad(fr);
	// }
	// }

	public void comprobarTodo() {
		boolean bn = false;
		if (comprobarLocal())
			bn = !bajaInventarioService
					.convertirListaBajaInventarioReporteListaBajaInventarioDetalle(
							listaBajaInventarioReporte, bajaInventario);
		enviarVariableVista("error", bn);
	}

	public BajaInventario getBajaInventario() {
		return bajaInventario;
	}

	public String getCriterioBusquedaRapida() {
		return criterioBusquedaRapida;
	}

	public List<BajaInventarioReporte> getListaBajaInventarioReporte() {
		return listaBajaInventarioReporte;
	}

	public List<Producto> getListaBusquedaRapida() {
		return listaBusquedaRapida;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public MotivoBaja[] getListaMotivoBajas() {
		return MotivoBaja.values();
	}

	public Local getLocal() {
		return local;
	}

	public String getLogin() {
		return login;
	}

	public String getPass() {
		return pass;
	}

	public Producto getProducto() {
		return producto;
	}

	public BigDecimal getTotal() {
		return redondearTotales(total);
	}

	@PostConstruct
	public void init() {
		setListaLocales(localService.obtenerPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 3));
		limpiarBajaInventario();
	}

	public void insertar(ActionEvent actionEvent) {
		boolean bn = empleadoService.autorizacion(login, pass);
		login = "";
		pass = "";
		if (bn) {
			bajaInventario.setEstablecimiento(local.getCodigoEstablecimiento());
			bajaInventarioService
					.convertirListaBajaInventarioReporteListaBajaInventarioDetalle(
							listaBajaInventarioReporte, bajaInventario);
			bajaInventarioService.insertar(bajaInventario);
			limpiarBajaInventario();
		}
	}

	public void insertarProductoRapido(SelectEvent event) {
		Producto producto = (Producto) event.getObject();
		BajaInventarioReporte fr = listaBajaInventarioReporte
				.get(listaBajaInventarioReporte.size() - 1);
		fr.setCodigo(producto.getEan());
		if (bajaInventarioService.cargarProductoLista(fr,
				listaBajaInventarioReporte, local.getId())) {
			listaBajaInventarioReporte.add(new BajaInventarioReporte());
			presentaMensaje(FacesMessage.SEVERITY_INFO, "AGREGÓ PRODUCTO "
					+ producto.getNombre());
		} else {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO HAY EXISTENCIA PARA DAR DE BAJA");
		}
	}

	public boolean isBnLocal() {
		return bnLocal;
	}

	public void limpiarBajaInventario() {
		bajaInventario = new BajaInventario();
		bajaInventario
				.setDetalleBajaInventario(new ArrayList<DetalleBajaInventario>());

		listaBajaInventarioReporte = new ArrayList<BajaInventarioReporte>();
		listaBajaInventarioReporte.add(new BajaInventarioReporte());
		total = newBigDecimal();
		local = new Local();

		setBnLocal(true);
		if (listaLocales.size() == 1) {
			// local.setId(listaLocales.get(0).getId());
			local = listaLocales.get(0);
			setBnLocal(false);
		} else {
			setBnLocal(true);
		}

	}

	public void obtenerLocal() {
		Local local = localService.obtenerPorLocalId(this.local.getId());
		if (local.getLocalBodeguero() != null
				&& !local.getLocalBodeguero().isEmpty()) {
			local = local.getLocalBodeguero().get(0).getLocal();
		}
	}

	public void setBajaInventario(BajaInventario bajaInventario) {
		this.bajaInventario = bajaInventario;
	}

	public void setBnLocal(boolean bnLocal) {
		this.bnLocal = bnLocal;
	}

	public void setCriterioBusquedaRapida(String criterioBusquedaRapida) {
		this.criterioBusquedaRapida = criterioBusquedaRapida;
	}

	public void setListaBajaInventarioReporte(
			List<BajaInventarioReporte> listaBajaInventarioReporte) {
		this.listaBajaInventarioReporte = listaBajaInventarioReporte;
	}

	public void setListaBusquedaRapida(List<Producto> listaBusquedaRapida) {
		this.listaBusquedaRapida = listaBusquedaRapida;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}