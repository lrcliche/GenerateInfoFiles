package src;

/**
 * Helper de validaciones (sin {@code main}).
 * <p>
 * Devuelve {@code null} si la linea es valida; de lo contrario devuelve el mensaje de error.
 */
public final class TestValidator {

	/**
	 * Constructor privado para evitar instancias.
	 */
	private TestValidator() {
	}

	/**
	 * Valida una linea de producto en formato {@code ID;Nombre;Precio}.
	 *
	 * @param data columnas separadas por ';'
	 * @return mensaje de error o {@code null}
	 */
	public static String validarProducto(String[] data) {
		if (data == null || data.length != 3) {
			return "línea mal formada";
		}
		if (isBlank(data[0]) || isBlank(data[1]) || isBlank(data[2])) {
			return "línea mal formada";
		}
		return null;
	}

	/**
	 * Valida una linea de vendedor en formato {@code TipoDoc;Numero;Nombre;Apellido}.
	 *
	 * @param data columnas separadas por ';'
	 * @return mensaje de error o {@code null}
	 */
	public static String validarVendedor(String[] data) {
		if (data == null || data.length != 4) {
			return "línea mal formada";
		}
		if (isBlank(data[0]) || isBlank(data[1]) || isBlank(data[2]) || isBlank(data[3])) {
			return "línea mal formada";
		}
		return null;
	}

	/**
	 * Valida una linea de venta.
	 * <p>
	 * Si es encabezado, valida existencia del vendedor.
	 * Si es detalle, valida existencia del producto y cantidad.
	 *
	 * @param data columnas separadas por ';'
	 * @param esEncabezado true si corresponde a la primera linea del archivo
	 * @param productoExiste true si el producto esta en el catalogo
	 * @param vendedorExiste true si el vendedor esta en el archivo de vendedores
	 * @param cantidad cantidad ya parseada (opcional)
	 * @return mensaje de error o {@code null}
	 */
	public static String validarVenta(String[] data, boolean esEncabezado, boolean productoExiste, boolean vendedorExiste,
			Integer cantidad) {
		if (data == null || data.length != 2) {
			return "línea mal formada";
		}

		if (isBlank(data[0]) || isBlank(data[1])) {
			return "línea mal formada";
		}

		if (esEncabezado) {
			if (!vendedorExiste) {
				return "vendedor inexistente";
			}
			return null;
		}

		if (!productoExiste) {
			return "producto inexistente";
		}

		if (cantidad != null && cantidad.intValue() <= 0) {
			return "cantidad <= 0";
		}

		return null;
	}

	private static boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}
}

