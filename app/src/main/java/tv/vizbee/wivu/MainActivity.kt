package tv.vizbee.wivu

import android.os.Bundle
import androidx.fragment.app.FragmentActivity


/**
 * MainActivity is the primary activity for the app, extending FragmentActivity.
 * This activity sets up the initial UI.
 *
 * MainActivity contains logic to launch the main fragment (AppsFragment).
 */
class MainActivity : FragmentActivity() {

    /**
     * Called when the activity is first created. Sets up the initial layout,
     * initializes the main fragment, and prepares any necessary services.
     *
     * @param savedInstanceState Contains data supplied in onSaveInstanceState(Bundle)
     *                           when the activity is being reinitialized.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if this is the first creation of the activity to avoid re-adding the fragment.
        if (savedInstanceState == null) {
            // Set up the main UI by adding the AppsFragment to the fragment container.
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, AppsFragment())
                .commitNow()
        }

        // Uncomment below code to start ForegroundMonitorService if needed for monitoring foreground apps
        /*
        val serviceIntent = Intent(this, ForegroundMonitorService::class.java)

        // Start the foreground service based on OS version compatibility.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent) // Required for Android O and above
        } else {
            startService(serviceIntent)
        }
        */
    }
}