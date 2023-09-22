package com.example.visionchess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
class FriendsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        buttonGoBack.startAnimation(animationFadeIn)
        buttonAddFriend.startAnimation(animationFadeIn)
        addFriendEditText.startAnimation(animationFadeIn)
        val friends = rootView.findViewById<RecyclerView>(R.id.friends)
        val friendRequests = rootView.findViewById<RecyclerView>(R.id.friendRequestsReceived)
        val friendRequestsSent = rootView.findViewById<RecyclerView>(R.id.friendRequestsSent)



        buttonAddFriend.setOnClickListener {
            val friendUsername = addFriendEditText.text.toString()
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
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }

        // Inflate the layout for this fragment
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FriendsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}