package view;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class RegistrationView extends Application {
    private UserController userController = new UserController();

    @Override
    public void start(Stage primaryStage) {
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Label lblRole = new Label("Role:");
        ComboBox<String> cbRole = new ComboBox<>();
        cbRole.getItems().addAll("Admin", "Event Organizer", "Vendor", "Guest"); // Added Admin role

        Button btnRegister = new Button("Register");

        btnRegister.setOnAction(e -> {
            try {
                String email = txtEmail.getText();
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                String role = cbRole.getValue();

                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                    showAlert("Validation Error", "All fields must be filled.");
                    return;
                }
                if (password.length() < 5) {
                    showAlert("Validation Error", "Password must be at least 5 characters long.");
                    return;
                }

                User user = new User();
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(password);
                user.setRole(role);

                boolean success = userController.registerUser(user);
                if (success) {
                    showAlert("Success", "Registration successful!");
                    primaryStage.close();
                    new LoginView().start(new Stage());
                } else {
                    showAlert("Error", "Registration failed. Email or username might be taken.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, lblEmail, txtEmail, lblUsername, txtUsername, lblPassword, txtPassword, lblRole, cbRole, btnRegister);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setTitle("Register");
        primaryStage.setScene(scene);
        primaryStage.show();
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
