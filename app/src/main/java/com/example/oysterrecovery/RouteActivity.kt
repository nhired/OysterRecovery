package com.example.oysterrecovery

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
<<<<<<< HEAD
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
=======
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oysterrecovery.Adapter.MyAdapter
import com.google.firebase.database.*
>>>>>>> ad5bf611284a53c5b2e57f4b3ca6c0e114a73f27

import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.content_route.*

class RouteActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val mRestaurantRef = database.getReference("restaurants")
    val resList = ArrayList<Restaurant>()
<<<<<<< HEAD
    lateinit var mRecyclerView: RecyclerView
=======

    val ITEM_COUNT = 10
    var totalItem = 0
    var lastVisible = 0

    lateinit var adapter: MyAdapter

    var isLoading = false
    var isMaxData = false

    var lastNode:String? = ""
    var lastKey :String? = ""


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.route_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item!!.itemId
        if(id == R.id.refresh) {

            isMaxData = false
            lastNode = adapter.lastItemId
            adapter.notifyDataSetChanged()
            getLastKey()
            getRoutes()

        }
        return true
    }









>>>>>>> ad5bf611284a53c5b2e57f4b3ca6c0e114a73f27
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
       // setSupportActionBar(toolbar)

        getLastKey()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

<<<<<<< HEAD
        var hashMap : HashMap<String, String> = HashMap<String, String> ()
        mRestaurantRef.addValueEventListener(object: ValueEventListener {
=======
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter = MyAdapter(this)
        recyclerView.adapter = adapter

        getRoutes()

        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItem = layoutManager.itemCount
                lastVisible = layoutManager.findLastVisibleItemPosition()

                if(!isLoading && totalItem <= lastVisible + ITEM_COUNT) {
                    getRoutes()
                    isLoading = true
                }
            }
        })

//        button.setOnClickListener {
//            val int =  Intent(this, MyRouteActivity::class.java)
//            startActivity(int)
//        }
    }

    private fun getRoutes() {
        if(!isMaxData) {
            val query:Query

            if(TextUtils.isEmpty(lastNode))
                query = FirebaseDatabase.getInstance().reference
                    .child("routes")
                    .orderByKey()
                    .limitToFirst(ITEM_COUNT)
            else
                query = FirebaseDatabase.getInstance().reference
                    .child("routes")
                    .orderByKey()
                    .startAt(lastNode)
                    .limitToFirst(ITEM_COUNT)


            query.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.hasChildren()) {
                        val routeList = ArrayList<String>()
                        for(snapshot in p0.children)
                            routeList.add(snapshot.getValue(String::class.java)!!)

                        lastNode = routeList[routeList.size - 1]

                        if(!lastNode.equals(lastKey))
                            routeList.removeAt(routeList.size -1)
                        else
                            lastNode = "end"

                        adapter.addAll(routeList)
                        isLoading = false
                    } else {
                        isLoading = false
                        isMaxData = true
                    }
                }

            })
        }
    }


    private fun getLastKey() {
        var keyRef = FirebaseDatabase.getInstance().getReference()
            .child("routes")
            .orderByKey()
            .limitToLast(1)

        keyRef.addListenerForSingleValueEvent(object: ValueEventListener{
>>>>>>> ad5bf611284a53c5b2e57f4b3ca6c0e114a73f27
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
<<<<<<< HEAD
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
=======
                for(userSnapShot in p0.children)
                     lastKey = userSnapShot.key
>>>>>>> ad5bf611284a53c5b2e57f4b3ca6c0e114a73f27
            }
        })

<<<<<<< HEAD
        button.setOnClickListener {
            val int =  Intent(this, MyRouteActivity::class.java)
            startActivity(int)
        }
        Log.d(TAG, "$hashMap")
        setContentView(R.layout.content_my_route)
        mRecyclerView = findViewById(R.id.route3)
        RecyclerView_Config().setConfig(mRecyclerView, this@RouteActivity, , hashMap.keys.toList())
    }

=======
    }





    
>>>>>>> ad5bf611284a53c5b2e57f4b3ca6c0e114a73f27
    companion object {
        val TAG = "FirebaseDB Reading"
    }
}
