package com.example.myimagefilters.activities.developers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myimagefilters.R

class DevActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev)

        val message = """
            Hello there I am Aditya Kumar. Thank you for downloading my application.
            Feel free to visit my website DevConnect and check out my other projects.
            here is my link:- https://radiant-bastion-62859.herokuapp.com/profile/3947f970-07f0-4863-bdf6-0d247c0b2e82/
            """.trimIndent()
        var textView_info = findViewById<TextView>(R.id.textView_info)
        textView_info.setText(message)

    }

    //this function will animate the dev activity when doing sliding out animation
    override fun onBackPressed() {
        super.onBackPressed()
        this@DevActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}