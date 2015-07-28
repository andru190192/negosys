package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaImagen;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.guardarImagen;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.leerImagen;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Parametro;
import ec.com.redepronik.negosys.invfac.service.ParametroService;
import ec.com.redepronik.negosys.utils.configuraciones.TablaTipoRedondeo;
import ec.com.redepronik.negosys.utils.configuraciones.TipoRedondeo;

@Controller
@Scope("session")
public class ParametroBean {

	@Autowired
	private ParametroService parametroService;

	private Parametro parametro;

	private List<TipoRedondeo> listaTipoRedondeos;
	private List<TablaTipoRedondeo> listaTablaTipoRedondeos;

	public ParametroBean() {
		parametro = new Parametro();
	}

	public List<TablaTipoRedondeo> getListaTablaTipoRedondeos() {
		return listaTablaTipoRedondeos;
	}

	public List<TipoRedondeo> getListaTipoRedondeos() {
		return listaTipoRedondeos;
	}

	public Parametro getParametro() {
		return parametro;
	}

	public void guardar(ActionEvent actionEvent) {
		parametroService.actualizar(parametro);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"ACTUALIZACIÓN CORRECTA DE PARÁMETROS");
	}

	@PostConstruct
	public void init() {
		parametro = parametroService.obtener();

		listaTipoRedondeos = new ArrayList<TipoRedondeo>();
		for (RoundingMode r : RoundingMode.values())
			listaTipoRedondeos.add(new TipoRedondeo(r.name(), r.ordinal()));

		listaTablaTipoRedondeos = new ArrayList<TablaTipoRedondeo>();
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("5.5", "6", "5", "6",
				"5", "6", "5", "6", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("2.5", "3", "2", "3",
				"2", "3", "2", "2", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("1.6", "2", "1", "2",
				"1", "2", "2", "2", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("1.1", "2", "1", "2",
				"1", "1", "1", "1", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("1.0", "1", "1", "1",
				"1", "1", "1", "1", "1"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("-1", "-1", "-1",
				"-1", "-1", "-1", "-1", "-1", "-1"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("-1,1", "-2", "-1",
				"-1", "-2", "-1", "-1", "-1", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("-1.6", "-2", "-1",
				"-1", "-2", "-2", "-2", "-2", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("-2,5", "-3", "-2",
				"-2", "-3", "-3", "-2", "-2", "ArithmeticException"));
		listaTablaTipoRedondeos.add(new TablaTipoRedondeo("-5,5", "-6", "-5",
				"-5", "-6", "-6", "-5", "-6", "ArithmeticException"));
	}

	public void setListaTablaTipoRedondeos(
			List<TablaTipoRedondeo> listaTablaTipoRedondeos) {
		this.listaTablaTipoRedondeos = listaTablaTipoRedondeos;
	}

	public void setListaTipoRedondeos(List<TipoRedondeo> listaTipoRedondeos) {
		this.listaTipoRedondeos = listaTipoRedondeos;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public void subirImagen(FileUploadEvent event) {
		try {
			guardarImagen(getRutaImagen(),
					leerImagen(event.getFile().getInputstream(), null), "logo",
					0, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
