package src;

import java.io.File;

/**
 * Utilidad para operaciones basicas con archivos y carpetas.
 */
public final class FileUtil {

	/**
	 * Constructor privado para evitar instancias.
	 */
    private FileUtil() {
    }

    /**
     * Crea las carpetas base del proceso si no existen.
     * <p>
     * - {@code generated_files}
     * - {@code generated_files/sales}
     */
    public static void createBaseDirectories() {
        File baseDirectory = new File(Constants.BASE_PATH);
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }

        File salesDirectory = new File(Constants.BASE_PATH + File.separator + Constants.SALES_FOLDER);
        if (!salesDirectory.exists()) {
            salesDirectory.mkdirs();
        }
    }
}
