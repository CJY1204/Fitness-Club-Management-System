package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;

/*
  Admin Class
  ---------------------------------------------------
  Represents a system administrator.
  
  Responsibilities:
  - Store admin login information
  - Validate login credentials
  - Manage members, equipment, and classes
  - Generate revenue reports
  
  Relationship:
  - ASSOCIATION with Member, Equipment, FitnessClass, Trainer
 */

// Group member - Tang Zhi Hao

public class Admin {

    private String adminId;
    private String password;
    private String name;
    private String email;

    /*
     * Parameterized constructor for Admin
     * 
     * @param adminId  Unique admin identifier
     * @param password Login password
     */
    public Admin(String adminId, String password, String name, String email) {
        this.adminId = adminId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    
    //Validates login credentials  
    //@param inputId       Admin ID entered by user
    //@param inputPassword Password entered by user
    //@return true if credentials match, false otherwise
     
    public boolean validateCredentials(String inputId, String inputPassword) {
        return this.adminId.equals(inputId) && this.password.equals(inputPassword);
    }

    //  Getters 

    public String getAdminId() {
        return adminId;
    }
    public String getPassword() {
        return password;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    public void setPassword(String password) { this.password = password; }

    // ==================== Display Methods ====================

    /*
     * Displays the admin menu
     */
    public void displayMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Manage Members (View Active/Expired)"); // manageMembers()
        System.out.println("2. Manage Equipment (Flag Maintenance)"); // manageEquipment()
        System.out.println("3. Manage Trainers "); // manageTrainers()
        System.out.println("4. Manage Staff (Assign Trainer to Classes)"); //assignClass()
        System.out.println("5. Generate Revenue Report"); // generateMonthlyRevenueReport()
        System.out.println("6. Logout");
    }

    // ==================== Business Logic Methods ====================

    public void manageMembers(ArrayList<Member> members, Scanner input) {

        System.out.println("\n--- Member List ---");

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        // Display member list
        System.out.printf("%-8s | %-15s | %-10s | %-10s%n", "ID", "Name", "Type", "Status");
        System.out.println("------------------------------------------------------------");
        for (Member m : members) {
            System.out.printf("%-8s | %-15s | %-10s | %-10s%n",
                m.getMemberId(),
                m.getName(),
                m.getMembership().getMembershipName(),
                m.getStatus());
        }

        // Select target member
        System.out.print("\nEnter Member ID to manage (or '0' to go back): ");
        String targetId = input.next();

        if (targetId.equals("0")) return;

        // Find member
        Member foundMember = null;
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(targetId)) {
                foundMember = m;
                break;
            }
        }

        if (foundMember == null) {
            System.out.println("Error: Member with ID " + targetId + " not found.");
            return;
        }

        // Show action menu
        boolean done = false;
        while (!done) {
            System.out.println("\nManaging Member: " + foundMember.getName());
            System.out.println("Current Status : " + foundMember.getStatus());
            System.out.println("[1] Set Active");
            System.out.println("[2] Set Expired");
            System.out.println("[3] Delete Member");
            System.out.println("[4] Cancel");
            System.out.print("Choice: ");

            String choice = input.next();
            switch (choice) {
                case "1":
                    foundMember.setStatus("Active");
                    System.out.println("Successfully set to Active.");
                    done = true;
                    break;
                case "2":
                    foundMember.setStatus("Expired");
                    System.out.println("Successfully set to Expired.");
                    done = true;
                    break;
                case "3":
                    // Delete member
                    members.remove(foundMember);
                    System.out.println("Member " + foundMember.getMemberId() +
                        " - " + foundMember.getName() + " deleted successfully.");
                    done = true;
                    break;
                case "4":
                    System.out.println("Action cancelled.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
            }
        }

        System.out.println("Note: Changes saved in memory. Data will be finalized on system exit.");
    }
    
    // Manage equipment
    // Allows admin to view, add, update, and delete equipment
    //@param equipments List of all equipment
    
