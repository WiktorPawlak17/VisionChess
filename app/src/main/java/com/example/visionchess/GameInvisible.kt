package com.example.visionchess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import org.json.JSONObject
import java.io.File
import java.util.Locale


@Suppress("SameParameterValue")
class GameInvisible : Fragment(), TextToSpeech.OnInitListener {
    val game = ChessGame()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechHandler: SpeechRecognitionHandler
    private lateinit var scope: CoroutineScope
    private lateinit var handler: android.os.Handler
    private lateinit var timeReferenceMe: com.google.firebase.database.Query
    private lateinit var timeReferenceOpponent: com.google.firebase.database.Query
    private lateinit var timerWhiteRunnable: Runnable
    private lateinit var timerBlackRunnable: Runnable
    private var timerWhiteSeconds = 7
    private var timerBlackSeconds = 7
    private var timerWhiteMinutes = 7
    private var timerBlackMinutes = 7
    private var timerWhiteSecondsIncrement = 0
    private var timerBlackSecondsIncrement = 0
    private var color = ""
    private lateinit var player1Reference: com.google.firebase.database.Query
    private lateinit var player2Reference: com.google.firebase.database.Query
    private lateinit var player1timer: TextView
    private lateinit var player2timer: TextView
    private var opponent : String = ""
    private lateinit var receivedMess: TextView
    private lateinit var settings: Settings
    private val allPieces = ArrayList<String>()
    private var whiteMovesCounter = 0
    private var blackMovesCounter = 0
    private lateinit var sayThatAgainButton: Button
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        settings = context?.let { readSettingsFromFile(it,"settings.json") }!!
        val rootView = inflater.inflate(R.layout.fragment_game_invisible, container, false)
        val player2pic = rootView.findViewById<ImageView>(R.id.player2Picture)
        val player1pic = rootView.findViewById<ImageView>(R.id.player1Picture)
        val buttonShowBoard = rootView.findViewById<Button>(R.id.showBoardButton)
        val player2name = rootView.findViewById<TextView>(R.id.player2Name)
        val player1name = rootView.findViewById<TextView>(R.id.player1Name)
        receivedMess = rootView.findViewById(R.id.receivedMessage)
        sayThatAgainButton = rootView.findViewById(R.id.sayThatAgain)
        val fragmentManager = activity?.supportFragmentManager
        player2timer = rootView.findViewById(R.id.player2Timer)
        player1timer = rootView.findViewById(R.id.player1Timer)
        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val gameLiveReference = databaseReference.child("gameLive")
        val currentOpponentReference = gameLiveReference.child(currentUser!!.uid).child("opponent")
        textToSpeech = TextToSpeech(requireContext(), this)
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

        handler = android.os.Handler(android.os.Looper.getMainLooper())
        timeReferenceMe = databaseReference.child("gameLive").child(currentUser.uid).child("timeFormat")
        timeReferenceOpponent = databaseReference.child("gameLive").child(opponent).child("timeFormat")
        player1Reference = databaseReference.child("users").child(currentUser.uid)
        player2Reference = databaseReference.child("users").child(opponent)

        val referenceToGetColor = databaseReference.child("gameLive").child(currentUser.uid).child("color")
        referenceToGetColor.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                color = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })


        buttonShowBoard.setOnClickListener {
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, GameVisible())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }



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



        timerWhiteRunnable = object : Runnable {
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
                if(color == "white"){
                    if(timerWhiteSeconds < 10) {
                        player1timer.text = "$timerWhiteMinutes:0$timerWhiteSeconds"
                    }
                    else {
                        player1timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                    }
                }else {
                    if(timerWhiteSeconds < 10){
                        player2timer.text = "$timerWhiteMinutes:0$timerWhiteSeconds"
                    }else {
                        player2timer.text = "$timerWhiteMinutes:$timerWhiteSeconds"
                    }
                }


                handler.postDelayed(this, 1000)
            }
