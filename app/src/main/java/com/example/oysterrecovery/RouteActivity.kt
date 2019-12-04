
package com.example.oysterrecovery

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oysterrecovery.Adapter.RouteAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_route.*
import kotlinx.android.synthetic.main.activity_route.*

class RouteActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RouteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        //setSupportActionBar(toolbar)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar);
//        toolbar!!.title = "Active Routes"

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RouteAdapter(this)
        recyclerView.adapter = adapter

        Log.d(TAG, "Entered beginning method!!!111")
        requestRoutes()

    }

    private fun requestRoutes() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("routes")

        // Attach a listener to read the data at our posts reference
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "Entered onDataChange method!")
                val routeList = ArrayList<String>()

                for(snapshot in dataSnapshot.children) {
                    Log.d("Testing the key of the route", snapshot.key)
                    routeList.add(snapshot.getValue<String>(String::class.java)!!)
                }

                adapter.addAll(routeList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }


    companion object {
        val TAG = "FirebaseDB Route Reading"
    }
}
