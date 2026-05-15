# Movieflix

Full-stack Netflix-like web application scaffold with Vue 3 frontend modules and Spring Boot backend modules.

## Tech Stack

- Frontend: Vue 3 (Composition API), Vue Router, Pinia
- Backend: Spring Boot 3 (REST, layered architecture)
- Database: MySQL 8
- Auth: JWT access token + refresh token, OTP verification
- Email: SMTP integration (MailHog for local)
- Video: Upload + streaming endpoint
- Payments: Subscription-linked payment workflow + webhook handling
- DevOps: Docker Compose, environment-driven config, Swagger

## Project Structure

```
.
├── backend
│   ├── .gitignore
│   ├── build.gradle
│   ├── Dockerfile
│   ├── gradle/
│   ├── gradlew
│   ├── gradlew.bat
│   ├── settings.gradle
│   └── src/main
│       ├── java/com/movieflix
│       │   ├── config
│       │   ├── controller
│       │   ├── dto
│       │   ├── entity
│       │   ├── exception
│       │   ├── repository
│       │   ├── security
│       │   ├── service
│       │   └── MovieflixApplication.java
│       └── resources/application.yml
├── frontend
│   ├── Dockerfile
│   ├── package.json
│   ├── vite.config.js
│   └── src
│       ├── components
│       ├── modules
│       │   ├── admin
│       │   ├── auth
│       │   ├── home
│       │   └── player
│       ├── router
│       ├── services
│       ├── stores
│       └── App.vue
├── docker-compose.yml
└── README.md
```

## Features Implemented in Scaffold

- OTP-based signup and password reset flow
- OTP-based login flow
- JWT login with refresh token endpoint
- BCrypt password encryption and validation annotations
- Role-aware route protection (USER/ADMIN patterns)
- Admin video upload (metadata + file + thumbnail)
- Video browsing and HTTP byte-range streaming endpoint
- Profile management APIs
- Watch history upsert and pagination APIs
- Subscription create/validate/activate flow
- Payment record model + create endpoint + webhook transition
- Global exception handling
- Swagger/OpenAPI UI
- Rate limiting filter for login attempts

## Key Code Snippets

### 1) Authentication (JWT + OTP)

`AuthController` and `AuthServiceImpl` expose:

- `POST /api/auth/otp`
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `POST /api/auth/login/otp`
- `POST /api/auth/refresh`
- `POST /api/auth/forgot-password/reset`

Core files:

- `backend/src/main/java/com/movieflix/controller/AuthController.java`
- `backend/src/main/java/com/movieflix/service/impl/AuthServiceImpl.java`
- `backend/src/main/java/com/movieflix/service/impl/OtpServiceImpl.java`
- `backend/src/main/java/com/movieflix/security/JwtTokenProvider.java`

### 2) Video Upload + Streaming

`VideoController`:

- `POST /api/videos/upload` (ADMIN only, multipart upload)
- `GET /api/videos/stream/{id}` (subscription-protected stream endpoint with `Range` support)

Core files:

- `backend/src/main/java/com/movieflix/controller/VideoController.java`
- `backend/src/main/java/com/movieflix/service/impl/LocalStorageService.java`
- `backend/src/main/java/com/movieflix/service/impl/VideoServiceImpl.java`

### 3) Subscription Middleware / Guard

`SubscriptionGuard` enforces plan restrictions before stream access.

Core files:

- `backend/src/main/java/com/movieflix/security/SubscriptionGuard.java`
- `backend/src/main/java/com/movieflix/service/impl/SubscriptionServiceImpl.java`

### 4) Profiles + Watch History

Profile endpoints:

- `POST /api/profiles`
- `GET /api/profiles`
- `DELETE /api/profiles/{profileId}`

Watch history endpoints:

- `POST /api/watch-history`
- `GET /api/watch-history?profileId=...&page=0&size=20`

## Local Run

```bash
docker compose up --build
```

Backend only:

```bash
cd backend
./gradlew bootRun
```

Windows PowerShell:

```powershell
cd backend
.\gradlew.bat bootRun
```

Endpoints:

- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- MailHog UI: http://localhost:8025

## Environment Configuration

Backend supports environment-based configs:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_ACCESS_TOKEN_TTL_MS`
- `STORAGE_PROVIDER`
- `STORAGE_LOCAL_PATH`
- `MAIL_HOST`
- `MAIL_PORT`

Frontend supports:

- `VITE_API_BASE_URL`

## Notes

- Payment gateway integration is scaffolded for secure provider implementation (Stripe/Razorpay/PayPal adapters can be added in `PaymentService`).
- For production, add provider signature validation in webhook endpoint and migrate to HLS transcoding pipeline for adaptive streaming.
