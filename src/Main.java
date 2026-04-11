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

public class Main {

    private static Map<Integer, Product> productMap = new HashMap<>();
    private static Map<Long, SalesmenData> salesmanMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            processReports();
            System.out.println("Reporte Generado Exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    private static void processReports() throws IOException {
        FileUtil.createBaseDirectories();
        
        loadProducts();
        loadSalesmen();
        searchAndProcessSalesFiles();
        
        generateSalesmanReport();
        generateProductReport();
    }

    private static void loadProducts() throws IOException {
        File file = new File(Constants.BASE_PATH + File.separator + Constants.PRODUCTS_FILE);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3) {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    
                    productMap.put(id, new Product(id, name, price));
                }
            }
        }
    }

    private static void loadSalesmen() throws IOException {
        File file = new File(Constants.BASE_PATH + File.separator + Constants.SALESMEN_FILE);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 4) {
                    String docType = data[0].trim();
                    long docNumber = Long.parseLong(data[1].trim());
                    String firstName = data[2].trim();
                    String lastName = data[3].trim();
                    
                    salesmanMap.put(docNumber, new SalesmenData(docNumber, docType, firstName, lastName));
                }
            }
        }
    }

    private static void searchAndProcessSalesFiles() throws IOException {
        File folder = new File(Constants.BASE_PATH + File.separator + Constants.SALES_FOLDER);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) return;

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".txt") 
                && file.getName().startsWith(Constants.SALES_FILE_PREFIX)) {
                processSingleSalesFile(file);
            }
        }
    }

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

    private static void generateSalesmanReport() throws IOException {
        List<SalesmenData> salesmenList = new ArrayList<>(salesmanMap.values());
        
        salesmenList.sort(Comparator.comparingDouble(SalesmenData::getTotalCollected).reversed());

        String fileName = Constants.BASE_PATH + File.separator + "salesmen_info.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (SalesmenData salesman : salesmenList) {
                String line = salesman.getFullName() + ";" + salesman.getTotalCollected();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static void generateProductReport() throws IOException {
        List<Product> productList = new ArrayList<>(productMap.values());
        
        productList.sort(Comparator.comparingInt(Product::getTotalQuantitySold).reversed());

        String fileName = Constants.BASE_PATH + File.separator + "products_info.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : productList) {
                String line = product.getName() + ";" + product.getPrice() + ";" + product.getTotalQuantitySold();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static class Product {
        private int id;
        private String name;
        private double price;
        private int totalQuantitySold = 0;

        public Product(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getTotalQuantitySold() { return totalQuantitySold; }
        public void addQuantitySold(int qty) { this.totalQuantitySold += qty; }
    }

    private static class SalesmenData {
        private long documentNumber;
        private String documentType;
        private String firstName;
        private String lastName;
        private double totalCollected = 0;

        public SalesmenData(long documentNumber, String documentType, String firstName, String lastName) {
            this.documentNumber = documentNumber;
            this.documentType = documentType;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFullName() {
            return firstName + "_" + lastName;
        }

        public double getTotalCollected() {
            return totalCollected;
        }

        public void addTotalCollected(double amount) {
            this.totalCollected += amount;
        }
    }
}