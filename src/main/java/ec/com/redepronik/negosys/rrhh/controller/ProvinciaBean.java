package ec.com.redepronik.negosys.rrhh.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Provincia;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;

@Controller
@Scope("session")
public class ProvinciaBean {

	@Autowired
	private CiudadService ciudadService;

	private List<Ciudad> listaCiudades;

	private Provincia provincia;
	private Ciudad ciudad;

	public ProvinciaBean() {
		limpiarObjetos();
	}

	public void cargarCiudades() {
		listaCiudades = ciudadService.obtenerPorProvincia(provincia);
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public Provincia[] getListaProvincias() {
		return Provincia.values();
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void insertarCiudad() {
		boolean bn = true;
		for (Ciudad c : listaCiudades)
			if (c.getNombre().compareToIgnoreCase(ciudad.getNombre()) == 0) {
				bn = false;
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"YA EXISTE LA CIUDAD");
				break;
			}
		if (bn) {
			ciudad.setProvincia(provincia);
			ciudadService.insertar(ciudad);
			cargarCiudades();
		}
		ciudad = new Ciudad();
	}

	public void limpiarObjetos() {
		ciudad = new Ciudad();
		listaCiudades = new ArrayList<Ciudad>();
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}
