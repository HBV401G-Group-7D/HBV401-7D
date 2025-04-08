package hbv7d.repository;

import hbv7d.database.DBConnection;
import hbv7d.model.Booking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingRepository {

    // Býr til booking töflu ef ekki núþegar til
    public void createBookingTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Booking ("
                   + "bookingID INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "tourID INTEGER, "
                   + "userID INTEGER, "
                   + "status TEXT NOT NULL, "
                   + "FOREIGN KEY (tourID) REFERENCES Tour(tourID), "
                   + "FOREIGN KEY (userID) REFERENCES User(id)"
                   + ");";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Booking table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Býr til nýtt booking
    // Skilar id á booking ef það tekst, annars -1
    public int createBooking(Booking booking) {
        int bookingId = -1;
        String sql = "INSERT INTO Booking (tourID, userID, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, booking.getTour());
            pstmt.setInt(2, booking.getUserId());
            pstmt.setString(3, booking.getStatus().toString());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        bookingId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingId;
    }
    
    // Breytir bókun í CONFIRMED.
    public boolean confirmBooking(int bookingID) {
        String sql = "UPDATE Booking SET status = 'CONFIRMED' WHERE bookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Breytir bókun í CANCELLED.
    public boolean cancelBooking(int bookingID) {
        String sql = "UPDATE Booking SET status = 'CANCELLED' WHERE bookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Merkir bókun sem failed og kallar þá á cancelbooking til að merkja það sem cancelled
    public boolean bookingFailed(int bookingID) {
        return cancelBooking(bookingID);
    }
}
