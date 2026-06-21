import java.time.LocalDate;

public class PerishableProduct extends Product {
    private LocalDate expiryDate;

    public PerishableProduct(int productId, String productName, int price, int quantity, LocalDate expiryDate) {
        super(productId, productName, price, quantity);
        this.expiryDate = expiryDate;
    }

    // getter for expiry date
    public LocalDate getExpiryDate() { return expiryDate; }

    // setter for expiry date
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    @Override
    public void displayProductInfo() {
        System.out.printf("%-10s %-20s %-10d %-10d %-15s%n",
            getProductId(),
            getProductName(),
            getPrice(),
            getQuantity(),
            expiryDate);
    }
}
