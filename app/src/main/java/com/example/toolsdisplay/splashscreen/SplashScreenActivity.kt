package com.example.toolsdisplay.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.toolsdisplay.R
import com.example.toolsdisplay.home.views.HomeActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }, 2000)

    }

}