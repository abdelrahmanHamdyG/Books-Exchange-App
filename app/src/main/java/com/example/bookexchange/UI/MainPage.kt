package com.example.bookexchange.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookexchange.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        var bottom=findViewById<BottomNavigationView>(R.id.main_bottom)
        var frame=findViewById<FrameLayout>(R.id.main_frame_layout)
        var toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_page_toolbar)

        bottom.selectedItemId = R.id.navigation_home
        loadFragment(HomeFragment())
        setSupportActionBar(toolbar)



        var auth=FirebaseAuth.getInstance()
        Log.i("my_trag","main_page_before_if")

        if(auth.currentUser==null){
            Log.i("my_trag","main_page")
            Intent(this, SignIn::class.java).apply {
                startActivity(this)
            }
        }

        bottom.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> loadFragment(HomeFragment())

                R.id.navigation_books -> loadFragment(MyBooksFragment())

                R.id.navigation_requests ->Toast.makeText(this@MainPage,"Temporary",Toast.LENGTH_LONG).show()
            }
            true
        }

    }

    fun loadFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu,menu)
        var searchButton=menu!!.findItem(R.id.search_menu_search)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.search_menu_search ->{


            }

        }
        return super.onOptionsItemSelected(item)
    }
}