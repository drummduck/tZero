package com.example.drummduck.tzero

import java.lang.StringBuilder

/**
 * Holds all data to be used for querying restaurant map and for display on fragment.
 * @author: Nathan Donaldson
 */
class Restaurant(var resID : Int, var name : String, var url : String, var address : String, var cuisines : String,
                 var rating : Double, var hasDeliver : Boolean, var hasReservation : Boolean, var menuUrl : String){

    constructor() : this(-1, "N/A", "N/A", "N/A", "N/A", -1.0,
        false, false, "N/A" )

    override fun toString(): String {
      var builder = StringBuilder()
        builder.append("Name: " + name + "\n\n")
        builder.append("Address: " + address + "\n\n")
        builder.append("Zomato Site: " + url + "\n\n")
        builder.append("Zomato Menu: " + menuUrl + "\n\n")
        builder.append("Cuisines:: " + cuisines + "\n\n")
        builder.append("Rating: " + rating.toString() + "\n\n")
        builder.append("Delivery: " + (if(hasDeliver) "yes" else "no") + "\n\n")
        builder.append("hasReservation: " + (if(hasReservation) "yes" else "no"))

        return builder.toString()
    }
}