package com.example.oysterrecovery

import android.content.Intent
import android.location.Geocoder
import android.location.Address
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


class FinishActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.hide()
        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        button4.setOnClickListener {
            //Calls reoptimize, then resets back to routes
            optimizeRoutes()
            val int =  Intent(this, MainActivity::class.java)
            startActivity(int)
        }
        //Retrieve oyster count from intent, uncomment when merging.
        textView4.text = "You helped save " + intent.getStringExtra("TOTAL_OYSTERS") + " oysters!"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Read in completed routes
        var rList = intent.getStringArrayListExtra("COMPLETED_ROUTES")
//        for(rest in rList) {
//            //Replace coord with firebase data
//            mMap.addMarker(MarkerOptions().position(LatLng(38.97, -76.49)).title(rest)).showInfoWindow()
//        }
        // Add a marker in annapolis and move the camera

        val geocoder = Geocoder(this)
        val addresses:List<Address>
        addresses = geocoder.getFromLocationName("37 West St, Annapolis, MD 21401", 1)
        if (addresses.isNotEmpty())
        {
            val latitude = addresses.get(0).getLatitude()
            val longitude = addresses.get(0).getLongitude()
            mMap.addMarker(MarkerOptions().position(LatLng(latitude,longitude)).title("Stan & Joes Saloon West")).showInfoWindow()
        }

        val anna = LatLng(38.97, -76.49)
        val anna2 = LatLng(38.99, -76.50)
        val anna3 = LatLng(38.95,-76.51)
        mMap.addMarker(MarkerOptions().position(anna).title("Oyster City")).showInfoWindow()
        mMap.addMarker(MarkerOptions().position(anna2).title("Shell Shack"))
        mMap.addMarker(MarkerOptions().position(anna3).title("Joe's Oysters"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(anna))

        mMap.setMinZoomPreference(12.0f)
        mMap.setMaxZoomPreference(20.0f)
    }

    fun optimizeRoutes() {
        //Read in completed routes
        val routes = FirebaseDatabase.getInstance().getReference("routes")

//        routes.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (keyNode in dataSnapshot.children) {
//                }
//            }
//        }
        var rList = intent.getStringArrayListExtra("COMPLETED_ROUTES")
        //Case 1: All routes completed
        if(rList!!.size == 3){
            //Remove route from firebase
            //snapshot

        }else if(rList!!.size == 1){
            //Case 2: 1 Route is Completed
            //Remove completed route, update route entry in fb
        }else if(rList!!.size == 2) {
            //Case 3: Add the remaining route to nearest cluster

        }
    }

}
