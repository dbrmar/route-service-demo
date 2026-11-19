# Learning project: Spring Boot service on Cloud Run

Goal: I have a Java/Spring interview in about a day. This is a learning project,
so explain in plain full sentences and keep it short.

Stack: Java 21, Spring Boot 4.1.x, Gradle (Groovy), Spring Web (webmvc), Validation,
Spring Data JPA, H2, Actuator, JUnit 5 and Mockito, Docker, GitHub Actions, Google
Cloud Run. I use Windows and PowerShell. Package com.example.route, application
class RouteServiceDemoApplication.

Rules:
- Never claim something works without running it. Show me the real output, and if
  a command fails, show me the error and explain it.
- Do not add libraries, features or files I have not asked for.
- Never run gcloud commands, never touch cloud credentials, and never delete
  anything outside this project folder.
- Use constructor injection, records for DTOs, and ProblemDetail for errors.
- After finishing a task, explain each file you wrote in plain full sentences,
  then quiz me with five short questions, one at a time, and tell me what my
  answers missed.

Topics I must be able to explain: @Transactional and propagation, how Hibernate
tracks changes, how a query is made in JPA, constructor injection, bean scopes,
PUT versus PATCH, what a Dockerfile does, what a CI pipeline does, what Cloud Run
is and what a service account is.
