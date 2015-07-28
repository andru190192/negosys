package ec.com.redepronik.negosys.invfac.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Banco;
import ec.com.redepronik.negosys.invfac.service.BancoService;

@Controller
@Scope("session")
public class BancoBean {

	@Autowired
	private BancoService bancoService;

	private List<Banco> listaBancos;
	private Banco banco;
	private FacesMessage msg;

	public BancoBean() {
		banco = new Banco();
	}

	public void actualizar(ActionEvent actionEvent) {
		String mensaje = bancoService.actualizar(banco);
		mensaje(mensaje, "ACTUALIZÓ");
	}

	public void eliminar(ActionEvent actionEvent) {
		bancoService.eliminar(banco);
		if (banco.getActivo())
			presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"ACTIVO", "BANCO: " + banco.getNombre()));
		else
			presentaMensaje(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"DESACTIVO", "BANCO: " + banco.getNombre()));
	}

	public Banco getBanco() {
		return banco;
	}

	public List<Banco> getListaBancos() {
		if (listaBancos == null) {
			listaBancos = new ArrayList<Banco>();
			listaBancos = bancoService.obtener(null);
		}
		return listaBancos;
	}

	public void insertar(ActionEvent actionEvent) {
		String mensaje = bancoService.insertar(banco);
		listaBancos = bancoService.obtener(null);
		mensaje(mensaje, "INSERTÓ");
	}

	public void limpiar() {
		banco = new Banco();
	}

	private void mensaje(String mensaje, String titulo) {
		RequestContext context = RequestContext.getCurrentInstance();
		if (mensaje.compareTo("SAVE") == 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo,
					"Banco: " + banco.getNombre());
			context.addCallbackParam("error", true);
			limpiar();
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR", mensaje);
			context.addCallbackParam("error", false);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	private void presentaMensaje(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void setListaBancos(List<Banco> listaBancos) {
		this.listaBancos = listaBancos;
	}
}
