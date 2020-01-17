package com.example.environmentchallenge.ui.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.example.environmentchallenge.MainActivity

import com.example.environmentchallenge.R
import com.example.environmentchallenge.database.friend.Friend
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookDialog
import com.facebook.FacebookException
import com.facebook.share.model.GameRequestContent
import com.facebook.share.widget.GameRequestDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class FriendsFragment : Fragment() {
    lateinit var friends:ArrayList<friendInfo>
    lateinit var mAdapter:MultiSelectionAdapter<friendInfo>
    lateinit var listView: ListView
    lateinit var requestDialog:GameRequestDialog
    lateinit var callback: CallbackManager
    lateinit var listOfID:List<String>
    companion object {
        fun newInstance() = FriendsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_friends, container, false)

        listView = root.findViewById(R.id.friendList)
        callback = CallbackManager.Factory.create()
        requestDialog = GameRequestDialog(this)
        listOfID = List
        requestDialog.registerCallback(callback, object:FacebookCallback<GameRequestDialog.Result>{
            override fun onSuccess(result: GameRequestDialog.Result?) {

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }
        })
        startInit()

        val btn = root.findViewById<Button>(R.id.invite_btn)
        btn.setOnClickListener{

        }

        return root
    }

    private fun init(){
        friends = ArrayList<friendInfo>()

        this.context?.let {
            mAdapter = MultiSelectionAdapter<friendInfo>(it,friends)
        }
        listView.adapter = mAdapter

    }

    private fun startInit(){
        CoroutineScope(IO).launch {
            loadFriend()
        }
    }

    private suspend fun loadFriend(){
        friends = ArrayList<friendInfo>()
        val friend:List<Friend> = MainActivity.friendDB.friendDatabaseDAO.getAll()

        for(i in 0 until friend.size){
            val temp2 = friendInfo()
            temp2.setName(friend[i].friendName)
            temp2.setID(friend[i].fbID)

            friends.add(temp2)
        }
        this.context?.let {
            mAdapter = MultiSelectionAdapter<friendInfo>(it, friends)
        }
        withContext(Main) {
            listView.adapter = mAdapter
        }
    }

    private fun onClickInviteBtn(){
        val content = GameRequestContent.Builder()
            .setMessage("Come to challenge and Save Environment")
            .setRecipients()
    }
}
