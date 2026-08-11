# Event Management System - Architecture & Design Documentation

## Overview

This document provides comprehensive design documentation for the Event Management System including HLD (High-Level Design), LLD (Low-Level Design), and UML diagrams.

---

## 1. High-Level Design (HLD)

**File:** `HLD_Diagram.puml`

### Purpose
The HLD diagram shows the system architecture at a macro level, illustrating how different components interact and their relationships.

### Key Components

#### 1. **Client Layer**
- Web Client, Mobile Client, Third-Party Applications
- Various endpoints for different user types

#### 2. **API Gateway & Security Layer**
- **REST API Gateway**: Entry point for all API requests
- **OAuth2 Authentication**: Handles third-party authentication (Google)
- **JWT Token Service**: Manages JWT tokens for session management
- **CORS Handler**: Handles cross-origin requests

#### 3. **Application Layer (Controllers)**
- **Event Controller**: Manages event CRUD operations
- **User Controller**: Manages user profiles and information
- **Session Controller**: Handles session validation and refresh
- **Registration Controller**: Manages event registrations

#### 4. **Business Logic Layer (Services)**
- **Event Service**: Event-related business logic
- **User Service**: User-related business logic
- **Registration Service**: Registration workflow and logic
- **Session Validation Service**: Session and token validation

#### 5. **Data Persistence Layer (Repositories)**
- **Event Repository**: Event data access
- **User Repository**: User data access
- **Registration Repository**: Registration data access

#### 6. **Database Layer**
- **PostgreSQL Database**
  - events table (Event data with JSONB support)
  - users table (User information and roles)
  - event_registrations table (Registration records)

#### 7. **External Services**
- Google OAuth2 for authentication
- Email Service for notifications
- File Storage for banners, logos, etc.

#### 8. **Cache Layer**
- Redis Cache for frequently accessed data

#### 9. **Utilities & Middleware**
- Exception Handling
- Logging Service
- Jackson Configuration for JSON serialization

### Data Flow
1. Client sends request → API Gateway → Security/CORS Handler
2. Request routed to appropriate Controller
3. Controller calls Service layer
4. Service calls Repository for data access
5. Repository queries Database
6. Response flows back through layers with HATEOAS links added

---

## 2. Low-Level Design (LLD)

**File:** `LLD_Diagram.puml`

### Purpose
The LLD diagram provides detailed class-level design showing methods, attributes, and specific interactions between components.

### Controller Layer

#### **EventController**
```java
Methods:
- getAllEvents()           : Get all events
- getEventById(id)        : Get event by ID
- createEvent(dto, auth)  : Create new event
- updateEvent(id, dto)    : Update event details
- deleteEvent(id, auth)   : Delete event
- searchEvents(query)     : Search events by query
```

#### **UserController**
```java
Methods:
- getUserProfile(id)      : Get user details
- getAllUsers()           : Get all users
- createUser(dto)         : Create new user
- updateUser(id, dto)     : Update user info
- deleteUser(id)          : Delete user
```

#### **EventRegistrationController**
```java
Methods:
- registerForEvent(dto)         : Register user for event
- getUserRegistrations(userId)  : Get registrations of a user
- getEventRegistrations(eventId): Get registrations for an event
- cancelRegistration(id)        : Cancel a registration
```

#### **SessionController**
```java
Methods:
- validateSession(request)  : Validate user session
- refreshSession(request)   : Refresh JWT token
```

### Service Layer

#### **EventService**
```java
Key Methods:
- getAllEvents()          : Retrieve all events
- getEventById(id)        : Retrieve event by ID
- createEvent(dto, user)  : Create with validation
- updateEvent()           : Update event details
- deleteEvent()           : Delete event
- searchEvents()          : Full-text search
- getEventsByUser()       : Get events created by user
- Caching: Redis integration for performance
```

#### **UserService**
```java
Key Methods:
- getUserById(id)         : Get user by ID
- getAllUsers()           : Get all users
- createUser(dto)         : Create new user
- updateUser()            : Update user profile
- deleteUser()            : Remove user
- getUserByEmail()        : Find by email
```

#### **EventRegistrationService**
```java
Key Methods:
- registerForEvent(dto)           : Register user
- cancelRegistration(id)          : Cancel registration
- getUserRegistrations(userId)    : Get user's registrations
- getEventRegistrations(eventId)  : Get event's registrations
- checkRegistrationStatus()       : Check status
- getRegistrationCount()          : Count registrations
- Email notifications on registration
```

#### **SessionValidationService**
```java
Key Methods:
- validateUserSession(token)  : Validate JWT token
- refreshToken(token)         : Generate new token
- revokeToken(token)          : Invalidate token
- getUserFromToken(token)     : Extract user from token
```

