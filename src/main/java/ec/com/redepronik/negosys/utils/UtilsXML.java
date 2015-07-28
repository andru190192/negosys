package ec.com.redepronik.negosys.utils;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class UtilsXML {

	public static void guardarXML(Document document, String archivo) {
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(archivo));
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void guardarXMLFirmado(Document document, String archivo) {
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(archivo));
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T> void objetoTOxml(Class<T> clase, String ruta, Object obj) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clase);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(obj, new File(ruta));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static <T> Object xmlTOobjeto(Class<T> clase, InputStream inputStream) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clase);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return jaxbUnmarshaller.unmarshal(inputStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> Object xmlTOobjeto(Class<T> clase, String ruta) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clase);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return jaxbUnmarshaller.unmarshal(new File(ruta));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
