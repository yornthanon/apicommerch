# Postman Testing Guide (Spring Boot E-commerch)

ឯកសារនេះជាគន្លឹះសម្រាប់ **test API** ក្នុង Postman សម្រាប់ module:
- Users
- Orders
- Order Items (nested under Order)
- Payments

> Base path ក្នុង code: `/api/v1/...`

---

## 1) Setup ក្នុង Postman

### 1.1 Create Environment
បង្កើត Environment ឈ្មោះ `local` ហើយដាក់ variables:
- `baseUrl` = `http://localhost:8081`
  - Note: project នេះ config `server.port=8081` ក្នុង `application.properties` (default)

### 1.2 Headers
សម្រាប់ request ដែលមាន body:
- `Content-Type: application/json`

---

## 2) Test Flow (សំខាន់)

លំដាប់ test ដែលណែនាំ:
- **Create Category** (ទទួលបាន `categoryId`)
- **Create Product** (multipart/form-data + image file) (ទទួលបាន `productId`)
- **Create User** (ទទួលបាន `userId`)
- **Create Order** (ប្រើ `userId` + `productId`)
- (Optional) Add/Update/Delete OrderItem
- Create Payment (amount ត្រូវស្មើ order `totalAmount`)

---

## 3) Categories API

Base: `{{baseUrl}}/api/v1/categories`

### 3.1 Create Category
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/categories/create`
- **Body (raw JSON)** *(example)*:

```json
{
  "name": "Electronics"
}
```

- **Expected**: `200 OK` (Response មាន `id`)

### 3.2 Get All Categories
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/categories/getall`
- **Expected**: `200 OK` (List)

### 3.3 Get Category By Id
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/categories/getbyid/{id}`

### 3.4 Update Category
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/categories/update/{id}`
- **Body (raw JSON)**:

```json
{
  "name": "Electronics Updated"
}
```

### 3.5 Delete Category
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/categories/delete/{id}`
- **Expected**: `200 OK` (String message)

---

## 4) Products API

Base: `{{baseUrl}}/api/v1/products`

> Create/Update product ប្រើ **`multipart/form-data`** (មិនមែន raw JSON) ព្រោះ `imageUrl` ជា file (`MultipartFile`).

### 4.1 Create Product (form-data)
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/products/create`
- **Body**: `form-data`
  - `name` (text): `iPhone 15`
  - `price` (text): `999.99`
  - `description` (text): `New model`
  - `stock` (text): `10`
  - `categoryId` (text): `1`
  - `imageUrl` (file): *(select an image file เช่น `product.jpg`)*
- **Expected**: `200 OK` (Response មាន `id` និង `imageUrl`)

### 4.2 Get All Products
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/products/getall`

### 4.3 Get Product By Id
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/products/getbyid/{id}`

### 4.4 Get Products By Category
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/products/category/{categoryId}`

### 4.5 Update Product (form-data)
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/products/update/{id}`
- **Body**: `form-data` (fields ដូច create)

### 4.6 Delete Product
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/products/delete/{id}`
- **Expected**: `204 No Content`

---

## 5) Users API

Base: `{{baseUrl}}/api/v1/users`

### 2.1 Create User
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/users/create`
- **Body (raw JSON)**:

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "123456"
}
```

- **Expected**:
  - `200 OK`
  - Response មាន `id`, `username`, `email`

### 2.2 Get All Users
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/users/getall`
- **Expected**: `200 OK` (List)

### 2.3 Get User By Id
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/users/getbyid/{id}`
- **Example**: `{{baseUrl}}/api/v1/users/getbyid/1`
- **Expected**: `200 OK`
- **Not found**: throws `ResourceNotFoundException`

### 2.4 Update User
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/users/update/{id}`
- **Body**:

```json
{
  "username": "john_updated",
  "email": "john_updated@example.com",
  "password": "123456"
}
```

- **Expected**: `200 OK`

