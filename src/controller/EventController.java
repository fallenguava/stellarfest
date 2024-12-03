package controller;

import database.DatabaseConnection;
import model.Event;

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
}
