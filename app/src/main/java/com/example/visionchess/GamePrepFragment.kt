package com.example.visionchess

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GamePrepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamePrepFragment : Fragment() {

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
        val rootView = inflater.inflate(R.layout.fragment_game_prep, container, false)
        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val whichButtonClicked = arguments
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val timeFormatTextView = rootView.findViewById<TextView>(R.id.timeFormatTextView)
        val timeFormatSpinner = rootView.findViewById<Spinner>(R.id.spinner2)
        val howManyPeeksTextView = rootView.findViewById<TextView>(R.id.howManyPeeksTextView)
        val howManyPeeksSpinner = rootView.findViewById<Spinner>(R.id.spinner3)
        val goBackButton = rootView.findViewById<Button>(R.id.buttonGoBackFromPrep)
        val letsPlayButton = rootView.findViewById<Button>(R.id.buttonLetsPlayFinally)
        timeFormatTextView.startAnimation(animationFadeIn)
        timeFormatSpinner.startAnimation(animationFadeIn)
        goBackButton.startAnimation(animationFadeIn)
        letsPlayButton.startAnimation(animationFadeIn)
        val fragmentManager = activity?.supportFragmentManager
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val handler = Handler(Looper.getMainLooper())
        val currentGameMode = Bundle()
        val nextFragment = GameInvisible()

        nextFragment.arguments = currentGameMode
        letsPlayButton.setOnClickListener{
            val timeFormat = timeFormatSpinner.selectedItem.toString()
            val howManyPeeks = howManyPeeksSpinner.selectedItem.toString()
            //currentGameMode.putString("timeFormat", timeFormat)
            //currentGameMode.putString("howManyPeeks", howManyPeeks)
            if(whichButtonClicked != null) {
                var foundAnOpponent = false
                var opponent = ""
                val buttonClicked = whichButtonClicked.getString("buttonClicked")
                val waitingForGameReference = databaseReference.child("waitingForGame")
                waitingForGameReference.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(snap in snapshot.children){
                            if(snap.child("timeFormat").value.toString() == timeFormat && snap.child("howManyPeeks").value.toString() == howManyPeeks && snap.child("gameMode").value.toString() == buttonClicked){
                                foundAnOpponent = true
                                opponent = snap.key.toString()
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                })



                if(foundAnOpponent){
                    databaseReference.child("waitingForGame").child(opponent).removeValue()
                    databaseReference.child("waitingForGame").child(currentUser!!.uid).removeValue()
                    databaseReference.child("games").child(currentUser.uid).child("opponent").setValue(opponent)
                    databaseReference.child("games").child(opponent).child("opponent").setValue(currentUser.uid)
                    databaseReference.child("games").child(currentUser.uid).child("timeFormat").setValue(timeFormat)
                    databaseReference.child("games").child(opponent).child("timeFormat").setValue(timeFormat)
                    databaseReference.child("games").child(currentUser.uid).child("howManyPeeks").setValue(howManyPeeks)
                    databaseReference.child("games").child(opponent).child("howManyPeeks").setValue(howManyPeeks)
                    databaseReference.child("games").child(currentUser.uid).child("gameMode").setValue(buttonClicked)
                    databaseReference.child("games").child(opponent).child("gameMode").setValue(buttonClicked)
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)
                        ?.commit()
                }else{
                    databaseReference.child("waitingForGame").child(currentUser!!.uid).child("timeFormat").setValue(timeFormat)
                    databaseReference.child("waitingForGame").child(currentUser.uid).child("howManyPeeks").setValue(howManyPeeks)
                    databaseReference.child("waitingForGame").child(currentUser.uid).child("gameMode").setValue(buttonClicked)
                    // Waiting for an opponent
                    Toast.makeText(context, "Waiting for an opponent", Toast.LENGTH_SHORT).show()

                    while(!foundAnOpponent){
                        waitingForGameReference.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(snap in snapshot.children){
                                    if(snap.key.toString() != currentUser.uid){
                                        foundAnOpponent = true
                                        opponent = snap.key.toString()
                                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)
                                            ?.commit()
                                    }
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

        }
        if(whichButtonClicked != null) {
            val buttonClicked = whichButtonClicked.getString("buttonClicked")
            if(buttonClicked == "CasualGame" || buttonClicked == "HotSeat"|| buttonClicked == "BlindfoldVsNonBlindfold"){
                howManyPeeksTextView.visibility = View.VISIBLE
                howManyPeeksSpinner.visibility = View.VISIBLE
                howManyPeeksTextView.startAnimation(animationFadeIn)
                howManyPeeksSpinner.startAnimation(animationFadeIn)
            }else {
                howManyPeeksTextView.visibility = View.INVISIBLE
                howManyPeeksSpinner.visibility = View.INVISIBLE
            }
            handler.postDelayed({
                inflater.inflate(R.layout.fragment_play, container, false)
            }, 250)

            goBackButton.setOnClickListener{
                goBackButton.startAnimation(animationFadeOut)
                if(buttonClicked == "CasualGame" || buttonClicked == "HotSeat"|| buttonClicked == "BlindfoldVsNonBlindfold") {
                    howManyPeeksTextView.startAnimation(animationFadeOut)
                    howManyPeeksSpinner.startAnimation(animationFadeOut)
                }
                timeFormatTextView.startAnimation(animationFadeOut)
                timeFormatSpinner.startAnimation(animationFadeOut)
                letsPlayButton.startAnimation(animationFadeOut)
                handler.postDelayed({
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, PlayFragment())?.addToBackStack(null)
                        ?.commit()
                                    }, 250)

            }
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
         * @return A new instance of fragment GamePrep.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GamePrepFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}