package com.example.visionchess.Fragments

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
import com.example.visionchess.GameInvisible
import com.example.visionchess.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class GamePrepFragment : Fragment() {


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
            if(whichButtonClicked != null) {
                var foundAnOpponent = false
                var opponent: String
                val buttonClicked = whichButtonClicked.getString("buttonClicked")
                val currentGameString = buttonClicked + timeFormat + howManyPeeks
                val newGameReference = databaseReference.child(currentGameString)
                val randomNumberGenerated = mutableListOf(0,1)
                val randomNumber = randomNumberGenerated.random()
                newGameReference.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(snap in snapshot.children){
                                if(snap.key.toString() != currentUser!!.uid){
                                    foundAnOpponent = true
                                    opponent = snap.key.toString()
                                    databaseReference.child(currentGameString).child(opponent).removeValue()
                                    databaseReference.child(currentGameString).child(currentUser.uid).removeValue()
                                    databaseReference.child("gameLive").child(currentUser.uid).child("opponent").setValue(opponent)
                                    databaseReference.child("gameLive").child(opponent).child("opponent").setValue(currentUser.uid)
                                    databaseReference.child("gameLive").child(currentUser.uid).child("timeFormat").setValue(timeFormat)
                                    databaseReference.child("gameLive").child(opponent).child("timeFormat").setValue(timeFormat)
                                    databaseReference.child("gameLive").child(currentUser.uid).child("howManyPeeks").setValue(howManyPeeks)
                                    databaseReference.child("gameLive").child(opponent).child("howManyPeeks").setValue(howManyPeeks)
                                    databaseReference.child("gameLive").child(currentUser.uid).child("gameMode").setValue(buttonClicked)
                                    databaseReference.child("gameLive").child(opponent).child("gameMode").setValue(buttonClicked)
                                    if(randomNumber == 0) {
                                        databaseReference.child("gameLive").child(currentUser.uid)
                                            .child("color").setValue("white")
                                        databaseReference.child("gameLive").child(opponent)
                                            .child("color").setValue("black")
                                    }else{
                                        databaseReference.child("gameLive").child(currentUser.uid)
                                            .child("color").setValue("black")
                                        databaseReference.child("gameLive").child(opponent)
                                            .child("color").setValue("white")
                                    }
                                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)
                                        ?.commit()

                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }

                    
                })





                    if(!foundAnOpponent){
                        databaseReference.child(currentGameString).child(currentUser!!.uid).child("timeFormat").setValue(timeFormat)
                        databaseReference.child(currentGameString).child(currentUser.uid).child("howManyPeeks").setValue(howManyPeeks)
                        databaseReference.child(currentGameString).child(currentUser.uid).child("gameMode").setValue(buttonClicked)
                        // Waiting for an opponent
                        Toast.makeText(context, "Waiting for an opponent", Toast.LENGTH_SHORT).show()
                        val liveGameReference = databaseReference.child("gameLive").child(currentUser.uid)
                        val matchCheckingRunnable = object: Runnable{
                            override fun run() {
                                handler.postDelayed(this, 1000)

                                liveGameReference.addListenerForSingleValueEvent(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children) {
                                            if (snap.key.toString() != currentUser.uid) {
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

                                if(foundAnOpponent){
                                    handler.removeCallbacks(this)
                                    handler.postDelayed({
                                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)
                                            ?.commit()
                                    }, 2000)
                                }
                            }
                        }
                        matchCheckingRunnable.run()
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






}