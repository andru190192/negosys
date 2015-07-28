package ec.com.redepronik.negosys.seguridad.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.actualizarMatriz;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaImagen;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.leerImagen;
import static ec.com.redepronik.negosys.utils.UtilsDate.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.actualizar;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsWebService.fechaCorte;

import java.io.InputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ParametroService;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.seguridad.entity.Menu;
import ec.com.redepronik.negosys.seguridad.service.MenuService;

@Controller
@Scope("session")
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MenuService menuService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private LocalService localService;

	@Autowired
	private PersonaService personaService;

	private MenuModel menuModel;
	private String nombreUsuario;
	private StreamedContent logo;

	public MenuBean() {
	}

	public void cargarMenu() {
		if (menuModel == null) {
			menuModel = new DefaultMenuModel();

			DefaultSubMenu subMenu1 = null;
			DefaultSubMenu subMenu2 = null;
			DefaultSubMenu subMenu3 = null;
			DefaultMenuItem menuItem = null;

			int padre1 = 0;
			int padre2 = 0;
			int padre3 = 0;

			List<Menu> listMenu = menuService
					.obtenerPorUsuario(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			for (Menu menu2 : listMenu) {
				if (menu2.getVista().compareTo("-") == 0) {
					if (menu2.getNivel() == 1) {
						padre1 = menu2.getId();
						subMenu1 = new DefaultSubMenu(menu2.getNombre());
						if (menu2.getIcono().compareTo("-") != 0)
							subMenu1.setIcon(menu2.getIcono());
						menuModel.addElement(subMenu1);
					} else if (menu2.getNivel() == 2) {
						padre2 = menu2.getId();
						subMenu2 = new DefaultSubMenu(menu2.getNombre());
						if (menu2.getIcono().compareTo("-") != 0)
							subMenu2.setIcon(menu2.getIcono());
						subMenu1.addElement(subMenu2);
					} else if (menu2.getNivel() == 3) {
						padre3 = menu2.getId();
						subMenu3 = new DefaultSubMenu(menu2.getNombre());
						if (menu2.getIcono().compareTo("-") != 0)
							subMenu3.setIcon(menu2.getIcono());
						subMenu2.addElement(subMenu3);
					}
				} else {
					menuItem = new DefaultMenuItem(menu2.getNombre());
					menuItem.setUrl(menu2.getVista());
					if (menu2.getIcono().compareTo("-") != 0)
						menuItem.setIcon(menu2.getIcono());
					menuItem.setAjax(true);
					menuItem.setUpdate("centro");
					if (padre1 == menu2.getPadre())
						subMenu1.addElement(menuItem);
					else if (padre2 == menu2.getPadre())
						subMenu2.addElement(menuItem);
					else if (padre3 == menu2.getPadre())
						subMenu3.addElement(menuItem);
				}
			}
		}
	}

	public StreamedContent getLogo() {
		return logo;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	@PostConstruct
	public void init() {
		actualizar(parametroService.obtener());
		actualizarMatriz(localService.obtenerMatriz());
		Date fechaCorteWS = null;
		Persona p = personaService.obtenerActivoPorCedula(SecurityContextHolder
				.getContext().getAuthentication().getName());

		Boolean estadoConexion = null;
		try {
			if (new Socket(parametro.getWsBucket(), 80).isConnected())
				estadoConexion = true;
		} catch (Exception e) {
			estadoConexion = false;
		}

		if (estadoConexion) {
			fechaCorteWS = fechaCorte(parametro.getRuc());
			if (compareTo(fechaCorteWS, parametro.getFechaCorte()) != 0) {
				parametro.setFechaCorte(fechaCorteWS);
				parametroService.actualizar(parametro);
			}

			setNombreUsuario("BIENVENID@: " + p.getNombre() + " "
					+ p.getApellido());
			if (compareTo(new Date(), parametro.getFechaCorte()) <= 0) {
				cargarMenu();
				InputStream is = leerImagen(getRutaImagen() + "logo.png");
				if (is != null)
					setLogo(new DefaultStreamedContent(is, "imagenes/png"));
				String claveActual = personaService.generarClave(p.getCedula());
				if (claveActual.compareTo(p.getPassword()) == 0)
					redireccionar("../../views/seguridad/cambiarClaveNueva.jsf");
			} else
				redireccionar("../../views/seguridad/errorPago.jsf");
		} else {
			setNombreUsuario("BIENVENID@: " + p.getNombre() + " "
					+ p.getApellido());
			if (compareTo(new Date(), parametro.getFechaCorte()) <= 0) {
				cargarMenu();
				InputStream is = leerImagen(getRutaImagen() + "logo.png");
				if (is != null)
					setLogo(new DefaultStreamedContent(is, "imagenes/png"));
				String claveActual = personaService.generarClave(p.getCedula());
				if (claveActual.compareTo(p.getPassword()) == 0)
					redireccionar("../../views/seguridad/cambiarClaveNueva.jsf");
			} else
				redireccionar("../../views/seguridad/errorPago.jsf");
		}

	}

	public void cargarSistema() {

	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

}