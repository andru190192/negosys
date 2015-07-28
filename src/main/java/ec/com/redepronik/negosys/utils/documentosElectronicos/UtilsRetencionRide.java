package ec.com.redepronik.negosys.utils.documentosElectronicos;

import static ec.com.redepronik.negosys.utils.UtilsArchivos.convertirString;
import static ec.com.redepronik.negosys.utils.UtilsXML.xmlTOobjeto;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.convertirStringADocument;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.documentoRIDE;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.quitarFirma;

import java.io.ByteArrayInputStream;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.DocumentoReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.RetencionReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.ComprobanteRetencionE;

public class UtilsRetencionRide {

	public static RetencionReporteRIDE retencionRIDE(String xml) {
		DocumentoReporteRIDE documento = documentoRIDE(convertirStringADocument(xml));

		if (documento.getEstado().equalsIgnoreCase("AUTORIZADO")) {
			try {
				ComprobanteRetencionE comprobanteRetencionE = (ComprobanteRetencionE) xmlTOobjeto(
						ComprobanteRetencionE.class,
						new ByteArrayInputStream(
								convertirString(quitarFirma(documento
										.getDocumento()))));

				RetencionReporteRIDE retencionReporte = new RetencionReporteRIDE();
				retencionReporte.setNumeroAutorizacion(documento
						.getNumeroAutorizacion());
				retencionReporte.setFechaAutorizacion(documento
						.getFechaAutorizacion());
				retencionReporte.setRetencion(comprobanteRetencionE);
				return retencionReporte;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
