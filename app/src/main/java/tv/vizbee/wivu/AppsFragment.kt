package tv.vizbee.wivu

import ADBTestFragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * AppsFragment is a simple [Fragment] subclass that provides a UI with a button to launch
 * the Netflix app. If Netflix is not installed, the fragment displays a toast notification.
 */
class AppsFragment : Fragment() {

    /**
     * Called to initialize the fragment's view by inflating its layout and setting up button click listeners.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that this fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_apps, container, false)

        // Locate the button in the layout and set an OnClickListener to handle clicks
        val netflix: Button = view.findViewById(R.id.netflix)
        netflix.setOnClickListener {
            // Launch the Netflix app or show a message if it is not installed
            launchNetflix(requireContext())
            Log.d("AppsFragment", "Netflix button clicked")
        }

        val adbCommands: Button = view.findViewById(R.id.adb_commands)
        adbCommands.setOnClickListener {
            if (savedInstanceState == null) {
                // Set up the main UI by adding the AppsFragment to the fragment container.
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_browse_fragment, ADBTestFragment())
                    .commitNow()
            }// Execute ADB commands to simulate remote control key presses

        }

        return view
    }

    /**
     * Attempts to launch the Netflix app. If the app is not installed, a Toast message is displayed.
     *
     * @param context The context used to access package information and to start the activity.
     */
    private fun launchNetflix(context: Context) {
        val packageName = "com.netflix.ninja"
        val launchIntent = context.packageManager?.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            // Start the Netflix app if it is available on the device
            context.startActivity(launchIntent)
        } else {
            // Show a message if the Netflix app is not installed
            Toast.makeText(context, "Netflix app is not installed", Toast.LENGTH_SHORT).show()
        }
    }
}