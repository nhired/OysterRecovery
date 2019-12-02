package com.example.oysterrecovery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_my_route.*
import kotlinx.android.synthetic.main.content_my_route.*
import kotlinx.android.synthetic.main.content_route.*

class MyRouteActivity : AppCompatActivity() {
    companion object {
        val selectedRoute = "6,9,11"
        val TAG = "Debugging"
        //     var restaurants = HashMap<String, Restaurant?>()
    }

    lateinit var box1:CheckBox
    lateinit var box2:CheckBox
    lateinit var box3:CheckBox
    lateinit var collectedText:TextView
    lateinit var find1:ImageButton
    lateinit var find2:ImageButton
    lateinit var find3:ImageButton

    var oys1 = 0
    var oys2 = 0
    var oys3 = 0

    var address1 = ""
    var address2 = ""
    var address3 = ""


    var totalOysterCollected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the main body layout
        setContentView(R.layout.activity_my_route)

        // Set the outside toolbar (on top)
        setSupportActionBar(toolbar)

        // Populate hash of resId's to resNames (assuming needed route is provided via extra)
        box1 = findViewById<CheckBox>(R.id.route1)
        box2 = findViewById<CheckBox>(R.id.route2)
        box3 = findViewById<CheckBox>(R.id.route3)

        collectedText = findViewById<TextView>(R.id.CollectedText)

        find1 = findViewById<ImageButton>(R.id.findRes1)
        find2 = findViewById<ImageButton>(R.id.findRes2)
        find3 = findViewById<ImageButton>(R.id.findRes3)


        readRestaurants()

        Log.w(TAG, " " + intent.extras)
        box1.setOnClickListener {
            if(box1.isChecked){
                totalOysterCollected += oys1
                collectedText.text = ((totalOysterCollected).toString() + " Oysters")
            } else {
                totalOysterCollected -= oys1
                collectedText.text = ((totalOysterCollected).toString() + " Oysters")
            }
        }

        box2.setOnClickListener {
            if(box2.isChecked){
                totalOysterCollected += oys2
                collectedText.text = ((totalOysterCollected).toString() + " Oysters")
            } else {
                totalOysterCollected -= oys2
                collectedText.text = ((totalOysterCollected).toString() + " Oysters")
            }
        }

        box3.setOnClickListener {
            if(box3.isChecked){
                totalOysterCollected += oys3
                collectedText.text = ((totalOysterCollected).toString() + " Oysters")
            } else {
                totalOysterCollected -= oys3
                collectedText.text = ((totalOysterCollected).toString() + " Oysters")
            }
        }

        find1.setOnClickListener{
            searchRestaurant(box1.text.toString())
        }
        find2.setOnClickListener{
            searchRestaurant(box2.text.toString())
        }
        find3.setOnClickListener{
            searchRestaurant(box3.text.toString())
        }

        // Pass selected information via extras to finishActivity intent
        fab.setOnClickListener {
            val finishIntent =  Intent(this, RouteActivity::class.java)
//            finishIntent.putExtra("totalCollected", "totalOysterCollected.toString()")
            startActivity(finishIntent)
        }

//        button2.setOnClickListener{
//            val int =  Intent(this, MainActivity::class.java)
//            startActivity(int)
//        }
    }

    private fun readRestaurants(){
        val mRestaurants = FirebaseDatabase.getInstance().getReference("restaurants")
        mRestaurants.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val resList = HashMap<String, Restaurant?>()
                for (keyNode in dataSnapshot.children) {
                    val restaurant = keyNode.getValue<Restaurant>(Restaurant::class.java)
                    resList.put(keyNode.key!!, restaurant)
                }

                /* Pseudocode:

                 if(three == true){
                      for(route in threeRoutes){
                          String resID = getIntent().getStringExtra("res1");
                          findTextViewById.setText(restaurants.get(resID).name)
                      }
                 } else {
                      // Do the same for four restaurants in route
                 }

                */
                Log.d("Testing sending intent1: ",intent.getStringExtra("resOne").toString())
                Log.d("Testing sending intent2: ",intent.getStringExtra("resTwo").toString())
                Log.d("Testing sending intent3: ",intent.getStringExtra("resThree").toString())

                Log.d("Testing sending intent1: ",resList[intent.getStringExtra("resOne").toString()]!!.name)
                Log.d("Testing sending intent2: ",resList[intent.getStringExtra("resTwo").toString()]!!.name)
                Log.d("Testing sending intent3: ",resList[intent.getStringExtra("resThree").toString()]!!.name)


                box1.text = resList[intent.getStringExtra("resOne").toString()]!!.name
                //               intent.putExtra("resOne", resList["6"]!!.name)
                box2.text = resList[intent.getStringExtra("resTwo").toString()]!!.name
//                intent.putExtra("resTwo", resList["9"]!!.name)
                box3.text = resList[intent.getStringExtra("resThree").toString()]!!.name
//                intent.putExtra("resOne", resList["11"]!!.name)

                oys1 += resList[intent.getStringExtra("resOne").toString()]!!.oysterNumber.toInt()
                oys2 += resList[intent.getStringExtra("resTwo").toString()]!!.oysterNumber.toInt()
                oys3 += resList[intent.getStringExtra("resThree").toString()]!!.oysterNumber.toInt()

//                totalOysterCollected += resList["6"]!!.oysterNumber.toInt()
//                totalOysterCollected +=  resList["9"]!!.oysterNumber.toInt()
//                totalOysterCollected += resList["11"]!!.oysterNumber.toInt()

                collectedText.text = (totalOysterCollected.toString() + " Oysters")
//                intent.putExtra("TotalCollected", totalOysterCollected.toString())



                mRestaurants.removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
                // Required
            }
        })
    }

    private fun searchRestaurant(searchFor:String){
        val find = validURL(searchFor)
//        Log.w(TAG, find)
        var searchInt = Intent(Intent.ACTION_VIEW)
        searchInt.data = Uri.parse(find)
        startActivity(searchInt)
    }

    private fun validURL(original:String): String{
        var urlBase = "https://www.google.com/maps/search/?api=1&query="

        for(letter in original){
            if(letter.isLetter()){
                urlBase += letter.toLowerCase().toString()
            } else if(letter == ' '){
                urlBase += "+"
            }
        }
        return urlBase
    }

}