package ec.com.redepronik.negosys.invfac.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ec.com.redepronik.negosys.invfac.entityAux.Anio;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.invfac.entityAux.DocumentoElectronico;
import ec.com.redepronik.negosys.invfac.entityAux.Mes;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.FacturaReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.NotaCreditoReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.RetencionReporteRIDE;

public interface DocumentosElectronicosService {

	public List<DocumentoElectronico> documentosAutorizados(String cedulaRuc,
			Mes mes, Anio anio);

	public List<DocumentoElectronico> documentosAutorizadosNoSubidos();

	public List<DocumentoElectronico> documentosFirmados();

	public List<DocumentoElectronico> documentosGenerados();

	public List<DocumentoElectronico> documentosTransmitidosSinRespuesta();

	@Transactional
	public void enviarAutorizarXML(boolean presentaMensaje,
			DocumentoElectronico documentoElectronico);

	@Transactional
	public String generarRetencionXML(int retencionId);

	@Transactional
	public void autorizarXML(boolean presentaMensaje, boolean normal,
			DocumentoElectronico docElec);

	@Transactional
	public void firmarXML(boolean presentaMensaje, DocumentoElectronico docElec);

	@Transactional
	public String generarFacturaXML(int facturaId, CantidadFactura cf);

	@Transactional
	public void subirComprobantesElectronicos(boolean presentaMensaje,
			DocumentoElectronico docElec);

	@Transactional
	public String generarNotaCreditoXML(int notaCreditoId, CantidadFactura cf);

	public List<FacturaReporteRIDE> imprimirRideFactura(String xml);

	public void generarRIDE(DocumentoElectronico documentoElectronico);

	public void envioCorreo(boolean presentaMensaje,
			DocumentoElectronico documentoElectronico, Map<String, Object> map);

	public List<NotaCreditoReporteRIDE> imprimirRideNotaCredito(String xml);

	public List<RetencionReporteRIDE> imprimirRideRetencion(String xml);

	public void cambiarConsumidorFinal();

}
