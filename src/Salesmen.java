package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representa un vendedor del sistema.
 */
public class Salesmen {

	private String documentType;
	private long documentNumber;
	private String firstName;
	private String lastName;

	/**
	 * Crea un vendedor con sus datos basicos.
	 *
	 * @param documentType   tipo de documento
	 * @param documentNumber numero de documento
	 * @param firstName      nombre del vendedor
	 * @param lastName       apellido del vendedor
	 */
	public Salesmen(String documentType, long documentNumber, String firstName, String lastName) {
		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Obtiene el tipo de documento.
	 *
	 * @return tipo de documento
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * Obtiene el numero de documento.
	 *
	 * @return numero de documento
	 */
	public long getDocumentNumber() {
		return documentNumber;
	}

	/**
	 * Obtiene el nombre del vendedor.
	 *
	 * @return nombre del vendedor
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Obtiene el apellido del vendedor.
	 *
	 * @return apellido del vendedor
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Obtiene el nombre completo en un solo texto.
	 *
	 * @return nombre completo del vendedor
	 */
	public String getFullName() {
		return firstName + "_" + lastName;
	}

	/**
	 * Genera el archivo plano de vendedores y retorna la lista generada para poder
	 * crear luego los archivos de ventas.
	 *
	 * @param salesmanCount cantidad de vendedores a generar
	 * @return lista de vendedores generados
	 */
	public List<Salesmen> createSalesManInfoFile(int salesmanCount) {
		String fileName = Constants.BASE_PATH + java.io.File.separator + Constants.SALESMEN_FILE;
		Random random = new Random();
		List<Salesmen> generatedSalesmen = new ArrayList<Salesmen>();

		String[] firstNames = { "Andres", "Alanis", "John", "Carolina", "Jose", "Maria", "Luis", "Fernanda",
				"Ricardo", "Laura" };
		String[] lastNames = { "Posada", "Gallo", "Merchan", "Valencia", "Leon", "Gomez", "Rodriguez", "Lopez",
				"Martinez", "Perez" };
		String[] docTypes = { "CC", "CE", "TI" };

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (int i = 0; i < salesmanCount; i++) {
				Salesmen salesman = new Salesmen(docTypes[random.nextInt(docTypes.length)],
						1000000000L + random.nextInt(900000000), firstNames[random.nextInt(firstNames.length)],
						lastNames[random.nextInt(lastNames.length)]);

				generatedSalesmen.add(salesman);
				writer.write(salesman.getDocumentType() + ";" + salesman.getDocumentNumber() + ";"
						+ salesman.getFirstName() + ";" + salesman.getLastName());
				writer.newLine();
			}

			System.out.println(
					"Process completed successfully: '" + fileName + "' generated with " + salesmanCount + " records.");

		} catch (IOException e) {
			/**
			 * Mensaje de error obligatorio en caso de fallo [cite: 58]
			 * 
			 */
			System.err.println("An error occurred while creating the salesmen info file: " + e.getMessage());
		}

		return generatedSalesmen;
	}

	/**
	 * Genera el archivo de ventas de un vendedor.
	 *
	 * Primera linea:
	 * TipoDocumento;NumeroDocumento
	 *
	 * Siguientes lineas:
	 * IDProducto;Cantidad
	 *
	 * @param randomSalesCount cantidad de ventas a generar
	 * @param name             nombre de referencia del vendedor
	 * @param id               documento del vendedor
	 * @throws IOException si ocurre un error de escritura
	 */
	public void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
		String fileName = Constants.BASE_PATH + java.io.File.separator + Constants.SALES_FOLDER
				+ java.io.File.separator + Constants.SALES_FILE_PREFIX + id + ".txt";
		Random random = new Random();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write(getDocumentType() + ";" + id);
			writer.newLine();

			for (int i = 0; i < randomSalesCount; i++) {
				int productId = random.nextInt(Constants.DEFAULT_PRODUCTS_COUNT) + 1;
				int quantity = random.nextInt(20) + 1;

				writer.write(productId + ";" + quantity);
				writer.newLine();
			}
		}
	}
}
