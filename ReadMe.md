# Event Management Project

## Overview
The Event Management Project is a comprehensive application designed to facilitate the organization and management of events. It provides features for event creation, user registration, and participation tracking.

## Features
- **Event Creation and Management**: Users can create events with details such as name, description, date, time, location, and more.
- **User Registration**: Allows users to register for events.
- **Role-Based Access**: Supports roles such as `ADMIN`, `USER`, and `ORGANIZER`.
- **API Integration**: Fetch external JSON data via API for additional event information.
- **HATEOAS Links**: Provides HATEOAS links for user resources.

## Project Structure
The project is organized as follows:
- **Frontend**: 
  - `index.html`: A static HTML page for user interaction with event creation and registration functionalities.

- **Backend**:
  - `EventManagementApplication.java`: The main entry point of the Spring Boot application.
  - `EventDTO.java`: Data Transfer Object for event details.
  - `Role.java`: Enum for defining user roles.
  - `UserService.java`: Service for managing user-related operations.
  - `EventRegistrationService.java`: Service for managing event registrations.
  - `UserRepository.java` and `EventRepository.java`: Repositories for interacting with the database.

- **Configuration**:
  - `JacksonConfig.java`: Configuration for Jackson ObjectMapper to handle serialization and deserialization.

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/vijha742/Event-Management-Project.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Event-Management-Project
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Usage
- Access the application at `http://localhost:8080` after running the server.
- Use the form on the homepage to create events and register users.
- View and manage events using the table interface.

## Future Enhancements (TODOs)
1. **Enhanced Event Management**:
   - Add support for recurring events.
   - Implement notifications and reminders for participants.
2. **Analytics Dashboard**:
   - Provide metrics like the number of attendees, popular events, etc.
3. **Improved UI/UX**:
   - Enhance the user interface with modern design frameworks like Bootstrap or Material-UI.
   - Make the application mobile-responsive.
4. **Integration with External Services**:
   - Integrate with calendar services (e.g., Google Calendar) for event reminders.
   - Enable payment gateway support for paid events.
5. **Testing and CI/CD**:
   - Add unit and integration tests for critical features.
   - Implement Continuous Integration/Continuous Deployment (CI/CD) pipelines.

## Contribution
We welcome contributions to enhance the Event Management Project! Here's how you can contribute:
1. Fork the repository and create your branch:
   ```bash
   git checkout -b feature/YourFeatureName
   ```
2. Commit your changes:
   ```bash
   git commit -m "Add your message here"
   ```
3. Push your branch to your fork:
   ```bash
   git push origin feature/YourFeatureName
   ```
4. Create a pull request to the `main` branch of this repository.

Feel free to open issues for feature requests or bug reports.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
