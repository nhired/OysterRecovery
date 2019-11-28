package com.example.oysterrecovery.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oysterrecovery.R
import kotlinx.android.synthetic.main.route_item.view.*

class MyAdapter(internal var context: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var routeList: MutableList<String> = ArrayList()

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
        holder.miles.text = "0.5"

    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         internal var route: TextView = itemView.findViewById(R.id.route_item)
          internal var miles: TextView = itemView.findViewById(R.id.milesAway)

    }
}