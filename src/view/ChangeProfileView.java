package view;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import utility.*;

public class ChangeProfileView extends Application {
    private UserController userController = new UserController();

    @Override
    public void start(Stage primaryStage) {
        User currentUser = SessionManager.getCurrentUser();

        Label lblEmail = new Label("New Email:");
        TextField txtEmail = new TextField();
        Label lblUsername = new Label("New Username:");
        TextField txtUsername = new TextField();
        Label lblOldPassword = new Label("Old Password:");
        PasswordField txtOldPassword = new PasswordField();
        Label lblNewPassword = new Label("New Password:");
        PasswordField txtNewPassword = new PasswordField();

        Button btnUpdate = new Button("Update Profile");

        btnUpdate.setOnAction(e -> {
            try {
                String email = txtEmail.getText().isEmpty() ? currentUser.getEmail() : txtEmail.getText();
                String username = txtUsername.getText().isEmpty() ? currentUser.getUsername() : txtUsername.getText();
                String oldPassword = txtOldPassword.getText();
                String newPassword = txtNewPassword.getText().isEmpty() ? null : txtNewPassword.getText();

                if (newPassword != null && newPassword.length() < 5) {
                    showAlert("Validation Error", "New password must be at least 5 characters long.");
                    return;
                }

                boolean success = userController.updateUserProfile(currentUser.getId(), email, username, oldPassword, newPassword);
                if (success) {
                    showAlert("Success", "Profile updated successfully!");
                    primaryStage.close();
                } else {
                    showAlert("Error", "Profile update failed. Check your inputs.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, lblEmail, txtEmail, lblUsername, txtUsername, lblOldPassword, txtOldPassword, lblNewPassword, txtNewPassword, btnUpdate);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setTitle("Change Profile");
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
