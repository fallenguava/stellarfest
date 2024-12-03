package controller;

import database.DatabaseConnection;
import model.Event;
import model.Invitation;
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
}
