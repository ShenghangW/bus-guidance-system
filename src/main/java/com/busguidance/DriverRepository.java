package com.busguidance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Repository for managing Driver records using a TXT file for persistence.
// Supports Add, Retrieve, Update, and Count operations.
public class DriverRepository {

    private static final String FILE_PATH = "drivers.txt";

    // Adds a new driver to the TXT file.
    // Validates that the driverID is unique before adding.
    public boolean add(Driver driver) throws IOException {
        if (retrieve(driver.getDriverID()) != null) {
            throw new IllegalArgumentException("Driver ID already exists: " + driver.getDriverID());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(driver.toString());
            writer.newLine();
        }
        return true;
    }

    // Retrieves a driver by driverID from the TXT file.
    // Returns null if not found.
    public Driver retrieve(String driverID) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Format: driverID|name|experienceYears|licenseType|street|streetName|city|state|country|birthdate
                if (parts.length >= 10 && parts[0].equals(driverID)) {
                    String id = parts[0];
                    String name = parts[1];
                    int experience = Integer.parseInt(parts[2]);
                    String license = parts[3];
                    // Address is parts 4-8 joined with |
                    String address = parts[4] + "|" + parts[5] + "|" + parts[6] + "|" + parts[7] + "|" + parts[8];
                    String birthdate = parts[9];
                    return new Driver(id, name, experience, license, address, birthdate);
                }
            }
        }
        return null;
    }

    // Updates an existing driver's details in the TXT file.
    // Enforces D4 and D5 immutability rules.
    public boolean update(Driver updatedDriver) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 10 && parts[0].equals(updatedDriver.getDriverID())) {
                    // D5: keep original name (cannot be changed)
                    // D4: keep original license if experience > 10
                    int originalExperience = Integer.parseInt(parts[2]);
                    String licenseToUse;
                    if (originalExperience > 10) {
                        licenseToUse = parts[3]; // keep original license
                    } else {
                        licenseToUse = updatedDriver.getLicenseType();
                    }
                    String updatedLine = parts[0] + "|" + parts[1] + "|" +
                            updatedDriver.getExperienceYears() + "|" + licenseToUse + "|" +
                            updatedDriver.getAddress() + "|" + updatedDriver.getBirthdate();
                    lines.add(updatedLine);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            }
        }
        return found;
    }

    // Returns the number of drivers stored in the TXT file.
    public int count() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return 0;

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            while (reader.readLine() != null) count++;
        }
        return count;
    }

    // Returns all drivers stored in the TXT file.
    public List<Driver> retrieveAll() throws IOException {
        List<Driver> drivers = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return drivers;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 10) {
                    String address = parts[4] + "|" + parts[5] + "|" + parts[6] + "|" + parts[7] + "|" + parts[8];
                    drivers.add(new Driver(parts[0], parts[1], Integer.parseInt(parts[2]),
                            parts[3], address, parts[9]));
                }
            }
        }
        return drivers;
    }

    // Deletes the TXT file - used for cleaning up in integration tests.
    public void clearAll() {
        new File(FILE_PATH).delete();
    }
}