package src;

/**
 * Representa un vendedor del sistema.
 */
public class Salesman {

    private String documentType;
    private long documentNumber;
    private String firstName;
    private String lastName;

    /**
     * Crea un vendedor con sus datos basicos.
     *
     * @param documentType tipo de documento
     * @param documentNumber numero de documento
     * @param firstName nombre del vendedor
     * @param lastName apellido del vendedor
     */
    public Salesman(String documentType, long documentNumber, String firstName, String lastName) {
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
}
