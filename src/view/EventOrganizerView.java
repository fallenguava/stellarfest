package view;

import controller.EventController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Event;
import utility.*;

import java.util.List;

public class EventOrganizerView extends Application {
    private EventController eventController = new EventController();

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Event Organizer Dashboard");

        Button btnViewEvents = new Button("View Organized Events");
        Button btnCreateEvent = new Button("Create Event");

        btnViewEvents.setOnAction(e -> {
            try {
                List<Event> events = eventController.getOrganizedEvents(SessionManager.getCurrentUser().getId());
                Stage eventsStage = new Stage();
                VBox eventList = new VBox(10);
                for (Event event : events) {
                    Label eventLabel = new Label(event.getName() + " (" + event.getDate() + ")");
                    Button detailsButton = new Button("Details");
                    detailsButton.setOnAction(d -> viewEventDetails(event));
                    eventList.getChildren().addAll(eventLabel, detailsButton);
                }
                Scene scene = new Scene(eventList, 400, 400);
                eventsStage.setTitle("Organized Events");
                eventsStage.setScene(scene);
                eventsStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnCreateEvent.setOnAction(e -> {
            Stage createEventStage = new Stage();
            Label lblEventName = new Label("Event Name:");
            TextField txtEventName = new TextField();
            Label lblEventDate = new Label("Event Date:");
            DatePicker dpEventDate = new DatePicker();
            Label lblEventLocation = new Label("Event Location:");
            TextField txtEventLocation = new TextField();
            Label lblEventDescription = new Label("Event Description:");
            TextField txtEventDescription = new TextField();
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

                    boolean success = eventController.createEvent(event);
                    if (success) {
                        showAlert("Success", "Event created successfully!");
                        createEventStage.close();
                    } else {
                        showAlert("Error", "Failed to create event.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            VBox createEventLayout = new VBox(10, lblEventName, txtEventName, lblEventDate, dpEventDate, lblEventLocation, txtEventLocation, lblEventDescription, txtEventDescription, btnSubmit);
            createEventLayout.setAlignment(Pos.CENTER);
            Scene createEventScene = new Scene(createEventLayout, 400, 400);
            createEventStage.setTitle("Create Event");
            createEventStage.setScene(createEventScene);
            createEventStage.show();
        });

        VBox vbox = new VBox(10, lblTitle, btnViewEvents, btnCreateEvent);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Event Organizer Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void viewEventDetails(Event event) {
        try {
            Stage detailsStage = new Stage();
            Label lblName = new Label("Event Name: " + event.getName());
            Label lblDate = new Label("Event Date: " + event.getDate());
            Label lblLocation = new Label("Event Location: " + event.getLocation());
            Label lblDescription = new Label("Description: " + event.getDescription());

            VBox detailsLayout = new VBox(10, lblName, lblDate, lblLocation, lblDescription);
            detailsLayout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(detailsLayout, 400, 300);
            detailsStage.setTitle("Event Details");
            detailsStage.setScene(scene);
            detailsStage.show();
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
