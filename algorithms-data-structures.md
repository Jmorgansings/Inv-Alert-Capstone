# Algorithms and Data Structures Narrative

## Artifact Description
**Artifact Link:** [Inv-Alert-Capstone](https://github.com/Jmorgansings/Inv-Alert-Capstone/blob/main/app/src/main/java/com/example/jasminemorganinventorymanagement/DataDisplayActivity.kt)

This artifact, `DataDisplayActivity.kt`, showcases the practical application of data structures and algorithms within my inventory management application. The core data structure used is a `List`, specifically an `ArrayList`, which holds the inventory items fetched from the database. This `ArrayList` serves as the dataset for a `RecyclerView`, a highly efficient UI component for displaying large, scrollable lists. The main algorithm demonstrated is the sorting algorithm, which reorders the data based on user selection.

## Use of Data Structures and Algorithms

The `ArrayList` data structure was chosen for its flexibility and direct compatibility with the `RecyclerView.Adapter`. It allows for dynamic resizing and provides O(1) time complexity for accessing elements by index, which is ideal for the `onBindViewHolder` method of the adapter as it binds data to views.

The most prominent algorithm is the **sorting functionality**. When a user selects "Sort by Name" or "Sort by Quantity" from the menu, a specific algorithm is executed. This isn't a complex, in-memory sort like bubble sort or quicksort. Instead, I leveraged the power of the database by implementing a more efficient approach:
1. The user's menu selection is captured in `onOptionsItemSelected`.
2. A new SQL query is constructed that includes an `ORDER BY` clause (e.g., `ORDER BY name ASC` or `ORDER BY quantity ASC`).
3. The `DatabaseHelper` executes this query, which uses its own highly optimized internal sorting algorithms to return a pre-sorted dataset.
4. The existing `ArrayList` in the activity is cleared, and the new, sorted data is fetched and added to it.
5. `adapter.notifyDataSetChanged()` is called, which efficiently updates the `RecyclerView` to reflect the sorted list.

This design choice demonstrates an understanding of algorithmic efficiency. Instead of pulling all data into memory and then sorting it on the device (which would be inefficient for large datasets), I delegated the sorting operation to the SQLite database engine, which is specifically designed to perform such tasks quickly and with minimal resource consumption. This approach ensures the application remains responsive and scalable.

