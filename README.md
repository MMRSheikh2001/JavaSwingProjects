# Java Swing Projects

Four desktop applications written in **Java** with **Swing**. Each one is a separate NetBeans (Ant) project in its own folder and stores its data in a database through **JDBC**. The three most recent projects use SQLite, and the earlier POS project uses MySQL.

| Project | What it does | Database | Folder |
|---------|--------------|----------|--------|
| Student Grade Tracker | Enter student scores, see the average, highest and lowest | None (in memory, `ArrayList`) | `Student_Grade_Tracker` |
| Stock Trading Platform | Simulated stock market with buying, selling, holdings and profit | SQLite | `StockTradingPlatform` |
| Hotel Reservation System | Room search, booking, simulated payment and cancellation | SQLite | `HotelReservationSystem` |
| POS Home Practice (earlier project) | Point of sale for a home store: products, suppliers, categories and sales | MySQL | `POSHomePractice` |

## Common Design

- **Models** are plain classes for the data (`User`, `Stock`, `Room`, `Product` and so on).
- **DAO classes** hold all SQL. They use `PreparedStatement` with `?` placeholders, so values are never joined into the SQL text.
- **Views** are Swing `JFrame` screens built with the NetBeans GUI Builder. The four guest screens of the Hotel project are written in code.
- Stock Trading Platform and Hotel Reservation System use a **single login screen** for admin and users. The `isAdmin` flag of the account decides which dashboard opens.
- In those two projects, transactions, reservations and rooms are never deleted. Reservations are marked `CANCELLED` and rooms are marked inactive, so the history stays consistent.

## Requirements

- JDK 17 or later
- NetBeans (the projects are Ant projects)
- The database drivers are included in each project's `lib/` folder (`sqlite-jdbc` and `mysql-connector-j`)
- MySQL is needed only for POS Home Practice

## How to Run

1. Clone the repository:
   ```
   git clone https://github.com/MMRSheikh2001/JavaSwingProjects.git
   ```
2. In NetBeans, open one project folder as a project and run it (F6).
3. The SQLite projects look for their `.db` file in the project folder. NetBeans starts them from there by default, so the included database files are found.

Demo logins (also shown on the login screens): `admin` / `1234` for Stock Trading Platform and Hotel Reservation System, and `mahbub` / `1234` for POS Home Practice.

---

## 1. Student Grade Tracker

A small application for entering student scores and seeing class statistics.

**Features**
- Add a student with a name and a score from 0 to 100, with input validation
- Table of all students with a letter grade (A from 90, B from 80, C from 70, D from 60, otherwise F)
- Average score, highest score and lowest score, with the names of the students
- Select a row to load it into the fields, and delete the selected student
- Reset button to clear the fields

**Technologies:** Java 17, Swing, `ArrayList`

**Run:** run the project. The main class opens `StudentGradeForm`. Data is kept in memory only and is lost when the application closes.

---

## 2. Stock Trading Platform

A simulated stock market. Prices change on their own, users trade with virtual cash, and an admin manages users and stocks.

**Features**
- Stock prices move randomly by up to 2% every 3 seconds while the application runs, and never fall below 1.00
- The market table on the Buy screen refreshes every 3 seconds
- Buy shares with cash and sell holdings back at the current price
- Holdings with the average buy price, and a transaction history for each user
- User dashboard with cash balance, number of holdings, number of transactions, Total Bought, Total Sold and Total Profit
- Admin screens to add and update users and stocks, and to view all holdings and transactions
- Transactions cannot be edited or deleted from the application

**How trading works**
- Buy: the total is deducted from cash, the stock's available quantity decreases, the holding is created or increased, and a BUY transaction is saved. The average buy price is (old cost + new cost) ÷ total shares.
- Sell: cash is added, the holding decreases, the shares return to the market, and a SELL transaction is saved.
- Profit = Total Sold + current value of shares still owned − Total Bought.

**Technologies:** Java 17, Swing, SQLite (`sqlite-jdbc`), `javax.swing.Timer` for the price simulation and screen refresh

**Database (`stocktrading.db`):** `users`, `stocks`, `holdings`, `transactions`

