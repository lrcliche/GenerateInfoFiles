package src;

/**
 * Contiene las constantes generales del proyecto.
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

    /** Cantidad por defecto de productos. */
    public static final int DEFAULT_PRODUCTS_COUNT = 20;
    /** Cantidad por defecto de vendedores. */
    public static final int DEFAULT_SALESMEN_COUNT = 5;
    /** Cantidad minima de ventas. */
    public static final int DEFAULT_MIN_SALES = 5;
    /** Cantidad maxima de ventas. */
    public static final int DEFAULT_MAX_SALES = 15;
}
