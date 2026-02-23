# Capstone Project: Inv-Alert (Inventory Management App)

Inv-Alert is a full-featured inventory management application developed for Android as my capstone project for the CS499 course. The application provides a secure and efficient way for users to manage a list of products, including their names, quantities, and prices.

## Features

- **Secure User Authentication:** Users can log in with a username and password. The system includes functionality for securely changing passwords.
- **Inventory Display:** All inventory items are displayed in a clean, scrollable list using Android's `RecyclerView`.
- **Dynamic Sorting:** Users can instantly sort the inventory list by product name or by quantity, allowing for efficient data organization. This is powered by optimized database queries.
- **Persistent Data Storage:** All user and inventory data is stored locally and reliably in a SQLite database, ensuring data is never lost between sessions.
- **Modern UI:** The application is built with modern Android components from AndroidX and Google's Material Design library, providing a responsive and intuitive user interface.

## Technical Details

- **Language:** Kotlin
- **Architecture:** The application follows the principle of Separation of Concerns, with UI logic (`DataDisplayActivity.kt`) clearly decoupled from the database logic (`DatabaseHelper.java`).
- **Database:** SQLite is used for all CRUD (Create, Read, Update, Delete) operations.
- **Key Libraries:** `androidx.appcompat`, `com.google.android.material`, `androidx.constraintlayout`.

This project serves as a comprehensive demonstration of my skills in software engineering, database design, and algorithm implementation within the Android ecosystem.

