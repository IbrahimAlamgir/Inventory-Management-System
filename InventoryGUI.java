import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InventoryGUI extends JFrame {
    private final InventoryManager manager;
    private final JTextArea displayArea;

    // A modern color palette for a professional look
    private static final Color COLOR_BACKGROUND = new Color(45, 52, 54);
    private static final Color COLOR_SIDEBAR = new Color(99, 110, 114);
    private static final Color COLOR_DISPLAY_AREA = new Color(45, 52, 54);
    private static final Color COLOR_TEXT = new Color(223, 228, 234);
    private static final Color COLOR_ACCENT = new Color(0, 168, 255);
    private static final Color COLOR_SUCCESS = new Color(85, 239, 196);
    private static final Color COLOR_DANGER = new Color(255, 118, 117);
    private static final Color COLOR_WARNING = new Color(253, 203, 110);


    public InventoryGUI() {
        manager = new InventoryManager();
        manager.loadFromFile();

        setTitle("Inventory Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // -------- Main Panel with a gradient background --------
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(53, 59, 72), getWidth(), getHeight(), new Color(34, 47, 62));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);


        // -------- Header --------
        JLabel title = new JLabel("Inventory Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);


        // -------- Sidebar with Buttons --------
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 15));

        // Adding buttons with their respective colors and actions
        sidebar.add(createButton(" Add Product", COLOR_ACCENT, this::addProduct));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createButton("View Products", COLOR_ACCENT, this::viewProducts));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createButton("Search Product", COLOR_ACCENT, this::searchProduct));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createButton("Update Product", COLOR_WARNING, this::updateProduct));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createButton("Delete Product", COLOR_DANGER, this::deleteProduct));
        sidebar.add(Box.createVerticalGlue()); // Pushes the save button to the bottom
        sidebar.add(createButton("   Save to File", COLOR_SUCCESS, this::saveInventory));


        mainPanel.add(sidebar, BorderLayout.WEST);

        // -------- Display Area --------
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setForeground(COLOR_TEXT);
        displayArea.setBackground(COLOR_DISPLAY_AREA);
        displayArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SIDEBAR, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        displayArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // Utility to create styled buttons
    private JButton createButton(String text, Color accentColor, Runnable action) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setFocusPainted(false);
      
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add the action listener
        button.addActionListener(e -> action.run());

       

        return button;
    }


    // ---------------- ADD ----------------
    private void addProduct() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Enter Product ID:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);

            String name = JOptionPane.showInputDialog(this, "Enter Product Name:");
            if (name == null || name.trim().isEmpty()) return;

            String priceStr = JOptionPane.showInputDialog(this, "Enter Product Price:");
            if (priceStr == null) return;
            int price = Integer.parseInt(priceStr);

            String qtyStr = JOptionPane.showInputDialog(this, "Enter Product Quantity:");
            if (qtyStr == null) return;
            int qty = Integer.parseInt(qtyStr);


            String perishable = JOptionPane.showInputDialog(this, "Is this product perishable? (yes/no):");
            if (perishable != null && perishable.equalsIgnoreCase("yes")) {
                String expiry = JOptionPane.showInputDialog(this, "Enter Expiry Date (YYYY-MM-DD):");
                LocalDate expiryDate = LocalDate.parse(expiry);
                manager.addProduct(id, name, price, qty, expiryDate);
            } else {
                manager.addProduct(id, name, price, qty);
            }

            displayArea.setText("Product added successfully.\n");
            manager.saveToFile();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- VIEW ----------------
    private void viewProducts() {
        displayArea.setText(manager.getFormattedProductList());
    }

    // ---------------- SEARCH ----------------
    private void searchProduct() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Enter Product ID to search:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            int id = Integer.parseInt(idStr);
            String result = manager.searchProductById(id);
            displayArea.setText(result);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- UPDATE ----------------
    private void updateProduct() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Enter Product ID to update:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            int id = Integer.parseInt(idStr);

            String result = manager.searchProductById(id);
            displayArea.setText(result);

            String[] options = {"Name", "Price", "Quantity", "Expiry Date"};
            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "What would you like to update?",
                    "Update Product",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == null) return;

            switch (choice) {
                case "Name" -> {
                    String newName = JOptionPane.showInputDialog(this, "Enter new product name:");
                    if (newName != null && !newName.trim().isEmpty()) {
                        manager.updateProductName(id, newName);
                    }
                }
                case "Price" -> {
                    String newPriceStr = JOptionPane.showInputDialog(this, "Enter new price:");
                    if (newPriceStr != null) {
                        int newPrice = Integer.parseInt(newPriceStr);
                        manager.updateProductPrice(id, newPrice);
                    }
                }
                case "Quantity" -> {
                    String newQtyStr = JOptionPane.showInputDialog(this, "Enter new quantity:");
                    if (newQtyStr != null) {
                        int newQty = Integer.parseInt(newQtyStr);
                        manager.updateProductQuantity(id, newQty);
                    }
                }
                case "Expiry Date" -> {
                    String newDateStr = JOptionPane.showInputDialog(this, "Enter new expiry date (YYYY-MM-DD):");
                    if (newDateStr != null) {
                        LocalDate expiryDate = LocalDate.parse(newDateStr);
                        manager.updateProductExpiry(id, expiryDate);
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            manager.saveToFile();
            viewProducts(); // Refresh the view

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- DELETE ----------------
    private void deleteProduct() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Enter Product ID to delete:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            int id = Integer.parseInt(idStr);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this product?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteProductById(id);
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                manager.saveToFile();
                viewProducts(); // Refresh the view
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- SAVE ----------------
    private void saveInventory() {
        try {
            manager.saveToFile();
            JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving inventory: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        // Set a modern Look and Feel for better UI rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new InventoryGUI().setVisible(true));
    }
}