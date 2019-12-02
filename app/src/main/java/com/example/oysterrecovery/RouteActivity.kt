
package com.example.oysterrecovery

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oysterrecovery.Adapter.RouteAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_route.*


class RouteActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RouteAdapter
    private var routeList: ArrayList<Route> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RouteAdapter(this)
        recyclerView.adapter = adapter

        Log.d(TAG, "Entered beginning method!!!111")

    }

    override fun onStart() {
        super.onStart()
        if (routeList.size == 0) {
            requestRoutes()
        }
    }

    private fun requestRoutes() {
        Log.d(TAG, "Entered begining of request routes")
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("routes")

        // Attach a listener to read the data at our posts reference
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "Entered onDataChange method!")
                val routeList = ArrayList<String>()

                for(snapshot in dataSnapshot.children) {
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
