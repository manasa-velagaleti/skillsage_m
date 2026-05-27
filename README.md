# SkillSage

SkillSage is an employee skill management web application built with Spring Boot. It helps teams record employee skills, proficiency levels, experience, and run basic skill-analysis reports.

## Key features
- Employee registration and secure login (Spring Security, BCrypt)
- Admin dashboard to manage users and view organization-wide skill analysis
- Add / update employee skills and proficiencies
- Search and filter skills
- Simple email/OTP flows for registration and password reset

## Architecture overview
SkillSage follows a classic layered Spring Boot architecture:

- Presentation: Thymeleaf templates and static assets (src/main/resources/templates and static/images). Controller classes handle web requests and return views.
- Service: Business logic (services under com.example.service and com.example.logservice).
- Persistence: Spring Data JPA repositories (com.example.logrepo) and Hibernate with MySQL. Entities: Login_det, Skills, EmployeeSkill.
- Security: Spring Security with a custom UserDetailsService (CustomUserDetailsService) and BCrypt password encoding.

Sequence at startup
1. Application starts (EmployeeApplication)
2. Spring Data repositories and JPA initialize
3. Hibernate creates/updates schema (spring.jpa.hibernate.ddl-auto=update)
4. Application serves web UI on configured port (default 8001)

## Tech stack
- Java 17
- Spring Boot 3.2 (Spring Data JPA, Spring Security, Thymeleaf)
- MySQL
- Maven (wrapper included)

## Prerequisites
- JDK 17
- Maven (or use the bundled ./mvnw)
- MySQL (or run via Docker)
- Port 8001 must be free

## Configuration
Edit `src/main/resources/application.properties` to set datasource and mail properties. Defaults used in this repo:
```
spring.datasource.url=jdbc:mysql://localhost:3306/skillsage
spring.datasource.username=root
spring.datasource.password=root (change this)
server.port=8001
```

## Run (local)
1. Create database if missing: `CREATE DATABASE skillsage;`
2. Build: `./mvnw -DskipTests package`
3. Run: `java -jar target/your-application.jar`
4. Open: `http://localhost:8001`

Alternative: use Docker Compose (mysql + app). See `docker-compose.yaml` and `Dockerfile`.

## Database schema (high level)
- login_details (empid PK, email UNIQUE, password (BCrypt), role, fullname, ...)
- skills (skillid PK, domain, skillname, subdomain)
- employee_skill (composite PK empid+skillid, proficiency, exp, cert_source)

## Admin account (created during this session)
- empid: `67890`
- password: `567890`

(Password stored hashed with BCrypt in DB.)

## Screenshots
Placeholders below reference images included in the repository (images/ and static/images/):

- Admin dashboard
  ![Admin Page](images/AdminPage.png)
- Employee dashboard
  ![Employee Home](images/EmployeeHome.png)
- Skill analysis
  ![Skill Analysis](images/Skill Analysis.png)
- Search UI
  ![Search](images/search.png)
- Update skill UI
  ![Update](images/Update.png)

## Security notes
- Change database root password after testing and avoid using root in production.
- Revoke any GitHub PATs used during this session.
- Consider moving credentials to environment variables or a secrets manager.

## Contribution
Contributions welcome. Suggested next improvements:
- Add API endpoints with OpenAPI docs
- Replace JSP/Thymeleaf templates with a SPA front-end (React/Vue)
- Add automated tests and CI pipeline

## License
Add a license file (e.g., MIT) if you plan to open source this project.

---

If you'd like, I can:
- Rename images to remove spaces and update README links
- Commit and push README to the remote repo
- Generate API endpoint docs and an ER diagram
Tell me which of the above to do next.