package tv.vizbee.wivu

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log

/**
 * ForegroundAppDetector provides methods to detect the currently active (foreground) app on the device.
 * It uses UsageStatsManager for reliable app usage statistics and falls back on ActivityManager for older devices.
 * Additionally, it checks and requests the necessary permissions for usage stats access.
 */
class ForegroundAppDetector {

    companion object {
        private const val USAGE_STATS_INTERVAL = 1000L // 1-second interval for checking usage stats
    }

    /**
     * Gets the package name of the foreground app using UsageStatsManager.
     * @param context The context to access system services.
     * @return The package name of the currently foreground app or null if unavailable.
     */
    fun getForegroundApp(context: Context): String? {
        try {
            // Get the UsageStatsManager system service for retrieving usage stats
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            // Define the time range for which usage stats should be checked
            val endTime = System.currentTimeMillis()
            val startTime = endTime - USAGE_STATS_INTERVAL

            // Query the usage stats for the given time range
            val usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )

            if (usageStats != null && usageStats.isNotEmpty()) {
                // Sort the retrieved usage stats by last time used and get the most recent one
                val sortedStats = usageStats.sortedByDescending { it.lastTimeUsed }
                return sortedStats.firstOrNull()?.packageName
            }

            // Fallback to ActivityManager for older devices or if no usage stats available
            return getForegroundAppLegacy(context)
        } catch (e: Exception) {
            Log.e("ForegroundDetector", "Error getting foreground app", e)
            return null
        }
    }

    /**
     * Legacy method to get the foreground app for older Android versions using ActivityManager.
     * @param context The context to access system services.
     * @return The package name of the topmost activity or null if unavailable.
     */
    private fun getForegroundAppLegacy(context: Context): String? {
        try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appTasks = activityManager.appTasks
            if (appTasks != null && appTasks.isNotEmpty()) {
                return appTasks[0].taskInfo?.topActivity?.packageName
            }
        } catch (e: Exception) {
            Log.e("ForegroundDetector", "Error in legacy detection", e)
        }
        return null
    }

    /**
     * Checks if the app has permission to access usage stats.
     * @param context The context to access system services.
     * @return True if permission is granted; otherwise, false.
     */
    fun checkUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Opens the settings screen where the user can grant usage stats permission.
     * @param context The context to start the settings activity.
     */
    fun requestUsageStatsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * Retrieves the app name from a given package name.
     * @param context The context to access the PackageManager.
     * @param packageName The package name of the app.
     * @return The app's name if available; otherwise, returns the package name.
     */
    fun getAppNameFromPackage(context: Context, packageName: String): String {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: Exception) {
            Log.e("ForegroundDetector", "Error getting app name", e)
            return packageName
        }
    }
}

