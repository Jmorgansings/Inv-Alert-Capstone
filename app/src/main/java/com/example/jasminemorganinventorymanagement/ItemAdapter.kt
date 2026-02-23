package com.example.jasminemorganinventorymanagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ItemAdapter(context: Context, private val items: List<Item>) :
    ArrayAdapter<Item>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val item = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
        }

        // Lookup view for data population
        val itemNameTextView = view!!.findViewById<TextView>(R.id.itemNameTextView)
        val itemQuantityTextView = view.findViewById<TextView>(R.id.itemQuantityTextView)

        // Populate the data into the template view using the data object
        if (item != null) {
            itemNameTextView.text = item.name
            itemQuantityTextView.text = "Quantity: ${item.quantity}"
        }

        // Return the completed view to render on screen
        return view
    }
}
