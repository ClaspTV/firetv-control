package tv.vizbee.wivu

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

/**
 * WivuLauncherService is a foreground service designed to launch the MainActivity on start.
 * This service includes notification setup to comply with Android's foreground service requirements.
 * It also creates a notification channel for displaying the service notification.
 */
class WivuLauncherService : Service() {

    companion object {
        private const val LOG_TAG = "WivuLauncherService" // Unique ID for notification channel
        private const val CHANNEL_ID = "WivuLauncherServiceChannel" // Unique ID for notification channel
        private const val NOTIFICATION_ID = 1 // Unique ID for the service notification
    }

    /**
     * Called by the system when the service is first created. Sets up the notification channel
     * and starts the service in the foreground with an active notification.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "Service onCreate")

        // Create notification channel for foreground service notifications
        createNotificationChannel()

        // Create a notification for the foreground service
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Wivu Service")
            .setContentText("Service is running")
            .build()

        // Start the service in the foreground, showing a notification to the user
        startForeground(NOTIFICATION_ID, notification)
    }

    /**
     * Handles the service start request and launches the MainActivity. This method
     * ensures that the service restarts automatically if it gets terminated.
     *
     * @param intent The Intent supplied to startService(Intent).
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return A flag that indicates the behavior of the service if killed.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "Service LaunchApp")

        // Intent to launch MainActivity in a new task
        val launchIntent = Intent(this, MainActivity::class.java)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)

        // Ensures the service continues running until explicitly stopped
        return START_STICKY
    }

    /**
     * This service does not support binding, so it returns null.
     *
     * @param intent The Intent that was used to bind to this service.
     * @return null since this service is not designed to bind with components.
     */
    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * Creates a notification channel to allow the service to run in the foreground
     * with a visible notification, which is required for background execution.
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Wivu Launcher Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }
}