/**
 * UseCase4RoomSearch
 *
 * This class demonstrates room search functionality using read-only access
 * to a centralized inventory. It ensures that system state is not modified
 * during search operations.
 *
 * @author YourName
 * @version 4.0
 */

import java.util.*;

// -------------------- DOMAIN MODEL --------------------

// Abstract Room class
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type       : " + roomType);
        System.out.println("Beds            : " + numberOfBeds);
        System.out.println("Price/Night     : $" + pricePerNight);
    }
}

// Concrete Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// -------------------- INVENTORY (READ-ONLY ACCESS) --------------------

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // intentionally unavailable
        inventory.put("Suite Room", 2);
    }

    // Read-only access method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose all room types (read-only usage)
    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

// -------------------- SEARCH SERVICE --------------------

class RoomSearchService {

    private RoomInventory inventory;
    private List<Room> rooms;

    public RoomSearchService(RoomInventory inventory, List<Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    // Search available rooms (read-only operation)
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Validation: show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Rooms : " + available);
                System.out.println("----------------------------------------");
            }
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v4.0            ");
        System.out.println("========================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize room domain objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory, rooms);

        // Perform search (read-only)
        searchService.searchAvailableRooms();

        System.out.println("Application terminated.");
    }
}