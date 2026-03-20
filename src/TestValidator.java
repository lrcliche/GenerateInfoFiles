package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Validador de consistencia para los archivos generados.
 * Verifica formatos, separadores y existencia de archivos segun la guia.
 */
public class TestValidator {

	public static void main(String[] args) {
		System.out.println("=== INICIANDO VALIDACION DE FORMATO ===");

		boolean productsOk = validateFile(Constants.BASE_PATH + File.separator + Constants.PRODUCTS_FILE, 3, "PRODUCTOS");

		boolean salesmenOk = validateFile(Constants.BASE_PATH + File.separator + Constants.SALESMEN_FILE, 4,
				"VENDEDORES");

		if (productsOk && salesmenOk) {
			System.out.println("\nEXITO: Los archivos cumplen con el formato de la guia.");
		} else {
			System.out.println("\nERROR: Se encontraron inconsistencias en los archivos.");
		}
	}

	private static boolean validateFile(String path, int expectedColumns, String label) {
		File file = new File(path);
		if (!file.exists()) {
			System.err.println("[-] " + label + ": Archivo no encontrado en " + path);
			return false;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			if (line == null) {
				System.err.println("[-] " + label + ": El archivo esta vacio.");
				return false;
			}

			String[] parts = line.split(";");
			if (parts.length != expectedColumns) {
				System.err.println("[-] " + label + ": Formato incorrecto. Se esperaban " + expectedColumns
						+ " columnas separadas por ';'");
				System.err.println("    Linea ejemplo: " + line);
				return false;
			}

			System.out.println("[+] " + label + ": Formato validado correctamente (" + expectedColumns + " columnas).");
			return true;
		} catch (IOException e) {
			System.err.println("[-] " + label + ": Error al leer el archivo.");
			return false;
		}
	}
}
