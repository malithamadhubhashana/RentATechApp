package com.example.techarent.model;

public class TechDevice {
    private String id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private double pricePerDay;
    private boolean isAvailable;
    private String imageUrl;
    private String specifications;

    public TechDevice() {}

    public TechDevice(String id, String name, String brand, String category, 
                     String description, double pricePerDay, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSpecifications() { return specifications; }
    public void setSpecifications(String specifications) { this.specifications = specifications; }

    public String getCategoryEmoji() {
        switch (category.toLowerCase()) {
            case "smartphones": return "📱";
            case "laptops": return "💻";
            case "tablets": return "📱";
            case "cameras": return "📷";
            case "gaming": return "🎮";
            case "audio": return "🎧";
            default: return "📱";
        }
    }
}
