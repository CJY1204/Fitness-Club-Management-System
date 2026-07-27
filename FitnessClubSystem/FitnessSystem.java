package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;


/*
  FitnessSystem
  ---------------------------------------------------
  Main controller of the entire system.
  
  Responsibilities:
  - Display main menu (Register / Login)
  - Control workflow
  - Manage collections of users
  
  Relationship:
  - HAS-A relationship with Member, Trainer, Admin
  - Controls system flow
*/

// Group member - Tang Zhi Hao

public class FitnessSystem {

    private ArrayList<Member> members;
    private ArrayList<Trainer> trainers;
    private ArrayList<Admin> admins;
    private ArrayList<Booking> bookings;
    private ArrayList<FitnessClass> classes;
    private ArrayList<Equipment> equipments;
    private FileManager fileManager;
    private int memberCounter = 1;

    private Scanner input;

    public FitnessSystem() {
        members = new ArrayList<>();
        trainers = new ArrayList<>();
        admins = new ArrayList<>();
        bookings = new ArrayList<>();
        classes = new ArrayList<>();
        equipments = new ArrayList<>();
        input = new Scanner(System.in);
        fileManager = new FileManager();
        
        admins = fileManager.loadAdmins();
        trainers = fileManager.loadTrainers();
        equipments = fileManager.loadEquipments();

        classes = fileManager.loadFitnessClasses(trainers);

        members = fileManager.loadMembers();
        
        bookings = fileManager.loadBookings(members, classes);


        updateMemberCounter();   
    }

    public void startSystem() {
        String choice = "";

        while (!choice.equals("3")) {
            System.out.println();
            displayWelcomeBanner();
            System.out.println("===== Fitness Club Management System =====");
            System.out.println("1. Register (Member)");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = input.next();

            switch (choice) {
                case "1":
                    registerMember();
                    break;
                case "2":
                    loginMenu();
                    break;
                case "3":
                    System.out.println("Exiting system...");
                    System.out.println("Saving all data... Goodbye!");
                    fileManager.saveAll(members, trainers, equipments, classes, bookings);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void displayWelcomeBanner() {
        System.out.println("==============================================================");
        System.out.println("|                                                            |");
        System.out.println("|   ███████╗██╗████████╗███╗   ██╗███████╗███████╗███████╗   |");
        System.out.println("|   ██╔════╝██║╚══██╔══╝████╗  ██║██╔════╝██╔════╝██╔════╝   |");
        System.out.println("|   █████╗  ██║   ██║   ██╔██╗ ██║█████╗  ███████╗███████╗   |");
        System.out.println("|   ██╔══╝  ██║   ██║   ██║╚██╗██║██╔══╝  ╚════██║╚════██║   |");
        System.out.println("|   ██║     ██║   ██║   ██║ ╚████║███████╗███████║███████║   |");
        System.out.println("|   ╚═╝     ╚═╝   ╚═╝   ╚═╝  ╚═══╝╚══════╝╚══════╝╚══════╝   |");
        System.out.println("|                                                            |");
        System.out.println("|                 C L U B   S Y S T E M                      |");
        System.out.println("|                                                            |");
        System.out.println("==============================================================");
    }

        private void displayMemberBanner() {
        System.out.println("==================================================");
        System.out.println("|                                                |");
        System.out.println("|               WELCOME, MEMBER                  |");
        System.out.println("|           FITNESS CLUB MEMBER MENU             |");
        System.out.println("|                                                |");
        System.out.println("==================================================");
    }

    private void displayTrainerBanner() {
        System.out.println("==================================================");
        System.out.println("|                                                |");
        System.out.println("|              WELCOME, TRAINER                  |");
        System.out.println("|          FITNESS CLUB TRAINER MENU             |");
        System.out.println("|                                                |");
        System.out.println("==================================================");
    }

    private void displayAdminBanner() {
        System.out.println("==================================================");
        System.out.println("|                                                |");
        System.out.println("|               WELCOME, ADMIN                   |");
        System.out.println("|            FITNESS CLUB ADMIN MENU             |");
        System.out.println("|                                                |");
        System.out.println("==================================================");
    }

    
    private String generateMemberId(int typeChoice) {
        String prefix = "M";
        if (typeChoice == 1) prefix = "MB";
        else if (typeChoice == 2) prefix = "MS";
        else if (typeChoice == 3) prefix = "MG";
        
        return String.format("%s%03d", prefix, memberCounter++);
    }
    
    private void updateMemberCounter() {
        int maxId = 0;
        for (Member m : members) {
            String id = m.getMemberId(); 
            String numericPart = id.replaceAll("[^0-9]", ""); 
            if (!numericPart.isEmpty()) {
                int number = Integer.parseInt(numericPart);
                if (number > maxId) maxId = number;
            }
        }
        memberCounter = maxId + 1;
    }

    
    //Registration for new Member.
    //Member selects membership level.
    //System auto-generates unique ID.
    //Saves to TXT file after success.
    private void registerMember() {

        input.nextLine(); // clear buffer

        System.out.println("\n=== Member Registration ===");

        //  Name Validation: not empty, letters and spaces only 
        String name;
        while (true) {
            System.out.print("Enter Name: ");
            name = input.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
            } else if (!name.matches("[a-zA-Z ]+")) {
                System.out.println("Name must contain letters only. Please try again.");
            } else {
                break;
            }
        }

        // Age Validation: must be between 10 and 100 
        int age;
        while (true) {
            System.out.print("Enter Age: ");
            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
                continue;
            }
            age = input.nextInt();
            if (age < 10 || age > 100) {
                System.out.println("Age must be between 10 and 100. Please try again.");
            } else {
                break;
            }
        }
        input.nextLine(); // clear buffer after nextInt()

        //  Height Validation: must be between 0.5m and 2.5m
        double height;
        while (true) {
            System.out.print("Enter Height (m): ");
            if (!input.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
                continue;
            }
            height = input.nextDouble();
            if (height < 0.5 || height > 3.0) {
                System.out.println("Height must be between 0.5m and 3.0m. Please try again.");
            } else {
                break;
            }
        }
        input.nextLine(); // clear buffer

        //  Weight Validation: must be between 10kg and 300kg 
        double weight;
        while (true) {
            System.out.print("Enter Weight (kg): ");
            if (!input.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
                continue;
            }
            weight = input.nextDouble();
            if (weight < 10 || weight > 300) {
                System.out.println("Weight must be between 10kg and 500kg. Please try again.");
            } else {
                break;
            }
        }

        //  Membership Selection Validation 
        System.out.println("\nSelect Membership Type:");
        System.out.println("1. Basic (Price: RM50)");
        System.out.println("2. Silver (Price: RM100)");
        System.out.println("3. Gold (Price: RM200)");

        int choice;
        while (true) {
            System.out.print("Enter choice: ");
            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
                input.nextLine();
                continue;
            }
            choice = input.nextInt();
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            } else {
                break;
            }
        }

