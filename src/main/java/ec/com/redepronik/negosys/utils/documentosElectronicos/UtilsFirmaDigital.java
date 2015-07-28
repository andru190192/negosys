package ec.com.redepronik.negosys.utils.documentosElectronicos;

import static ec.com.redepronik.negosys.utils.UtilsArchivos.getRutaFirmaDigital;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.convertirStringADocument;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.w3c.dom.Document;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.EnumFormatoFirma;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;

public class UtilsFirmaDigital {

	private static DataToSign configuracionFirma() {
		DataToSign confiFirma = new DataToSign();
		confiFirma.setXadesFormat(EnumFormatoFirma.XAdES_BES);
		confiFirma.setEsquema(XAdESSchemas.XAdES_132);
		confiFirma.setXMLEncoding("UTF-8");
		confiFirma.setEnveloped(true);
		confiFirma.setDocument(convertirStringADocument(xml));
		confiFirma.addObject(new ObjectToSign(new InternObjectToSign(
				"comprobante"),
				"CON EL APOYO TECNOLOGICO DE REDEPRONIK SYSTEM", null,
				"text/xml", null));
		confiFirma.setParentSignNode("comprobante");
		return confiFirma;
	}

	private static Document crearFirma() {
		KeyStore keyStore = getKeyStore();
		String alias = getAlias(keyStore);

		X509Certificate certificate = null;
		try {
			certificate = (X509Certificate) keyStore.getCertificate(alias);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrivateKey privateKey = null;
		KeyStore tmpKs = keyStore;
		try {
			privateKey = (PrivateKey) tmpKs.getKey(alias,
					claveFirma.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Provider provider = keyStore.getProvider();
		DataToSign confiFirma = configuracionFirma();
		Document docFirmado = null;
		try {
			FirmaXML firma = new FirmaXML();
			Object[] res = firma.signFile(certificate, confiFirma, privateKey,
					provider);
			docFirmado = (Document) res[0];
		} catch (Exception ex) {
			System.err.println("Error realizando la firma");
			ex.printStackTrace();
		}
		return docFirmado;
	}

	public static Document firmar(String claveFirma, String xml) {
		UtilsFirmaDigital.claveFirma = claveFirma;
		UtilsFirmaDigital.xml = xml;
		return crearFirma();
	}

	private static String getAlias(KeyStore keyStore) {
		try {
			Enumeration<String> nombres = keyStore.aliases();
			while (nombres.hasMoreElements()) {
				String alias = nombres.nextElement();
				if (keyStore.isKeyEntry(alias))
					return alias;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static KeyStore getKeyStore() {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream(getRutaFirmaDigital()),
					claveFirma.toCharArray());
			return ks;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String claveFirma;

	private static String xml;

}
