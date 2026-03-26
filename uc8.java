/**
 * UseCase8BookingHistoryReport
 *
 * This class demonstrates maintaining booking history and generating reports.
 * It introduces persistence mindset using in-memory storage (List).
 *
 * Booking history stores confirmed reservations in order.
 * Reporting service reads data without modifying it.
 *
 * @author YourName
 * @version 8.0
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

// -------------------- BOOKING HISTORY --------------------

class BookingHistory {

    // List to maintain ordered history
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored: " + reservation.getReservationId());
    }

    // Retrieve all reservations (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// -------------------- REPORTING SERVICE --------------------

class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display full booking history
    public void displayAllBookings() {
        System.out.println("\n--- Booking History ---\n");

        List<Reservation> reservations = history.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {

        System.out.println("\n--- Booking Summary Report ---\n");

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            summary.put(
                    r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println("Room Type : " + entry.getKey() +
                    " | Total Bookings : " + entry.getValue());
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v8.0            ");
        System.out.println("========================================");

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("SINGLEROOM-1", "Alice", "Single Room");
        Reservation r2 = new Reservation("SINGLEROOM-2", "Bob", "Single Room");
        Reservation r3 = new Reservation("SUITEROOM-1", "Charlie", "Suite Room");

        // Store in history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Display all bookings
        reportService.displayAllBookings();

        // Generate summary
        reportService.generateSummaryReport();

        System.out.println("\nApplication terminated.");
    }
}