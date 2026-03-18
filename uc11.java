/**
 * UseCase11ConcurrentBookingSimulation
 *
 * Demonstrates concurrent booking requests and thread-safe room allocation.
 * Uses synchronization to prevent race conditions on shared inventory and booking queue.
 *
 * Author: YourName
 * Version: 11.0
 */

import java.util.*;

// -------------------- RESERVATION --------------------

class Reservation {
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
}

// -------------------- THREAD-SAFE INVENTORY --------------------

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    // synchronized to prevent race conditions
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    public synchronized void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// -------------------- BOOKING QUEUE --------------------

class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean hasRequests() {
        return !queue.isEmpty();
    }
}

// -------------------- BOOKING PROCESSOR THREAD --------------------

class BookingProcessor extends Thread {

    private BookingQueue bookingQueue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue bookingQueue, RoomInventory inventory) {
        super(name);
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (bookingQueue.hasRequests()) {
            Reservation res = bookingQueue.getNextRequest();
            if (res != null) {
                boolean success = inventory.allocateRoom(res.getRoomType());
                if (success) {
                    System.out.println(getName() + " confirmed booking for " + res.getGuestName()
                            + " (" + res.getRoomType() + ")");
                } else {
                    System.out.println(getName() + " FAILED to book " + res.getGuestName()
                            + " (" + res.getRoomType() + ") - No availability");
                }
                // Simulate processing time
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Book My Stay - Concurrent Booking     ");
        System.out.println("   Hotel Booking System v11.0            ");
        System.out.println("========================================");

        // Shared resources
        RoomInventory inventory = new RoomInventory();
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate multiple guests submitting requests
        bookingQueue.addRequest(new Reservation("R1", "Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("R2", "Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("R3", "Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("R4", "David", "Single Room"));
        bookingQueue.addRequest(new Reservation("R5", "Eve", "Double Room")); // No availability

        // Start multiple threads to process bookings concurrently
        BookingProcessor processor1 = new BookingProcessor("Thread-1", bookingQueue, inventory);
        BookingProcessor processor2 = new BookingProcessor("Thread-2", bookingQueue, inventory);

        processor1.start();
        processor2.start();

        try {
            processor1.join();
            processor2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Inventory Status:");
        System.out.println("Single Room  : " + inventory.getAvailability("Single Room"));
        System.out.println("Double Room  : " + inventory.getAvailability("Double Room"));
        System.out.println("Suite Room   : " + inventory.getAvailability("Suite Room"));

        System.out.println("\nApplication terminated.");
    }
}