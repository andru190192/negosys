package ec.com.redepronik.negosys.utils.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;

public interface ReporteService {

	public <T> void generarReportePDF(List<T> listaReporte,
			Map<String, Object> parametros, String nombreReporte);

	public <T> void generarReportePDFSencillo(List<T> listaReporte,
			Map<String, Object> parametros, String nombreReporte);

	public <T> void generarReporteRIDE(List<T> listaReporte,
			String nombreReporte, InfoTributaria infoTributaria);

	public <T> File generarReporteRIDECorreo(List<T> listaReporte,
			String nombreReporte, InfoTributaria infoTributaria);

	public <T> void generarReporteXLS(List<T> listaReporte,
			Map<String, Object> parametros, String nombreReporte);

	public <T> void generarReporteXLSSencillo(List<T> listaReporte,
			Map<String, Object> parametros, String nombreReporte);

}
