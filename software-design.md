---
layout: default
title: Software Design & Engineering
---

# Artifact: Software Design and Engineering

This page showcases the enhancement of the "Inv-Alert" Android application, with a specific focus on improvements in Software Design and Engineering. The core of this enhancement was to transform the application from a functional prototype into a robust, reliable, and maintainable piece of software.

---

### 1. Description of the Artifact

The primary artifact is the `DatabaseHelper.kt` class from the "Inv-Alert" application. Originally created for a mobile development course, this class handled all database interactions. However, it lacked any formal error handling, making it brittle and difficult to debug. Any database issue, such as a corrupted file or a failed query, would cause the application to crash without providing any diagnostic information.

### 2. Justification for Inclusion in the ePortfolio

I chose this artifact because it demonstrates a foundational principle of professional software engineering: **designing for resilience**. A well-designed application doesn't just work under ideal conditions; it fails gracefully and provides clear feedback when things go wrong. This enhancement showcases my ability to move beyond just "making it work" to "making it right."

**How the Artifact Was Improved:**

*   **Implementation of Try-Catch Blocks:** I wrapped every single database operation (add, delete, update, query) within `try-catch` blocks. This ensures that no database exception can crash the application.
*   **Structured Error Logging:** Instead of letting the app crash, the `catch` block now uses Android's built-in `Log.e()` function. It logs a descriptive error message along with the specific exception details to Logcat. This demonstrates an understanding of professional debugging practices, making it significantly easier to diagnose and fix issues in the future.
*   **Return Value Integrity:** The functions were modified to return meaningful values upon failure (e.g., `false` or `-1L`). This allows the calling code (like `DataDisplayActivity`) to know that an operation failed and react accordingly, for instance, by showing a "Toast" message to the user instead of crashing.

This enhancement demonstrates a key software design skill: creating a stable data layer that isolates failures and provides a clear path for debugging, which is essential for building scalable and maintainable applications.

### 3. Meeting Course Outcomes

This artifact directly demonstrates my progress toward **Course Outcome 4: Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices...**

By implementing structured exception handling and using Android's `Logcat` tool, I applied a well-founded industry technique to improve the application's reliability and maintainability. This moved the project from a simple script to a piece of engineered software, delivering more value by being significantly more robust.

### 4. Reflection on the Enhancement Process

The process of implementing this enhancement was a lesson in proactive thinking. Initially, the app worked, so there was no obvious "bug" to fix. The challenge was to anticipate future problems. I learned that good software design isn't just about solving current issues, but about building a foundation that prevents future ones.

The most insightful moment was seeing the "Error adding item" message appear in Logcat for the first time during a test. The app didn't crash; it simply informed me of the problem. This was a powerful demonstration of the difference between fragile and robust code. It taught me that logging is not an optional feature but a critical component of the development lifecycle. This experience has instilled in me a "design for debugging" mindset that I will carry into all my future projects.

---

### View the Complete Source Code

The full, enhanced source code for the "Inv-Alert" application, including the improved `DatabaseHelper.kt`, is available in the project's GitHub repository.

[**View Project on GitHub**](jasminemorganinventorymanagement4.zip)
