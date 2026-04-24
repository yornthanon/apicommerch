# ការពន្យល់ Assignment: Spring Security (Form Login)

ឯកសារនេះពន្យល់តាម requirement ក្នុង assignment របស់អ្នក៖
- ប្រើ **Spring Security + Form Login**
- បែងចែក API ជា **Public** និង **Private**
- បង្កើត **Custom Filter** សម្រាប់ log request
- យល់ពី **Session** និង **Authentication**

## 1) គោលបំណង

អ្នកត្រូវរៀបចំប្រព័ន្ធ login ដោយប្រើ Spring Security ដែលមានលក្ខណៈ៖
1. អ្នកប្រើមិនទាន់ login អាចចូលបានតែ endpoint public
2. endpoint private ត្រូវការ authentication
3. រាល់ request ត្រូវបាន audit/log ដើម្បីដឹងថា user ជានរណា និងស្ថានភាព login

## 2) Endpoint និង Access Rule

- `/home` → **Public** (ចូលបានដោយមិនបាច់ login)
- `/dashboard` → **Private** (ត្រូវ login មុន)
- `/profile` → **Private** (ត្រូវ login មុន)

ន័យសាមញ្ញ៖
- Public = អ្នកណាក៏អាច request បាន
- Private = តែ user ដែល authenticate រួច

## 3) Form Login ដំណើរការ

Flow សង្ខេប៖
1. User ចូល endpoint private ដូចជា `/dashboard`
2. បើមិនទាន់ authenticate → Spring Security បញ្ជូនទៅ login page
3. User បញ្ចូល username/password
4. បើត្រឹមត្រូវ → បង្កើត authentication ក្នុង SecurityContext និងភ្ជាប់ជាមួយ session
5. បន្ទាប់មក user អាចចូល endpoint private បាន

## 4) Session និង Authentication

ពាក្យសំខាន់ៗ៖
- **Authentication**: ព័ត៌មានអំពី user ដែល login ជោគជ័យ (username, roles, authorities)
- **SecurityContext**: កន្លែងដែល Spring Security រក្សាទុក Authentication សម្រាប់ request បច្ចុប្បន្ន
- **HttpSession**: session នៅ server ដែលជួយរក្សាស្ថានភាព login រវាង requests

ពេល login រួច៖
- request បន្ទាប់ៗ Spring អាចស្គាល់ថា user ដដែលបាន authenticate ហើយ
- ដូច្នេះមិនចាំបាច់ login រាល់ request

## 5) Custom Filter: `RequestAuditFilter`

បង្កើត filter ដើម្បី log ព័ត៌មានសំខាន់ៗក្នុង request នីមួយៗ៖
- URL path
- HTTP method
- Session ID (បើមាន)
- Username (បើ login)
- Roles / Authorities
- Login status (authenticated ឬ anonymous)

គោលបំណង៖
- ងាយ debug
- ងាយតាមដានសុវត្ថិភាព
- អាចយក log ទៅវិភាគពេលមាន issue

## 6) ទិន្នន័យ Log ដែលគួរមាន (ឧទាហរណ៍)

```text
[AUDIT] method=GET path=/dashboard session=ABC123 user=student1 roles=[ROLE_USER] authenticated=true
[AUDIT] method=GET path=/home session=null user=anonymous roles=[] authenticated=false
```

## 7) ឧទាហរណ៍ Testing Flow

1. បើក `/home` → ចូលបានភ្លាម
2. បើក `/dashboard` មុន login → ត្រូវ redirect ទៅ login
3. login ជោគជ័យ
4. បើក `/dashboard` និង `/profile` → ចូលបាន
5. ពិនិត្យ console/log ថា `RequestAuditFilter` បានបង្ហាញ field ទាំងអស់

## 8) Checklist មុនដាក់ Assignment

- [ ] មាន `SecurityFilterChain` កំណត់ public/private ត្រឹមត្រូវ
- [ ] បើក form login និង login/logout ដំណើរការ
- [ ] បង្កើត `RequestAuditFilter` ហើយ register ក្នុង security chain
- [ ] Log បង្ហាញ path, method, session, username, roles, login status
- [ ] សាកល្បង endpoint ទាំង 3 (`/home`, `/dashboard`, `/profile`) តាម flow ខាងលើ

---

បើអ្នកចង់ ខ្ញុំអាចបង្កើតជំហានបន្ទាប់អោយបានភ្លាម៖
1) `SecurityConfig` sample
2) `RequestAuditFilter` class ពេញ
3) `Controller` endpoints សាកល្បង (`/home`, `/dashboard`, `/profile`)
