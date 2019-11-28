package com.example.oysterrecovery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var mRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        setSupportActionBar(toolbar)

        var hashMap : HashMap<String, String> = HashMap<String, String> ()
        mRestaurantRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read restaurant")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(postSnapshot in p0.children){
                    val key = postSnapshot.key
                    val restaurant = postSnapshot.getValue(Restaurant::class.java)
                    resList.add(restaurant!!)
                    val addy = restaurant.address
                    val name = restaurant.name
                    val capacity = restaurant.oysterCapacity
                    val num = restaurant.oysterNumber
                    hashMap.put(name, addy)
                    Log.d(TAG, "RestaurantID is $key")
                    Log.d(TAG, "Resaturant addy is $addy\n Restaurant name is $name\n" +
                            "Restaurant capacity is $capacity\n Restaurant oyster amount is $num ")
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
        Log.d(TAG, "$hashMap")
        setContentView(R.layout.content_my_route)
        mRecyclerView = findViewById(R.id.route3)
        RecyclerView_Config().setConfig(mRecyclerView, this@RouteActivity, , hashMap.keys.toList())
    }

    companion object {
        val TAG = "FirebaseDB Reading"
    }
}
