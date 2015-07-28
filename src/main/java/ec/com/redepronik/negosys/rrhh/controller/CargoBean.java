package ec.com.redepronik.negosys.rrhh.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.rrhh.entity.Cargo;
import ec.com.redepronik.negosys.rrhh.service.CargoService;

@Controller
@Scope("session")
public class CargoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CargoService cargoService;

	private List<Cargo> listaCargos;
	private Cargo cargo;

	public CargoBean() {
		listaCargos = new ArrayList<Cargo>();
		cargo = new Cargo();
	}

	public void actualizar(ActionEvent actionEvent) {
		String mensaje = cargoService.actualizar(cargo);
		mensaje(mensaje, "ACTUALIZÓ");
	}

	public void eliminar(ActionEvent actionEvent) {
		cargoService.eliminar(cargo);
	}

	public Cargo getCargo() {
		return cargo;
	}

	public List<Cargo> getListaCargos() {
		listaCargos = cargoService.obtener(null);
		return listaCargos;
	}

	public void insertar(ActionEvent actionEvent) {
		String mensaje = cargoService.insertar(cargo);
		mensaje(mensaje, "INSERTÓ");
	}

	public void limpiar() {
		cargo = new Cargo();
	}

	private void mensaje(String mensaje, String titulo) {
		if (mensaje.compareTo("SAVE") == 0) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"CARGO: " + cargo.getNombre(), "error", true);
			limpiar();
		} else
			presentaMensaje(FacesMessage.SEVERITY_INFO, "ERROR" + mensaje,
					"error", false);
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setListaCargos(List<Cargo> listaCargos) {
		this.listaCargos = listaCargos;
	}

}
