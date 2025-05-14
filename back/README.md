# Backend - Shop Application

This folder contains the backend of the Shop Application built with **Java** and **Spring Boot**. Below is a detailed overview of the project, including its tech stack, available controllers, commands to run the project, database recommendations, and other necessary information.

---

## 🛠️ Tech Stack

- **Programming Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven (using Maven Wrapper: `mvnw` / `mvnw.cmd`)
- **Security:** Spring Security with JWT (JSON Web Tokens)
- **Logging:** Configured with Logback
- **API Documentation:** OpenAPI (Swagger integration via `OpenApiConfig`)
- **Testing:** JUnit (located under `src/test`)

---

## Controllers

The project exposes several REST controllers for managing the shop functionalities:

- **CartController:** Handles shopping cart operations.
- **ProductController:** Manages product-related requests.
- **UserController:** Manages user authentication and registration.
- **WishListController:** Handles wish list operations.

---

## Commands to Run the Project

### Using Maven Wrapper

- **On Windows:**  
  Run the following command in the `back` directory:
  ```
  mvnw.cmd spring-boot:run
  ```

- **On Unix/Linux/Mac:**  
  Ensure the `mvnw` file has execution permissions and run:
  ```
  ./mvnw spring-boot:run
  ```

### Using Maven (if installed globally)

From the `back` directory, execute:
```
mvn spring-boot:run
```

### Building the Project

To build the project, run:
```
mvn clean package
```
---

## Database

The application is designed to work with a **relational database**. While the configuration in `application.properties` can be set up for various databases, the following recommendations apply:

Update the `application.properties` database configurations accordingly.

For example, you can run the PostgreSQL database using Docker with the following command:

```
docker run --name postgres-shop -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=shop -p 5432:5432 -d postgres
```

---

## Additional Information

- **Configuration Files:**  
  You can find application configurations in `src/main/resources/application.properties` and logging configurations in `src/main/resources/logback-spring.xml`.

- **Project Structure:**  
  - `src/main/java`: Contains the main application code.
  - `pom.xml`: Defines the project dependencies and build configurations.

- **Development Best Practices:**  
  Follow standard Spring Boot practices for coding, testing, and documentation. Ensure that any sensitive configuration (like database credentials) is managed securely, preferably using environment variables or secure vault solutions in production.

- **Swagger API:**  
  Access the API documentation at [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---
