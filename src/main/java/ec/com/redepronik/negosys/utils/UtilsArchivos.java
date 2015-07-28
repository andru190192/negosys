package ec.com.redepronik.negosys.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

public class UtilsArchivos {

	private static final Properties p = System.getProperties();

	private static final String sep = p.getProperty("file.separator");

	private static final String rutaRaiz = (p.getProperty("os.name")
			.compareToIgnoreCase("linux") == 0 ? "/opt" : p
			.getProperty("user.home"))
			+ sep + "negosys" + sep;

	// devuelve un array byte de un archivo
	public static byte[] convertir(File file) {
		byte[] a = new byte[(int) file.length()];
		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(a);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	// devuelve un array byte de un archivo
	public static byte[] convertir(String ruta) {
		File file = new File(ruta);
		byte[] a = new byte[(int) file.length()];
		try {
			FileInputStream fis = new FileInputStream(file);
			fis.read(a);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public static File convertir(String nombre, String archivo) {
		try {
			byte[] arc = convertirString(archivo);
			File file = File.createTempFile(nombre, ".xml");
			file.deleteOnExit();
			FileOutputStream fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(arc);
			fileOuputStream.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// devuelve un array byte de un string
	public static byte[] convertirString(String archivo) {
		try {
			return archivo.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// devuelve la ruta y si no existe la crea
	public static String crearRuta(String ruta) {
		File directorio = new File(ruta);
		if (!directorio.exists())
			directorio.mkdirs();
		return ruta;
	}

	// devuelve true si elimina el archivo
	public static boolean eliminarArchivo(String archivo) {
		File directorio = new File(archivo);
		return directorio.delete();
	}

	// devuelve una lista de archivos que contiene
	public static List<String> getlistArchivos(String directorio) {
		File f = new File(directorio);
		List<String> list = new ArrayList<String>();
		for (String archivo : f.list())
			if (archivo.endsWith(".xml"))
				list.add(archivo);
		return list;
	}

	public static String getRutaCertificadoSeguridadSRIProduccion() {
		return getRutaDocumentosElectronicos()
				+ "certificadoSeguridadSRIProduccion";
	}

	public static String getRutaCertificadoSeguridadSRIPruebas() {
		return getRutaDocumentosElectronicos()
				+ "certificadoSeguridadSRIPruebas";
	}

	public static String getRutaDocumentosElectronicos() {
		return crearRuta(getRutaRaiz() + "documentosElectronicos" + sep);
	}

	public static String getRutaFirmaDigital() {
		return getRutaDocumentosElectronicos() + "firmaDigital.pfx";
	}

	public static String getRutaFuente() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/fuente")
				+ sep;
	}

	public static String getRutaImagen() {
		return crearRuta(getRutaRaiz() + "imagenes" + sep);
	}

	public static String getRutaImagenEmpleado() {
		return crearRuta(getRutaImagen() + "empleados" + sep);
	}

	public static String getRutaImagenProduccion() {
		return getRutaDocumentosElectronicos() + "produccion.jpeg";
	}

	public static String getRutaImagenProducto() {
		return crearRuta(getRutaImagen() + "productos" + sep);
	}

	public static String getRutaImagenPruebas() {
		return getRutaDocumentosElectronicos() + "pruebas.jpeg";
	}

	public static String getRutaLogo() {
		return getRutaImagen() + "logo.png";
	}

	public static String getRutaRaiz() {
		return crearRuta(rutaRaiz);
	}

	public static String getRutaReportes() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/reportes")
				+ sep;
	}

	public static String getRutaSRIAutorizados() {
		return crearRuta(getRutaDocumentosElectronicos() + "autorizados" + sep);
	}

	public static String getRutaSRIAutorizadosNoSubidos() {
		return crearRuta(getRutaSRIAutorizados() + "noSubidos" + sep);
	}

	public static String getRutaSRIFirmados() {
		return crearRuta(getRutaDocumentosElectronicos() + "firmados" + sep);
	}

	public static String getRutaSRIFirmadosRechazados() {
		return crearRuta(getRutaSRIFirmados() + "rechazados" + sep);
	}

	public static String getRutaSRIFirmadosTransmitidosSinRespuesta() {
		return crearRuta(getRutaSRIFirmados() + "transmitidosSinRespuesta"
				+ sep);
	}

	public static String getRutaSRIGenerados() {
		return crearRuta(getRutaDocumentosElectronicos() + "generados" + sep);
	}

	public static String getRutaSRINoAutorizados() {
		return crearRuta(getRutaDocumentosElectronicos() + "noAutorizados"
				+ sep);
	}

	// guarda la imagen en una ruta especifica y si el ancho y alto es diferente
	// de cero la redimenciona a la imagen
	public static void guardarImagen(String ruta, BufferedImage imagen,
			String nombreImagen, int ancho, int alto) {
		try {
			if (ancho != 0 && alto != 0)
				imagen = redimencionarImagen(imagen, ancho, alto);
			ImageIO.write(imagen, "png", new File(ruta + sep + nombreImagen
					+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String leerArchivo(String archivo) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					archivo), "UTF-8"));
			String linea;
			while ((linea = br.readLine()) != null)
				sb.append(linea + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return sb.toString();
	}

	// lee la imagen entrante y la devuelve como BufferedImage
	public static BufferedImage leerImagen(InputStream imagen, String ruta) {
		try {
			if (imagen != null)
				return ImageIO.read(imagen);
			File directorio = new File(ruta);
			if (directorio.exists())
				return ImageIO.read(directorio);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	// lee la imagen entrante y la devuelve como BufferedImage
	public static InputStream leerImagen(String ruta) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			BufferedImage bi = leerImagen(null, ruta);
			if (bi != null)
				ImageIO.write(bi, "png", os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean moverArchivo(String archivo, String archivoFinal) {
		File a = new File(archivo);
		return a.renameTo(new File(archivoFinal));
	}

	// redimenciona a la imagen
	private static BufferedImage redimencionarImagen(BufferedImage imagen,
			int ancho, int alto) {
		Image img = imagen.getScaledInstance(ancho, alto,
				Image.SCALE_AREA_AVERAGING);
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		bufferedImage.getGraphics().drawImage(img, 0, 0, null);
		return bufferedImage;
	}

}
