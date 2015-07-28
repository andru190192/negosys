package ec.com.redepronik.negosys.invfac.controller;

import static ec.com.redepronik.negosys.utils.Utils.expRegular;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoDate;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestampCompleto;
import static ec.com.redepronik.negosys.utils.UtilsMath.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.invfac.entity.Banco;
import ec.com.redepronik.negosys.invfac.entity.Entrada;
import ec.com.redepronik.negosys.invfac.entity.EstadoProductoVenta;
import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.PagoEntrada;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entity.TipoPago;
import ec.com.redepronik.negosys.invfac.entity.TipoPrecioProducto;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.invfac.entityAux.PersonaCedulaNombre;
import ec.com.redepronik.negosys.invfac.entityAux.Pvp;
import ec.com.redepronik.negosys.invfac.report.InventarioReportes;
import ec.com.redepronik.negosys.invfac.service.BancoService;
import ec.com.redepronik.negosys.invfac.service.DocumentosElectronicosService;
import ec.com.redepronik.negosys.invfac.service.FacturaService;
import ec.com.redepronik.negosys.invfac.service.KardexService;
import ec.com.redepronik.negosys.invfac.service.LocalService;
import ec.com.redepronik.negosys.invfac.service.ProductoService;
import ec.com.redepronik.negosys.rrhh.entity.Cliente;
import ec.com.redepronik.negosys.rrhh.entity.EmpleadoCargo;
import ec.com.redepronik.negosys.rrhh.entity.Persona;
import ec.com.redepronik.negosys.rrhh.service.ClienteService;
import ec.com.redepronik.negosys.rrhh.service.EmpleadoService;

