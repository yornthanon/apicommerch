# Spring Security Project - API Security & Audit Logging

## 1. Spring Security Setup
- Enable Spring Security with form login.
- Separate API endpoints into public and private.
- Add a custom filter to log request information (RequestAuditFilter).
- Track session and authentication details.

## 2. Endpoints
- `/home` — **Public** (accessible without login)
- `/dashboard` — **Private** (requires login)
- `/profile` — **Private** (requires login)

## 3. Custom RequestAuditFilter
Logs the following for each request:
- URL path
- HTTP Method
- Session ID (if available)
- Username (if logged in)
- Roles / Authorities
- Whether the user is authenticated

## Implementation Steps
1. **Configure Security**
   - Use `SecurityConfiguration.java` to define endpoint access rules and add filters.
2. **Create Endpoints**
   - Implement `/home`, `/dashboard`, `/profile` in a controller.
3. **Add RequestAuditFilter**
   - Log all required request/session/authentication info for auditing.

---

**Note:**
- `/home` is public, `/dashboard` and `/profile` are private.
- All access and authentication events are logged for auditing and debugging.

