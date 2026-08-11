# Event Management System - Design Documentation Index

## Quick Start Guide

This directory contains comprehensive architecture and design documentation for the Event Management System in PlantUML format.

---

## 📊 Diagram Files

### 1. **HLD_Diagram.puml** - High-Level Design
**Purpose**: System architecture overview showing all major components and their interactions

**Key Content:**
- Client Layer (Web, Mobile, Third-party)
- API Gateway & Security Layer
- Application Controllers
- Business Logic Services
- Data Persistence (Repositories)
- Database Structure
- External Services Integration
- Caching Layer
- Utilities & Middleware

**Use Case**: 
- System overview presentations
- Architecture understanding
- Component identification
- Stakeholder communication

**Audience**: Architects, Product Managers, Team Leads

---

### 2. **LLD_Diagram.puml** - Low-Level Design
**Purpose**: Detailed class-level design with methods, attributes, and interactions

**Key Content:**
- Controller Classes with methods
- Service Layer implementations
- DTO Structures
- Entity Models with attributes
- Repository Interfaces
- Security components
- Exception handling classes

**Use Case:**
- Detailed implementation reference
- Code development guide
- Design review meetings
- Technical documentation

**Audience**: Developers, Tech Leads, Architects

---

### 3. **UML_Class_Diagram.puml** - UML Class Diagram
**Purpose**: Object-oriented design showing classes, enumerations, and relationships

**Key Content:**
- Core Entity Classes (Event, User, EventRegistration)
- Enumerations (EventStatus, UserRole, RegistrationStatus)
- Service Classes
- Controller Classes
- Repository Interfaces
- Class relationships and cardinalities
- Association and inheritance

**Use Case:**
- Object model understanding
- Database design validation
- Design pattern implementation
- Code architecture review

**Audience**: Developers, Architects, OOP specialists

---

### 4. **Sequence_OAuth2_Authentication.puml** - Authentication Flow
**Purpose**: Detailed sequence of OAuth2 authentication with Google

**Key Content:**
- User initiates login
- OAuth2 authorization code exchange
- Google authentication
- User creation/update in database
- JWT token generation
- Dashboard redirect

**Use Case:**
- Understanding login flow
- Security review
- Troubleshooting auth issues
- API documentation

**Audience**: Developers, Security Engineers

---

### 5. **Sequence_Event_Creation.puml** - Event Creation Flow
**Purpose**: Complete sequence of event creation process

**Key Content:**
- User sends event creation request
- Authentication validation
- User verification
- Event data validation
- Database insertion
- Cache update
- Response with HATEOAS links

**Use Case:**
- Event creation workflow
- Validation rules understanding
- Error handling review
- API behavior documentation

**Audience**: Developers, QA Engineers

---

### 6. **Sequence_Event_Registration.puml** - Registration Flow
**Purpose**: Detailed sequence of event registration process

**Key Content:**
- Registration request handling
- Event and user validation
- Duplicate check
- Capacity checking
- Waiting list vs registered status
- Email notification
- Response generation

**Use Case:**
- Registration workflow
- Business logic validation
- Capacity management
- Notification system

**Audience**: Developers, Business Analysts

---

### 7. **Component_Diagram.puml** - System Components
**Purpose**: Component-level architecture showing modules and their dependencies

**Key Content:**
- Presentation Layer
- API & Gateway Layer
- Security Layer
- Controller Layer
- Service Layer
- Data Access Layer
- Caching Layer
- External Services
- Database Layer
- Configuration & Utilities

**Use Case:**
- System integration
- Module dependencies
- Technology stack
- Deployment architecture

**Audience**: Architects, DevOps Engineers

---

### 8. **Database_ER_Diagram.puml** - Entity-Relationship Diagram
**Purpose**: Database schema design and relationships

**Key Content:**
- users table (schema, indexes, constraints)
- events table (with JSONB support)
- event_registrations table
- Foreign key relationships
- Unique constraints
- Composite indexes
- Data types and sizes

