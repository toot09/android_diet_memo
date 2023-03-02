package com.ujone.diet_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        try {
            Toast.makeText(this, "이미 로그인 되어 있습니다", Toast.LENGTH_SHORT).show()
            Log.d("SplashActivity",auth.currentUser!!.uid)
        } catch (e : Exception) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "비회원 로그인 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "비회원 로그인 성공", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)

    }
}