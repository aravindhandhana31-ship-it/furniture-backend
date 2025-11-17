# Furniture E-Commerce Backend (Spring Boot)

This is the backend service for the Furniture E-Commerce application.  
It handles user authentication, product management, category management, and order processing.

This backend is built using **Spring Boot**, **MySQL**, **JWT Authentication**, and follows REST API standards.

---

# 🌐 Live Backend URL
```
https://furniture-backend-m441.onrender.com
```

Use this as the base URL for all API calls.

Example:
```
https://furniture-backend-m441.onrender.com/api/products
```

---

# 📌 Features

- User signup & login (JWT Token)
- Role-based access (USER / ADMIN)
- Category management
- Product management
- Order creation & tracking
- Image upload support
- Secure APIs using Spring Security
- CORS enabled for frontend

---

# 🛠️ Technologies Used

- Java 17  
- Spring Boot 3  
- Spring Security + JWT  
- Spring Data JPA  
- MySQL  
- Maven  

---

# 🚀 How to Run the Project

## 1. Create MySQL Database
```
CREATE DATABASE furnituredb;
```

## 2. Configure MySQL Credentials  
In `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/furnituredb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

## 3. Install dependencies and run
```
mvn spring-boot:run
```

Backend will start on:
```
http://localhost:8080
```

---

# 📘 API Documentation

All APIs follow this base path:
```
/api
```

Below is a complete list of available endpoints.

---

# 🔐 1. Authentication APIs (`/api/auth`)

### ✅ POST `/signup` – Register a new user  
**Request Body**
```json
{
  "name": "John",
  "email": "john@example.com",
  "password": "123456",
  "confirmPassword": "123456",
  "role": ["user"]
}
```

### ✅ POST `/signin` – Login and get JWT token  
**Request Body**
```json
{
  "email": "john@example.com",
  "password": "123456"
}
```

**Response contains**
- token  
- user id  
- name  
- email  
- roles  

---

# 📂 2. Category APIs (`/api/categories`)

### ✅ GET `/` – Get all categories

### ✅ POST `/` – Create category (Admin only)  
**Form-Data**
- name  
- description  
- image (optional file)

### ❌ DELETE `/{id}` – Delete a category (Admin only)

### 📊 GET `/product-count` – Get how many products are in each category

---

# 📦 3. Product APIs (`/api/products`)

### ✅ GET `/` – Get all products

### ✅ GET `/{id}` – Get product by ID

### ✅ GET `/category/{categoryId}` – Get products by category

### ✅ POST `/` – Create product (Admin)  
**Form-Data**
- name
- price
- categoryId
- image (optional)

### ✏️ PUT `/{id}` – Update product (Admin)

### ❌ DELETE `/{id}` – Delete product (Admin)

---

# 🛒 4. Order APIs (`/api/orders`)

### 📝 POST `/create` – Create order  
**Request Body**
```json
{
  "userEmail": "user@example.com",
  "orderStatus": "Processing",
  "paymentStatus": "Success",
  "shippingAddress": "Some address",
  "phoneNumber": "9876543210",
  "items": [
    { "productId": 1, "quantity": 2, "price": 1200 }
  ]
}
```

### 📦 GET `/` – Get all orders (Admin)

### 📬 GET `/user/{email}` – Get orders by user email

### 🔍 GET `/{id}` – Get order details

### 🔄 PUT `/{id}/status?status=Shipped` – Update order status (Admin)

### ❌ DELETE `/{id}` – Delete an order (Admin)

---

# 🔒 Security Access Levels

| Endpoint | Access |
|---------|--------|
| `/api/auth/**` | Public |
| `GET /api/products/**` | Public |
| `GET /api/categories/**` | Public |
| `/api/orders/**` | USER + ADMIN |
| `POST /api/**` | ADMIN |
| `PUT /api/**` | ADMIN |
| `DELETE /api/**` | ADMIN |

---

# 🖼️ Image Uploads

Uploaded product & category images are stored in:
```
/uploads/images/
```

You can return the image using:
```
BASE_URL/uploads/images/{filename}
```

---

# 🌍 CORS Settings

These domains are allowed:
- http://localhost:3000  
- http://localhost:5173  
- https://furnitureecom.netlify.app  

---

# 🔑 Authorization Header (For protected routes)

Include JWT token like this:

```
Authorization: Bearer <your_token>
```

---

# 📄 Final Notes

- API is fully tested and ready for production.
- Admin-only operations require a valid JWT token with ADMIN role.
- The backend is deployed on Render:  
  **https://furniture-backend-m441.onrender.com**

---

