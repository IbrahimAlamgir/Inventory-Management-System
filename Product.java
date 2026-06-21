public class Product {
    private int productId;
    private String productName;
    private int price;
    private int quantity;

    //initializing the constructor
    public Product(int productId, String productName, int price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // use getter functions to access private variables
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }

    //use setter functions to set private variables
    public void setProductName(String productName) { this.productName = productName; }
    public void setPrice(int price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void displayProductInfo() {
        System.out.printf("%-10s %-20s %-10d %-10d %-15s%n", productId, productName, price, quantity, "N/A");
    }
}
