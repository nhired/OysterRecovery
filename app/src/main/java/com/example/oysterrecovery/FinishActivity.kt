package com.example.oysterrecovery

import android.content.Intent
import android.location.Geocoder
import android.location.Address
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_finish.*
import kotlinx.android.synthetic.main.content_finish.*
import com.google.firebase.database.DatabaseError




class FinishActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var rList = mutableListOf<String>()
    private var aList = mutableListOf<String>()
    val resList = HashMap<String, Restaurant?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.hide()
        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)


        //Initialize rList

        if(!intent.getStringExtra("resOne").isNullOrBlank()){
            rList.add(intent.getStringExtra("resOne"))
        }
        if(!intent.getStringExtra("resTwo").isNullOrBlank()){
            rList.add(intent.getStringExtra("resTwo"))
        }
        if(!intent.getStringExtra("resThree").isNullOrBlank()){
            rList.add(intent.getStringExtra("resThree"))
        }
        if(!intent.getStringExtra("resFour").isNullOrBlank()){
            rList.add(intent.getStringExtra("resFour"))
        }

        button4.setOnClickListener {
            //Calls reoptimize, then resets back to routes
            optimizeRoutes()
            val int =  Intent(this, RouteActivity::class.java)
            startActivity(int)
        }
        //Retrieve oyster count from intent, uncomment when merging.
        textView4.text = "You helped save " + intent.getStringExtra("TOTAL_OYSTERS") + " oysters!"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Read in completed routes
        val rests = FirebaseDatabase.getInstance().getReference("restaurants")
        // Add a marker in annapolis and move the camera
        val geocoder = Geocoder(this)

        rests.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (keyNode in dataSnapshot.children) {
                    val restaurant = keyNode.getValue<Restaurant>(Restaurant::class.java)
                    resList.put(keyNode.key!!, restaurant)
                }

                for (keyNode in dataSnapshot.children) {
                    //As long as they're not in the same route, add the address to the aList
//                    if(!rList.contains(keyNode.key)){
//                        aList.add(keyNode.child("address").value.toString())
//                    }


                    if(rList.contains(keyNode.key)){
                        //textView4.text = intent.getStringExtra("resOne")
                        //"3,2,14"
                        //rests.child("1").setValue("3,2,14")
                        val addresses:List<Address> = geocoder.getFromLocationName(keyNode.child("address").value.toString(), 1)
                        if (addresses.isNotEmpty())
                        {
                            val latitude = addresses[0].latitude
                            val longitude = addresses[0].longitude
                            mMap.addMarker(MarkerOptions().position(LatLng(latitude,longitude)).title(keyNode.child("name").value.toString())).showInfoWindow()
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })




        val anna = LatLng(38.97, -76.49)
//        val anna2 = LatLng(38.99, -76.50)
//        val anna3 = LatLng(38.95,-76.51)
//        mMap.addMarker(MarkerOptions().position(anna).title("Oyster City")).showInfoWindow()
//        mMap.addMarker(MarkerOptions().position(anna2).title("Shell Shack"))
//        mMap.addMarker(MarkerOptions().position(anna3).title("Joe's Oysters"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(anna))

        mMap.setMinZoomPreference(12.0f)
        mMap.setMaxZoomPreference(20.0f)
    }

    fun optimizeRoutes() {
        //Read in completed routes
        val routes = FirebaseDatabase.getInstance().getReference("routes")
        val rests = FirebaseDatabase.getInstance().getReference("restaurants")
        routes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //Find routeID
                var routeID = "200"
                for (keyNode in dataSnapshot.children) {

                    //textView4.text = keyNode.value.toString()
                    val c = keyNode.value as String
                    // h holds all restIDS in the route
                    val h = c.split(",")
                    // Check which route the completed ones were from
                    if(h.contains(rList.first())){
                        //Found the route
                        routeID = keyNode.key as String
                        if(rList.size == 1) {
                            var toRet = ""
                            for(rest in h) {
                                if(!rList.contains(rest)) {
                                    toRet += rest + ","
                                }
                            }
                            toRet = toRet.dropLast(1)
                            //Gets rid of last comma
                            routes.child(routeID).setValue(toRet)
                            //textView4.text = toRet
                            break;

                        }else if(rList.size == 2) {
                            var toRet = ""
                            for(rest in h) {
                                if(!rList.contains(rest)) {
                                    toRet += rest + ","
                                }
                            }
                            //This is the only restuarant not in a route now.
                            toRet = toRet.dropLast(1)
                            routes.child(routeID).removeValue()

                            //Get current rest location
                            val rest = resList.get(toRet)
                            val geocoder = Geocoder(this@FinishActivity)
                            val restLocation = geocoder.getFromLocationName(rest!!.address,1)

                            var min = 1000f;
                            var minID = "";

                            for(entry in resList) {
                                if(!rList.contains(entry.key)){
                                    val latitude = restLocation[0].latitude
                                    val longitude = restLocation[0].longitude

                                    val compLocation = geocoder.getFromLocationName(entry.value!!.address,1)
                                    if(compLocation.isNotEmpty()){
                                        val lat2 = compLocation[0].latitude
                                        val long2 = compLocation[0].longitude

                                        val results = FloatArray(3)
                                        Location.distanceBetween(latitude,longitude,lat2,long2,results)
                                        if(min > results[0]){
                                            min = results[0]
                                            minID = entry.key
                                        }
                                    }
                                }
                            }

                            for (kn in dataSnapshot.children) {
                                val values = kn.value as String
                                // h holds all restIDS in the route
                                val hold = values.split(",")
                                if(hold.contains(minID)) {
                                    routes.child(kn.key as String).setValue(kn.value.toString() + "," + toRet)
                                    break;
                                }

                            }
                            //Figure out which route has a closest neighbor
                        }
//                        routeID = keyNode.key as String
//
//                        textView4.text = routeID
                    }
                }

//                if(rList.size == 1) {
//                    //Delete the completed place, leave the rest
//                }else if(rList.size == 2) {
//                    //Check each route to find one that is less than X meters away and add, if can't find, use MIN
//                    for (keyNode in dataSnapshot.children) {
//                        if(keyNode.key == "1"){
//                            //textView4.text = keyNode.value.toString()
//                            //"3,2,14"
//                            //routes.child("1").setValue("3,2,14")
//
//
//                        }
//                    }
//                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        //Case 1: All routes completed
//        if(rList!!.size == 3){
//            //Remove route from firebase
//            //snapshot
//
//        }else if(rList!!.size == 1){
//            //Case 2: 1 Route is Completed
//            //Remove completed route, update route entry in fb
//        }else if(rList!!.size == 2) {
//            //Case 3: Add the remaining route to nearest cluster
//
//        }
    }

}
