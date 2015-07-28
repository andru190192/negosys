package ec.com.redepronik.negosys.utils;

import static ec.com.redepronik.negosys.utils.UtilsDate.diasMora;

import java.math.BigDecimal;
import java.util.Date;

import ec.com.redepronik.negosys.invfac.entity.Parametro;

public class UtilsMath {

	public static void actualizar(Parametro parametro) {
		UtilsMath.parametro = parametro;
	}

	public static int compareTo(BigDecimal valor, BigDecimal valor1) {
		return valor.compareTo(valor1);
	}

	public static int compareTo(BigDecimal valor, Integer valor1) {
		return compareTo(valor, new BigDecimal(valor1));
	}

	public static int compareTo(BigDecimal valor, Long valor1) {
		return compareTo(valor, new BigDecimal(valor1));
	}

	public static int compareTo(BigDecimal valor, String valor1) {
		return compareTo(valor, new BigDecimal(valor1));
	}

	public static BigDecimal divide(BigDecimal divisor, BigDecimal dividendo) {
		try {
			return divisor.divide(dividendo, parametro.getPrecisionDecimal(),
					parametro.getTipoRedondeo());
		} catch (ArithmeticException e) {
			return newBigDecimal();
		}
	}

	public static BigDecimal divide(BigDecimal divisor, Integer dividendo) {
		return divide(divisor, new BigDecimal(dividendo));
	}

	public static BigDecimal divide(BigDecimal divisor, Long dividendo) {
		return divide(divisor, new BigDecimal(dividendo));
	}

	public static BigDecimal divide(BigDecimal divisor, String dividendo) {
		return divide(divisor, new BigDecimal(dividendo));
	}

	public static BigDecimal divide(Integer divisor, Integer dividendo) {
		return divide(new BigDecimal(divisor), dividendo);
	}

	// devuelve el iva de un valor dado
	public static BigDecimal iva(BigDecimal valor, BigDecimal porcentaje) {
		return multiplicarDivide(valor, porcentaje);
	}

	// devuelve el iva de un valor dado
	public static BigDecimal iva(BigDecimal valor, int porcentaje) {
		return multiplicarDivide(valor, porcentaje);
	}

	public static BigDecimal moraTotal(Date fechaPago, Date fechaMora,
			BigDecimal porcentajeMora) {
		return multiplicar(divide(porcentajeMora, "30"),
				diasMora(fechaPago, fechaMora));
	}

	public static BigDecimal multiplicar(BigDecimal multiplicando,
			BigDecimal multiplicador) {
		return redondear(multiplicando.multiply(multiplicador));
	}

	public static BigDecimal multiplicar(BigDecimal multiplicando,
			Integer multiplicador) {
		return multiplicar(multiplicando, new BigDecimal(multiplicador));
	}

	public static BigDecimal multiplicar(BigDecimal multiplicando,
			Long multiplicador) {
		return multiplicar(multiplicando, new BigDecimal(multiplicador));
	}

	public static BigDecimal multiplicar(BigDecimal multiplicando,
			String multiplicador) {
		return multiplicar(multiplicando, new BigDecimal(multiplicador));
	}

	public static BigDecimal multiplicarDivide(BigDecimal multiplicando,
			BigDecimal multiplicador) {
		return divide(multiplicar(multiplicando, multiplicador), porcentaje);
	}

	public static BigDecimal multiplicarDivide(BigDecimal multiplicando,
			BigDecimal multiplicador, Integer divisor) {
		return divide(multiplicar(multiplicando, multiplicador), divisor);
	}

	public static BigDecimal multiplicarDivide(BigDecimal multiplicando,
			Integer multiplicador) {
		return multiplicarDivide(multiplicando, new BigDecimal(multiplicador));
	}

	public static BigDecimal multiplicarDivide(BigDecimal multiplicando,
			Integer multiplicador, BigDecimal divisor) {
		return divide(multiplicar(multiplicando, multiplicador), divisor);
	}

	public static BigDecimal multiplicarDivide(BigDecimal multiplicando,
			Long multiplicador) {
		return multiplicarDivide(multiplicando, new BigDecimal(multiplicador));
	}

	public static BigDecimal multiplicarDivide(BigDecimal multiplicando,
			String multiplicador) {
		return multiplicarDivide(multiplicando, new BigDecimal(multiplicador));
	}

	public static BigDecimal newBigDecimal() {
		return redondear("0.00");
	}

	public static BigDecimal newBigDecimal(Integer valor) {
		return redondear(valor);
	}

	public static BigDecimal newBigDecimal(String valor) {
		return redondear(valor);
	}

	public static BigDecimal parse(Object object) {
		return redondear(String.valueOf(object));
	}

	public static BigDecimal redondear(BigDecimal numero) {
		return numero.setScale(parametro.getPrecisionDecimal(),
				parametro.getTipoRedondeo());
	}

	public static BigDecimal redondear(Integer numero) {
		return redondear(new BigDecimal(numero));
	}

	public static BigDecimal redondear(Long numero) {
		return redondear(new BigDecimal(numero));
	}

	public static BigDecimal redondear(String numero) {
		return redondear(new BigDecimal(numero));
	}

	public static BigDecimal redondearEscala(BigDecimal numero, int escala) {
		return numero.setScale(escala, parametro.getTipoRedondeo());
	}

	public static BigDecimal redondearTotales(BigDecimal numero) {
				return redondearEscala(numero, 2);
	}

	public static String redondearTotalS(BigDecimal numero) {
		return String.valueOf(redondearEscala(numero, 2));
	}

	public static String redondearTotalS(String numero) {
		return String.valueOf(redondearEscala(new BigDecimal(numero), 2));
	}

	public static String string(BigDecimal numero) {
		return String.valueOf(numero);
	}

	// devuelve el valor sumandole el iva
	public static BigDecimal valorConIva(BigDecimal valor) {
		return multiplicar(valor, "1.12");
	}

	// devuelve la suma del valor mas su porcentaje
	public static BigDecimal valorConPorcentaje(BigDecimal valor,
			BigDecimal porcentaje) {
		return valor.add(multiplicarDivide(valor, porcentaje));
	}

	// devuelve el valor quitandole el iva
	public static BigDecimal valorSinIva(BigDecimal valor) {
		return divide(valor, "1.12");
	}

	public static Parametro parametro;

	private static final BigDecimal porcentaje = new BigDecimal("100");
}
