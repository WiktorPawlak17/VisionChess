package com.example.visionchess

import android.annotation.SuppressLint
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




class GameInvisible : Fragment() {




    @SuppressLint("SetTextI18n")
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
                val opponentNameReference = opponentReference.child("nickname")
                opponentNameReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        player2name.text = snapshot.value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })
        val currentUsernameReference = databaseReference.child("users").child(currentUser.uid).child("nickname")
        currentUsernameReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                player1name.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val timeReferenceMe = databaseReference.child("gameLive").child(currentUser.uid).child("timeFormat")
        val timeReferenceOpponent = databaseReference.child("gameLive").child(opponent).child("timeFormat")
        var timerWhiteSeconds = 7
        var timerBlackSeconds = 7
        var timerWhiteMinutes = 7
        var timerBlackMinutes = 7
        var timerWhiteSecondsIncrement = 0
        var timerBlackSecondsIncrement = 0
        var color = ""

        val referenceToGetColor = databaseReference.child("gameLive").child(currentUser.uid).child("color")
        referenceToGetColor.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                color = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })





        player1timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
        player2timer.text = "$timerBlackMinutes:$timerBlackSeconds"

        timeReferenceMe.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timeFormat = snapshot.value.toString()
                when(timeFormat.length){
                    5 -> {
                        timerWhiteMinutes = timeFormat[0].toString().toInt()
                        timerBlackMinutes = timerWhiteMinutes
                        databaseReference.child("gameLive").child(currentUser.uid).child("timeLeft").setValue("$timerWhiteMinutes:00")
                    }
                    6 -> {
                        timerWhiteMinutes = timeFormat.substring(0, 2).toInt()
                        timerBlackMinutes = timerWhiteMinutes
                        databaseReference.child("gameLive").child(currentUser.uid).child("timeLeft").setValue("$timerWhiteMinutes:00")

                    }
                    13 -> {
                        timerWhiteMinutes = timeFormat[0].toString().toInt()
                        timerBlackMinutes = timerWhiteMinutes
                        timerWhiteSecondsIncrement = timeFormat[8].toString().toInt()
                        timerBlackSecondsIncrement = timerWhiteSecondsIncrement
                        databaseReference.child("gameLive").child(currentUser.uid).child("timeLeft").setValue("$timerWhiteMinutes:00")

                    }
                    14 -> {
                        timerWhiteMinutes = timeFormat.substring(0, 2).toInt()
                        timerBlackMinutes = timerWhiteMinutes
                        timerWhiteSecondsIncrement = timeFormat[9].toString().toInt()
                        timerBlackSecondsIncrement = timerWhiteSecondsIncrement
                        databaseReference.child("gameLive").child(currentUser.uid).child("timeLeft").setValue("$timerWhiteMinutes:00")

                    }
                    15 -> {
                        timerWhiteMinutes = timeFormat.substring(0, 2).toInt()
                        timerBlackMinutes = timerWhiteMinutes
                        timerWhiteSecondsIncrement = timeFormat.substring(9, 11).toInt()
                        timerBlackSecondsIncrement = timerWhiteSecondsIncrement
                        databaseReference.child("gameLive").child(currentUser.uid).child("timeLeft").setValue("$timerWhiteMinutes:00")

                    }


                }
                timerWhiteSeconds = 0
                timerBlackSeconds = 0
                player1timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                player2timer.text = "$timerBlackMinutes:$timerBlackSeconds"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        val game = ChessGame()

        val timerWhiteRunnable = object : Runnable {
            override fun run() {
                timerWhiteSeconds--
                if (timerWhiteSeconds == -1) {
                    timerWhiteMinutes--
                    timerWhiteSeconds = 59
                }
                if(timerWhiteSeconds == 0 && timerWhiteMinutes == 0){
                    Toast.makeText(context, "You lost on time", Toast.LENGTH_SHORT).show()
                    game.isTimeUp = true
                    game.isGameFinished = true
                }
                if(color == "black"){
                    if(timerWhiteSeconds < 10) {
                        player2timer.text = "$timerWhiteMinutes:0$timerWhiteSeconds"
                    }
                    else {
                        player2timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                    }
                }else {
                    if(timerWhiteSeconds < 10){
                        player1timer.text = "$timerWhiteMinutes:0$timerWhiteSeconds"
                    }else {
                        player1timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                    }
                }


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
                    game.isTimeUp = true
                    game.isGameFinished = true
                }
                if (color == "black") {
                    if (timerWhiteSeconds < 10) {
                        player1timer.text = "$timerWhiteMinutes:0$timerWhiteSeconds"
                    } else {
                        player1timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                    }
                }
                if(timerBlackSeconds < 10){
                    player2timer.text = "$timerBlackMinutes:0$timerBlackSeconds"
                }else {
                    player2timer.text = "$timerBlackMinutes:$timerBlackSeconds"
                }
                handler.postDelayed(this, 1000)
            }
        }


        val player1Reference = databaseReference.child("users").child(currentUser.uid)
        val player2Reference = databaseReference.child("users").child(opponent)

        handler.postDelayed({
           if(color == "black") {
//                player2pic.rotation = 180f
//                player2name.rotation = 180f
//                player2timer.rotation = 180f
//                player1pic.rotation = 180f
//                player1name.rotation = 180f
//                player1timer.rotation = 180f
//                buttonShowBoard.rotation = 180f

            }else {
//
            }
        }, 600)





        try {

            val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/$currentUser")
            val localFile = File.createTempFile("images", "jpg")
            avatarRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                player1pic.setImageBitmap(bitmap)
            }

            //val inputStream = context?.contentResolver?.openInputStream(uri)
            //val bitmap = BitmapFactory.decodeStream(inputStream)
            //avatar.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()


        }
        try {

            val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/$opponent")
            val localFile = File.createTempFile("images", "jpg")
            avatarRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                player2pic.setImageBitmap(bitmap)
            }

            //val inputStream = context?.contentResolver?.openInputStream(uri)
            //val bitmap = BitmapFactory.decodeStream(inputStream)
            //avatar.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
        }

        timerWhiteRunnable.run()
        timerBlackRunnable.run()
        //PRINT ALL
        for(i in 1..8){
            Toast.makeText(context,
                game.getPieceAtPosition("E$i")?.name.toString()+
                        game.getPieceAtPosition("E$i")?.pieceSees().toString(),
                Toast.LENGTH_SHORT).show()
        }



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