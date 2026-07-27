package FitnessClubSystem;

/*
 * Abstract Membership Class
 * ------------------------
 * Demonstrates ABSTRACTION & POLYMORPHISM.
 * Different membership types override calculateFee().
 * 
 * Relationship:
 * - Parent class of BasicMembership, SilverMembership, GoldMembership
 * 
 */

// Group member - Cheng Jun Yu

public abstract class Membership {

    private String membershipName;
    private double price;

    /*
     * Parameterized constructor for Membership
     * 
     * @param membershipName Name of the membership tier
     * @param price          Monthly fee for the membership
     */
    public Membership(String membershipName, double price) {
        this.membershipName = membershipName;
        this.price = price;
    }

    // ==================== Getters and Setters ====================

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // ==================== Abstract Methods ====================

    /*
     * Calculates the membership fee
     * To be implemented by each subclass
     * @return calculated fee as double
     */
    public abstract double calculateFee();

    /*
     * Checks if membership includes priority class booking privilege
     * @return true if priority booking is available, false otherwise
     */
    public abstract boolean hasPriorityBooking();

    /*
     * Checks if membership includes sauna access privilege
     * @return true if sauna access is available, false otherwise
     */
    public abstract boolean hasSaunaAccess();

    // ==================== Object Overrides ====================

    /*
     * Compares this Membership to another object
     * Two Memberships are equal if they have the same name and price
     * 
     * @param obj Object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Membership other = (Membership) obj;
        return membershipName.equals(other.membershipName) &&
               Double.compare(price, other.price) == 0;
    }

    /*
     * Returns a string representation of the Membership object
     * 
     * @return Formatted string with membership name and price
     */
    @Override
    public String toString() {
        return String.format("%s Membership (RM%.2f)", membershipName, price);
    }

}