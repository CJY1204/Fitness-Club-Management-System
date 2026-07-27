package FitnessClubSystem;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Member Class
 * ---------------------------------------------------
 * Represents a gym member in the system.
 * 
 * Responsibilities:
 * - Store member personal information
 * - Validate login credentials
 * - Calculate BMI
 * - Manage profile viewing and class booking
 * 
 * Relationship:
 * - HAS-A relationship with Membership (Aggregation)
 * 
 */

// Group member - Cheng Jun Yu

public class Member {

    private String memberId;
    private String password;
    private String name;
    private int age;
    private double height;  // in meters
    private double weight;  // in kg
    private String status;  // Active / Expired
    private Membership membership;

    /*
     * Parameterized constructor for Member
     * 
     * @param memberId   Unique member identifier
     * @param password   Login password
     * @param name       Member's full name
     * @param age        Member's age
     * @param height     Height in meters
     * @param weight     Weight in kg
     * @param membership Membership object (Basic/Silver/Gold)
     * @param status     Account status (Active/Expired)
     */
    public Member(String memberId, String password, String name, int age,
                  double height, double weight, Membership membership, String status) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.membership = membership;
        this.status = status;
    }

    // ==================== Getters and Setters ====================
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public String getMemberId() {
        return memberId;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public double getHeight() {
        return height;
    }
    public double getWeight() {
        return weight;
    }
    public String getStatus() {
        return status;
    }
    public Membership getMembership() {
        return membership;
    }

    // ==================== Core Methods ====================

    /*
     * Validates login credentials
     * 
     * @param inputId       Member ID entered by user
     * @param inputPassword Password entered by user
     * @return true if credentials match, false otherwise
     */
    public boolean validateCredentials(String inputId, String inputPassword) {
        try {
            return this.memberId.equals(inputId) && this.password.equals(inputPassword);
        } catch (NullPointerException e) {
            System.out.println("Error validating credentials: " + e.getMessage());
            return false;
        }
    }

    /*
     * Calculates Body Mass Index (BMI)
     * Formula: weight (kg) / height² (m)
     * 
     * @return BMI value
     */
    public double calculateBMI() {
        try {
            if (height <= 0) {
                throw new ArithmeticException("Height must be greater than 0.");
            }
            return weight / (height * height);
        } catch (ArithmeticException e) {
            System.out.println("BMI calculation error: " + e.getMessage());
            return 0.0;
        }
    }

    /*
     * Returns BMI category based on standard WHO classification
     * 
     * @return BMI category as String
     */
    public String getBMICategory() {
        try {
            double bmi = calculateBMI();
            if (bmi < 18.5) return "Underweight";
            else if (bmi < 25.0) return "Normal";
            else if (bmi < 30.0) return "Overweight";
            else return "Obese";
        } catch (Exception e) {
            System.out.println("Error determining BMI category: " + e.getMessage());
            return "Unknown";
        }
    }

    // ==================== Display Methods ====================

    /*
     * Displays the member menu
     */
    public void displayMenu() {
        System.out.println("\n=== Member Menu ===");
        System.out.println("1. View Profile");
        System.out.println("2. View Classes & Book");
        System.out.println("3. View My Bookings & Cancel");
        System.out.println("4. Logout");
    }

    /*
     * Display member profile information
     * Shows personal details and membership information
     */
    public void viewProfile() {
        try {
            System.out.println(this.toString());
        } catch (NullPointerException e) {
            System.out.println("Error displaying profile: " + e.getMessage());
        }
    }

    /*
     * Check and display sauna access status
     */
    public void checkSaunaAccess() {
        try {
            if (membership.hasSaunaAccess()) {
                System.out.println("Sauna access granted.");
            } else {
                System.out.println("Sauna access is only available for Silver and Gold members.");
            }
        } catch (NullPointerException e) {
            System.out.println("Error checking sauna access: membership not found.");
        }
    }

    // ==================== Booking Related Methods ====================

    /*
     * Book a fitness class
     * Delegates to Booking.performBooking()
     * 
     * @param classes  List of all fitness classes
     * @param bookings List of all bookings
     * @param input    Scanner for user input
     */
    public void bookClass(ArrayList<FitnessClass> classes, ArrayList<Booking> bookings, Scanner input) {
        try {
            Booking.performBooking(this, classes, bookings, input);
        } catch (Exception e) {
            System.out.println("Error during booking process: " + e.getMessage());
        }
    }

    /*
     * View available classes and optionally book one
     * 
     * @param classes  List of all fitness classes
     * @param bookings List of all bookings
     * @param input    Scanner for user input
     */
    public void viewClasses(ArrayList<FitnessClass> classes, ArrayList<Booking> bookings, Scanner input) {
        try {
            FitnessClass.showMemberSchedule(classes);
            System.out.print("\nDo you want to book a class? (Y/N): ");
            String choice = input.next();
            if (choice.equalsIgnoreCase("Y")) {
                this.bookClass(classes, bookings, input);
            }
        } catch (Exception e) {
            System.out.println("Error viewing classes: " + e.getMessage());
        }
    }

    /*
     * View member's own bookings and optionally cancel one
     * 
     * @param allBookings List of all bookings in the system
     * @param input       Scanner for user input
     */
    public void viewBooking(ArrayList<Booking> allBookings, Scanner input) {
        try {
            System.out.println("\n===== My Current Bookings =====");
            ArrayList<Booking> myCurrentBookings = new ArrayList<>();

            for (Booking b : allBookings) {
                if (b.getMember().getMemberId().equals(this.memberId)) {
                    System.out.printf("[%s] Class: %-15s | Date: %s | Status: %s%n",
                        b.getBookingId(), b.getFitnessClass().getClassName(),
                        b.getSessionDate(), b.getBookingStatus());
                    myCurrentBookings.add(b);
                }
            }

            if (myCurrentBookings.isEmpty()) {
                System.out.println("No bookings found.");
                return;
            }

            System.out.print("\nEnter Booking ID to cancel (or 'N' to go back): ");
            String bid = input.next();

            if (!bid.equalsIgnoreCase("N")) {
                for (Booking b : myCurrentBookings) {
                    if (b.getBookingId().equalsIgnoreCase(bid)) {
                        if (b.getBookingStatus().equals("Cancelled")) {
                            System.out.println("This booking is already cancelled.");
                            return;
                        }
                        b.setBookingStatus("Cancelled");
                        b.getFitnessClass().cancelEnrollment(this);
                        System.out.println("Success! Booking " + bid + " cancelled.");
                        return;
                    }
                }
                System.out.println("Booking ID not found in your list.");
            }
        } catch (NullPointerException e) {
            System.out.println("Error accessing booking data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error in viewBooking: " + e.getMessage());
        }
    }

    // ==================== Object Overrides ====================

    /*
     * Compares this Member to another object
     * Two Members are considered equal if they have the same memberId
     * 
     * @param obj Object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return memberId.equals(member.memberId);
    }

    /*
     * Returns a string representation of the Member object
     * 
     * @return Formatted string with member details
     */
    @Override
    public String toString() {
        // Prepare maxMonthlyBookings row based on membership type
        String bookingLimit;
        if (membership instanceof BasicMembership) {
            bookingLimit = String.valueOf(((BasicMembership) membership).getMaxMonthlyBookings());
        } else {
            bookingLimit = "None";
        }

        return "\n+----------------------------------+" +
            "\n|        MEMBER PROFILE            |" +
            "\n+----------------------------------+" +
            "\n| ID       : " + String.format("%-22s", memberId)                                       + "|" +
            "\n| Name     : " + String.format("%-22s", name)                                           + "|" +
            "\n| Age      : " + String.format("%-22s", age)                                            + "|" +
            "\n| Height   : " + String.format("%-22s", height + " m")                                  + "|" +
            "\n| Weight   : " + String.format("%-22s", weight + " kg")                                 + "|" +
            "\n+----------------------------------+" +
            "\n|        MEMBERSHIP DETAILS        |" +
            "\n+----------------------------------+" +
            "\n| Type     : " + String.format("%-22s", membership.getMembershipName())                 + "|" +
            "\n| Fee      : " + String.format("%-22s", "RM " + membership.getPrice())                  + "|" +
            "\n| Status   : " + String.format("%-22s", status)                                         + "|" +
            "\n| Sauna    : " + String.format("%-22s", membership.hasSaunaAccess() ? "Yes" : "No")     + "|" +
            "\n| Priority : " + String.format("%-22s", membership.hasPriorityBooking() ? "Yes" : "No") + "|" +
            "\n| Max Monthly Booking : " + String.format("%-11s", bookingLimit) + "|" +
            "\n+----------------------------------+" +
            "\n|        HEALTH STATS              |" +
            "\n+----------------------------------+" +
            String.format("%n| BMI      : %-22.2f|", calculateBMI()) +
            "\n| Category : " + String.format("%-22s", getBMICategory()) + "|" +
            "\n+----------------------------------+";
    }
}