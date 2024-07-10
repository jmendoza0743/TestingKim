import java.sql.*;
import java.util.Scanner;

public class DogAdoptionSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dog_adoption";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database successfully.");

            while (true) {
                System.out.println("\n--- Paws for Adoption: Dog Adoption System ---");
                System.out.println("1. Add a Dog");
                System.out.println("2. Add a Shelter");
                System.out.println("3. Add an Adopter");
                System.out.println("4. Record an Adoption");
                System.out.println("5. View Available Dogs");
                System.out.println("6. View Available Shelters");
                System.out.println("7. Update Dog Information");
                System.out.println("8. Delete Dog");
                System.out.println("9. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addDog(conn, scanner);
                        break;
                    case 2:
                        addShelter(conn, scanner);
                        break;
                    case 3:
                        addAdopter(conn, scanner);
                        break;
                    case 4:
                        recordAdoption(conn, scanner);
                        break;
                    case 5:
                        viewAvailableDogs(conn);
                        break;
                    case 6:
                        viewAvailableShelters(conn);
                        break;
                    case 7:
                        updateDog(conn, scanner);
                        break;
                    case 8:
                        deleteDog(conn, scanner);
                        break;
                    case 9:
                        System.out.println("Exiting program. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found. Please add it to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection error");
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing database connection.");
                e.printStackTrace();
            }
            if (scanner != null) scanner.close();
        }
    }

    private static void addDog(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n--- Add a Dog ---");
        System.out.print("Enter dog name: ");
        String name = scanner.nextLine();
        System.out.print("Enter breed: ");
        String breed = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter size: ");
        String size = scanner.nextLine();
        System.out.print("Enter shelter ID: ");
        int shelterId = scanner.nextInt();

        String sql = "INSERT INTO Dogs (Name, Breed, Age, Size, ShelterID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, breed);
            pstmt.setInt(3, age);
            pstmt.setString(4, size);
            pstmt.setInt(5, shelterId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Dog added successfully.");
            } else {
                System.out.println("Failed to add dog.");
            }
        }
    }

    private static void addShelter(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n--- Add a Shelter ---");
        System.out.print("Enter shelter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();

        String sql = "INSERT INTO Shelters (Name, Location, ContactInfo) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, contactInfo);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Shelter added successfully.");
            } else {
                System.out.println("Failed to add shelter.");
            }
        }
    }

    private static void addAdopter(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n--- Add an Adopter ---");
        System.out.print("Enter adopter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();

        String sql = "INSERT INTO Adopters (Name, Age, Address, ContactInfo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, address);
            pstmt.setString(4, contactInfo);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Adopter added successfully.");
            } else {
                System.out.println("Failed to add adopter.");
            }
        }
    }

    private static void recordAdoption(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n--- Record an Adoption ---");
        System.out.print("Enter dog ID: ");
        int dogId = scanner.nextInt();
        System.out.print("Enter adopter ID: ");
        int adopterId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter adoption date (YYYY-MM-DD): ");
        String adoptionDate = scanner.nextLine();

        String sql = "INSERT INTO Adoptions (DogID, AdopterID, AdoptionDate) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dogId);
            pstmt.setInt(2, adopterId);
            pstmt.setDate(3, Date.valueOf(adoptionDate));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Adoption recorded successfully.");
            } else {
                System.out.println("Failed to record adoption.");
            }
        }
    }

    private static void viewAvailableDogs(Connection conn) throws SQLException {
        String sql = "SELECT * FROM AvailableDogs";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Available Dogs ---");
            System.out.printf("%-5s %-15s %-15s %-5s %-10s %-20s%n", "ID", "Name", "Breed", "Age", "Size", "Shelter");
            System.out.println("------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-15s %-5d %-10s %-20s%n",
                        rs.getInt("DogID"),
                        rs.getString("Name"),
                        rs.getString("Breed"),
                        rs.getInt("Age"),
                        rs.getString("Size"),
                        rs.getString("ShelterName"));
            }
        }
    }

    private static void viewAvailableShelters(Connection conn) throws SQLException {
        String sql = "SELECT * FROM AvailableShelters";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Available Shelters ---");
            System.out.printf("%-5s %-20s %-30s %-20s%n", "ID", "Name", "Location", "Contact Info");
            System.out.println("------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-30s %-20s%n",
                        rs.getInt("ShelterID"),
                        rs.getString("Name"),
                        rs.getString("Location"),
                        rs.getString("ContactInfo"));
            }
        }
    }

    private static void updateDog(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n--- Update Dog Information ---");
        System.out.print("Enter dog ID to update: ");
        int dogId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new name (press enter to skip): ");
        String newName = scanner.nextLine();
        System.out.print("Enter new age (0 to skip): ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new size (press enter to skip): ");
        String newSize = scanner.nextLine();

        String sql = "UPDATE Dogs SET Name = COALESCE(?, Name), Age = COALESCE(?, Age), Size = COALESCE(?, Size) WHERE DogID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName.isEmpty() ? null : newName);
            pstmt.setObject(2, newAge == 0 ? null : newAge);
            pstmt.setString(3, newSize.isEmpty() ? null : newSize);
            pstmt.setInt(4, dogId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Dog information updated successfully.");
            } else {
                System.out.println("Update failed. Dog not found.");
            }
        }
    }

    private static void deleteDog(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n--- Delete Dog ---");
        System.out.print("Enter dog ID to delete: ");
        int dogId = scanner.nextInt();

        String sql = "DELETE FROM Dogs WHERE DogID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dogId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Dog deleted successfully.");
            } else {
                System.out.println("Deletion failed. Dog not found.");
            }
        }
    }
}