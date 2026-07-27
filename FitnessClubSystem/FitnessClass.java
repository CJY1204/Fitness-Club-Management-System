package FitnessClubSystem;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// FitnessClass
// - AGGREGATION with Member (class contains members)
// by Khoo See Ze

public class FitnessClass {

    private String classId;
    private String className;
    private LocalDate date;
    private String timeSlot;
    private Trainer instructor;
    private int maxCapacity;
    private int currentEnrollment;
    private ArrayList<Member> bookedMembers;
    private static final int GOLD_RESERVED_SLOTS = 2;
    

    public FitnessClass(String classId, String className, int maxCapacity) {
        this.classId = classId;
        this.className = className;
        this.maxCapacity = maxCapacity;
        this.bookedMembers = new ArrayList<>();
    }


    public void setClassId(String classId) { this.classId = classId; }
    public void setClassName(String className) { this.className = className; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public void setInstructor(Trainer trainer) {
        this.instructor = trainer;
    }

    public void setSchedule(LocalDate date, String timeSlot) {
        this.date = date;
        this.timeSlot = timeSlot;
    }
    public void setCurrentEnrollment(int currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    public String getClassName() { return className; }
    public String getClassId() { return classId; }
    public LocalDate getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public Trainer getInstructor() { return instructor; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentEnrollment() { return currentEnrollment; }
    public ArrayList<Member> getBookedMembers() {return bookedMembers;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FitnessClass other = (FitnessClass) obj;
        return classId.equals(other.classId);
    }

    public static void showMemberSchedule(ArrayList<FitnessClass> classes) {
        System.out.println("\n==========================================================================");
        System.out.println("                         WEEKLY CLASS SCHEDULE                            ");
        System.out.println("==========================================================================");

        if (classes == null || classes.isEmpty()) {
            System.out.println("No classes scheduled at the moment. Check back later!");
            return;
        }

        System.out.println("+---------+----------------+--------------------+------------+------------+");
        System.out.printf("| %-7s | %-14s | %-18s | %-10s | %-10s |%n",
            "ID", "Class Name", "Date & Time", "Instructor", "Available");
        System.out.println("+---------+----------------+--------------------+------------+------------+");

        for (FitnessClass c : classes) {
            System.out.println(c.toMemberString()); // use toMemberString()
        }

        System.out.println("+---------+----------------+--------------------+------------+------------+");
    }

    public String toMemberString() {
        String schedule = (date != null && timeSlot != null)
                        ? date.toString() + " " + timeSlot
                        : "TBA";
        String trainerName = (instructor != null) ? instructor.getName() : "TBA";
        String availability = bookedMembers.size() + "/" + maxCapacity;

        return String.format("| %-7s | %-14s | %-18s | %-10s | %-10s |",
            classId, className, schedule, trainerName, availability);
    }
    //jy

    // show class only for trainer 
    public static void displayClasses(ArrayList<FitnessClass> classes) {
        System.out.println("+---------+----------------+--------------------+-----------+---------+----------+");
        System.out.printf("| %-7s | %-14s | %-18s | %-9s | %-7s | %-8s |%n",
            "ClassID", "Class Name", "Schedule", "TrainerID", "Current", "Capacity");
        System.out.println("+---------+----------------+--------------------+-----------+---------+----------+");

        if (classes == null || classes.isEmpty()) {
            System.out.println("| No classes available.                                                        |");
        } else {
            for (FitnessClass c : classes) {
                System.out.println(c.toString()); // each row via toString()
            }
        }

        System.out.println("+---------+----------------+--------------------+-----------+---------+----------+");
    }

    // for admin
    public static void printClassDetail(ArrayList<FitnessClass> classes, ArrayList<Trainer> trainers) {
        System.out.println("\n========================================");
        System.out.println("         STAFF ASSIGNMENT MENU          ");
        System.out.println("========================================");
        if (classes.isEmpty()) {
            System.out.println("No fitness classes available.");
            return;
        }

        if (trainers.isEmpty()) {
            System.out.println("No trainers available.");
            return;
        }
        
        System.out.println("\n--- Available Fitness Classes ---");
        FitnessClass.displayClasses(classes); // use your new unified method


        System.out.println("\n--- Available Trainers ---");
        Trainer.displayTrainers(trainers);    // Trainer handles its own display
    }

    public boolean enrollMember(Member member) {
        int currentSize = bookedMembers.size();

        if (currentSize >= maxCapacity) {
            System.out.println("\n[FAILED] This class is absolutely full!");
            return false;
        }

        if (currentSize >= (maxCapacity - GOLD_RESERVED_SLOTS)) {
            if (!member.getMembership().hasPriorityBooking()) {
                System.out.println("\n[RESTRICTED] Only Gold Members can book the last " + GOLD_RESERVED_SLOTS + " priority slots.");
                System.out.println("Please upgrade your membership to Gold for priority access.");
                return false;
            }
        }

        bookedMembers.add(member);
        currentEnrollment = bookedMembers.size(); 
        return true;
    }

    public boolean isFullForMember(Member member) {
        boolean isGold = member.getMembership().getMembershipName().equalsIgnoreCase("Gold");
        if (isGold) {
            return bookedMembers.size() >= maxCapacity;
        } else {
            return bookedMembers.size() >= (maxCapacity - GOLD_RESERVED_SLOTS);
        }
    }

    public boolean addMember(Member member) {
        if (bookedMembers.size() < maxCapacity) {
            bookedMembers.add(member);
            this.currentEnrollment = bookedMembers.size(); 
            return true;
        }
        return false;
    }

    public boolean cancelEnrollment(Member member) {
        if (bookedMembers.remove(member)) {
            this.currentEnrollment = bookedMembers.size(); 
            return true;
        }
        return false;
    }


    // let admin use this method
    public static void createClass(ArrayList<FitnessClass> classes, Scanner input) {
        System.out.println("\n========================================");
        System.out.println("         CREATE NEW CLASS                ");
        System.out.println("========================================");
        
        try {
            System.out.print("Enter Class ID: ");
            String classId = input.next().trim();
            if (classId.isEmpty()) {
                System.out.println("[ERROR] Class ID cannot be empty.");
                return;
            }
            for (FitnessClass c : classes) {
                if (c.getClassId().equalsIgnoreCase(classId)) {
                    System.out.println("[ERROR] Class ID '" + classId + "' already exists.");
                    return;
                }
            }
            input.nextLine();
            System.out.print("Enter Class Name: ");
            String className = input.nextLine().trim();
            if (className.isEmpty()) {
                System.out.println("[ERROR] Class Name cannot be empty.");
                return;
            }
            
            System.out.print("Enter Max Capacity: ");
            if (!input.hasNextInt()) {
                System.out.println("[ERROR] Invalid capacity. Please enter a number.");
                input.next();
                return;
            }
            int maxCapacity = input.nextInt();
            if (maxCapacity <= 0) {
                System.out.println("[ERROR] Capacity must be greater than 0.");
                return;
            }

            FitnessClass newClass = new FitnessClass(classId, className, maxCapacity);
            classes.add(newClass);
            System.out.println("========================================");
            System.out.println("  SUCCESS: Class '" + className + "' created.");
            System.out.println("========================================");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred while creating class.");
        }
    }
    public static void setSchedule(ArrayList<FitnessClass> classes, Scanner input) {
        System.out.println("\n========================================");
        System.out.println("         SET CLASS SCHEDULE             ");
        System.out.println("========================================");
        
        try {
            System.out.print("Enter Class ID: ");
            String classId = input.next().trim();
            if (classId.isEmpty()) {
                System.out.println("[ERROR] Class ID cannot be empty.");
                return;
            }
            
            FitnessClass targetClass = null;
            for (FitnessClass c : classes) {
                if (c.getClassId().equalsIgnoreCase(classId)) {
                    targetClass = c;
                    break;
                }
            }
            
            if (targetClass == null) {
                System.out.println("[ERROR] Class with ID '" + classId + "' not found.");
                return;
            }
            
            input.nextLine();
            System.out.print("Enter Date (yyyy-MM-dd): ");
            String dateStr = input.nextLine().trim();
            if (dateStr.isEmpty()) {
                System.out.println("[ERROR] Date cannot be empty.");
                return;
            }
            
            System.out.print("Enter Time Slot (e.g., 10:00AM): ");
            String timeSlot = input.nextLine().trim();
            if (timeSlot.isEmpty()) {
                System.out.println("[ERROR] Time slot cannot be empty.");
                return;
            }

            LocalDate date = LocalDate.parse(dateStr);
            targetClass.setSchedule(date, timeSlot);
            System.out.println("========================================");
            System.out.println("  SUCCESS: Schedule set for " + targetClass.getClassName());
            System.out.println("           on " + date + " at " + timeSlot);
            System.out.println("========================================");
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("[ERROR] Invalid date format. Please use yyyy-MM-dd (e.g., 2026-03-18).");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred while setting schedule.");
        }
    }

    public static void deleteClass(ArrayList<FitnessClass> classes, Scanner input) {
        System.out.println("\n========================================");
        System.out.println("         DELETE CLASS                  ");
        System.out.println("========================================");
        
        try {
            if (classes.isEmpty()) {
                System.out.println("[INFO] No classes available to delete.");
                return;
            }
            
            System.out.print("Enter Class ID to delete: ");
            String classId = input.next().trim();
            if (classId.isEmpty()) {
                System.out.println("[ERROR] Class ID cannot be empty.");
                return;
            }

            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).getClassId().equalsIgnoreCase(classId)) {
                    String deletedName = classes.get(i).getClassName();
                    classes.remove(i);
                    System.out.println("========================================");
                    System.out.println("  SUCCESS: Class '" + deletedName + "' deleted.");
                    System.out.println("========================================");
                    return;
                }
            }
            System.out.println("[ERROR] Class with ID '" + classId + "' not found.");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred while deleting class.");
        }
    }

