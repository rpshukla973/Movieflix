---
name: instruction
description: Describe when to use this prompt
---

You are agent, build a full-stack Netflix-like web application name will be Movieflix with the following requirements:

TECH STACK:

- Frontend: Vue.js (latest version, use Vue 3 + Composition API)
- Backend: Spring Boot (Java, REST API architecture)
- Database: MySQL
- Architecture: Microfrontend architecture for frontend and modular microservices-inspired backend
- Authentication: JWT-based authentication with refresh tokens
- Email Service: OTP verification via email for signup and login
- Video Streaming: Use efficient streaming (HLS or progressive streaming)
- Storage: Local storage or cloud storage (configurable)

FEATURES:

1. USER AUTHENTICATION:

- User Signup with email OTP verification
- User Login with JWT + refresh token
- Password encryption using BCrypt
- Input validation (frontend + backend)
- Forgot password with OTP reset
- Role-based access (USER, ADMIN)

2. ADMIN PANEL:

- Secure admin login
- Upload videos (with metadata: title, genre, description, thumbnail)
- Manage users (activate/deactivate)
- Manage subscriptions (plans, pricing)
- Dashboard analytics (basic stats)

3. USER FEATURES:

- Profile creation (multiple profiles like Netflix)
- Browse homepage (categories like Trending, Popular, Recommended)
- Search functionality
- Video player with streaming support
- Watch history and continue watching
- Subscription purchase and validation

4. VIDEO MANAGEMENT:

- Video upload API (admin only)
- Store metadata in MySQL
- Stream videos efficiently using backend endpoints
- Thumbnail support

5. SUBSCRIPTION SYSTEM:

- Plans (Basic, Standard, Premium)
- Middleware to restrict content based on subscription
- Expiry and renewal logic

6. FRONTEND STRUCTURE (MICROFRONTEND):

- Split into modules:
  - Auth Module
  - Home Module
  - Player Module
  - Admin Module
- Use Vue Router
- State management (Pinia)
- Reusable components (Navbar, Cards, Modals)

7. BACKEND STRUCTURE:

- Layered architecture:
  - Controller
  - Service
  - Repository
  - DTO
  - Security
- Use Spring Security with JWT filters
- Email service for OTP
- Exception handling (global handler)
- Validation using annotations

8. DATABASE DESIGN:

- Tables:
  - Users
  - Roles
  - Profiles
  - Videos
  - Categories
  - Subscriptions
  - WatchHistory
  - OTP_Verification
  - Payments Table (id, user_id, subscription_id, amount, payment_status, payment_method, transaction_id, created_at)
- Proper relationships (OneToMany, ManyToMany)

9. SECURITY:

- JWT authentication & authorization
- CSRF protection (if needed)
- Rate limiting for login attempts
- Secure password hashing
- Input sanitization

10. OPTIMIZATION:

- Lazy loading in frontend
- Pagination for video lists
- Caching (Spring Cache or Redis optional)
- Efficient SQL queries with indexing

11. UI/UX:

- Netflix-like UI (dark theme)
- Responsive design
- Smooth transitions and animations

12. DEVOPS:

- Docker setup for frontend, backend, and MySQL
- Environment-based configurations
- API documentation (Swagger)

13. PAYMENT INTEGRATION:

- Integrate secure payment gateway (e.g., Stripe, Razorpay, or PayPal)
- Allow users to purchase subscription plans (Basic, Standard, Premium)
- Payment flow:
  - Select plan → Proceed to payment → Confirm payment → Activate subscription
- Store transaction details in database
- Handle payment success, failure, and pending states
- Webhook support for payment confirmation
- Generate invoices/receipts for users
- Prevent unauthorized access without active subscription
- Retry mechanism for failed payments
- Secure APIs for payment processing
- Support test/sandbox mode for development

OUTPUT REQUIREMENTS:

- Provide full project structure (frontend + backend)

- Include key code snippets for:

  - Authentication (JWT + OTP)

  - Video upload and streaming

  - Subscription middleware

- Ensure clean, maintainable, and scalable code

- Use best practices and comments where necessary

 

IMPORTANT:

- Code should be production-ready

- Follow clean architecture principles

- Avoid hardcoding values, use environment configs
