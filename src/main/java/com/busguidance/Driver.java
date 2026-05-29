package com.busguidance;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Represents a bus driver with validation for all fields.
// Enforces conditions D1-D5 as per specification.
public class Driver {

    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType; // Light, Medium, Heavy, PublicTransport
    private String address;
    private String birthdate; // DD-MM-YYYY

    // Constructor that validates all fields on creation.
    public Driver(String driverID, String name, int experienceYears,
                  String licenseType, String address, String birthdate) {
        if (!isValidDriverID(driverID)) {
            throw new IllegalArgumentException("Invalid driverID: " + driverID);
        }
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address format: " + address);
        }
        if (!isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate format: " + birthdate);
        }
        if (!isValidLicenseType(licenseType)) {
            throw new IllegalArgumentException("Invalid license type: " + licenseType);
        }
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // D1: driver ID format:
    // Exactly 10 characters.
    // First two chars are digits 2-9.
    // At least two special characters between positions 3-8.
    // Last two chars are uppercase letters A-Z.
    public static boolean isValidDriverID(String driverID) {
        if (driverID == null || driverID.length() != 10) return false;

        // First two characters must be digits 2-9
        if (!Character.isDigit(driverID.charAt(0)) || !Character.isDigit(driverID.charAt(1))) return false;
        if (driverID.charAt(0) < '2' || driverID.charAt(0) > '9') return false;
        if (driverID.charAt(1) < '2' || driverID.charAt(1) > '9') return false;

        // Last two characters must be uppercase letters A-Z
        if (!Character.isUpperCase(driverID.charAt(8)) || !Character.isUpperCase(driverID.charAt(9))) return false;

        // At least two special characters between positions 3-8 (index 2-7)
        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = driverID.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }
        return specialCount >= 2;
    }

    // D2: address format:
    // Format: StreetNumber|StreetName|City|State|Country
    public static boolean isValidAddress(String address) {
        if (address == null) return false;
        String[] parts = address.split("\\|");
        return parts.length == 5 && java.util.Arrays.stream(parts).allMatch(p -> !p.trim().isEmpty());
    }

    // D3: birthdate format:
    // Format: DD-MM-YYYY
    public static boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(birthdate, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Validates license type is one of the accepted values.
    public static boolean isValidLicenseType(String licenseType) {
        return licenseType != null && (
                licenseType.equals("Light") ||
                        licenseType.equals("Medium") ||
                        licenseType.equals("Heavy") ||
                        licenseType.equals("PublicTransport")
        );
    }

    // Calculates driver age from birthdate.
    public int getAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(this.birthdate, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Getters
    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType() { return licenseType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }

    // D5: driverID cannot be modified
    public void setDriverID(String driverID) {
        throw new UnsupportedOperationException("driverID cannot be modified.");
    }

    // D5: name cannot be modified
    public void setName(String name) {
        throw new UnsupportedOperationException("Driver name cannot be modified.");
    }

    // D4: licenseType cannot be changed if experience > 10 years
    public void setLicenseType(String licenseType) {
        if (this.experienceYears > 10) {
            throw new UnsupportedOperationException("Cannot change license type for drivers with more than 10 years of experience.");
        }
        if (!isValidLicenseType(licenseType)) {
            throw new IllegalArgumentException("Invalid license type: " + licenseType);
        }
        this.licenseType = licenseType;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setAddress(String address) {
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address format.");
        }
        this.address = address;
    }

    public void setBirthdate(String birthdate) {
        if (!isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate format.");
        }
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return driverID + "|" + name + "|" + experienceYears + "|" +
                licenseType + "|" + address + "|" + birthdate;
    }
}