### 2.5 Delete User
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/users/delete/{id}`
- **Expected**: `200 OK` (String message)

---

## 3) Orders API

Base: `{{baseUrl}}/api/v1/orders`

> Order create/update ត្រូវការ `userId` និង `orderItemRequestDTOS` (list)

### 3.1 Create Order
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/orders/create`
- **Body**:

```json
{
  "userId": 1,
  "orderItemRequestDTOS": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```

- **Expected**:
  - `200 OK`
  - Response មាន `id`, `dateTime`, `status`, `totalAmount`, `items[]`

### 3.2 Get All Orders
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/orders/getall`
- **Expected**: `200 OK` (List)

### 3.3 Get Order By Id
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/orders/getbyid/{id}`
- **Example**: `{{baseUrl}}/api/v1/orders/getbyid/1`
- **Expected**: `200 OK`

### 3.4 Update Order
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/orders/update/{id}`
- **Body**:

```json
{
  "userId": 1,
  "orderItemRequestDTOS": [
    { "productId": 1, "quantity": 3 }
  ]
}
```

- **Expected**: `200 OK`

### 3.5 Delete Order
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/orders/delete/{id}`
- **Expected**: `204 No Content`

---

## 4) Order Items API (Nested)

Base: `{{baseUrl}}/api/v1/orders/{orderId}/items`

### 4.1 Add Item To Order
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/orders/{orderId}/items/add`
- **Example**: `{{baseUrl}}/api/v1/orders/1/items/add`
- **Body**:

```json
{
  "productId": 1,
  "quantity": 1
}
```

- **Expected**: `200 OK`

### 4.2 Update Order Item
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/orders/{orderId}/items/update/{itemId}`
- **Example**: `{{baseUrl}}/api/v1/orders/1/items/update/10`
- **Body**:

```json
{
  "productId": 1,
  "quantity": 2
}
```

- **Expected**: `200 OK`

### 4.3 Delete Order Item
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/orders/{orderId}/items/delete/{itemId}`
- **Example**: `{{baseUrl}}/api/v1/orders/1/items/delete/10`
- **Expected**: `204 No Content`

---

## 5) Payments API

Base: `{{baseUrl}}/api/v1/payments`

> Payment create ត្រូវការ `orderId`, `totalamount`, `paymentMethod`  
> Note: code validate ថា `totalamount` **ត្រូវស្មើ** `order.totalAmount`

### 5.1 Create Payment
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/payments/create`
- **Body**:

```json
{
  "orderId": 1,
  "totalamount": 100.00,
  "paymentMethod": "CASH"
}
```

- **Expected**: `200 OK`
- **If amount mismatch**: `400` (IllegalArgumentException)

### 5.2 Get All Payments
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/payments/getall`
- **Expected**: `200 OK` (List)

### 5.3 Get Payment By Id
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/payments/getbyid/{id}`
- **Example**: `{{baseUrl}}/api/v1/payments/getbyid/1`
- **Expected**: `200 OK`

### 5.4 Update Payment Status (Query Param)
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/payments/update-status/{id}?status=COMPLETED`
- **Valid values**: `PENDING` | `COMPLETED` | `FAILED`
- **Expected**: `200 OK`

### 5.5 Delete Payment
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/payments/delete/{id}`
- **Expected**: `204 No Content`

---

## 6) Common Errors (សំខាន់)

### 6.1 405 Method Not Allowed
មើលថា:
- Method ត្រូវ (POST/GET/PUT/DELETE)
- URL path ត្រូវ (ex: `/create`, `/getall`, `/getbyid/{id}`, ...)

### 6.2 400 Bad Request (Validation)
កើតឡើងពេល:
- JSON field ខ្វះ (`@NotNull`, `@NotEmpty`, `@NotBlank`)
- format email មិនត្រឹមត្រូវ
- `quantity < 1`
- `totalamount <= 0`

### 6.3 404 Not Found (ResourceNotFoundException)
កើតឡើងពេល:
- user/order/product/payment id មិនមាន

