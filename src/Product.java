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

	/**
	 * Crea un producto con sus datos principales.
	 *
	 * Generates a text file containing a specified number of random product
	 * records. Each record includes a sequential ID, a generic name, and a random
	 * price.
	 *
	 * @param productsCount The total number of product entries to be written to the
	 *                      file.
	 *
	 * @param id            identificador del producto
	 * @param name          nombre del producto
	 * @param price         precio del producto
	 */
	public Product(String id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	/**
	 * Obtiene el identificador del producto.
	 *
	 * @return id del producto
	 */
	public String getId() {
		return id;
	}

	/**
	 * Obtiene el nombre del producto.
	 *
	 * @return nombre del producto
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtiene el precio del producto.
	 *
	 * @return precio del producto
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Entity representing a product and its file generation utilities. This class
	 * handles the creation of pseudo-random data for testing purposes.
	 */

	/**
	 * Creates a text file with pseudo-random product information. The format of
	 * each line is: ID Product; Name Product; Price Per Unit. * @param
	 * productsCount The number of products to be generated in the file[cite: 42].
	 */
	public void createProductsFile(int productsCount) {

		String fileName = "products_info.txt";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (int i = 1; i <= productsCount; i++) {
				/**
				 * Generación de precio aleatorio entre 10.0 y 110.0
				 */
				double pricePerUnit = 10 + (Math.random() * 100);

				/**
				 * Formato requerido: ID;Nombre;Precio Usamos String.format para asegurar que el
				 * precio tenga un formato decimal limpio
				 */
				String line = String.format("%d;Product_%d;%.2f", i, i, pricePerUnit);

				writer.write(line);
				writer.newLine();
			}
			/**
			 * El programa debe mostrar un mensaje de finalización exitosa
			 * 
			 */
			System.out.println("Process completed successfully: '" + fileName + "' generated.");

		} catch (IOException e) {
			/**
			 * El programa debe mostrar un mensaje de error si algo sale mal
			 * 
			 */
			System.err.println("An error occurred while creating the products file: " + e.getMessage());
		}
	}
}
