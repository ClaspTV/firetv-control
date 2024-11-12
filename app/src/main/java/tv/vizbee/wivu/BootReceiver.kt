package tv.vizbee.wivu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * BootReceiver is a BroadcastReceiver that listens for boot completion events
 * and triggers the WivuLauncherService when the device has finished booting.
 * It handles multiple boot events to ensure compatibility with various devices and conditions.
 */
class BootReceiver : BroadcastReceiver() {

    /**
     * Called when the BootReceiver receives an Intent broadcast.
     * This method checks for boot-related actions and starts the WivuLauncherService in the background.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received, containing the action to be handled.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the received action matches boot completion events
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" || // Custom action for quick boot events on some devices
            intent.action == "com.amazon.device.ACTION_REBOOT_COMPLETE" // Custom action specific to Amazon Fire devices
        ) {
            // Log boot completion and notify the user via a Toast message
            Log.d("BootReceiver", "Boot completed, starting service")
            Toast.makeText(context, "Boot completed, starting service", Toast.LENGTH_LONG).show()

            // Create an Intent to start the WivuLauncherService
            val serviceIntent = Intent(context, WivuLauncherService::class.java)

            // Start the service as a foreground service for devices running Android O or later
            context.startForegroundService(serviceIntent)
        }
    }
}