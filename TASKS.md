# Event Management – Task Backlog

> Tasks are atomic and self-contained unless grouped under a **Super-Task**, which means the sub-tasks share context or ordering constraints.

---

## 🐛 Bugs

### BUG-01 – Hardcoded string `"email"` in `createUserwithOAuth` ✅ **DONE**
**File:** `service/UserService.java:31`
```java
// BUG: passes the literal string "email" instead of the variable `email`
User preUser = userRepository.findByEmail("email").orElse(null);
```
Fix: change `"email"` → `email` (the local variable). **Fixed: line 31 now uses `email` variable.**

---

### BUG-02 – Unused variable `accessToken` in `CustomOAuth2UserService.loadUser` ✅ **DONE**
**File:** `security/CustomOAuth2UserService.java:29`
`String accessToken = userRequest.getAccessToken().getTokenValue();` is assigned but never read (the value is fetched again inline at line 34). Remove the dead assignment. **Fixed: line 29 is now commented out.**

---

### BUG-03 – `EventRepository.delete` shadows `JpaRepository.delete`
**File:** `repository/EventRepository.java:21-24`
A custom `@Modifying @Query` named `delete(Event)` is declared, but `JpaRepository` already provides `delete(T entity)`. The custom query is also never called directly — `EventService.deleteEvent` calls `eventRepo.delete(event)` which will resolve to the JPA default, bypassing the custom query entirely. Either remove the custom method and use `deleteById`, or rename it to something unambiguous like `deleteByEvent`.

---

### BUG-04 – `UserRepository.findUserWithAdminAndRegistrations` is missing `@Param` and is never used
**File:** `repository/UserRepository.java:18-19`
```java
@Query("SELECT u FROM User u WHERE u.id = :id")
User findUserWithAdminAndRegistrations();   // no @Param("id"), no argument
```
The method signature has no parameter for `:id`, so it will throw an error at runtime if called. Either fix the signature (`User findUserWithAdminAndRegistrations(UUID id)`) or delete it if unused.

---

### BUG-05 – `EventRegistrationRepository.findByEventIdWithUser` has a wrong `@Param` name
**File:** `repository/EventRegistrationRepository.java:26`
```java
List<EventRegistration> findByEventIdWithUser(@Param("userId") UUID eventId);
```
The `@Param` name is `"userId"` but the JPQL uses `:eventId`. This will fail at runtime. Fix: change `@Param("userId")` → `@Param("eventId")`.

---

### BUG-06 – `deleteEvent` in `EventController` returns `500` for "not found" instead of `404`
**File:** `controller/EventController.java:103-105`
```java
if (event == null) {
    return ResponseEntity.internalServerError().body("Event not found with id: " + eventId);
}
```
A missing resource should return `404 Not Found`, not `500 Internal Server Error`.

---

### BUG-07 – `EventService.updateEvent` creates a new event if ID is not found (upsert)
**File:** `service/EventService.java:72`
```java
Event existingEvent = eventRepo.findById(Id).orElseGet(() -> eventRepo.save(event));
```
A `PUT /{id}` that silently creates a new record when the ID doesn't exist is confusing and breaks REST semantics. If creating via PUT is intentional, it must be documented; otherwise throw a proper `EventNotFoundException`.

---

### BUG-08 – `UserService.updateUser` creates a blank `User` if ID not found
**File:** `service/UserService.java:51`
```java
User existingUser = userRepository.findById(id).orElseGet(() -> new User());
```
Saving a `new User()` with no mandatory fields will either violate DB constraints or silently create a ghost record. Should throw `UserNotFoundException` (or equivalent) instead.

---

### BUG-09 – `UserService.getUserByEmail` / `toUserReturnDTO` will NPE on null user
**File:** `service/UserService.java:93-94` and `62-74`
`getUserByEmail` returns `null` when the email is not found, then immediately calls `toUserReturnDTO(null)`, which calls `user.getId()` → `NullPointerException`. Same pattern in `getUser(UUID id)` at line 44-45.

---

### BUG-10 – `SecurityConfig.targetUrl` null-check is dead code
**File:** `security/SecurityConfig.java:71-73`
```java
if (targetUrl == null) {
    targetUrl = "/";
}
```
`targetUrl` is a non-null field initialized to `"http://localhost:5173"`, so this branch is never taken. Remove the dead check.

---

### BUG-11 – `SessionCookie.setSecure(true)` breaks local HTTP development
**File:** `security/SecurityConfig.java:66-67`
The session cookie is marked `Secure` unconditionally, meaning it will never be sent over plain `http://localhost`. Use an environment-aware flag (e.g., `${app.cookie.secure:false}`) or derive it from the request.

---

