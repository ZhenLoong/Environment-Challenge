package com.example.environmentchallenge

import android.app.Dialog
import android.os.AsyncTask
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.pm.PermissionInfoCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.environmentchallenge.database.challenge.Challenge
import com.example.environmentchallenge.database.challenge.ChallengeDatabase
import com.example.environmentchallenge.database.friend.FriendDatabase
import com.example.environmentchallenge.database.ranking.Ranking
import com.example.environmentchallenge.database.ranking.RankingDatabase
import com.example.environmentchallenge.database.user.User
import com.example.environmentchallenge.database.user.UserDatabase
import com.example.environmentchallenge.ui.profile.ProfileFragment
import com.facebook.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONObject.NULL
import org.w3c.dom.Text
import java.util.concurrent.locks.Lock

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        lateinit var challengeDb: ChallengeDatabase
        lateinit var rankingDb: RankingDatabase
        lateinit var userDB: UserDatabase
        lateinit var friendDB:FriendDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val challengeList = ArrayList<Challenge>()
        challengeList.add(Challenge("C-001", "Compost Heap", "Start a compost heap to reduce the waste you send to landfill sites"))
        challengeList.add(Challenge("C-002", "Save Water", "Take a quick shower, not a leisurely bath, to save water"))
        challengeList.add(Challenge("C-003", "Eco Bike", "Get on your bike instead of driving to reduce carbon monoxide emission"))
        challengeList.add(Challenge("C-004", "Lights Off", "Turn off unneeded lights or when leaving a room to conserve energy"))
        challengeList.add(Challenge("C-005", "No To Plastic Bags", "Refuse plastic bags or at least reuse them. Cloth bags are better."))

        val rankingList = ArrayList<Ranking>()
        rankingList.add(Ranking("R-001", "Harry Hong", 1000))
        rankingList.add(Ranking("R-002", "Kimochi Khim", 829))
        rankingList.add(Ranking("R-003", "Timothy Tai", 500))
        rankingList.add(Ranking("R-004", "Larry Loong", 360))

        userDB = UserDatabase.getInstance(this)
        friendDB = FriendDatabase.getInstance(this)
        challengeDb = ChallengeDatabase.getInstance(this)
        rankingDb = RankingDatabase.getInstance(this)

        if(challengeDb.challengeDatabaseDAO != null) { challengeDb.challengeDatabaseDAO.clear() }
        if(rankingDb.rankingDatabaseDAO != null){ rankingDb.rankingDatabaseDAO.clear() }

        for (i in challengeList) {
            challengeDb.challengeDatabaseDAO.insert(i)
        }
        for(j in rankingList){
            rankingDb.rankingDatabaseDAO.insert(j)
        }

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
                startCoroutine()
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
            drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            startCoroutine()
        }
    }
    private fun startCoroutine(){
        CoroutineScope(IO).launch {
            delay(1000)
            getProfileData()
        }
    }

    private suspend fun getProfileData() {
        val user: List<User> = userDB.userDatabaseDAO.getAll()
        loadProfileData(user)
    }

    private suspend fun loadProfileData(user:List<User>){
        withContext(Main){
            var name:TextView = findViewById(R.id.drawer_name)
            var circleImageView:CircleImageView = findViewById(R.id.drawer_pic)
            for (s in user) {
                name.setText(s.userName)
                Glide.with(this@MainActivity).load(s.userPicUrl).into(circleImageView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            AsyncTask.execute(kotlinx.coroutines.Runnable {
                userDB.userDatabaseDAO.clearAll()
                friendDB.friendDatabaseDAO.clearAll()
            })
        }
        catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}
