package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	 * Entity representing a salesman and utilities to generate random salesman data
	 * files. This class ensures data consistency as required by the academic
	 * project guidelines.
	 */

	/**
	 * Creates a file with pseudo-random information for a specified number of
	 * salesmen. The format of each line is:
	 * DocumentType;DocumentNumber;FirstNames;LastNames. * @param salesmanCount The
	 * number of salesmen to be generated in the file.
	 */
	public void createSalesManInfoFile(int salesmanCount) {
		String fileName = Constants.BASE_PATH + java.io.File.separator + "salesmen_info.txt";
		
		Random random = new Random();

		/**
		 * Listas de nombres y apellidos reales para cumplir con la "coherencia" pedida
		 * 
		 */
		String[] firstNames = { "Andres", "Alanis", "John", "Andres", "Carolina", "Jose", "Maria", "Luis", "Fernanda",
				"Ricardo" };
		String[] lastNames = { "Posada", "Gallo", "Merchan", "Valencia", "Leon", "Gomez", "Rodriguez", "Lopez",
				"Martinez", "Perez" };
		String[] docTypes = { "CC", "CE", "TI" };

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (int i = 0; i < salesmanCount; i++) {
				String type = docTypes[random.nextInt(docTypes.length)];
				long document = 1000000000L + random.nextInt(900000000);
				String name = firstNames[random.nextInt(firstNames.length)];
				String lastName = lastNames[random.nextInt(lastNames.length)];

				/**
				 * Formato requerido: Tipo Documento; Número Documento; Nombres; Apellidos
				 * 
				 */
				String line = type + ";" + document + ";" + name + ";" + lastName;

				writer.write(line);
				writer.newLine();
			}
			/**
			 * Mensaje de éxito obligatorio [cite: 58]
			 */
			System.out.println(
					"Process completed successfully: '" + fileName + "' generated with " + salesmanCount + " records.");

		} catch (IOException e) {
			/**
			 * Mensaje de error obligatorio en caso de fallo [cite: 58]
			 * 
			 */
			System.err.println("An error occurred while creating the salesmen info file: " + e.getMessage());
		}
	}
}