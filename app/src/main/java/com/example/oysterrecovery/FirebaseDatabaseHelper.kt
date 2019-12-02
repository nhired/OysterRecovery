package com.example.oysterrecovery

import com.google.firebase.database.*

class FirebaseDatabaseHelper {
    var mDatabase: FirebaseDatabase
    var mReferenceRestaurant: DatabaseReference
    var restaurants: MutableList<Restaurant>

    interface DataStatus {
        fun DataIsLoaded(listOfRes: List<Restaurant>, listOfKeys: List<String>)
        fun DataIsInserted()
        fun DataIsUpdated()
        fun DataIsDeleted()
    }

    constructor() {
        this.mDatabase = FirebaseDatabase.getInstance()
        this.mReferenceRestaurant = mDatabase.getReference("restaurants")
        this.restaurants = ArrayList()
    }

    fun readRestaurants(dStatus: DataStatus){
        mReferenceRestaurant.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                restaurants.clear()
                var keys: MutableList<String> = ArrayList()
                for(keyNode in dataSnapshot.children){
                    keys.add(keyNode.key!!)
                    val restaurant = keyNode.getValue<Restaurant>(Restaurant::class.java)
                    restaurants.add(restaurant!!)
                }
                dStatus.DataIsLoaded(restaurants, keys)
            }

            override fun onCancelled(p0: DatabaseError) {
                // Required
            }
        })
    }
}