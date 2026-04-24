# Project Code Walkthrough (Understanding Guide)

ឯកសារនេះសម្រាប់ **មើលឡើងវិញដើម្បីយល់ project**៖ structure, ដំណើរការ code, និង flow នៃ API។

> App run port: `8081` (មើល `src/main/resources/application.properties`)

---

## 1) Project Structure (Package Overview)

Path ជាទូទៅ៖ `src/main/java/com/springboot/relationship/`

- **`Entity/`**: JPA entities (tables) + relationships
- **`Repository/`**: Spring Data JPA repositories (DB access)
- **`Service/`**: interfaces (contract)
- **`Service/Implement/`**: business logic implementation
- **`Controller/`**: REST endpoints (รับ request → call service → return response)
- **`DTO/RequestDTO/`**: request payload models + validation annotations
- **`DTO/ResponeDTO/`**: response payload models
- **`Exception/`**: `ResourceNotFoundException` + `GlobalExceptionHandler`
- **`Configuration/`**: config utilities (ex: upload image)

---

## 2) Core Database Model (Entities & Relationships)

### 2.1 User
- Table: `users`
- Important columns: `username` (unique), `email` (unique), `password`
- Relationship:
  - **User (1) → (Many) Order**

### 2.2 Category
- Table: `categories` (used to group products)
- Relationship:
  - **Category (1) → (Many) Product**

### 2.3 Product
- Table: `products`
- Important columns: `name` (unique), `price`, `stock`, `imageUrl`, `description`
- Relationship:
  - **Product (1) → (Many) OrderItem**
  - **Product (Many) → (1) Category**

### 2.4 Order
- Table: `orders`
- Important columns: `status`, `totalAmount`, `date_time`
- Relationship:
  - **Order (Many) → (1) User**
  - **Order (1) → (Many) OrderItem** (`cascade=ALL`, `orphanRemoval=true`)
  - **Order (1) → (1) Payment** (nullable `payment_id`, payment create later)

### 2.5 OrderItem
- Table: `order_items`
- Stores: `quantity`, `price`, FK to `order_id`, `product_id`
- Relationship:
  - **OrderItem (Many) → (1) Order**
  - **OrderItem (Many) → (1) Product**

### 2.6 Payment
- Table: `payments`
- Stores: `totalamount`, `paymentMethod`, `status`, `paymentDate`
- Relationship:
  - Connected to **Order via `orders.payment_id`**
  - Meaning: Payment is created after order, then attached to order.

---

## 3) How a Request Moves Through the Code

Pattern:
1. **Controller** receives HTTP request
2. Validates request with `@Valid` + DTO annotations
3. Calls **Service** method
4. Service uses **Repository** to read/write DB
5. Service returns **ResponseDTO**
6. Controller returns `ResponseEntity`

If error:
- Validation error → `GlobalExceptionHandler.handleMethodArgumentNotValidException()` → `400`
- `IllegalArgumentException` → `400`
- `ResourceNotFoundException` → `404`
- DB constraint error (`DataIntegrityViolationException`) → `400` + `detail`
- Other exceptions → `500` + `detail`

---

## 4) Main Modules (What Each Controller Does)

### 4.1 Category Module
- Controller: `Controller/CategoryController.java`
- Typical endpoints:
  - `POST /api/v1/categories/create`
  - `GET /api/v1/categories/getall`
  - `GET /api/v1/categories/getbyid/{id}`
  - `PUT /api/v1/categories/update/{id}`
  - `DELETE /api/v1/categories/delete/{id}`

Why needed:
- Product creation requires `categoryId`.

### 4.2 Product Module
- Controller: `Controller/ProductController.java`
- Important:
  - Create/Update uses **`multipart/form-data`** because `ProductRequestDTO.imageUrl` is a file (`MultipartFile`)
  - Service uses `UploadImage` to save file and store filename in DB

Typical endpoints:
- `POST /api/v1/products/create` (multipart)
- `GET /api/v1/products/getall`
- `GET /api/v1/products/getbyid/{id}`
- `GET /api/v1/products/category/{categoryId}`
- `PUT /api/v1/products/update/{id}` (multipart)
- `DELETE /api/v1/products/delete/{id}`

### 4.3 User Module
- Controller: `Controller/UserController.java`
- Highlights:
  - Repository has `existsByUsername`, `existsByEmail` to prevent duplicates

### 4.4 Order Module
- Controller: `Controller/OrderController.java`
- Service: `Service/Implement/OrderServiceImplement.java`

What happens in `createOrder()`:
- Find `User` by `userId` (required)
- For each item:
  - Find `Product` by `productId`
  - Check stock
  - Create `OrderItem` and add into `order.getOrderItems()` (important with orphanRemoval)
  - Calculate `totalAmount`
- Save `Order` once (cascade saves items)

Update order:
- Deletes old items (by `orderId`) then rebuilds new items
- Recalculates `totalAmount`

### 4.5 OrderItem Module (Nested under Order)
- Controller: `Controller/OrderItemController.java`
- Base path: `/api/v1/orders/{orderId}/items`
- Keeps `order.totalAmount` consistent when add/update/delete items

### 4.6 Payment Module
- Controller: `Controller/PaymentController.java`
- Service: `Service/Implement/PaymentServiceImplement.java`

Rules in `createPayment()`:
- Find Order by `orderId`
- `payment.totalamount` must equal `order.totalAmount`
- Create Payment and **attach it to Order** (`order.setPayment(payment)`) then save order

---

## 5) End-to-End Flow (Recommended)

1. **Create Category** → get `categoryId`
2. **Create Product** (multipart) → get `productId`
3. **Create User** → get `userId`
4. **Create Order** (`userId` + `productId`) → get `orderId` + `totalAmount`
5. (Optional) add/update/delete order items → `totalAmount` changes
6. **Create Payment** (`orderId` + `totalamount == order.totalAmount`)

See also:
- `POSTMAN_TESTING_GUIDE.md`
- `PROJECT_FLOW.md`

---

## 6) Common Issues (What They Mean)

- **`Order items cannot be empty`**
  - `orderItemRequestDTOS` must be a non-empty array.
- **`Product not found` / `User not found`**
  - wrong id, or you didn’t create data yet.
- **`Invalid request data violates database constraints`**
  - unique / FK / not-null / length constraints in DB.
- **500 error**
  - check response `detail` field (enabled in `GlobalExceptionHandler`).

