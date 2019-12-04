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

                    if(rList.contains(keyNode.key)){
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(anna))

        mMap.setMinZoomPreference(12.0f)
        mMap.setMaxZoomPreference(20.0f)
    }

    fun optimizeRoutes() {
        //Read in completed routes
        val routes = FirebaseDatabase.getInstance().getReference("routes")
        val rests = FirebaseDatabase.getInstance().getReference("restaurants")
        var end = false
        routes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //Find routeID
                var routeID = "200"
                for (keyNode in dataSnapshot.children) {
                    if(end) {
                        break
                    }
                    //textView4.text = keyNode.value.toString()
                    val c = keyNode.value as String
                    // h holds all restIDS in the route
                    val h = c.split(",")
                    // Check which route the completed ones were from
                    if(h.contains(rList.first())){
                        //Found the route
                        routeID = keyNode.key as String
                        val origRoute = keyNode.value.toString()
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
                            end = true
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

                            //Get current rest location
                            val rest = resList.get(toRet)
                            //textView4.text = rest?.address.toString()
                            val geocoder = Geocoder(this@FinishActivity)
                            val restLocation = geocoder.getFromLocationName(rest?.address.toString(),1)

                            var min = 1000f;
                            var minID = "";

                            var holder2 = origRoute.split(",")


                            val listo = listOf<String>(holder2[0],holder2[1],holder2[2])

                            for(entry in resList) {
                                if(!listo.contains(entry.key)){
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
                                    end = true;
                                    //TODO:ONLY UNCOMMENT DURING DEMO
                                    //routes.child(routeID).removeValue()
                                    break;
                                }

                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


    }

}
