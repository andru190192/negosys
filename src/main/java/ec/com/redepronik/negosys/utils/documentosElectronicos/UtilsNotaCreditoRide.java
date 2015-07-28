package ec.com.redepronik.negosys.utils.documentosElectronicos;

import static ec.com.redepronik.negosys.utils.UtilsArchivos.convertirString;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsXML.xmlTOobjeto;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.convertirStringADocument;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.documentoRIDE;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.quitarFirma;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.redondearCantidadFactura;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import ec.com.redepronik.negosys.invfac.entity.Impuesto;
import ec.com.redepronik.negosys.invfac.entityAux.CantidadFactura;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.DocumentoReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.NotaCreditoReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.TotalConImpuestos.TotalImpuesto;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.notaCredito.NotaCreditoE;

public class UtilsNotaCreditoRide {

	private static CantidadFactura cantidadFactura(NotaCreditoE notaCreditoE) {
		BigDecimal st12 = newBigDecimal();
		BigDecimal st0 = newBigDecimal();
		BigDecimal stExentoIva = newBigDecimal();
		BigDecimal stNoObjetoIva = newBigDecimal();
		BigDecimal iva12 = newBigDecimal();
		BigDecimal valorIce = newBigDecimal();
		BigDecimal valorIRBPNR = newBigDecimal();

		for (TotalImpuesto ti : notaCreditoE.getInfoNotaCredito()
				.getTotalConImpuestos().getTotalImpuesto()) {
			int cod = Integer.parseInt(ti.getCodigo());
			if (cod == Impuesto.IV.getId()) {
				if (ti.getCodigoPorcentaje().equalsIgnoreCase("0"))
					st0 = st0.add(ti.getBaseImponible());
				else if (ti.getCodigoPorcentaje().equalsIgnoreCase("2")) {
					st12 = st12.add(ti.getBaseImponible());
					iva12 = iva12.add(ti.getValor());
				} else if (ti.getCodigoPorcentaje().equalsIgnoreCase("6"))
					stNoObjetoIva = stNoObjetoIva.add(ti.getBaseImponible());
				else if (ti.getCodigoPorcentaje().equalsIgnoreCase("7"))
					stExentoIva = stExentoIva.add(ti.getBaseImponible());
			} else if (cod == Impuesto.IC.getId())
				valorIce = valorIce.add(ti.getValor());
			else if (cod == Impuesto.IR.getId())
				valorIRBPNR = valorIRBPNR.add(ti.getValor());
		}

		CantidadFactura cf = new CantidadFactura();
		cf.setStSinImpuesto(notaCreditoE.getInfoNotaCredito()
				.getTotalSinImpuestos());
		cf.setSt12(st12);
		cf.setSt0(st0);
		cf.setStNoObjetoIva(stNoObjetoIva);
		cf.setStExentoIva(stExentoIva);
		cf.setValorIce(valorIce);
		cf.setValorIRBPNR(valorIRBPNR);
		cf.setIva12(iva12);
		cf.setValorTotal(notaCreditoE.getInfoNotaCredito()
				.getValorModificacion());

		return redondearCantidadFactura(cf);
	}

	public static NotaCreditoReporteRIDE notaCreditoRIDE(String xml) {
		DocumentoReporteRIDE documento = documentoRIDE(convertirStringADocument(xml));

		if (documento.getEstado().equalsIgnoreCase("AUTORIZADO")) {
			try {
				NotaCreditoE notaCreditoE = (NotaCreditoE) xmlTOobjeto(
						NotaCreditoE.class,
						new ByteArrayInputStream(
								convertirString(quitarFirma(documento
										.getDocumento()))));

				NotaCreditoReporteRIDE notaCreditoReporte = new NotaCreditoReporteRIDE();
				notaCreditoReporte.setNumeroAutorizacion(documento
						.getNumeroAutorizacion());
				notaCreditoReporte.setFechaAutorizacion(documento
						.getFechaAutorizacion());
				notaCreditoReporte.setNotaCredito(notaCreditoE);
				notaCreditoReporte
						.setCantidadFactura(cantidadFactura(notaCreditoE));
				return notaCreditoReporte;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
