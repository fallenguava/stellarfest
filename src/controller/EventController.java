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

public class EventController {
	
	public List<Event> getOrganizedEvents(int organizerId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM events WHERE organizer_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, organizerId);

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

    public boolean createEvent(Event event) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO events (name, date, location, description, organizer_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, event.getName());
        stmt.setString(2, event.getDate());
        stmt.setString(3, event.getLocation());
        stmt.setString(4, event.getDescription());
        stmt.setInt(5, event.getOrganizerId());
        int rows = stmt.executeUpdate();
        conn.close();
        return rows > 0;
    }

    public boolean editEventName(int eventId, String newName) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "UPDATE events SET name = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, newName);
        stmt.setInt(2, eventId);
        int rows = stmt.executeUpdate();
        conn.close();
        return rows > 0;
    }
    
    public List<User> getAllGuests() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users WHERE role = 'Guest'";
        PreparedStatement stmt = conn.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<User> guests = new ArrayList<>();
        while (rs.next()) {
            User guest = new User();
            guest.setId(rs.getInt("id"));
            guest.setUsername(rs.getString("username"));
            guest.setEmail(rs.getString("email"));
            guest.setRole(rs.getString("role"));
            guests.add(guest);
        }
        conn.close();
        return guests;
    }

    public List<User> getAllVendors() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users WHERE role = 'Vendor'";
        PreparedStatement stmt = conn.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<User> vendors = new ArrayList<>();
        while (rs.next()) {
            User vendor = new User();
            vendor.setId(rs.getInt("id"));
            vendor.setUsername(rs.getString("username"));
            vendor.setEmail(rs.getString("email"));
            vendor.setRole(rs.getString("role"));
            vendors.add(vendor);
        }
        conn.close();
        return vendors;
    }
    
    public int getLastInsertedEventId() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT MAX(id) AS last_id FROM events";
        PreparedStatement stmt = conn.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        int lastId = 0;
        if (rs.next()) {
            lastId = rs.getInt("last_id");
        }
        conn.close();
        return lastId;
    }



}
