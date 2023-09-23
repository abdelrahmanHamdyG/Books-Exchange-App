package com.example.bookexchange.UI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var searchBar:EditText
    lateinit var bottom: BottomNavigationView
    lateinit var backButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val auth=FirebaseAuth.getInstance()


        if(auth.currentUser==null){
            Log.i("my_trag","main_page")
            Intent(this, SignIn::class.java).apply {
                startActivity(this)

            }
            finish()
        }

        searchBar=findViewById<EditText>(R.id.main_page_edit)
        bottom=findViewById<BottomNavigationView>(R.id.main_bottom)
        val frame=findViewById<FrameLayout>(R.id.main_frame_layout)
        backButton=findViewById<Button>(R.id.main_page_back)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_page_toolbar)

        bottom.selectedItemId = R.id.navigation_home
        loadFragment(HomeFragment())

        setSupportActionBar(toolbar)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager



        backButton.setOnClickListener {

            searchBar.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(searchBar    .windowToken, 0)

        }
        searchBar.setOnFocusChangeListener{view,HasFocus->

            if(HasFocus) {
                bottom.visibility = View.GONE
                backButton.visibility=View.VISIBLE

            }
            else{
                bottom.visibility=View.VISIBLE
                backButton.visibility=View.GONE
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

        //menuInflater.inflate(R.menu.search_menu,menu)
        var searchButton=menu!!.findItem(R.id.search_menu_search)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        /*when(item.itemId){
            R.id.search_menu_search ->{


            }

        }
        */

        return super.onOptionsItemSelected(item)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.LOG("onBackPressed")
        if(searchBar.isFocused) {
            bottom.visibility = View.GONE
            backButton.visibility = View.VISIBLE
        }

    }


}