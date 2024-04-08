package com.example.sos_demo

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class DialTileService : TileService() {
    override fun onClick() {
        super.onClick()

        // Define the custom number to dial
        val phoneNumber = "1234567890"

        // Create an intent to dial the number
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Start the dialer activity
        startActivityAndCollapse(intent)
    }

    override fun onStartListening() {
        super.onStartListening()

        // Set tile properties
        val tile = qsTile
        tile.state = Tile.STATE_ACTIVE
        tile.label = "Dial Number"
        tile.updateTile()
    }
}