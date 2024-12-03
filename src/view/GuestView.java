package view;

import java.util.List;

import controller.InvitationController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Event;
import utility.*;

public class GuestView extends Application {
    private InvitationController invitationController = new InvitationController();

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Guest Dashboard");

        Button btnViewInvitations = new Button("View Invitations");
        Button btnViewAcceptedEvents = new Button("View Accepted Events");

        btnViewInvitations.setOnAction(e -> {
            try {
                Stage invitationStage = new Stage();
                VBox invitationLayout = new VBox(10);
                var invitations = invitationController.getPendingInvitations(SessionManager.getCurrentUser().getId());

                for (var invitation : invitations) {
                    Label lblEvent = new Label("Event: " + invitation.getEventName());
                    Button btnAccept = new Button("Accept");
                    btnAccept.setOnAction(ev -> {
                        try {
                            boolean success = invitationController.acceptInvitation(invitation.getId());
                            if (success) {
                                showAlert("Success", "Invitation accepted!");
                                invitationStage.close();
                            } else {
                                showAlert("Error", "Failed to accept the invitation.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    invitationLayout.getChildren().addAll(lblEvent, btnAccept);
                }

                Scene scene = new Scene(invitationLayout, 400, 400);
                invitationStage.setTitle("Pending Invitations");
                invitationStage.setScene(scene);
                invitationStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnViewAcceptedEvents.setOnAction(e -> {
            try {
                Stage acceptedEventsStage = new Stage();
                VBox acceptedEventsLayout = new VBox(10);
                acceptedEventsLayout.setAlignment(Pos.CENTER);

                List<Event> acceptedEvents = invitationController.getAcceptedEvents(SessionManager.getCurrentUser().getId());

                if (acceptedEvents.isEmpty()) {
                    Label noEventsLabel = new Label("No accepted events.");
                    acceptedEventsLayout.getChildren().add(noEventsLabel);
                } else {
                    for (Event event : acceptedEvents) {
                        Label lblEvent = new Label("Event: " + event.getName() + " | Date: " + event.getDate());
                        Label lblLocation = new Label("Location: " + event.getLocation());
                        Label lblDescription = new Label("Description: " + event.getDescription());
                        acceptedEventsLayout.getChildren().addAll(lblEvent, lblLocation, lblDescription, new Separator());
                    }
                }

                Scene scene = new Scene(acceptedEventsLayout, 400, 400);
                acceptedEventsStage.setTitle("Accepted Events");
                acceptedEventsStage.setScene(scene);
                acceptedEventsStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to fetch accepted events.");
            }
        });

        VBox vbox = new VBox(10, lblTitle, btnViewInvitations, btnViewAcceptedEvents);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Guest Dashboard");
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
