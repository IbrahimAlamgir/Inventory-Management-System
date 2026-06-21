import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class InventoryManager {
    private Product[] products = new Product[50]; //creating an array of 50 products
    private int count = 0;
    private final String FILE_NAME = System.getProperty("user.dir") + File.separator + "inventory_data.txt";

    public InventoryManager() { //loading old data from file upon starting
        loadFromFile();
    }

    // ---------------- ADD PRODUCT ----------------
    public void addProduct(int id, String name, int price, int quantity)
            throws InvalidInputException, DuplicateProductException, InventoryFullException {
        
        if (count >= products.length) {
            throw new InventoryFullException("Inventory is full! Cannot add more products.");
        }
        
        if (id <= 0) throw new InvalidInputException("Product ID must be positive.");
        if (price < 0) throw new InvalidInputException("Price cannot be negative.");
        if (quantity < 0) throw new InvalidInputException("Quantity cannot be negative.");
        

        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == id)
                throw new DuplicateProductException("Product ID already exists.");
        }

        products[count++] = new Product(id, name, price, quantity);
        saveToFile();
    }

    //we are overloading addProduct function as we also support products with expiry date
    public void addProduct(int id, String name, int price, int quantity, LocalDate expiryDate) 
            throws InvalidInputException, DuplicateProductException {
        if (id <= 0) throw new InvalidInputException("Product ID must be positive.");
        if (price < 0) throw new InvalidInputException("Price cannot be negative.");
        if (quantity < 0) throw new InvalidInputException("Quantity cannot be negative.");

        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == id)
                throw new DuplicateProductException("Product ID already exists.");
        }

        products[count++] = new PerishableProduct(id, name, price, quantity, expiryDate);
        saveToFile();
    }

    // ---------------- VIEW PRODUCTS ----------------
    public String getFormattedProductList() {
        if (count == 0) return "No products available.\n";

        boolean hasPerishables = false;
        for (int i = 0; i < count; i++) {
            if (products[i] instanceof PerishableProduct) {
                hasPerishables = true;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        if (hasPerishables) {
            sb.append(String.format("%-10s %-20s %-10s %-10s %-15s%n",
                    "ID", "Name", "Price", "Qty", "Expiry"));
            sb.append("------------------------------------------------------------------\n");
            for (int i = 0; i < count; i++) {
                Product p = products[i];
                if (p instanceof PerishableProduct pp) {
                    sb.append(String.format("%-10d %-20s %-10d %-10d %-15s%n",
                            pp.getProductId(), pp.getProductName(),
                            pp.getPrice(), pp.getQuantity(),
                            pp.getExpiryDate()));
                } else {
                    sb.append(String.format("%-10d %-20s %-10d %-10d %-15s%n",
                            p.getProductId(), p.getProductName(),
                            p.getPrice(), p.getQuantity(), "N/A"));
                }
            }
        } else {
            sb.append(String.format("%-10s %-20s %-10s %-10s%n",
                    "ID", "Name", "Price", "Qty"));
            sb.append("--------------------------------------------------\n");
            for (int i = 0; i < count; i++) {
                Product p = products[i];
                sb.append(String.format("%-10d %-20s %-10d %-10d%n",
                        p.getProductId(), p.getProductName(),
                        p.getPrice(), p.getQuantity()));
            }
        }

        return sb.toString();
    }

    // ---------------- SEARCH ----------------
    public String searchProductById(int id) throws ProductNotFoundException {
        Product p = findProductById(id);
        if (p instanceof PerishableProduct pp) {
            return String.format("Product found:%n%-10d %-20s %-10d %-10d %-15s%n",
                    pp.getProductId(), pp.getProductName(),
                    pp.getPrice(), pp.getQuantity(),
                    pp.getExpiryDate());
        }
        return String.format("Product found:%n%-10d %-20s %-10d %-10d%n",
                p.getProductId(), p.getProductName(),
                p.getPrice(), p.getQuantity());
    }

    // ---------------- UPDATE ----------------
    public void updateProductName(int id, String newName) throws ProductNotFoundException {
        Product p = findProductById(id);
        p.setProductName(newName);
        saveToFile();
    }

    public void updateProductPrice(int id, int newPrice) throws ProductNotFoundException {
        Product p = findProductById(id);
        p.setPrice(newPrice);
        saveToFile();
    }

    public void updateProductQuantity(int id, int newQty) throws ProductNotFoundException {
        Product p = findProductById(id);
        p.setQuantity(newQty);
        saveToFile();
    }

    public void updateProductExpiry(int id, LocalDate newExpiryDate) throws ProductNotFoundException {
        Product p = findProductById(id);
        if (p instanceof PerishableProduct) {
            ((PerishableProduct) p).setExpiryDate(newExpiryDate);
            saveToFile();
        } else {
            throw new ProductNotFoundException("This product does not have an expiry date.");
        }
    }

    // ---------------- DELETE ----------------
    public void deleteProductById(int id) throws ProductNotFoundException {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == id) {
                found = true;
                for (int j = i; j < count - 1; j++) {
                    products[j] = products[j + 1];
                }
                products[--count] = null;
                break;
            }
        }
        if (!found) throw new ProductNotFoundException("Product with ID " + id + " not found.");
        saveToFile();
    }

    // ---------------- FILE I/O ----------------
    public void saveToFile() {
        System.out.println("Saving to: " + new File(FILE_NAME).getAbsolutePath());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < count; i++) {
                Product p = products[i];
                if (p instanceof PerishableProduct pp) {
                    bw.write(pp.getProductId() + "," + pp.getProductName() + "," +
                            pp.getPrice() + "," + pp.getQuantity() + "," + pp.getExpiryDate());
                } else {
                    bw.write(p.getProductId() + "," + p.getProductName() + "," +
                            p.getPrice() + "," + p.getQuantity());
                }
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        count=0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int price = Integer.parseInt(data[2]);
                int qty = Integer.parseInt(data[3]);
                if (data.length > 4) {
                    LocalDate expiry = LocalDate.parse(data[4]);
                    products[count++] = new PerishableProduct(id, name, price, qty, expiry);
                } else {
                    products[count++] = new Product(id, name, price, qty);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }

    // ---------------- UTIL ----------------
    private Product findProductById(int id) throws ProductNotFoundException {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == id) return products[i];
        }
        throw new ProductNotFoundException("Product with ID " + id + " not found.");
    }
}
