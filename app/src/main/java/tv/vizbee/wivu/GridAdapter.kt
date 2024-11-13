package tv.vizbee.wivu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GridAdapter(
    private val context: Context,
    private val items: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): String = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_grid, parent, false)

        val textView = view as TextView
        textView.text = items[position]

        return view
    }
}