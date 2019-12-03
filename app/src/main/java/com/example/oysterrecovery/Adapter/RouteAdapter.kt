package com.example.oysterrecovery.Adapter

import android.content.Context
import android.content.Intent
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.oysterrecovery.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class RouteAdapter(internal var context: Context) : RecyclerView.Adapter<RouteAdapter.RouteHolder>() {


    private var routes: MutableList<String> = ArrayList()


    fun addAll(newRoutes: List<String>) {
        val init =  routes.size
        routes.addAll(newRoutes)
        notifyItemRangeChanged(init, newRoutes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.route_item, parent, false)
        return RouteHolder(itemView)
    }

    override fun getItemCount() = routes.size

    override fun onBindViewHolder(holder: RouteHolder, position: Int)  {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("restaurants")


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Entered", "The ondata change method")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<Restaurant?>()

                for (keyNode in p0.children) {
                    val restaurant = keyNode.getValue<Restaurant>(Restaurant::class.java)
                    list.add(restaurant)
                }

//                for(r in list) {
//                    Log.d(TAG, r!!.name)
//                }

                val arr = routes[position].split(',')

                val r1 = list.get(arr[0].toInt() - 1)
                val r2 = list.get(arr[1].toInt() - 1)
                if(arr.size > 2){
                    val r3 = list.get(arr[2].toInt() - 1)
                    holder.r3.text = r3!!.name
                }else{
                    holder.r3.text = ""
                }
                holder.route.text = r1!!.name + " Route"

                holder.r1.text = r1!!.name
                holder.r2.text = r2!!.name
                if(arr.size == 4) {
                    val r4 = list.get(arr[3].toInt() - 1)
                    holder.r4.text = r4!!.name
                } else {
                    holder.r4.text = ""

                }

            }

        })


        val arr = routes[position].split(',')
        holder.btn.setOnClickListener{
            val intent = Intent(context, MyRouteActivity::class.java)
            intent.putExtra("resOne", arr[0])
            intent.putExtra("resTwo", arr[1])
            intent.putExtra("resThree", arr[2])

            if(arr.size == 4) {
                intent.putExtra("resFour", arr[3])
            } else {
                intent.putExtra("resFour", "")

            }

            context.startActivity(intent)
        }

        Picasso.get().load(holder.imageUri).into(holder.ivBasicImage);




    }


    class RouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var route: TextView = itemView.findViewById(R.id.route_item)
        internal var routeIcon : ImageView = itemView.findViewById(R.id.routeIcon)

        internal var r1: TextView = itemView.findViewById(R.id.restaurant1)
        internal var r2: TextView = itemView.findViewById(R.id.restaurant2)
        internal var r3: TextView = itemView.findViewById(R.id.restaurant3)
        internal var r4: TextView = itemView.findViewById(R.id.restaurant4)

        private var relativelay: RelativeLayout = itemView.findViewById(R.id.expandableView)
        private var card: CardView = itemView.findViewById(R.id.cardView)

        val imageUri = "https://cdn1.iconfinder.com/data/icons/business-e-commerce-logistics-full-colours-set-1/91/Business_E-commerce__Logistics_C-29-512.png"
        val ivBasicImage = itemView.findViewById(R.id.routeIcon) as ImageView

        internal var btn = itemView.findViewById<Button>(R.id.startRoutebtn)



        init {
            itemView.setOnClickListener(this)

        }

        //4
        override fun onClick(v: View) {
            if(relativelay.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(card, AutoTransition())
                relativelay.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(card, AutoTransition())
                relativelay.visibility = View.GONE
            }


        }

    }

    companion object {
        val TAG = "Route Adapter"
    }



}

