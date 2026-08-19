# To-Do CRUD API

A simple REST API for managing a to-do list.

This project was built as part of the **FlyRank Backend Internship – Week 2 Assignment** to practice the fundamentals of backend development, including **HTTP, REST APIs, CRUD operations, status codes, validation, Swagger UI, Git, and GitHub**.

The application currently stores tasks **in memory**, so the data will be reset whenever the server restarts.

## Features

* Create a new task
* Get all tasks
* Get a task by ID
* Update an existing task
* Delete a task
* Input validation
* Proper HTTP status codes
* Health check endpoint
* Interactive API documentation with Swagger UI

## Task Structure

Each task contains:

```json
{
  "id": 1,
  "title": "Learn REST APIs",
  "done": false
}
```

## API Endpoints

| Method   | Endpoint      | Description                      | Success Status   |
| -------- | ------------- | -------------------------------- | ---------------- |
| `GET`    | `/`           | Get API information              | `200 OK`         |
| `GET`    | `/health`     | Check whether the API is running | `200 OK`         |
| `GET`    | `/tasks`      | Get all tasks                    | `200 OK`         |
| `GET`    | `/tasks/{id}` | Get a task by ID                 | `200 OK`         |
| `POST`   | `/tasks`      | Create a new task                | `201 Created`    |
| `PUT`    | `/tasks/{id}` | Update an existing task          | `200 OK`         |
| `DELETE` | `/tasks/{id}` | Delete a task                    | `204 No Content` |

If a requested task does not exist, the API returns:

```http
404 Not Found
```

Invalid task data returns:

```http
400 Bad Request
```

## Running the Project

### Requirements

* Java 17+
* Maven

Clone the repository:

```bash
git clone <YOUR-GITHUB-REPOSITORY-URL>
cd <YOUR-REPOSITORY-NAME>
```

Run the application:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Swagger UI

Swagger UI provides interactive documentation where all API endpoints can be tested directly from the browser.

Open:

```text
http://localhost:8080/swagger-ui/index.html
```

### Swagger Screenshot

![Swagger UI](Screenshot From 2026-08-19 17-55-56.png)

Using Swagger UI, you can perform the complete CRUD cycle:

1. Create a task with `POST /tasks`
2. View tasks with `GET /tasks`
3. Update the task with `PUT /tasks/{id}`
4. Delete the task with `DELETE /tasks/{id}`

## Example Request

Create a new task:

```bash
curl -i -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy milk"}'
```

Example response:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 4,
  "title": "Buy milk",
  "done": false
}
```

Get all tasks:

```bash
curl -i http://localhost:8080/tasks
```

Get a specific task:

```bash
curl -i http://localhost:8080/tasks/1
```

Delete a task:

```bash
curl -i -X DELETE http://localhost:8080/tasks/1
```

A successful delete returns:

```http
HTTP/1.1 204 No Content
```

## Validation

The API validates incoming task data.

For example, creating a task without a valid title is rejected:

```json
{
  "title": ""
}
```

The server responds with `400 Bad Request`.

The API also returns `404 Not Found` when a task ID does not exist.

## Technologies Used

* Java
* Spring Boot
* Spring Web
* Jakarta Validation
* Swagger / OpenAPI
* Maven
* Git
* GitHub

## What I Practiced

Through this project I practiced:

* Understanding the HTTP request-response cycle
* Designing REST API endpoints
* Implementing CRUD operations
* Working with path parameters and request bodies
* Validating client input
* Using HTTP status codes correctly
* Documenting and testing APIs with Swagger UI
* Using Git commits to track development progress
* Publishing a backend project to GitHub

## AI vs Me

### My First Prompt

I asked an AI assistant to build the same API after I had already completed my own version manually.

```text
Build a small REST API for managing a to-do list using Java and Spring Boot.

Create the AI-generated version inside a separate folder named `ai-version/`. Do not modify any existing project files outside that folder.

Requirements:

Use:
- Java
- Spring Boot
- Maven
- Spring Web
- Jakarta Validation
- Swagger/OpenAPI

Do not use a database. Store all tasks in memory while the application is running.

A task should contain:
- id — number
- title — string
- done — boolean

Start the application with 3 example tasks already stored in memory.

Implement these CRUD endpoints:

- GET /tasks — return all tasks.
- GET /tasks/{id} — return one task by ID.
- POST /tasks — create a new task.
- PUT /tasks/{id} — update the title and/or done status of an existing task.
- DELETE /tasks/{id} — delete a task.

Also implement:

- GET / — return JSON containing the API name, version, and /tasks as an available endpoint.
- GET /health — return { "status": "ok" }.

HTTP behavior:

- Successful GET → 200 OK
- Successful POST → 201 Created
- Successful PUT → 200 OK
- Successful DELETE → 204 No Content
- Task ID does not exist → 404 Not Found
- Invalid request data → 400 Bad Request

When a task does not exist, return a JSON error message such as:

{ "error": "Task 99 not found" }

Validation:

For POST /tasks:
- title is required.
- title cannot be null, empty, or blank.
- New tasks must automatically start with done = false.
- The server should generate the next available ID.

For PUT /tasks/{id}:
- Allow changing title, done, or both.
- Reject an empty or invalid request body with 400 Bad Request.
- If a title is provided, it cannot be blank.

Swagger:

Add Swagger/OpenAPI documentation.

Swagger UI should display all endpoints and allow me to test the full CRUD flow from the browser.

Add short descriptions for the endpoints.

