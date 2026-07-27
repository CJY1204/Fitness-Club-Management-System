package FitnessClubSystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Booking Class
 * ---------------------------------------------------
 * Represents a class booking made by a Member.
 *
 * Responsibilities:
 * - Store booking information
 * - Perform booking process
 * - Track booking status
 * - Prevent duplicate bookings
 * - Enforce Basic membership monthly booking limit
 *
 * Relationship:
 * - COMPOSITION with Member (Booking cannot exist without Member)
 * - COMPOSITION with FitnessClass (Booking cannot exist without FitnessClass)

 */

// Group member - Khoo See Ze

public class Booking {

    private String bookingId;
    private Member member;
    private FitnessClass fitnessClass;
    private LocalDate sessionDate;
    private String bookingStatus;
    private String memberTier;
    private LocalDate bookingDate; // track when booking was made

    /*
     * Parameterized constructor for Booking
     *
     * @param bookingId   Unique booking identifier
     * @param member      Member who made the booking
     * @param fitnessClass FitnessClass being booked
     * @param sessionDate Date of the class session
     * @param memberTier  Membership tier at time of booking
     */
    public Booking(String bookingId, Member member, FitnessClass fitnessClass, LocalDate sessionDate, String memberTier) {
        this.bookingId     = bookingId;
        this.member        = member;
        this.fitnessClass  = fitnessClass;
        this.sessionDate   = sessionDate;
        this.bookingStatus = "Confirmed";
        this.memberTier    = memberTier;
        this.bookingDate   = LocalDate.now(); // record when booking was made
    }

    // ==================== Getters and Setters ====================

