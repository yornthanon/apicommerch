# Project Flow (From Start to Finish)

Flow នេះបង្ហាញលំដាប់ការប្រើ API ពីដំបូងដល់ចប់ ដើម្បីអាច test បាន end-to-end (Postman)។

Base URL: `http://localhost:8081`  
API Prefix: `/api/v1`

---

## 0) Start Application

- Run Spring Boot application
- បញ្ជាក់ DB connect បាន (tables/DDL បង្កើតបាន)

---

## 1) Create Category (ដំបូង)

គោលបំណង: ទទួលបាន `categoryId` ដើម្បីប្រើពេល create product

- **POST** `/api/v1/categories/create`
- **Body (JSON)** *(example)*:

```json
{
  "name": "Electronics"
}
```

- **Save**: `categoryId` (ពី response)

---

## 2) Create Product (ត្រូវមាន Category មុន)

គោលបំណង: ទទួលបាន `productId` ដើម្បីប្រើពេល create order/order-items

សំខាន់: endpoint នេះប្រើ **multipart/form-data** ព្រោះ `imageUrl` ជា file (`MultipartFile`)

- **POST** `/api/v1/products/create`
- **Body**: `form-data`
  - `name` (text): `iPhone 15`
  - `price` (text): `999.99`
  - `description` (text): `New model`
  - `stock` (text): `10`
  - `categoryId` (text): *(ដាក់ `categoryId` ពី Step 1)*
  - `imageUrl` (file): *(ជ្រើសរូបភាព `jpg/png`)*

- **Save**: `productId` (ពី response)

> Tip: បង្កើត product ច្រើនបាន (productId 1, 2, 3...) ដើម្បី test order មាន items ច្រើន។

---

## 3) Create User

គោលបំណង: ទទួលបាន `userId` ដើម្បីប្រើពេល create order

- **POST** `/api/v1/users/create`
- **Body (JSON)** *(example)*:

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "123456"
}
```

- **Save**: `userId` (ពី response)

---

## 4) Create Order (User + Products)

គោលបំណង: បង្កើត order និង order items, ហើយគណនា `totalAmount`

- **POST** `/api/v1/orders/create`
- **Body (JSON)** *(example)*:

```json
{
  "userId": 1,
  "orderItemRequestDTOS": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```

- **Save**: `orderId` និង `totalAmount` (ពី response)
- **Note**:
  - ប្រសិនបើ `product.stock < quantity` → error “Not enough stock”
  - `status` default = `PENDING`

---

## 5) Manage Order Items (Optional)

Base nested path: `/api/v1/orders/{orderId}/items`

### 5.1 Add Item To Order
- **POST** `/api/v1/orders/{orderId}/items/add`
- **Body (JSON)**:

```json
{
  "productId": 1,
  "quantity": 1
}
```

### 5.2 Update Order Item
- **PUT** `/api/v1/orders/{orderId}/items/update/{itemId}`
- **Body (JSON)**:

```json
{
  "productId": 1,
  "quantity": 2
}
```

### 5.3 Delete Order Item
- **DELETE** `/api/v1/orders/{orderId}/items/delete/{itemId}`

> Actions ទាំងនេះនឹងធ្វើឲ្យ `order.totalAmount` ប្រែប្រួល។

---

## 6) Create Payment (Final Step)

គោលបំណង: payment សម្រាប់ order

សំខាន់: `totalamount` **ត្រូវស្មើ** `order.totalAmount`

- **POST** `/api/v1/payments/create`
- **Body (JSON)** *(example)*:

```json
{
  "orderId": 1,
  "totalamount": 100.00,
  "paymentMethod": "CASH"
}
```

- **Result**:
  - Payment created
  - Status default = `COMPLETED`

---

## 7) Verify Data (Quick Checks)

- Categories: **GET** `/api/v1/categories/getall`
- Products: **GET** `/api/v1/products/getall`
- Users: **GET** `/api/v1/users/getall`
- Orders: **GET** `/api/v1/orders/getall` ឬ `/api/v1/orders/getbyid/{id}`
- Payments: **GET** `/api/v1/payments/getall`

---

## Common Blockers

- **Product not found**: ត្រូវ create product មុន (Step 2)
- **Category not found**: ត្រូវ create category មុន (Step 1)
- **405 Method Not Allowed**: method/URL មិនត្រូវ
- **400 Validation**: JSON/form-data ខ្វះ field required

