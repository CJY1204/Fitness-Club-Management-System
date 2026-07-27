package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;


 /* PURPOSE:
 * - Manage trainer information
 * - View class schedule
 * - View assigned members
 * - Track member progress
 */

 // Group member - Cheng Jun YU

public class Trainer {

    // Fields
     private String trainerId;
     private String password;
     private String name;

    // Constructor
    public Trainer(String trainerId, String password, String name) {
        this.trainerId = trainerId;
        this.password = password;
        this.name = name;
    }

   
    // Setter
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }

  
    // Getter
    public String getTrainerId() { return trainerId; }
    public String getPassword() { return password; }
    public String getName() { return name; }

    // Login Validation
    public boolean validateCredentials(String inputId, String inputPassword) {
        return trainerId.equals(inputId) && password.equals(inputPassword);
    }

    // Display Menu
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║      TRAINER MENU          ║");
        System.out.println("║ ID: " + trainerId + "                   ║");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ 1. View Class Schedule     ║");
        System.out.println("║ 2. View Assigned Members   ║");
        System.out.println("║ 3. Track Member Progress   ║");
        System.out.println("║ 4. Logout                  ║");
        System.out.println("╚════════════════════════════╝");
    }

     // Validate trainer ID format
        private static boolean isValidTrainerId(String id) {
        if (id == null) return false;
         id = id.trim();
        return id.matches("^T\\d+$");
    }

     // Create new trainer
    public static void createTrainer(ArrayList<Trainer> trainers, Scanner input) {
        System.out.print("New Trainer ID (format T001): ");
        String id = input.next().trim();

        if (!isValidTrainerId(id)) {
            System.out.println("Invalid ID format. Must be T followed by numbers (e.g., T001).");
            return;
        }

        for (Trainer t : trainers) {
            if (t.getTrainerId().equalsIgnoreCase(id)) {
                System.out.println("That ID already exists.");
                return;
            }
        }

        input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        System.out.print("Name: ");
        String name = input.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        trainers.add(new Trainer(id, password, name));
        System.out.println("Trainer created successfully.");
    }

    // Update trainer
    public static void updateTrainer(ArrayList<Trainer> trainers, Scanner input) {
        if (trainers.isEmpty()) {
            System.out.println("No trainers to update.");
            return;
        }

        displayTrainers(trainers);
        System.out.print("Enter Trainer ID to update: ");
        String id = input.next().trim();

        Trainer found = null;
        for (Trainer t : trainers) {
            if (t.getTrainerId().equalsIgnoreCase(id)) {
                found = t;
                break;
            }
        }

        if (found == null) {
            System.out.println("Trainer not found.");
            return;
        }

        System.out.println("\nUpdating: " + found.getName());
        System.out.println("[1] Update Name");
        System.out.println("[2] Update Password");
        System.out.println("[3] Update Both");
        System.out.print("Choice: ");
        String choice = input.next();

        input.nextLine();
        switch (choice) {
            case "1":
                System.out.print("New Name: ");
                String name = input.nextLine().trim();
                if (!name.isEmpty()) {
                    found.setName(name);
                    System.out.println("Name updated.");
                }
                break;
            case "2":
                System.out.print("New Password: ");
                String password = input.nextLine().trim();
                if (!password.isEmpty()) {
                    found.setPassword(password);
                    System.out.println("Password updated.");
                }
                break;
            case "3":
                System.out.print("New Name: ");
                name = input.nextLine().trim();
                System.out.print("New Password: ");
                password = input.nextLine().trim();
                if (!name.isEmpty()) found.setName(name);
                if (!password.isEmpty()) found.setPassword(password);
                System.out.println("Trainer updated.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // Delete trainer
    public static void deleteTrainer(ArrayList<Trainer> trainers, Scanner input) {
        if (trainers.isEmpty()) {
            System.out.println("No trainers to delete.");
            return;
        }

        displayTrainers(trainers);
        System.out.print("Enter Trainer ID to delete: ");
        String id = input.next().trim();

        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getTrainerId().equalsIgnoreCase(id)) {
                String name = trainers.get(i).getName();
                trainers.remove(i);
                System.out.println("Trainer " + name + " deleted.");
                return;
            }
        }
        System.out.println("Trainer not found.");
    }

    // View TRAINERS LIST
    public static void displayTrainers(ArrayList<Trainer> trainers) {

        System.out.println("\n===== TRAINER LIST =====");

        if (trainers == null || trainers.isEmpty()) {
            System.out.println("No trainers found.");
            return;
        }

        System.out.printf("%-10s | %-25s%n", "ID", "Name");
        System.out.println("-------------------------------");

        for (Trainer t : trainers) {
            System.out.println(t);
        }
    }
    
    // View Schedule
    public void viewMySchedule(ArrayList<FitnessClass> classes) {

        String title = "MY CLASS SCHEDULE";
        int width = 60;

        int padding = (width - title.length()) / 2;

        System.out.println("\n" + "=".repeat(width));
        System.out.println(" ".repeat(padding) + title);
        System.out.println("=".repeat(width));

        if (classes == null || classes.isEmpty()) {
            System.out.println("No classes scheduled.");
            return;
        }

        System.out.printf("%-10s %-20s %-20s %-10s%n",
                "Class ID", "Class Name", "Schedule", "Slots");
        System.out.println("-".repeat(width));

        boolean found = false;

        for (FitnessClass c : classes) {

            if (c.getInstructor() != null &&c.getInstructor().getTrainerId().equalsIgnoreCase(trainerId)) {

                String schedule = (c.getDate() != null && c.getTimeSlot() != null)? c.getDate() + " " + c.getTimeSlot()
                        : "TBA";

                System.out.printf("%-10s %-20s %-20s %-10s%n",
                        c.getClassId(),
                        c.getClassName(),
                        schedule,
                        c.getBookedMembers().size() + "/" + c.getMaxCapacity()
                );
                found = true;
            }
        }

        if (!found) {
            System.out.println("No assigned classes.");
        }
    }

    // View Assigned Members 
    public void viewAssignedMembers(ArrayList<FitnessClass> classes) {

        System.out.println("\n===== ASSIGNED MEMBERS =====");

        if (classes == null || classes.isEmpty()) {
            System.out.println("No classes found.");
            return;
        }

        ArrayList<String> seen = new ArrayList<>();

        for (FitnessClass c : classes) {

            if (c.getInstructor() != null &&c.getInstructor().getTrainerId().equalsIgnoreCase(trainerId)) {

                for (Member m : c.getBookedMembers()) {

                    if (!seen.contains(m.getMemberId())) {
                        seen.add(m.getMemberId());

                        System.out.println(
                                m.getMemberId() + " | " +
                                m.getName() + " | " +
                                m.getMembership().getMembershipName()
                        );
                    }
                }
            }
        }

        if (seen.isEmpty()) {
            System.out.println("No assigned members.");
        }
    }

    // Tracks and displays member BMI
    public void trackMemberProgress(Scanner input, ArrayList<Member> members,ArrayList<FitnessClass> classes) {
        
        input.nextLine();
        System.out.print("Enter Member ID: ");
        String id = input.nextLine().trim();

        Member found = null;

        // Search Members
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(id)) {
                found = m;
                break;
            }
        }

        if (found == null) {
            System.out.println("\n+===================+");
            System.out.println("| Member not found. |");
            System.out.println("+===================+");
            return;
        }

        // CHECK ACCESS
        if (!isMemberInMyClasses(found, classes)) {
            System.out.println("\n+====================+");
            System.out.println("| Member not in your class. |");
            System.out.println("+====================+");
            return;
        }

        double bmi = found.calculateBMI();

          System.out.println("\n+==============+==============+");
          System.out.println("| MEMBER PROGRESS TRACKING    |");
          System.out.println("+==============+==============+");
          System.out.printf("| %-10s | %-14s |%n", "ID", found.getMemberId());
          System.out.printf("| %-10s | %-14s |%n", "Name", found.getName());
          System.out.printf("| %-10s | %-14s |%n", "Age", found.getAge());
          System.out.printf("| %-10s | %-14s |%n", "Height", found.getHeight() + " m");
          System.out.printf("| %-10s | %-14s |%n", "Weight", found.getWeight() + " kg");
          System.out.printf("| %-10s | %-14s |%n", "BMI", String.format("%.2f", bmi));
          System.out.printf("| %-10s | %-14s |%n", "Category",  getBmiCategory(bmi));
          System.out.println("+==============+==============+");
    }
        //  Checks if member belongs to trainer's classes
    private boolean isMemberInMyClasses(Member m, ArrayList<FitnessClass> classes) {

        if (classes == null) return false;

        for (FitnessClass c : classes) {

            if (c.getInstructor() != null &&
                c.getInstructor().getTrainerId().equalsIgnoreCase(trainerId)) {

                for (Member bm : c.getBookedMembers()) {

                    if (bm.getMemberId().equalsIgnoreCase(m.getMemberId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
        // Returns BMI category
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
            else if (bmi < 25) return "Normal";
            else if (bmi < 30) return "Overweight";
            else return "Obese";
    }


    // OBJECT METHODS
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Trainer)) return false;

        Trainer t = (Trainer) obj;
        return trainerId.equals(t.trainerId);
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-25s", trainerId, name);
    }
}