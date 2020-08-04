package com.raj.databindinglibrary

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*

import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.navigation_activity.*
import kotlinx.android.synthetic.main.navigation_activity.my_nav_host_fragment
import kotlinx.android.synthetic.main.navigation_activity.toolbar
import kotlinx.android.synthetic.main.navigation_activity_navigationview.*

class DataBindingDemoActivity : AppCompatActivity() {

//    val dataViewModel by lazy {
//        ViewModelProvider(this).get(DataViewModel::class.java)
//    }
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remember this for BottomBar Navigation
//        setContentView(R.layout.navigation_activity)

        // This is for NavigationView
        setContentView(R.layout.navigation_activity_navigationview)

        val navHost = my_nav_host_fragment as NavHostFragment
        val navController = navHost.navController



        //Remember this for BottomBar Navigation
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        bottom_nav_view.setupWithNavController(navController)


        //NavigationUI with DrawableLayout
        val drawerlayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.firstFragment,R.id.secondFragment),drawerlayout)
        setSupportActionBar(toolbar)
        setupNavigationMenu(navController)
        setupActionBar(navController,appBarConfiguration)



//        supportFragmentManager.beginTransaction().replace(R.id.container,DataBindingFragment()).commit()
//        databinding.lifecycleOwner = this
//        databinding.viewModelData = dataViewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val returnval =  super.onCreateOptionsMenu(menu)
         menuInflater.inflate(R.menu.overflow_menu,menu)
        return returnval
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment)) || super.onOptionsItemSelected(item)

    }


}