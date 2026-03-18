/**
 * UseCase12DataPersistenceRecovery
 *
 * Demonstrates persistence of hotel booking system state
 * (inventory and booking history) and recovery after application restart.
 * Uses Java serialization to save/load data to/from a file.
 *
 * Author: YourName
 * Version: 12.0
 */

import java.io.*;
import java.util.*;

// -------------------- ROOM INVENTORY --------------------

class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    @Override
    public String toString() {
        return "Single Room: " + getAvailability("Single Room") +
                ", Double Room: " + getAvailability("Double Room") +
                ", Suite Room: " + getAvailability("Suite Room");
    }
}

// -------------------- RESERVATION --------------------

class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// -------------------- PERSISTENCE SERVICE --------------------

class PersistenceService {

    private static final String INVENTORY_FILE = "inventory.dat";
    private static final String HISTORY_FILE = "booking_history.dat";

    public static void saveInventory(RoomInventory inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static RoomInventory loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
            return (RoomInventory) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous inventory found. Initializing new inventory.");
            return new RoomInventory();
        }
    }

    public static void saveBookingHistory(List<Reservation> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
            oos.writeObject(history);
        } catch (IOException e) {
            System.err.println("Error saving booking history: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Reservation> loadBookingHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HISTORY_FILE))) {
            return (List<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous booking history found. Starting fresh.");
            return new ArrayList<>();
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" Book My Stay - Data Persistence Demo");
        System.out.println(" Hotel Booking System v12.0");
        System.out.println("========================================");

        // Load previous state if available
        RoomInventory inventory = PersistenceService.loadInventory();
        List<Reservation> bookingHistory = PersistenceService.loadBookingHistory();

        System.out.println("Current Inventory: " + inventory);
        System.out.println("Booking History (" + bookingHistory.size() + "):");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        // Simulate new bookings
        Reservation res1 = new Reservation("R100", "Alice", "Single Room");
        if (inventory.allocateRoom(res1.getRoomType())) bookingHistory.add(res1);

        Reservation res2 = new Reservation("R101", "Bob", "Double Room");
        if (inventory.allocateRoom(res2.getRoomType())) bookingHistory.add(res2);

        System.out.println("\nAfter New Bookings:");
        System.out.println("Inventory: " + inventory);
        System.out.println("Booking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        // Persist state for next run
        PersistenceService.saveInventory(inventory);
        PersistenceService.saveBookingHistory(bookingHistory);

        System.out.println("\nSystem state saved successfully. Application terminated.");
    }
}