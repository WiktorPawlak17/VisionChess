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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File




class GameInvisible : Fragment() {
    val game = ChessGame()



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
        val receivedMess = rootView.findViewById<TextView>(R.id.receivedMessage)
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
        var isWhiteTimerRunning = false
        var isBlackTimerRunning = false
        val player1Reference = databaseReference.child("users").child(currentUser.uid)
        val player2Reference = databaseReference.child("users").child(opponent)

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
                    //handler.removeCallbacks(this)
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
//            fun stop(){
//                handler.removeCallbacks(this)
//            }

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
                    //handler.removeCallbacks(this)
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
//            fun stop(){
//                handler.removeCallbacks(this)
//            }

        }










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
        //IMPLEMENT SPEECH RECON HERE
        val speechHandler = SpeechRecognitionHandler(requireContext())
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch { while(!game.isGameFinished) {

            if (game.isWhiteTurn) {
                if(!isWhiteTimerRunning){
                    timerWhiteRunnable.run()
                    isWhiteTimerRunning = true
                }


                    speechHandler.startRecognition()
                    val message = speechHandler.recognizedMessage
                    receivedMess.text = message
                    //game.isWhiteTurn = false
                    val currentBoardState = game.getChessBoard()



            } else {
                if(!isBlackTimerRunning){
                    timerBlackRunnable.run()
                    isBlackTimerRunning = true
                }

                    speechHandler.startRecognition()
//                    while(!speechHandler.youDone){
//                        delay(100)
//                    }
//                    speechHandler.stopRecognition()
//                    speechHandler.youDone = true
                    val message = speechHandler.recognizedMessage
                    receivedMess.text = message

                    //game.isWhiteTurn = true

            }

            delay(1000)
        }

                handler.removeCallbacks(timerWhiteRunnable)
                handler.removeCallbacks(timerBlackRunnable)

            }





        //PRINT SOME

//        val pos = "D8"
//        Toast.makeText(context,
//               "$pos"+     game.getPieceAtPosition(pos)?.name.toString()+
//                    pieceReallySees(pos),
//                    Toast.LENGTH_LONG).show()




        buttonShowBoard.setOnClickListener {
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, GameVisible())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        // Inflate the layout for this fragment
        return rootView
    }
private fun pieceReallySees(position : String): MutableList<String>? {
    val list = game.getPieceAtPosition(position)?.pieceSees()
    if(list !=null){
        val col = game.letterToNumberMapPlayerVersion[position[0].toString()]
        val row = position[1].toString().toInt()
        var foundBlockade = false
        val distanceToRightBound = 8 - col!!
        val distanceToUpBound = 8 - row
        val distanceToLeftBound = col - 1
        val distanceToDownBound = row - 1
        val distanceToRightUp = if(distanceToRightBound < distanceToUpBound) distanceToRightBound else distanceToUpBound
        val distanceToLeftUp = if(distanceToLeftBound < distanceToUpBound) distanceToLeftBound else distanceToUpBound
        val distanceToRightDown = if(distanceToRightBound < distanceToDownBound) distanceToRightBound else distanceToDownBound
        val distanceToLeftDown = if(distanceToLeftBound < distanceToDownBound) distanceToLeftBound else distanceToDownBound

        when(game.getPieceAtPosition(position)?.name){
            "P" -> { //GONNA DO NOTHING
            }
            "R" -> {
                //CHECKING FOR BLOCKADES IN ROWS

                for (i in col + 1..8){
                    if(foundBlockade){
                        list.remove("${game.numberToLetterMapPlayerVersion[i]}$row")
                    }
                    if(game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[i]}$row")!=null){
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for (i in col - 1 downTo 1){
                    if(foundBlockade){
                        list.remove("${game.numberToLetterMapPlayerVersion[i]}$row")
                    }
                    if(game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[i]}$row")!=null){
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                //CHECKING FOR BLOCKADES IN COLUMNS
                for(i in row+1..8){
                    if(foundBlockade){
                        list.remove("${position[0]}$i")
                    }
                    if(game.getPieceAtPosition("${position[0]}$i")!=null){
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for (i in row-1 downTo 1){
                    if(foundBlockade){
                        list.remove("${position[0]}$i")
                    }
                    if(game.getPieceAtPosition("${position[0]}$i")!=null){
                        foundBlockade = true
                    }

                }
            }
            "N" -> { //GONNA DO NOTHING
            }
            "B" -> {
                //CHECK FOR RIGHT UP

                for(i in 1..distanceToRightUp) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col + i]}${row + i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col + i]}${row + i}") != null) {
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for(i in 1..distanceToLeftUp) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col - i]}${row + i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col - i]}${row + i}") != null) {
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for (i in 1..distanceToRightDown) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col + i]}${row - i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col + i]}${row - i}") != null) {
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for (i in 1..distanceToLeftDown) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col - i]}${row - i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col - i]}${row - i}") != null) {
                        foundBlockade = true
                    }

                }


            }
            "Q" -> {
//CHECKING FOR BLOCKADES IN ROWS

                for (i in col + 1..8){
                    if(foundBlockade){
                        list.remove("${game.numberToLetterMapPlayerVersion[i]}$row")
                    }
                    if(game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[i]}$row")!=null){
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for (i in col - 1 downTo 1){
                    if(foundBlockade){
                        list.remove("${game.numberToLetterMapPlayerVersion[i]}$row")
                    }
                    if(game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[i]}$row")!=null){
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                //CHECKING FOR BLOCKADES IN COLUMNS
                for(i in row+1..8){
                    if(foundBlockade){
                        list.remove("${position[0]}$i")
                    }
                    if(game.getPieceAtPosition("${position[0]}$i")!=null){
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for (i in row-1 downTo 1){
                    if(foundBlockade){
                        list.remove("${position[0]}$i")
                    }
                    if(game.getPieceAtPosition("${position[0]}$i")!=null){
                        foundBlockade = true
                    }

                }

                //CHECK FOR RIGHT UP

                for(i in 1..distanceToRightUp) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col + i]}${row + i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col + i]}${row + i}") != null) {
                        foundBlockade = true
                    }

                }
                foundBlockade = false
                for(i in 1..distanceToLeftUp) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col - i]}${row + i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col - i]}${row + i}") != null) {
                        foundBlockade = true
                    }

                }
                foundBlockade = false

                for (i in 1..distanceToRightDown) {

                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col + i]}${row - i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col + i]}${row - i}") != null) {
                        foundBlockade = true
                    }
                }
                foundBlockade = false
                for (i in 1..distanceToLeftDown) {
                    if (foundBlockade) {
                        list.remove("${game.numberToLetterMapPlayerVersion[col - i]}${row - i}")
                    }
                    if (game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col - i]}${row - i}") != null) {
                        foundBlockade = true
                    }

                }

            }
            "K" -> { //DO NOTHING
            }
        }
    }
    return list
}

}