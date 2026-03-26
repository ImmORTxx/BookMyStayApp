/**
 * UseCase3InventorySetup
 *
 * This class demonstrates centralized inventory management using HashMap.
 * It replaces scattered availability variables with a single source of truth.
 *
 * The RoomInventory class encapsulates all inventory-related operations.
 *
 * @author YourName
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory Management Class
class RoomInventory {

    // HashMap to store room type -> available count
    private Map<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Method to display all inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---\n");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type       : " + entry.getKey());
            System.out.println("Available Rooms : " + entry.getValue());
            System.out.println("----------------------------------------");
        }
    }
}

// Main Application Class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v3.1            ");
        System.out.println("========================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Demonstrate retrieval
        System.out.println("\nChecking availability for Double Room...");
        int available = inventory.getAvailability("Double Room");
        System.out.println("Available Double Rooms: " + available);

        // Demonstrate update
        System.out.println("\nUpdating availability for Double Room...");
        inventory.updateAvailability("Double Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("Application terminated.");
    }
}