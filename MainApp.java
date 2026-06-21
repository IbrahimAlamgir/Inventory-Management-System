// import java.util.InputMismatchException;
// import java.util.Scanner;

// public class MainApp {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         InventoryManager manager = new InventoryManager();

//         manager.loadFromFile(); //load from file on starting

//         // Creating menu driven program
//         while (true) {
//             try {
//                 System.out.println("""
//                     ----------Inventory Management System----------
//                     1. Add Product
//                     2. View All Products
//                     3. Update Product
//                     4. Delete Product
//                     5. Search Product by ID
//                     6. Exit
//                     Enter your choice:
//                     """);

//                 int choice = sc.nextInt();
//                 sc.nextLine();

//                 if (choice == 1) {
//                     manager.addProduct();
//                     manager.saveToFile();
//                 } else if (choice == 2) {
//                     manager.viewProduct();
//                 } else if (choice == 3) {
//                     manager.updateProduct();
//                     manager.saveToFile();
//                 } else if (choice == 4) {
//                     manager.deleteProduct();
//                     manager.saveToFile();
//                 } else if (choice == 5) {
//                     manager.searchProduct();
//                 } else if (choice == 6) {
//                     manager.saveToFile();
//                     System.out.println("\nExiting...");
//                     break;
//                 } else {
//                     System.out.println("\nEnter a valid choice\n");
//                 }

//             } catch (InputMismatchException e) {
//                 System.out.println("\nInvalid input! Please enter a number only.\n");
//                 sc.nextLine();
                
//             } catch (FileSaveException e) {
//                 System.out.println(e.getMessage());
//             }
//         }

//         sc.close();
//     }
// }
