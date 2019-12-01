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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oysterrecovery.Adapter.MyAdapter
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.android.synthetic.main.content_route.*

class RouteActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val mRestaurantRef = database.getReference("restaurants")
    val resList = ArrayList<Restaurant>()

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
       // setSupportActionBar(toolbar)

        getLastKey()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)


        adapter = MyAdapter(this, resList)
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
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(userSnapShot in p0.children)
                     lastKey = userSnapShot.key
            }
        })

    }


    companion object {
        val TAG = "FirebaseDB Reading of Routes"
    }
}
