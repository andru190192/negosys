package ec.com.redepronik.negosys.invfac.entity;

import java.math.BigDecimal;

public enum TarifaRetencion {

	I3(ImpuestoRetencion.IV, "1", "30%", new BigDecimal("30")), I7(
			ImpuestoRetencion.IV, "2", "70%", new BigDecimal("70")), I1(
			ImpuestoRetencion.IV, "3", "100%", new BigDecimal("100")), S5(
			ImpuestoRetencion.SD, "4580", "5%", new BigDecimal("5")), R01(
			ImpuestoRetencion.RE,
			"303",
			"Honorarios profesionales y demás pagos por servicios relacionados con el título profesional",
			new BigDecimal("5")), R02(ImpuestoRetencion.RE, "303A",
			"Utilización o aprovechamiento de la imagen o renombre",
			new BigDecimal("5")), R03(
			ImpuestoRetencion.RE,
			"304",
			"Servicios predomina el intelecto no relacionados con el título profesional",
			new BigDecimal("5")), R04(
			ImpuestoRetencion.RE,
			"304A",
			"Comisiones y demás pagos por servicios predomina intelecto no relacionados con el título profesional",
			new BigDecimal("5")), R05(
			ImpuestoRetencion.RE,
			"304B",
			"Pagos a notarios y registradores de la propiedad y mercantil por sus actividades ejercidas como tales",
			new BigDecimal("5")), R06(
			ImpuestoRetencion.RE,
			"304C",
			"Pagos a deportistas, entrenadores, árbitros, miembros del cuerpo técnico por sus actividades ejercidas como tales",
			new BigDecimal("5")), R07(ImpuestoRetencion.RE, "304D",
			"Pagos a artistas por sus actividades ejercidas como tales",
			new BigDecimal("5")), R08(ImpuestoRetencion.RE, "304E",
			"Honorarios y demás pagos por servicios de docencia",
			new BigDecimal("5")), R09(ImpuestoRetencion.RE, "307",
			"Servicios predomina la mano de obra", new BigDecimal("5")), R10(
			ImpuestoRetencion.RE,
			"309",
			"Servicios prestados por medios de comunicación y agencias de publicidad",
			new BigDecimal("5")), R11(
			ImpuestoRetencion.RE,
			"310",
			"Servicio de transporte privado de pasajeros o transporte público o privado de carga",
			new BigDecimal("5")), R12(
			ImpuestoRetencion.RE,
			"311",
			"Por pagos a través de liquidación de compra (nivel cultural o rusticidad)",
			new BigDecimal("5")), R13(ImpuestoRetencion.RE, "312",
			"Transferencia de bienes muebles de naturaleza corporal",
			new BigDecimal("1")), R14(
			ImpuestoRetencion.RE,
			"312A",
			"Compra de bienes de origen agrícola, avícola, pecuario, apícola, cunícula, bioacuático, y forestal",
			new BigDecimal("5")), R15(
			ImpuestoRetencion.RE,
			"314A",
			"Regalías por concepto de franquicias de acuerdo a Ley de Propiedad Intelectual - pago a personas naturales",
			new BigDecimal("5")), R16(
			ImpuestoRetencion.RE,
			"314B",
			"Cánones, derechos de autor, marcas, patentes y similares de acuerdo a Ley de Propiedad Intelectual – pago a personas naturales",
			new BigDecimal("5")), R17(
			ImpuestoRetencion.RE,
			"314C",
			"Regalías por concepto de franquicias de acuerdo a Ley de Propiedad Intelectual - pago a sociedades",
			new BigDecimal("5")), R18(
			ImpuestoRetencion.RE,
			"314D",
			"Cánones, derechos de autor, marcas, patentes y similares de acuerdo a Ley de Propiedad Intelectual – pago a sociedades",
			new BigDecimal("5")), R19(
			ImpuestoRetencion.RE,
			"319",
			"Cuotas de arrendamiento mercantil, inclusive la de opción de compra",
			new BigDecimal("5")), R20(ImpuestoRetencion.RE, "320",
			"Por arrendamiento bienes inmuebles", new BigDecimal("5")), R21(
			ImpuestoRetencion.RE, "322",
			"Seguros y reaseguros (primas y cesiones)", new BigDecimal("5")), R22(
			ImpuestoRetencion.RE,
			"323",
			"Por rendimientos financieros pagados a naturales y sociedades (No a IFIs)",
			new BigDecimal("5")), R23(ImpuestoRetencion.RE, "323A",
			"Por RF: depósitos Cta. Corriente", new BigDecimal("5")), R24(
			ImpuestoRetencion.RE, "323B1",
			"Por RF: depósitos Cta. Ahorros Sociedades", new BigDecimal("5")), R25(
			ImpuestoRetencion.RE,
			"323D",
			"Por rendimientos financieros: compra, cancelación o redención de mini bem´s y bem´s",
			new BigDecimal("5")), R26(ImpuestoRetencion.RE, "323E",
			"Por RF: depósito a plazo fijo gravados", new BigDecimal("5")), R27(
			ImpuestoRetencion.RE, "323E2",
			"Por RF: depósito a plazo fijo exentos", new BigDecimal("5")), R28(
			ImpuestoRetencion.RE, "323F",
			"Por rendimientos financieros: operaciones de reporto - repos",
			new BigDecimal("5")), R29(
			ImpuestoRetencion.RE,
			"323G",
			"Por RF: inversiones (captaciones) rendimientos distintos de aquellos pagados a IFIs",
			new BigDecimal("5")), R30(ImpuestoRetencion.RE, "323H",
			"Por RF: obligaciones", new BigDecimal("5")), R31(
			ImpuestoRetencion.RE, "323I",
			"Por RF: bonos convertible en acciones", new BigDecimal("5")), R32(
			ImpuestoRetencion.RE,
			"323K",
			"Por RF: Intereses en operaciones de crédito entre instituciones del sistema financiero y entidades economía popular y solidaria",
			new BigDecimal("5")), R33(
			ImpuestoRetencion.RE,
			"323L",
			"Por RF: Por inversiones entre instituciones del sistema financiero y entidades economía popular y solidaria.",
			new BigDecimal("5")), R34(ImpuestoRetencion.RE, "323M",
			"Por RF: Inversiones en títulos valores en renta fija gravados",
			new BigDecimal("5")), R35(ImpuestoRetencion.RE, "323N",
			"Por RF: Inversiones en títulos valores en renta fija exentos",
			new BigDecimal("5")), R36(
			ImpuestoRetencion.RE,
			"323O",
			"Por RF: Intereses pagados a bancos y otras entidades sometidas al control de la Superintendencia de Bancos y de la Economía Popular y Solidaria",
			new BigDecimal("5")), R37(
			ImpuestoRetencion.RE,
			"323P",
			"Por RF: Intereses pagados por entidades del sector público a favor de sujetos pasivos ",
			new BigDecimal("5")), R38(ImpuestoRetencion.RE, "323Q",
			"Por RF: Otros intereses y rendimientos financieros gravados",
			new BigDecimal("5")), R39(ImpuestoRetencion.RE, "323R",
			"Por RF: Otros intereses y rendimientos financieros exentos",
			new BigDecimal("5")), R40(ImpuestoRetencion.RE, "325",
			"Por loterías, rifas, apuestas y similares", new BigDecimal("5")), R41(
			ImpuestoRetencion.RE, "327",
			"Por venta de combustibles a comercializadoras",
			new BigDecimal("5")), R42(ImpuestoRetencion.RE, "328",
			"Por venta de combustibles a distribuidores", new BigDecimal("5")), R43(
			ImpuestoRetencion.RE, "332",
			"Otras compras de bienes y servicios no sujetas a retención",
			new BigDecimal("5")), R44(
			ImpuestoRetencion.RE,
			"332A",
			"Por la enajenación ocasional de acciones o participaciones y títulos valores",
			new BigDecimal("5")), R45(ImpuestoRetencion.RE, "332B",
			"Compra de bienes inmuebles", new BigDecimal("5")), R46(
			ImpuestoRetencion.RE, "332C", "Transporte público de pasajeros",
			new BigDecimal("5")), R47(
			ImpuestoRetencion.RE,
			"332D",
			"Pagos en el país por transporte de pasajeros o transporte internacional de carga, a compañías nacionales o extranjeras de aviación o marítimas",
			new BigDecimal("5")), R48(
			ImpuestoRetencion.RE,
			"332E",
			"Valores entregados por las cooperativas de transporte a sus socios",
			new BigDecimal("5")), R49(
			ImpuestoRetencion.RE,
			"332F",
			"Compraventa de divisas distintas al dólar de los Estados Unidos de América",
			new BigDecimal("5")), R50(ImpuestoRetencion.RE, "334",
			"Pagos con tarjeta de crédito", new BigDecimal("5")), R51(
			ImpuestoRetencion.RE,
			"335",
			"Pago local tarjeta de crédito reportada por la Emisora de tarjeta de crédito, solo recap",
			new BigDecimal("5")), R52(
			ImpuestoRetencion.RE,
			"335A",
			"Pago al exterior tarjeta de crédito reportada por la Emisora de tarjeta de crédito, solo recap",
			new BigDecimal("5")), R53(ImpuestoRetencion.RE, "340A",
			"Por energía eléctrica", new BigDecimal("5")), R54(
			ImpuestoRetencion.RE,
			"340B",
			"Por actividades de construcción de obra material inmueble, urbanización, lotización o actividades similares",
			new BigDecimal("5")), R55(ImpuestoRetencion.RE, "341",
			"Otras retenciones aplicables el 2%", new BigDecimal("5")), R56(
			ImpuestoRetencion.RE, "344A", "Ganancias de capital",
			new BigDecimal("5")), R57(ImpuestoRetencion.RE, "345",
			"Dividendos personas naturales residentes en el Ecuador",
			new BigDecimal("5")), R58(
			ImpuestoRetencion.RE,
			"345A",
			"Dividendos gravados distribuidos en acciones (reinversión de utilidades sin derecho a reducción tarifa IR)",
			new BigDecimal("5")), R59(
			ImpuestoRetencion.RE,
			"345B",
			"Dividendos exentos a sociedades, nacionales o extranjeras, domiciliadas en el Ecuador",
			new BigDecimal("5")), R60(
			ImpuestoRetencion.RE,
			"345C",
			"Dividendos exentos distribuidos en acciones (reinversión de utilidades con derecho a reducción tarifa IR)",
			new BigDecimal("5")), R61(ImpuestoRetencion.RE, "347",
			"Dividendos anticipados", new BigDecimal("5")), R62(
			ImpuestoRetencion.RE,
			"347A",
			"Dividendos anticipados préstamos accionistas, beneficiarios o partìcipes",
			new BigDecimal("5")), R63(ImpuestoRetencion.RE, "348",
			"Compra local de banano a productor", new BigDecimal("5")), R64(
			ImpuestoRetencion.RE, "349",
			"Impuesto a la actividad bananera productor-exportador",
			new BigDecimal("5")), R65(ImpuestoRetencion.RE, "500",
			"Pago al exterior - Rentas Inmobiliarias", new BigDecimal("5")), R66(
			ImpuestoRetencion.RE, "501",
			"Pago al exterior - Beneficios Empresariales", new BigDecimal("5")), R67(
			ImpuestoRetencion.RE, "502",
			"Pago al exterior - Servicios Empresariales", new BigDecimal("5")), R68(
			ImpuestoRetencion.RE, "503",
			"Pago al exterior - Navegación Marítima y/o aérea", new BigDecimal(
					"5")), R69(ImpuestoRetencion.RE, "504",
			"Pago al exterior- Dividendos", new BigDecimal("5")), R70(
			ImpuestoRetencion.RE, "504A",
			"Pago al exterior - Dividendos a sociedades en paraísos fiscales",
			new BigDecimal("5")), R71(ImpuestoRetencion.RE, "504B",
			"Pago al exterior - Dividendos anticipados", new BigDecimal("5")), R72(
			ImpuestoRetencion.RE,
			"504C",
			"Pago al exterior - Dividendos anticipados préstamos accionistas, beneficiarios o partìcipes",
			new BigDecimal("5")), R73(ImpuestoRetencion.RE, "505",
			"Pago al exterior - Rendimientos financieros", new BigDecimal("5")), R74(
			ImpuestoRetencion.RE,
			"505A",
			"Pago al exterior – Intereses de créditos de Instituciones Financieras del exterior",
			new BigDecimal("5")), R75(ImpuestoRetencion.RE, "505B",
			"Pago al exterior – Intereses de créditos de gobierno a gobierno",
			new BigDecimal("5")), R76(
			ImpuestoRetencion.RE,
			"505C",
			"Pago al exterior – Intereses de créditos de organismos multilaterales",
			new BigDecimal("5")), R77(
			ImpuestoRetencion.RE,
			"505D",
			"Pago al exterior - Intereses por financiamiento de proveedores externos",
			new BigDecimal("5")), R78(ImpuestoRetencion.RE, "505E",
			"Pago al exterior - Intereses de otros créditos externos",
			new BigDecimal("5")), R79(ImpuestoRetencion.RE, "505F",
			"Pago al exterior - Otros Intereses y Rendimientos Financieros",
			new BigDecimal("5")), R80(
			ImpuestoRetencion.RE,
			"509",
			"Pago al exterior - Cánones, derechos de autor, marcas, patentes y similares",
			new BigDecimal("5")), R81(ImpuestoRetencion.RE, "509A",
			"Pago al exterior - Regalías por concepto de franquicias",
			new BigDecimal("5")), R82(ImpuestoRetencion.RE, "510",
			"Pago al exterior - Ganancias de capital", new BigDecimal("5")), R83(
			ImpuestoRetencion.RE, "511",
			"Pago al exterior - Servicios profesionales independientes",
			new BigDecimal("5")), R84(ImpuestoRetencion.RE, "512",
			"Pago al exterior - Servicios profesionales dependientes",
			new BigDecimal("5")), R85(ImpuestoRetencion.RE, "513",
			"Pago al exterior - Artistas", new BigDecimal("5")), R86(
			ImpuestoRetencion.RE, "513A", "Pago al exterior - Deportistas",
			new BigDecimal("5")), R87(ImpuestoRetencion.RE, "514",
			"Pago al exterior - Participación de consejeros", new BigDecimal(
					"5")), R88(ImpuestoRetencion.RE, "515",
			"Pago al exterior - Entretenimiento Público", new BigDecimal("5")), R89(
			ImpuestoRetencion.RE, "516", "Pago al exterior - Pensiones",
			new BigDecimal("5")), R90(ImpuestoRetencion.RE, "517",
			"Pago al exterior - Reembolso de Gastos", new BigDecimal("5")), R91(
			ImpuestoRetencion.RE, "518",
			"Pago al exterior - Funciones Públicas", new BigDecimal("5")), R92(
			ImpuestoRetencion.RE, "519", "Pago al exterior - Estudiantes",
			new BigDecimal("5")), R93(ImpuestoRetencion.RE, "520",
			"Pago al exterior - Por otros conceptos", new BigDecimal("5")), R94(
			ImpuestoRetencion.RE,
			"520A",
			"Pago al exterior - Pago a proveedores de servicios hoteleros y turísticos en el exterior",
			new BigDecimal("5")), R95(ImpuestoRetencion.RE, "520B",
			"Pago al exterior - Arrendamientos mercantil internacional",
			new BigDecimal("5")), R96(ImpuestoRetencion.RE, "520C",
			"Pago al exterior - Seguros, cesiones y reaseguros",
			new BigDecimal("5")), R97(
			ImpuestoRetencion.RE,
			"520D",
			"Pago al exterior - Comisiones por exportaciones y por promoción de turismo receptivo",
			new BigDecimal("5")), R98(
			ImpuestoRetencion.RE,
			"520E",
			"Pago al exterior - Por las empresas de transporte marítimo o aéreo y por empresas pesqueras de alta mar, por su actividad.",
			new BigDecimal("5")), R99(ImpuestoRetencion.RE, "520F",
			"Pago al exterior - Por las agencias internacionales de prensa",
			new BigDecimal("5")), R100(
			ImpuestoRetencion.RE,
			"520G",
			"Pago al exterior - Contratos de fletamento de naves para empresas de transporte aéreo o marítimo internacional",
			new BigDecimal("5")), R101(ImpuestoRetencion.RE, "521",
			"Pago al exterior - No sujetos a retencion ", new BigDecimal("5"));

	private final String id;
	private final String nombre;
	private final BigDecimal porcentaje;
	private final ImpuestoRetencion impuestoRetencion;
	private static TarifaRetencion[] listas;

	private TarifaRetencion(ImpuestoRetencion impuestoRetencion, String id,
			String nombre, BigDecimal porcentaje) {
		this.id = id;
		this.nombre = nombre;
		this.porcentaje = porcentaje;
		this.impuestoRetencion = impuestoRetencion;
	}

	public static TarifaRetencion[] obtenerPorImpuesto(
			ImpuestoRetencion impuestoRetencion) {
		listas = new TarifaRetencion[TarifaRetencion.values().length];
		int c = 0;
		for (TarifaRetencion tr : TarifaRetencion.values()) {
			if (tr.getImpuestoRetencion() == impuestoRetencion) {
				listas[c] = tr;
				c++;
			}
		}
		TarifaRetencion listaFinal[] = new TarifaRetencion[c];
		for (int i = 0; i < listas.length; i++) {
			if (listas[i] != null) {
				listaFinal[i] = listas[i];
			}
		}
		return listaFinal;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public ImpuestoRetencion getImpuestoRetencion() {
		return impuestoRetencion;
	}

}