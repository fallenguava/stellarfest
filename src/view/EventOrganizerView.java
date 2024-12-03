package view;

import controller.EventController;
import controller.InvitationController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Event;
import model.Invitation;
import model.User;
import utility.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class EventOrganizerView extends Application {
    private EventController eventController = new EventController();
    private InvitationController invitationController = new InvitationController();

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Event Organizer Dashboard");

        Button btnViewEvents = new Button("View Organized Events");
        Button btnCreateEvent = new Button("Create Event");
        Button btnEditProfile = new Button("Edit Profile");
        Button btnLogout = new Button("Logout");

        btnViewEvents.setOnAction(e -> viewOrganizedEvents());
        btnCreateEvent.setOnAction(e -> createEvent());
        btnEditProfile.setOnAction(e -> editProfile());
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            try {
                new LoginView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, lblTitle, btnViewEvents, btnCreateEvent, btnEditProfile, btnLogout);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Event Organizer Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void viewEventDetails(Event event) {
        Stage detailsStage = new Stage();
        VBox detailsLayout = new VBox(10);
        try {
            Label lblName = new Label("Event Name: " + event.getName());
            Label lblDate = new Label("Event Date: " + event.getDate());
            Label lblLocation = new Label("Event Location: " + event.getLocation());
            Label lblDescription = new Label("Description: " + event.getDescription());

            // Fetch and display invited guests in a table
            Label lblGuests = new Label("Invited Guests:");
            TableView<Invitation> guestsTable = new TableView<>();
            TableColumn<Invitation, String> guestNameColumn = new TableColumn<>("Guest Name");
            guestNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getUsername()));
            TableColumn<Invitation, String> guestEmailColumn = new TableColumn<>("Email");
            guestEmailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getEmail()));
            TableColumn<Invitation, String> guestStatusColumn = new TableColumn<>("Status");
            guestStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
            guestsTable.getColumns().addAll(guestNameColumn, guestEmailColumn, guestStatusColumn);

            ObservableList<Invitation> guestInvitations = FXCollections.observableArrayList(invitationController.getInvitedGuests(event.getId()));
            guestsTable.setItems(guestInvitations);

            // Fetch and display invited vendors in a table
            Label lblVendors = new Label("Invited Vendors:");
            TableView<Invitation> vendorsTable = new TableView<>();
            TableColumn<Invitation, String> vendorNameColumn = new TableColumn<>("Vendor Name");
            vendorNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getUsername()));
            TableColumn<Invitation, String> vendorEmailColumn = new TableColumn<>("Email");
            vendorEmailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getEmail()));
            TableColumn<Invitation, String> vendorStatusColumn = new TableColumn<>("Status");
            vendorStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
            vendorsTable.getColumns().addAll(vendorNameColumn, vendorEmailColumn, vendorStatusColumn);

            ObservableList<Invitation> vendorInvitations = FXCollections.observableArrayList(invitationController.getInvitedVendors(event.getId()));
            vendorsTable.setItems(vendorInvitations);

            Button btnEditName = new Button("Edit Event Name");
            btnEditName.setOnAction(ev -> {
                TextInputDialog inputDialog = new TextInputDialog(event.getName());
                inputDialog.setTitle("Edit Event Name");
                inputDialog.setHeaderText("Edit Event Name");
                inputDialog.setContentText("New Name:");

                inputDialog.showAndWait().ifPresent(newName -> {
                    try {
                        if (newName.isEmpty()) {
                            showAlert("Validation Error", "Event name cannot be empty.");
                            return;
                        }
                        boolean success = eventController.editEventName(event.getId(), newName);
                        if (success) {
                            showAlert("Success", "Event name updated!");
                            detailsStage.close();
                        } else {
                            showAlert("Error", "Failed to update event name.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Database error occurred.");
                    }
                });
            });

            detailsLayout.getChildren().addAll(lblName, lblDate, lblLocation, lblDescription, lblGuests,
                    guestsTable, lblVendors, vendorsTable, btnEditName);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load event details.");
        }

        Scene scene = new Scene(detailsLayout, 600, 600);
        detailsStage.setTitle("Event Details");
        detailsStage.setScene(scene);
        detailsStage.show();
    }

    private void createEvent() {
        Stage createEventStage = new Stage();
        Label lblEventName = new Label("Event Name:");
        TextField txtEventName = new TextField();
        Label lblEventDate = new Label("Event Date:");
        DatePicker dpEventDate = new DatePicker();
        Label lblEventLocation = new Label("Event Location:");
        TextField txtEventLocation = new TextField();
        Label lblEventDescription = new Label("Event Description:");
        TextField txtEventDescription = new TextField();

        // Add Guests
        Label lblAddGuests = new Label("Add Guests:");
        ObservableList<User> guestList = FXCollections.observableArrayList();
        try {
            List<User> allGuests = eventController.getAllGuests(); // No arguments needed
            guestList.addAll(allGuests);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TableView<User> guestTableView = createUserTableView(guestList);

        // Add Vendors
        Label lblAddVendors = new Label("Add Vendors:");
        ObservableList<User> vendorList = FXCollections.observableArrayList();
        try {
            List<User> allVendors = eventController.getAllVendors(); // No arguments needed
            vendorList.addAll(allVendors);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TableView<User> vendorTableView = createUserTableView(vendorList);

        Button btnSubmit = new Button("Create Event");
        btnSubmit.setOnAction(ev -> {
            try {
                String name = txtEventName.getText();
                String date = dpEventDate.getValue().toString();
                String location = txtEventLocation.getText();
                String description = txtEventDescription.getText();

                if (name.isEmpty() || date.isEmpty() || location.isEmpty() || description.isEmpty()) {
                    showAlert("Validation Error", "All fields are required.");
                    return;
                }
                if (location.length() < 5) {
                    showAlert("Validation Error", "Location must be at least 5 characters long.");
                    return;
                }
                if (description.length() > 200) {
                    showAlert("Validation Error", "Description cannot exceed 200 characters.");
                    return;
                }

                Event event = new Event();
                event.setName(name);
                event.setDate(date);
                event.setLocation(location);
                event.setDescription(description);
                event.setOrganizerId(SessionManager.getCurrentUser().getId());

                boolean eventCreated = eventController.createEvent(event);

                if (eventCreated) {
                    int newEventId = eventController.getLastInsertedEventId(); // Fetch the ID of the created event
                    List<User> selectedGuests = guestTableView.getSelectionModel().getSelectedItems();
                    List<User> selectedVendors = vendorTableView.getSelectionModel().getSelectedItems();

                    invitationController.addGuestsToEvent(selectedGuests, newEventId);
                    invitationController.addVendorsToEvent(selectedVendors, newEventId);

                    showAlert("Success", "Event created successfully!");
                    createEventStage.close();
                } else {
                    showAlert("Error", "Failed to create event.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error occurred.");
            }
        });

        VBox createEventLayout = new VBox(10, lblEventName, txtEventName, lblEventDate, dpEventDate,
                lblEventLocation, txtEventLocation, lblEventDescription, txtEventDescription,
                lblAddGuests, guestTableView, lblAddVendors, vendorTableView, btnSubmit);
        createEventLayout.setAlignment(Pos.CENTER);

        Scene createEventScene = new Scene(createEventLayout, 600, 800);
        createEventStage.setTitle("Create Event");
        createEventStage.setScene(createEventScene);
        createEventStage.show();
    }

    private TableView<User> createUserTableView(ObservableList<User> userList) {
        TableView<User> tableView = new TableView<>(userList);

        TableColumn<User, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableView.getColumns().addAll(colUsername, colEmail);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        return tableView;
    }

    private void editProfile() {
        Stage editProfileStage = new Stage();
        VBox layout = new VBox(10);

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField(SessionManager.getCurrentUser().getUsername());

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(SessionManager.getCurrentUser().getEmail());

        Button btnSubmit = new Button("Save Changes");

        btnSubmit.setOnAction(e -> {
            try {
                String newUsername = txtUsername.getText();
                String newEmail = txtEmail.getText();

                if (newUsername.isEmpty() || newEmail.isEmpty()) {
                    showAlert("Validation Error", "All fields are required.");
                    return;
                }

                // Update the user in the database (not implemented, assume a method updateUser exists)
                boolean success = true; // Replace with the actual call to the controller

                if (success) {
                    showAlert("Success", "Profile updated successfully!");
                    editProfileStage.close();
                } else {
                    showAlert("Error", "Failed to update profile.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "An error occurred while updating the profile.");
            }
        });

        layout.getChildren().addAll(lblUsername, txtUsername, lblEmail, txtEmail, btnSubmit);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        editProfileStage.setScene(scene);
        editProfileStage.setTitle("Edit Profile");
        editProfileStage.show();
    }

    private void viewOrganizedEvents() {
        Stage eventsStage = new Stage();
        VBox eventList = new VBox(10);
        try {
            List<Event> events = eventController.getOrganizedEvents(SessionManager.getCurrentUser().getId());

            if (events.isEmpty()) {
                eventList.getChildren().add(new Label("No events organized yet."));
            } else {
                TableView<Event> tableView = new TableView<>(FXCollections.observableArrayList(events));

                TableColumn<Event, String> colName = new TableColumn<>("Event Name");
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));

                TableColumn<Event, String> colDate = new TableColumn<>("Date");
                colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

                TableColumn<Event, String> colLocation = new TableColumn<>("Location");
                colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

                tableView.getColumns().addAll(colName, colDate, colLocation);

                Button btnDetails = new Button("View Details");
                btnDetails.setOnAction(e -> {
                    Event selectedEvent = tableView.getSelectionModel().getSelectedItem();
                    if (selectedEvent != null) {
                        viewEventDetails(selectedEvent);
                    } else {
                        showAlert("Error", "Please select an event to view details.");
                    }
                });

                eventList.getChildren().addAll(tableView, btnDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load events.");
        }

        Scene scene = new Scene(eventList, 600, 400);
        eventsStage.setTitle("Organized Events");
        eventsStage.setScene(scene);
        eventsStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
