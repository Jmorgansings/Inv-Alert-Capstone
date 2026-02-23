package com.example.jasminemorganinventorymanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Inventory.db"
        private const val DATABASE_VERSION = 2 // Incremented version for schema change
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users table with salt
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, salt TEXT)")
        // Create Items table
        db.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, quantity INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // If upgrading from a version without the salt column
            try {
                db.execSQL("ALTER TABLE users ADD COLUMN salt TEXT")
            } catch (e: Exception) {
                Log.e("DatabaseHelper", "Error upgrading users table, might already exist.", e)
            }
        }
        db.execSQL("DROP TABLE IF EXISTS items")
        onCreate(db)
    }

    // --- User Functions ---

    fun addUser(username: String, password: String): Boolean {
        if (userExists(username)) {
            Log.e("DatabaseHelper", "User already exists: $username")
            return false // User already exists
        }
        return try {
            val db = this.writableDatabase
            val salt = generateSalt()
            val hash = hashPassword(password, salt)

            val values = ContentValues().apply {
                put("username", username)
                put("password", byteArrayToHexString(hash))
                put("salt", byteArrayToHexString(salt))
            }

            val result = db.insert("users", null, values)
            db.close()
            result != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error adding user", e)
            false
        }
    }

    private fun userExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE username = ?", arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun checkUser(username: String, password: String): Pair<Boolean, Int> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val saltHex = cursor.getString(cursor.getColumnIndexOrThrow("salt"))
            val storedHashHex = cursor.getString(cursor.getColumnIndexOrThrow("password"))

            val salt = hexStringToByteArray(saltHex)
            val storedHash = hexStringToByteArray(storedHashHex)
            val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val hash = factory.generateSecret(spec).encoded

            if (storedHash.contentEquals(hash)) {
                cursor.close()
                db.close()
                return Pair(true, id) // Success, return true and user ID
            }
        }
        cursor.close()
        db.close()
        return Pair(false, -1) // Failure
    }

    fun updateUserPassword(userId: Int, newPassword: String): Boolean {
        return try {
            val db = this.writableDatabase
            val newSalt = generateSalt()
            val newHash = hashPassword(newPassword, newSalt)
            val values = ContentValues().apply {
                put("password", byteArrayToHexString(newHash))
                put("salt", byteArrayToHexString(newSalt))
            }
            val result = db.update("users", values, "id = ?", arrayOf(userId.toString()))
            db.close()
            result > 0
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error updating password", e)
            false
        }
    }

    fun getUsernameById(userId: Int): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT username FROM users WHERE id = ?", arrayOf(userId.toString()))
        return if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            cursor.close()
            db.close()
            username
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // --- Cryptography Functions ---

    private fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    private fun hashPassword(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val hexChars = CharArray(array.size * 2)
        for (j in array.indices) {
            val v = array[j].toInt() and 0xFF
            hexChars[j * 2] = "0123456789abcdef"[v ushr 4]
            hexChars[j * 2 + 1] = "0123456789abcdef"[v and 0x0F]
        }
        return String(hexChars)
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    // --- Item Functions ---

    fun addItem(name: String, quantity: Int): Long {
        return try {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put("name", name)
                put("quantity", quantity)
            }
            val result = db.insert("items", null, values)
            db.close()
            result
        } catch(e: Exception) {
            Log.e("DatabaseHelper", "Error adding item", e)
            -1L
        }
    }

    fun getAllItems(): List<Item> {
        val itemList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                itemList.add(Item(id, name, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }

    fun deleteItem(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete("items", "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
