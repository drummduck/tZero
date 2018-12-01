package com.example.drummduck.tzero

/**
 * Wrapper to identify if viewHolder is selected
 * Created by ndonaldson on 5/21/18.
 */

class SelectableRestaurant(var item: RestaurantSelectItem, var isSelected: Boolean) : RestaurantSelectItem(item.restaurantName, item.restaurantAddress) {

    init {
        this.isSelected = isSelected
        this.item = item
    }
}