package com.example.visionchess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
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
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val buttonGoBack = rootView.findViewById<Button>(R.id.buttonGoBackFromProfile)
        val gameName = rootView.findViewById<TextView>(R.id.gameName)
        val blitzRating = rootView.findViewById<TextView>(R.id.blitzRating)
        val rapidRating = rootView.findViewById<TextView>(R.id.rapidRating)
        val logOut = rootView.findViewById<Button>(R.id.logOut)
        val fragmentManager = activity?.supportFragmentManager
        val animationFadeOut = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val animationFadeIn = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val auth = FirebaseAuth.getInstance()


        /////////////////////////////////////////////
        //Random stuff added itself???
        /////////////////////////////////////////////
//        val user = auth.currentUser
//        val email = user?.email
//        val uid = user?.uid
//        val db = com.google.firebase.database.FirebaseDatabase.getInstance().reference
//        val userRef = db.child("users").child(uid.toString())
//        val gameNameRef = userRef.child("gameName")
//        val blitzRatingRef = userRef.child("blitzRating")
//        val rapidRatingRef = userRef.child("rapidRating")
//        gameNameRef.get().addOnSuccessListener {
//            gameName.text = it.value.toString()
//        }
        buttonGoBack.startAnimation(animationFadeIn)
        gameName.startAnimation(animationFadeIn)
        blitzRating.startAnimation(animationFadeIn)
        rapidRating.startAnimation(animationFadeIn)
        logOut.startAnimation(animationFadeIn)

        buttonGoBack.setOnClickListener{
            buttonGoBack.startAnimation(animationFadeOut)
            gameName.startAnimation(animationFadeOut)
            blitzRating.startAnimation(animationFadeOut)
            rapidRating.startAnimation(animationFadeOut)
            logOut.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }


        logOut.setOnClickListener {
            auth.signOut()
            buttonGoBack.startAnimation(animationFadeOut)
            gameName.startAnimation(animationFadeOut)
            blitzRating.startAnimation(animationFadeOut)
            rapidRating.startAnimation(animationFadeOut)
            logOut.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.addToBackStack(null)
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}