package hbv7d.repository;

import hbv7d.database.DBConnection;
import hbv7d.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    // Býr til user töflu.
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "name TEXT NOT NULL, "
                   + "email TEXT NOT NULL"
                   + ");";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("User table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Vistar User.
    public void save(User user) {
        String sql = "INSERT INTO User (name, email) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Finnur user m.v. id.
    public User findById(int userId) {
        User user = null;
        String sql = "SELECT id, name, email FROM User WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    // Eyðir user m.v. id
    public void delete(int userId) {
        String sql = "DELETE FROM User WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
