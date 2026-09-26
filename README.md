# To-Do CRUD API

A Spring Boot REST API for managing a to-do list.

This project started as the **FlyRank Backend Internship – Week 2 Assignment** and originally stored tasks in memory. In **Week 3 / Assignment A2**, the storage layer was replaced with a real SQLite database while keeping the CRUD API behavior the same.

## Week 3 Goal

The architecture is now:

```
Client
   ↓
Spring Boot REST API
   ↓
TaskRepository
   ↓
SQLite
   ↓
tasks.db
```

The API still exposes the same CRUD endpoints, but task data now survives application restarts.

## Technologies

- Java 21
- Spring Boot 4.1.0
- Spring Web
- Spring JDBC
- SQLite
- Jakarta Validation
- Swagger / OpenAPI
- Maven
- Git / GitHub

## Database

SQLite was chosen because it is a lightweight database stored in a single file and requires no separate database server.

The application creates:

```text
tasks.db
└── tasks
    ├── id
    ├── title
    └── done
```

The `tasks.db` file is created automatically when the application starts.

The `tasks` table is also created automatically if it does not exist.

On the first run, three example tasks are inserted:

1. Learn Spring Boot
2. Build REST API
3. Test the API

The seed data is inserted only when the table is empty, so restarting the application does not create duplicate tasks.

The local database file is intentionally ignored by Git so every clone can create its own fresh database.

## API Endpoints

| Method | Endpoint | Description | Success |
|---|---|---|---|
| GET | `/` | API information | 200 OK |
| GET | `/health` | Health check | 200 OK |
| GET | `/tasks` | Get all tasks | 200 OK |
| GET | `/tasks/{id}` | Get one task | 200 OK |
| GET | `/tasks?done=true` | Filter by completion status | 200 OK |
| POST | `/tasks` | Create a task | 201 Created |
| PUT | `/tasks/{id}` | Update a task | 200 OK |
| DELETE | `/tasks/{id}` | Delete a task | 204 No Content |

Unknown task IDs return:

```http
404 Not Found
```

with a JSON error such as:

```json
{
  "error": "Task 99 not found"
}
```

Invalid task data returns:

```http
400 Bad Request
```

with a JSON error message.

## Task Structure

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "done": false
}
```

The database controls the task ID. A newly created task always starts with `done = false`.

## SQL Storage

The repository uses parameterized SQL queries. User-controlled values are passed separately rather than concatenated into SQL strings.

Examples used by the application:

```sql
SELECT id, title, done FROM tasks;

SELECT id, title, done
FROM tasks
WHERE id = ?;

INSERT INTO tasks (title, done)
VALUES (?, ?);

UPDATE tasks
SET title = ?, done = ?
WHERE id = ?;

DELETE FROM tasks
WHERE id = ?;
```

## Running the Project

### Requirements

- Java 21
- Maven

Run:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

On Windows, you can also use:

```powershell
./mvnw.cmd spring-boot:run
```

## Swagger UI

Open:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger can be used to test the complete CRUD cycle.

## Example Requests

### Create

```bash
curl -i -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy milk"}'
```

Example:

```json
{
  "id": 4,
  "title": "Buy milk",
  "done": false
}
```

### Get all

```bash
curl -i http://localhost:8080/tasks
```

### Get one

```bash
curl -i http://localhost:8080/tasks/1
```

### Filter

```bash
curl -i "http://localhost:8080/tasks?done=false"
```

### Update

```bash
curl -i -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot deeply","done":true}'
```

### Delete

```bash
curl -i -X DELETE http://localhost:8080/tasks/1
```

Successful deletion returns:

```http
204 No Content
```

## Persistence Check

To prove that SQLite is working:

1. Start the application.
2. Run `GET /tasks`.
3. Create a new task.
4. Stop the application.
5. Start it again.
6. Run `GET /tasks` again.
7. Confirm the created task is still present.

The same data can also be inspected by opening `tasks.db` with **DB Browser for SQLite**.

## GitHub / Assignment Stages

- Stage 0 — Create and initialize the SQLite database.
- Stage 1 — Read tasks from SQLite.
- Stage 2 — Insert new tasks into SQLite.
- Stage 3 — Update and delete tasks using SQL.
- Stage 4 — Explore the database manually with DB Browser for SQLite.
- Stage 5 — Document and publish the database-backed API.

## Assignment Context

This is the Spring Boot adaptation of the FlyRank Week 3 Assignment A2, **Connecting your CRUD to the database**.

The assignment's central requirement is to replace in-memory storage with persistent SQLite storage while keeping the API behavior the same.
