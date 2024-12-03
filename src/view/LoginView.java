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

public class LoginView extends Application {

    private UserController userController = new UserController();

    @Override
    public void start(Stage primaryStage) {
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Button btnLogin = new Button("Login");

        btnLogin.setOnAction(e -> {
            try {
                String email = txtEmail.getText();
                String password = txtPassword.getText();
                User user = userController.loginUser(email, password);

                if (user != null) {
                    SessionManager.setCurrentUser(user);
                    switch (user.getRole()) {
                        case "Admin":
                            new AdminView().start(new Stage());
                            break;
                        case "Event Organizer":
                            new EventOrganizerView().start(new Stage());
                            break;
                        case "Vendor":
                            new VendorView().start(new Stage());
                            break;
                        case "Guest":
                            new GuestView().start(new Stage());
                            break;
                    }
                    primaryStage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials!");
                    alert.show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, lblEmail, txtEmail, lblPassword, txtPassword, btnLogin);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
