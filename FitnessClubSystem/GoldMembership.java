package FitnessClubSystem;

/*
 * GoldMembership Class
 * ------------------------
 * Represents the Gold tier membership (highest tier).
 * Unique attributes: hasPriorityBooking, hasSaunaAccess
 * 
 * Relationship:
 * - Subclass of Membership (Inheritance)
 */

// Group member - Cheng Jun Yu

public class GoldMembership extends Membership {

    private static final double DEFAULT_PRICE = 200.0;
    private boolean hasPriorityBooking;
    private boolean hasSaunaAccess;

    /*
     * Parameterized constructor for GoldMembership
     */
    public GoldMembership(boolean hasPriorityBooking, boolean hasSaunaAccess) {
        super("Gold", DEFAULT_PRICE);
        this.hasPriorityBooking = hasPriorityBooking;
        this.hasSaunaAccess = hasSaunaAccess;
    }

    // ==================== Getters and Setters ====================

    /*
     * Sets the priority booking privilege
     */
    public void setHasPriorityBooking(boolean hasPriorityBooking) {
        this.hasPriorityBooking = hasPriorityBooking;
    }

    /*
     * Sets the sauna access privilege
     */
    public void setHasSaunaAccess(boolean hasSaunaAccess) {
        this.hasSaunaAccess = hasSaunaAccess;
    }

    // ==================== Abstract Method Implementations ====================

 
    // Calculates the Gold membership fee
    @Override
    public double calculateFee() {
        return getPrice(); // changed from 'price' since field is now private in parent
    }

    /*
     * Checks if Gold membership includes priority booking
     * 
     * @return true if priority booking is available
     */
    @Override
    public boolean hasPriorityBooking() {
        return hasPriorityBooking;
    }

    /*
     * Checks if Gold membership includes sauna access
     * 
     * @return true if sauna access is available
     */
    @Override
    public boolean hasSaunaAccess() {
        return hasSaunaAccess;
    }

    // ==================== Object Overrides ====================

    /*
     * Compares this GoldMembership to another object
     * Two GoldMemberships are equal if parent fields match AND
     * both privilege flags are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        GoldMembership other = (GoldMembership) obj;
        return hasPriorityBooking == other.hasPriorityBooking &&
               hasSaunaAccess == other.hasSaunaAccess;
    }

    /* Returns a string representation of the GoldMembership object */
    @Override
    public String toString() {
        return super.toString() + String.format(" | Priority Booking: %s | Sauna Access: %s",
            hasPriorityBooking ? "Yes" : "No",
            hasSaunaAccess ? "Yes" : "No");
    }

    
}