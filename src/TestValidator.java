package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Validador de consistencia para los archivos generados.
 * Verifica formatos, separadores y existencia de archivos según la guía.
 */
public class TestValidator {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO VALIDACION DE FORMATO ===");
        
        boolean productsOk = validateFile(
            Constants.BASE_PATH + File.separator + "products_info.txt", 
            3, // ID;Nombre;Precio
            "PRODUCTOS"
        );

        boolean salesmenOk = validateFile(
            Constants.BASE_PATH + File.separator + "salesmen_info.txt", 
            4, // Tipo;Doc;Nombre;Apellido
            "VENDEDORES"
        );

        if (productsOk && salesmenOk) {
            System.out.println("\n✅ EXITO: Los archivos cumplen con el formato de la guia.");
        } else {
            System.out.println("\n❌ ERROR: Se encontraron inconsistencias en los archivos.");
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

            // Validar que el separador sea ";" y no ","
            String[] parts = line.split(";");
            if (parts.length != expectedColumns) {
                System.err.println("[-] " + label + ": Formato incorrecto. Se esperaban " + expectedColumns + " columnas separadas por ';'");
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
