package hbv7d.repository;

import hbv7d.database.DBConnection;
import hbv7d.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            pstmt.setInt(1, booking.getTourId());
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



    
    // Breytir bókun í CONFIRMED m.v. aðstæður
    public boolean confirmBooking(int bookingID) {
        try (Connection conn = DBConnection.getConnection()) {
            // Sækir tourID og userID
            String selectSql = "SELECT tourID, userID FROM Booking WHERE bookingID = ?";
            int tourID, userID;
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setInt(1, bookingID);
                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        tourID = rs.getInt("tourID");
                        userID = rs.getInt("userID");
                    } else {
                        // Bókun ekki til
                        return false;
                    }
                }
            }
            
            // Sér hvort notandi sé núþegar með bókun
            String duplicateSql = "SELECT COUNT(*) AS count FROM Booking " +
                                  "WHERE tourID = ? AND userID = ? AND status = 'CONFIRMED' AND bookingID != ?";
            try (PreparedStatement dupPstmt = conn.prepareStatement(duplicateSql)) {
                dupPstmt.setInt(1, tourID);
                dupPstmt.setInt(2, userID);
                dupPstmt.setInt(3, bookingID);
                try (ResultSet rs = dupPstmt.executeQuery()) {
                    if (rs.next() && rs.getInt("count") > 0) {
                        System.out.println("User already has a confirmed booking for this tour.");
                        return false;
                    }
                }
            }
            
            // Ef það er ekki núþegar bókað þá er búið til bókunina
            String updateSql = "UPDATE Booking SET status = 'CONFIRMED' WHERE bookingID = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setInt(1, bookingID);
                int rowsAffected = updatePstmt.executeUpdate();
                return rowsAffected > 0;
            }
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

    //Skilar lista af öllum Bookings eftir UserId
    public List<Booking> findBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT bookingID, tourID, userID, status FROM Booking WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int bookingID = rs.getInt("bookingID");
                    int tourID = rs.getInt("tourID");
                    int userID = rs.getInt("userID");
                    String statusStr = rs.getString("status");
                    

                    Booking booking = new Booking(userID, tourID);
                    booking.setBookingID(bookingID);
                    

                    Booking.BookingStatus status = Booking.BookingStatus.valueOf(statusStr);
                    booking.setStatus(status);  
                    
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking findBookingsById(int bookingId) {
//        List<Booking> bookings = new ArrayList<>();
        Booking booking = null;
        String sql = "SELECT bookingID, tourID, userID, status FROM Booking WHERE bookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int bookingID = rs.getInt("bookingID");
                    int tourID = rs.getInt("tourID");
                    int userID = rs.getInt("userID");
                    String statusStr = rs.getString("status");


                    booking = new Booking(userID, tourID);
                    booking.setBookingID(bookingID);


                    Booking.BookingStatus status = Booking.BookingStatus.valueOf(statusStr);
                    booking.setStatus(status);

//                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }
}
