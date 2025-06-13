# E-Commerce Backend (Spring Boot)

This is a backend project for an e-commerce application, built with Spring Boot, Spring Security, JPA, and MySQL.

## Features
- User authentication (JWT-based)
- Product management (CRUD)
- Category management
- Cart and cart item management
- Order management
- Image upload and download
- Role-based access control

## Tech Stack
- Java 24
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- MySQL
- ModelMapper
- JWT (io.jsonwebtoken)
- Lombok

## Getting Started

### Prerequisites
- Java 17+ (Java 24 recommended)
- Maven
- MySQL

### Setup
1. **Clone the repository**
2. **Configure database**
   - Edit `src/main/resources/application.properties` with your MySQL credentials and database name.
3. **Build the project**
   ```sh
   ./mvnw clean install
   ```
4. **Run the application**
   ```sh
   ./mvnw spring-boot:run
   ```
   The server will start at `http://localhost:5000` (default port, can be changed in `application.properties`).

### API Endpoints
See [`api.http`](api.http) for example requests.

#### Auth
- `POST /ecom-java/auth/login` — Login

#### Products
- `GET /ecom-java/products/all` — Get all products
- `GET /ecom-java/products/product/{id}/product` — Get product by ID
- `POST /ecom-java/products/add` — Add new product

#### Categories
- `GET /ecom-java/products/by-brand?brand=...` — Get products by brand
- `GET /ecom-java/products/product/{category}/all/products` — Get products by category

#### Cart
- `POST /ecom-java/cart-items/item/add?productId=...&quantity=...` — Add item to cart
- `PUT /ecom-java/cart-items/cart/{cartId}/item/{itemId}/update?quantity=...` — Update cart item
- `DELETE /ecom-java/cart-items/cart/{cartId}/item/{itemId}/remove` — Remove cart item
- `GET /ecom-java/carts/{userId}/my-cart` — Get user's cart
- `DELETE /ecom-java/carts/{cartId}/clear` — Clear cart
- `GET /ecom-java/carts/{cartId}/cart/total-price` — Get cart total price

#### Orders
- `POST /ecom-java/orders/order?userId=...` — Add order

#### Images
- `POST /ecom-java/images/upload?productId=...` — Upload images
- `GET /ecom-java/images/image/download/{id}` — Download image

#### Users
- `GET /ecom-java/users/{id}/user` — Get user by ID

## Development
- Use Lombok for boilerplate code reduction.
- ModelMapper is used for DTO mapping. Ensure a `ModelMapper` bean is defined in your configuration.
- JWT is used for authentication and authorization.

## License
This project is for educational/demo purposes.
