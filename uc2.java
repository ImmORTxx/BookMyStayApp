/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates basic object-oriented design using abstraction,
 * inheritance, and polymorphism for a Hotel Booking System.
 *
 * It initializes different room types and displays their details along with
 * static availability.
 *
 * @author YourName
 * @version 2.1
 */

// Abstract class representing a generic Room
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;

    // Constructor
    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    // Method to display room details
    public void displayRoomDetails() {
        System.out.println("Room Type       : " + roomType);
        System.out.println("Number of Beds  : " + numberOfBeds);
        System.out.println("Price per Night : $" + pricePerNight);
    }
}

// Concrete class: Single Room
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

// Concrete class: Double Room
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

// Concrete class: Suite Room
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// Main application class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v2.1            ");
        System.out.println("========================================");

        // Create room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        // Display details
        System.out.println("\n--- Room Details & Availability ---\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms : " + singleRoomAvailable);
        System.out.println("----------------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms : " + doubleRoomAvailable);
        System.out.println("----------------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms : " + suiteRoomAvailable);
        System.out.println("----------------------------------------");

        System.out.println("Application terminated.");
    }
}
