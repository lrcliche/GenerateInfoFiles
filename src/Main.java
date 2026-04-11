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
 * Clase principal para el procesamiento de datos de ventas y generación de reportes.
 * 
 * Esta clase coordina la lectura de archivos de productos y vendedores,
 * procesa los archivos de ventas individuales y genera dos reportes CSV:
 * uno ordenado por vendedores y otro por productos.
 * 
 * @author Andrés Felipe Posada Valencia
 * @version 1.0
 */
public class Main {

    /** Ruta base donde se encuentran los archivos generados. */
    private static final String BASE_PATH = "generated_files";
    /** Nombre del archivo de productos. */
    private static final String PRODUCTS_FILE = "products.txt";
    /** Nombre del archivo de vendedores. */
    private static final String SALESMEN_FILE = "salesmen_info.txt";
    /** Nombre de la carpeta de ventas. */
    private static final String SALES_FOLDER = "sales";
    /** Prefijo de los archivos de ventas. */
    private static final String SALES_FILE_PREFIX = "sale";
    
    /** Mapa de productos indexado por ID de producto. */
    private static Map<Integer, Product> productMap = new HashMap<>();
    /** Mapa de vendedores indexado por número de documento. */
    private static Map<Long, SalesmenData> salesmanMap = new HashMap<>();

    /**
     * Punto de entrada de la aplicación de generación de reportes.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            processReports();
            System.out.println("Reporte Generado Exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    /**
     * Coordina el proceso completo de generación de reportes.
     * 
     * Secuencia de operaciones:
     * 1. Crea los directorios necesarios
     * 2. Carga los productos desde archivo
     * 3. Carga los vendedores desde archivo
     * 4. Procesa todos los archivos de ventas
     * 5. Genera el reporte de vendedores
     * 6. Genera el reporte de productos
     * 
     * @throws IOException Si ocurre un error al leer o escribir archivos
     */
    private static void processReports() throws IOException {
        new File(BASE_PATH).mkdirs();
        new File(BASE_PATH + File.separator + SALES_FOLDER).mkdirs();
        
        loadProducts();
        loadSalesmen();
        searchAndProcessSalesFiles();
        
        generateSalesmanReport();
        generateProductReport();
    }

    /**
     * Carga los productos desde el archivo de productos.
     * 
     * Formato esperado del archivo: ID;Nombre;Precio
     * Soporta números decimales con punto o coma como separador decimal.
     * 
     * @throws IOException Si el archivo no puede ser leído
     */
    private static void loadProducts() throws IOException {
        File file = new File(BASE_PATH + File.separator + PRODUCTS_FILE);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3) {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim().replace(',', '.'));
                    
