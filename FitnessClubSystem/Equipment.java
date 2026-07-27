package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;

//Used by Admin to update status.
//Allow Admin to update equipment status

public class Equipment {

    private String equipmentId; // Unique equipment identifier
    private String name; // Equipment name
    private String status; // example: "Available", "Under Maintenance", "Broken"

    // Parameterized constructor for Equipment
    public Equipment(String equipmentId, String name, String status) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.status = status;
    }
    
    //Setters
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }
    public void setName(String name) { this.name = name; }
    public void setStatus(String status) { this.status = status; }

    // Getters (For use by Admin and FileManager) 
    public String getEquipmentId() { 
        return equipmentId; 
    }
    
    public String getName() {
        return name; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    //Formatting string used for file saving 
    public String toFileString() {
        return equipmentId + "|" + name + "|" + status;
    }

    // Converts user input to standard status format
    public static String normalizeStatus(String raw) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.equalsIgnoreCase("Available")) {
            return "Available";
        }
        if (s.equalsIgnoreCase("Under Maintenance")) {
            return "Under Maintenance";
        }
        if (s.equalsIgnoreCase("Broken")) {
            return "Broken";
        }
        return null;
    }

    // Update equipment status
    // Admin calls this when equipment needs maintenance or is fixed
    public void updateStatus(String newStatus) {

        // Normalize Status 
        String normalized = normalizeStatus(newStatus);
        if (normalized != null) {
            this.status = normalized;

            //Update Success
            System.out.println("Equipment " + equipmentId + " status is now: " + normalized);
        } else {
            //Invalid Input
            System.out.println("Invalid status. Use: Available, Under Maintenance, or Broken");
        }
    }


    // display equipment information
    public static void displayEquipmentList(ArrayList<Equipment> equipments) {
        System.out.println("\n+--------+---------------------------+----------------------+");
        System.out.println("| ID     | Name                      | Status               |");
        System.out.println("+--------+---------------------------+----------------------+");

        if (equipments == null || equipments.isEmpty()) {
            System.out.println("| No equipment found.                                       |");
            System.out.println("+--------+---------------------------+----------------------+");
            return;
        }

        for (Equipment e : equipments) {
            System.out.println(e);
        }

        System.out.println("+--------+---------------------------+----------------------+");
    }

    // Prompts user to choose status
    private static String promptStatusChoice(Scanner input) {
        String newStatus = null;
        while (newStatus == null) {
            System.out.println("Set status:");
            System.out.println("  1 - Available");
            System.out.println("  2 - Under Maintenance");
            System.out.println("  3 - Broken");
            System.out.print("Enter choice (1-3): ");
            String statusPick = input.next().trim();
            switch (statusPick) {
                case "1":
                    newStatus = "Available";
                    break;
                case "2":
                    newStatus = "Under Maintenance";
                    break;
                case "3":
                    newStatus = "Broken";
                    break;
                default:
                    System.out.println("Invalid choice. Use 1, 2, or 3.");
            }
        }
        return newStatus;
    }
 
    //Update Status
    public static void updateEquipmentStatusOnly(ArrayList<Equipment> equipments, Scanner input) {
        if (equipments.isEmpty()) {
            System.out.println("No equipment to update.");
            return;
        }
        displayEquipmentList(equipments);
        System.out.print("Enter Equipment ID: ");
        String id = input.next().trim();
        
        boolean found = false;
        for (Equipment e : equipments) {
            if (e.getEquipmentId().equalsIgnoreCase(id)) {
                found = true;
                String newStatus = promptStatusChoice(input);
                e.updateStatus(newStatus);
                break;
            }
        }
        if (!found) {
            System.out.println("Equipment ID not found.");
        }
    }
    
    // Validate equipment ID format
    private static boolean isValidEquipmentId(String id) {

        if (id == null) return false;
        id = id.trim();

        // Format: E + numbers only
        return id.matches("^E\\d+$");
    }
    
    // Adds new equipment to the list.
    public static void addNewEquipment(ArrayList<Equipment> equipments, Scanner input) {
        
        // Displays all equipment in the list.
        System.out.print("New Equipment ID: ");
        String id = input.next().trim();

        // validation: first character must be a letter
        if (!isValidEquipmentId(id)) {
            System.out.println("Invalid ID. Format must be E followed by numbers (e.g., E001).");
            return;
        }

        // check duplicate ID
        for (Equipment e : equipments) {
            if (e.getEquipmentId().equalsIgnoreCase(id)) {
                System.out.println("That ID already exists.");
                return;
            }
        }
        input.nextLine();

        System.out.print("Equipment name: ");
        String name = input.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        
        //Set Status
        String newStatus = promptStatusChoice(input);

        equipments.add(new Equipment(id, name, newStatus));
        System.out.println("Equipment added successfully.");
    }

    // Deletes equipment from the list by matching Equipment ID.
    public static void deleteEquipment(ArrayList<Equipment> equipments, Scanner input) {
        if (equipments.isEmpty()) {
            System.out.println("No equipment to delete.");
            return;
        }
        System.out.print("Enter Equipment ID to delete: ");
        String id = input.next().trim();

        for (int i = 0; i < equipments.size(); i++) {
            if (equipments.get(i).getEquipmentId().equalsIgnoreCase(id)) {
                equipments.remove(i);
                System.out.println("Equipment removed.");
                return;
            }
        }

        System.out.println("Equipment ID not found.");
    }

    //Two Equipment objects are considered equal if they have the same equipmentId
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Equipment equipment = (Equipment) obj;
        return equipmentId.equals(equipment.equipmentId);
    }

    //Returns a string representation of the Equipment object
    @Override
    public String toString() {
      return String.format("| %-6s | %-25s | %-20s |",
            equipmentId, name, status);
}

}