package ec.com.redepronik.negosys.utils;

import static ec.com.redepronik.negosys.utils.Utils.invertirPalabra;
import static ec.com.redepronik.negosys.utils.Utils.subString;
import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoString;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.invfac.entity.Local;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.FacturaReporte;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

public class UtilsImpresion {

	public static String centrar(String palabra, int ancho) {
		if (palabra.length() > ancho)
			return palabra.substring(0, ancho);
		else {
			String ret = "";
			int c = (ancho - palabra.length()) / 2;
			for (int i = 0; i < ancho; i++)
				if (i == c) {
					ret += palabra;
					i += palabra.length() - 1;
				} else
					ret += " ";
			return ret;
		}
	}

	public static void escribeCabezeraFactura(Local local, String nombre) {
		RandomAccessFile fichero = null;
		try {
			fichero = new RandomAccessFile(getRutaArchivoFactura(nombre), "rw");
			if (fichero.length() == 0) {
				fichero.seek(0);
				fichero.writeBytes(centrar(local.getNombre(), 40) + "\n"
						+ centrar(local.getDireccion(), 40) + "\n"
						+ "\nCODIGO  DESCR.     CANT.  P.UNIT   VALOR"
						+ "\n----------------------------------------");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fichero != null)
					fichero.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void escribeCuerpoFactura(String nombre, FacturaReporte fr,
			String comando) {
		String imp = fr.getImpuesto().contains("I") ? "*" : "";
		String may = comando.compareToIgnoreCase("m") == 0 ? "M" : "";
		String cod = fr.getCodigo();
		if (cod.length() < 7)
			cod = espacio(cod, 7);
		int esp = 7;
		if (imp.compareToIgnoreCase("") != 0
				&& may.compareToIgnoreCase("") != 0)
			esp = 5;
		else if (imp.compareToIgnoreCase("") != 0
				|| may.compareToIgnoreCase("") != 0)
			esp = 6;
		escribirFactura(
				nombre,
				"\n" + imp + may + subString(cod, esp) + " "
						+ fr.getDescripcion().substring(0, 10) + " "
						+ espacioCantidad(fr.getCantidad(), 5) + " "
						+ espacio(fr.getPrecioUnitVenta(), 7) + " "
						+ espacio(fr.getImporte(), 7));
	}

	public static void escribeFinFactura(String nombre, CantidadFactura cf,
			Factura e, int items, BigDecimal efectivo) {
		RandomAccessFile fichero = null;
		try {
			Persona p = e.getCajero().getEmpleado().getPersona();
			String fin = "\nSUBTOTAL 12%.................$ "
					+ espacio(cf.getSt12(), 9)
					+ "\nSUBTOTAL 0%..................$ "
					+ espacio(cf.getSt0(), 9)
					+ "\nTOTAL DESCUENTO..............$ "
					+ espacio(cf.gettDescuento(), 9)
					+ "\nIVA 12%......................$ "
					+ espacio(cf.getIva12(), 9)
					+ "\nVALOR TOTAL..................$ "
					+ espacio(cf.getValorTotal(), 9) + "\n\nEFECTIVO: "
					+ efectivo + "\nCAMBIO: "
					+ redondearTotales(efectivo.subtract(cf.getValorTotal()))
					+ "\nITEMS: " + items + " FECHA: "
					+ fechaFormatoString(e.getFechaInicio()) + "\nCAJER@: "
					+ p.getApellido() + " " + p.getNombre()
					+ "\nREVISE SUS DOCUMENTOS ELECTRONICOS"
					+ "\nEN e-comprobantes.supernisho.com.ec" + "\nEMPRESA: "
					+ e.getClienteFactura().getNombreComercial()
					+ "\nCED.o RUC: "
					+ e.getClienteFactura().getPersona().getCedula() + "\n"
					+ "\n" + centrar("SR. CLIENTE", 40) + "\n"
					+ centrar("VERIFIQUE EL DETALLE IMPRESO.", 40) + "\n"
					+ centrar("CUALQUIER CORRECCION DE LA MISMA, SERA", 40)
					+ "\n" + centrar("ACEPTADA HASTA 48 HORAS DESPUES", 40)
					+ "\n" + centrar("DE REALIZADA LA COMPRA", 40) + "\n"
					+ centrar("O LLAME AL 2948357", 40) + "\n\n"
					+ centrar("NEGOSYS 2.0 - WWW.REDEPRONIK.COM.EC", 40)
					+ "\nfin";
			fichero = new RandomAccessFile(getRutaArchivoFactura(nombre), "rw");
			fichero.seek(fichero.length());
			fichero.writeBytes(fin);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fichero != null)
					fichero.close();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}

	public static void escribirFactura(String nombre, String escribe) {
		RandomAccessFile fichero = null;
		try {
			fichero = new RandomAccessFile(getRutaArchivoFactura(nombre), "rw");
			fichero.seek(fichero.length());
			fichero.writeBytes(escribe);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fichero != null)
					fichero.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String espacio(BigDecimal valor, int ancho) {
		return espacio(String.valueOf(redondearTotales(valor)), ancho);
	}

	public static String espacio(String valor, int ancho) {
		String aux = invertirPalabra(valor);
		for (int i = aux.length(); i < ancho; i++)
			aux += " ";
		return invertirPalabra(aux);
	}

	public static String espacioCantidad(int valor, int ancho) {
		return espacio(String.valueOf(valor), ancho);
	}

	// devuelve la ruta y si no existe la crea
	public static File getRutaArchivoFactura(String nombre) {
		File directorio = new File("C:/Users/Public/" + nombre + ".txt");
		if (!directorio.exists()) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(directorio);
				bw = new BufferedWriter(fw);
				bw.write("");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fw)
						fw.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return directorio;
	}

}
