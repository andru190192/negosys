package ec.com.redepronik.negosys.invfac.service;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsDate.dateCompleto;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.divide;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicar;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondear;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.KardexDao;
import ec.com.redepronik.negosys.invfac.dao.ProductoDao;
import ec.com.redepronik.negosys.invfac.entity.EstadoKardex;
import ec.com.redepronik.negosys.invfac.entity.Kardex;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.invfac.entityAux.KardexReporte;

@Service
public class KardexServiceImpl implements KardexService {

	@Autowired
	private KardexDao kardexDao;

	@Autowired
	private ProductoDao productoDao;

	public String actualizar(Kardex kardex) {
		kardexDao.actualizar(kardex);
		return "SAVE";
	}

	public void actualizarNotaKardex(String codigoDocumentoAnterior,
			String codigoDocumentoNuevo) {
		List<Kardex> list = kardexDao.obtenerListaPorAtributo(Kardex.class,
				"nota", "'%" + codigoDocumentoAnterior + "%'", null);
		for (Kardex kardex : list) {
			kardex.setNota(kardex.getNota().replaceAll(codigoDocumentoAnterior,
					codigoDocumentoNuevo));
		}
	}

	private List<KardexReporte> cargarKardex(List<Kardex> kardexs) {
		List<KardexReporte> listaKardexReportes = new ArrayList<KardexReporte>();
		KardexReporte kr = new KardexReporte();
		for (Kardex kardex : kardexs) {
			int estado = kardex.getEstadoKardex().getId();
			if (estado == 1)
				listaKardexReportes.add(llenar(kardex, null, "", 1));
			else if (estado == 2 || estado == 3 || estado == 7 || estado == 9)
				kr = llenar(kardex, null, " " + kardex.getNota(), 2);
			else if (estado == 4 || estado == 5 || estado == 8)
				kr = llenar(kardex, null, " " + kardex.getNota(), 3);
			else if (estado == 6) {
				listaKardexReportes.add(llenar(kardex, kr, null, 4));
				kr = new KardexReporte();
			}
		}
		return listaKardexReportes;
	}

	public Boolean comprobarKardexInicial(Kardex kardex) {
		List<Kardex> k = kardexDao
				.obtenerPorHql(
						"select k from Kardex k where k.producto.id=?1 and k.local.id=?2",
						new Object[] { kardex.getProducto().getId(),
								kardex.getLocal().getId() });
		return k.isEmpty() ? false : true;
	}