    public String getBookingId() { return bookingId; }
    public Member getMember() { return member; }
    public FitnessClass getFitnessClass() { return fitnessClass; }
    public LocalDate getSessionDate() { return sessionDate; }
    public String getBookingStatus() { return bookingStatus; }
    public String getMemberTier() { return memberTier; }
    public LocalDate getBookingDate() { return bookingDate; }

    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public void setMember(Member member) { this.member = member; }
    public void setFitnessClass(FitnessClass fitnessClass) { this.fitnessClass = fitnessClass; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public void setMemberTier(String memberTier) { this.memberTier = memberTier; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    // ==================== File Methods ====================

    /*
     * Returns formatted string for file storage
     * @return pipe-delimited string of booking data
     */
    public String toFileString() {
        String bookingDateStr = (bookingDate != null) ? bookingDate.toString() : LocalDate.now().toString();
        return bookingId + "|" + member.getMemberId() + "|" + fitnessClass.getClassId() + "|" +
               sessionDate + "|" + bookingStatus + "|" + memberTier + "|" + bookingDateStr;
    }

    // ==================== Display Methods ====================

    /*
     * Displays all bookings in a formatted table
     * @param bookings List of all bookings to display
     */
    public static void displayBookings(ArrayList<Booking> bookings) {
        System.out.println("\n=====================================================");
        System.out.println("              BOOKING LIST                      ");
        System.out.println("=====================================================");
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("[INFO] No bookings found.");
            System.out.println("=====================================================");
            return;
        }
        System.out.printf("  %-8s | %-15s | %-15s | %-12s | %-10s | %-10s%n",
            "BookingID", "Member", "Class", "Date", "Status", "Tier");
        System.out.println("  --------+-----------------+-----------------+--------------+------------+----------");
        for (Booking currentBooking : bookings) {
            System.out.println(currentBooking.toString());
        }
        System.out.println("=====================================================");
        System.out.println("  Total Bookings: " + bookings.size());
        System.out.println("=====================================================");
    }

    // ==================== Business Logic ====================

    /*
     * Performs the full booking process for a member
     * Checks class availability, duplicate bookings,
     * and Basic membership monthly booking limit
     *
     * @param member   Member making the booking
     * @param classes  List of all fitness classes
     * @param bookings List of all existing bookings
     * @param input    Scanner for user input
     */
    public static void performBooking(Member member, ArrayList<FitnessClass> classes,
                                       ArrayList<Booking> bookings, Scanner input) {
        System.out.println("\n========================================");
        System.out.println("       CLASS BOOKING PROCESS          ");
        System.out.println("========================================");

        try {
            if (classes == null || classes.isEmpty()) {
                System.out.println("[ERROR] No classes available for booking.");
                System.out.println("        Please check back later.");
                return;
            }

            System.out.print("Enter Class ID you want to book: ");
            String targetClassId = input.next().trim();

            if (targetClassId.isEmpty()) {
                System.out.println("[ERROR] Class ID cannot be empty.");
                return;
            }

            // Find class
            FitnessClass targetClass = null;
            for (FitnessClass fitnessClass : classes) {
                if (fitnessClass.getClassId().equalsIgnoreCase(targetClassId)) {
                    targetClass = fitnessClass;
                    break;
                }
            }

            if (targetClass == null) {
                System.out.println("[ERROR] Class with ID '" + targetClassId + "' not found.");
                return;
            }

            // Priority booking message for Gold members
            if (member.getMembership().hasPriorityBooking()) {
                System.out.println(">> Priority booking recognized for " + member.getName() + " (Gold Member)");
            }

            // Check duplicate booking
            for (Booking existingBooking : bookings) {
                if (existingBooking.getMember().getMemberId().equals(member.getMemberId()) &&
                    existingBooking.getFitnessClass().getClassId().equalsIgnoreCase(targetClassId) &&
                    existingBooking.getBookingStatus().equalsIgnoreCase("Confirmed")) {

                    System.out.println("[ERROR] You have already booked this class!");
                    System.out.println("        Booking ID: " + existingBooking.getBookingId());
                    return;
                }
            }

            // ===== Check Basic membership monthly booking limit =====
            if (member.getMembership() instanceof BasicMembership) {
                BasicMembership basic      = (BasicMembership) member.getMembership();
                int maxBookings            = basic.getMaxMonthlyBookings();
                int currentMonth           = LocalDate.now().getMonthValue();
                int currentYear            = LocalDate.now().getYear();

                int monthlyCount = 0;
                for (Booking b : bookings) {
                    if (b.getMember().getMemberId().equals(member.getMemberId())
                            && b.getBookingStatus().equalsIgnoreCase("Confirmed")
                            && b.getBookingDate() != null                              // use bookingDate
                            && b.getBookingDate().getMonthValue() == currentMonth      // month booking was MADE
                            && b.getBookingDate().getYear() == currentYear) {          // year booking was MADE
                        monthlyCount++;
                    }
                }

                System.out.println(">> Monthly bookings used: " + monthlyCount + "/" + maxBookings);

                if (monthlyCount >= maxBookings) {
                    System.out.println("[RESTRICTED] You have reached your monthly booking limit!");
                    System.out.println("             Basic members can only book " + maxBookings + " classes per month.");
                    System.out.println("             Please upgrade to Silver or Gold for unlimited bookings.");
                    return;
                }
            }
            // ===== End of Basic limit check =====

            // Enroll member into class
            if (targetClass.enrollMember(member)) {
                String generatedBookingId = "BK-" + (bookings.size() + 1001);

                Booking createdBooking = new Booking(
                    generatedBookingId,
                    member,
                    targetClass,
                    targetClass.getDate(),
                    member.getMembership().getMembershipName()
                );

                bookings.add(createdBooking);

                System.out.println("========================================");
                System.out.println("  SUCCESS: Booking Confirmed!");
                System.out.println("----------------------------------------");
                System.out.println("  Booking ID : " + generatedBookingId);
                System.out.println("  Class      : " + targetClass.getClassName());
                System.out.println("  Date       : " + targetClass.getDate());
                System.out.println("  Time       : " + targetClass.getTimeSlot());
                System.out.println("  Member     : " + member.getName());
                System.out.println("  Tier       : " + member.getMembership().getMembershipName());
                System.out.println("========================================");
            } else {
                System.out.println("[ERROR] Booking failed. Please check class availability.");
            }

        } catch (NullPointerException e) {
            System.out.println("[ERROR] Booking data is missing: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred during booking: " + e.getMessage());
        }
    }

    // ==================== Object Overrides ====================

    /*
     * Compares this Booking to another object
     * Two Bookings are equal if they have the same bookingId
     *
     * @param obj Object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return bookingId.equals(booking.bookingId);
    }

    /*
     * Returns a string representation of the Booking object
     *
     * @return Formatted row string with booking details
     */
    @Override
    public String toString() {
        String memberName  = (member != null) ? member.getName() : "Unknown";
        String className   = (fitnessClass != null) ? fitnessClass.getClassName() : "Unknown";
        String dateString  = (sessionDate != null) ? sessionDate.toString() : "TBA";
        return String.format("  %-8s | %-15s | %-15s | %-12s | %-10s | %-10s",
            bookingId,
            memberName,
            className,
            dateString,
            bookingStatus,
            memberTier);
    }

}