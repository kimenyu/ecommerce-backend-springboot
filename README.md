# ECommerce Platform

ECommerce Platform is a comprehensive back-end application built using Spring Boot, designed to provide a full-featured e-commerce experience. This application integrates with Stripe for payment processing and includes functionalities such as listing products, managing categories, user authorization, handling permissions, error handling, and adding products to wishlists.

## Features

- **User Authentication and Authorization**
- **Product Listing and Management**
- **Category Management**
- **Permissions Handling**
- **Error Handling**
- **Wishlist Functionality**
- **Stripe Payments Integration**

## Technologies

- **Back-End**: Java (Spring Boot)
- **Database**: PostgreSQL (for storing user, product, and order data)
- **Payment Processing**: Stripe
- **Other Libraries**: Spring Security (for authentication and authorization), Lombok (for reducing boilerplate code)
## Setup and Installation

### Prerequisites

- Java 11+
- MYSQL
- Maven
- Stripe account and API keys

### Steps

1. **Clone the repository:**

    ```bash
    git clone https://github.com/kimenyu/ecommerce-backend-springboot

    cd ecommerce-backend-springboot
    ```

2. **Install dependencies:**

    ```bash
    mvn install
    ```

3. **Create a `application.properties` file in the `src/main/resources` directory and configure your database and Stripe API keys:**

    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.jpa.hibernate.ddl-auto=update

    # Stripe Configuration
    stripe.apiKey=your_stripe_api_key
    ```

4. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

