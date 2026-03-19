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
			System.out.println("GenerateInfoFiles... In Progress");

			// 1. Crear estructura de carpetas
			FileUtil.createBaseDirectories();

			// 2. Generar archivo de productos
			Product productTool = new Product("0", "Default", 0.0);
			productTool.createProductsFile(Constants.DEFAULT_PRODUCTS_COUNT);

			// 3. Generar archivo de información de vendedores
			Salesmen salesmanTool = new Salesmen("CC", 0, "Default", "Default");
			salesmanTool.createSalesManInfoFile(Constants.DEFAULT_SALESMEN_COUNT);

			System.out.println("Archivos generados correctamente en la carpeta: " + Constants.BASE_PATH);
		} catch (Exception e) {
			System.out.println("Error al generar archivos: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
