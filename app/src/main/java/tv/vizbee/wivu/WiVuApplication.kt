package tv.vizbee.wivu

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * WiVuApplication is the main Application class for the app.
 * It implements the DefaultLifecycleObserver interface to monitor the app's lifecycle state changes.
 * This class observes when the application moves to the foreground or background.
 *
 * The lifecycle is managed by ProcessLifecycleOwner, which provides lifecycle events for the entire
 * application process, not just individual activities or fragments.
 */
class WiVuApplication : Application(), DefaultLifecycleObserver {

    /**
     * Called when the application is created. Initializes application-wide configurations.
     * Here, it sets up a lifecycle observer using ProcessLifecycleOwner to listen for
     * app-wide foreground/background transitions.
     */
    override fun onCreate() {
        super<Application>.onCreate()

        // Register this class as a lifecycle observer to track app-wide lifecycle events.
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Called when the app enters the foreground, i.e., becomes visible to the user.
     * This method is triggered when the entire app process moves to the foreground.
     *
     * @param owner The LifecycleOwner which controls the observer, in this case, the entire process.
     */
    override fun onStart(owner: LifecycleOwner) {
        Log.d("WiVuApplication", "App is in the foreground")
    }

    /**
     * Called when the app enters the background, i.e., is no longer visible to the user.
     * This method is triggered when the entire app process moves to the background.
     *
     * @param owner The LifecycleOwner which controls the observer, in this case, the entire process.
     */
    override fun onStop(owner: LifecycleOwner) {
        Log.d("WiVuApplication", "App is in the background")
//        getCurrentForegroundApp()
    }

    private fun getCurrentForegroundApp() {
        val detector = ForegroundAppDetector() // Initialize the detector
        val foregroundApp = detector.getForegroundApp(this)
        foregroundApp?.let { packageName ->
            val appName = detector.getAppNameFromPackage(this, packageName)
            Log.d("WiVuApplication", "Current App: $appName ($packageName)")
            // Add further handling of foreground app info if needed
            checkCurrentAppIsWiVuSupportedAp(appName)
        }
    }

    private fun checkCurrentAppIsWiVuSupportedAp(appName: String) {
        if (appName == "Netflix") {
            Log.d(
                "WiVuApplication",
                "WiVu supported third party app -> Netflix is in the foreground"
            )
        } else {
            bringAppToForeground(this, packageName)
        }

    }

    private fun bringAppToForeground(context: Context, packageName: String) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val recentTasks = activityManager.appTasks

        for (task in recentTasks) {
            val taskInfo = task.taskInfo
            if (taskInfo.baseActivity?.packageName == packageName) {
                task.moveToFront()
                return
            }
        }
        Toast.makeText(context, "App is not running", Toast.LENGTH_SHORT).show()
    }

}