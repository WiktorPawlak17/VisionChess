package com.example.visionchess

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.visionchess.Pieces.Piece
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale


class GameVisible : Fragment(),TextToSpeech.OnInitListener {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var receivedHashMap: HashMap<String, Piece?>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        textToSpeech = TextToSpeech(requireContext(), this)
//        val numberToLetterSmallVersion = mapOf(
//            1 to "a",
//            2 to "b",
//            3 to "c",
//            4 to "d",
//            5 to "e",
//            6 to "f",
//            7 to "g",
//            8 to "h"
//        )
//        val letterToNumberSmallVersion = mapOf(
//            "a" to 1,
//            "b" to 2,
//            "c" to 3,
//            "d" to 4,
//            "e" to 5,
//            "f" to 6,
//            "g" to 7,
//            "h" to 8
//        )
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val fragmentManager = activity?.supportFragmentManager
        textToSpeech = TextToSpeech(requireContext(), this)
        var timeLeft = 15
        val timerCounter = object : Runnable {
            override fun run() {
                timeLeft--
                if (timeLeft == 10) {
                    textToSpeech.speak("10", TextToSpeech.QUEUE_FLUSH, null, null)
                }
                if (timeLeft == 3) {
                    textToSpeech.speak("3", TextToSpeech.QUEUE_FLUSH, null, null)
                }
                if (timeLeft == 2) {
                    textToSpeech.speak("2", TextToSpeech.QUEUE_FLUSH, null, null)
                }
                if (timeLeft == 1) {
                    textToSpeech.speak("1", TextToSpeech.QUEUE_FLUSH, null, null)
                }
                handler.postDelayed(this, 1000)
            }

        }

