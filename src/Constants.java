package src;

/**
 * Constantes generales del proyecto.
 */
public final class Constants {

	/**
	 * Constructor privado para evitar instancias.
	 */
    private Constants() {
    }

    /** Ruta base donde se generan los archivos. */
    public static final String BASE_PATH = "generated_files";
    /** Nombre del archivo de productos. */
    public static final String PRODUCTS_FILE = "products.txt";
    /** Nombre del archivo de vendedores. */
    public static final String SALESMEN_FILE = "salesmen_info.txt";
    /** Nombre de la carpeta de ventas. */
    public static final String SALES_FOLDER = "sales";
    /** Prefijo de los archivos de ventas. */
    public static final String SALES_FILE_PREFIX = "sale";

    public static final String SALESMEN_REPORT_CSV = "salesmen_report.csv";
    public static final String PRODUCTS_REPORT_CSV = "products_report.csv";
    public static final String ERRORS_FILE = "errors.txt";
    public static final String CONCLUSION_SOURCE_FILE = "ConclusionFinal.txt";
    public static final String CONCLUSION_ROOT_FILE = "Conclusion.txt";
    public static final String CONCLUSION_FILE = "conclusion.txt";

    public static final int DEFAULT_PRODUCTS_COUNT = 20;
    public static final int DEFAULT_SALESMEN_COUNT = 5;
    public static final int DEFAULT_MIN_SALES = 5;
    public static final int DEFAULT_MAX_SALES = 15;
}
