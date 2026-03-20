package src;

import java.util.List;

/**
 * Clase principal para generar la estructura base de archivos.
 */
public class GenerateInfoFiles {

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
			List<Salesmen> generatedSalesmen = salesmanTool.createSalesManInfoFile(Constants.DEFAULT_SALESMEN_COUNT);

			for (Salesmen salesman : generatedSalesmen) {
				int randomSalesCount = Constants.DEFAULT_MIN_SALES
						+ (int) (Math.random() * (Constants.DEFAULT_MAX_SALES - Constants.DEFAULT_MIN_SALES + 1));
				salesman.createSalesMenFile(randomSalesCount, salesman.getFullName(), salesman.getDocumentNumber());
			}

			System.out.println("Archivos generados correctamente en la carpeta: " + Constants.BASE_PATH);
		} catch (Exception e) {
			System.out.println("Error al generar archivos: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
