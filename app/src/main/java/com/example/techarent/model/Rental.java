package com.example.techarent.model;

import java.util.Date;

public class Rental {
    private String id;
    private String deviceId;
    private String deviceName;
    private String deviceCategory;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    private RentalStatus status;

    public enum RentalStatus {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

    public Rental() {}

    public Rental(String id, String deviceId, String deviceName, String deviceCategory,
                 Date startDate, Date endDate, double totalCost, RentalStatus status) {
        this.id = id;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceCategory = deviceCategory;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceCategory() { return deviceCategory; }
    public void setDeviceCategory(String deviceCategory) { this.deviceCategory = deviceCategory; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }

    public String getCategoryEmoji() {
        switch (deviceCategory.toLowerCase()) {
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
