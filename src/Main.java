package src;

import java.io.IOException;

/**
 * Main class responsible for processing sales data and generating reports.
 * This class reads product and salesman information, processes individual 
 * sales files, and produces two CSV reports: sales by vendor and sales by product.
 * * @author Andrés Felipe Posada Valencia
 * @version 1.0
 */
public class Main {
    /**
     * Entry point of the reporting application.
     * Executes the processing logic and handles success or error messages.
     * * @param args Command line arguments (not used).
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
     * Processes sales data and generates reports.
     * Reads product and salesman information, processes individual 
     * sales files, and produces two CSV reports: sales by vendor and sales by product.
     * * @throws IOException If a file read or write operation fails.
     */
    private static void processReports() throws IOException {
        // 1. Load Products and Salesmen into memory (using Maps for efficiency)

        // 2. Iterate through sales files in the project folder
        // 3. Aggregate totals
        // 4. Sort and export reports
    }
    /**
     * Generates the Salesmen Report ordered by total money collected.
     * Requirements: Name;TotalMoney (Ordered Descending)
     */
    private static void generateSalesmanReport() {
        // Logic to create the CSV described in requirement 3 
    }
    /**
     * Generates the Products Report ordered by quantity sold.
     * Requirements: Name;Price;TotalQuantity (Ordered Descending)
     */
    private static void generateProductReport() {
        // Logic to create the CSV described in requirement 4 [cite: 36, 37]
    }
}
