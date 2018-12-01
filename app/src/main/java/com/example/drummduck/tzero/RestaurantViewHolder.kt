package com.example.drummduck.tzero

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.CheckedTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

/**
 * This sets the onClickListener for each card and background colors as well
 */
class RestaurantViewHolder
/**
 * Sets onClickListener for the card.
 * @param view
 * @param listener
 */
    (view: View, internal var itemSelectedListener: OnItemSelectedListener) : RecyclerView.ViewHolder(view) {
    internal var textView: CheckedTextView
    internal var mItem: SelectableRestaurant? = null


    init {
        textView = view.findViewById(R.id.checked_text_item)
        textView.setOnClickListener(object : View.OnClickListener {
            override  fun onClick(view: View) {


               if(mItem!!.isSelected) {
                    setChecked(false)
                } else
                    setChecked(true)
                itemSelectedListener.onItemSelected(mItem)

            }
        })
    }

    /**
     * Sets color scheme and selection of card
     * @param value
     */
    fun setChecked(value: Boolean) {

        var color = -1
        val background = textView.background
        if (background is ColorDrawable)
            color = background.color
        if (color == Color.LTGRAY) {
            textView.setBackgroundColor(Color.DKGRAY)
            Log.i("RestarauntViewHolder", "Changing to DKGRAY!")
        } else if (color == Color.DKGRAY) {
            textView.setBackgroundColor(Color.LTGRAY)
            Log.i("RestarauntViewHolder", "Changing to LTGRAY!")
        } else {
            textView.setBackgroundColor(Color.LTGRAY)
            Log.i("RestarauntViewHolder", "Changing to DEFAULT LTGRAY!")
        }

        mItem!!.isSelected = value
        textView.isChecked = value
    }

    fun setColor(color: Int) {
        textView.setBackgroundColor(color)
    }

    /**
     * Used in DeviceSelection to decide what to do when selecting item.
     */
    interface OnItemSelectedListener {

        fun onItemSelected(item: SelectableRestaurant?)
    }

}