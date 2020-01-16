package com.example.environmentchallenge

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.pm.PermissionInfoCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.environmentchallenge.ui.profile.ProfileFragment
import com.facebook.*
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.concurrent.locks.Lock

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_daily_challenge, R.id.nav_weekly_challenge,
                R.id.nav_ranking, R.id.nav_profile, R.id.nav_about_us
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        checkLoginStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    var tokenTracker: AccessTokenTracker = object: AccessTokenTracker(){
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            var toolbar:Toolbar = findViewById(R.id.toolbar)
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            if(currentAccessToken == null)
            {
                drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                toolbar.isEnabled = false
                toolbar.isVisible = false
            }else{
                drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                toolbar.isEnabled = true
                toolbar.isVisible = true
                loadUserProfile()
            }
        }
    }
    private fun checkLoginStatus() {
        var toolbar:Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (AccessToken.getCurrentAccessToken() == null)
        {
            drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            toolbar.isEnabled = false
            toolbar.isVisible = false
        }else{
            loadUserProfile()
        }
    }
    private fun loadUserProfile(){

    }
}
