package controller;

import database.DatabaseConnection;
import model.Event;
import model.Invitation;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvitationController {
	
	public List<Invitation> getPendingInvitations(int userId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT i.id, e.name AS event_name FROM invitations i " +
                       "JOIN events e ON i.event_id = e.id " +
                       "WHERE i.user_id = ? AND i.status = 'Pending'";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);

        ResultSet rs = stmt.executeQuery();
        List<Invitation> invitations = new ArrayList<>();
        while (rs.next()) {
            Invitation invitation = new Invitation();
            invitation.setId(rs.getInt("id"));
            invitation.setEventName(rs.getString("event_name"));
            invitations.add(invitation);
        }

        conn.close();
        return invitations;
    }
	
	public List<Invitation> getAcceptedInvitations(int userId) throws SQLException {
	    Connection conn = DatabaseConnection.getConnection();
	    String query = "SELECT i.id, e.name AS event_name, e.date AS event_date " +
	                   "FROM invitations i " +
	                   "JOIN events e ON i.event_id = e.id " +
	                   "WHERE i.user_id = ? AND i.status = 'Accepted'";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    stmt.setInt(1, userId);

	    ResultSet rs = stmt.executeQuery();
	    List<Invitation> invitations = new ArrayList<>();
	    while (rs.next()) {
	        Invitation invitation = new Invitation();
	        invitation.setId(rs.getInt("id"));
	        invitation.setEventName(rs.getString("event_name"));
	        invitation.setEventDate(rs.getString("event_date"));
	        invitations.add(invitation);
	    }

	    conn.close();
	    return invitations;
	}
	
	public List<Event> getAcceptedEvents(int userId) throws SQLException {
	    Connection conn = DatabaseConnection.getConnection();
	    String query = "SELECT e.id, e.name, e.date, e.location, e.description, e.organizer_id " +
	                   "FROM invitations i " +
	                   "JOIN events e ON i.event_id = e.id " +
	                   "WHERE i.user_id = ? AND i.status = 'Accepted'";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    stmt.setInt(1, userId);

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



    public boolean acceptInvitation(int invitationId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "UPDATE invitations SET status = 'Accepted' WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, invitationId);
        int rows = stmt.executeUpdate();
        conn.close();
        return rows > 0;
    }
    
    public boolean addGuestsToEvent(List<User> guests, int eventId) throws SQLException {
        if (guests == null || guests.isEmpty()) {
            throw new IllegalArgumentException("Guest list cannot be empty.");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO invitations (event_id, user_id, status) VALUES (?, ?, 'Pending')";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (User guest : guests) {
                if (guest == null || guest.getId() <= 0) {
                    throw new IllegalArgumentException("Invalid guest data provided.");
                }
                stmt.setInt(1, eventId);
                stmt.setInt(2, guest.getId());
                stmt.addBatch();
            }

            int[] rows = stmt.executeBatch();

            // Ensure all inserts were successful
            for (int row : rows) {
                if (row <= 0) return false;
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Database error while adding guests to event.");
        }
    }

    public boolean addVendorsToEvent(List<User> vendors, int eventId) throws SQLException {
        if (vendors == null || vendors.isEmpty()) {
            throw new IllegalArgumentException("Vendor list cannot be empty.");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO invitations (event_id, user_id, status) VALUES (?, ?, 'Pending')";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (User vendor : vendors) {
                if (vendor == null || vendor.getId() <= 0) {
                    throw new IllegalArgumentException("Invalid vendor data provided.");
                }
                stmt.setInt(1, eventId);
                stmt.setInt(2, vendor.getId());
                stmt.addBatch();
            }

            int[] rows = stmt.executeBatch();

            for (int row : rows) {
                if (row <= 0) return false;
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Database error while adding vendors to event.");
        }
    }
    
    public List<Invitation> getInvitedGuests(int eventId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT i.id, i.status, u.id AS user_id, u.username, u.email FROM invitations i " +
                       "JOIN users u ON i.user_id = u.id " +
                       "WHERE i.event_id = ? AND u.role = 'Guest'";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        List<Invitation> invitations = new ArrayList<>();
        while (rs.next()) {
            Invitation invitation = new Invitation();
            invitation.setId(rs.getInt("id"));
            invitation.setStatus(rs.getString("status"));

            User guest = new User();
            guest.setId(rs.getInt("user_id"));
            guest.setUsername(rs.getString("username"));
            guest.setEmail(rs.getString("email"));

            invitation.setUser(guest);
            invitations.add(invitation);
        }

        conn.close();
        return invitations;
    }

    public List<Invitation> getInvitedVendors(int eventId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT i.id, i.status, u.id AS user_id, u.username, u.email FROM invitations i " +
                       "JOIN users u ON i.user_id = u.id " +
                       "WHERE i.event_id = ? AND u.role = 'Vendor'";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        List<Invitation> invitations = new ArrayList<>();
        while (rs.next()) {
            Invitation invitation = new Invitation();
            invitation.setId(rs.getInt("id"));
            invitation.setStatus(rs.getString("status"));

            User vendor = new User();
            vendor.setId(rs.getInt("user_id"));
            vendor.setUsername(rs.getString("username"));
            vendor.setEmail(rs.getString("email"));

            invitation.setUser(vendor);
            invitations.add(invitation);
        }

        conn.close();
        return invitations;
    }

}
