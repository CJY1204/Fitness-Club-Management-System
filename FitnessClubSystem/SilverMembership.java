package FitnessClubSystem;

/*
 * SilverMembership Class
 * ------------------------
 * Represents the Silver tier membership.
 * Unique attribute: hasSaunaAccess
 * 
 */

//Group member - Cheng Jun Yu

public class SilverMembership extends Membership {

    private static final double DEFAULT_PRICE = 100.0;
    private boolean hasSaunaAccess;

    /*
     * Parameterized constructor for SilverMembership
     * @param hasSaunaAccess Whether the member has sauna access
     */
    public SilverMembership(boolean hasSaunaAccess) {
        super("Silver", DEFAULT_PRICE);
        this.hasSaunaAccess = hasSaunaAccess;
    }

    /*
     * Sets the sauna access privilege
     * @param hasSaunaAccess New sauna access value
     */
    public void setHasSaunaAccess(boolean hasSaunaAccess) {
        this.hasSaunaAccess = hasSaunaAccess;
    }


    @Override
    public boolean hasPriorityBooking() {
        return false;
    }

    @Override
    public boolean hasSaunaAccess() {
        return hasSaunaAccess;
    }

    @Override
    public double calculateFee() {
        return getPrice(); // changed from 'price' since field is now private in parent
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        SilverMembership other = (SilverMembership) obj;
        return hasSaunaAccess == other.hasSaunaAccess;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Sauna Access: %s", 
            hasSaunaAccess ? "Yes" : "No");
    }

}