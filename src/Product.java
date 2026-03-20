package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Representa un producto con su informacion basica.
 */
public class Product {

	private String id;
	private String name;
	private double price;

	public Product(String id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	/**
	 * Genera el archivo de productos con formato:
	 * IDProducto;NombreProducto;PrecioPorUnidad
	 *
	 * @param productsCount cantidad de productos a generar
	 */
	public void createProductsFile(int productsCount) {
		String fileName = Constants.BASE_PATH + java.io.File.separator + Constants.PRODUCTS_FILE;

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (int i = 1; i <= productsCount; i++) {
				double pricePerUnit = 10 + (Math.random() * 100);
				String line = String.format("%d;Product_%d;%.2f", i, i, pricePerUnit);

				writer.write(line);
				writer.newLine();
			}

			System.out.println("Process completed successfully: '" + fileName + "' generated.");
		} catch (IOException e) {
			System.err.println("An error occurred while creating the products file: " + e.getMessage());
		}
	}
}
