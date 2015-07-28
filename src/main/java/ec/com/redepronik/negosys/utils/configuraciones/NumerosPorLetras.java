package ec.com.redepronik.negosys.utils.configuraciones;

import java.math.BigDecimal;

public class NumerosPorLetras {

	private static String convertNumber(int number) {
		String num = String.valueOf(number);

		if (num.length() > 3)
			throw new NumberFormatException(
					"La longitud maxima debe ser 3 digitos");
		// Caso especial con el 100
		if (num.equals("100"))
			return "CIEN";

		StringBuilder output = new StringBuilder();
		if (getDigitAt(num, 2) != 0)
			output.append(CENTENAS[getDigitAt(num, 2) - 1]);

		int k = Integer.parseInt(String.valueOf(getDigitAt(num, 1))
				+ String.valueOf(getDigitAt(num, 0)));

		if (k <= 20)
			output.append(UNIDADES[k]);
		else if (k > 30 && getDigitAt(num, 0) != 0)
			output.append(DECENAS[getDigitAt(num, 1) - 2] + "Y "
					+ UNIDADES[getDigitAt(num, 0)]);
		else
			output.append(DECENAS[getDigitAt(num, 1) - 2]
					+ UNIDADES[getDigitAt(num, 0)]);

		return output.toString();
	}

	public static String convertNumberToLetter(BigDecimal number)
			throws NumberFormatException {

		StringBuilder converted = new StringBuilder();

		// formateamos el numero, para ajustarlo a el formato de dos puntos
		// decimales
		number = number.setScale(2, 6);

		// Validamos que sea un numero legal
		if (number.compareTo(new BigDecimal("999999999")) > 0)
			throw new NumberFormatException(
					"El numero es mayor de 999'999.999, "
							+ "no es posible convertirlo");

		if (number.compareTo(new BigDecimal("0")) < 0)
			throw new NumberFormatException("El numero debe ser positivo");

		String splitNumber[] = String.valueOf(number).replace('.', '#')
				.split("#");

		// Descompone el trio de millones
		int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				8))
				+ String.valueOf(getDigitAt(splitNumber[0], 7))
				+ String.valueOf(getDigitAt(splitNumber[0], 6)));
		if (millon == 1)
			converted.append("UN MILLON ");
		else if (millon > 1)
			converted.append(convertNumber(millon) + "MILLONES ");

		// Descompone el trio de miles
		int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				5))
				+ String.valueOf(getDigitAt(splitNumber[0], 4))
				+ String.valueOf(getDigitAt(splitNumber[0], 3)));
		if (miles == 1)
			converted.append("MIL ");
		else if (miles > 1)
			converted.append(convertNumber(miles) + "MIL ");

		// Descompone el ultimo trio de unidades
		int cientos = Integer.parseInt(String.valueOf(getDigitAt(
				splitNumber[0], 2))
				+ String.valueOf(getDigitAt(splitNumber[0], 1))
				+ String.valueOf(getDigitAt(splitNumber[0], 0)));
		if (cientos == 1)
			converted.append("UN");

		if (millon + miles + cientos == 0)
			converted.append("CERO");
		if (cientos > 1)
			converted.append(convertNumber(cientos));

		converted.append("DOLARES");

		// Descompone los centavos
		int centavos = Integer.parseInt(String.valueOf(getDigitAt(
				splitNumber[1], 2))
				+ String.valueOf(getDigitAt(splitNumber[1], 1))
				+ String.valueOf(getDigitAt(splitNumber[1], 0)));
		if (centavos == 1)
			converted.append(" CON UN CENTAVO");
		else if (centavos > 1)
			converted.append(" CON " + convertNumber(centavos) + "CENTAVOS");

		return converted.toString();
	}

	private static int getDigitAt(String origin, int position) {
		if (origin.length() > position && position >= 0)
			return origin.charAt(origin.length() - position - 1) - 48;
		return 0;
	}

	private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ",
			"CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
			"ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
			"DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE" };

	private static final String[] DECENAS = { "VENTI", "TREINTA ", "CUARENTA ",
			"CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
			"CIEN " };

	private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ",
			"TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
			"SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };

}