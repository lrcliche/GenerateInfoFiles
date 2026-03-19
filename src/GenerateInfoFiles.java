package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal para generar la estructura base de archivos.
 */
public class GenerateInfoFiles {
    private static final List<Product> PRODUCTS = new ArrayList<Product>();
    private static final List<Salesmen> SALESMEN = new ArrayList<Salesmen>();

    /**
     * Punto de entrada de la aplicacion.
     *
     * @param args argumentos de ejecucion
     */
    public static void main(String[] args) {
        try {
            System.out.println("GenerateInfoFile... In Progress");
            FileUtil.createBaseDirectories();
            System.out.println("Archivos generados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al generar archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
