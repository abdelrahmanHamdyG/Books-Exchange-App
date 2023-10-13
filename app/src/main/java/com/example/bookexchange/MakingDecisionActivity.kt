package com.example.bookexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MakingDecisionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_decision)

        val myKey=intent.extras!!.getString("myKey")
        val hisKey=intent.extras!!.getString("hisKey")
        val fromMe=intent.extras!!.getBoolean("fromMe")
        val requestName=myKey+hisKey





    }
}