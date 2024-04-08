package com.example.sos_demo

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class VolumeButtonPressService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "sos_channel"
    }

    enum class Actions {
        START,
        STOP
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun start() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:+1234567890")

            val callPendingIntent = PendingIntent.getActivity(
                this, 0, callIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(this, "sos_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("SOS LHE User")
                .setContentInfo("LHE SOS service is running....")
                .setContentIntent(pendingIntent)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    "Call",
                    callPendingIntent
                )
                .setOngoing(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notificationManager.notify(NOTIFICATION_ID, notification)

            startForeground(1, notification)
        } else {
            Toast.makeText(this, "Please grant phone call permission in app settings.", Toast.LENGTH_LONG).show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}