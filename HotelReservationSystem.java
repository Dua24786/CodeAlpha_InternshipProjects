import java.util.*;
import java.io.*;

class Room {
    private int roomNumber;
    private String category;
    private double price;
    private boolean available;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Room No: " + roomNumber +
                " | Category: " + category +
                " | Price: $" + price +
                " | Available: " + available;
    }
}

class Reservation {
    private String customerName;
    private Room room;

    public Reservation(String customerName, Room room) {
        this.customerName = customerName;
        this.room = room;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Customer: " + customerName +
                " | Room: " + room.getRoomNumber() +
                " | Category: " + room.getCategory();
    }
}

class Payment {
    public static boolean processPayment(double amount) {
        System.out.println("Processing payment of $" + amount);
        System.out.println("Payment Successful!");
        return true;
    }
}

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    public static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 50));
        rooms.add(new Room(102, "Standard", 50));

        rooms.add(new Room(201, "Deluxe", 100));
        rooms.add(new Room(202, "Deluxe", 100));

        rooms.add(new Room(301, "Suite", 200));
    }

    public static void searchRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.println(room);
            }
        }
    }

    public static void bookRoom(String customerName, int roomNo) {

        for (Room room : rooms) {

            if (room.getRoomNumber() == roomNo && room.isAvailable()) {

                if (Payment.processPayment(room.getPrice())) {

                    room.setAvailable(false);

                    Reservation reservation =
                            new Reservation(customerName, room);

                    reservations.add(reservation);

                    saveReservation(reservation);

                    System.out.println("Booking Successful!");
                }
                return;
            }
        }

        System.out.println("Room not available.");
    }

    public static void viewReservations() {

        if (reservations.isEmpty()) {
            System.out.println("No Reservations Found.");
            return;
        }

        System.out.println("\nBooking Details:");
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }

    public static void modifyReservation(String customerName,
                                         int newRoomNo) {

        for (Reservation reservation : reservations) {

            if (reservation.getCustomerName()
                    .equalsIgnoreCase(customerName)) {

                reservation.getRoom().setAvailable(true);

                reservations.remove(reservation);

                bookRoom(customerName, newRoomNo);

                System.out.println("Reservation Modified.");
                return;
            }
        }

        System.out.println("Reservation not found.");
    }

    public static void cancelReservation(String customerName) {

        Iterator<Reservation> iterator =
                reservations.iterator();

        while (iterator.hasNext()) {

            Reservation reservation = iterator.next();

            if (reservation.getCustomerName()
                    .equalsIgnoreCase(customerName)) {

                reservation.getRoom().setAvailable(true);

                iterator.remove();

                System.out.println("Reservation Cancelled.");
                return;
            }
        }

        System.out.println("Reservation not found.");
    }

    public static void saveReservation(Reservation reservation) {

        try (FileWriter writer =
                     new FileWriter("reservations.txt", true)) {

            writer.write(reservation.toString() + "\n");

        } catch (IOException e) {
            System.out.println("File Error!");
        }
    }

    public static void main(String[] args) {

        initializeRooms();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Search Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. View Booking Details");
            System.out.println("4. Modify Reservation");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    searchRooms();
                    break;

                case 2:
                    System.out.print("Enter Customer Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Number: ");
                    int roomNo = sc.nextInt();

                    bookRoom(name, roomNo);
                    break;

                case 3:
                    viewReservations();
                    break;

                case 4:
                    System.out.print("Enter Customer Name: ");
                    String customer = sc.nextLine();

                    System.out.print("Enter New Room Number: ");
                    int newRoom = sc.nextInt();

                    modifyReservation(customer, newRoom);
                    break;

                case 5:
                    System.out.print("Enter Customer Name: ");
                    String cancelName = sc.nextLine();

                    cancelReservation(cancelName);
                    break;

                case 6:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}