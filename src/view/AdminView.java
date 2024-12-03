package view;

import java.sql.SQLException;

import controller.AdminController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import model.Event;

public class AdminView extends Application {
    private AdminController adminController = new AdminController();

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Admin Dashboard");

        Button btnViewUsers = new Button("View All Users");
        Button btnViewEvents = new Button("View All Events");

        // View All Users Button
        btnViewUsers.setOnAction(e -> {
            try {
                Stage userStage = new Stage();
                VBox userLayout = new VBox(10);
                userLayout.setAlignment(Pos.CENTER);

                var users = adminController.getAllUsers();

                if (users.isEmpty()) {
                    userLayout.getChildren().add(new Label("No users found."));
                } else {
                    for (User user : users) {
                        Label lblUser = new Label("User: " + user.getUsername() + " (" + user.getRole() + ")");
                        Button btnDelete = new Button("Delete");
                        btnDelete.setOnAction(ev -> deleteUser(user, userStage));
                        userLayout.getChildren().addAll(lblUser, btnDelete, new Separator());
                    }
                }

                Scene scene = new Scene(userLayout, 400, 400);
                userStage.setTitle("All Users");
                userStage.setScene(scene);
                userStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to load users.");
            }
        });

        // View All Events Button
        btnViewEvents.setOnAction(e -> {
            try {
                Stage eventStage = new Stage();
                VBox eventLayout = new VBox(10);
                eventLayout.setAlignment(Pos.CENTER);

                var events = adminController.getAllEvents();

                if (events.isEmpty()) {
                    eventLayout.getChildren().add(new Label("No events found."));
                } else {
                    for (Event event : events) {
                        Label lblEvent = new Label("Event: " + event.getName());
                        Button btnDelete = new Button("Delete");
                        btnDelete.setOnAction(ev -> deleteEvent(event, eventStage));
                        eventLayout.getChildren().addAll(lblEvent, btnDelete, new Separator());
                    }
                }

                Scene scene = new Scene(eventLayout, 400, 400);
                eventStage.setTitle("All Events");
                eventStage.setScene(scene);
                eventStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to load events.");
            }
        });

        VBox vbox = new VBox(10, lblTitle, btnViewUsers, btnViewEvents);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Delete User
    private void deleteUser(User user, Stage userStage) {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean success = true;
					try {
						success = adminController.deleteUser(user.getId());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if (success) {
                        showAlert("Success", "User deleted!");
                        userStage.close();
                        start(new Stage()); // Refresh the AdminView
                    } else {
                        showAlert("Error", "Failed to delete user.");
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
                    boolean success = true;
					try {
						success = adminController.deleteEvent(event.getId());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if (success) {
                        showAlert("Success", "Event deleted!");
                        eventStage.close();
                        start(new Stage()); // Refresh the AdminView
                    } else {
                        showAlert("Error", "Failed to delete event.");
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
