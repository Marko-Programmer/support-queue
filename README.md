# Support Triage Queue Service

A Spring Boot-based microservice designed to ingest support tickets and manage a prioritized queue using complex SLA (Business Hours) calculations.

<br>

# 🚀 How to Run
## Prerequisites

* **Java 21**
* **Maven 3.9+**
<br>

## Steps
1. Clone the repository.
2. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at http://localhost:8080.
4. OpenAPI/Swagger UI: Access http://localhost:8080/swagger-ui/index.html to explore and test API endpoints.

<br>

## 🧪 Testing
The project includes Unit tests for core logic and Integration tests for API flows.
To run all tests with one command:
```bash
./mvnw clean test
```
<br>

## Verified Test Scenarios
* **SLA Calculation:** Logic for crossing business day boundaries (e.g., Friday 16:30 to Monday 09:30).
* **Triage Sorting:** Verification that the queue correctly orders tickets by `dueAt`, `severity`, and `customerTier`.
* **API Flow:** End-to-end test creating a ticket via `POST` and immediately retrieving it via `GET /tickets/{id}`.

<br>

## 🏗 Key Decisions & Assumptions
* **In-Memory Persistence:** Used `ConcurrentHashMap` for thread-safe ticket storage, satisfying the "small service" requirement without database overhead.
* **Business Hours SLA:** I implemented a custom calculation logic that strictly follows **Europe/Warsaw** time (09:00–17:00, Mon-Fri).
* **Mandatory Fields:** I assume that `severity` and `customerTier` are mandatory. Without these, it is impossible to correctly compute the SLA or prioritize the ticket.
* **Off-Hours Handling:** If a ticket is created outside business hours (e.g., Saturday), the SLA "clock" starts ticking exactly at **09:00 AM on the next business day** (Monday).
* **Minimal SLA Verification:** To verify overnight transitions, I used a 4-hour SLA case (e.g., `Wed 16:30 + 2h SLA = Thu 10:30`). This confirms the logic works across different days without needing massive datasets.
* **Manual Mapping:** I deliberately moved from MapStruct to manual DTO mapping (static factory methods) to ensure 100% build stability across different IDEs and CI environments.

<br>

## 🤖 AI Usage Disclosure
* **Tools used:** Gemini 2.0 / ChatGPT.
* **What AI was used for:**
    * Core Logic: Generating the initial iteration-based algorithm for calculating SLA business hours (skipping weekends and nights).
    * Boilerplate: Scaffolding for Spring Boot DTOs, Controllers, and GitHub Actions YAML configuration.
<br>

## Examples of AI output I rejected or corrected:
1. **Package Refactoring:** The AI generated code using generic packages (e.g., `com.example...`). I manually corrected all package declarations to match my project structure (`com.marko.support_queue`).
2. **Controller Architecture:** I rejected the AI's simplified controller methods without `ResponseEntity`. I manually wrapped all responses to provide proper HTTP status codes (like `201 Created` with a `Location` header).


<br>
  
## 📋 Ordering Rules (Triage Logic)
The queue (`GET /queue`) is sorted using the following tie-breaking priority:
1. **Earliest `dueAt` first** (Primary SLA deadline).
2. **Higher Severity first** (1 is more urgent than 2).
3. **Higher Tier first** (ENTERPRISE > PRO > FREE).
4. **Older `createdAt` first** (First-in-first-out for equal priority).
<br>


### 🌟 Bonuses
* **OpenAPI (Swagger):** Documented API with operation summaries.
* **GitHub Actions:** Simple CI workflow that runs `mvn test` on every push to `main`.
* **Sample Data:** The application automatically seeds data from `tickets.sample.json` on startup.
