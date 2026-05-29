package com.busguidance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Repository for managing Bus records using a TXT file for persistence.
// Supports Add, Retrieve, Update, and Count operations.
public class BusRepository {

    private static final String FILE_PATH = "buses.txt";

    // Adds a new bus to the TXT file.
    // Validates that the busID is unique before adding.
    public boolean add(Bus bus) throws IOException {
        if (retrieve(bus.getBusID()) != null) {
            throw new IllegalArgumentException("Bus ID already exists: " + bus.getBusID());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(bus.toString());
            writer.newLine();
        }
        return true;
    }

    // Retrieves a bus by busID from the TXT file.
    // Returns null if not found.
    public Bus retrieve(String busID) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Format: busID|capacity|fuelLevel|fuelType
                if (parts.length == 4 && parts[0].equals(busID)) {
                    String id = parts[0];
                    int capacity = Integer.parseInt(parts[1]);
                    double fuelLevel = Double.parseDouble(parts[2]);
                    String fuelType = parts[3];
                    return new Bus(id, capacity, fuelLevel, fuelType);
                }
            }
        }
        return null;
    }

    // Updates an existing bus's details in the TXT file.
    // B2: capacity cannot increase
    public boolean update(Bus updatedBus) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4 && parts[0].equals(updatedBus.getBusID())) {
                    int originalCapacity = Integer.parseInt(parts[1]);
                    // B2: capacity cannot increase
                    if (updatedBus.getCapacity() > originalCapacity) {
                        throw new IllegalArgumentException("Bus capacity cannot be increased during update.");
                    }
                    lines.add(updatedBus.toString());
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

    // Returns the number of buses stored in the TXT file.
    public int count() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return 0;

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            while (reader.readLine() != null) count++;
        }
        return count;
    }

    // Returns all buses stored in the TXT file.
    public List<Bus> retrieveAll() throws IOException {
        List<Bus> buses = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return buses;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    buses.add(new Bus(parts[0], Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2]), parts[3]));
                }
            }
        }
        return buses;
    }

    // Deletes the TXT file - used for cleaning up in integration tests.
    public void clearAll() {
        new File(FILE_PATH).delete();
    }
}