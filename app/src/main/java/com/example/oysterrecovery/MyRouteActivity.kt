package com.example.oysterrecovery

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_my_route.*
import kotlinx.android.synthetic.main.content_my_route.*
import kotlinx.android.synthetic.main.content_route.*

class MyRouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_route)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        button2.setOnClickListener{
            val int =  Intent(this, MainActivity::class.java)
            startActivity(int)
        }
    }

}
