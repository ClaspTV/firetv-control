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
     */
    private fun executeADBCommands() {
        // Handler to delay commands
        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_DPAD_DOWN"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 5000) // 5-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_DPAD_UP"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 10000) // 10-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_DPAD_RIGHT"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 15000) // 15-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_DPAD_LEFT"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 20000) // 20-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_ENTER"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 25000) // 25-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_BACK"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 30000) // 30-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_HOME"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 35000) // 35-second delay

        Handler(Looper.getMainLooper()).postDelayed({
            val command = "input keyevent KEYCODE_MENU"
            Log.d("ADBTestFragment", command)
            Runtime.getRuntime().exec(command)
        }, 40000) // 40-second delay
    }
}