    public void manageEquipment(ArrayList<Equipment> equipments, Scanner input) {
        if (equipments == null) {
            System.out.println("No equipment data available.");
            return;
        }

        String sub;
        do {
            // Clear spacing
            System.out.println("\n==============================================");
            System.out.println("           EQUIPMENT MANAGEMENT               ");
            System.out.println("==============================================");

            // Display table
            Equipment.displayEquipmentList(equipments);

            // Menu
            System.out.println("\n-------------- ACTION MENU -------------------");
            System.out.println(" 1. Update Equipment Status");
            System.out.println(" 2. Add New Equipment");
            System.out.println(" 3. Delete Equipment");
            System.out.println(" 0. Back to Admin Menu");
            System.out.println("----------------------------------------------");
            System.out.print(" Enter your choice: ");

            sub = input.next().trim();

            System.out.println(); // spacing after input

            switch (sub) {
                case "1":
                    System.out.println(">>> Update Equipment Status");
                    Equipment.updateEquipmentStatusOnly(equipments, input);
                    break;

                case "2":
                    System.out.println(">>> Add New Equipment");
                    Equipment.addNewEquipment(equipments, input);
                    break;

                case "3":
                    System.out.println(">>> Delete Equipment");
                    Equipment.deleteEquipment(equipments, input);
                    break;

                case "0":
                    System.out.println("Returning to Admin Menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (!sub.equals("0"));
    }

    // manage T
     public void manageTrainers(ArrayList<Trainer> trainers, Scanner input) {
        String choice;
        do {
            System.out.println("\n================= TRAINER MANAGEMENT =================");
            Trainer.displayTrainers(trainers);
            System.out.println("\n1. Create Trainer");
            System.out.println("2. Update Trainer");
            System.out.println("3. Delete Trainer");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = input.next();

            switch (choice) {
                case "1":
                    Trainer.createTrainer(trainers, input);
                    break;
                case "2":
                    Trainer.updateTrainer(trainers, input);
                    break;
                case "3":
                    Trainer.deleteTrainer(trainers, input);
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (!choice.equals("0"));
    }
    // manage T

     //Manage class assignments
     //Allows admin to create classes, set schedules, assign trainers, and delete classes
     //@param classes  List of all fitness classes
     //@param trainers List of all trainers
    public void assignClass(ArrayList<FitnessClass> classes, ArrayList<Trainer> trainers, Scanner input) {
        String choice;

        while (true) {
            System.out.println("\n==============================================================");
            System.out.println("|                    MANAGE STAFF PORTAL                     |");
            System.out.println("==============================================================");

            System.out.println("\n---------------------- FITNESS CLASS LIST ---------------------");
            FitnessClass.displayClasses(classes);

            System.out.println("\n------------------------ TRAINER LIST -------------------------");
            Trainer.displayTrainers(trainers);

            System.out.println("\n==============================================================");
            System.out.println("|                       STAFF ACTIONS                        |");
            System.out.println("==============================================================");
            System.out.println("|  1. Create Classes                                         |");
            System.out.println("|  2. Set Schedule                                           |");
            System.out.println("|  3. Assign Trainer                                         |");
            System.out.println("|  4. Delete Classes                                         |");
            System.out.println("|  5. Return to Admin Menu                                   |");
            System.out.println("==============================================================");
            System.out.print("Enter choice: ");

            choice = input.next();

            switch (choice) {
                case "1":
                    FitnessClass.createClass(classes, input);
                    break;
                case "2":
                    FitnessClass.setSchedule(classes, input);
                    break;
                case "3":
                    FitnessClass.assignTrainerToClass(classes, trainers, input);
                    break;
                case "4":
                    FitnessClass.deleteClass(classes, input);
                    break;
                case "5":
                    System.out.println("Returning to Admin Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1 to 5.");
            }
        }
    }

    //Generate monthly revenue report
    // Displays total revenue and membership breakdown 
    //@param members List of all members
     
    public void generateMonthlyRevenueReport(ArrayList<Member> members) {
        double totalRevenue = 0.0;

        int goldCount = 0;
        int silverCount = 0;
        int basicCount = 0;

        double goldRevenue = 0.0;
        double silverRevenue = 0.0;
        double basicRevenue = 0.0;

        int activeCount = 0;
        int expiredCount = 0;

        for (Member m : members) {
            String status = m.getStatus();
            String membershipType = m.getMembership().getMembershipName();
            double fee = m.getMembership().getPrice();

            if (status.equalsIgnoreCase("Active")) {
                activeCount++;
                totalRevenue += fee;

                if (membershipType.equalsIgnoreCase("Gold")) {
                    goldCount++;
                    goldRevenue += fee;
                } else if (membershipType.equalsIgnoreCase("Silver")) {
                    silverCount++;
                    silverRevenue += fee;
                } else if (membershipType.equalsIgnoreCase("Basic")) {
                    basicCount++;
                    basicRevenue += fee;
                }
            } else if (status.equalsIgnoreCase("Expired")) {
                expiredCount++;
            }
        }

        System.out.println();
        System.out.println("==============================================================");
        System.out.println("                 FITNESS CLUB MONTHLY REVENUE REPORT          ");
        System.out.println("==============================================================");
        System.out.printf("%-30s : %d%n", "Total Members", members.size());
        System.out.printf("%-30s : %d%n", "Active Members", activeCount);
        System.out.printf("%-30s : %d%n", "Expired Members", expiredCount);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s%n", "Membership", "Members", "Revenue (RM)");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-15s %-15d RM %-17.2f%n", "Gold", goldCount, goldRevenue);
        System.out.printf("%-15s %-15d RM %-17.2f%n", "Silver", silverCount, silverRevenue);
        System.out.printf("%-15s %-15d RM %-17.2f%n", "Basic", basicCount, basicRevenue);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-30s : RM %.2f%n", "Total Monthly Revenue", totalRevenue);
        System.out.println("==============================================================");
    }

    //Object Overrides
     //Compares this Admin to another object
     //Two Admins are considered equal if they have the same adminId
     //@param obj Object to compare with
     //@return true if the objects are equal, false otherwise
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Admin admin = (Admin) obj;
        return adminId.equals(admin.adminId);
    }


    @Override
    public String toString() {
        return String.format("%-8s | %-15s",adminId , password);
    }

}