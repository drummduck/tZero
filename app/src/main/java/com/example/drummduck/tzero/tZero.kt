package com.example.drummduck.tzero

import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.os.Handler
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import android.support.v7.widget.LinearLayoutManager

/**
 * This activity has a RecyclerView of restaurants in an area and their addresses based on a lat/long
 * search in an EditText with a Button to fire it off.
 */
class tZero : AppCompatActivity(), RestaurantViewHolder.OnItemSelectedListener, RestaurantFragment.OnFragmentInteractionListener {

    private lateinit var recycler: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var searchBar: EditText
    private lateinit var restaurants : HashMap<String, Restaurant>
    private lateinit var recyclerRestaurants : MutableList<RestaurantSelectItem>
    private lateinit var mContext : Context
    private lateinit var progressBar: ProgressBar
    private lateinit var selectedRestaurant: String
    private lateinit var adapter : RestaurauntsAdapter

    private var lat: Double = 0.0
    private var long: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_t_zero)

        val clickListener = View.OnClickListener { view ->

            when (view.getId()) {
                R.id.searchButton -> {
                    Log.i("tZero", "SearchButton clicked")
                    searchArea()
                }
            }
        }

        recycler = findViewById(R.id.restaurants)
        searchButton = findViewById(R.id.searchButton)
        searchButton.setOnClickListener(clickListener)
        searchBar = findViewById(R.id.searchBar)
        progressBar = findViewById(R.id.progressBar)
        restaurants = HashMap()
        recyclerRestaurants = mutableListOf()
        mContext = this

        val layoutManager = LinearLayoutManager(this)
        recycler.setLayoutManager(layoutManager)
    }


    /**
     * Parses the text in the search box for format: (lat, long)
     * Makes API search call to Zomato for restaurants in that area
     * Adds certain restaurant information parsed from reply into a mutable list
     * On success it sets the adapter for the RecyclerView
     */
    fun searchArea(){
        restaurants.clear()

        Log.i("tZero", "searchBarText: " + searchBar.text)
        var list : List<String> = searchBar.text.split(",\\s+".toRegex())
        try {
            lat = list[0].toDouble()
            long = list[1].toDouble()
        }catch (e: Exception){
            Log.i("tZero", e.localizedMessage)
            Toast.makeText(mContext, "Lat/Long was not in the correct format...", Toast.LENGTH_LONG).show()
        }

        val LOCATION_SEARCH = "https://developers.zomato.com/api/v2.1/search?lat=${lat}&lon=${long}"
        val client = OkHttpClient()
        val mHandler = Handler()
        val request = Request.Builder().addHeader("user-key", "9df6fd1f11027f31e905c2983e9704eb")
            .url(LOCATION_SEARCH)
            .build()

        client.newCall(request).enqueue(object : Callback  {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("tZero", e.localizedMessage)
                var runnable = Runnable {
                    Toast.makeText(mContext, "Error while performing search...", Toast.LENGTH_LONG).show()
                    progressBar.setVisibility(View.INVISIBLE)
                }
                mHandler!!.post { runnable.run() }

            }
            override fun onResponse(call: Call, response: Response) {
               //Log.i("tZero", (response.body()?.string()))
                try {
                    if (response == null) {
                        Log.i("tZero", "response was null")
                        var runnable = Runnable {
                            Toast.makeText(mContext, "Nothing found in search...", Toast.LENGTH_LONG).show()
                            progressBar.setVisibility(View.INVISIBLE)
                        }
                        mHandler!!.post { runnable.run() }
                    } else {
                        var restaurant = Restaurant()
                        var json = JSONObject(response.body()?.string())
                        var restaurantsArray = json.getJSONArray("restaurants")
                        var restaurantObject: JSONObject
                        var outerRestaurantObject : JSONObject
                        var locationObject: JSONObject
                        var userRatingObject: JSONObject
                        Log.i("tZero", "restaurants length: " + restaurantsArray.length())

                        for (i in 0 until restaurantsArray.length()) {
                                outerRestaurantObject = restaurantsArray.getJSONObject(i)
                                restaurantObject = outerRestaurantObject.getJSONObject("restaurant")
                                restaurant.cuisines = restaurantObject.optString("cuisines", "N/A")
                                restaurant.name = restaurantObject.optString("name", "N/A")
                                restaurant.resID = restaurantObject.optInt("id", -1)
                                restaurant.url = restaurantObject.optString("url", "N/A")
                                restaurant.hasDeliver = restaurantObject.optBoolean("has_online_delivery", false)
                                restaurant.hasReservation = restaurantObject.optBoolean("is_table_reservation_supported", false)
                                restaurant.menuUrl = restaurantObject.optString("menu_url", "N/A")

                                locationObject = restaurantObject.getJSONObject("location")
                                restaurant.address = locationObject.optString("address", "N/A")

                                userRatingObject = restaurantObject.getJSONObject("user_rating")
                                restaurant.rating = userRatingObject.optDouble("aggregate_rating", -1.0)

                                Log.i("tZero", restaurant.toString())
                                restaurants.put(restaurant.name, restaurant)
                                recyclerRestaurants.add(RestaurantSelectItem(restaurant.name, restaurant.address))
                            }

                        var runnable = Runnable {
                            progressBar.setVisibility(View.INVISIBLE)
                            setAdapter()
                        }
                        mHandler!!.post { runnable.run() }
                    }
                } catch (e : Exception){
                    Log.i("tZero", e.localizedMessage)
                    var runnable = Runnable {
                        Toast.makeText(mContext, "Error while performing search...", Toast.LENGTH_LONG).show()
                        progressBar.setVisibility(View.INVISIBLE)
                    }
                    mHandler!!.post { runnable.run() }

                }
            }
        })
        progressBar.setVisibility(View.VISIBLE)
    }

    fun String.toDouble(): Double = java.lang.Double.parseDouble(this)

    /**
     * Sets the adapter for the RecyclerView based on the json parsed from api call
     */
    fun setAdapter(){
        Log.i("tZero", "Setting adapter")
        adapter = RestaurauntsAdapter(this, recyclerRestaurants, "")
        recycler.adapter = RestaurauntsAdapter(this, recyclerRestaurants, "")
    }

    /**
     * Opens the info of the restaurant on a fragment based on the name from the card
     */
    override fun onItemSelected(item: SelectableRestaurant?) {
        Log.i("tZero", "Item Selected!")
        if(item != null) selectedRestaurant = item!!.restaurantName!!
        val fragment = RestaurantFragment.newInstance(restaurants.get(selectedRestaurant).toString())
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_from_top,
            R.anim.slide_to_top,
            R.anim.slide_from_top,
            R.anim.slide_to_top
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.add(R.id.restaurantFragment, fragment, "restaurantFragment").commit()
    }

    override fun onFragmentInteraction() {
        getSupportFragmentManager().popBackStack()
    }
}
