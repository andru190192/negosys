package ec.com.redepronik.negosys.utils;

import static ec.com.redepronik.negosys.utils.Utils.subString;
import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaFuente;
import static ec.com.redepronik.negosys.utils.UtilsXML.guardarXML;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UtilsJRXML {

	public static void cargarJRXML(String archivo) {
		try {
			DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder creadorDocumento = fábricaCreadorDocumento
					.newDocumentBuilder();
			Document documento = creadorDocumento.parse(archivo);
			leerJRXML(documento);
			guardarXML(documento, archivo);
		} catch (Exception ex) {
			System.out
					.println("ERROR: No se ha podido crear el generador de documentos XML\n"
							+ ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static List<String> cargarJRXML(String path, String reporte) {
		List<String> list = new ArrayList<String>();
		try {
			DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder creadorDocumento = fábricaCreadorDocumento
					.newDocumentBuilder();
			Document documento = creadorDocumento.parse(path + reporte);
			list = leerSubReportesJRXML(documento);
			list.add(reporte);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public static List<String> cargarTodosJRXML(String path, String reporte) {
		List<String> list = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();
		list = cargarJRXML(path, reporte);
		for (String s : list)
			list1.addAll(cargarJRXML(path, s));

		list.addAll(list1);
		list = quitarLista(list);
		for (String s : list)
			cargarJRXML(path + s);
		return list;
	}

	public static void compilarJRXML(String path, String reporte) {
		List<String> list = cargarTodosJRXML(path, reporte);
		for (String report : list)
			try {
				JasperCompileManager.compileReportToFile(path + report, path
						+ subString(report, ".jrxml") + ".jasper");
			} catch (JRException e) {
				e.printStackTrace();
			}
	}

	public static void leerJRXML(Document documento) {
		NodeList listaFont = documento.getElementsByTagName("font");
		for (int i = 0; i < listaFont.getLength(); i++) {
			Node font = listaFont.item(i);
			Element e = (Element) font;
			e.setAttribute("isPdfEmbedded", "true");
			e.setAttribute("pdfEncoding", "Cp1252");
			e.setAttribute(
					"pdfFontName",
					getRutaFuente()
							+ nombreFuente(e.getAttribute("fontName"),
									e.getAttribute("isBold")));
		}
	}

	public static List<String> leerSubReportesJRXML(Document documento) {
		List<String> list = new ArrayList<String>();
		NodeList listaFont = documento
				.getElementsByTagName("subreportExpression");
		for (int i = 0; i < listaFont.getLength(); i++) {
			NodeList font = (NodeList) listaFont.item(i);
			Node font1 = font.item(0);
			String valor = font1.getNodeValue();
			valor = valor.substring(21, valor.length() - 1);
			list.add(subString(valor, ".jasper") + ".jrxml");
		}
		return list;
	}

	public static String nombreFuente(String nombreFuente, String tipo) {
		if (nombreFuente.compareToIgnoreCase("") == 0
				|| nombreFuente.compareToIgnoreCase("SansSerif") == 0)
			return tipo.compareToIgnoreCase("true") == 0 ? "sanssb__.ttf"
					: "sanss___.ttf";
		else if (nombreFuente.compareToIgnoreCase("Courier New") == 0)
			return tipo.compareToIgnoreCase("true") == 0 ? "courbd.ttf"
					: "cour.ttf";
		else if (nombreFuente.compareToIgnoreCase("Consolas") == 0)
			return tipo.compareToIgnoreCase("true") == 0 ? "consolab.ttf"
					: "consola.ttf";
		else if (nombreFuente.compareToIgnoreCase("Lucida Console") == 0)
			return "lucon.ttf";
		else if (nombreFuente.compareToIgnoreCase("Lucida Sans") == 0)
			return tipo.compareToIgnoreCase("true") == 0 ? "LSANSD.TTF"
					: "LSANS.TTF";
		return "";
	}

	private static List<String> quitarLista(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String dato = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				String dato1 = list.get(j);
				if (dato != null && dato1 != null
						&& dato.compareToIgnoreCase(dato1) == 0)
					list.set(j, null);
			}
		}
		List<String> list1 = new ArrayList<String>();
		for (String s : list) {
			if (s != null)
				list1.add(s);
		}
		return list1;
	}

}
