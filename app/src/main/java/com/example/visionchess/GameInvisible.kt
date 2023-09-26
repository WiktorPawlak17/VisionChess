package com.example.visionchess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.util.Timer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameInvisible.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameInvisible : Fragment() {
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
    private var timerWhite = Timer()
    val counterWhite = 0
    private var timerBlack = Timer()
    val counterBlack = 0
    private val currentGameMode = Bundle()
    private val timeFormat = currentGameMode.getString("TimeFormat")
    private val howManyPeeks = currentGameMode.getString("HowManyPeeks")
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_game_invisible, container, false)
        timerWhite.run {  }

        if(timeFormat!= null && howManyPeeks!=null) {
         ChessGame(timeFormat, howManyPeeks)
        }

        while(ChessGame.isGameFinished() == false) {
            if (ChessGame.isWhiteTurn()) {
                timerWhite.run {  }
                timerBlack.cancel()
            } else {
                timerBlack.run {  }
                timerWhite.cancel()
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
         * @return A new instance of fragment GameInvisible.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameInvisible().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}