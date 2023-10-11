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

        val currentGameMode = Bundle()
        //val timeFormat = currentGameMode.getString("TimeFormat")
        // howManyPeeks = currentGameMode.getString("HowManyPeeks")
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        //TIME FORMAT CRASHES THE CODe
        var timerWhite = Timer()
        //Toast.makeText(context, timeFormat, Toast.LENGTH_SHORT).show()
        //val counterWhite = timeFormat!!.toInt()*60
        var timerBlack = Timer()
        //val counterBlack = timeFormat!!.toInt()*60


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