        timerCounter.run()
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_game_visible, container, false)
        val a1 = rootView.findViewById<ImageView>(R.id.a1)
        val a2 = rootView.findViewById<ImageView>(R.id.a2)
        val a3 = rootView.findViewById<ImageView>(R.id.a3)
        val a4 = rootView.findViewById<ImageView>(R.id.a4)
        val a5 = rootView.findViewById<ImageView>(R.id.a5)
        val a6 = rootView.findViewById<ImageView>(R.id.a6)
        val a7 = rootView.findViewById<ImageView>(R.id.a7)
        val a8 = rootView.findViewById<ImageView>(R.id.a8)
        val b1 = rootView.findViewById<ImageView>(R.id.b1)
        val b2 = rootView.findViewById<ImageView>(R.id.b2)
        val b3 = rootView.findViewById<ImageView>(R.id.b3)
        val b4 = rootView.findViewById<ImageView>(R.id.b4)
        val b5 = rootView.findViewById<ImageView>(R.id.b5)
        val b6 = rootView.findViewById<ImageView>(R.id.b6)
        val b7 = rootView.findViewById<ImageView>(R.id.b7)
        val b8 = rootView.findViewById<ImageView>(R.id.b8)
        val c1 = rootView.findViewById<ImageView>(R.id.c1)
        val c2 = rootView.findViewById<ImageView>(R.id.c2)
        val c3 = rootView.findViewById<ImageView>(R.id.c3)
        val c4 = rootView.findViewById<ImageView>(R.id.c4)
        val c5 = rootView.findViewById<ImageView>(R.id.c5)
        val c6 = rootView.findViewById<ImageView>(R.id.c6)
        val c7 = rootView.findViewById<ImageView>(R.id.c7)
        val c8 = rootView.findViewById<ImageView>(R.id.c8)
        val d1 = rootView.findViewById<ImageView>(R.id.d1)
        val d2 = rootView.findViewById<ImageView>(R.id.d2)
        val d3 = rootView.findViewById<ImageView>(R.id.d3)
        val d4 = rootView.findViewById<ImageView>(R.id.d4)
        val d5 = rootView.findViewById<ImageView>(R.id.d5)
        val d6 = rootView.findViewById<ImageView>(R.id.d6)
        val d7 = rootView.findViewById<ImageView>(R.id.d7)
        val d8 = rootView.findViewById<ImageView>(R.id.d8)
        val e1 = rootView.findViewById<ImageView>(R.id.e1)
        val e2 = rootView.findViewById<ImageView>(R.id.e2)
        val e3 = rootView.findViewById<ImageView>(R.id.e3)
        val e4 = rootView.findViewById<ImageView>(R.id.e4)
        val e5 = rootView.findViewById<ImageView>(R.id.e5)
        val e6 = rootView.findViewById<ImageView>(R.id.e6)
        val e7 = rootView.findViewById<ImageView>(R.id.e7)
        val e8 = rootView.findViewById<ImageView>(R.id.e8)
        val f1 = rootView.findViewById<ImageView>(R.id.f1)
        val f2 = rootView.findViewById<ImageView>(R.id.f2)
        val f3 = rootView.findViewById<ImageView>(R.id.f3)
        val f4 = rootView.findViewById<ImageView>(R.id.f4)
        val f5 = rootView.findViewById<ImageView>(R.id.f5)
        val f6 = rootView.findViewById<ImageView>(R.id.f6)
        val f7 = rootView.findViewById<ImageView>(R.id.f7)
        val f8 = rootView.findViewById<ImageView>(R.id.f8)
        val g1 = rootView.findViewById<ImageView>(R.id.g1)
        val g2 = rootView.findViewById<ImageView>(R.id.g2)
        val g3 = rootView.findViewById<ImageView>(R.id.g3)
        val g4 = rootView.findViewById<ImageView>(R.id.g4)
        val g5 = rootView.findViewById<ImageView>(R.id.g5)
        val g6 = rootView.findViewById<ImageView>(R.id.g6)
        val g7 = rootView.findViewById<ImageView>(R.id.g7)
        val g8 = rootView.findViewById<ImageView>(R.id.g8)
        val h1 = rootView.findViewById<ImageView>(R.id.h1)
        val h2 = rootView.findViewById<ImageView>(R.id.h2)
        val h3 = rootView.findViewById<ImageView>(R.id.h3)
        val h4 = rootView.findViewById<ImageView>(R.id.h4)
        val h5 = rootView.findViewById<ImageView>(R.id.h5)
        val h6 = rootView.findViewById<ImageView>(R.id.h6)
        val h7 = rootView.findViewById<ImageView>(R.id.h7)
        val h8 = rootView.findViewById<ImageView>(R.id.h8)
        val player2pic = rootView.findViewById<ImageView>(R.id.player2pic)
        val player1pic = rootView.findViewById<ImageView>(R.id.player1pic)
        val player2name = rootView.findViewById<TextView>(R.id.player2name)
        val player1name = rootView.findViewById<TextView>(R.id.player1name)
        val blackRook1 = rootView.findViewById<ImageView>(R.id.blackRook1)
        val blackKnight1 = rootView.findViewById<ImageView>(R.id.blackKnight1)
        val blackBishop1 = rootView.findViewById<ImageView>(R.id.blackBishop1)
        val blackQueen1 = rootView.findViewById<ImageView>(R.id.blackQueen)
        val blackKing = rootView.findViewById<ImageView>(R.id.blackKing)
        val blackBishop2 = rootView.findViewById<ImageView>(R.id.blackBishop2)
        val blackKnight2 = rootView.findViewById<ImageView>(R.id.blackKnight2)
        val blackRook2 = rootView.findViewById<ImageView>(R.id.blackRook2)
        val blackPawn1 = rootView.findViewById<ImageView>(R.id.blackAPawn)
        val blackPawn2 = rootView.findViewById<ImageView>(R.id.blackBPawn)
        val blackPawn3 = rootView.findViewById<ImageView>(R.id.blackCPawn)
        val blackPawn4 = rootView.findViewById<ImageView>(R.id.blackDPawn)
        val blackPawn5 = rootView.findViewById<ImageView>(R.id.blackEPawn)
        val blackPawn6 = rootView.findViewById<ImageView>(R.id.blackFPawn)
        val blackPawn7 = rootView.findViewById<ImageView>(R.id.blackGPawn)
        val blackPawn8 = rootView.findViewById<ImageView>(R.id.blackHPawn)
        val whiteRook1 = rootView.findViewById<ImageView>(R.id.whiteRook1)
        val whiteKnight1 = rootView.findViewById<ImageView>(R.id.whiteKnight1)
        val whiteBishop1 = rootView.findViewById<ImageView>(R.id.whiteBishop1)
        val whiteQueen1 = rootView.findViewById<ImageView>(R.id.whiteQueen)
        val whiteKing = rootView.findViewById<ImageView>(R.id.whiteKing)
        val whiteBishop2 = rootView.findViewById<ImageView>(R.id.whiteBishop2)
        val whiteKnight2 = rootView.findViewById<ImageView>(R.id.whiteKnight2)
        val whiteRook2 = rootView.findViewById<ImageView>(R.id.whiteRook2)
        val whitePawn1 = rootView.findViewById<ImageView>(R.id.whiteAPawn)
        val whitePawn2 = rootView.findViewById<ImageView>(R.id.whiteBPawn)
        val whitePawn3 = rootView.findViewById<ImageView>(R.id.whiteCPawn)
        val whitePawn4 = rootView.findViewById<ImageView>(R.id.whiteDPawn)
        val whitePawn5 = rootView.findViewById<ImageView>(R.id.whiteEPawn)
        val whitePawn6 = rootView.findViewById<ImageView>(R.id.whiteFPawn)
        val whitePawn7 = rootView.findViewById<ImageView>(R.id.whiteGPawn)
        val whitePawn8 = rootView.findViewById<ImageView>(R.id.whiteHPawn)
        var whiteKnightCount = 0
        var whiteRookCount = 0
        var whiteBishopCount = 0
        var whiteQueenCount = 0
        var whitePawnCount = 0
        var blackKnightCount = 0
        var blackRookCount = 0
        var blackBishopCount = 0
        var blackQueenCount = 0
        var blackPawnCount = 0
        val getString = arguments?.getString("chessBoard2")

