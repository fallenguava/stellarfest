package view;

import controller.InvitationController;
import controller.UserController;
import javafx.application.Application;
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

public class VendorView extends Application {
    private InvitationController invitationController = new InvitationController();

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Vendor Dashboard");

        Button btnViewInvitations = new Button("View Invitations");
        Button btnViewAcceptedEvents = new Button("View Accepted Events");
        Button btnEditProfile = new Button("Edit Profile");
        Button btnLogout = new Button("Logout");

        btnViewInvitations.setOnAction(e -> viewPendingInvitations());
        btnViewAcceptedEvents.setOnAction(e -> viewAcceptedEvents());
        btnEditProfile.setOnAction(e -> editProfile());
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            try {
                new LoginView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, lblTitle, btnViewInvitations, btnViewAcceptedEvents, btnEditProfile, btnLogout);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Vendor Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void editProfile() {
        Stage editProfileStage = new Stage();
        VBox editLayout = new VBox(10);

        Label lblTitle = new Label("Edit Profile");

        User currentUser = SessionManager.getCurrentUser();

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(currentUser.getEmail());

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField(currentUser.getUsername());

        Label lblOldPassword = new Label("Old Password:");
        PasswordField txtOldPassword = new PasswordField();

        Label lblNewPassword = new Label("New Password:");
        PasswordField txtNewPassword = new PasswordField();

        Button btnUpdate = new Button("Update Profile");
        btnUpdate.setOnAction(e -> {
            try {
                String email = txtEmail.getText();
                String username = txtUsername.getText();
                String oldPassword = txtOldPassword.getText();
                String newPassword = txtNewPassword.getText();

                if (email.isEmpty() || username.isEmpty() || oldPassword.isEmpty()) {
                    showAlert("Validation Error", "Email, Username, and Old Password are required.");
                    return;
                }

                if (!newPassword.isEmpty() && newPassword.length() < 5) {
                    showAlert("Validation Error", "New Password must be at least 5 characters long.");
                    return;
                }

                UserController userController = new UserController();
                boolean updated = userController.updateUserProfile(
                    currentUser.getId(),
                    email,
                    username,
                    oldPassword,
                    newPassword.isEmpty() ? null : newPassword
                );

                if (updated) {
                    showAlert("Success", "Profile updated successfully!");
                    // Update session data
                    currentUser.setEmail(email);
                    currentUser.setUsername(username);
                    editProfileStage.close();
                } else {
                    showAlert("Error", "Failed to update profile. Check your inputs.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "An error occurred while updating profile.");
            }
        });

        editLayout.getChildren().addAll(
            lblTitle, lblEmail, txtEmail, lblUsername, txtUsername,
            lblOldPassword, txtOldPassword, lblNewPassword, txtNewPassword, btnUpdate
        );
        editLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(editLayout, 400, 400);
        editProfileStage.setTitle("Edit Profile");
        editProfileStage.setScene(scene);
        editProfileStage.show();
    }

    private void viewPendingInvitations() {
        Stage invitationStage = new Stage();
        try {
            List<Invitation> invitations = invitationController.getPendingInvitations(SessionManager.getCurrentUser().getId());
            ObservableList<Invitation> invitationList = FXCollections.observableArrayList(invitations);

            TableView<Invitation> tableView = createInvitationTableView(invitationList);
            if (invitationList.isEmpty()) {
                tableView.setPlaceholder(new Label("No pending invitations."));
            }

            VBox layout = new VBox(10, tableView);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, 600, 400);
            invitationStage.setTitle("Pending Invitations");
            invitationStage.setScene(scene);
            invitationStage.show();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load invitations.");
        }
    }

    private void viewAcceptedEvents() {
        Stage acceptedEventsStage = new Stage();
        try {
            List<Event> events = invitationController.getAcceptedEvents(SessionManager.getCurrentUser().getId());
            ObservableList<Event> eventList = FXCollections.observableArrayList(events);

            TableView<Event> tableView = createEventTableView(eventList);
            if (eventList.isEmpty()) {
                tableView.setPlaceholder(new Label("No accepted events."));
            }

            VBox layout = new VBox(10, tableView);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, 600, 400);
            acceptedEventsStage.setTitle("Accepted Events");
            acceptedEventsStage.setScene(scene);
            acceptedEventsStage.show();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load accepted events.");
        }
    }

    private TableView<Invitation> createInvitationTableView(ObservableList<Invitation> invitations) {
        TableView<Invitation> tableView = new TableView<>(invitations);

        TableColumn<Invitation, String> colEventName = new TableColumn<>("Event Name");
        colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<Invitation, Void> colAccept = new TableColumn<>("Actions");
        colAccept.setCellFactory(param -> new TableCell<>() {
            private final Button btnAccept = new Button("Accept");

            {
                btnAccept.setOnAction(e -> {
                    Invitation invitation = getTableView().getItems().get(getIndex());
                    try {
                        boolean success = invitationController.acceptInvitation(invitation.getId());
                        if (success) {
                            showAlert("Success", "Invitation accepted!");
                            tableView.getItems().remove(invitation);
                        } else {
                            showAlert("Error", "Failed to accept invitation.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Database error occurred.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAccept);
                }
            }
        });

        tableView.getColumns().addAll(colEventName, colAccept);
        return tableView;
    }

    private TableView<Event> createEventTableView(ObservableList<Event> events) {
        TableView<Event> tableView = new TableView<>(events);

        TableColumn<Event, String> colEventName = new TableColumn<>("Event Name");
        colEventName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Event, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Event, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        tableView.getColumns().addAll(colEventName, colDate, colLocation);
        return tableView;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
