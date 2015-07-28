package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Grupo;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.KardexReporte;
import ec.com.redepronik.negosys.invfac.service.GrupoService;
import ec.com.redepronik.negosys.invfac.service.KardexService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.utils.service.ReporteService;

@Controller
@Scope("session")
public class KardexBean {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private GrupoService grupoService;

	@Autowired
	ReporteService reporteService;

	@Autowired
	LocalService localService;

	private List<KardexReporte> listaKardexReporte;
	private KardexReporte kardexSaldo;
	private Producto producto;
	private Producto productoAux;
	private Local local;
	private List<Local> listaLocales;
	private Date fechaInicio;
	private Date fechaFinal;
	private List<Producto> listaProductos;
	private List<Grupo> listaGrupos;
	private String criterioBusqueda;
	private Grupo grupo;

	public KardexBean() {
	}

	public void cargarKardex() {
		listaKardexReporte = kardexService.generarkardex(producto, fechaInicio,
				fechaFinal, local);
		if (!listaKardexReporte.isEmpty()) {
			listaKardexReporte = kardexService
					.invertirKardex(listaKardexReporte);
			kardexSaldo = listaKardexReporte.get(0);
			productoAux = producto;
		}
	}

	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public KardexReporte getKardexSaldo() {
		return kardexSaldo;
	}

	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	public List<KardexReporte> getListaKardexReporte() {
		return listaKardexReporte;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public Local getLocal() {
		return local;
	}

	public Producto getProducto() {
		return producto;
	}

	public void imprimirKardex(ActionEvent actionEvent) {
		if (listaKardexReporte.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO HAY KARDEX A IMPRIMIR");
		else {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("nomPro", productoAux.getNombre());
			parametros.put("gruPro", productoAux.getGrupo().getNombre());
			parametros.put("canMinPro", productoAux.getCantidadMinima());
			reporteService.generarReportePDF(
					kardexService.invertirKardex(listaKardexReporte),
					parametros, "Kardex");
		}
	}

	@PostConstruct
	private void init() {
		listaGrupos = grupoService.obtener();
		listaLocales = localService.obtener(true);
		limpiarObjetos();
	}

	public void limpiarObjetos() {
		listaKardexReporte = new ArrayList<KardexReporte>();
		producto = new Producto();
		local = new Local();
		grupo = new Grupo();
	}

	public void obtenerProducto() {
		producto = productoService.obtenerPorProductoId(producto.getId());
		cargarKardex();
	}

	public void obtenerProductos() {
		setListaProductos(productoService.obtener(criterioBusqueda,
				grupo.getId()));
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public void setKardexSaldo(KardexReporte kardexSaldo) {
		this.kardexSaldo = kardexSaldo;
	}

	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public void setListaKardexReporte(List<KardexReporte> listaKardexReporte) {
		this.listaKardexReporte = listaKardexReporte;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
