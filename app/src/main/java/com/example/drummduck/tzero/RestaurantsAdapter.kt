package com.example.drummduck.tzero

import android.graphics.Color
import kotlin.collections.*
import android.util.TypedValue
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Adapter for recyclerview
 * Holds all information for each card
 */
class RestaurauntsAdapter(
    private val listener: RestaurantViewHolder.OnItemSelectedListener,
    items: MutableList<RestaurantSelectItem>,  restaurantSelectedName: String?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), RestaurantViewHolder.OnItemSelectedListener {

    private val mValues: MutableList<SelectableRestaurant>
    private var restaurantSelected: String?  = null
    init {

        mValues = mutableListOf()
        for (item in items) {
            if (restaurantSelectedName != null && !restaurantSelectedName.isEmpty() && item.restaurantName.equals(restaurantSelectedName)) {
                Log.i("RestaurantsAdapter", "restaurantSelectedName: $restaurantSelectedName")
                mValues.add(SelectableRestaurant(item, true))
                restaurantSelected = restaurantSelectedName
            } else {
                Log.i("RestaurantsAdapter", "Adding restaurant to adapter")
                mValues.add(SelectableRestaurant(item, false))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkedview, parent, false)
        return RestaurantViewHolder(itemView, this)
    }

    //Updates the recyclerview with a list of SelectableRestaurants
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val holder = viewHolder as RestaurantViewHolder
        val selectableItem = mValues[position]
        val name = "Name: " + selectableItem.restaurantName + "\n\n" + "Address: " + selectableItem.restaurantAddress
        holder.textView.setText(name)
        Log.i("RestaurantsAdapter", "restaurantSelected: " + restaurantSelected + ", restaurantName: " + selectableItem.restaurantName)
        if (!restaurantSelected.isNullOrBlank() && restaurantSelected.equals(selectableItem.restaurantName)) {
            holder.textView.setChecked(true)
        } else{
            holder.textView.setChecked(false)
        }
        val value = TypedValue()
        holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true)
        val checkMarkDrawableResId = value.resourceId
        holder.textView.setCheckMarkDrawable(checkMarkDrawableResId)

        holder.mItem = selectableItem

        if (selectableItem.isSelected) {
            holder.setColor(Color.LTGRAY)
        } else {
            holder.setColor(Color.DKGRAY)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }


    override fun onItemSelected(item: SelectableRestaurant?) {
        for (selectableItem in mValues) {
             if (selectableItem.equals(item) && selectableItem.isSelected) {
                selectableItem.isSelected = true
                 restaurantSelected = item!!.restaurantName
            } else if(selectableItem.equals(item) && !selectableItem.isSelected){
                 selectableItem.isSelected = false
                 restaurantSelected = null
             }
             else{
                 selectableItem.isSelected = false
             }
        }
        notifyDataSetChanged()

        listener.onItemSelected(item)    }
}