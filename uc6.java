/**
 * UseCase6RoomAllocationService
 *
 * This class demonstrates booking confirmation and room allocation.
 * It ensures:
 * - FIFO request processing
 * - Unique room assignment using Set
 * - Inventory consistency using HashMap
 *
 * Double-booking is prevented by enforcing uniqueness of room IDs.
 *
 * @author YourName
 * @version 6.0
 */

import java.util.*;

// -------------------- RESERVATION --------------------

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- INVENTORY SERVICE --------------------

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n--- Updated Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// -------------------- BOOKING QUEUE --------------------

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private RoomInventory inventory;

    // Map: Room Type -> Set of allocated room IDs
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Global set to ensure uniqueness across all room IDs
    private Set<String> allAllocatedRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType, int count) {
        return roomType.replace(" ", "").toUpperCase() + "-" + count;
    }

    // Process booking request
    public void processReservation(Reservation reservation) {

        String roomType = reservation.getRoomType();

        // Check availability
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("Booking failed for " + reservation.getGuestName() +
                    " (No rooms available: " + roomType + ")");
            return;
        }

        // Initialize set for room type if not exists
        allocatedRooms.putIfAbsent(roomType, new HashSet<>());

        Set<String> roomSet = allocatedRooms.get(roomType);

        // Generate unique room ID
        String roomId;
        int counter = roomSet.size() + 1;

        do {
            roomId = generateRoomId(roomType, counter++);
        } while (allAllocatedRoomIds.contains(roomId));

        // Allocate room
        roomSet.add(roomId);
        allAllocatedRoomIds.add(roomId);

        // Update inventory immediately
        inventory.decrementRoom(roomType);

        // Confirm reservation
        System.out.println("Booking CONFIRMED for " + reservation.getGuestName());
        System.out.println("Room Type : " + roomType);
        System.out.println("Room ID   : " + roomId);
        System.out.println("----------------------------------------");
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v6.0            ");
        System.out.println("========================================");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Process queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Show final inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated.");
    }
}