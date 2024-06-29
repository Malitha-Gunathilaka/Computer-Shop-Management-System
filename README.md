# Computer Shop Management System

## Overview

The Computer Shop Management System is a Java-based application designed to manage the operations of a computer shop. It includes functionalities for user authentication, customer management, product management, and quotation management. The application uses MySQL as the backend database and Swing for the user interface.

## Features

- **User Authentication**: 
  - Login and logout functionality.
  - User roles (e.g., admin, salesperson).

- **Customer Management**: 
  - Add, edit, delete customer information.
  - Store customer contact details and purchase history.

- **Product Management**: 
  - Add, edit, delete products (PC components, accessories, etc.).
  - Manage product categories and stock levels.
  - Store product details such as price, description, and specifications.

- **Quotation Management**: 
  - Create new quotations.
  - Select products and quantities for a quotation.
  - Calculate total cost including taxes and discounts.
  - Save, print, or email quotations.
  - View and manage existing quotations.

- **Database Management**: 
  - Store and retrieve all data (customers, products, quotations) in a database.
  - Backup and restore database.
 
## Project Structure
computer-shop/
│
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── com/
│ │ │ │ ├── computershop/
│ │ │ │ │ ├── dao/
│ │ │ │ │ │ ├── CustomerDAO.java
│ │ │ │ │ │ ├── ProductDAO.java
│ │ │ │ │ │ ├── QuotationDAO.java
│ │ │ │ │ │ ├── UserDAO.java
│ │ │ │ │ ├── model/
│ │ │ │ │ │ ├── Customer.java
│ │ │ │ │ │ ├── Product.java
│ │ │ │ │ │ ├── Quotation.java
│ │ │ │ │ │ ├── QuotationItem.java
│ │ │ │ │ │ ├── User.java
│ │ │ │ │ ├── util/
│ │ │ │ │ │ ├── Database.java
│ │ │ │ │ ├── ui/
│ │ │ │ │ │ ├── CustomerManagementForm.java
│ │ │ │ │ │ ├── ProductManagementForm.java
│ │ │ │ │ │ ├── QuotationManagementForm.java
│ │ │ │ │ │ ├── CustomerQuotationForm.java
│ │ │ │ │ │ ├── LoginForm.java
│ │ │ │ │ │ ├── MainFrame.java
│ │ │ │ │ └── Main.java
│ │ └── resources/
│ └── test/
│ └── java/


## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- A Java IDE like IntelliJ IDEA or NetBeans

## Database Setup

1. Create the database:
    ```sql
    CREATE DATABASE computer_shop;

    USE computer_shop;

    CREATE TABLE customers (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        email VARCHAR(100),
        phone VARCHAR(20)
    );

    CREATE TABLE products (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        category VARCHAR(100),
        price DECIMAL(10, 2),
        stock INT
    );

    CREATE TABLE quotations (
        id INT AUTO_INCREMENT PRIMARY KEY,
        customer_id INT,
        total DECIMAL(10, 2),
        date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (customer_id) REFERENCES customers(id)
    );

    CREATE TABLE quotation_items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        quotation_id INT,
        product_id INT,
        quantity INT,
        price DECIMAL(10, 2),
        FOREIGN KEY (quotation_id) REFERENCES quotations(id),
        FOREIGN KEY (product_id) REFERENCES products(id)
    );

    CREATE TABLE settings (
        id INT AUTO_INCREMENT PRIMARY KEY,
        tax_rate DOUBLE NOT NULL,
        discount_rate DOUBLE NOT NULL
    );
    ```

2. Insert some initial data if needed.

## How to Run

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/computer-shop.git
    ```

2. Open the project in your preferred Java IDE.

3. Configure the database connection in `Database.java`:
    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/computer_shop";
    private static final String USER = "your_database_username";
    private static final String PASSWORD = "your_database_password";
    ```

4. Build and run the project.

## Usage

1. **Login**: Start the application and log in with your credentials.
2. **Customer Management**: Add, edit, or delete customers.
3. **Product Management**: Add, edit, or delete products.
4. **Quotation Management**: Create new quotations, add products to the quotation, calculate totals, and generate bills.

## Contributions

Contributions are welcome! Please submit a pull request or open an issue to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions or feedback, please contact [your_email@example.com].