//        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
//        val databaseReference = database.reference
//        val auth = FirebaseAuth.getInstance()
//        val currentUser = auth.currentUser
//        val gameLiveReference = databaseReference.child("gameLive")
//        val findChessBoard = gameLiveReference.child("${currentUser!!.uid}/currentBoardState")
//        findChessBoard.addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
//            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
//                val chessBoard = snapshot.value as HashMap<String, Piece?>
//                receivedHashMap = chessBoard
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                println("error")
//            }
//            })


        for (row in 1..8) {
            for (col in 'A'..'H') {
                if (receivedHashMap["$row$col"] != null) {
                    var currCount = 0
                    val name = receivedHashMap["$row$col"]!!.name
                    val color = receivedHashMap["$row$col"]!!.color
                    val position = receivedHashMap["$row$col"]!!.position
                    val isAlive = receivedHashMap["$row$col"]!!.isAlive
                    val col2 = col.lowercaseChar()
                    val currPos = "$col2$row"
                    val currID =
                        resources.getIdentifier(currPos, "id", requireContext().packageName)
                    val currTile = rootView.findViewById<ImageView>(currID)
                    //val constraintLayout = rootView.findViewById<ConstraintLayout>(R.id.frameLayout6)
                    when (name) {
                        "Knight" -> {
                            currCount = if (color == "white") {
                                whiteKnightCount++
                                whiteKnightCount
                            } else {
                                blackKnightCount++
                                blackKnightCount
                            }
                        }

                        "Rook" -> {
                            currCount = if (color == "white") {
                                whiteRookCount++
                                whiteRookCount
                            } else {
                                blackRookCount++
                                blackRookCount
                            }
                        }

                        "Bishop" -> {
                            currCount = if (color == "white") {
                                whiteBishopCount++
                                whiteBishopCount
                            } else {
                                blackBishopCount++
                                blackBishopCount
                            }
                        }

                        "Queen" -> {
                            currCount = if (color == "white") {
                                whiteQueenCount++
                                whiteQueenCount
                            } else {
                                blackQueenCount++
                                blackQueenCount
                            }
                        }

                        "King" -> {
//                                if(color=="white"){
//
//                                }else {
//
//                                }
                        }

                        "Pawn" -> {
                            currCount = if (color == "white") {
                                whitePawnCount++
                                whitePawnCount
                            } else {
                                blackPawnCount++
                                blackPawnCount
                            }
                        }

                    }
                    val currPiece = if (name == "King") {
                        resources.getIdentifier("$color$name", "id", requireContext().packageName)
                    } else {
                        resources.getIdentifier(
                            "$color$name$currCount",
                            "id",
                            requireContext().packageName
                        )

                    }
                    val currPieceImageView = rootView.findViewById<ImageView>(currPiece)
                    currPieceImageView.visibility = View.VISIBLE
                    val currTileHeight = currTile.height
                    val currTileWidth = currTile.width

                    val layoutParams =
                        currPieceImageView.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.topToTop = currTile.id
                    layoutParams.bottomToBottom = currTile.id
                    layoutParams.startToStart = currTile.id
                    layoutParams.endToEnd = currTile.id
                    layoutParams.width = currTileWidth / 2
                    layoutParams.height = currTileHeight / 2
                    currPieceImageView.layoutParams = layoutParams
                }


            }
        }

        handler.postDelayed({
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainerView, GameInvisible())?.addToBackStack(null)
                ?.commit()
            val bundle = Bundle()
            bundle.putString("chessBoard2", receivedHashMap.toString())
            val receiverFragment = GameVisible()
            receiverFragment.arguments = bundle
        }, 15000)


        return rootView
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
}
//    fun findSubStringsAndReturnHashmap(mainString:String, subStringsToFind:HashMap<String,Piece?>):HashMap<String, Piece?>{
//        val piecesToFind = arrayListOf<String>()
//        piecesToFind.add("King")
//        piecesToFind.add("Queen")
//        piecesToFind.add("Bishop")
//        piecesToFind.add("Knight")
//        piecesToFind.add("Rook")
//        piecesToFind.add("Pawn")
//
//        val substrings = mainString.split(",")
//
//        for(substring in substrings){
//            for(piece in piecesToFind){
//                if(substring.contains(piece)){
//                    val position = substring.substring(0,2)
//                    subStringsToFind[position] = when(substring){
//                        "King" -> {
//                            Piece("King",
//                }
//            }
//        }
//        }
//
//
//
//    }
//
//}
