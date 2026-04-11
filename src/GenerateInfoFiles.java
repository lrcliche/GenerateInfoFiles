package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase con la lógica completa del escenario.
 * <p>
 * No contiene {@code main}; el único {@code main} del proyecto está en {@link Main}.
 * <p>
 * Responsabilidades:
 * <ul>
 *   <li>Generar archivos base (productos, vendedores y ventas).</li>
 *   <li>Validar y procesar información.</li>
 *   <li>Generar reportes CSV, {@code errors.txt} y conclusiones.</li>
 * </ul>
 */
public final class GenerateInfoFiles {

	private static final Map<Integer, Product> productMap = new HashMap<>();
	private static final Map<Long, SalesmenData> salesmanMap = new HashMap<>();
	private static final List<ErrorEntry> errors = new ArrayList<>();

	/**
	 * Constructor privado para evitar instancias.
	 */
	private GenerateInfoFiles() {
	}

	/**
	 * Ejecuta el flujo completo: generación + procesamiento.
	 * <p>
	 * Siempre intenta escribir {@code errors.txt} al final.
	 *
	 * @throws Exception si ocurre un error inesperado
	 */
	public static void run() throws Exception {
		errors.clear();

		try {
			generateBaseFiles();
			processReports();
		} finally {
			try {
				writeErrorsFile();
			} catch (IOException e) {
				System.err.println("Error al escribir errors.txt: " + e.getMessage());
			}
		}
	}

	/**
	 * Genera la estructura base y los archivos de entrada del proceso.
	 *
	 * @throws IOException si ocurre un error de escritura
	 */
	private static void generateBaseFiles() throws IOException {
		System.out.println("Generando archivos base...");

		FileUtil.createBaseDirectories();
		cleanOldSalesFiles();

		Product productTool = new Product("0", "Default", 0.0);
		productTool.createProductsFile(Constants.DEFAULT_PRODUCTS_COUNT);

		Salesmen salesmanTool = new Salesmen("CC", 0, "Default", "Default");
		List<Salesmen> generatedSalesmen = salesmanTool.createSalesManInfoFile(Constants.DEFAULT_SALESMEN_COUNT);

		for (Salesmen salesman : generatedSalesmen) {
			int randomSalesCount = Constants.DEFAULT_MIN_SALES
					+ (int) (Math.random() * (Constants.DEFAULT_MAX_SALES - Constants.DEFAULT_MIN_SALES + 1));
			salesman.createSalesMenFile(randomSalesCount, salesman.getFullName(), salesman.getDocumentNumber());
		}

		System.out.println("Archivos generados correctamente en la carpeta: " + Constants.BASE_PATH);
	}

