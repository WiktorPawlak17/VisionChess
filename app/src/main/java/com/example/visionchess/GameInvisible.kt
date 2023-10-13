package com.example.visionchess

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
import java.util.Timer



class GameInvisible : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_game_invisible, container, false)
        val player2pic = rootView.findViewById<ImageView>(R.id.player2pic2)
        val player1pic = rootView.findViewById<ImageView>(R.id.player1pic3)
        val buttonShowBoard = rootView.findViewById<Button>(R.id.buttonShowBoard)
        val player2name = rootView.findViewById<TextView>(R.id.player2name2)
        val player1name = rootView.findViewById<TextView>(R.id.player1name2)
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



//        if(timeFormat!= null && howManyPeeks!=null) {
//            val game = ChessGame()
//            timerWhite.run{}
//            while(game.isGameFinished()){
//            player1timer.text = counterWhite.toString()
//            }
//        }

        buttonShowBoard.setOnClickListener {
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, GameVisible())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        // Inflate the layout for this fragment
        return rootView
    }

    companion object {


    }
}