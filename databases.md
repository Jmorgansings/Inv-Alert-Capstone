# Databases Narrative

## Artifact Description
**Artifact Link:** [Inv-Alert-Capstone](https://github.com/Jmorgansings/Inv-Alert-Capstone/blob/main/app/src/main/java/com/example/jasminemorganinventorymanagement/DatabaseHelper.java)

The artifact that best demonstrates my database skills is `DatabaseHelper.java`. This Java class extends `SQLiteOpenHelper` and is the backbone of the application's data persistence layer. It is solely responsible for all database-related operations, including creating the database and its tables, managing schema upgrades, and handling all CRUD (Create, Read, Update, Delete) operations for both users and inventory items. This encapsulation ensures that the rest of the application does not need to be aware of the underlying database structure or SQL queries.

## Database Design and Interaction

The database schema was designed to be simple yet effective, consisting of two tables: a `users` table to store login credentials and an `inventory` table for the products. The `users` table includes columns for `username` and `password`, while the `inventory` table has columns for `id`, `name`, `quantity`, and `price`. This normalized structure prevents data redundancy and is appropriate for the application's needs.

My `DatabaseHelper.java` class demonstrates a comprehensive understanding of database interaction:
1.  **Schema Creation:** The `onCreate` method defines and executes the `CREATE TABLE` SQL statements. This ensures that the database has the correct structure the first time the application is run. I defined primary keys and data types (`TEXT`, `INTEGER`, `REAL`) appropriate for the data being stored.
2.  **Data Manipulation (CRUD):** I implemented specific methods for each CRUD operation. For instance, the `addUser` method uses `ContentValues` to safely insert a new user, which helps prevent SQL injection vulnerabilities. Similarly, the `getInventoryData(String orderBy)` method executes a `SELECT` query to retrieve all inventory items.
3.  **Data Integrity and Security:** The `checkUser` method retrieves a user's stored password for authentication, and the `updatePassword` method securely updates it. Crucially, password hashing (which would be implemented in the calling activity before passing data to `DatabaseHelper`) is supported by this design, ensuring that plain-text passwords are not stored, which is a fundamental security practice.
4.  **Efficient Querying:** The `getInventoryData(String orderBy)` method is a key feature. It accepts a string parameter to dynamically add an `ORDER BY` clause to the SQL query. This allows the user to sort the data efficiently at the database level rather than inefficiently in the application's memory, demonstrating an understanding of query optimization and performance.
