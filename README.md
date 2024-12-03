# StellarFest

StellarFest is a Java-based application designed to bring the magic of celestial events to users through an interactive, user-friendly experience. This project, built using Java and Eclipse, showcases the beauty of astronomy and provides features such as event tracking, celestial body visualization, and educational resources.

---

## Features

- **Interactive Celestial Event Tracker**: Stay updated with upcoming celestial events like meteor showers, eclipses, and planet alignments.
- **Visualizations**: View stunning graphical representations of stars, planets, and constellations.
- **Educational Content**: Learn about the science behind celestial events, with detailed explanations and historical context.
- **Personalized Alerts**: Set reminders for events you don’t want to miss.
- **Cross-Platform Compatibility**: A Java-based solution that works on any system with a Java runtime environment.

---

## Installation

Follow these steps to set up StellarFest on your local machine:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/fallenguava/stellarfest.git
   ```
2. **Navigate to the Project Directory**:
   ```bash
   cd stellarfest
   ```
3. **Open in Eclipse**:
   - Open Eclipse.
   - Go to `File > Open Projects from File System`.
   - Select the `StellarFest` folder.
4. **Run the Project**:
   - Ensure you have a Java Runtime Environment (JRE) installed.
   - Right-click on the `Main.java` file and select `Run As > Java Application`.

---

## Usage

1. Launch the application to explore celestial events.
2. Browse the event tracker to view upcoming astronomical phenomena.
3. Use the search feature to find details about specific celestial bodies.
4. Set personalized alerts for events of interest.

---

## Project Structure

### **StellarFest Project Structure**

- **`src/`**  
  - **`controller/`**  
    - **`AdminController.java`**: Handles admin-specific operations such as managing users, events, and profile updates.  
    - **`EventController.java`**: Manages event-related operations like creating, updating, and fetching event details.  
    - **`InvitationController.java`**: Responsible for managing invitations, including sending, updating statuses, and retrieving invitation details.  
    - **`UserController.java`**: Handles user-related operations such as login, registration, and profile management.  

  - **`database/`**  
    - **`DatabaseConnection.java`**: Provides methods for establishing and managing the database connection.  

  - **`model/`**  
    - **`Event.java`**: Represents the structure and properties of an event entity in the system.  
    - **`Invitation.java`**: Defines the structure and properties of an invitation entity, linking events and users.  
    - **`User.java`**: Represents the structure and properties of a user entity.  

  - **`utility/`**  
    - **`SessionManager.java`**: Manages user sessions for tracking the currently logged-in user.  

  - **`view/`**  
    - **`AdminView.java`**: Provides the admin dashboard interface with features for managing users, events, and profile updates.  
    - **`EventOrganizerView.java`**: Offers an event organizer dashboard for managing events, inviting guests/vendors, and editing profiles.  
    - **`GuestView.java`**: Enables guests to view and accept invitations, as well as update their profiles.  
    - **`LoginView.java`**: Displays the login interface for users with different roles (admin, organizer, guest, vendor).  
    - **`VendorView.java`**: Provides a dashboard for vendors to manage event invitations and profile details.  

  - **`Main.java`**: The entry point for the application, initializing the user interface and managing navigation.  

- **`resources/`**  
  - **`styles/`**  
    - **`app.css`**: Contains CSS styling for the application’s user interface.  
  - **`images/`**  
    - **`logo.png`**: An image used for branding or visual elements.  

- **`database/`**  
  - **`schema.sql`**: SQL script to create the necessary tables (`users`, `events`, `invitations`) in the database.  
  - **`seed.sql`**: SQL script to populate the database with initial data for testing purposes.  

- **`documentation/`**  
  - **`ExternalDocumentation.docx`**: Documentation aimed at external stakeholders, detailing project goals and functionality.  
  - **`InternalDocumentation.docx`**: Technical documentation for developers, outlining system architecture and code structure.  

- **`tests/`**  
  - **`controller/`**  
    - Unit tests for all controllers (`AdminControllerTest.java`, `EventControllerTest.java`, etc.).  
  - **`model/`**  
    - Unit tests for models (`EventTest.java`, `InvitationTest.java`, `UserTest.java`).  
  - **`utility/`**  
    - Tests for session management (`SessionManagerTest.java`).  

- **`build/`**  
  - Directory for build artifacts, logs, and generated files.  

- **Project Root Files**  
  - **`README.md`**: Provides an overview of the project, installation instructions, and usage details.  
  - **`.gitignore`**: Lists files and directories to be excluded from version control.  
  - **`pom.xml / build.gradle`**: Configuration files for build automation tools like Maven or Gradle.  

---

## Requirements

- **Java**: Version 11 or higher.
- **Eclipse IDE** (recommended for development).
- **JavaFX**
- **MySQL Java Connector**

---

## Contributions

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make your changes and commit them:
   ```bash
   git commit -m "Add your message here"
   ```
4. Push the changes:
   ```bash
   git push origin feature/your-feature-name
   ```
5. Create a pull request on the original repository.

---

## License

This project is licensed under the [MIT License](LICENSE). Feel free to use, modify, and distribute this project as per the license terms.

---

## Acknowledgements

- Inspired by the wonders of the cosmos.
- Special thanks to the open-source community for tools and libraries.
- This is a Object Oriented Analysis and Design Assignment
- Group of 3
- Using Public open sources code and Generative AI for Error and problem misunderstanding.

---

## Contact

For questions, feedback, or collaboration opportunities, feel free to reach out:

- GitHub: [fallenguava](https://github.com/fallenguava)
- Email: winanda.hartadi@binus.ac.id
- Email: eugenia.hibau@binus.ac.id
- Email: francesco.christian@binus.ac.id
