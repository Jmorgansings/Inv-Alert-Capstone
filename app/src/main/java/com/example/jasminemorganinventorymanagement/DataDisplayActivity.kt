package com.example.jasminemorganinventorymanagement

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataDisplayActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var itemsGridView: GridView
    private lateinit var adapter: ItemAdapter
    private var itemList = mutableListOf<Item>()
    private var currentUserId: Int = -1 // Variable to hold the logged-in user's ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_display)

        // Get the user ID passed from LoginActivity
        currentUserId = intent.getIntExtra("USER_ID", -1)

        if (currentUserId == -1) {
            // If no user ID, something went wrong. Go back to login.
            Toast.makeText(this, "Error: User not identified.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        dbHelper = DatabaseHelper(this)
        itemsGridView = findViewById(R.id.itemsGridView)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { showAddItemDialog() }

        itemsGridView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedItem = itemList[position]
            AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete '${selectedItem.name}'?")
                .setPositiveButton("Yes") { _, _ ->
                    dbHelper.deleteItem(selectedItem.id)
                    loadInventoryData()
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
        loadInventoryData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.data_display_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_name -> {
                sortData(SortType.BY_NAME)
                true
            }
            R.id.sort_by_quantity -> {
                sortData(SortType.BY_QUANTITY)
                true
            }
            R.id.action_change_password -> {
                showChangePasswordDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showChangePasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Password")

        // Create a layout for the dialog
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(48, 24, 48, 24)

        val oldPasswordEditText = EditText(this).apply {
            hint = "Old Password"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        val newPasswordEditText = EditText(this).apply {
            hint = "New Password"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        layout.addView(oldPasswordEditText)
        layout.addView(newPasswordEditText)
        builder.setView(layout)

        builder.setPositiveButton("Change") { dialog, _ ->
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            if (oldPassword.isNotBlank() && newPassword.isNotBlank()) {
                val username = dbHelper.getUsernameById(currentUserId)
                if (username != null) {
                    val (isSuccess, _) = dbHelper.checkUser(username, oldPassword)
                    if (isSuccess) {
                        val updated = dbHelper.updateUserPassword(currentUserId, newPassword)
                        if (updated) {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun sortData(sortType: SortType) {
        when (sortType) {
            SortType.BY_NAME -> {
                itemList.sortBy { it.name.lowercase() }
                Toast.makeText(this, "Sorted by name", Toast.LENGTH_SHORT).show()
            }
            SortType.BY_QUANTITY -> {
                itemList.sortBy { it.quantity }
                Toast.makeText(this, "Sorted by quantity", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.notifyDataSetChanged()
    }

    private enum class SortType { BY_NAME, BY_QUANTITY }

    private fun loadInventoryData() {
        itemList.clear()
        itemList.addAll(dbHelper.getAllItems())
        adapter = ItemAdapter(this, itemList)
        itemsGridView.adapter = adapter
    }

    private fun showAddItemDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Item")

        val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
        builder.setView(view)

        val nameEditText = view.findViewById<EditText>(R.id.itemNameEditText)
        val quantityEditText = view.findViewById<EditText>(R.id.itemQuantityEditText)
        val addButton = view.findViewById<Button>(R.id.addItemButton)
        val dialog = builder.create()

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val quantityStr = quantityEditText.text.toString()

            if (name.isNotBlank() && quantityStr.isNotBlank()) {
                val quantity = quantityStr.toIntOrNull()
                if (quantity != null) {
                    val result = dbHelper.addItem(name, quantity)
                    if (result != -1L) {
                        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                        loadInventoryData()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Error adding item", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid number for quantity", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
}
