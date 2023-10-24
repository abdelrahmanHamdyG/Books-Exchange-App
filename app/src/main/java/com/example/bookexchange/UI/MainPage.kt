package com.example.bookexchange.UI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.MainPageViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var searchBar:EditText
    lateinit var bottom: BottomNavigationView
    lateinit var mainPageViewModel: MainPageViewModel
    lateinit var auth: FirebaseAuth
    lateinit var backButton:Button
    var lastCount=0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        auth=FirebaseAuth.getInstance()


        if(auth.currentUser==null){
            Log.i("my_trag","main_page")
            Intent(this, SignIn::class.java).apply {
                startActivity(this)

            }
            finish()
        }

        mainPageViewModel=ViewModelProvider(this)[MainPageViewModel::class.java]

        searchBar=findViewById<EditText>(R.id.main_page_edit)
        bottom=findViewById<BottomNavigationView>(R.id.main_bottom)
        val frame=findViewById<FrameLayout>(R.id.main_frame_layout)
        backButton=findViewById<Button>(R.id.main_page_back)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_page_toolbar)




        bottom.selectedItemId = R.id.navigation_home
        loadFragment(HomeFragment(this))

        setSupportActionBar(toolbar)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        mainPageViewModel.readSize(auth.currentUser!!.uid)


        mainPageViewModel.count.observe(this){count->
            AppUtils.LOG("Count is $count")
            lastCount=count;
            val item=bottom.menu.findItem(R.id.navigation_requests)
            if(count>0){
                item.title="Requests: $count";

            }else{

                item.title="Requests";

            }



        }


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
                R.id.navigation_home -> loadFragment(HomeFragment(this@MainPage))

                R.id.navigation_books -> loadFragment(MyBooksFragment(this@MainPage))

                R.id.navigation_requests ->loadFragment(RequestsFragment(lastCount))
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
        //var searchButton=menu!!.findItem(R.id.search_menu_search)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.sign_out->{

                val firebaseAuth=FirebaseAuth.getInstance()
                firebaseAuth.signOut()

                startActivity(Intent(this@MainPage,SignIn::class.java))
                finish();
                true;
            }

        }


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