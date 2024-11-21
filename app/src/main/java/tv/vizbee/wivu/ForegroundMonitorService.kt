package tv.vizbee.wivu

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ForegroundMonitorService is a foreground service that monitors the currently active (foreground) app
 * on the device. This service requires the usage stats permission to access information about
 * the foreground app. It periodically checks which app is in the foreground, logs this information,
 * and can be used for further handling if needed.
 */
class ForegroundMonitorService : Service() {

    private var isMonitoring = false // Tracks whether monitoring is active
    private lateinit var detector: ForegroundAppDetector // Detector for identifying foreground app
    private var monitoringJob: Job? = null // Job for coroutine monitoring task
    private val coroutineScope =
        CoroutineScope(Dispatchers.Default + Job()) // Scope for background tasks

    /**
     * Initializes the service.
     * Starts the service in the foreground with a notification to ensure it remains active.
     */
    override fun onCreate() {
        super.onCreate()
        detector = ForegroundAppDetector() // Initialize the detector
        startForeground(NOTIFICATION_ID, createNotification()) // Start service in the foreground
    }

    /**
     * Called when the service is started. Begins monitoring the foreground app if not already running.
     * @return START_STICKY to ensure the service is recreated if it is killed by the system.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isMonitoring) {
            startMonitoring() // Start monitoring foreground app
        }
        return START_STICKY
    }

    /**
     * Starts monitoring the foreground app using a coroutine. The app in the foreground
     * is checked every second, and its package name and app name are logged.
     */
    private fun startMonitoring() {
        isMonitoring = true
        monitoringJob = coroutineScope.launch {
            while (isActive) { // Continues monitoring while coroutine is active
                val foregroundApp = detector.getForegroundApp(applicationContext)
                foregroundApp?.let { packageName ->
                    val appName = detector.getAppNameFromPackage(applicationContext, packageName)
                    Log.d("ForegroundMonitor", "Current App: $appName ($packageName)")
                    // Add further handling of foreground app info if needed
                    if (appName != "Wivu" && appName !="Netflix" && appName != "Settings") {
                        launchApp()
                    }
                }
                delay(1000) // Delay for 1 second between checks
            }
        }
    }

    private fun launchApp() {
        Log.d("ForegroundMonitor", "Relaunch")
        val launchIntent = Intent(applicationContext, MainActivity::class.java)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)
    }

    /**
     * Cleans up resources by stopping the monitoring job and cancelling the coroutine scope.
     * Called when the service is being destroyed.
     */
    override fun onDestroy() {
        isMonitoring = false
        monitoringJob?.cancel() // Stop monitoring job
        coroutineScope.cancel() // Cancel coroutine scope
        super.onDestroy()
    }

    /**
     * Method required for binding the service. Returns null since this is a started service.
     */
    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * Creates and returns a notification for running the service in the foreground.
     * The notification is used to ensure the service continues running even if the app is backgrounded.
     */
    private fun createNotification(): Notification {
        val channelId = "foreground_monitor_channel"

        // Create a notification channel for Android O and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Foreground Monitor",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Return a low-priority notification to avoid showing it prominently
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Foreground App Monitor")
            .setContentText("Monitoring active apps")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1 // ID for the foreground notification
    }
}