Important constraints:

- Do not add a database.
- Do not add authentication.
- Do not add features that were not requested.
- Keep the project simple and appropriate for a beginner CRUD assignment.
- The application should run locally using Maven.
- Make sure the project compiles and starts successfully.

After generating the project, tell me:

1. Which files you created.
2. How to run the application.
3. The Swagger UI URL.
4. Any implementation decisions you made that were not explicitly specified by me.
```

### What the AI Did Better

The AI organized the project more clearly than my first implementation. My version keeps most of the application logic inside one controller, while the AI separated the application into `controller`, `service`, `dto`, `model`, and `exception` packages. I understand why this is useful: the controller handles HTTP requests, the service contains the business logic, DTOs define the data accepted from clients, and the exception handler is responsible for consistent error responses.

The AI also handled task creation more safely. In my version, `POST /tasks` accepts a complete `Task` object directly. Because Jackson creates the object using the no-argument constructor, my automatic counter is not used when a task comes from JSON, so a created task can keep the default ID of `0`. A client could also send values such as `id` or `done` itself.

The AI instead accepts a `CreateTaskRequest` containing only `title`, then generates the ID inside the service and explicitly sets:

```java
done = false
```

This follows the API requirements more closely and prevents the client from controlling fields that should be controlled by the server.

Another improvement was partial updating. The AI uses an `UpdateTaskRequest` where `title` and `done` are nullable. This allows it to distinguish between:

```json
{
  "done": false
}
```

and a request where `done` was not provided at all.

My version accepts a full `Task` object and always executes:

```java
task.setTitle(updatedTask.getTitle());
task.setDone(updatedTask.isDone());
```

This means my code replaces both values instead of only the fields supplied by the client. Because `title` also has `@NotBlank`, a request that only tries to update `done` may fail validation.

The AI also has a `GlobalExceptionHandler` and returns errors in a consistent JSON format such as:

```json
{
  "error": "Task 99 not found"
}
```

My implementation uses `ResponseStatusException` for some errors and returns an empty `404` response from the delete endpoint, so the error response is less consistent.

### Concrete Differences I Found

1. **Project structure**

   My implementation contains most CRUD logic directly in `toDocontroller`, while the AI uses separate controller, service, DTO, model, and exception classes.

2. **Creating IDs**

   My `Task(String title)` constructor increments a static counter for the three initial tasks, but tasks received through `@RequestBody` use the empty constructor and do not automatically receive the next ID. The AI explicitly manages `nextId` inside `TaskService`.

3. **POST request fields**

   My endpoint accepts an entire `Task`, meaning the client can send fields such as `id` and `done`. The AI accepts only `title` through `CreateTaskRequest` and sets `done` to `false` itself.

4. **PUT behavior**

   My update method replaces both `title` and `done`. The AI supports changing only `title`, only `done`, or both.

5. **Error handling**

   The AI has custom exceptions and a global exception handler that consistently produces JSON error responses. My version handles errors directly in the controller and does not always return the requested JSON error format.

6. **GET /tasks mapping**

   In my uploaded version, the `getTasks()` method does not currently have a `@GetMapping("/tasks")` annotation. I also attempted to filter tasks with:

   ```java
   @GetMapping("/tasks?done={done}")
   ```

   using `@PathVariable`. Query parameters should instead be handled using something like `@RequestParam`. The AI has a normal `GET /tasks` endpoint mapped directly through its controller.

7. **Database dependency**

   My `pom.xml` contains an H2 dependency even though I am still storing tasks only in memory and the assignment does not require a database. The AI version does not include a database dependency.

### What the AI Got Wrong or Quietly Decided

I did not find a major CRUD requirement that the AI clearly ignored in the generated source code, but it made several decisions that I had not specified.

For example, the AI chose **Java 17**, while my hand-built project uses **Java 21**. I only said to use Java, so the AI had to choose the version itself.

The AI also automatically trims task titles:

```java
request.getTitle().trim()
```

I never asked it to modify whitespace in titles.

It chose the contents and completion states of the three example tasks itself. One of its initial tasks is already marked as completed.

It also interpreted "next available ID" as a continuously increasing counter. For example, if task `2` is deleted, the next task will still receive `4`, `5`, etc. My prompt did not define whether deleted IDs should ever be reused.

Finally, the AI chose a layered architecture even though I only requested a simple CRUD application. This was a reasonable decision, but it demonstrates that when a specification does not describe architecture, the AI makes that decision for the developer.

### What My Prompt Forgot to Specify

My prompt did not specify:

* Which Java version to use.
* Whether to use DTOs or accept `Task` directly.
* How the project should be divided into controller/service layers.
* Whether titles should be trimmed.
* The exact contents and `done` values of the three initial tasks.
* Exactly how IDs should behave after a task has been deleted.
* Whether there should be a maximum title length.
* Whether Maven Wrapper files such as `mvnw` should be included.

These were all details that the AI had to decide without asking me.

This showed me that even when the main functionality is clearly described, many implementation details are still left open unless the specification defines them.

### Rematch

For my second prompt, I would make the specification more precise by explicitly requiring **Java 21**, defining the ID-generation strategy, specifying the structure of create and update requests, defining whether titles should be trimmed, and specifying how the three initial tasks should be created.

After improving the prompt, the second version should require fewer assumptions from the AI and should match the intended behavior more closely.

## Assignment

**FlyRank Internship – Backend Track – Week 2**

**Assignment:** Build Your First CRUD API