        Membership membership;
        switch (choice) {
            case 1: membership = new BasicMembership(10);         break;
            case 2: membership = new SilverMembership(true);      break;
            default: membership = new GoldMembership(true, true); break;
        }

        input.nextLine(); // clear buffer after nextInt()

        //Password Validation: min 6 chars, at least 1 digit
        String password;
        while (true) {
            System.out.print("Create Password (min 6 chars, must include a number): ");
            password = input.nextLine().trim();
            if (password.length() < 6) {
                System.out.println("Password must be at least 6 characters. Please try again.");
            } else if (!password.matches(".*\\d.*")) {
                System.out.println("Password must contain at least one number. Please try again.");
            } else {
                break;
            }
        }

        String memberId = generateMemberId(choice);

        Member newMember = new Member(
                memberId,
                password,
                name,
                age,
                height,
                weight,
                membership,
                "Active"
        );

        members.add(newMember);
        fileManager.saveAllMembers(members);

        System.out.println("\nRegistration Successful!");
        System.out.println("--------------------------------");
        System.out.println("Your Member ID  : " + memberId);
        System.out.println("Name            : " + name);
        System.out.println("Membership      : " + membership.getMembershipName());
        System.out.println("Fee to pay      : RM " + membership.getPrice());
        System.out.println("--------------------------------");

