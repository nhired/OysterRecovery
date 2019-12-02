package com.example.oysterrecovery.Adapter

import android.content.Context
import android.renderscript.Sampler
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oysterrecovery.R
import com.example.oysterrecovery.Restaurant
import com.example.oysterrecovery.RouteActivity.Companion.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.route_item.view.*

class MyAdapter(internal var context: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var routeList: MutableList<String> = ArrayList()

    private val database = FirebaseDatabase.getInstance()
    private val mRestaurantRef = database.getReference("restaurants")

    val lastItemId:String?
        get() = routeList[routeList.size - 1]

    fun addAll(newRoutes: List<String>) {
        val init =  routeList.size
         routeList.addAll(newRoutes)
        notifyItemRangeChanged(init, newRoutes.size)
    }

    fun removeLastItem() {
        routeList.removeAt(routeList.size - 1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView: View = LayoutInflater.from(context).inflate(R.layout.route_item, parent, false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.route.text = routeList[position]


        val routes = routeList[position]
        val arr = routes.split(',')

        val id1 = arr[0]
        val id2 = arr[1]
        val id3 = arr[2]

       // holder.r1.text = resList.get(id1.toInt()).name
       // holder.r2.text = resList.get(id2.toInt()).name
        //holder.r3.text = resList.get(id3.toInt()).name
       // holder.r3.text = ""

    }

    private fun getRestaurants() {
        mRestaurantRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(postSnapshot in p0.children) {
                    val key = postSnapshot.key
                    val res = postSnapshot.getValue(Restaurant::class.java)
                   // resList.add(res!!)
                }
            }

        })
    }


     class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
         internal var route: TextView = itemView.findViewById(R.id.route_item)


//         internal var r1: TextView = itemView.findViewById(R.id.restaurant1)
//         internal var r2: TextView = itemView.findViewById(R.id.restaurant2)
//         internal var r3: TextView = itemView.findViewById(R.id.restaurant3)
//         internal var r4: TextView = itemView.findViewById(R.id.restaurant4)

       //  internal var layout: LinearLayout = itemView.findViewById(R.id.expandableLay)

         init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d(TAG, "Item was CLICKED " )

        }


         companion object {
             //5
             private val TAG = "My View Holder"
         }

    }

    companion object {
        //5
         val TAG = "My Adapter"
    }
}