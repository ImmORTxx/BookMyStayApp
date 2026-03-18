/**
 * UseCase9ErrorHandlingValidation
 *
 * This class demonstrates structured validation and error handling
 * using custom exceptions and fail-fast principles.
 *
 * It ensures invalid inputs are rejected before affecting system state.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// -------------------- CUSTOM EXCEPTION --------------------

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------------------- RESERVATION MODEL --------------------

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

// -------------------- INVENTORY --------------------

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        inventory.put(roomType, available - 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

// -------------------- VALIDATOR --------------------

class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type existence
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + reservation.getRoomType());
        }
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {
            // Step 1: Validate (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate (only if valid)
            inventory.decrement(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("Booking CONFIRMED for " + reservation.getGuestName());
            System.out.println("Room Type : " + reservation.getRoomType());
            System.out.println("----------------------------------------");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking FAILED: " + e.getMessage());
            System.out.println("----------------------------------------");
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v9.0            ");
        System.out.println("========================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Alice", "Single Room");   // valid
        Reservation r2 = new Reservation("", "Double Room");        // invalid name
        Reservation r3 = new Reservation("Bob", "Luxury Room");     // invalid room type
        Reservation r4 = new Reservation("Charlie", "Suite Room");  // no availability

        // Process bookings
        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4);

        System.out.println("\nApplication continues running safely.");
        System.out.println("Application terminated.");
    }
}