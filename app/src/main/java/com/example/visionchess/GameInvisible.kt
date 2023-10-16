package com.example.visionchess

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.Timer



class GameInvisible : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_game_invisible, container, false)
        val player2pic = rootView.findViewById<ImageView>(R.id.player2Picture)
        val player1pic = rootView.findViewById<ImageView>(R.id.player1Picture)
        val buttonShowBoard = rootView.findViewById<Button>(R.id.showBoardButton)
        val player2name = rootView.findViewById<TextView>(R.id.player2Name)
        val player1name = rootView.findViewById<TextView>(R.id.player1Name)
        val player2timer = rootView.findViewById<TextView>(R.id.player2Timer)
        val player1timer = rootView.findViewById<TextView>(R.id.player1Timer)
        val fragmentManager = activity?.supportFragmentManager

        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val gameLiveReference = databaseReference.child("gameLive")
        val currentOpponentReference = gameLiveReference.child(currentUser!!.uid).child("opponent")
        var opponent = ""
        currentOpponentReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                opponent = snapshot.value.toString()
                val opponentReference = databaseReference.child("users").child(opponent)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        //TIME FORMAT CRASHES THE CODe
        var timerWhite = Timer()
        var timerBlack = Timer()
        val timeReferenceMe = databaseReference.child("gameLive").child(currentUser.uid).child("timeFormat")
        val timeReferenceOpponent = databaseReference.child("gameLive").child(opponent).child("timeFormat")
        var timerWhiteSeconds = 5
        var timerBlackSeconds = 5
        var timerWhiteMinutes = 5
        var timerBlackMinutes = 5
        var timerWhiteSecondsIncrement = 0
        var timerBlackSecondsIncrement = 0
//        timeReferenceMe.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val timeFormat = snapshot.value.toString()
//                when(timeFormat.length){
//                    5 -> {
//                        timerWhiteMinutes = timeFormat.substring(0, 1).toInt()
//                        timerBlackMinutes = timerWhiteMinutes
//                    }
//                    6 -> {
//                        timerWhiteMinutes = timeFormat.substring(0, 2).toInt()
//                        timerBlackMinutes = timerWhiteMinutes
//                    }
//                    13 -> {
//                        timerWhiteMinutes = timeFormat.substring(0, 1).toInt()
//                        timerBlackMinutes = timerWhiteMinutes
//                        timerWhiteSecondsIncrement = timeFormat.substring(9, 10).toInt()
//                        timerBlackSecondsIncrement = timerWhiteSecondsIncrement
//                    }
//                    14 -> {
//                        timerWhiteMinutes = timeFormat.substring(0, 2).toInt()
//                        timerBlackMinutes = timerWhiteMinutes
//                        timerWhiteSecondsIncrement = timeFormat.substring(10, 11).toInt()
//                        timerBlackSecondsIncrement = timerWhiteSecondsIncrement
//                    }
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//            }
//        })



        val timerWhiteRunnable = object : Runnable {
            override fun run() {
                timerWhiteSeconds--
                if (timerWhiteSeconds == -1) {
                    timerWhiteMinutes--
                    timerWhiteSeconds = 59
                }
                if(timerWhiteSeconds == 0 && timerWhiteMinutes == 0){
                    Toast.makeText(context, "You lost on time", Toast.LENGTH_SHORT).show()
                }
                if(timerWhiteSeconds < 10){
                    player1timer.text = "$timerWhiteMinutes:0$timerWhiteSeconds"
                }
                player1timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                handler.postDelayed(this, 1000)
            }
        }
        val timerBlackRunnable = object : Runnable {
            override fun run() {
                timerBlackSeconds--
                if (timerBlackSeconds == -1) {
                    timerBlackMinutes--
                    timerBlackSeconds = 59
                }
                if(timerBlackSeconds == 0 && timerBlackMinutes == 0){
                    Toast.makeText(context, "You lost on time", Toast.LENGTH_SHORT).show()
                }
                if(timerBlackSeconds < 10){
                    player2timer.text = "$timerBlackMinutes:0$timerBlackSeconds"
                }
                player2timer.text = "$timerBlackMinutes:$timerBlackSeconds"
                handler.postDelayed(this, 1000)
            }
        }


        val player1Reference = databaseReference.child("users").child(currentUser.uid)
        val player2Reference = databaseReference.child("users").child(opponent)
//        try {
//
//            val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
//            val storageRef = storage.reference
//            val avatarRef = storageRef.child("images/$currentUser")
//            val localFile = File.createTempFile("images", "jpg")
//            avatarRef.getFile(localFile).addOnSuccessListener {
//                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                player1pic.setImageBitmap(bitmap)
//            }
//
//            //val inputStream = context?.contentResolver?.openInputStream(uri)
//            //val bitmap = BitmapFactory.decodeStream(inputStream)
//            //avatar.setImageBitmap(bitmap)
//        } catch (e: Exception) {
//            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
//
//
//        }
//        try {
//
//            val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
//            val storageRef = storage.reference
//            val avatarRef = storageRef.child("images/$opponent")
//            val localFile = File.createTempFile("images", "jpg")
//            avatarRef.getFile(localFile).addOnSuccessListener {
//                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                player2pic.setImageBitmap(bitmap)
//            }
//
//            //val inputStream = context?.contentResolver?.openInputStream(uri)
//            //val bitmap = BitmapFactory.decodeStream(inputStream)
//            //avatar.setImageBitmap(bitmap)
//        } catch (e: Exception) {
//            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
//        }
//        player1Reference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val player1Name = snapshot.child("nickname").value.toString()
//                val player1Pic = snapshot.child("profilePic").value.toString()
//                player1name.text = player1Name
//                player1pic.setImageResource(player1Pic.toInt())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//            }
//        })
//        player2Reference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val player2Name = snapshot.child("nickname").value.toString()
//                val player2Pic = snapshot.child("profilePic").value.toString()
//                player2name.text = player2Name
//                player2pic.setImageResource(player2Pic.toInt())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//            }
//        })

        timerWhiteRunnable.run()


        buttonShowBoard.setOnClickListener {
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, GameVisible())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        // Inflate the layout for this fragment
        return rootView
    }


}