                    productMap.put(id, new Product(name, price));
                }
            }
        }
    }

    /**
     * Carga los vendedores desde el archivo de información de vendedores.
     * 
     * Formato esperado del archivo: TipoDoc;Numero;Nombre;Apellido
     * 
     * @throws IOException Si el archivo no puede ser leído
     */
    private static void loadSalesmen() throws IOException {
        File file = new File(BASE_PATH + File.separator + SALESMEN_FILE);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 4) {
                    long docNumber = Long.parseLong(data[1].trim());
                    String firstName = data[2].trim();
                    String lastName = data[3].trim();
                    
                    salesmanMap.put(docNumber, new SalesmenData(firstName, lastName));
                }
            }
        }
    }

    /**
     * Busca y procesa todos los archivos de ventas en la carpeta de ventas.
     * 
     * Solo procesa archivos .txt que comienzan con el prefijo configurado.
     * Cada archivo representa las ventas de un vendedor.
     * 
     * @throws IOException Si ocurre un error al leer algún archivo
     */
    private static void searchAndProcessSalesFiles() throws IOException {
        File folder = new File(BASE_PATH + File.separator + SALES_FOLDER);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) return;

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".txt") 
                && file.getName().startsWith(SALES_FILE_PREFIX)) {
                processSingleSalesFile(file);
            }
        }
    }

    /**
     * Procesa un archivo de ventas individuales.
     * 
     * Formato del archivo:
     * - Primera línea: TipoDocumento;NumeroDocumento (identifica al vendedor)
     * - Líneas siguientes: IDProducto;Cantidad (una venta por línea)
     * 
     * Actualiza los totales de productos vendidos y dinero recolectado.
     * 
     * @param file Archivo de ventas a procesar
     * @throws IOException Si el archivo no puede ser leído
     */
    private static void processSingleSalesFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            if (firstLine == null) return;

            String[] header = firstLine.split(";");
            if (header.length < 2) return;
            
            long salesmanId = Long.parseLong(header[1].trim());

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length < 2) continue;
                
                int productId = Integer.parseInt(data[0].trim());
                int quantity = Integer.parseInt(data[1].trim());

                Product product = productMap.get(productId);
                SalesmenData salesman = salesmanMap.get(salesmanId);
                
                if (product != null && salesman != null) {
                    double subtotal = product.getPrice() * quantity;
                    salesman.addTotalCollected(subtotal);
                    product.addQuantitySold(quantity);
                }
            }
        }
    }

    /**
     * Genera el reporte de vendedores ordenado por total recolectado.
     * 
     * Formato del archivo de salida: Nombre;TotalRecaudado
     * Los vendedores se ordenan en orden descendente por total recolectado.
     * 
     * @throws IOException Si no se puede escribir el archivo de reporte
     */
    private static void generateSalesmanReport() throws IOException {
        List<SalesmenData> salesmenList = new ArrayList<>(salesmanMap.values());
        
        salesmenList.sort(Comparator.comparingDouble(SalesmenData::getTotalCollected).reversed());

        String fileName = BASE_PATH + File.separator + "salesmen_report.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (SalesmenData salesman : salesmenList) {
                String line = salesman.getFullName() + ";" + salesman.getTotalCollected();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Genera el reporte de productos ordenado por cantidad vendida.
     * 
     * Formato del archivo de salida: Nombre;Precio;CantidadTotal
     * Los productos se ordenan en orden descendente por cantidad vendidas.
     * 
     * @throws IOException Si no se puede escribir el archivo de reporte
     */
    private static void generateProductReport() throws IOException {
        List<Product> productList = new ArrayList<>(productMap.values());
        
        productList.sort(Comparator.comparingInt(Product::getTotalQuantitySold).reversed());

        String fileName = BASE_PATH + File.separator + "products_report.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : productList) {
                String line = product.getName() + ";" + product.getPrice() + ";" + product.getTotalQuantitySold();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Clase interna que representa un producto con su información y totales de venta.
     */
    private static class Product {
        private String name;
        private double price;
        private int totalQuantitySold = 0;

        /**
         * Crea un nuevo producto.
         * 
         * @param name Nombre del producto
         * @param price Precio por unidad del producto
         */
        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getTotalQuantitySold() { return totalQuantitySold; }
        
        /**
         * Agrega cantidad vendida al total del producto.
         * 
         * @param qty Cantidad a agregar
         */
        public void addQuantitySold(int qty) { this.totalQuantitySold += qty; }
    }

    /**
     * Clase interna que representa un vendedor con su información y totales.
     */
    private static class SalesmenData {
        private String firstName;
        private String lastName;
        private double totalCollected = 0;

        /**
         * Crea un nuevo registro de vendedor.
         * 
         * @param firstName    Nombre del vendedor
         * @param lastName    Apellido del vendedor
         */
        public SalesmenData(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        /**
         * Obtiene el nombre completo del vendedor.
         * 
         * @return Nombre completo en formato Nombre_Apellido
         */
        public String getFullName() {
            return firstName + "_" + lastName;
        }

        /**
         * Obtiene el total de dinero recolectado por el vendedor.
         * 
         * @return Total acumulado
         */
        public double getTotalCollected() {
            return totalCollected;
        }

        /**
         * Agrega un monto al total recolectado.
         * 
         * @param amount Monto a agregar
         */
        public void addTotalCollected(double amount) {
            this.totalCollected += amount;
        }
    }
}