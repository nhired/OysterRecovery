package com.example.oysterrecovery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        sign_up_pg.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {

        if(emailTxt.text.toString().isEmpty()) {
            Toast.makeText(baseContext, "Please input email address",
                Toast.LENGTH_SHORT).show()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTxt.text.toString()).matches()) {
            Toast.makeText(baseContext, "Please enter valid email address",
                Toast.LENGTH_SHORT).show()
            return

        }

        if(firstPswd.text.toString().isEmpty()) {
            Toast.makeText(baseContext, "Please enter password",
                Toast.LENGTH_SHORT).show()
            return
        }

        if(firstPswd.text.toString() != secondPswd.text.toString()) {
            Toast.makeText(baseContext, "Passwords do not match, please re-enter password",
                Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(emailTxt.text.toString(), secondPswd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    startActivity(Intent(this, RouteActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Sign Up failed. Try again",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }

    }


    companion object {
        val TAG = "FirebaseDB User Authentication"
    }
}
