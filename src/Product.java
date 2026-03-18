package src;

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
     * @param id identificador del producto
     * @param name nombre del producto
     * @param price precio del producto
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
}