### BUG-12 – `EventRegistrationController.registerUser` accepts raw UUID body
**File:** `controller/EventRegistrationController.java:33-36`
`@RequestBody UUID eventId` is not idiomatic REST — Jackson may reject bare JSON strings. Wrap the payload in a small DTO (e.g., `{ "eventId": "..." }`) or move the event ID to the path.

---

### BUG-13 – `markAsAttended` uses `findByUserId` then filters in memory
**File:** `service/EventRegistrationService.java:48-55`
The method loads **all** registrations for a user and then filters for the target event in Java. This is an N+1/full-scan pattern. Replace with a dedicated repository query: `findByUserIdAndEventId(UUID userId, UUID eventId)`.

---

### BUG-14 – `.env` file committed to repository (secrets leak)
**File:** `.gitignore:3` lists `.env`, but the file is present in the working tree and **already committed** (it appears in the workspace).  
Revoke and rotate all secrets in the file immediately. Ensure `.env` is removed from git history (`git rm --cached .env`).

---

## 🔒 Security

### SEC-01 – Session token is Base64-encoded username + timestamp (no signature)
**File:** `security/SecurityConfig.java:105-113`
The custom `generateSessionToken` is trivially forgeable — anyone can craft a valid token. Either rely solely on Spring's built-in server-side session management (already in use) and remove the custom token entirely, or use a proper signed JWT.

---

### SEC-02 – `SessionValidationService` is instantiated but never actually called
**File:** `security/SecurityConfig.java:117-120`, `controller/SessionController.java:34`
The cookie-based validation path is fully commented out in `SessionController`. The custom token generation in the success handler adds a cookie that is never validated. Either wire it up or remove the entire dead infrastructure.

---

### SEC-03 – CSRF is disabled globally
**File:** `security/SecurityConfig.java:35`
`csrf.disable()` is applied to all endpoints. For a session-cookie–based app this opens CSRF attack surface. Consider enabling CSRF with `CookieCsrfTokenRepository.withHttpOnlyFalse()` (compatible with SPA frontends).

---

### SEC-04 – `@CrossOrigin` on individual controllers duplicates global CORS config
**Files:** `controller/EventController.java:44`, `controller/EventRegistrationController.java:25`
CORS is already configured globally in `SecurityConfig.corsConfigurationSource()`. Repeating `@CrossOrigin` on controllers is redundant and may silently override the global policy. Remove the per-controller annotations.

---

### SEC-05 – Authorization in `deleteUser` / `replaceUser` calls `getCurrentUserData()` twice
**File:** `controller/UserController.java:119-120`, `181`
`getCurrentUserData()` runs a full authentication + DB lookup each time. Cache the result in a local variable. Also, comparing role as `"ADMIN"` string instead of using the `Role.ADMIN` enum constant is fragile.

---

### SEC-06 – `newEvent` endpoint does not check if the authenticated user matches `admin` field
**File:** `controller/EventController.java:83-94`
Any authenticated user can create an event and assign any `admin` UUID, including someone else's account. The endpoint should enforce that `newEvent.getAdmin()` equals the currently authenticated user's ID (unless the caller is ADMIN).

---

### SEC-07 – Hardcoded default credentials in `application.properties`
**File:** `src/main/resources/application.properties:26-27`
```
spring.security.user.name=admin
spring.security.user.password=admin123
```
These are active in production if basic-auth is not explicitly disabled. Remove or externalize them.

---

## ♻️ Refactors

### REF-01 – Duplicated inner DTO classes mirror the model package
**File:** `dto/EventResponseDTO.java:203-285`
`AboutData`, `AboutSection`, `CallToAction`, `GuidelinesData`, `ResourceInfo`, `TimelineItem`, `FaqItem`, `Coordinators`, `Announcement` are all re-declared as non-static inner classes inside `EventResponseDTO`. They are structural duplicates of the model classes in `com.event_management.model`. Promote them to top-level DTO classes in the `dto` package and share them across DTOs.

---

### REF-02 – `EventResponseDTO` constructor manually maps every nested field
**File:** `dto/EventResponseDTO.java:45-95`
~200 lines of manual mapping. Introduce a mapper library (MapStruct is already partially configured via `lombok-mapstruct-binding`) or a dedicated `EventMapper` class, and replace the constructor with a mapper call.

---

### REF-03 – `getAllBaseEventsAsDTO` and `getAllEventsBaseDTOs` do the same thing
**File:** `service/EventService.java:37-48`
Both methods query all events and project to `EventBaseDTO`. One uses the assembler, the other uses the constructor directly. Remove one; prefer the constructor-based version which is simpler.

---