**Use Case:**
- Database design review
- SQL query optimization
- Schema migration
- Data model validation

**Audience**: Database Administrators, Developers

---

### 9. **ARCHITECTURE_DOCUMENTATION.md** - Complete Documentation
**Purpose**: Comprehensive written documentation of entire architecture

**Sections:**
1. Overview of the system
2. HLD explanation with components
3. LLD detailed class descriptions
4. UML diagram explanation
5. Sequence diagram flows
6. Component architecture
7. Database schema details
8. API endpoints summary
9. Design patterns used
10. Security architecture
11. Performance optimizations
12. Scalability considerations
13. Usage guidelines
14. File locations
15. Rendering instructions
16. Future enhancements

**Use Case:**
- Reference documentation
- Onboarding new team members
- Architecture decision documentation
- Technical proposals

**Audience**: All technical team members

---

## 🎯 How to Use These Files

### For New Team Members
1. Start with **ARCHITECTURE_DOCUMENTATION.md** sections 1-3
2. Review **HLD_Diagram.puml** for overall structure
3. Study **Component_Diagram.puml** for module organization
4. Read LLD_Diagram.puml for implementation details

### For Developers
1. Reference **LLD_Diagram.puml** for class structures
2. Check **Sequence Diagrams** for workflow understanding
3. Consult **Database_ER_Diagram.puml** for schema
4. Review **UML_Class_Diagram.puml** for relationships

### For Architects
1. Review **HLD_Diagram.puml** for complete architecture
2. Check **Component_Diagram.puml** for integration points
3. Analyze **ARCHITECTURE_DOCUMENTATION.md** for design decisions
4. Review scalability and performance sections

### For DevOps/Infrastructure
1. Study **Component_Diagram.puml** for deployment needs
2. Check external services and dependencies
3. Review database layer requirements
4. Check caching and performance configurations

### For Database Administrators
1. Study **Database_ER_Diagram.puml** for schema
2. Review table structures and indexes
3. Check relationship constraints
4. Plan backup and replication strategies

### For Testing/QA
1. Review **Sequence Diagrams** for expected flows
2. Check **LLD_Diagram.puml** for input validation
3. Study **ARCHITECTURE_DOCUMENTATION.md** - API Endpoints section
4. Review error handling and edge cases

---

## 🔄 How to Render These Diagrams

### Online
1. Visit https://www.plantuml.com/plantuml/uml/
2. Copy content of any .puml file
3. Paste into the editor
4. View rendered diagram
5. Export as PNG, SVG, or PDF

### Command Line
```bash
# Install PlantUML
# On macOS: brew install plantuml
# On Ubuntu: sudo apt install plantuml

# Render single diagram
plantuml HLD_Diagram.puml -o output/

# Render all diagrams
for file in *.puml; do
  plantuml "$file" -o output/
done
```

### IDE Integration
- **VS Code**: Install "PlantUML" extension
- **IntelliJ IDEA**: Install "PlantUML integration" plugin
- **Eclipse**: Install "PlantUML" plugin

### In Documentation
Use in markdown with:
```markdown
![HLD Diagram](./HLD_Diagram.puml)
```

---

## 📋 Diagram Dependencies

```
HLD_Diagram.puml
  ├── Provides overview for all detailed diagrams
  ├── Referenced by Component_Diagram.puml
  └── Detailed in LLD_Diagram.puml

LLD_Diagram.puml
  ├── Expands on HLD classes
  ├── Maps to UML_Class_Diagram.puml
  └── Referenced by Sequence diagrams

Sequence Diagrams (3 files)
  ├── Use entities from UML_Class_Diagram.puml
  ├── Show components from Component_Diagram.puml
  └── Access database from Database_ER_Diagram.puml

Database_ER_Diagram.puml
  ├── Referenced by LLD_Diagram.puml
  ├── Used by sequence diagrams
  └── Detailed in ARCHITECTURE_DOCUMENTATION.md
```

---

## 🔍 Finding Specific Information

