package com.example.oysterrecovery

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.okhttp.Route

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    lateinit var rellay1 : RelativeLayout
    lateinit var rellay2 : RelativeLayout

    val handler = Handler()

    val runnable = Runnable {

        rellay1.visibility = View.VISIBLE
        rellay2.visibility = View.VISIBLE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()

        // Set a click listener for first button widget

        rellay1 = findViewById(R.id.rellay1)
        rellay2 = findViewById(R.id.rellay2)

        handler.postDelayed(runnable, 2000)


        signUpBtn.setOnClickListener {
            val int =  Intent(this, SignUpActivity::class.java)
            startActivity(int)
            finish()
        }

        loginBtn.setOnClickListener {
            val int =  Intent(this, RouteActivity::class.java)
            startActivity(int)
            finish()
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