### REF-04 – `EventModelAssembler` implements `RepresentationModelAssembler<EventBaseDTO, ...>` but also has overloaded `toModel` for `Event` and `EventResponseDTO`
**File:** `assembler/EventModelAssembler.java`
The assembler violates the single-responsibility of `RepresentationModelAssembler` by handling three different types via overloading. Split into `EventBaseDTOModelAssembler`, keeping `EventModelAssembler` for `EventResponseDTO` only.

---

### REF-05 – `UserController` mixes SLF4J via `LoggerFactory` while other classes use `@Slf4j`
**File:** `controller/UserController.java:50`
```java
private final Logger logger = LoggerFactory.getLogger(UserController.class);
```
Replace with `@Slf4j` (Lombok) to be consistent with `EventService`, `UserService`, etc.

---

### REF-06 – `replaceUser` has a large commented-out duplicate method above it ✅ **DONE**
**File:** `controller/UserController.java:129-143`
The old `replaceUser` implementation is left commented out. Remove dead commented code. **Removed in cleanup.**

---

### REF-07 – Debug logging in `replaceUser` should be removed or scoped ✅ **DONE**
**File:** `controller/UserController.java:152-179`
Extensive debug statements were added during troubleshooting but never cleaned up. Remove them or convert to `TRACE` level after the fix is confirmed. **Removed in cleanup.**

---

### REF-08 – `JacksonConfig.java` is entirely commented out ✅ **DONE**
**File:** `config/JacksonConfig.java`
The entire file is wrapped in a block comment. Either delete the file or uncomment and activate the configuration (the project may need it for correct `JavaTimeModule` behavior with `LocalDate`/`LocalTime`). **File deleted.**

---

### REF-09 – `pom.xml` groupId is `com.` (truncated)
**File:** `pom.xml:11`
```xml
<groupId>com.</groupId>
```
Should be `com.event_management` (or a proper reverse-domain groupId). This is a cosmetic/metadata issue but looks like an oversight.

---

### REF-10 – `application.properties` has leading-space indentation on some lines
**File:** `src/main/resources/application.properties:45-48,50`
Lines for Google OAuth and the ngrok comment have a leading space, which Spring Boot tolerates but is inconsistent and confusing. Normalize indentation.

---

### REF-11 – `logging.level.com.event_management.controller.UserController.package` is not a valid key
**File:** `src/main/resources/application.properties:24`
The key ends in `.package`, which is not a valid Spring logging key. It should be `logging.level.com.event_management.controller`. The mistyped key is silently ignored.

---

### REF-12 – `CreateUserRequestDTO` has no Lombok annotations and is unused ✅ **DONE**
**File:** `dto/CreateUserRequestDTO.java`
Fields are private with no getters/setters and the class is never referenced. Either wire it up as the `@RequestBody` for `POST /user` (replacing the raw `User` entity) or delete it. **File deleted.**

---

### REF-13 – `UserController.getUser` and `UserController.getUserName` return raw `OAuth2User` / plain `String` (no DTO)
**File:** `controller/UserController.java:52-61`
`GET /user/find-user` returns a raw email string and `GET /user/user` returns the full `OAuth2User` (Spring internal object). Both should return proper response DTOs.

---

### REF-14 – `EventController.getAllBaseEvents` self-link points to `getAllEvents`, not `getAllBaseEvents`
**File:** `controller/EventController.java:69`
```java
linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel()
```
Should be `getAllBaseEvents()` for a correct HATEOAS self-link.

---

## 🚀 Optimizations

### OPT-01 – `getAllEvents` and `getAllBaseEvents` execute the same query, loading all registrations + users
**File:** `repository/EventRepository.java:18-19`, `service/EventService.java:32-42`
The list endpoints fetch full registration graphs (all users per event) to build the base DTO, which only needs `id`, `name`, `date`, `location`, `admin`. Use a JPQL projection or a lighter query for the list endpoint.

---

### OPT-02 – `EventRegistration` uses `FetchType.EAGER` for both `user` and `event`
**File:** `model/EventRegistration.java:24,29`
Both associations are `EAGER`, meaning every load of `EventRegistration` triggers two additional SELECTs. Change to `LAZY` and use explicit `JOIN FETCH` only where needed.

---

### OPT-03 – `EventService` is annotated `@Transactional` at class level but individual read methods re-annotate with `@Transactional(readOnly = true)`
**File:** `service/EventService.java:24,31,36,44,51`
The class-level `@Transactional` starts a write transaction for every method, including reads. Flip the default: annotate the class with `@Transactional(readOnly = true)` and override only write methods with `@Transactional`.

---

### OPT-04 – Redis is declared as a dependency but no caching annotations are used
**File:** `pom.xml:46-48`, `application.properties:8-11`
`spring-boot-starter-data-redis` and `spring.cache.type=redis` are configured, but no `@Cacheable`/`@CacheEvict` annotations exist anywhere. Either apply caching to expensive read endpoints (e.g., `getAllEvents`) or remove the unused dependency.

