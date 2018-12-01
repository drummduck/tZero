package com.example.drummduck.tzero

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

private const val DATA = "data"

/**
 * A fragment that holds a TextView and Button.
 * Shows restaraunt info on click from recyclerview
 */
class RestaurantFragment : Fragment() {
    private var param1: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var textView : TextView? = null
    private var backButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_restaurant_info, container, false)
        textView = view.findViewById(R.id.restaurantString)
        backButton = view.findViewById(R.id.backButton)

        backButton!!.setOnClickListener{
            listener!!.onFragmentInteraction()
        }

        textView!!.setText(param1)
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }

    companion object {

        @JvmStatic
        fun newInstance(data: String) =
            RestaurantFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA, data)
                }
            }
    }
}
