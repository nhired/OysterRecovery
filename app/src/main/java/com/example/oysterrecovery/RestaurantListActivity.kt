package com.example.oysterrecovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class RestaurantListActivity: AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        mRecyclerView = findViewById(R.id.recyclerview_restaurants)
        FirebaseDatabaseHelper().readRestaurants(object: FirebaseDatabaseHelper.DataStatus {
            override fun DataIsLoaded(listOfRes: List<Restaurant>, listOfKeys: List<String>) {
                RecyclerView_Config().setConfig(mRecyclerView, this@RestaurantListActivity, listOfRes, listOfKeys)
            }

            override fun DataIsInserted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun DataIsUpdated() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun DataIsDeleted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
