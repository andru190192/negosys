package ec.com.redepronik.negosys.utils;

import java.util.regex.Pattern;

import ec.com.redepronik.negosys.invfac.entity.Grupo;

public class Utils {

	public static String checksumEan13(String codigo) {
		codigo = codigo.substring(0, codigo.length() - 1);
		int numImp = 0;
		int numPar = 0;
		int sumTot = 0;
		String codAux = invertirPalabra(codigo);
		for (int i = 1; i <= codAux.length(); i++)
			if (i % 2 == 0 && i != 0)
				numPar += convertir(codAux.charAt(i - 1));
			else
				numImp += convertir(codAux.charAt(i - 1));
		sumTot = (numImp * 3) + numPar;
		String aux = invertirPalabra(String.valueOf(sumTot));
		int decenaSuperior = sumTot + (10 - convertir(aux.charAt(0)));
		int checksum = decenaSuperior - sumTot;
		if (checksum == 10)
			checksum = 0;
		return String.valueOf(checksum);
	}

	public static boolean comprobarCedula(String cedula) {
		if (cedula.compareTo("2222222222") == 0
				|| cedula.compareTo("4444444444") == 0
				|| cedula.compareTo("5555555555") == 0
				|| cedula.compareTo("7777777777") == 0
				|| cedula.compareTo("9999999999") == 0
				|| cedula.compareTo("0000000000") == 0)
			return false;

		if (cedula.length() == 10) {
			int s = 0;
			for (int i = 0; i < cedula.length(); i++) {
				int ced = Integer
						.parseInt(Character.toString(cedula.charAt(i)));
				if (i % 2 == 0) {
					String c1 = String.valueOf(ced * 2);
					s += c1.length() == 2 ? Integer.parseInt(Character
							.toString(c1.charAt(0)))
							+ Integer
									.parseInt(Character.toString(c1.charAt(1)))
							: Integer.parseInt(c1);
				} else
					s += ced;
			}
			return s % 10 == 0 ? true : false;
		}
		return cedula.length() == 13 ? true : false;
	}

	public static boolean comprobarEan13(String codigo) {
		if (!validaNumeros(codigo) && codigo.length() == 13)
			return codigo.substring(codigo.length() - 1, codigo.length())
					.compareToIgnoreCase(checksumEan13(codigo)) == 0 ? true
					: false;
		return false;
	}

	public static int convertir(char c) {
		return Integer.parseInt(String.valueOf(c));
	}

	public static boolean expRegular(String palabra, String exp) {
		return Pattern.compile(exp).matcher(palabra).matches() ? true : false;
	}

	public static String generarEan13(Grupo grupo) {
		String cod = "20" + String.format("%03d", grupo.getId())
				+ String.format("%07d", grupo.getContador() + 1);
		int numImp = 0;
		int numPar = 0;
		int sumTot = 0;
		String codAux = invertirPalabra(cod);
		for (int i = 1; i <= codAux.length(); i++)
			if (i % 2 == 0 && i != 0)
				numPar += convertir(codAux.charAt(i - 1));
			else
				numImp += convertir(codAux.charAt(i - 1));
		sumTot = (numImp * 3) + numPar;
		String aux = invertirPalabra(String.valueOf(sumTot));
		int decenaSuperior = sumTot + (10 - convertir(aux.charAt(0)));
		int checksum = decenaSuperior - sumTot;
		if (checksum == 10)
			checksum = 0;
		return cod = cod + checksum;
	}

	public static String invertirPalabra(String palabra) {
		return new StringBuilder(palabra).reverse().toString();
	}

	public static String insertarPalabra(int pos, String palabra, String element) {
		String nuevaPalabra = "";
		for (int i = 0; i < palabra.length(); i++)
			nuevaPalabra += (i == pos) ? palabra.charAt(i) + element : palabra
					.charAt(i);
		return nuevaPalabra;
	}

	public static String subString(String palabra, int fin) {
		palabra = invertirPalabra(palabra);
		return invertirPalabra(palabra.substring(0, fin));
	}

	public static String subString(String palabra, String subS) {
		palabra = invertirPalabra(palabra);
		return invertirPalabra(palabra.substring(subS.length(),
				palabra.length()));
	}

	public static boolean validaNumeros(String palabra) {
		for (int i = 0; i < palabra.length(); i++)
			if (Character.isLetter(palabra.charAt(i)))
				return true;
		return false;
	}
}
