package ec.com.redepronik.negosys.invfac.report;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.rrhh.entity.Ciudad;
import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.Proveedor;
import ec.com.redepronik.negosys.rrhh.service.CiudadService;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.ProveedorService;
import ec.com.redepronik.negosys.utils.service.ReporteService;

@Controller
public class DirectorioReport {

	@Autowired
	private ReporteService reporteService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private CiudadService ciudadService;

	private List<Ciudad> listaCiudades;
	private Integer ciudadId;

	public DirectorioReport() {
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	@PostConstruct
	// este es el select que carga al arrancar el servidor en modo development
	public void init() {
		listaCiudades = ciudadService.obtener(true);
	}

	public void reporteCliente(ActionEvent actionEvent) {
		if (ciudadId == 0) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "SELECCIONE UNA CIUDAD");
		} else {
			List<Cliente> list = clienteService
					.ReporteObtenerPorCiudad(ciudadId);
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO HAY DATOS QUE MOSTRAR");
			else {
				Map<String, Object> parametro = new HashMap<String, Object>();
				parametro.put("ciudad",
						ciudadService.obtenerPorCiudadId(ciudadId).getNombre());
				reporteService.generarReportePDF(list, parametro, "Cliente");
			}
		}
		ciudadId = 0;
	}

	public void reporteProveedor(ActionEvent actionEvent) {
		List<Proveedor> list = proveedorService
				.ReporteObtenerPorCiudad(ciudadId);
		if (list.isEmpty())
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"NO HAY DATOS QUE MOSTRAR");
		else {
			Map<String, Object> parametro = new HashMap<String, Object>();
			String ciudad = ciudadId == 0 ? "TODOS" : ciudadService
					.obtenerPorCiudadId(ciudadId).getNombre();
			parametro.put("ciudad", ciudad);
			reporteService.generarReportePDF(list, parametro, "Proveedor");
		}
		ciudadId = 0;
	}

	public Integer getCiudadId() {
		return ciudadId;
	}

	public void setCiudadId(Integer ciudadId) {
		this.ciudadId = ciudadId;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

}