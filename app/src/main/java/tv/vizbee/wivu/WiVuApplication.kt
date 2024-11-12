package tv.vizbee.wivu

import android.app.Application
import android.util.Log
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
    }
}