package com.example.visionchess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment(), OnYesListener {
    // TODO: Rename and change types of parameters
    private lateinit var friendsAdapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_friends, container, false)
        val buttonGoBack = rootView.findViewById<Button>(R.id.buttonGoBackFromFriends)
        val fragmentManager = activity?.supportFragmentManager
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val buttonAddFriend = rootView.findViewById<Button>(R.id.buttonAddFriend)
        val addFriendEditText = rootView.findViewById<EditText>(R.id.addFriend)
        val friendsTextView = rootView.findViewById<TextView>(R.id.textViewFriends)
        val friendRequestsTextView = rootView.findViewById<TextView>(R.id.textViewFriendsReceived)
        val friendRequestsSentTextView = rootView.findViewById<TextView>(R.id.textViewFriendsSent)
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        buttonGoBack.startAnimation(animationFadeIn)
        buttonAddFriend.startAnimation(animationFadeIn)
        addFriendEditText.startAnimation(animationFadeIn)
        friendsTextView.startAnimation(animationFadeIn)
        friendRequestsTextView.startAnimation(animationFadeIn)
        friendRequestsSentTextView.startAnimation(animationFadeIn)
        val friends = rootView.findViewById<RecyclerView>(R.id.friends)
        val friendRequestsSent = rootView.findViewById<RecyclerView>(R.id.friendRequestsSent)
        val friendRequestsReceived = rootView.findViewById<RecyclerView>(R.id.friendRequestsReceived)
        val friendsList = mutableListOf<String>()
        val friendRequestsSentList = mutableListOf<String>()
        val friendRequestsReceivedList = ArrayList<String>()
        val friendsReference = databaseReference.child("users/${currentUser?.uid}/friends")
        val friendsReceivedReference = databaseReference.child("users/${currentUser?.uid}/friendRequestsReceived")
        val friendsSentReference = databaseReference.child("users/${currentUser?.uid}/friendRequestsSent")

        val valueEventListenerFriends = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                for (friend in snapshot.children) {
                    val friendNickname = friend.value.toString()
                    friendsList.add(friendNickname)

                }

                friendsAdapter = CustomAdapter(friendsList)
            //    friendsAdapter.notifyDataSetChanged()
                friends.startAnimation(animationFadeIn)
                friends.layoutManager = LinearLayoutManager(context)
                friends.adapter = friendsAdapter
             //   (friends.adapter as CustomAdapter).notifyDataSetChanged()
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
            // Failed to read value
            }
        }
        val valueEventListenerSent = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                for (friend in snapshot.children) {
                    val friendNickname = friend.value.toString()
                    friendRequestsSentList.add(friendNickname)

                }
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Failed to read value
            }
        }
        val valueEventListenerReceived = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                for (friend in snapshot.children) {
                    val friendNickname = friend.value.toString()
                    friendRequestsReceivedList.add(friendNickname)

                }
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Failed to read value
            }
        }

        friendsReference.addValueEventListener(valueEventListenerFriends)
        friendsReference.removeEventListener(valueEventListenerFriends)
        friendsSentReference.addValueEventListener(valueEventListenerSent)
        friendsReceivedReference.addValueEventListener(valueEventListenerReceived)
        handler.postDelayed({
         //   friends.startAnimation(animationFadeIn)
            friendRequestsReceived.startAnimation(animationFadeIn)
            friendRequestsSent.startAnimation(animationFadeIn)
            friendRequestsReceived.startAnimation(animationFadeIn)
            //friends.adapter = CustomAdapter(friendsList)
       //     friends.layoutManager = LinearLayoutManager(context)
            friendRequestsSent.adapter = CustomAdapter(friendRequestsSentList)
            friendRequestsSent.layoutManager = LinearLayoutManager(context)
            friendRequestsReceived.adapter = CustomAdapterReceived(friendRequestsReceivedList,this)
            friendRequestsReceived.layoutManager = LinearLayoutManager(context)
        },1000)



        buttonAddFriend.setOnClickListener {
            val friendUsername = addFriendEditText.text.toString()
            friendRequestsSentList.add(friendUsername)
            friendRequestsSent.adapter = CustomAdapter(friendRequestsSentList)
            friendRequestsSent.layoutManager = LinearLayoutManager(context)
            databaseReference.child("users/${currentUser?.uid}/friendRequestsSent").push().setValue(friendUsername)
            val query = databaseReference.child("users").orderByChild("nickname").equalTo(friendUsername)
            query.addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                        for (user in snapshot.children) {
                            val userId = user.key
                            val currentUserNicknamePath = databaseReference.child("users/${currentUser?.uid}/nickname")
                            currentUserNicknamePath.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val currentUserNickname = snapshot.value.toString()
                                        databaseReference.child("users/$userId/friendRequestsReceived")
                                            .push().setValue(currentUserNickname)
                                    }
                                }

                                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                                    // Failed to read value
                                }
                            })


                        }

                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    // Failed to read value
                }
            })
        }



        buttonGoBack.setOnClickListener {
            buttonGoBack.startAnimation(animationFadeOut)
            buttonAddFriend.startAnimation(animationFadeOut)
            addFriendEditText.startAnimation(animationFadeOut)
            friends.startAnimation(animationFadeOut)
            friendRequestsReceived.startAnimation(animationFadeOut)
            friendRequestsSent.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            friendRequestsTextView.startAnimation(animationFadeOut)
            friendRequestsSentTextView.startAnimation(animationFadeOut)

            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }

        // Inflate the layout for this fragment
        return rootView
    }

    companion object {

    }

    override fun yesClicked() {

    }
}