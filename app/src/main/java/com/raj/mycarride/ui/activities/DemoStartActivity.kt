package com.raj.mycarride.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raj.mycarride.R
import kotlinx.android.synthetic.main.activity_demo_start.*

class DemoStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_start)
        setlisteners()
    }

    private fun setlisteners() {
        location.setOnClickListener {
            var intent : Intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)

        }
    }
}