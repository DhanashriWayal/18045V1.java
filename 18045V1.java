import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Product 
{
    private String name;
    private int price;

    public Product(String name, int price) 
    {
        this.name = name;
        this.price = price;
    }

    public String getName() 
    {
        return name;
    }

    public int getPriceInINR() 
    {
        return price;
    }

    public String toString() 
    {
        return name + ": " + formatCurrency(getPriceInINR());
    }

    private String formatCurrency(int amount) 
    {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Customer 
{
    private String name;

    public Customer(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
}

class OrderItem 
{
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) 
    {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() 
    {
        return product;
    }

    public int getQuantity() 
    {
        return quantity;
    }

    public int getTotalPriceInINR() 
    {
        return product.getPriceInINR() * quantity;
    }

    public String toString() 
    {
        return product.getName() + " (x" + quantity + "): " + formatCurrency(getTotalPriceInINR());
    }

    private String formatCurrency(int amount) 
    {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Order 
{
    private Customer customer;
    private List<OrderItem> orderItems;
    private int totalAmount = 0;

    public Order(Customer customer) 
    {
        this.customer = customer;
        this.orderItems = new ArrayList<>();
    }

    public Customer getCustomer() 
    {
        return customer;
    }

    public void addProduct(Product product, int quantity) 
    {
        orderItems.add(new OrderItem(product, quantity));
        totalAmount += product.getPriceInINR() * quantity;
    }

    public void removeProduct(String productName) 
    {
        boolean found = false;
        for (int i = 0; i < orderItems.size(); i++) 
        {
            if (orderItems.get(i).getProduct().getName().equalsIgnoreCase(productName)) 
            {
                OrderItem item = orderItems.remove(i);
                totalAmount -= item.getTotalPriceInINR();
                System.out.println("Removed " + item.getQuantity() + " x " + productName + " from your order.");
                found = true;
                break;
            }
        }
        if (!found) 
        {
            System.out.println("Product \"" + productName + "\" not found in your order.");
        }
    }

    public void printOrder() 
    {
        System.out.println("Order for " + customer.getName() + ":");
        for (OrderItem item : orderItems) 
        {
            System.out.println(item);
        }
        System.out.println("Total Amount: " + formatCurrency(totalAmount));
    }

    private String formatCurrency(int amount) 
    {
        return "Rupees " + String.format("%,d", amount);
    }
}

class ProductService 
{
    private static Product[] products = 
    {
        new Product("Pen", 10),
        new Product("Notebook", 50),
        new Product("Eraser", 5),
        new Product("Marker", 15),
        new Product("Folder", 20),
        new Product("Pencil", 5),
        new Product("Highlighter", 20),
        new Product("Stapler", 55),
        new Product("Glue", 25),
        new Product("Scissors", 60)
    };

    public static void displayProducts() 
    {
        System.out.println("Available products:");
        for (int i = 0; i < products.length; i++) 
        {
            System.out.println((i + 1) + ". " + products[i]);
        }
    }

    public static int askForProductChoice(Scanner scanner) 
    {
        System.out.print("Select a product by number (or type 0 to finish): ");
        return scanner.nextInt();
    }

    public static int askForQuantity(Scanner scanner, int choice) 
    {
        System.out.print("Enter quantity for " + getProduct(choice).getName() + ": ");
        return scanner.nextInt();
    }

    public static void addProductToOrder(Order order, int choice, int quantity) 
    {
        order.addProduct(getProduct(choice), quantity);
    }

    public static Product getProduct(int choice) 
    {
        if (choice > 0 && choice <= products.length) 
        {
            return products[choice - 1];
        }
        return null;
    }
}

public class StationeryShopA 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        Customer customer = new Customer(customerName);
        Order order = new Order(customer);
        ProductService.displayProducts();
        boolean finished = false;
        int productLimit = 50;
        int selectedProducts = 0;

        while (!finished && selectedProducts < productLimit) 
        {
            System.out.println("\nMenu:");
            System.out.println("1. Add product");
            System.out.println("2. Remove product");
            System.out.println("3. Finish order");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (option) 
            {
                case 1: // Add product
                    int choice = ProductService.askForProductChoice(scanner);
                    if (choice == 0) 
                    {
                        System.out.println("Returning to menu...");
                    } else 
                    {
                        int quantity = ProductService.askForQuantity(scanner, choice);
                        if (quantity > 0) 
                        {
                            ProductService.addProductToOrder(order, choice, quantity);
                            selectedProducts++;
                            System.out.println("Added " + quantity + " x " + ProductService.getProduct(choice).getName() + " to your order.");
                        } 
                        else 
                        {
                            System.out.println("Quantity must be greater than 0.");
                        }
                    }
                    break;

                case 2: 
                    System.out.print("Enter the name of the product to remove: ");
                    String productName = scanner.nextLine();
                    order.removeProduct(productName);
                    break;

                case 3:
                    finished = true;
                    break;

                default:
                    System.out.println("Invalid option. Please select again.");
            }

            if (selectedProducts == productLimit) 
            {
                System.out.println("Product limit reached. You can select a maximum of " + productLimit + " products.");
                finished = true;
            }
        }

        order.printOrder();
        scanner.close();
    }
}
