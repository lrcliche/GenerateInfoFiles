package src;

/**
 * Clase principal del programa (único {@code main} del proyecto).
 * <p>
 * Ejecuta toda la lógica del escenario desde {@link GenerateInfoFiles}.
 */
public class Main {

	/**
	 * Punto de entrada del programa.
	 *
	 * @param args argumentos de ejecución (no usados)
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Iniciando proceso...");
			GenerateInfoFiles.run();
			System.out.println("Proceso finalizado correctamente.");
		} catch (Exception e) {
			System.err.println("Error en la ejecución: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

