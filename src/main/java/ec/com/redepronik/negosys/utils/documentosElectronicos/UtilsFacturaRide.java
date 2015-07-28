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
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosRIDE.FacturaReporteRIDE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.TotalConImpuestos.TotalImpuesto;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura.FacturaE;

public class UtilsFacturaRide {

	private static CantidadFactura cantidadFactura(FacturaE facturaE) {
		BigDecimal st12 = newBigDecimal();
		BigDecimal st0 = newBigDecimal();
		BigDecimal stExentoIva = newBigDecimal();
		BigDecimal stNoObjetoIva = newBigDecimal();
		BigDecimal iva12 = newBigDecimal();
		BigDecimal valorIce = newBigDecimal();
		BigDecimal valorIRBPNR = newBigDecimal();

		for (TotalImpuesto ti : facturaE.getInfoFactura()
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
		cf.setStSinImpuesto(facturaE.getInfoFactura().getTotalSinImpuestos());
		cf.setSt12(st12);
		cf.setSt0(st0);
		cf.setStNoObjetoIva(stNoObjetoIva);
		cf.setStExentoIva(stExentoIva);
		cf.settDescuento(facturaE.getInfoFactura().getTotalDescuento());
		cf.setValorIce(valorIce);
		cf.setValorIRBPNR(valorIRBPNR);
		cf.setIva12(iva12);
		cf.setPropina(facturaE.getInfoFactura().getPropina());
		cf.setValorTotal(facturaE.getInfoFactura().getImporteTotal());

		return redondearCantidadFactura(cf);
	}

	public static FacturaReporteRIDE facturaRIDE(String xml) {
		DocumentoReporteRIDE documento = documentoRIDE(convertirStringADocument(xml));

		if (documento.getEstado().equalsIgnoreCase("AUTORIZADO")) {
			try {
				FacturaE facturaE = (FacturaE) xmlTOobjeto(
						FacturaE.class,
						new ByteArrayInputStream(
								convertirString(quitarFirma(documento
										.getDocumento()))));

				FacturaReporteRIDE facturaReporte = new FacturaReporteRIDE();
				facturaReporte.setNumeroAutorizacion(documento
						.getNumeroAutorizacion());
				facturaReporte.setFechaAutorizacion(documento
						.getFechaAutorizacion());
				facturaReporte.setFactura(facturaE);
				facturaReporte.setCantidadFactura(cantidadFactura(facturaE));
				return facturaReporte;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
