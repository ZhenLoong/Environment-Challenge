package com.example.environmentchallenge.ui.profile

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.environmentchallenge.MainActivity

import com.example.environmentchallenge.R
import com.example.environmentchallenge.database.user.User
import com.example.environmentchallenge.ui.login.LoginViewModel
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var loginViewModel: LoginViewModel
    lateinit var name:TextView
    lateinit var age:TextView
    lateinit var circleImageView: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        name = root.findViewById(R.id.name_edit)
        age = root.findViewById(R.id.age_edit)
        circleImageView = root.findViewById(R.id.profile_pic)

        CoroutineScope(IO).launch {
            getProfileData()
        }


        return root
    }
    var tokenTracker: AccessTokenTracker = object: AccessTokenTracker(){
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            if(currentAccessToken == null)
            {
                view?.findNavController()?.navigate(R.id.action_nav_profile_to_nav_login)
            }
        }
    }

    private suspend fun getProfileData(){
        val user: List<User> = MainActivity.userDB.userDatabaseDAO.getAll()
        loadProfileData(user)
    }

    private suspend fun loadProfileData(user:List<User>){
        withContext(Main){
            for (u in user){
                name.setText(u.userName)
                age.setText(u.userAge)
                Glide.with(this@ProfileFragment).load(u.userPicUrl).into(circleImageView)
            }
        }
    }
}