### DTO Layer
- **EventDTO**: Core event data transfer
- **EventResponseDTO**: Response format with relationships
- **UserResponseDTO**: User data in response format
- **CreateUserRequestDTO**: User creation request
- **EventRegistrationDTO**: Registration data transfer

### Entity/Model Layer

#### **Event Entity**
```
Attributes:
- id (Long): Primary key
- name (String): Event name
- description (Text): Detailed description
- date (LocalDateTime): Event date/time
- location (String): Event location
- capacity (Integer): Max participants
- banner (Text): Banner image URL
- status (Enum): UPCOMING, ONGOING, COMPLETED, CANCELLED
- additionalFields (Map<String, Object>): JSONB for flexibility
- createdBy (User FK): Event organizer
- createdAt, updatedAt: Timestamps
```

#### **User Entity**
```
Attributes:
- id (Long): Primary key
- email (String): Unique email
- name (String): User name
- role (Enum): ADMIN, USER, ORGANIZER
- oauthProvider (String): OAuth provider name
- profilePicture (Text): Profile image URL
- createdAt, updatedAt: Timestamps
```

#### **EventRegistration Entity**
```
Attributes:
- id (Long): Primary key
- event (Event FK): Reference to event
- user (User FK): Reference to user
- status (Enum): REGISTERED, WAITING_LIST, CANCELLED, APPROVED
- role (String): User's role in event
- registeredAt (Timestamp): Registration time
- cancelledAt (Timestamp): Cancellation time
```

### Repository Interfaces

#### **EventRepository**
- JPA-based data access
- Custom queries for filtering by user, status, date
- Pagination and sorting support

#### **UserRepository**
- Find by email, ID
- Role-based queries
- User management operations

#### **EventRegistrationRepository**
- Find by event and user combination
- List registrations by event or user
- Status-based filtering
- Active registrations query

### Security & Configuration

#### **SecurityConfig**
- Spring Security configuration
- OAuth2 setup
- JWT integration
- CORS configuration

#### **JwtTokenProvider**
- Token generation
- Token validation
- User extraction from token
- Expiration handling

#### **CustomOAuth2UserService**
- OAuth2 user loading
- User creation/update on first login
- Profile picture and provider mapping

### Exception Handling

#### **EventNotFoundException**
- Custom exception for missing events
- Thrown when event ID not found

#### **EventNotFoundAdvice**
- Global exception handler
- Returns standardized error responses
- HTTP status mapping

---

## 3. UML Class Diagram

**File:** `UML_Class_Diagram.puml`

### Core Entity Classes

#### **Event**
```
Attributes: 
  id, name, description, date, time, location, capacity,
  banner, logo, badge, status, additionalFields, createdBy,
  createdAt, updatedAt

Methods:
  + getId(): Long
  + getName(): String
  + getStatus(): EventStatus
  + setStatus(EventStatus): void
  + getCreatedBy(): User
```

#### **User**
```
Attributes:
  id, email, name, role, oauthProvider, profilePicture,
  createdAt, updatedAt

Methods:
  + getId(): Long
  + getEmail(): String
  + getRole(): UserRole
  + setRole(UserRole): void
  + isAdmin(): boolean
```

#### **EventRegistration**
```
Attributes:
  id, event, user, status, role, registeredAt, cancelledAt

Methods:
  + getId(): Long
  + getEvent(): Event
  + getUser(): User
  + getStatus(): RegistrationStatus
  + cancel(): void
```

### Enumerations

#### **EventStatus**
- UPCOMING
- ONGOING
- COMPLETED
- CANCELLED

#### **UserRole**
- ADMIN
- USER
- ORGANIZER

#### **RegistrationStatus**
- REGISTERED
- CANCELLED
- WAITING_LIST
- APPROVED

### Relationships

#### One-to-Many Relationships
- User → Events (created by user)
- Event → EventRegistrations (has registrations)
- User → EventRegistrations (participates in events)

#### Cardinalities
```
Event "1" -- "*" EventRegistration : has
User "1" -- "*" EventRegistration : has
User "1" -- "*" Event : creates
```

---

## 4. Sequence Diagrams

### 4.1 OAuth2 Authentication Flow

**File:** `Sequence_OAuth2_Authentication.puml`

```
Flow:
1. User clicks "Login with Google"
2. Browser redirects to /oauth2/authorization/google
3. SecurityConfig sends authorization request to Google
4. User logs in to Google
5. Google returns authorization code
6. Backend exchanges code for access token
7. CustomOAuth2UserService loads user details
8. System checks if user exists in database
9. If new user: create user record
10. If existing user: update last login
11. JWT token generated
12. Redirect to dashboard with token
```

