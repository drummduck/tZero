package com.example.drummduck.tzero

/**
 * This is the information held for a card in the recyclerview
 */
open class RestaurantSelectItem(var restaurantName: String?, var restaurantAddress : String?) {

    override fun equals(obj: Any?): Boolean {
        if (obj == null)
            return false

        val itemCompare = obj as RestaurantSelectItem?
        return if (itemCompare!!.restaurantName == this.restaurantName && itemCompare!!.restaurantAddress == this.restaurantAddress)
            true else false

    }
}