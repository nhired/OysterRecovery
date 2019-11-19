package com.example.oysterrecovery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.content_route.*

class RouteActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val mRestaurantRef = database.getReference("restaurants")
    val resList = ArrayList<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        setSupportActionBar(toolbar)

        mRestaurantRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read restaurant")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(postSnapshot in p0.children){
                    val key = postSnapshot.key
                    val restaurant = postSnapshot.getValue(Restaurant::class.java)
                    resList.add(restaurant!!)
                    Log.d(TAG, "RestaurantID is $key, Restaurant Attributes are $restaurant")
                }
            }
        })
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        button.setOnClickListener {
            val int =  Intent(this, MyRouteActivity::class.java)
            startActivity(int)
        }
    }
    
    companion object {
        val TAG = "FirebaseDB Reading"
    }
}