---

### OPT-05 – `spring-boot-starter-oauth2-resource-server` is included but not configured ✅ **DONE**
**File:** `pom.xml:50-52`
The resource-server starter (for JWT bearer token validation) is on the classpath but there is no `spring.security.oauth2.resourceserver.*` config and no `@EnableResourceServer`. Remove if not needed to reduce startup overhead and potential auto-configuration conflicts. **Dependency removed from pom.xml.**

---

### OPT-06 – Hikari `idle-timeout` is set to `1000000` ms (~16 min) but `max-lifetime` is `1800000` ms (30 min)
**File:** `application.properties:31-32`
Best practice: `idle-timeout` < `max-lifetime` < database's `wait_timeout`. The current values are likely accidental large numbers. Tune to sensible defaults: e.g., `idle-timeout=600000`, `max-lifetime=1800000`.

---

## 🧪 Testing

### TEST-01 – Zero test coverage (no unit or integration tests written)
**File:** `src/test/`
Only the default auto-generated application context test exists. Add unit tests for at minimum: `UserService`, `EventService`, `EventRegistrationService`, and `CustomOAuth2UserService`.

---

### TEST-02 – No input validation annotations on DTOs or request bodies
**Files:** `dto/EventDTO.java`, `dto/UserResponseDTO.java`, `controller/EventController.java`, `controller/UserController.java`
`@NotNull`, `@NotBlank`, `@Size`, etc. are never used even though `spring-boot-starter-validation` is a dependency and `@Validated` is present on all controllers. Add Bean Validation constraints to DTOs.

---

## 📋 Incomplete / TODO Items

### TODO-01 – Auto-mark registrations as `MISSED` after event end time
**File:** `controller/EventRegistrationController.java:56-57`
An existing `//TODO` comment describes a background job to mark absent attendees. Implement using Spring `@Scheduled` or a PostgreSQL cron (pg_cron), as noted in the comment.

---

### TODO-02 – `updateEvent` does not handle partial updates or nested JSON fields
**File:** `service/EventService.java:69-80`
An existing `//TODO` comment calls out that the update logic overwrites with nulls from missing fields and doesn't handle `aboutData`, `guidelinesData`, etc. Implement a proper partial-update (PATCH semantics or explicit null-checks).

---

### TODO-03 – `updateUser` does not fetch/update `rollNo` or `department`
**File:** `service/UserService.java:49-59`
An existing `//TODO` comment questions how to source these fields. Decide: include them in `UserResponseDTO` update flow (they are already in the DTO) and simply set them in `updateUser`, or document them as read-only.

---

## 🗂️ Super-Tasks

### [ST-A] Secrets & Environment Hygiene _(ordered)_
> BUG-14 → SEC-07 → REF-09 must be done together to fully clean up credential and config issues.

1. **BUG-14** – Remove `.env` from git history and rotate leaked secrets.
2. **SEC-07** – Remove hardcoded `spring.security.user.*` credentials.
3. **REF-10** – Fix indentation / leading spaces in `application.properties`.
4. **REF-11** – Fix the broken logging key.

---

### [ST-B] Null-safety in Service Layer _(ordered)_
> BUG-09 → BUG-07 → BUG-08 must be addressed together to ensure consistent error-handling.

1. **BUG-09** – Guard `toUserReturnDTO` / `getUserByEmail` against null users.
2. **BUG-07** – Replace upsert in `updateEvent` with a proper not-found exception.
3. **BUG-08** – Replace silent new-user creation in `updateUser` with a not-found exception.

---

### [ST-C] Dead Session Token Infrastructure _(ordered)_
> SEC-01 → SEC-02 must be resolved together; removing one without the other leaves dangling references.

1. **SEC-01** – Evaluate and remove or properly implement the custom session token.
2. **SEC-02** – Remove the commented-out validation path and the unused `SessionValidationService` wiring.

---

### [ST-D] DTO Consolidation _(ordered)_
> REF-01 → REF-02 → REF-12 form a single coherent refactor.

1. **REF-01** – Promote inner DTO classes in `EventResponseDTO` to top-level classes.
2. **REF-02** – Replace the manual constructor mapping in `EventResponseDTO` with a mapper.
3. **REF-12** – Wire up or delete `CreateUserRequestDTO`.

---

### [ST-E] List Endpoint Performance _(ordered)_
> OPT-01 → OPT-02 → REF-03 should be done together to avoid regression.

1. **OPT-01** – Use a lightweight JPQL projection for list endpoints.
2. **OPT-02** – Change `EventRegistration` associations to `LAZY`.
3. **REF-03** – Delete the duplicate `getAllBaseEventsAsDTO` service method.