        System.out.println("\nPress Enter to go back to Main Menu...");
        input.nextLine();
    }


    //Second level login menu
    private void loginMenu() {

        while (true) {
            System.out.println("\n=== User Login ===");
            System.out.println("1. Member Login"); 
            System.out.println("2. Trainer Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");

            String roleChoice = input.next(); 

            switch (roleChoice) {
                case "1":
                    memberLogin();
                    break;
                case "2":
                    trainerLogin();
                    break;
                case "3":
                    adminLogin();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
            }
        }
    }

    //memberLogin()
    private void memberLogin() {

        Member currentMember = null;

        while (currentMember == null) {
            System.out.print("Enter Member ID: ");
            String memberId = input.next();

            System.out.print("Enter Password: ");
            String password = input.next();

            for (Member m : members) {
                if (m.validateCredentials(memberId, password)) {
                    currentMember = m;
                    break;
                }
            }

            // ===== ADD HERE — check expired AFTER credential match =====
            if (currentMember != null && currentMember.getStatus().equalsIgnoreCase("Expired")) {
                System.out.println("\nYour membership has expired. Please contact admin or register a new member.");
                currentMember = null; // reset so loop continues
                System.out.print("Type 'exit' to go back or any key to retry: ");
                if (input.next().equalsIgnoreCase("exit")) return;
                continue;
            }
            //end of add

            // Invalid login
            if (currentMember == null) {
                System.out.println("\nInvalid Member ID or Password!");
                System.out.println("Please try again...");
                System.out.print("Type 'exit' to go back or any key to retry: ");
                if (input.next().equalsIgnoreCase("exit")) {
                    return;
                }
            }
        }

        System.out.println("\nLogin successful!");
        displayMemberBanner();
        System.out.println("Welcome " + currentMember.getMemberId() + " - " + currentMember.getName());

        String choice; 
        while(true){
            currentMember.displayMenu();
            System.out.print("Enter choice: ");
            choice = input.next();

            switch (choice) {
                case "1":
                    currentMember.viewProfile();
                    System.out.println("Press any key or Enter to go back...");
                    // 1. Consume the leftover newline from input.next()
                    input.nextLine();
                    // 2. This actually waits for the user to hit Enter (or type + Enter)
                    input.nextLine(); 
                    break;
                case "2":
                    currentMember.viewClasses(classes, bookings, input);
                    fileManager.saveBookings(bookings); 
                    fileManager.updateFitnessClasses(classes);
                    break;
                case "3":
                    currentMember.viewBooking(bookings, input);
                    fileManager.saveBookings(bookings); 
                    fileManager.updateFitnessClasses(classes);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
            }
        }
    }

    private void trainerLogin() {
        Trainer currentTrainer = null;

        while (currentTrainer == null) {
            System.out.println("\n--- Trainer Login ---");
            System.out.print("Enter Trainer ID: ");
            String trainerId = input.next();

            System.out.print("Enter Password: ");
            String password = input.next();

        
            for (Trainer t : trainers) {
                if (t.validateCredentials(trainerId, password)){ 
                    currentTrainer = t; 
                    break;
                }
            }

            if (currentTrainer == null) {
                System.out.println("\nInvalid Trainer ID or Password!");
                System.out.println("Please try again...");
                
                System.out.print("Type 'exit' to go back or any key to retry: ");
                if (input.next().equalsIgnoreCase("exit")){
                    return;
                }
            }
        }

        System.out.println("\nLogin successful!");
        displayTrainerBanner();
        System.out.println("Welcome " + currentTrainer.getTrainerId() + " - " + currentTrainer.getName());


       boolean isTrainerLoggedIn = true;
        while (isTrainerLoggedIn) {
            currentTrainer.displayMenu(); 
            System.out.print("Enter choice: ");
            String choice = input.next().trim();

            switch (choice) {
                case "1":
                    currentTrainer.viewMySchedule(classes);
                    break;
                case "2":
                    currentTrainer.viewAssignedMembers(classes);
                    break;
                case "3":
                    currentTrainer.trackMemberProgress(input, members, classes);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    isTrainerLoggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
            }
        }
    }

    //adminLogin()
    private void adminLogin() {
        Admin currentAdmin = null;


        while (currentAdmin == null) {
            System.out.println("\n--- Admin Login ---");
            System.out.print("Enter Admin ID: ");
            String id = input.next();

            System.out.print("Enter Password: ");
            String password = input.next();

            for (Admin a : admins) {
                if (a.validateCredentials(id, password)) {  
                    currentAdmin = a; 
                    break;
                }
            }

            if (currentAdmin == null) {
                System.out.println("\nInvalid Admin ID or Password!");
                System.out.print("Type 'exit' to go back or any key to retry: ");
                if (input.next().equalsIgnoreCase("exit")){
                    return;
                }
            }
        }


        System.out.println("\nLogin successful! Welcome, Administrator.");
        displayAdminBanner();
        System.out.println("Welcome " + currentAdmin.getAdminId() + " - " + currentAdmin.getName());

        String choice;
        while(true){
            currentAdmin.displayMenu(); 
            System.out.print("Enter choice: ");
            choice = input.next();

            switch (choice) {
                case "1":
                    currentAdmin.manageMembers(members,input);
                    fileManager.saveAllMembers(members);
                    break;
                case "2":
                    if(equipments != null){
                        currentAdmin.manageEquipment(equipments,input);
                        fileManager.updateEquipments(equipments);
                    }
                    break;
                case "3":
                    currentAdmin.manageTrainers(trainers, input);
                    fileManager.updateTrainers(trainers);
                    break;
                case "4":
                    currentAdmin.assignClass(classes, trainers, input);
                    fileManager.updateFitnessClasses(classes);
                case "5":
                    currentAdmin.generateMonthlyRevenueReport(members);
                    break;
                case "6" :
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-6.");
            }
        }
    }
    // zhi hao

    public static void main(String[] args) {
        FitnessSystem system = new FitnessSystem();
        system.startSystem();
    }

    
}