**Key Participants:**
- User/Browser
- EventManagementApp (Backend)
- Google OAuth2 Service
- JWT Token Provider
- User Repository
- PostgreSQL

### 4.2 Event Creation Flow

**File:** `Sequence_Event_Creation.puml`

```
Flow:
1. User sends POST /api/events request
2. EventController extracts user from OAuth token
3. Verify user exists in database
4. EventService validates event data
5. If validation fails: return 400 Bad Request
6. If valid: insert event into database
7. Cache event for quick access
8. Return 201 Created with HATEOAS links
```

**Validations:**
- User must be authenticated
- Event name required
- Event date must be valid
- Location required
- Capacity must be positive

### 4.3 Event Registration Flow

**File:** `Sequence_Event_Registration.puml`

```
Flow:
1. User sends registration request
2. EventRegistrationService validates input
3. Verify event exists
4. Verify user exists
5. Check for duplicate registration
6. If already registered: return 400
7. Check event capacity
8. If full: add to WAITING_LIST
9. If available: set status to REGISTERED
10. Insert registration record
11. Send confirmation email
12. Return 201 Created with registration details
```

**Business Logic:**
- Prevents duplicate registrations
- Automatically adds to waiting list when event is full
- Sends email notifications
- Returns HATEOAS links for related resources

---

## 5. Component Diagram

**File:** `Component_Diagram.puml`

### Layered Architecture

#### **Presentation Layer**
- Web Client
- Mobile Client
- Admin Portal

#### **API & Gateway Layer**
- REST API endpoints
- API Gateway (routing, rate limiting)
- CORS Handler (cross-origin requests)

#### **Security Layer**
- OAuth2 Handler
- JWT Provider
- Security Filter Chain
- Custom OAuth2 Service

#### **Controller Layer**
- Event Controller
- User Controller
- Registration Controller
- Session Controller

#### **Service Layer**
- Event Service
- User Service
- Registration Service
- Session Validation Service

#### **Data Access Layer**
- Event Repository
- User Repository
- Registration Repository
- JPA Base Repository

#### **Caching Layer**
- Cache Manager
- Redis Client

#### **External Services**
- Google OAuth2
- Email Service
- File Storage Service

#### **Data Persistence**
- PostgreSQL Database
  - Event Table
  - User Table
  - Registration Table

#### **Configuration & Utilities**
- Jackson Config (JSON serialization)
- Security Config
- Environment Config
- Exception Advice
- Exception Handler
- Logger
- Validator
- DTO Mapper

### Component Interactions
```
Clients → REST API → API Gateway → Controller
Controller → Service → Repository → Database
Service ← Cache Manager
Service → External Services
Exception Handlers ← All Layers
Utilities used by all layers
```

---

## 6. Database/ER Diagram

**File:** `Database_ER_Diagram.puml`

### Schema Overview

#### **users Table**
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  name VARCHAR(255) NOT NULL,
  oauth_provider VARCHAR(100),
  profile_picture TEXT,
  role VARCHAR(50) DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**Indexes:**
- `email` (UNIQUE)
- `oauth_provider`
- `role`