@Controller
@Scope("session")
public class FacturaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private LocalService localService;

	@Autowired
	private InventarioReportes facturaReport;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private DocumentosElectronicosService documentosElectronicosService;

	@Autowired
	private BancoService bancoService;

	private List<Persona> listaClientes;
	private List<Local> listaLocales;
	private List<PersonaCedulaNombre> listaVendedores;
	private List<Producto> listaProductos;
	private List<Pvp> listaPrecios;
	private List<Banco> listaBancos;

	private List<FacturaReporte> listaFacturaReporte;

	private Factura factura;
	private Producto producto;
	private CantidadFactura cantidadFactura;
	private TipoPrecioProducto tipoPrecioProducto;
	private Entrada entrada;

	private String cliente;
	private String clienteFactura;
	private String vendedor;
	private String establecimiento;
	private Integer localId;

	private String criterioCliente;
	private String criterioProducto;
	private String cantidadProducto;

	private BigDecimal totalMonto;
	private int registrosActivos;

	public FacturaBean() {

	}

	public void agregarEntrada(Long tipoPago) {
		PagoEntrada pagoEntrada = entrada.getPagoEntradas().get(
				entrada.getPagoEntradas().size() - 1);

		if (tipoPago == 1)
			pagoEntrada.setTipoPago(TipoPago.EF);
		else if (tipoPago == 2)
			pagoEntrada.setTipoPago(TipoPago.TC);
		else if (tipoPago == 3)
			pagoEntrada.setTipoPago(TipoPago.CH);

		entrada.setFechaPago(timestampCompleto(new Date()));
		entrada.setFechaMora(fechaFormatoDate("01-01-1900"));
		entrada.setFechaLimite(entrada.getFechaPago());
		entrada.setPagado(true);
		entrada.setActivo(true);
		entrada.setCuota(pagoEntrada.getCuota());
		entrada.setSaldo(newBigDecimal());

		if (compareTo(pagoEntrada.getCuota(), cantidadFactura.getValorTotal()) > 0)
			pagoEntrada.setCuota(cantidadFactura.getValorTotal());
		pagoEntrada.setFechaPago(timestamp(entrada.getFechaPago()));
		pagoEntrada.setPagado(true);
		pagoEntrada.setActivo(true);

		entrada.addPagoEntrada(pagoEntrada);
		factura.addEntrada(entrada);

		totalMonto = totalMonto.add(entrada.getCuota());

		entrada = new Entrada();
		entrada.setPagoEntradas(new ArrayList<PagoEntrada>());
		entrada.addPagoEntrada(new PagoEntrada());

		if (totalMonto.compareTo(cantidadFactura.getValorTotal()) >= 0) {
			factura.setFechaCierre(timestamp());
			insertarFactura();
			RequestContext.getCurrentInstance().execute(
					"PF('dialogoPagoWV').hide()");
			RequestContext.getCurrentInstance().update("formCabeceraFactura");
			RequestContext.getCurrentInstance().update("formItems");
			RequestContext.getCurrentInstance().update("formTotales");
			RequestContext.getCurrentInstance().update("formTotal");
		}
	}

	public void agregarVendedor(PersonaCedulaNombre personaCedulaNombre) {
		factura.getVendedor().setId(personaCedulaNombre.getId());
		vendedor = personaCedulaNombre
				.getId()
				.toString()
				.concat("-")
				.concat(personaCedulaNombre.getNombre().concat(" ")
						.concat(personaCedulaNombre.getApellido()));
	}

	public void cambiarCantidad(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.calcularCantidad(facturaReporte,
				listaFacturaReporte);
		registrosActivos = facturaService
				.contarRegistrosActivos(listaFacturaReporte);
	}

	public void cambiarDescDol(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.calcularDescuentoDolares(
				facturaReporte, listaFacturaReporte);
	}

	public void cambiarDescPor(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.calcularDescuentoPorcentaje(
				facturaReporte, listaFacturaReporte);
	}

	public void cambiarEstado(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.cambiarEstado(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarImporte(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.calcularImporte(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarPrecio(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.calcularPrecio(facturaReporte,
				listaFacturaReporte);
	}

	public void cambiarTipoPrecio(FacturaReporte facturaReporte) {
		cantidadFactura = facturaService.calcularTipoPrecio(facturaReporte,
				listaFacturaReporte);
	}

	public void cargarCliente(Persona persona) {
		Persona p = clienteService.obtenerPorPersonaId(persona.getId());
		factura.setCliente(p.getCliente());
		factura.setClienteFactura(p.getCliente());
		cliente = p.getCliente().getId().toString().concat("-")
				.concat(p.getCedula()).concat("-").concat(p.getApellido())
				.concat(" ").concat(p.getNombre());
		clienteFactura = cliente;
		if (listaLocales.size() == 1) {
			Local local = listaLocales.get(0);
			establecimiento = local
					.getNombre()
					.concat("(")
					.concat(local
							.getCodigoEstablecimiento()
							.concat(")")
							.concat(" - PUNTO EMISIÓN(")
							.concat(String.format("%03d",
									local.getPuntoEmisionFactura()).concat(")")));

			boolean clienteVendedor = false;
			String aux = null;
			for (PersonaCedulaNombre pcn : listaVendedores) {
				if (pcn.getCedula().compareTo(
						factura.getCliente().getPersona().getCedula()) == 0) {
					clienteVendedor = true;
					aux = pcn
							.getId()
							.toString()
							.concat("-")
							.concat(pcn.getNombre().concat(" ")
									.concat(pcn.getApellido()));
					break;
				}
			}
			if (clienteVendedor && listaFacturaReporte.size() > 0) {
				vendedor = aux;
			} else if (listaVendedores.size() == 1) {
				vendedor = listaVendedores
						.get(0)
						.getId()
						.toString()
						.concat("-")
						.concat(listaVendedores.get(0).getNombre().concat(" ")
								.concat(listaVendedores.get(0).getApellido()));
				RequestContext.getCurrentInstance().execute(
						"PF('obtenerProductoWV').show()");
			} else
				RequestContext.getCurrentInstance().execute(
						"PF('dialogoVendedorWV').show()");
		} else
			RequestContext.getCurrentInstance().execute(
					"PF('dialogoPuntoEmisionWV').show()");
	}

	public void cargarClienteFactura() {
		factura.setClienteFactura(clienteService.cargarCliente(clienteFactura));
	}

	public void cargarListaPrecios(Producto p) {
		producto = p;
		listaPrecios = productoService
				.obtenerListaPreciosConImpuestosPorProducto(p);
	}

	public void comprobarEstablecimientoVendedor() {
		if (listaLocales.size() == 1) {
			Local local = listaLocales.get(0);
			establecimiento = local
					.getNombre()
					.concat("(")
					.concat(local
							.getCodigoEstablecimiento()
							.concat(")")
							.concat(" - PUNTO EMISIÓN(")
							.concat(String.format("%03d",
									local.getPuntoEmisionFactura()).concat(")")));

			boolean clienteVendedor = false;
			String aux = null;
			for (PersonaCedulaNombre pcn : listaVendedores) {
				if (pcn.getCedula().compareTo(
						factura.getCliente().getPersona().getCedula()) == 0) {
					clienteVendedor = true;
					aux = pcn
							.getId()
							.toString()
							.concat("-")
							.concat(pcn.getNombre().concat(" ")
									.concat(pcn.getApellido()));
					break;
				}
			}
			if (clienteVendedor && listaFacturaReporte.size() > 0) {
				vendedor = aux;
			} else if (listaVendedores.size() == 1) {
				vendedor = listaVendedores
						.get(0)
						.getId()
						.toString()
						.concat("-")
						.concat(listaVendedores.get(0).getNombre().concat(" ")
								.concat(listaVendedores.get(0).getApellido()));
				RequestContext.getCurrentInstance().execute(
						"PF('obtenerProductoWV').show()");
			} else
				RequestContext.getCurrentInstance().execute(
						"PF('dialogoVendedorWV').show()");
		} else
			RequestContext.getCurrentInstance().execute(
					"PF('dialogoPuntoEmisionWV').show()");
	}

	public void agregarPuntoEmision(Local local) {
		establecimiento = local
				.getNombre()
				.concat("(")
				.concat(local
						.getCodigoEstablecimiento()
						.concat(")")
						.concat(" - PUNTO EMISIÓN(")
						.concat(String.format("%03d",
								local.getPuntoEmisionFactura()).concat(")")));

		boolean clienteVendedor = false;
		String aux = null;
		for (PersonaCedulaNombre pcn : listaVendedores) {
			if (pcn.getCedula().compareTo(
					factura.getCliente().getPersona().getCedula()) == 0) {
				clienteVendedor = true;
				aux = pcn
						.getId()
						.toString()
						.concat("-")
						.concat(pcn.getNombre().concat(" ")
								.concat(pcn.getApellido()));
				break;
			}
		}
		if (clienteVendedor && listaFacturaReporte.size() > 0) {
			vendedor = aux;
		} else if (listaVendedores.size() == 1) {
			vendedor = listaVendedores
					.get(0)
					.getId()
					.toString()
					.concat("-")
					.concat(listaVendedores.get(0).getNombre().concat(" ")
							.concat(listaVendedores.get(0).getApellido()));
			RequestContext.getCurrentInstance().execute(
					"PF('obtenerProductoWV').show()");
		} else
			RequestContext.getCurrentInstance().execute(
					"PF('dialogoVendedorWV').show()");
		RequestContext.getCurrentInstance().execute(
				"PF('dialogoPuntoEmisionWV').hide()");
	}

	public void convertirFactura(Cliente cliente, EmpleadoCargo vendedor,
			Local local, List<FacturaReporte> list) {
		nuevaFactura();
		this.cliente = cliente.getPersona().getCiudad().getNombre() + " - "
				+ cliente.getPersona().getCedula() + "-"
				+ cliente.getPersona().getApellido() + " "
				+ cliente.getPersona().getNombre();
		this.clienteFactura = this.cliente;
		this.factura.setCliente(cliente);
		this.factura.setClienteFactura(cliente);
		this.vendedor = vendedor.getId() + "-"
				+ vendedor.getEmpleado().getPersona().getNombre() + " "
				+ vendedor.getEmpleado().getPersona().getApellido();
		this.factura.setVendedor(vendedor);
		this.factura.setEstablecimiento(establecimiento);
		this.establecimiento = local
				.getNombre()
				.concat("(")
				.concat(local
						.getCodigoEstablecimiento()
						.concat(")")
						.concat(" - PUNTO EMISIÓN(")
						.concat(String.format("%03d",
								local.getPuntoEmisionFactura()).concat(")")));
		this.listaFacturaReporte = list;

		for (FacturaReporte fr : list)
			cantidadFactura = facturaService.calcularCantidadFactura(
					cantidadFactura, fr);
	}

	public CantidadFactura getCantidadFactura() {
		return facturaService.redondearCantidadFactura(cantidadFactura);
	}

	public String getCantidadProducto() {
		return cantidadProducto;
	}

	public String getCliente() {
		return cliente;
	}

	public String getClienteFactura() {
		return clienteFactura;
	}

	public String getCriterioCliente() {
		return criterioCliente;
	}

	public String getCriterioProducto() {
		return criterioProducto;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public Factura getFactura() {
		return factura;
	}

	public List<Banco> getListaBancos() {
		return listaBancos;
	}

	public List<Persona> getListaClientes() {
		return listaClientes;
	}

	public EstadoProductoVenta[] getListaEstadoProductoVenta() {
		return EstadoProductoVenta.values();
	}

	public List<FacturaReporte> getListaFacturaReporte() {
		return listaFacturaReporte;
	}

	public List<Local> getListaLocales() {
		return listaLocales;
	}

	public List<Pvp> getListaPrecios() {
		return listaPrecios;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public List<PersonaCedulaNombre> getListaVendedores() {
		return listaVendedores;
	}

	public Producto getProducto() {
		return producto;
	}

	public int getRegistrosActivos() {
		return registrosActivos;
	}

	public TipoPrecioProducto getTipoPrecioProducto() {
		return tipoPrecioProducto;
	}

	public BigDecimal getTotalMonto() {
		return redondearTotales(totalMonto);
	}

	public String getVendedor() {
		return vendedor;
	}

	public void ingresarDetalleProducto() {
		String estadoProducto = null;
		Integer cantidad = 0;
		boolean insertar = false;
		if (cantidadProducto.contains("-")) {
			String aux[] = cantidadProducto.split("-");
			if (aux.length == 3
					&& expRegular(cantidadProducto,
							"(cnt-|CNT-|cla-|CLA-|mov-|MOV-|dir-|DIR-|tvc-|TVC-){1,3}[0-9]+(-){1,1}[0-9]+")) {
				// String ope = aux[0];
				// String num = aux[1];
				// if (ope.compareToIgnoreCase("cnt") == 0) {
				// facturaReporte.setCodigo("06");
				// facturaReporte.setDescripcion("RECARGA DE CNT-#" + num);
				// } else if (ope.compareToIgnoreCase("cla") == 0) {
				// facturaReporte.setCodigo("05");
				// facturaReporte.setDescripcion("RECARGA DE CLARO-#" + num);
				// } else if (ope.compareToIgnoreCase("mov") == 0) {
				// facturaReporte.setCodigo("01");
				// facturaReporte
				// .setDescripcion("RECARGA DE MOVISTAR-#" + num);
				// } else if (ope.compareToIgnoreCase("dir") == 0) {
				// facturaReporte.setCodigo("12");
				// facturaReporte.setDescripcion("RECARGA DE DIRECTV-#" + num);
				// } else if (ope.compareToIgnoreCase("tvc") == 0) {
				// facturaReporte.setCodigo("31");
				// facturaReporte.setDescripcion("RECARGA DE TVCABLE-#" + num);
				// }
				// facturaReporte.setPrecioUnitVenta(new BigDecimal(aux[2]));
			}
		} else if (expRegular(cantidadProducto, "[0-9]+")) {
			estadoProducto = "N";
			cantidad = Integer.parseInt(cantidadProducto);
			insertar = true;
		} else if (expRegular(cantidadProducto, "(n|N|p|P|i|I|c|C){1,1}[0-9]+")) {
			estadoProducto = String.valueOf(cantidadProducto.charAt(0));
			cantidad = Integer.parseInt(cantidadProducto.substring(1,
					cantidadProducto.length()));
			insertar = true;
		}
		if (cantidad == 0) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CANTIDAD INCORRECTA");
			insertar = false;
		}

		if (insertar) {
			Integer cantidadActual = kardexService.obtenerSaldoActual(
					producto.getEan(), localId).getCantidad();
			if (cantidadActual == 0) {
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO HAY SUFICIENTE STOCK, " + cantidadActual
								+ " UNIDAD(ES) EN EL LOCAL");
				RequestContext.getCurrentInstance().execute(
						"PF('dialogoCantidadProducto').hide()");
			} else if (cantidad <= cantidadActual) {
				Pvp pvp = null;
				for (Pvp p : listaPrecios) {
					if (p.getPvp()) {
						pvp = p;
						break;
					}
				}

				if (pvp == null) {
					presentaMensaje(FacesMessage.SEVERITY_INFO,
							"ESTE PRODUCTO NO TIENE PVP");
				} else {
					cantidadFactura = facturaService.cargarProductoLista(
							estadoProducto, cantidad, producto.getEan(),
							listaFacturaReporte, localId, pvp);

					presentaMensaje(FacesMessage.SEVERITY_INFO,
							"AGREGÓ PRODUCTO " + producto.getNombre());

					RequestContext.getCurrentInstance().execute(
							"PF('dialogoCantidadProducto').hide()");
				}
			} else {
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO HAY SUFICIENTE STOCK, " + cantidadActual
								+ " UNIDAD(ES) EN EL LOCAL");
			}
		}
		cantidadProducto = new String();
	}

	public void ingresarDetalleProducto(Pvp pvp) {
		String estadoProducto = null;
		Integer cantidad = 0;
		boolean insertar = false;
		if (cantidadProducto.contains("-")) {
			String aux[] = cantidadProducto.split("-");
			if (aux.length == 3
					&& expRegular(cantidadProducto,
							"(cnt-|CNT-|cla-|CLA-|mov-|MOV-|dir-|DIR-|tvc-|TVC-){1,3}[0-9]+(-){1,1}[0-9]+")) {
				// String ope = aux[0];
				// String num = aux[1];
				// if (ope.compareToIgnoreCase("cnt") == 0) {
				// facturaReporte.setCodigo("06");
				// facturaReporte.setDescripcion("RECARGA DE CNT-#" + num);
				// } else if (ope.compareToIgnoreCase("cla") == 0) {
				// facturaReporte.setCodigo("05");
				// facturaReporte.setDescripcion("RECARGA DE CLARO-#" + num);
				// } else if (ope.compareToIgnoreCase("mov") == 0) {
				// facturaReporte.setCodigo("01");
				// facturaReporte
				// .setDescripcion("RECARGA DE MOVISTAR-#" + num);
				// } else if (ope.compareToIgnoreCase("dir") == 0) {
				// facturaReporte.setCodigo("12");
				// facturaReporte.setDescripcion("RECARGA DE DIRECTV-#" + num);
				// } else if (ope.compareToIgnoreCase("tvc") == 0) {
				// facturaReporte.setCodigo("31");
				// facturaReporte.setDescripcion("RECARGA DE TVCABLE-#" + num);
				// }
				// facturaReporte.setPrecioUnitVenta(new BigDecimal(aux[2]));
			}
		} else if (expRegular(cantidadProducto, "[0-9]+")) {
			estadoProducto = "N";
			cantidad = Integer.parseInt(cantidadProducto);
			insertar = true;
		} else if (expRegular(cantidadProducto, "(n|N|p|P|i|I|c|C){1,1}[0-9]+")) {
			estadoProducto = String.valueOf(cantidadProducto.charAt(0));
			cantidad = Integer.parseInt(cantidadProducto.substring(1,
					cantidadProducto.length()));
			insertar = true;
		}

		if (cantidad == 0) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CANTIDAD INCORRECTA");
			RequestContext
					.getCurrentInstance()
					.execute(
							"document.getElementById('formCantidadProducto:txtCantidadProducto').focus()");
			insertar = false;
		}

		if (insertar) {
			Integer cantidadActual = kardexService.obtenerSaldoActual(
					producto.getEan(), localId).getCantidad();
			if (cantidadActual == 0) {
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO HAY SUFICIENTE STOCK, " + cantidadActual
								+ " UNIDAD(ES) EN EL LOCAL");
				RequestContext.getCurrentInstance().execute(
						"PF('dialogoCantidadProducto').hide()");
			} else if (cantidad <= cantidadActual) {
				cantidadFactura = facturaService.cargarProductoLista(
						estadoProducto, cantidad, producto.getEan(),
						listaFacturaReporte, localId, pvp);

				presentaMensaje(FacesMessage.SEVERITY_INFO, "AGREGÓ PRODUCTO "
						+ producto.getNombre());

				RequestContext.getCurrentInstance().execute(
						"PF('dialogoCantidadProducto').hide()");
			} else {
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO HAY SUFICIENTE STOCK, " + cantidadActual
								+ " UNIDAD(ES) EN EL LOCAL");
			}
		}
		cantidadProducto = new String();
	}

	@PostConstruct
	public void init() {
		listaLocales = localService.obtenerPorCedulaAndCargo(
				SecurityContextHolder.getContext().getAuthentication()
						.getName(), 4);
		listaVendedores = empleadoService.obtenerPorCargo(5);
		listaBancos = bancoService.obtener(true);
		nuevaFactura();
	}

	public void cargarPorDefectoProductosFacturar() {
		for (Producto p : productoService.obtenerPorDefectoFacturar()) {
			// FacturaReporte fr = new FacturaReporte();
			// fr.setProducto(p);
			// listaFacturaReporte.add(fr);
			Pvp pvp = null;
			for (Pvp pv : productoService
					.obtenerListaPreciosConImpuestosPorProducto(p)) {
				if (pv.getPvp()) {
					pvp = pv;
					break;
				}
			}
			facturaService.cargarProductoLista("N", 0, p.getEan(),
					listaFacturaReporte, localId, pvp);
		}
	}

	public void insertarFactura() {
		if (cantidadFactura.getValorTotal().compareTo(new BigDecimal("19")) > 0
				&& factura.getClienteFactura().getPersona().getCedula()
						.compareTo("9999999999999") == 0) {
			presentaMensaje(
					FacesMessage.SEVERITY_FATAL,
					"COMPRA MAYOR A $20.00 INGRESE UN RUC DIFERENTE A CONSUMIDOR FINAL",
					"consumidorFinal", true);
		} else {
			Integer facturaId = facturaService.insertar(factura,
					listaFacturaReporte).getId();
			if (facturaId != null) {
				// facturaReport.reporteFacturaC(facturaId);
				documentosElectronicosService.generarFacturaXML(facturaId,
						cantidadFactura);
				nuevaFactura();
				RequestContext.getCurrentInstance().execute(
						"PF('dialogoPagoWV').hide()");
			}
		}
	}

	public void nuevaFactura() {
		// FACTURA
		factura = new Factura();
		factura.setCliente(new Cliente());
		factura.setClienteFactura(new Cliente());
		factura.setVendedor(new EmpleadoCargo());
		factura.setEstablecimiento(new String());
		factura.setEstablecimiento(listaLocales.get(0)
				.getCodigoEstablecimiento());
		factura.setEntradas(new ArrayList<Entrada>());
		// ENTRADA
		entrada = new Entrada();
		entrada.setPagoEntradas(new ArrayList<PagoEntrada>());
		entrada.addPagoEntrada(new PagoEntrada());
		// VARIABLES AXULIARES
		totalMonto = newBigDecimal();
		cantidadFactura = new CantidadFactura();
		setCliente("");
		setClienteFactura("");
		setEstablecimiento("");
		setVendedor("");
		registrosActivos = 0;
		cantidadProducto = new String();
		criterioProducto = new String();
		localId = localService.obtenerPorEstablecimiento(
				factura.getEstablecimiento()).getId();

		listaClientes = new ArrayList<Persona>();
		listaFacturaReporte = new ArrayList<FacturaReporte>();

		cargarPorDefectoProductosFacturar();

		RequestContext.getCurrentInstance().update("formCliente:tablaClientes");
		RequestContext.getCurrentInstance().execute(
				"PF('buscarCliente').show()");
	}

	public List<String> obtenerClienteFacturaPorBusqueda(
			String criterioClienteBusqueda) {
		List<String> lista = clienteService
				.obtenerListaClientesAutoComplete(criterioClienteBusqueda);
		if (lista.size() == 1) {
			clienteFactura = (lista.get(0));
			cargarClienteFactura();
		}
		return lista;
	}

	public void obtenerClientes() {
		if (expRegular(criterioCliente, "(c|C){1,1}[0-9]+")) {
			listaClientes = clienteService.obtener(null,
					Integer.parseInt(criterioCliente.substring(1)));
		} else {
			listaClientes = clienteService.obtener(criterioCliente, null);
		}
		if (listaClientes.size() == 1) {
			Persona p = clienteService.obtenerPorPersonaId(listaClientes.get(0)
					.getId());
			factura.setCliente(p.getCliente());
			factura.setClienteFactura(p.getCliente());
			cliente = p.getCliente().getId().toString().concat("-")
					.concat(p.getCedula()).concat("-").concat(p.getApellido())
					.concat(" ").concat(p.getNombre());
			clienteFactura = cliente;
			RequestContext.getCurrentInstance().execute(
					"PF('buscarCliente').hide()");
			comprobarEstablecimientoVendedor();
		}
		criterioCliente = "";
	}

	public void obtenerClientesFactura() {
		if (expRegular(criterioCliente, "(c|C){1,1}[0-9]+"))
			listaClientes = clienteService.obtener(null,
					Integer.parseInt(criterioCliente.substring(1)));
		else
			listaClientes = clienteService.obtener(criterioCliente, null);

		if (listaClientes.size() == 1) {
			Persona p = clienteService.obtenerPorPersonaId(listaClientes.get(0)
					.getId());
			factura.setClienteFactura(p.getCliente());
			clienteFactura = p.getCliente().getId().toString().concat("-")
					.concat(p.getCedula()).concat("-").concat(p.getApellido())
					.concat(" ").concat(p.getNombre());
			RequestContext.getCurrentInstance().execute(
					"PF('buscarClienteFactura').hide()");
		}
		criterioCliente = "";
	}

	public void obtenerProducto() {
		if (expRegular(criterioProducto, "(c|C){1,1}[0-9]+"))
			listaProductos = productoService.obtenerPorLocal(null,
					Long.parseLong(criterioProducto.substring(1)), localId);
		else
			listaProductos = productoService.obtenerPorLocal(criterioProducto,
					null, localId);
	}

	public void setCantidadFactura(CantidadFactura cantidadFactura) {
		this.cantidadFactura = cantidadFactura;
	}

	public void setCantidadProducto(String cantidadProducto) {
		this.cantidadProducto = cantidadProducto;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setClienteFactura(String clienteFactura) {
		this.clienteFactura = clienteFactura;
	}

	public void setCriterioCliente(String criterioCliente) {
		this.criterioCliente = criterioCliente;
	}

	public void setCriterioProducto(String criterioProducto) {
		this.criterioProducto = criterioProducto;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setListaBancos(List<Banco> listaBancos) {
		this.listaBancos = listaBancos;
	}

	public void setListaClientes(List<Persona> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public void setListaFacturaReporte(List<FacturaReporte> listaFacturaReporte) {
		this.listaFacturaReporte = listaFacturaReporte;
	}

	public void setListaLocales(List<Local> listaLocales) {
		this.listaLocales = listaLocales;
	}

	public void setListaPrecios(List<Pvp> listaPrecios) {
		this.listaPrecios = listaPrecios;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public void setListaVendedores(List<PersonaCedulaNombre> listaVendedores) {
		this.listaVendedores = listaVendedores;
	}

	public void setRegistrosActivos(int registrosActivos) {
		this.registrosActivos = registrosActivos;
	}

	public void setTipoPrecioProducto(TipoPrecioProducto tipoPrecioProducto) {
		this.tipoPrecioProducto = tipoPrecioProducto;
	}

	public void setTotalMonto(BigDecimal totalMonto) {
		this.totalMonto = totalMonto;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}
}