	/**
	 * Elimina archivos de ventas anteriores para evitar mezclar datos viejos con nuevos.
	 */
	private static void cleanOldSalesFiles() {
		File salesDirectory = new File(Constants.BASE_PATH + File.separator + Constants.SALES_FOLDER);
		File[] files = salesDirectory.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isFile() && file.getName().startsWith(Constants.SALES_FILE_PREFIX) && file.getName().endsWith(".txt")) {
				file.delete();
			}
		}
	}

	/**
	 * Coordina el proceso de lectura, validación, procesamiento y salida.
	 *
	 * @throws IOException si falla lectura o escritura
	 */
	private static void processReports() throws IOException {
		System.out.println("Procesando reportes...");

		productMap.clear();
		salesmanMap.clear();

		FileUtil.createBaseDirectories();

		loadProducts();
		loadSalesmen();
		searchAndProcessSalesFiles();

		generateSalesmanReport();
		generateProductReport();
		writeConclusions();
	}

	/**
	 * Carga productos desde {@code products.txt} con validaciones.
	 *
	 * @throws IOException si falla la lectura
	 */
	private static void loadProducts() throws IOException {
		File file = new File(Constants.BASE_PATH + File.separator + Constants.PRODUCTS_FILE);
		if (!file.exists()) {
			logError(file.getName(), 0, "archivo no encontrado", file.getPath());
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int lineNumber = 0;

			while ((line = br.readLine()) != null) {
				lineNumber++;

				if (line.trim().isEmpty()) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
					continue;
				}

				String[] data = line.split(";");
				String validationError = TestValidator.validarProducto(data);
				if (validationError != null) {
					logError(file.getName(), lineNumber, validationError, line);
					continue;
				}

				try {
					int id = Integer.parseInt(data[0].trim());
					String name = data[1].trim();
					double price = Double.parseDouble(data[2].trim().replace(',', '.'));

					if (price <= 0) {
						logError(file.getName(), lineNumber, "precio <= 0", data[2].trim());
						continue;
					}

					productMap.put(id, new Product(String.valueOf(id), name, price));
				} catch (Exception e) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
				}
			}
		}
	}

	/**
	 * Carga vendedores desde {@code salesmen_info.txt} con validaciones.
	 *
	 * @throws IOException si falla la lectura
	 */
	private static void loadSalesmen() throws IOException {
		File file = new File(Constants.BASE_PATH + File.separator + Constants.SALESMEN_FILE);
		if (!file.exists()) {
			logError(file.getName(), 0, "archivo no encontrado", file.getPath());
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int lineNumber = 0;

			while ((line = br.readLine()) != null) {
				lineNumber++;

				if (line.trim().isEmpty()) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
					continue;
				}

				String[] data = line.split(";");
				String validationError = TestValidator.validarVendedor(data);
				if (validationError != null) {
					logError(file.getName(), lineNumber, validationError, line);
					continue;
				}

				try {
					long docNumber = Long.parseLong(data[1].trim());
					String firstName = data[2].trim();
					String lastName = data[3].trim();

					salesmanMap.put(docNumber, new SalesmenData(firstName, lastName));
				} catch (Exception e) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
				}
			}
		}
	}

	/**
	 * Busca y procesa los archivos de ventas.
	 *
	 * @throws IOException si falla la lectura
	 */
	private static void searchAndProcessSalesFiles() throws IOException {
		File folder = new File(Constants.BASE_PATH + File.separator + Constants.SALES_FOLDER);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles == null) {
			return;
		}

		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith(".txt") && file.getName().startsWith(Constants.SALES_FILE_PREFIX)) {
				processSingleSalesFile(file);
			}
		}
	}

	/**
	 * Procesa un archivo de ventas individual.
	 *
	 * @param file archivo a procesar
	 * @throws IOException si falla la lectura
	 */
	private static void processSingleSalesFile(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String firstLine = br.readLine();
			if (firstLine == null) {
				logError(file.getName(), 1, "línea mal formada", "<vacio>");
				return;
			}

			String[] header = firstLine.split(";");
			if (header.length != 2) {
				logError(file.getName(), 1, "línea mal formada", firstLine);
				return;
			}

			long salesmanId;
			try {
				salesmanId = Long.parseLong(header[1].trim());
			} catch (Exception e) {
				logError(file.getName(), 1, "línea mal formada", firstLine);
				return;
			}

			boolean salesmanExists = salesmanMap.containsKey(salesmanId);
			String headerValidation = TestValidator.validarVenta(header, true, false, salesmanExists, null);
			if (headerValidation != null) {
				logError(file.getName(), 1, headerValidation, firstLine);
				return;
			}

			SalesmenData salesman = salesmanMap.get(salesmanId);
			if (salesman == null) {
				logError(file.getName(), 1, "vendedor inexistente", String.valueOf(salesmanId));
				return;
			}

			String line;
			int lineNumber = 1;
			while ((line = br.readLine()) != null) {
				lineNumber++;

				if (line.trim().isEmpty()) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
					continue;
				}

				String[] data = line.split(";");
				if (data.length != 2) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
					continue;
				}

				int productId;
				int quantity;
				try {
					productId = Integer.parseInt(data[0].trim());
					quantity = Integer.parseInt(data[1].trim());
				} catch (Exception e) {
					logError(file.getName(), lineNumber, "línea mal formada", line);
					continue;
				}

				Product product = productMap.get(productId);
				boolean productExists = product != null;

				String validationError = TestValidator.validarVenta(data, false, productExists, true, quantity);
				if (validationError != null) {
					String value = line;
					if ("producto inexistente".equals(validationError)) {
						value = String.valueOf(productId);
					} else if ("cantidad <= 0".equals(validationError)) {
						value = String.valueOf(quantity);
					}
					logError(file.getName(), lineNumber, validationError, value);
					continue;
				}

				if (product.getPrice() <= 0) {
					logError(file.getName(), lineNumber, "precio <= 0", String.valueOf(product.getPrice()));
					continue;
				}

				double subtotal = product.getPrice() * quantity;
				salesman.addTotalCollected(subtotal);
				product.addQuantitySold(quantity);
			}
		}
	}

	/**
	 * Registra un error en memoria y lo muestra en consola.
	 *
	 * @param archivo archivo de origen
	 * @param linea línea (1-based, 0 si no aplica)
	 * @param error descripción del error
	 * @param valor valor asociado
	 */
	private static void logError(String archivo, int linea, String error, String valor) {
		ErrorEntry entry = new ErrorEntry(archivo, linea, error, valor);
		errors.add(entry);
		System.err.println(entry.toLine());
	}

	/**
	 * Escribe {@code errors.txt} con formato: {@code archivo | línea | error | valor}.
	 *
	 * @throws IOException si falla la escritura
	 */
	private static void writeErrorsFile() throws IOException {
		FileUtil.createBaseDirectories();
		String fileName = Constants.BASE_PATH + File.separator + Constants.ERRORS_FILE;

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (ErrorEntry entry : errors) {
				writer.write(entry.toLine());
				writer.newLine();
			}
		}
	}

	/**
	 * Genera {@code conclusion.txt} y {@code conslusion.txt}.
	 *
	 * @throws IOException si falla la escritura
	 */
	private static void writeConclusions() throws IOException {
		String content = buildConclusionContent();
		writeTextFile(Constants.BASE_PATH + File.separator + Constants.CONCLUSION_FILE, content);
	}

	/**
	 * Contenido de conclusión (estilo estudiante).
	 *
	 * @return texto de conclusión
	 */
	private static String buildConclusionContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("Cambios\n");
		sb.append("# Cambios semana 5\n\n");

		sb.append("Que se aprendio:\n");
		sb.append("- A leer y procesar archivos de texto linea por linea sin asumir que todo viene perfecto.\n");
		sb.append("- A validar datos antes de usarlos para evitar reportes incorrectos.\n");
		sb.append("- A centralizar constantes y utilidades para que el codigo sea mas facil de mantener.\n\n");

		sb.append("Dificultades:\n");
		sb.append("- Manejar errores sin frenar todo el programa y al mismo tiempo dejar registro en consola y archivo.\n");
		sb.append("- Definir validaciones claras para lineas mal formadas, productos inexistentes y cantidades/precios invalidos.\n\n");

		sb.append("Conclusiones:\n");
		sb.append("- Estas validaciones ayudan a detectar problemas de calidad de datos y a auditar procesos.\n");
		sb.append("- Un archivo de errores facilita soporte, trazabilidad y correccion rapida.\n");

		return sb.toString();
	}

	/**
	 * Escribe un archivo de texto.
	 *
	 * @param path ruta de salida
	 * @param content contenido
	 * @throws IOException si falla la escritura
	 */
	private static void writeTextFile(String path, String content) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write(content);
		}
	}

	/**
	 * Genera {@code salesmen_report.csv} ordenado por total recolectado (desc).
	 *
	 * @throws IOException si falla la escritura
	 */
	private static void generateSalesmanReport() throws IOException {
		List<SalesmenData> salesmenList = new ArrayList<>(salesmanMap.values());
		salesmenList.sort(Comparator.comparingDouble(SalesmenData::getTotalCollected).reversed());

		String fileName = Constants.BASE_PATH + File.separator + Constants.SALESMEN_REPORT_CSV;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (SalesmenData salesman : salesmenList) {
				String line = salesman.getFullName() + ";" + salesman.getTotalCollected();
				writer.write(line);
				writer.newLine();
			}
		}
	}

	/**
	 * Genera {@code products_report.csv} ordenado por cantidad vendida (desc).
	 *
	 * @throws IOException si falla la escritura
	 */
	private static void generateProductReport() throws IOException {
		List<Product> productList = new ArrayList<>(productMap.values());
		productList.sort(Comparator.comparingInt(Product::getTotalQuantitySold).reversed());

		String fileName = Constants.BASE_PATH + File.separator + Constants.PRODUCTS_REPORT_CSV;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			for (Product product : productList) {
				String line = product.getName() + ";" + product.getPrice() + ";" + product.getTotalQuantitySold();
				writer.write(line);
				writer.newLine();
			}
		}
	}

	/**
	 * Entrada de error para {@code errors.txt}.
	 */
	private static final class ErrorEntry {
		private final String archivo;
		private final int linea;
		private final String error;
		private final String valor;

		private ErrorEntry(String archivo, int linea, String error, String valor) {
			this.archivo = archivo;
			this.linea = linea;
			this.error = error;
			this.valor = valor;
		}

		private String toLine() {
			return archivo + " | " + linea + " | " + error + " | " + (valor == null ? "" : valor);
		}
	}

	/**
	 * Estructura interna para acumular ventas por vendedor.
	 */
	private static class SalesmenData {
		private final String firstName;
		private final String lastName;
		private double totalCollected = 0;

		/**
		 * Crea un acumulador de vendedor.
		 *
		 * @param firstName nombre
		 * @param lastName apellido
		 */
		public SalesmenData(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		/**
		 * @return nombre completo en formato {@code Nombre_Apellido}
		 */
		public String getFullName() {
			return firstName + "_" + lastName;
		}

		/**
		 * @return total recolectado por el vendedor
		 */
		public double getTotalCollected() {
			return totalCollected;
		}

		/**
		 * Acumula el total recolectado.
		 *
		 * @param amount monto a sumar
		 */
		public void addTotalCollected(double amount) {
			this.totalCollected += amount;
		}
	}
}
