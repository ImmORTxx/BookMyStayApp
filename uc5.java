/**
 * UseCase5BookingRequestQueue
 *
 * This class demonstrates handling booking requests using a Queue
 * to ensure First-Come-First-Served (FIFO) processing.
 *
 * No inventory updates or allocations are performed at this stage.
 * The system only captures and stores booking intent.
 *
 * @author YourName
 * @version 5.0
 */

import java.util.*;

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

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}

// -------------------- BOOKING REQUEST QUEUE --------------------

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all queued requests
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO Order) ---\n");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        for (Reservation reservation : requestQueue) {
            reservation.displayReservation();
            System.out.println("----------------------------------------");
        }
    }

    // Peek next request (without removing)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v5.0            ");
        System.out.println("========================================");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue (FIFO)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queue
        bookingQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            next.displayReservation();
        }

        System.out.println("\nNote: No rooms are allocated at this stage.");
        System.out.println("Application terminated.");
    }
}