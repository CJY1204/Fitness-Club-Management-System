package FitnessClubSystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
  FileManager Class
  ---------------------------------------------------
  Handles all file read/write operations for the system.
 
  Responsibilities:
  - Save and load Members, Trainers, Admins
  - Save and load Bookings, FitnessClasses, Equipment
 */

// Group member - Tang Zhi Hao

public class FileManager {
//Members
//Saves all members to members.txt
//param members List of all members to save
    
    public void saveAllMembers(ArrayList<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("members.txt", false))) {
            for (Member m : members) {
                String data = m.getMemberId() + "|" +
                              m.getPassword() + "|" +
                              m.getName() + "|" +
                              m.getAge() + "|" +
                              m.getHeight() + "|" +
                              m.getWeight() + "|" +
                              m.getMembership().getMembershipName() + "|" +
                              m.getStatus();
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    
    //Loads all members from members.txt
    //return ArrayList of Member objects loaded from file
     
    public ArrayList<Member> loadMembers() {
        ArrayList<Member> members = new ArrayList<>();
        try {
            File file = new File("members.txt");
            if (!file.exists()) {
                System.out.println("Members file not found: members.txt");
                return members;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split("\\|");
                if(data.length < 8){
                    System.out.println("Skipping invalid member record: " + line);
                    continue;
                }

                String id             = data[0];
                String password       = data[1];
                String name           = data[2];
                int age               = Integer.parseInt(data[3]);
                double height         = Double.parseDouble(data[4]);
                double weight         = Double.parseDouble(data[5]);
                String membershipType = data[6];
                String status         = data[7];

                Membership membership = null;
        
                if (membershipType.equalsIgnoreCase("Basic")) {
                    membership = new BasicMembership(10);
                } else if (membershipType.equalsIgnoreCase("Silver")) {
                    membership = new SilverMembership(true);
                } else if (membershipType.equalsIgnoreCase("Gold")) {
                    membership = new GoldMembership(true, true);
                }

                if(membership == null){
                    System.out.println("Invalid mambership type for member: "+ id);
                    continue;
                }

                Member member = new Member(id, password, name, age, height, weight, membership, status);
                members.add(member);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading members file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing member data: " + e.getMessage());
        }
        return members;
    }

    // Bookings
    //Saves all bookings to bookings.txt
    //@param bookings List of all bookings to save
    public void saveBookings(ArrayList<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("bookings.txt"))) {
            for (Booking b : bookings) {
                writer.println(b.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    
     //Loads all bookings from bookings.txt
     //@param members List of members to match booking references
     //@param classes List of fitness classes to match booking references
     //@return ArrayList of Booking objects loaded from file
    public ArrayList<Booking> loadBookings(ArrayList<Member> members, ArrayList<FitnessClass> classes) {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File("bookings.txt");
        if (!file.exists()) {
            System.out.println("Bookings file not found: bookings.txt");
            return bookings;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 6) {
                    String bId      = parts[0];
                    String mId      = parts[1];
                    String cId      = parts[2];
                    LocalDate bookingDate = (parts.length >= 7) ? LocalDate.parse(parts[6]) : LocalDate.now();
                    String status   = parts[4];
                    String tier     = parts[5];

                    Member foundMember = null;
                    for (Member m : members) {
                        if (m.getMemberId().equals(mId)) {
                            foundMember = m;
                            break;
                        }
                    }

                    FitnessClass foundClass = null;
                    for (FitnessClass c : classes) {
                        if (c.getClassId().equals(cId)) {
                            foundClass = c;
                            break;
                        }
                    }

                    if (foundMember != null && foundClass != null) {
                        Booking b = new Booking(bId, foundMember, foundClass, bookingDate, tier);
                        b.setBookingStatus(status);
                        b.setBookingDate(bookingDate);
                        if (status.equalsIgnoreCase("Confirmed")) {
                            foundClass.addMember(foundMember);
                        }
                        bookings.add(b);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing booking data: " + e.getMessage());
        }
        return bookings;
    }

    //Fitness Classes
    
    public void addFitnessClass(FitnessClass fitnessClass) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fitnessclasses.txt", true))) {
            String instructorId = (fitnessClass.getInstructor() != null) ? fitnessClass.getInstructor().getTrainerId() : "TBA";
            String dateStr = (fitnessClass.getDate() != null) ? fitnessClass.getDate().toString() : "TBA";

            writer.write(fitnessClass.getClassId() + "|" +
                        fitnessClass.getClassName() + "|" +
                        dateStr + "|" +
                        fitnessClass.getTimeSlot() + "|" +
                        instructorId + "|" +
                        fitnessClass.getCurrentEnrollment() + "|" +
                        fitnessClass.getMaxCapacity());
            writer.newLine();
            System.out.println("Fitness class added: " + fitnessClass.getClassName());
        } catch (IOException e) {
            System.out.println("Error saving fitness class data.");
        }
    }


     //Updates all fitness classes in fitnessclasses.txt
     //@param fitnessClasses List of all fitness classes to save

    public void updateFitnessClasses(ArrayList<FitnessClass> fitnessClasses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fitnessclasses.txt"))) {
            for (FitnessClass fc : fitnessClasses) {
                String instructorId = (fc.getInstructor() != null) ? fc.getInstructor().getTrainerId() : "TBA";
                String dateStr      = (fc.getDate() != null) ? fc.getDate().toString() : "TBA";

                writer.write(fc.getClassId() + "|" +
                             fc.getClassName() + "|" +
                             dateStr + "|" +
                             fc.getTimeSlot() + "|" +
                             instructorId + "|" +
                             fc.getCurrentEnrollment() + "|" +
                             fc.getMaxCapacity());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating fitness classes: " + e.getMessage());
        }
    }


    // Loads all fitness classes from fitnessclasses.txt
    public ArrayList<FitnessClass> loadFitnessClasses(ArrayList<Trainer> trainers) {
        ArrayList<FitnessClass> fitnessClasses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            File file = new File("fitnessclasses.txt");
            if (!file.exists()) {
                System.out.println("Fitness classes file not found: fitnessclasses.txt");
                return fitnessClasses;
            }

            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length >= 7) {
                    String classId          = data[0];
                    String className        = data[1];
                    LocalDate date          = data[2].equals("TBA") ? null : LocalDate.parse(data[2], formatter);
                    String timeSlot         = data[3];
                    String instructorId     = data[4];
                    int currentEnrollment   = Integer.parseInt(data[5]);
                    int maxCapacity         = Integer.parseInt(data[6]);

                    Trainer instructor = null;
                    if (!instructorId.equals("TBA")) {
                        for (Trainer t : trainers) {
                            if (t.getTrainerId().equals(instructorId)) {
                                instructor = t;
                                break;
                            }
                        }
                    }

                    FitnessClass fc = new FitnessClass(classId, className, maxCapacity);
                    fc.setSchedule(date, timeSlot);
                    fc.setInstructor(instructor);
                    fc.setCurrentEnrollment(currentEnrollment);
                    fitnessClasses.add(fc);
                } else {
                    System.out.println("Skipping invalid fitness class entry: " + line);
                }
            }
            reader.close();
            System.out.println("Loaded " + fitnessClasses.size() + " fitness classes.");
        } catch (IOException e) {
            System.out.println("Error loading fitness classes file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing fitness class data: " + e.getMessage());
        }
        return fitnessClasses;
    }

    //Trainers
    
    public void saveTrainer(Trainer trainer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trainers.txt", true))) {
            writer.write(trainer.getTrainerId() + "|" + trainer.getPassword() + "|" + trainer.getName());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving trainer data.");
        }
    }


     //Updates all trainers in trainers.txt
     //@param trainers List of all trainers to save
    public void updateTrainers(ArrayList<Trainer> trainers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trainers.txt"))) {
            for (Trainer trainer : trainers) {
                writer.write(trainer.getTrainerId() + "|" +
                             trainer.getPassword() + "|" +
                             trainer.getName());
                writer.newLine();
            }
            System.out.println("Trainer data updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating trainers: " + e.getMessage());
        }
    }

    
     //Loads all trainers from trainers.txt
     //@return ArrayList of Trainer objects loaded from file
    
    public ArrayList<Trainer> loadTrainers() {
        ArrayList<Trainer> trainers = new ArrayList<>();
        try {
            File file = new File("trainers.txt");
            if (!file.exists()) {
                System.out.println("Trainers file not found: trainers.txt");
                return trainers;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length >= 3) {
                    String id       = data[0];
                    String password = data[1];
                    String name     = data[2];
                    trainers.add(new Trainer(id, password, name));
                } else {
                    System.out.println("Skipping invalid trainer entry: " + line);
                }
            }
            reader.close();
            System.out.println("Loaded " + trainers.size() + " trainers.");
        } catch (IOException e) {
            System.out.println("Error loading trainers file: " + e.getMessage());
        }
        return trainers;
    }

    //Equipment
     //Loads all equipment from equipments.txt
     //@return ArrayList of Equipment objects loaded from file
    
    public ArrayList<Equipment> loadEquipments() {
        ArrayList<Equipment> equipments = new ArrayList<>();
        try {
            File file = new File("equipments.txt");
            if (!file.exists()) {
                System.out.println("Equipment file not found: equipments.txt");
                return equipments;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length == 3) {
                    String id     = data[0];
                    String name   = data[1];
                    String status = data[2];
                    equipments.add(new Equipment(id, name, status));
                } else {
                    System.out.println("Skipping invalid equipment entry: " + line);
                }
            }
            reader.close();
            System.out.println("Loaded " + equipments.size() + " equipment items.");
        } catch (IOException e) {
            System.out.println("Error loading equipments file: " + e.getMessage());
        }
        return equipments;
    }


     //Updates all equipment in equipments.txt
     //@param equipments List of all equipment to save

    public void updateEquipments(ArrayList<Equipment> equipments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("equipments.txt"))) {
            for (Equipment e : equipments) {
                writer.write(e.toFileString());
                writer.newLine();
            }
            System.out.println("Equipment data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving equipment data: " + e.getMessage());
        }
    }

    // Admins 
     //Loads all admins from admins.txt
     //@return ArrayList of Admin objects loaded from file

    public ArrayList<Admin> loadAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        
        try {
            File file = new File("admins.txt");
            if (!file.exists()) {
                System.out.println("Admins file not found: admins.txt");
                return admins;
            }

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.isEmpty()){
                    continue;
                }

                String[] data     = line.split("\\|");
                if(data.length < 4){
                    System.out.println("Skipping invalid admin record: "+ line);
                    continue;
                }

                String adminId    = data[0].trim();
                String password   = data[1].trim();
                String name       = data[2].trim();
                String email      = data[3].trim();

                admins.add(new Admin(adminId, password, name, email));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error loading admins file: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error loading admins: " +e.getMessage());
        }

        return admins;
    }

     //Save All 
     //Saves all system data to their respective files
     //@param members    List of all members
     //@param trainers   List of all trainers
     //@param equipments List of all equipment
     //@param classes    List of all fitness classes
     //@param bookings   List of all bookings

    public void saveAll(ArrayList<Member> members,
                        ArrayList<Trainer> trainers,
                        ArrayList<Equipment> equipments,
                        ArrayList<FitnessClass> classes,
                        ArrayList<Booking> bookings) {
        try {
            System.out.println("Saving all data to files...");
            saveAllMembers(members);
            updateTrainers(trainers);
            updateEquipments(equipments);
            updateFitnessClasses(classes);
            saveBookings(bookings);
            System.out.println("All data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error saving all data: " + e.getMessage());
        }
    }

}