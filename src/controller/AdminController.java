package controller;

import database.DatabaseConnection;
import model.Event;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
	
	public List<User> getAllUsers() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users";
        PreparedStatement stmt = conn.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            users.add(user);
        }

        conn.close();
        return users;
    }

    // Fetch all events
    public List<Event> getAllEvents() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM events";
        PreparedStatement stmt = conn.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event event = new Event();
            event.setId(rs.getInt("id"));
            event.setName(rs.getString("name"));
            event.setDate(rs.getString("date"));
            event.setLocation(rs.getString("location"));
            event.setDescription(rs.getString("description"));
            event.setOrganizerId(rs.getInt("organizer_id"));
            events.add(event);
        }

        conn.close();
        return events;
    }

    // Delete user by ID
    public boolean deleteUser(int userId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);

        int rowsAffected = stmt.executeUpdate();
        conn.close();
        return rowsAffected > 0;
    }

    // Delete event by ID
    public boolean deleteEvent(int eventId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "DELETE FROM events WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, eventId);

        int rowsAffected = stmt.executeUpdate();
        conn.close();
        return rowsAffected > 0;
    }

    public ArrayList<Event> viewAllEvents() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM events";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ArrayList<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event event = new Event();
            event.setId(rs.getInt("id"));
            event.setName(rs.getString("name"));
            event.setDate(rs.getString("date"));
            event.setLocation(rs.getString("location"));
            event.setDescription(rs.getString("description"));
            events.add(event);
        }
        conn.close();
        return events;
    }

    public ArrayList<User> viewAllUsers() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            users.add(user);
        }
        conn.close();
        return users;
    }
}