	public List<KardexReporte> generarkardex(Producto producto,
			Date fechaInicio, Date fechaFinal, Local local) {
		List<KardexReporte> list = new ArrayList<KardexReporte>();

		if (producto.getId() == null || producto.getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN PRODUCTO");
		else if (local == null || local.getId() == null || local.getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "ESCOJA UN LOCAL");
		else if (fechaInicio != null && fechaFinal != null
				&& fechaInicio.compareTo(fechaFinal) >= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"LA FECHA DE INICIO ES MAYOR A LA FECHA FINAL");
		else {
			if (fechaInicio == null && fechaFinal == null) {
				int max = Long
						.valueOf(
								(long) kardexDao
										.contar("select count(*) from Kardex k inner join k.producto p inner join k.local l where p.id=?1 and l.id=?2",
												new Object[] {
														producto.getId(),
														local.getId() }))
						.intValue();
				list = cargarKardex(kardexDao
						.obtenerPorHql(
								"select k from Kardex k inner join k.producto p inner join k.local l where p.id=?1 and l.id=?2 order by k.fecha",
								new Object[] { producto.getId(), local.getId() },
								max >= 40 ? max - 40 : 0, max));
			} else
				list = cargarKardex(kardexDao
						.obtenerPorHql(
								"select k from Kardex k inner join k.producto p inner join k.local l where p.id=?1 and (k.fecha between ?2 and ?3) and l.id=?4 order by k.fecha",
								new Object[] { producto.getId(), fechaInicio,
										dateCompleto(fechaFinal), local.getId() }));
			if (list.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"ESTE PRODUCTO NO TIENE KARDEX");
		}
		return list;
	}

	public Kardex generarKardexSaldo(Kardex nuevo, int cantidad,
			BigDecimal precio, boolean ingreso) {
		Kardex kardex = new Kardex();
		kardex.setEstadoKardex(EstadoKardex.SA);
		kardex.setProducto(nuevo.getProducto());
		kardex.setLocal(nuevo.getLocal());
		kardex.setNota(null);
		kardex.setFecha(timestamp());
		if (ingreso)
			kardex.setCantidad(nuevo.getCantidad() + cantidad);
		else
			kardex.setCantidad(cantidad - nuevo.getCantidad());

		kardex.setPrecio(precio);
		kardex.setActivo(true);
		return kardex;
	}

	public void insertar(Kardex kardex) {
		kardexDao.insertar(kardex);
	}

	public boolean insertarKardexInicial(Producto producto, Local local,
			BigDecimal precio, Integer cantidad) {
		boolean retorno = false;
		if (producto.getId() == null || producto.getId() == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"ESCOJA UNA PRODUCTO VÁLIDO");
		else if (precio.compareTo(newBigDecimal()) == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO, "INGRESE UN PRECIO > 0");
		else if (cantidad == 0)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"INGRESE UNA CANTIDAD > 0");
		else {
			Kardex kardex = new Kardex();
			kardex.setLocal(local);
			kardex.setProducto(producto);
			kardex.setEstadoKardex(EstadoKardex.II);
			kardex.setCantidad(cantidad);
			kardex.setFecha(timestamp());
			kardex.setNota("");
			kardex.setPrecio(precio);
			kardex.setActivo(true);
			if (comprobarKardexInicial(kardex))
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"YA EXISTE UN INVENTARIO INICIAL DE ESTE PRODUCTO EN ESTE LOCAL");
			else {
				insertar(kardex);
				presentaMensaje(
						FacesMessage.SEVERITY_INFO,
						"SE INSERTO EL INVENTARIO INICIAL PARA EL PRODUCTO "
								+ producto.getNombre()
								+ " EN LA BODEGA DEL ESTABLECIMIENTO "
								+ local.getNombre());
			}
			retorno = true;
		}
		return retorno;
	}

	public List<KardexReporte> invertirKardex(List<KardexReporte> list) {
		List<KardexReporte> listAux = new ArrayList<KardexReporte>();
		for (int i = list.size() - 1; i >= 0; i--)
			listAux.add(list.get(i));
		return listAux;
	}

	private KardexReporte llenar(Kardex kardex, KardexReporte kr,
			String detalle, int columna) {
		if (columna == 4) {
			kr.setCantidadSaldo(kardex.getCantidad());
			kr.setPrecioSaldo(redondear(kardex.getPrecio()));
			kr.setTotalSaldo(multiplicar(kardex.getPrecio(),
					kardex.getCantidad()));
			return kr;
		} else {
			KardexReporte k = new KardexReporte();
			k.setFecha(kardex.getFecha());
			k.setDetalle(kardex.getEstadoKardex().getNombre() + detalle);
			k.setNombreBodega(kardex.getLocal().getNombre());

			if (columna == 1) {
				k.setCantidadSaldo(kardex.getCantidad());
				k.setPrecioSaldo(redondear(kardex.getPrecio()));
				k.setTotalSaldo(multiplicar(kardex.getPrecio(),
						kardex.getCantidad()));
			} else if (columna == 2) {
				k.setCantidadSalida(kardex.getCantidad());
				k.setPrecioSalida(redondear(kardex.getPrecio()));
				k.setTotalSalida(multiplicar(kardex.getPrecio(),
						kardex.getCantidad()));
			} else if (columna == 3) {
				k.setCantidadEntrada(kardex.getCantidad());
				k.setPrecioEntrada(redondear(kardex.getPrecio()));
				k.setTotalEntrada(multiplicar(kardex.getPrecio(),
						kardex.getCantidad()));
			}
			return k;
		}
	}

	public Kardex obtenerSaldoActual(String ean) {
		List<Kardex> lk = kardexDao
				.obtenerPorHql(
						"select k from Kardex k where (k.estadoKardex.id=1 or k.estadoKardex.id=6) and k.producto.ean=?1 order by k.fecha desc",
						new Object[] { ean });
		return lk.isEmpty() ? null : lk.get(0);
	}

	public Kardex obtenerSaldoActual(String ean, Integer localId) {
		List<Kardex> lk = kardexDao
				.obtenerPorHql(
						"select k from Kardex k where (k.estadoKardex like 'II' or k.estadoKardex like 'SA') and k.producto.ean=?1 and k.local.id=?2 order by k.fecha desc",
						new Object[] { ean, localId });
		return lk.isEmpty() ? null : lk.get(0);
	}

	public Map<String, Object> ponderarPrecio(Producto producto, int localId,
			BigDecimal precio, int cantidad) {
		Map<String, Object> parametro = new HashMap<String, Object>();

		int can = 0;
		BigDecimal pre = newBigDecimal();
		Kardex k = obtenerSaldoActual(producto.getEan(), localId);
		if (k != null) {
			can = k.getCantidad();
			pre = k.getPrecio();
			k.setActivo(false);
			actualizar(k);
		}

		cantidad = cantidad < 0 ? cantidad * -1 : cantidad;

		BigDecimal valorTotalNuevo = multiplicar(pre, can).add(
				multiplicar(precio, cantidad));
		BigDecimal valorNuevoPrecio = divide(valorTotalNuevo, can + cantidad);

		producto.setPrecio(valorNuevoPrecio);
		productoDao.actualizar(producto);
		parametro.put("precio", valorNuevoPrecio);
		parametro.put("cantidad", can);
		return parametro;
	}

}