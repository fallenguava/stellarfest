package controller;

import database.DatabaseConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public boolean registerUser(User user) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO users (email, username, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, user.getEmail());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getRole());
        int rows = stmt.executeUpdate();
        conn.close();
        return rows > 0;
    }

    public User loginUser(String email, String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            return user;
        }
        conn.close();
        return null;
    }
    
    public boolean updateUserProfile(int userId, String email, String username, String oldPassword, String newPassword) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        String validateQuery = "SELECT * FROM users WHERE id = ? AND password = ?";
        PreparedStatement validateStmt = conn.prepareStatement(validateQuery);
        validateStmt.setInt(1, userId);
        validateStmt.setString(2, oldPassword);
        ResultSet rs = validateStmt.executeQuery();
        if (!rs.next()) {
            conn.close();
            return false; 
        }

        if (!email.equals(rs.getString("email"))) {
            String emailQuery = "SELECT * FROM users WHERE email = ?";
            PreparedStatement emailStmt = conn.prepareStatement(emailQuery);
            emailStmt.setString(1, email);
            ResultSet emailRs = emailStmt.executeQuery();
            if (emailRs.next()) {
                conn.close();
                return false; 
            }
        }

        if (!username.equals(rs.getString("username"))) {
            String usernameQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement usernameStmt = conn.prepareStatement(usernameQuery);
            usernameStmt.setString(1, username);
            ResultSet usernameRs = usernameStmt.executeQuery();
            if (usernameRs.next()) {
                conn.close();
                return false;
            }
        }

        String updateQuery = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setString(1, email);
        updateStmt.setString(2, username);
        updateStmt.setString(3, newPassword != null ? newPassword : oldPassword); // Keep the old password if new is null
        updateStmt.setInt(4, userId);

        int rowsAffected = updateStmt.executeUpdate();
        conn.close();
        return rowsAffected > 0;
    }
}