    public static void assignTrainerToClass(ArrayList<FitnessClass> classes, ArrayList<Trainer> trainers, Scanner input) {
        System.out.println("\n========================================");
        System.out.println("      ASSIGN TRAINER TO CLASS          ");
        System.out.println("========================================");
        
        try {
            if (classes.isEmpty()) {
                System.out.println("[ERROR] No classes available. Please create a class first.");
                return;
            }
            if (trainers.isEmpty()) {
                System.out.println("[ERROR] No trainers available. Please add trainers first.");
                return;
            }
            
            System.out.print("Enter Class ID: ");
            String classId = input.next().trim();
            if (classId.isEmpty()) {
                System.out.println("[ERROR] Class ID cannot be empty.");
                return;
            }

            System.out.print("Enter Trainer ID: ");
            String trainerId = input.next().trim();
            if (trainerId.isEmpty()) {
                System.out.println("[ERROR] Trainer ID cannot be empty.");
                return;
            }

            FitnessClass selectedClass = null;
            for (FitnessClass c : classes) {
                if (c.getClassId().equalsIgnoreCase(classId)) {
                    selectedClass = c;
                    break;
                }
            }

            Trainer selectedTrainer = null;
            for (Trainer t : trainers) {
                if (t.getTrainerId().equalsIgnoreCase(trainerId)) {
                    selectedTrainer = t;
                    break;
                }
            }

            if (selectedClass == null && selectedTrainer == null) {
                System.out.println("[ERROR] Both Class ID '" + classId + "' and Trainer ID '" + trainerId + "' not found.");
            } else if (selectedClass == null) {
                System.out.println("[ERROR] Class with ID '" + classId + "' not found.");
            } else if (selectedTrainer == null) {
                System.out.println("[ERROR] Trainer with ID '" + trainerId + "' not found.");
            } else {
                selectedClass.setInstructor(selectedTrainer);
                System.out.println("========================================");
                System.out.println("  SUCCESS: Trainer assigned!");
                System.out.println("  Trainer   : " + selectedTrainer.getName());
                System.out.println("  Class     : " + selectedClass.getClassName());
                System.out.println("  Schedule  : " + (selectedClass.getDate() != null ? selectedClass.getDate() + " " + selectedClass.getTimeSlot() : "TBA"));
                System.out.println("========================================");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred while assigning trainer.");
        }
    }

    @Override
    public String toString() {
        String instructorId = (instructor != null) ? instructor.getTrainerId() : "pending";
        String schedule = (date != null && timeSlot != null) 
                        ? date.toString() + " " + timeSlot 
                        : "TBA";
        return String.format("| %-7s | %-14s | %-18s | %-9s | %-7d | %-8d |",
            classId, className, schedule, instructorId,
            bookedMembers.size(), maxCapacity);
    }


}