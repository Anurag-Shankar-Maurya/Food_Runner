package com.internshala.higherorderfunctionalitiessolution.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.internshala.higherorderfunctionalitiessolution.R
import com.internshala.higherorderfunctionalitiessolution.fragment.FAQFragment
import com.internshala.higherorderfunctionalitiessolution.fragment.FavouritesFragment
import com.internshala.higherorderfunctionalitiessolution.fragment.HomeFragment
import com.internshala.higherorderfunctionalitiessolution.fragment.ProfileFragment


/*This is the base activity which will hold all the fragments we create in this assignment
* The navigation drawer will be placed inside this activity*/


class HomeActivity : AppCompatActivity() {


    /*Defining the variables used in the activity*/

    /*This variable will be used for the toolbar which will hold the different titles*/
    private lateinit var toolbar: Toolbar

    /*This is the native layout for this activity as it holds the navigation drawer*/
    private lateinit var drawerLayout: DrawerLayout

    /*This is the view used to make the navigation drawer*/
    private lateinit var navigationView: NavigationView

    /*This variable is used as a flag to keep a check as to which menu item inside the navigation drawer is checked*/
    private var previousMenuItem: MenuItem? = null

    /*The action bar drawer toggle is used to handle the open and close events of the navigation drawer*/
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    /*Life-cycle method of the activity*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Linking a view to the activity*/
        setContentView(R.layout.activity_home)

        /*Created by us to handle the initialisation of variables*/
        init()

        /*This method is also user created to setup the toolbar*/
        setupToolbar()

        /*User created method to handle the action bar drawer toogle*/
        setupActionBarToggle()

        /*This is method is created to display the home fragment inside the activity by default*/
        displayHome()

        /*Below we handle the click listeners of the menu items inside the navigation drawer*/
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->

            /*Unchecking the previous menu item when a new item is clicked*/
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            /*Highlighting the new menu item, the one which is clicked*/
            item.isCheckable = true
            item.isChecked = true

            /*This sets the value of previous menu item as the current one*/
            previousMenuItem = item


            /*The closing of navigation drawer is delayed to make the transition smooth
            * We delay it by 0.1 second*/
            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)

            /*The fragment transaction takes care of the different fragments which will be opened and closed*/
            val fragmentTransaction = supportFragmentManager.beginTransaction()

            /*Getting the id of the clicked item to identify which fragment to display*/
            when (item.itemId) {

                /*Opening the home fragment*/
                R.id.home -> {
                    val homeFragment = HomeFragment()
                    fragmentTransaction.replace(R.id.frame, homeFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "All Restaurants"
                }

                /*Opening the profile fragment*/
                R.id.myProfile -> {
                    val profileFragment = ProfileFragment()
                    fragmentTransaction.replace(R.id.frame, profileFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "My profile"
                }

                /*Opening the favorites fragment*/
                R.id.favRes -> {
                    val favFragment = FavouritesFragment()
                    fragmentTransaction.replace(R.id.frame, favFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Favorite Restaurants"
                }

                /*Opening the frequently asked questions i.e. FAQ fragment*/
                R.id.faqs -> {
                    val faqFragment = FAQFragment()
                    fragmentTransaction.replace(R.id.frame, faqFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                }

                /*Exiting the application*/
                R.id.logout -> {

                    /*Creating a confirmation dialog*/
                    val builder = AlertDialog.Builder(this@HomeActivity)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            ActivityCompat.finishAffinity(this)
                        }
                        .setNegativeButton("No") { _, _ ->
                            displayHome()
                        }
                        .create()
                        .show()

                }

            }
            return@setNavigationItemSelectedListener true
        }
    }


    /*This is method is created to display the home fragment*/
    private fun displayHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }


    private fun setupActionBarToggle() {
        actionBarDrawerToggle =
            object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.openDrawer,
                R.string.closeDrawer
            ) {
                override fun onDrawerStateChanged(newState: Int) {
                    super.onDrawerStateChanged(newState)
                    val pendingRunnable = Runnable {
                        val inputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    }
                    Handler().postDelayed(pendingRunnable, 50)
                }
            }

        /*Adding the drawer toggle to the drawer layout*/
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        /*This handles the animation of the hamburger icon when the drawer is opened/closed*/
        actionBarDrawerToggle.syncState()

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        /*This is done to open the navigation drawer when the hamburger icon is clicked*/
        if (item?.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
