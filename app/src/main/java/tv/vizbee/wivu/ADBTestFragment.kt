import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import tv.vizbee.wivu.GridAdapter
import tv.vizbee.wivu.R
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * ADBTestFragment is a Fragment that displays a grid of items and allows the user
 * to interact with each item. It also demonstrates the execution of ADB (Android Debug Bridge)
 * commands with delays for simulating key events.
 */
class ADBTestFragment : Fragment() {

    // A list of items to display in the GridView
    private val items = listOf(
        "Item 1", "Item 2", "Item 3", "Item 4",
        "Item 5", "Item 6", "Item 7", "Item 8"
    )

    /**
     * Inflates the fragment's layout and sets up the GridView.
     *
     * @param inflater LayoutInflater to inflate the fragment's view.
     * @param container Optional parent view for the fragment's UI.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a_d_b_test, container, false)
        setupGridView(view)
        executeADBCommands()
        return view
    }

    /**
     * Initializes the GridView with a list of items and sets an item click listener.
     * When an item is clicked, a toast message is displayed.
     *
     * @param view The root view of the fragment's layout.
     */
    private fun setupGridView(view: View) {
        // Find GridView by its ID and set up the adapter with items
        val gridView: GridView = view.findViewById(R.id.gridView)
        val adapter = GridAdapter(requireContext(), items)
        gridView.adapter = adapter

        // Set up click listener for each item in the GridView
        gridView.setOnItemClickListener { _, _, position, _ ->
            showToast("Clicked: ${items[position]}")
        }
    }

    /**
     * Displays a toast message with the specified text.
     *
     * @param message The message to display in the toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Executes a series of ADB commands with delays, simulating key events.
     * Each command is logged and executed using a delay to space out the key events.
     *
     * Note:
     *
     * We are unable to perform specific system-level actions, such as triggering the Home button
     * and controlling volume, through ADB commands within the app due to permission restrictions.
     *
     * Explanation:
     * - Commands to simulate the Home button press or adjust volume levels (e.g., using `input keyevent KEYCODE_HOME`
     *   or `input keyevent KEYCODE_VOLUME_UP`) require elevated privileges.
     * - These commands are restricted and can only be executed by system-level apps or via an external ADB shell,
     *   as they require permissions not available to regular third-party apps.
     */
    private fun executeADBCommands() {
        // Define commands and their delays in a data structure
        val commands = listOf(
            "KEYCODE_DPAD_DOWN" to 5000L,
            "KEYCODE_DPAD_UP" to 10000L,
            "KEYCODE_DPAD_RIGHT" to 15000L,
            "KEYCODE_DPAD_LEFT" to 20000L,
            "KEYCODE_ENTER" to 25000L,
            "KEYCODE_BACK" to 30000L
        )

        // Use a single handler instance
        val handler = Handler(Looper.getMainLooper())

        // Execute commands using a more concise approach
        commands.forEach { (keycode, delay) ->
            handler.postDelayed({
                val command = "input keyevent $keycode"
                try {
                    Log.d("ADBTestFragment", "Executing: $command")
                    Runtime.getRuntime().exec(command)
                } catch (e: Exception) {
                    Log.e("ADBTestFragment", "Failed to execute command: $command", e)
                }
            }, delay)
        }
    }

    /**
     * Note:
     *
     * Attempted to retrieve the currently running activity within the app using an ADB command
     * (dumpsys activity activities | grep mResumedActivity), but encountered a permission denial:
     *
     * ```
     * Permission Denial: can't dump ActivityManager from pid=21157, uid=10217
     * due to missing android.permission.DUMP permission
     * ```
     *
     * Explanation:
     * - The command "dumpsys activity activities | grep mResumedActivity" is commonly used to get
     *   the currently resumed activity on the device.
     * - However, it requires the `android.permission.DUMP` permission, which is a restricted permission.
     * - This permission is typically only available to system apps and cannot be granted to third-party apps.
     *
     */
    private fun getCurrentActivityRunning() {

        try {
            // Execute the adb shell command
            val process =
                Runtime.getRuntime().exec("dumpsys activity activities | grep mResumedActivity")

            // Get the input stream and read the output
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            // Read each line of output and print it
            while (reader.readLine().also { line = it } != null) {
                // Print the output of the command
                Log.d("MainActivity", "Current Activity: $line")
            }

            // Wait for the process to finish
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
