package com.example.oysterrecovery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerView_Config {
    // Helps us make calls for activities method
    lateinit var mContext: Context
    lateinit var mRestaurantAdapter: RestaurantAdapter

    fun setConfig(recyclerView: RecyclerView, context: Context, restaurants: List<Restaurant>, keys: List<String>){
        mContext = context
        mRestaurantAdapter = RestaurantAdapter(restaurants, keys)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mRestaurantAdapter

    }

    // Class is responsible for inflating the layout restaurant list item and populating its view objects
    class RestaurantItemView : RecyclerView.ViewHolder {
        lateinit var mName: TextView
        lateinit var mAddress: TextView
        lateinit var mOysCapacity: TextView
        lateinit var mOysNumber: TextView

        lateinit var key: String

        constructor(itemView: ViewGroup) : super(itemView){
            LayoutInflater.from(RecyclerView_Config().mContext).inflate(R.layout.restaurant_list_item, itemView, false)

            mName = itemView.findViewById(R.id.resName_txtView)
            mAddress = itemView.findViewById(R.id.resAddress_txtView)
            mOysCapacity = itemView.findViewById(R.id.oysCap_txtView)
            mOysNumber = itemView.findViewById(R.id.oysNum_txtView)

        }

        fun bind(res: Restaurant, key: String){
            mName.setText(res.name)
            mAddress.setText(res.address)
            mOysCapacity.setText(res.oysterCapacity)
            mOysNumber.setText(res.oysterNumber)
            this.key = key
        }
    }

    // Responsible for creating restaurant item view and passing restaurant object and key to bind method
    class RestaurantAdapter: RecyclerView.Adapter<RestaurantItemView> {
        lateinit var mResList: List<Restaurant>
        lateinit var mKeys: List<String>

        constructor(mResList: List<Restaurant>, mKeys: List<String>) : super() {
            this.mResList = mResList
            this.mKeys = mKeys
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemView {
            return RestaurantItemView(parent)
        }

        override fun onBindViewHolder(holder: RestaurantItemView, position: Int) {
            holder.bind(mResList.get(position), mKeys.get(position))
        }

        override fun getItemCount(): Int {
            return mResList.size
        }
    }
}