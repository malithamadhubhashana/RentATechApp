package com.example.techarent.data;

import com.example.techarent.model.Rental;
import com.example.techarent.model.TechDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<TechDevice> devices;
    private List<Rental> rentals;

    private DataManager() {
        initializeData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initializeData() {
        // Initialize sample devices
        devices = new ArrayList<>();
        devices.add(new TechDevice("1", "iPhone 15 Pro Max", "Apple", "Smartphones",
                "Latest flagship smartphone with advanced camera system, A17 Pro chip, and titanium design. Perfect for photography, gaming, and professional use.",
                45.0, true));
        
        devices.add(new TechDevice("2", "MacBook Pro 16\"", "Apple", "Laptops",
                "High-performance laptop with M2 Max chip, 32GB RAM, and 1TB SSD. Ideal for creative professionals and developers.",
                89.0, true));
        
        devices.add(new TechDevice("3", "iPad Pro 12.9\"", "Apple", "Tablets",
                "Professional tablet with M2 chip, 256GB storage, and Apple Pencil support. Great for digital art and productivity.",
                35.0, true));
        
        devices.add(new TechDevice("4", "Canon EOS R5", "Canon", "Cameras",
                "Professional mirrorless camera with 45MP sensor, 8K video recording, and advanced autofocus system.",
                75.0, false));
        
        devices.add(new TechDevice("5", "PlayStation 5", "Sony", "Gaming",
                "Latest gaming console with lightning-fast loading, 4K gaming, and ray tracing support.",
                25.0, true));
        
        devices.add(new TechDevice("6", "Sony WH-1000XM5", "Sony", "Audio",
                "Premium noise-canceling headphones with exceptional sound quality and 30-hour battery life.",
                15.0, true));

        devices.add(new TechDevice("7", "Samsung Galaxy S24 Ultra", "Samsung", "Smartphones",
                "Android flagship with S Pen, advanced camera system, and powerful performance.",
                42.0, true));

        devices.add(new TechDevice("8", "Dell XPS 15", "Dell", "Laptops",
                "Premium Windows laptop with Intel i7, 16GB RAM, and stunning display.",
                65.0, true));

        // Initialize sample rentals
        rentals = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        // Active rental 1
        cal.set(2024, Calendar.JANUARY, 15);
        Date startDate1 = cal.getTime();
        cal.set(2024, Calendar.JANUARY, 22);
        Date endDate1 = cal.getTime();
        rentals.add(new Rental("r1", "1", "iPhone 15 Pro Max", "Smartphones",
                startDate1, endDate1, 315.0, Rental.RentalStatus.ACTIVE));

        // Active rental 2
        cal.set(2024, Calendar.JANUARY, 18);
        Date startDate2 = cal.getTime();
        cal.set(2024, Calendar.JANUARY, 25);
        Date endDate2 = cal.getTime();
        rentals.add(new Rental("r2", "5", "PlayStation 5", "Gaming",
                startDate2, endDate2, 175.0, Rental.RentalStatus.ACTIVE));

        // Completed rental
        cal.set(2024, Calendar.JANUARY, 1);
        Date startDate3 = cal.getTime();
        cal.set(2024, Calendar.JANUARY, 10);
        Date endDate3 = cal.getTime();
        rentals.add(new Rental("r3", "2", "MacBook Pro 16\"", "Laptops",
                startDate3, endDate3, 890.0, Rental.RentalStatus.COMPLETED));
    }

    public List<TechDevice> getAllDevices() {
        return new ArrayList<>(devices);
    }

    public List<TechDevice> getDevicesByCategory(String category) {
        List<TechDevice> filteredDevices = new ArrayList<>();
        for (TechDevice device : devices) {
            if (device.getCategory().equalsIgnoreCase(category)) {
                filteredDevices.add(device);
            }
        }
        return filteredDevices;
    }

    public List<TechDevice> getAvailableDevices() {
        List<TechDevice> availableDevices = new ArrayList<>();
        for (TechDevice device : devices) {
            if (device.isAvailable()) {
                availableDevices.add(device);
            }
        }
        return availableDevices;
    }

    public TechDevice getDeviceById(String id) {
        for (TechDevice device : devices) {
            if (device.getId().equals(id)) {
                return device;
            }
        }
        return null;
    }

    public List<Rental> getAllRentals() {
        return new ArrayList<>(rentals);
    }

    public List<Rental> getActiveRentals() {
        List<Rental> activeRentals = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getStatus() == Rental.RentalStatus.ACTIVE) {
                activeRentals.add(rental);
            }
        }
        return activeRentals;
    }

    public List<Rental> getRentalHistory() {
        List<Rental> history = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getStatus() != Rental.RentalStatus.ACTIVE) {
                history.add(rental);
            }
        }
        return history;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
        // Mark device as unavailable
        TechDevice device = getDeviceById(rental.getDeviceId());
        if (device != null) {
            device.setAvailable(false);
        }
    }

    public List<String> getCategories() {
        return Arrays.asList("All", "Smartphones", "Laptops", "Tablets", "Cameras", "Gaming", "Audio");
    }

    public List<TechDevice> searchDevices(String query) {
        List<TechDevice> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (TechDevice device : devices) {
            if (device.getName().toLowerCase().contains(lowerQuery) ||
                device.getBrand().toLowerCase().contains(lowerQuery) ||
                device.getCategory().toLowerCase().contains(lowerQuery) ||
                device.getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(device);
            }
        }
        
        return results;
    }
}
