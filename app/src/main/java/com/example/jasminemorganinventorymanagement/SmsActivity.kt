package com.example.jasminemorganinventorymanagement

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SmsActivity : AppCompatActivity() {

    private val SMS_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This line loads your XML layout file
        setContentView(R.layout.activity_sms)

        val sendSmsButton: Button = findViewById(R.id.sendSmsButton)
        sendSmsButton.setOnClickListener {
            checkPermissionAndSendSms()
        }
    }

    private fun checkPermissionAndSendSms() {
        // Check if the permission has already been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, we "send" the SMS (by showing a toast message)
            sendSms()
        } else {
            // If not granted, we request the permission from the user.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }
    }

    // This function is called after the user responds to the permission dialog
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted by the user
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show()
                sendSms()
            } else {
                // Permission was denied by the user
                Toast.makeText(this, "SMS Permission Denied. Cannot send notifications.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendSms() {
        // For this project, we don't need to send a real SMS.
        // We just prove we have permission by showing a message.
        Toast.makeText(this, "Permission is granted. Sending SMS now...", Toast.LENGTH_LONG).show()
    }
}
