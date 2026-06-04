package com.busguidance;

// Represents a bus with validation for all fields.
// Enforces conditions B1-B5 as per specification.
public class Bus {

    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    // Constructor that validates all fields on creation.
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        if (!isValidBusID(busID)) {
            throw new IllegalArgumentException("Invalid busID: " + busID);
        }
        if (!isValidFuelType(fuelType)) {
            throw new IllegalArgumentException("Invalid fuel type: " + fuelType);
        }
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // B1: busID format.
    // Exactly 8 characters, all must be digits.
    public static boolean isValidBusID(String busID) {
        if (busID == null || busID.length() != 8) return false;
        for (char c : busID.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    // Validates fuel type is one of the accepted values.
    public static boolean isValidFuelType(String fuelType) {
        return fuelType != null && (
                fuelType.equals("Diesel") ||
                        fuelType.equals("Hybrid") ||
                        fuelType.equals("Electricity")
        );
    }

    // Checks if a driver is allowed to drive this bus.
    public boolean isDriverEligible(Driver driver) {
        // B3: drivers older than 50 cannot drive buses with capacity >= 50
        if (driver.getAge() > 50 && this.capacity >= 50) {
            return false;
        }

        // B4: only drivers with at least 5 years experience can drive electric buses
        if (this.fuelType.equals("Electricity") && driver.getExperienceYears() < 5) {
            return false;
        }

        // B5: only Heavy or PublicTransport licence can drive electric or hybrid buses
        if (this.fuelType.equals("Electricity") || this.fuelType.equals("Hybrid")) {
            String license = driver.getLicenseType();
            if (!license.equals("Heavy") && !license.equals("PublicTransport")) {
                return false;
            }
        }

        return true;
    }

    // Getters
    public String getBusID() { return busID; }
    public int getCapacity() { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType() { return fuelType; }

    // B1: busID uniqueness is handled by repository
    public void setBusID(String busID) {
        if (!isValidBusID(busID)) {
            throw new IllegalArgumentException("Invalid busID: " + busID);
        }
        this.busID = busID;
    }

    // B2: capacity cannot increase during update
    public void setCapacity(int newCapacity) {
        if (newCapacity > this.capacity) {
            throw new IllegalArgumentException("Bus capacity cannot be increased during update.");
        }
        this.capacity = newCapacity;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        if (!isValidFuelType(fuelType)) {
            throw new IllegalArgumentException("Invalid fuel type: " + fuelType);
        }
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return busID + "|" + capacity + "|" + fuelLevel + "|" + fuelType;
    }
}