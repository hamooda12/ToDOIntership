# To-Do CRUD API

A Spring Boot REST API for managing a to-do list.

This project is part of the **FlyRank Backend Internship** and provides a CRUD API backed by PostgreSQL. The application can be run locally with Spring Boot or as a complete containerized stack using Docker Compose.

## Architecture

### Local development

```text
Client
   ↓
Spring Boot REST API
   ↓
TaskRepository
   ↓
PostgreSQL
```

### Docker Compose

```text
                    Docker Compose
                         │
              ┌──────────┴──────────┐
              │                     │
           app                     db
      Spring Boot API           PostgreSQL
              │                     │
              └──── db:5432 ────────┘
                         │
                     pgdata
                   named volume
```

The API container connects to PostgreSQL using the Compose service name `db`, not `localhost`.

## Technologies

- Java 21
- Spring Boot 4.1.0
- Spring Web
- Spring JDBC
- PostgreSQL
- Jakarta Validation
- Swagger / OpenAPI
- Maven
- Docker
- Docker Compose
- Git / GitHub

## Database

The application uses PostgreSQL.

The database is configured with:

```text
Database: tasks
Username: postgres
```

On the first run, the application creates the `tasks` table if it does not exist.

If the table is empty, three example tasks are inserted:

1. Learn Spring Boot
2. Build REST API
3. Test the API

The seed data is inserted only when the table is empty, so restarting the application does not create duplicate tasks.

## Environment Variables

Local configuration is loaded from a `.env` file.

Create a local `.env` file based on `.env.example`:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/tasks
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=dev
```

The `.env` file is ignored by Git and must not be committed.

For Docker Compose, the application container uses the PostgreSQL service name:

```text
jdbc:postgresql://db:5432/tasks
```

The committed `.env.example` contains the variables required for local development without exposing local environment files.

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

The database controls the task ID. A newly created task starts with `done = false` unless explicitly provided otherwise.

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

## Running with Docker Compose

### Requirements

- Docker Desktop
- Docker Compose

The complete application stack can be started with one command:

```bash
docker compose up
```

To rebuild the Spring Boot image before starting:

```bash
docker compose up --build
```

The stack contains:

- `app` — Spring Boot REST API
- `db` — PostgreSQL database
- `pgdata` — named PostgreSQL volume

The API will be available at:

```text
http://localhost:8080
```

Check the API:

```bash
curl http://localhost:8080/tasks
```

On PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/tasks
```

To stop the stack without deleting database data:

```bash
docker compose down
```

> Do not use `docker compose down -v` when you want to preserve the PostgreSQL data. The `-v` option removes the named volume.

## Running Locally

### Requirements

- Java 21
- Maven
- PostgreSQL

Configure the local PostgreSQL connection in `.env`, then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
./mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Swagger UI

Open:

```text
http://localhost:8080/swagger-ui.html
```

Swagger can be used to test the complete CRUD cycle.

## Example Requests

### Create

```bash
curl -i -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy milk"}'
```

Example response:

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

The PostgreSQL database uses a Docker named volume called `pgdata`.

To verify that database data survives container recreation:

1. Start the stack:
   ```bash
   docker compose up
   ```
2. Create a new task.
3. Verify the task exists with `GET /tasks`.
4. Stop the stack:
   ```bash
   docker compose down
   ```
5. Start the stack again:
   ```bash
   docker compose up
   ```
6. Run `GET /tasks` again.
7. Confirm that the previously created task is still present.

Do **not** use `docker compose down -v` for this test because removing the volume also removes the persisted database data.

## Docker Files

### Dockerfile

The project uses a multi-stage Docker build:

1. Maven/JDK image builds the Spring Boot application.
2. A smaller Eclipse Temurin JRE image runs the generated JAR.

### Docker Compose

The Compose configuration defines:

- Spring Boot API service: `app`
- PostgreSQL service: `db`
- API-to-database connection through `db:5432`
- PostgreSQL named volume: `pgdata`
- API port: `8080`

## Git and Secrets

The following local files/data are intentionally excluded from Git:

- `.env`
- local database files
- build output

Use `.env.example` as the template for required environment variables.

No local `.env` file should be committed to the repository.

## Assignment Progress

This implementation covers the containerized PostgreSQL stack:

- PostgreSQL runs in Docker.
- PostgreSQL data is stored in a named Docker volume.
- Spring Boot connects to PostgreSQL using environment variables.
- The API reads and writes tasks using PostgreSQL.
- The Spring Boot application is packaged in a Docker image.
- Docker Compose starts the API and PostgreSQL together.
- The API connects to PostgreSQL using the Compose service name `db`.
- Database persistence has been verified across `docker compose down` and `docker compose up`.

## Project Repository

GitHub:

```text
https://github.com/hamooda12/ToDOIntership
```