**Run:** run the project. The main class starts the price simulator and opens the login screen.

---

## 3. Hotel Reservation System

Guests search for available rooms by date, book, pay (simulated) and cancel. An admin manages users and rooms and sees all reservations and payments.

**Features**
- Room categories: Standard, Deluxe and Suite
- Search by check-in date, check-out date and category. A room is shown only if it is active and no booked reservation overlaps the dates. A stay may start on the day another one ends.
- Booking with the total calculated from the number of nights
- Simulated payment by card or cash, with a generated reference number. A card needs 16 digits and a 3-digit CVV, and a card number ending in `0000` is declined. The card number is not stored.
- Reservation history with a details view: room, dates, total, status, and the payment method, reference and time
- Cancellation of reservations that have not started. The record stays with the status `CANCELLED`.
- Admin screens to add and update users and rooms, and to view all reservations (with user name, room number and paid status) and all payments
- Admin dashboard with totals for users, rooms, reservations, payments and income

**Technologies:** Java 17, Swing, SQLite (`sqlite-jdbc`), `java.time` for dates

**Database (`hotelreservation.db`):** `users`, `rooms`, `reservations`, `payments`

**Run:** run the project. The main class opens the login screen.

---

## 4. POS Home Practice (earlier project)

A point-of-sale application for a home store, built earlier than the three projects above. It uses MySQL and a generic `DaoService<E>` interface for the data classes.

**Features**
- Login screen and a dashboard with buttons for Supplier, Product, Category and Sales
- Category screen: add, update and delete categories
- Supplier screen: add, update and delete suppliers (name, phone, contact person, contact person phone, address)
- Product screen: add, update and delete products with a price, a quantity, a category and a supplier chosen from lists. The product table shows the category and supplier names.
- Sales screen: choose a product, enter the unit price and quantity to get the total price, enter a discount percentage to get the discount amount and the actual price, then save the sale with the current date. All sales are listed in a table.
- Calculations are kept in a separate `SalesUtil` class

**Technologies:** Java 17, Swing, MySQL (`mysql-connector-j`)

**Database setup:** the project connects to `jdbc:mysql://localhost:3306/homestore` with user `root` and password `1234`. Change these in `util/DbUtil.java` if your server is different. The repository has no SQL script, so this one was written from the SQL statements in the DAO classes:

```sql
CREATE DATABASE homestore;
USE homestore;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(50),
    password VARCHAR(50),
    role VARCHAR(20)
);

CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE supplier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    cell VARCHAR(20),
    contactPersonName VARCHAR(100),
    contactPersonCell VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE,
    quantity DOUBLE,
    categoryId INT,
    supplierId INT
);

CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productName VARCHAR(100),
    unitPrice DOUBLE,
    quantity DOUBLE,
    totalPrice DOUBLE,
    discount DOUBLE,
    actualPrice DOUBLE,
    date DATE
);
```

**Run:** the `main` method of `POSHomePractice` is commented out. Run `view/Login.java` directly (Shift+F6), or uncomment the lines in `main` that open the login screen.

**Notes**
- The login checks a fixed user name and password in the code. The `user` table is not used for login.
- Saving a sale does not reduce the product's quantity.
- Sales can be added and listed, but not edited or deleted.

---

## Repository Structure

```
JavaSwingProjects/
├── Student_Grade_Tracker/     # in-memory project
├── StockTradingPlatform/      # SQLite: stocktrading.db
├── HotelReservationSystem/    # SQLite: hotelreservation.db
└── POSHomePractice/           # MySQL
```

Inside each project, the code is split into packages for models, DAO classes (or `db`), views, and in some projects services and utilities.

## Known Limitations

- Passwords are stored as plain text in the Stock Trading Platform and Hotel Reservation System, and the demo admin password is shown on their login screens.
- Payments in the Hotel Reservation System are simulated, and cancelled reservations are not refunded.
- Table creation and the first admin account are not created automatically in the SQLite projects. The included database files already contain them.
- The SQLite projects use a local file, so they are single-machine applications.

## Author

MD Mahbubur Rahman Sheikh — [@MMRSheikh2001](https://github.com/MMRSheikh2001)
