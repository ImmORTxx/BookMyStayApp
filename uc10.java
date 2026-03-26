/**
 * UseCase10BookingCancellation
 *
 * This class demonstrates safe booking cancellation with inventory rollback.
 *
 * It uses a Stack to track recently released room IDs (LIFO rollback) and
 * maintains inventory consistency and booking history integrity.
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// -------------------- RESERVATION MODEL --------------------

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("----------------------------------------");
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

// -------------------- BOOKING HISTORY --------------------

class BookingHistory {
    private Map<String, Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new HashMap<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.put(reservation.getReservationId(), reservation);
    }

    public boolean exists(String reservationId) {
        return confirmedReservations.containsKey(reservationId);
    }

    public Reservation removeReservation(String reservationId) {
        return confirmedReservations.remove(reservationId);
    }

    public Collection<Reservation> getAllReservations() {
        return confirmedReservations.values();
    }
}

// -------------------- CANCELLATION SERVICE --------------------

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack; // Tracks recently cancelled reservation IDs

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        if (!history.exists(reservationId)) {
            System.out.println("Cancellation FAILED: Reservation ID not found: " + reservationId);
            System.out.println("----------------------------------------");
            return;
        }

        // Step 1: Retrieve reservation
        Reservation reservation = history.removeReservation(reservationId);

        // Step 2: Rollback inventory
        inventory.increment(reservation.getRoomType());

        // Step 3: Track rollback for LIFO operations if needed
        rollbackStack.push(reservationId);

        // Step 4: Confirmation
        System.out.println("Booking CANCELLED successfully for Reservation ID: " + reservationId);
        System.out.println("Room Type inventory restored: " + reservation.getRoomType());
        System.out.println("----------------------------------------");
    }

    // Optional: Undo last cancellation
    public void undoLastCancellation() {
        if (rollbackStack.isEmpty()) {
            System.out.println("No cancellations to undo.");
            return;
        }

        String reservationId = rollbackStack.pop();
        System.out.println("Undoing cancellation for Reservation ID: " + reservationId);
        // For simplicity, actual restoration logic would require original Reservation object
        // This could be enhanced in a real system
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v10.0           ");
        System.out.println("========================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SINGLEROOM-1", "Alice", "Single Room");
        Reservation r2 = new Reservation("DOUBLEROOM-1", "Bob", "Double Room");
        Reservation r3 = new Reservation("SUITEROOM-1", "Charlie", "Suite Room");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Display initial reservations
        System.out.println("\n--- Confirmed Bookings ---");
        for (Reservation r : history.getAllReservations()) {
            r.display();
        }

        // Initialize cancellation service
        CancellationService cancellationService = new CancellationService(inventory, history);

        // Perform cancellations
        cancellationService.cancelBooking("DOUBLEROOM-1"); // valid
        cancellationService.cancelBooking("NONEXISTENT-1"); // invalid

        // Display remaining reservations
        System.out.println("\n--- Remaining Confirmed Bookings ---");
        for (Reservation r : history.getAllReservations()) {
            r.display();
        }

        System.out.println("\nApplication terminated.");
    }
}