/**
 * UseCase7AddOnServiceSelection
 *
 * This class demonstrates how add-on services can be attached to an
 * existing reservation without modifying core booking or inventory logic.
 *
 * It uses a Map<String, List<Service>> to maintain a one-to-many
 * relationship between reservation IDs and selected services.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// -------------------- SERVICE MODEL --------------------

class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println("Service : " + serviceName + " | Cost : $" + cost);
    }
}

// -------------------- ADD-ON SERVICE MANAGER --------------------

class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service '" + service.getServiceName() +
                "' to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        System.out.println("\n--- Services for Reservation ID: " + reservationId + " ---");

        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (Service service : services) {
            service.displayService();
        }
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null) return 0.0;

        double total = 0.0;
        for (Service service : services) {
            total += service.getCost();
        }
        return total;
    }
}

// -------------------- MAIN APPLICATION --------------------

public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay Application  ");
        System.out.println("   Hotel Booking System v7.0            ");
        System.out.println("========================================");

        // Simulated reservation ID (from Use Case 6)
        String reservationId = "SINGLEROOM-1";

        // Initialize Add-On Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 20.0);
        Service airportPickup = new Service("Airport Pickup", 50.0);
        Service spa = new Service("Spa Access", 80.0);

        // Guest selects services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        // Display selected services
        manager.displayServices(reservationId);

        // Calculate total cost
        double totalCost = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: $" + totalCost);

        System.out.println("\nNote: Booking and inventory remain unchanged.");
        System.out.println("Application terminated.");
    }
}