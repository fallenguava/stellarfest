package view;

import java.sql.SQLException;
import java.util.List;

import controller.AdminController;
import controller.UserController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import utility.SessionManager;
import model.Event;

public class AdminView extends Application {
    private AdminController adminController = new AdminController();

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Admin Dashboard");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnViewUsers = new Button("View All Users");
        Button btnViewEvents = new Button("View All Events");
        Button btnEditProfile = new Button("Edit Profile");
        btnEditProfile.setOnAction(e -> editProfile());


        btnViewUsers.setOnAction(e -> showUsers(primaryStage));
        btnViewEvents.setOnAction(e -> showEvents(primaryStage));

        VBox vbox = new VBox(10, lblTitle, btnViewUsers, btnViewEvents, btnEditProfile);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(vbox, 500, 400);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void editProfile() {
        Stage editProfileStage = new Stage();

        // Labels and input fields
        Label lblEmail = new Label("New Email:");
        TextField txtEmail = new TextField();
        Label lblUsername = new Label("New Username:");
        TextField txtUsername = new TextField();
        Label lblOldPassword = new Label("Current Password:");
        PasswordField txtOldPassword = new PasswordField();
        Label lblNewPassword = new Label("New Password:");
        PasswordField txtNewPassword = new PasswordField();

        Button btnSave = new Button("Save Changes");
        btnSave.setOnAction(ev -> {
            String newEmail = txtEmail.getText().trim();
            String newUsername = txtUsername.getText().trim();
            String oldPassword = txtOldPassword.getText();
            String newPassword = txtNewPassword.getText();

            if (newEmail.isEmpty() && newUsername.isEmpty() && newPassword.isEmpty()) {
                showAlert("Validation Error", "At least one field must be updated.");
                return;
            }

            try {
                boolean success = new UserController().updateUserProfile(
                    SessionManager.getCurrentUser().getId(),
                    newEmail.isEmpty() ? SessionManager.getCurrentUser().getEmail() : newEmail,
                    newUsername.isEmpty() ? SessionManager.getCurrentUser().getUsername() : newUsername,
                    oldPassword,
                    newPassword.isEmpty() ? null : newPassword
                );

                if (success) {
                    showAlert("Success", "Profile updated successfully!");
                    editProfileStage.close();
                } else {
                    showAlert("Error", "Failed to update profile. Check your current password or ensure the new data is unique.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error occurred.");
            }
        });

        VBox layout = new VBox(10, lblEmail, txtEmail, lblUsername, txtUsername, lblOldPassword, txtOldPassword, lblNewPassword, txtNewPassword, btnSave);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400, 400);
        editProfileStage.setTitle("Edit Profile");
        editProfileStage.setScene(scene);
        editProfileStage.show();
    }


    // Display Users in a TableView
    private void showUsers(Stage primaryStage) {
        Stage userStage = new Stage();
        TableView<User> table = new TableView<>();
        table.setEditable(false);

        // Define columns
        TableColumn<User, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, Void> colAction = new TableColumn<>("Actions");
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Delete");

            {
                btnDelete.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user, userStage);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });

        // Add columns to the table
        table.getColumns().addAll(colId, colUsername, colEmail, colRole, colAction);

        // Populate table with data
        try {
            List<User> users = adminController.getAllUsers();
            ObservableList<User> userData = FXCollections.observableArrayList(users);
            table.setItems(userData);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load users.");
        }

        VBox vbox = new VBox(10, new Label("All Users"), table);
        vbox.setStyle("-fx-padding: 20px;");
        Scene scene = new Scene(vbox, 600, 400);

        userStage.setTitle("All Users");
        userStage.setScene(scene);
        userStage.show();
    }

    // Display Events in a TableView
    private void showEvents(Stage primaryStage) {
        Stage eventStage = new Stage();
        TableView<Event> table = new TableView<>();
        table.setEditable(false);

        // Define columns
        TableColumn<Event, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Event, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Event, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Event, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Actions Column
        TableColumn<Event, Void> colAction = new TableColumn<>("Actions");
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Delete");
            private final Button btnDetails = new Button("View Details");

            {
                btnDelete.setOnAction(e -> {
                    Event event = getTableView().getItems().get(getIndex());
                    deleteEvent(event, eventStage);
                });

                btnDetails.setOnAction(e -> {
                    Event event = getTableView().getItems().get(getIndex());
                    viewEventDetails(event); // Method to show event details
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionButtons = new HBox(5, btnDetails, btnDelete);
                    setGraphic(actionButtons);
                }
            }
        });

        // Add columns to the table
        table.getColumns().addAll(colId, colName, colDate, colLocation, colAction);

        // Populate table with data
        try {
            List<Event> events = adminController.getAllEvents();
            ObservableList<Event> eventData = FXCollections.observableArrayList(events);
            table.setItems(eventData);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load events.");
        }

        VBox vbox = new VBox(10, new Label("All Events"), table);
        vbox.setStyle("-fx-padding: 20px;");
        Scene scene = new Scene(vbox, 600, 400);

        
        eventStage.setTitle("All Events");
        eventStage.setScene(scene);
        eventStage.show();
    }
    
    private void viewEventDetails(Event event) {
        Stage detailsStage = new Stage();
        VBox detailsLayout = new VBox(10);

        try {
            Event eventDetails = adminController.getEventDetails(event.getId());

            if (eventDetails != null) {
                Label lblName = new Label("Event Name: " + eventDetails.getName());
                Label lblDate = new Label("Event Date: " + eventDetails.getDate());
                Label lblLocation = new Label("Event Location: " + eventDetails.getLocation());
                Label lblDescription = new Label("Description: " + eventDetails.getDescription());

                Button btnClose = new Button("Close");
                btnClose.setOnAction(e -> detailsStage.close());

                detailsLayout.getChildren().addAll(lblName, lblDate, lblLocation, lblDescription, btnClose);
            } else {
                showAlert("Error", "Event details not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load event details.");
        }

        detailsLayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(detailsLayout, 400, 300);
        detailsStage.setTitle("Event Details");
        detailsStage.setScene(scene);
        detailsStage.show();
    }


    // Delete User
    private void deleteUser(User user, Stage userStage) {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        boolean success = adminController.deleteUser(user.getId());
                        if (success) {
                            showAlert("Success", "User deleted!");
                            showUsers(userStage); // Refresh user view
                        } else {
                            showAlert("Error", "Failed to delete user.");
                        }
                    } catch (SQLException e) {
                        showAlert("Error", "Cannot delete user due to related data: " + e.getMessage());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Delete Event
    private void deleteEvent(Event event, Stage eventStage) {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        boolean success = adminController.deleteEvent(event.getId());
                        if (success) {
                            showAlert("Success", "Event deleted!");
                            showEvents(eventStage); // Refresh event view
                        } else {
                            showAlert("Error", "Failed to delete event.");
                        }
                    } catch (SQLException e) {
                        showAlert("Error", "Cannot delete event due to related data: " + e.getMessage());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
