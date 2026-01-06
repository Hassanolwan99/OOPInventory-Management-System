package System;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private static final String FILE =
            "C:\\Users\\hassa\\Desktop\\4.2.1\\Object Oriented Programming\\project\\data.csv";

    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        StockManager stockManager = new StockManager(inventory);
        OrderService orderService = new OrderService(stockManager);

        // Load from file on startup
        CsvStore.loadProducts(FILE, inventory);
        System.out.println("Loaded from CSV. Products count = " + inventory.size());
        System.out.println("CSV file: " + FILE);

        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addProduct(sc, inventory);
                    case "2" -> removeProduct(sc, inventory);
                    case "3" -> searchProduct(sc, inventory);
                    case "4" -> updateStock(sc, inventory, stockManager); // FIXED
                    case "5" -> showLowStock(inventory);
                    case "6" -> placeOrder(sc, inventory, orderService);
                    case "7" -> printAll(inventory);
                    case "8" -> reloadFromFile(inventory);
                    case "0" -> {
                        System.out.println("Bye");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========== Inventory (FILE-BASED) ==========");
        System.out.println("1) Add Product ");
        System.out.println("2) Remove Product by SKU ");
        System.out.println("3) Search Product ");
        System.out.println("4) Update Stock (increase/decrease) ");
        System.out.println("5) Show Low Stock Products");
        System.out.println("6) Place Order (deduct stock) ");
        System.out.println("7) Print All Products");
        System.out.println("8) Reload from CSV now");
        System.out.println("0) Exit");
        System.out.println("============================================");
    }

    // ---------------- File helpers ----------------

    private static void saveNow(Inventory inventory) {
        CsvStore.saveProducts(FILE, inventory);
        System.out.println("Saved to CSV.");
    }

    private static void reloadFromFile(Inventory inventory) {
        CsvStore.reloadProducts(FILE, inventory);
        System.out.println("Reloaded from CSV. Products count = " + inventory.size());
    }

    // ---------------- Features ----------------

    private static void addProduct(Scanner sc, Inventory inventory) {
        System.out.print("SKU: ");
        String sku = sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Category (optional): ");
        String category = sc.nextLine();
        if (category == null || category.isBlank()) category = "General";

        System.out.print("Unit Price: ");
        double price = Double.parseDouble(sc.nextLine());

        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());

        System.out.print("Min Stock Level: ");
        int min = Integer.parseInt(sc.nextLine());

        System.out.print("Max Stock Level: ");
        int max = Integer.parseInt(sc.nextLine());

        Product p = new Product(sku, name, category, price, qty, min, max);
        boolean ok = inventory.addProduct(p);

        if (ok) {
            saveNow(inventory);
            System.out.println("Product added.");
        } else {
            System.out.println("Not added (duplicate SKU or invalid).");
        }
    }

    private static void removeProduct(Scanner sc, Inventory inventory) {
        System.out.print("Enter SKU to remove: ");
        String sku = sc.nextLine();

        boolean ok = inventory.removeBySku(sku);
        if (ok) {
            saveNow(inventory);
            System.out.println("Removed.");
        } else {
            System.out.println("SKU not found.");
        }
    }

    private static void searchProduct(Scanner sc, Inventory inventory) {
        // reload to guarantee search is from the file
        reloadFromFile(inventory);

        System.out.println("1) Search by SKU");
        System.out.println("2) Search by Name keyword");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();

        if (c.equals("1")) {
            System.out.print("SKU: ");
            String sku = sc.nextLine();
            Product p = inventory.findBySku(sku);
            System.out.println(p == null ? "Not found." : p);
        } else if (c.equals("2")) {
            System.out.print("Keyword: ");
            String k = sc.nextLine();
            List<Product> list = inventory.searchByName(k);
            if (list.isEmpty()) System.out.println("No matches.");
            else list.forEach(System.out::println);
        } else {
            System.out.println("Invalid.");
        }
    }

    // FIXED: now we receive inventory, so we can save correctly
    private static void updateStock(Scanner sc, Inventory inventory, StockManager stockManager) {
        System.out.println("1) Increase Stock");
        System.out.println("2) Decrease Stock");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();

        System.out.print("SKU: ");
        String sku = sc.nextLine();

        System.out.print("Amount: ");
        int amount = Integer.parseInt(sc.nextLine());

        if (c.equals("1")) {
            stockManager.increaseStock(sku, amount);
        } else if (c.equals("2")) {
            stockManager.decreaseStock(sku, amount);
        } else {
            System.out.println("Invalid.");
            return;
        }

        saveNow(inventory);
        System.out.println("New stock = " + stockManager.getStock(sku));
    }

    private static void showLowStock(Inventory inventory) {
        List<Product> low = inventory.getLowStockProducts();
        if (low.isEmpty()) {
            System.out.println("No low stock products.");
            return;
        }
        System.out.println("--- Low Stock ---");
        for (Product p : low) {
            System.out.println(p.getSku() + " | " + p.getName() +
                    " | qty=" + p.getQuantity() +
                    " | min=" + p.getMinStockLevel());
        }
    }

    private static void placeOrder(Scanner sc, Inventory inventory, OrderService orderService) {
        System.out.print("Order ID: ");
        String orderId = sc.nextLine();

        Order order = new Order(orderId);

        while (true) {
            System.out.print("Enter Product SKU to add (or 'done'): ");
            String sku = sc.nextLine().trim();
            if (sku.equalsIgnoreCase("done")) break;

            reloadFromFile(inventory);

            Product p = inventory.findBySku(sku);
            if (p == null) {
                System.out.println("SKU not found.");
                continue;
            }

            System.out.print("Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());

            order.addItem(p, qty);
            System.out.println("Item added.");
        }

        orderService.placeOrder(order);
        saveNow(inventory);

        System.out.println("Order placed. Status = " + order.getStatus());
        System.out.println("Total = " + order.getTotalAmount());
    }

    private static void printAll(Inventory inventory) {
        reloadFromFile(inventory);
        inventory.printAllProducts();
    }
}