### Looking for...
| Topic | Check File | Section |
|-------|-----------|---------|
| System architecture | HLD_Diagram.puml | All |
| Event creation logic | Sequence_Event_Creation.puml | All |
| Authentication flow | Sequence_OAuth2_Authentication.puml | All |
| Class structure | LLD_Diagram.puml | All |
| Object relationships | UML_Class_Diagram.puml | Relationships |
| Module dependencies | Component_Diagram.puml | All |
| Database schema | Database_ER_Diagram.puml | All |
| API endpoints | ARCHITECTURE_DOCUMENTATION.md | Section 7 |
| Design patterns | ARCHITECTURE_DOCUMENTATION.md | Section 8 |
| Security details | ARCHITECTURE_DOCUMENTATION.md | Section 9 |
| Performance tips | ARCHITECTURE_DOCUMENTATION.md | Section 10 |

---

## 📚 Documentation Structure

```
docs/
├── HLD_Diagram.puml                      [System Overview]
├── LLD_Diagram.puml                      [Class Details]
├── UML_Class_Diagram.puml                [Object Model]
├── Sequence_OAuth2_Authentication.puml   [Auth Flow]
├── Sequence_Event_Creation.puml          [Create Flow]
├── Sequence_Event_Registration.puml      [Register Flow]
├── Component_Diagram.puml                [Modules]
├── Database_ER_Diagram.puml              [Database]
├── ARCHITECTURE_DOCUMENTATION.md         [Complete Guide]
└── DIAGRAM_INDEX.md                      [This File]
```

---

## 🚀 Technology Stack Reference

### Backend
- **Framework**: Spring Boot 3.4.2
- **Language**: Java 21
- **ORM**: Spring Data JPA
- **Security**: Spring Security + OAuth2
- **API Style**: REST with HATEOAS

### Database
- **Primary**: PostgreSQL
- **Features**: JSONB support for flexible schema
- **Caching**: Redis

### Authentication
- **Method**: OAuth2 with Google
- **Token**: JWT (JSON Web Tokens)

### External Services
- **Auth Provider**: Google OAuth2
- **Email**: Email Service Integration
- **Storage**: File Storage for media

---

## 🔗 Key Relationships Summary

### Entity Relationships
```
User (1) ──creates──→ (N) Event
User (1) ──registers for──→ (N) Event [through EventRegistration]
Event (1) ──has──→ (N) EventRegistration
```

### Module Interactions
```
Client → API Gateway → Controllers → Services → Repositories → Database
              ↓
          Security/Auth
              ↓
          Exception Handlers
```

### Data Flow
```
Request → API Gateway → Auth → Controller → Service → Repo → DB
Response ← HATEOAS ← Assembler ← Service ← Response ← DB
```

---

## 📝 Maintenance Notes

### Update Triggers
- When adding new entity
- When creating new service
- When adding new controller endpoint
- When changing database schema
- When adding new workflow

### Update Process
1. Modify relevant .puml file(s)
2. Update ARCHITECTURE_DOCUMENTATION.md
3. Test diagram rendering
4. Commit with detailed message
5. Update any related documentation

---

## 👥 Contact & Questions

### For Diagram-Related Questions
- Refer to ARCHITECTURE_DOCUMENTATION.md
- Check specific diagram file headers
- Review sequence diagram flows

### For Implementation Questions
- Check LLD_Diagram.puml
- Review relevant sequence diagrams
- Consult ARCHITECTURE_DOCUMENTATION.md

---

## ✅ Checklist for Using Diagrams

- [ ] I understand the system overview (HLD)
- [ ] I know the component dependencies (Component Diagram)
- [ ] I understand the class structure (LLD & UML)
- [ ] I know the authentication flow (Sequence - Auth)
- [ ] I understand event creation (Sequence - Create)
- [ ] I understand registration (Sequence - Register)
- [ ] I know the database schema (ER Diagram)
- [ ] I've read the complete documentation

---

**Version:** 1.0  
**Last Updated:** 2026-05-23  
**Status:** Complete & Ready for Review