#### **events Table**
```sql
CREATE TABLE events (
  id BIGINT PRIMARY KEY,
  name VARCHAR(500) NOT NULL,
  description TEXT,
  date TIMESTAMP NOT NULL,
  time TIME,
  location VARCHAR(500),
  capacity INTEGER,
  banner TEXT,
  logo TEXT,
  badge TEXT,
  status VARCHAR(50) DEFAULT 'UPCOMING',
  additional_fields JSONB,
  created_by BIGINT NOT NULL REFERENCES users(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**Indexes:**
- `created_by` (Foreign Key)
- `status`
- `date`
- `(created_by, status)` (Composite)

**JSONB Flexible Fields:**
- social_links
- announcements
- about_data
- guidelines_data
- custom metadata

#### **event_registrations Table**
```sql
CREATE TABLE event_registrations (
  id BIGINT PRIMARY KEY,
  event_id BIGINT NOT NULL REFERENCES events(id),
  user_id BIGINT NOT NULL REFERENCES users(id),
  status VARCHAR(50) DEFAULT 'REGISTERED',
  role VARCHAR(100),
  registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  cancelled_at TIMESTAMP NULL,
  UNIQUE(event_id, user_id)
)
```

**Indexes:**
- `event_id` (Foreign Key)
- `user_id` (Foreign Key)
- `(event_id, status)` (Composite)

### Entity Relationships
```
users (1) ──→ (N) events [created via "created_by" FK]
users (1) ──→ (N) event_registrations
events (1) ──→ (N) event_registrations
```

---

## 7. API Endpoints Summary

### Event Endpoints
```
GET    /api/events                    - Get all events
GET    /api/events/{id}               - Get event by ID
POST   /api/events                    - Create event
PUT    /api/events/{id}               - Update event
DELETE /api/events/{id}               - Delete event
GET    /api/events/search             - Search events
```

### User Endpoints
```
GET    /api/users/{id}                - Get user profile
GET    /api/users                     - Get all users
POST   /api/users                     - Create user
PUT    /api/users/{id}                - Update user
DELETE /api/users/{id}                - Delete user
```

### Registration Endpoints
```
POST   /api/registrations             - Register for event
GET    /api/registrations/user/{id}   - Get user registrations
GET    /api/registrations/event/{id}  - Get event registrations
DELETE /api/registrations/{id}        - Cancel registration
```

### Session Endpoints
```
POST   /api/sessions/validate         - Validate session
POST   /api/sessions/refresh          - Refresh token
```

### OAuth2 Endpoints
```
GET    /oauth2/authorization/google   - Initiate Google login
GET    /login/oauth2/code/google      - OAuth2 callback
```

---

## 8. Design Patterns Used

1. **MVC Pattern**: Model-View-Controller separation
2. **Repository Pattern**: Data access abstraction
3. **Service Pattern**: Business logic encapsulation
4. **DTO Pattern**: Data transfer objects for API
5. **Builder Pattern**: Object construction
6. **Strategy Pattern**: Multiple authentication strategies (OAuth2)
7. **Facade Pattern**: API Gateway
8. **Observer Pattern**: Event listeners
9. **Singleton Pattern**: Service beans
10. **Template Method**: JPA repository templates
11. **HATEOAS Pattern**: Hypermedia links in responses
12. **Proxy Pattern**: Spring AOP proxies

---

## 9. Security Architecture

### Authentication Flow
1. OAuth2-based authentication with Google
2. JWT tokens for stateless sessions
3. Token validation on protected endpoints

### Authorization
- Role-based access control (RBAC)
- User roles: ADMIN, USER, ORGANIZER
- Owner-based access for personal resources

### Data Protection
- HTTPS for all communications
- CORS configuration for cross-origin requests
- Input validation and sanitization
- Exception handling without exposing sensitive info

---

## 10. Performance Optimizations

1. **Caching**: Redis for frequently accessed events
2. **Database Indexing**: On foreign keys and status fields
3. **Connection Pooling**: For database connections
4. **Lazy Loading**: Entity relationships loaded on demand
5. **Pagination**: List endpoints support pagination
6. **Query Optimization**: Custom repository queries

---

## 11. Scalability Considerations

1. **Stateless Services**: No session state in backend
2. **Horizontal Scaling**: Services can be load balanced
3. **Database Replication**: PostgreSQL replication support
4. **Cache Clustering**: Redis cluster support
5. **Microservices Ready**: Services can be separated

---

## 12. How to Use These Diagrams

### For Development
1. Reference LLD for detailed implementation
2. Use Component Diagram for module structure
3. Follow sequence diagrams for workflows
4. Check ER diagram for database schema

### For Architecture Review
1. Start with HLD overview
2. Review Component Diagram for integration points
3. Check ER diagram for data model
4. Review sequence diagrams for critical flows

### For Documentation
1. Use all diagrams in technical documentation
2. Include sequence diagrams in API documentation
3. Reference ER diagram in data migration guides
4. Use component diagram in deployment guides

---

## 13. File Locations

All diagram files are in PlantUML format and located in `/docs/` directory:

```
docs/
├── HLD_Diagram.puml                      # High-Level Design
├── LLD_Diagram.puml                      # Low-Level Design
├── UML_Class_Diagram.puml                # UML Class Diagram
├── Sequence_OAuth2_Authentication.puml   # Auth Flow
├── Sequence_Event_Creation.puml          # Event Creation Flow
├── Sequence_Event_Registration.puml      # Registration Flow
├── Component_Diagram.puml                # System Components
├── Database_ER_Diagram.puml              # Database Schema
└── Architecture_Documentation.md         # This file
```

---

## 14. Rendering Diagrams

These diagrams can be rendered using:
- **PlantUML Online Editor**: https://www.plantuml.com/plantuml/uml/
- **PlantUML CLI**: `plantuml diagram.puml`
- **IDE Plugins**: PlantUML plugins for VS Code, IntelliJ, etc.
- **Documentation Generators**: Markdown with embedded diagrams

---

## 15. Future Enhancements

Possible additions to the design:
1. Payment processing module
2. Notification service (SMS, Push)
3. Analytics and reporting module
4. Ticket generation and QR codes
5. Event feedback and ratings
6. Advanced search with filters
7. Calendar integration
8. Social media sharing
9. Multi-language support
10. Mobile app architecture

---

**Document Version:** 1.0  
**Last Updated:** 2026-05-23  
**Created for:** Event Management System