//            fun stop(){
//                handler.removeCallbacks(this)
//            }

        }
        timerBlackRunnable = object : Runnable {
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
                }else {
                    if(timerBlackSeconds < 10){
                        player2timer.text = "$timerBlackMinutes:0$timerBlackSeconds"
                    }else {
                        player2timer.text = "$timerBlackMinutes:$timerBlackSeconds"
                    }
                }

                handler.postDelayed(this, 1000)
            }
//            fun stop(){
//                handler.removeCallbacks(this)
//            }

        }





handler.postDelayed({

        try {

            val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/${currentUser.uid}")
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


    },1500  )

        handler.postDelayed({
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

        },1000)

        //PRINT SOME

//        val pos = "D8"
//        Toast.makeText(context,
//               "$pos"+     game.getPieceAtPosition(pos)?.name.toString()+
//                    pieceReallySees(pos),
//                    Toast.LENGTH_LONG).show()
        // Inflate the layout for this fragment

        sayThatAgainButton.setOnClickListener {
            if (color == "white") {
                handler.removeCallbacks(timerWhiteRunnable)
                whiteToMove()
            }else {
                handler.removeCallbacks(timerBlackRunnable)
                blackToMove()
            }
        }
        //GET THE SETTINGS
        when(settings.language){
            "Polish" -> {
                allPieces.add("Pion")
                allPieces.add("Wieża")
                allPieces.add("Skoczek")
                allPieces.add("Goniec")
                allPieces.add("Królowa")
                allPieces.add("Król")
            }
            "English" -> {
                allPieces.add("Pawn")
                allPieces.add("Rook")
                allPieces.add("Knight")
                allPieces.add("Bishop")
                allPieces.add("Queen")
                allPieces.add("King")
            }
            else -> {
                allPieces.add("Pawn")
                allPieces.add("Rook")
                allPieces.add("Knight")
                allPieces.add("Bishop")
                allPieces.add("Queen")
                allPieces.add("King")
            }
        }


        beginGameInvisible()
        return rootView
    }
    private fun readSettingsFromFile(context: Context, fileName: String): Settings? {
        try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                return null
            }

            val jsonString = file.readText()
            val jsonObject = JSONObject(jsonString)
            val settingsJson = jsonObject.getJSONObject("Settings")

            return Settings(
                firstLaunch = settingsJson.getBoolean("firstLaunch"),
                sayPawn = settingsJson.getBoolean("sayPawn"),
                sayTakes = settingsJson.getBoolean("sayTakes"),
                sayPromotion = settingsJson.getBoolean("sayPromotion"),
                sayCheck = settingsJson.getBoolean("sayCheck"),
                sayOpponentPlayed = settingsJson.getBoolean("sayOpponentPlayed"),
                language = settingsJson.getString("language")
            )
        } catch (e: Exception) {
            e.printStackTrace() // Print the error stack trace for debugging
            return null
        }
    }

    private fun beginGameInvisible() {
        handler.postDelayed({textToSpeech.speak("You're playing $color",
            TextToSpeech.QUEUE_FLUSH, null, null)},1000)
        handler.postDelayed({
            whiteToMove()
        }, 3000)

    }
    @SuppressLint("SetTextI18n")
    private fun whiteToMove(){
        if(!game.isGameFinished){
            game.isWhiteTurn = true
            checkIfMoveIsCheck()
            var notationMoveMade: String
            timerWhiteRunnable.run()
            if(color=="white"){
                speechHandler = SpeechRecognitionHandler(requireContext())
                scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    delay(500)
                    speechHandler.startRecognition()
                    while(!speechHandler.youDone){
                        delay(500)
                    }
                    handler.postDelayed({
                        val message = speechHandler.recognizedMessage
                        receivedMess.text = message
                        detectPieceInMessage(message)
                        var checkHowManyPiecesCanMakeThisMove = 0
                        val positionsSaved: ArrayList<String> = ArrayList()
                        val currentPieceToMove = detectPieceInMessage(message)
                        val parsedMessage = parseMessage(message)
                        for(row in 1..8){
                            for(col in 1..8){
                                val position = "${game.numberToLetterMapPlayerVersion[col]}$row"
                                if(game.getPieceAtPosition(position)?.name == currentPieceToMove ){
                                    if(pieceReallySees(position)?.contains(parsedMessage) == true||
                                        canPawnMoveThere(position, parsedMessage.toString())){
                                        if(game.getPieceAtPosition(position)!!.color == "white"){
                                            checkHowManyPiecesCanMakeThisMove++
                                            positionsSaved.add(position)
                                        }
                                    }


                                }
                            }
                    }
                        when(positionsSaved.size) {
                            0 -> {
                                handler.removeCallbacks(timerWhiteRunnable)
                                whiteToMove()
                            }

                            1 -> {
                                val position = positionsSaved[0]
                                if (game.movePiece("$position$parsedMessage")) {
                                    checkIfMoveIsCheck()
                                    handler.removeCallbacks(timerWhiteRunnable)
                                    timerWhiteSeconds += timerWhiteSecondsIncrement
                                    notationMoveMade = "$currentPieceToMove$position$parsedMessage"
                                    updateGameStateWhite(notationMoveMade)
                                    checkForGameEnd()
                                    blackToMove()
                                }
                            }

                            else -> {
                                if (containsSingleLetterWithSpacing(message)) {
                                    for (i in positionsSaved){
                                        if(extractSingleLetterWithSpacing(message) == i[0]){
                                            if (game.movePiece("$i$parsedMessage")) {
                                                checkIfMoveIsCheck()
                                                handler.removeCallbacks(timerWhiteRunnable)
                                                timerWhiteSeconds += timerWhiteSecondsIncrement
                                                val helper = i[0].toString()
                                                notationMoveMade = "$currentPieceToMove$helper$i$parsedMessage"
                                                updateGameStateWhite(notationMoveMade)
                                                checkForGameEnd()
                                                blackToMove()
                                            }
                                        }
                                    }
                                }

                                if (containsSingleNumberWithSpacing(message)) {
                                    for (i in positionsSaved){
                                        if(extractSingleNumberWithSpacing(message) == i[1]){
                                            if (game.movePiece("$i$parsedMessage")) {
                                                checkIfMoveIsCheck()
                                                handler.removeCallbacks(timerWhiteRunnable)
                                                timerWhiteSeconds += timerWhiteSecondsIncrement
                                                val helper = i[1].toString()
                                                notationMoveMade = "$currentPieceToMove$helper$i$parsedMessage"
                                                updateGameStateWhite(notationMoveMade)
                                                checkForGameEnd()
                                                blackToMove()
                                            }
                                        }
                                    }
                                }
                            }


                        }



                    },750)
                }
            }else {
                //AWAIT RESPONSE FROM OTHER PLAYER
                //HERE IS WHITE TO MOVE
                val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
                val databaseReference = database.reference
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                val gameLiveReference = databaseReference.child("gameLive")
                val moveToAwait = whiteMovesCounter + 1
                val currentRefToAwait = gameLiveReference.child("${currentUser?.uid}/moves/white/$moveToAwait")
                currentRefToAwait.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val move = snapshot.value.toString()
                        //get The timer from the other player
                        val timeLeftOpponent = gameLiveReference.child("$opponent/timeLeft")
                        timeLeftOpponent.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val timeLeft = snapshot.value.toString()
                                when(timeLeft.length){
                                    4-> {
                                        val timeLeftMinutes = timeLeft[0].toString().toInt()
                                        val timeLeftSeconds = timeLeft.substring(2, 4).toInt()
                                        timerWhiteMinutes = timeLeftMinutes
                                        timerWhiteSeconds = timeLeftSeconds
                                        player2timer.text = "$timeLeftMinutes:$timeLeftSeconds"
                                    }
                                    5-> {
                                        val timeLeftMinutes = timeLeft.substring(0, 2).toInt()
                                        val timeLeftSeconds = timeLeft.substring(3, 5).toInt()
                                        timerWhiteMinutes = timeLeftMinutes
                                        timerWhiteSeconds = timeLeftSeconds
                                        player2timer.text = "$timeLeftMinutes:$timeLeftSeconds"
                                    }
                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        })
                        if(move != "null"){
                            val currPiece = detectPieceInMessage(move)
                            var newMove = move.replace(currPiece, "")
                            when(newMove.length){
                                4 -> {
                                    if (game.movePiece(newMove)) {
                                        handler.removeCallbacks(timerWhiteRunnable)
                                        timerWhiteSeconds += timerWhiteSecondsIncrement
                                        checkForGameEnd()
                                        blackToMove()
                                    }
                                }
                                5->{
                                    newMove = newMove.substring(1,currPiece.length)
                                    if (game.movePiece(newMove)) {
                                        handler.removeCallbacks(timerWhiteRunnable)
                                        timerWhiteSeconds += timerWhiteSecondsIncrement
                                        checkForGameEnd()
                                        blackToMove()
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                })

            }

            }


    }




    private fun blackToMove(){
        if(!game.isGameFinished){
            game.isWhiteTurn = false
            timerBlackRunnable.run()
            checkIfMoveIsCheck()
            var notationMoveMade: String
            if(color=="black"){
                speechHandler = SpeechRecognitionHandler(requireContext())
                scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    delay(500)
                    speechHandler.startRecognition()
                    while(!speechHandler.youDone){
                        delay(500)
                    }
                    handler.postDelayed({
                        val message = speechHandler.recognizedMessage
                        receivedMess.text = message
                        detectPieceInMessage(message)
                        var checkHowManyPiecesCanMakeThisMove = 0
                        val positionsSaved: ArrayList<String> = ArrayList()
                        val currentPieceToMove = detectPieceInMessage(message)
//
                        val parsedMessage = parseMessage(message)
                        for(row in 1..8){
                            for(col in 1..8){
                                val position = "${game.numberToLetterMapPlayerVersion[col]}$row"
                                if(game.getPieceAtPosition(position)?.name == currentPieceToMove){
                                    if(pieceReallySees(position)?.contains(parsedMessage) == true
                                        ||
                                        canPawnMoveThere(position, parsedMessage.toString())){
                                        if(game.getPieceAtPosition(position)!!.color == "black"){
                                            checkHowManyPiecesCanMakeThisMove++
                                            positionsSaved.add(position)
                                        }

                                    }
                                }
                            }
                        }
                        when(positionsSaved.size) {
                            0 -> {
                                handler.removeCallbacks(timerBlackRunnable)
                                receivedMess.text = "Not a single piece can make this move"
                                blackToMove()
                            }

                            1 -> {
                                val position = positionsSaved[0]
                                if (game.movePiece("$position$parsedMessage")) {
                                    checkIfMoveIsCheck()
                                    handler.removeCallbacks(timerBlackRunnable)
                                    timerBlackSeconds += timerBlackSecondsIncrement
                                    notationMoveMade = "$currentPieceToMove$position$parsedMessage"
                                    updateGameStateBlack(notationMoveMade)
                                    checkForGameEnd()
                                    whiteToMove()
                                }
                            }

                            else -> {
                                if (containsSingleLetterWithSpacing(message)) {
                                    for (i in positionsSaved){
                                        if(extractSingleLetterWithSpacing(message) == i[0]){
                                            if (game.movePiece("$i$parsedMessage")) {
                                                checkIfMoveIsCheck()
                                                handler.removeCallbacks(timerBlackRunnable)
                                                timerBlackSeconds += timerBlackSecondsIncrement
                                                val helper = i[0].toString()
                                                notationMoveMade = "$currentPieceToMove$helper$i$parsedMessage"
                                                updateGameStateBlack(notationMoveMade)
                                                checkForGameEnd()
                                                whiteToMove()
                                            }
                                        }
                                    }
                                }

                                if (containsSingleNumberWithSpacing(message)) {
                                    for (i in positionsSaved){
                                        if(extractSingleNumberWithSpacing(message) == i[1]){
                                            if (game.movePiece("$i$parsedMessage")) {
                                                checkIfMoveIsCheck()
                                                handler.removeCallbacks(timerBlackRunnable)
                                                timerBlackSeconds += timerBlackSecondsIncrement
                                                val helper = i[1].toString()
                                                notationMoveMade = "$currentPieceToMove$helper$i$parsedMessage"
                                                updateGameStateBlack(notationMoveMade)
                                                checkForGameEnd()
                                                whiteToMove()
                                            }
                                        }
                                    }
                                }
                            }


                        }


//                        blackToMove()
                    },750)
                }
            }else {
                //HERE IS BLACK TO MOVE
                //AWAIT RESPONSE FROM OTHER PLAYER
                val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
                val databaseReference = database.reference
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                val gameLiveReference = databaseReference.child("gameLive")
                val moveToAwait = blackMovesCounter + 1
                val currentRefToAwait = gameLiveReference.child("${currentUser?.uid}/moves/black/$moveToAwait")
                currentRefToAwait.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val move = snapshot.value.toString()
                        //get The timer from the other player
                        handler.postDelayed({
                            val timeLeftOpponent = gameLiveReference.child("$opponent/timeLeft")
                            timeLeftOpponent.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot){
                                    val timeLeft = snapshot.value.toString()
                                    when(timeLeft.length){
                                        4-> {
                                            val timeLeftMinutes = timeLeft[0].toString().toInt()
                                            val timeLeftSeconds = timeLeft.substring(2, 4).toInt()
                                            timerWhiteMinutes = timeLeftMinutes
                                            timerWhiteSeconds = timeLeftSeconds
                                            player2timer.text = "$timeLeftMinutes:$timeLeftSeconds"
                                        }
                                        5-> {
                                            val timeLeftMinutes = timeLeft.substring(0, 2).toInt()
                                            val timeLeftSeconds = timeLeft.substring(3, 5).toInt()
                                            timerWhiteMinutes = timeLeftMinutes
                                            timerWhiteSeconds = timeLeftSeconds
                                            player2timer.text = "$timeLeftMinutes:$timeLeftSeconds"
                                        }
                                        6 -> {
                                            val timeLeftMinutes = timeLeft[1].code
                                            val timeLeftSeconds = timeLeft.substring(3, 5).toInt()
                                            timerWhiteMinutes = timeLeftMinutes
                                            timerWhiteSeconds = timeLeftSeconds
                                            player2timer.text = "$timeLeftMinutes:$timeLeftSeconds"
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                        },500)

                        if(move != "null"){
                            val currPiece = detectPieceInMessage(move)
                            var newMove = move.replace(currPiece, "")
                            when(newMove.length){
                                4 -> {
                                    if (game.movePiece(newMove)) {
                                        checkForGameEnd()
                                        handler.removeCallbacks(timerBlackRunnable)
                                        timerBlackSeconds += timerBlackSecondsIncrement
                                        whiteToMove()
                                    }
                                }
                                5->{
                                    newMove = newMove.substring(1,currPiece.length)
                                    if (game.movePiece(newMove)) {
                                        checkForGameEnd()
                                        handler.removeCallbacks(timerBlackRunnable)
                                        timerBlackSeconds += timerBlackSecondsIncrement
                                        whiteToMove()
                                    }
                                }
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

        }

    }
    private fun checkIfMoveIsCheck(): Boolean {
        val positionOfWhiteKing = ""
        val positionOfBlackKing = ""
        val squaresCoveredByWhite = ArrayList<String>()
        val squaresCoveredByBlack = ArrayList<String>()
        for (row in 1..8) {
            for (col in 'A'..'H') {
                val position = "$col$row"
                if (game.getPieceAtPosition(position)?.name == "King") {
                    for (row2 in 1..8) {
                        for (col2 in 'A'..'H') {
                            val position2 = "$col2$row2"
                            val currColorOfKing = game.getPieceAtPosition(position)?.color
                            if (game.getPieceAtPosition(position2)?.color != currColorOfKing) {
                                if (game.getPieceAtPosition(position2)?.pieceSees()
                                        ?.contains(position) == true
                                ) {
                                    if (currColorOfKing == "white") {
                                        squaresCoveredByBlack.add(position2)
                                    } else {
                                        squaresCoveredByWhite.add(position2)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        val uniqueListWhite = squaresCoveredByWhite.distinct()
        val uniqueListBlack = squaresCoveredByBlack.distinct()
        if(uniqueListWhite.contains(positionOfBlackKing) || uniqueListBlack.contains(positionOfWhiteKing)){
            return true
        }
        return false
    }

    private fun checkForGameEnd():Boolean {
        if(checkIfMoveIsCheck()){
            return true
        }
        return false
    }
    private fun updateGameStateWhite(notationMoveMade: String) {
        val database =
            FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        whiteMovesCounter++
        val gameLiveReference = databaseReference.child("gameLive")
        val myLiveReference =
            gameLiveReference.child("${currentUser?.uid}/moves/white/$whiteMovesCounter")
        val currentOpponentReference =
            gameLiveReference.child("$opponent/moves/white/$whiteMovesCounter")
        myLiveReference.setValue(notationMoveMade)
        currentOpponentReference.setValue(notationMoveMade)
        val timeLeft:String = if (color == "white") {
            "$timerWhiteMinutes:$timerWhiteSeconds"
        } else {
            "$timerBlackMinutes:$timerBlackSeconds"
        }
        gameLiveReference.child("${currentUser?.uid}/timeLeft").setValue(timeLeft)
        gameLiveReference.child("${currentUser?.uid}/currentBoardState").setValue(game.chessBoard2.toString())
        gameLiveReference.child("$opponent/currentBoardState").setValue(game.chessBoard2.toString())
    }
    private fun updateGameStateBlack(notationMoveMade: String) {
        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        blackMovesCounter++
        val gameLiveReference = databaseReference.child("gameLive")
        val myLiveReference =
            gameLiveReference.child("${currentUser?.uid}/moves/black/$blackMovesCounter")
        val currentOpponentReference =
            gameLiveReference.child("$opponent/moves/black/$blackMovesCounter")
        myLiveReference.setValue(notationMoveMade)
        currentOpponentReference.setValue(notationMoveMade)
        val timeLeft:String = if (color == "white") {
            "$timerWhiteMinutes:$timerWhiteSeconds"
        } else {
            "$timerBlackMinutes:$timerBlackSeconds"
        }
        gameLiveReference.child("${currentUser?.uid}/timeLeft").setValue(timeLeft)
        gameLiveReference.child("${currentUser?.uid}/currentBoardState").setValue(game.chessBoard2.toString())
        gameLiveReference.child("$opponent/currentBoardState").setValue(game.chessBoard2.toString())
    }
    private fun detectPieceInMessage(message: String):String {
    var currPiece = "Pawn"
    for(piece in allPieces){
        if(message.contains(piece)){
            currPiece = piece
        }
    }
        return currPiece
    }
    private fun containsSingleNumberWithSpacing(input: String): Boolean {
        val regex = """\s[1-8]\s""".toRegex()
        return regex.containsMatchIn(input)
    }
    private fun containsSingleLetterWithSpacing(input:String): Boolean {
        val regex = """\s[A-H]\s""".toRegex()
        return regex.containsMatchIn(input)
    }
    private fun extractSingleLetterWithSpacing(input: String): Char? {
        val regex = """\s([A-H])\s""".toRegex()
        val matchResult = regex.find(input)

        return matchResult?.groupValues?.get(1)?.singleOrNull()
    }
    private fun extractSingleNumberWithSpacing(input: String):Char? {
        val regex = """\s([1-8])\s""".toRegex()
        val matchResult = regex.find(input)

        return matchResult?.groupValues?.get(1)?.singleOrNull()
    }
    private fun parseMessage(mess: String): String? {
        var message = mess
        if(settings.sayPawn){
            //PL CODE
            if(message.contains("pion")){
                message = message.replace("pion","")
            }
            //ENG CODE
            if(message.contains("pawn")){
                message = message.replace("pawn","")
            }
        }
        if(settings.sayTakes){
            //PL CODE
            if(message.contains("bierze")){
                message = message.replace("bierze","")
            }
            //ENG CODE
            if(message.contains("takes")){
                message = message.replace("takes","")
            }
        }
        if(settings.sayPromotion) {
            //PL CODE
            if (message.contains("promocja")) {
                message = message.replace("promocja", "")
            }
            //ENG CODE
            if (message.contains("promotes to a")) {
                message = message.replace("promotes to a", "")
            }
            if (message.contains("promotion")) {
                message = message.replace("promotion", "")
            }
            if (message.contains("promote")) {
                message = message.replace("promote", "")
            }
            if (message.contains("promotes")) {
                message = message.replace("promotes", "")
            }

        }
        if(settings.sayCheck){
            //PL CODE
            if(message.contains("szach")){
                message = message.replace("szach","")
            }
            //ENG CODE
            if(message.contains("check")){
                message = message.replace("check","")
            }
        }

        val regex = Regex("([A-H])([1-8])")
        val matchResult = regex.find(message)

        return matchResult?.let {
            val letter = it.groupValues[1]
            val number = it.groupValues[2]
            "$letter$number"
        }


    }

    private fun canPawnMoveThere(fromPos:String, toPos:String): Boolean {
      if(game.getPieceAtPosition(fromPos)?.name == "Pawn"){
          val color = game.getPieceAtPosition(fromPos)?.color
            val col = game.letterToNumberMapPlayerVersion[fromPos[0].toString()]
            val row = fromPos[1].toString().toInt()
          if(color =="white"){
              if(game.getPieceAtPosition(fromPos)?.isMoved == false){
                  if(toPos == "${game.numberToLetterMapPlayerVersion[col]}${row+2}" && game.getPieceAtPosition(toPos) == null && game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col]}${row+1}") == null){
                      return true
                  }
              }
              if(toPos == "${game.numberToLetterMapPlayerVersion[col]}${row+1}" && game.getPieceAtPosition(toPos) == null){
                  return true
              }
          }
          else {
                if(game.getPieceAtPosition(fromPos)?.isMoved == false){
                    if(toPos == "${game.numberToLetterMapPlayerVersion[col]}${row-2}" && game.getPieceAtPosition(toPos) == null && game.getPieceAtPosition("${game.numberToLetterMapPlayerVersion[col]}${row-1}") == null){
                        return true
                    }
                }
                if(toPos == "${game.numberToLetterMapPlayerVersion[col]}${row-1}" && game.getPieceAtPosition(toPos) == null){
                    return true
                }
          }
      }
        return false
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
            "Pawn" -> { //GONNA DO NOTHING

            }
            "Rook" -> {
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
            "Knight" -> { //GONNA DO NOTHING
            }
            "Bishop" -> {
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
            "Queen" -> {
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
            "King" -> { //DO NOTHING
            }
        }
    }
    return list
}
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locale = Locale.getDefault()

            if (textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                textToSpeech.language = locale
            } else {
                textToSpeech.language = Locale.US
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
    }
}