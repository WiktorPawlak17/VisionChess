package com.example.visionchess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameVisible.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameVisible : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
        val blackQueen = rootView.findViewById<ImageView>(R.id.blackQueen)
        val blackKing = rootView.findViewById<ImageView>(R.id.blackKing)
        val blackBishop2 = rootView.findViewById<ImageView>(R.id.blackBishop2)
        val blackKnight2 = rootView.findViewById<ImageView>(R.id.blackKnight2)
        val blackRook2 = rootView.findViewById<ImageView>(R.id.blackRook2)
        val blackAPawn = rootView.findViewById<ImageView>(R.id.blackAPawn)
        val blackBPawn = rootView.findViewById<ImageView>(R.id.blackBPawn)
        val blackCPawn = rootView.findViewById<ImageView>(R.id.blackCPawn)
        val blackDPawn = rootView.findViewById<ImageView>(R.id.blackDPawn)
        val blackEPawn = rootView.findViewById<ImageView>(R.id.blackEPawn)
        val blackFPawn = rootView.findViewById<ImageView>(R.id.blackFPawn)
        val blackGPawn = rootView.findViewById<ImageView>(R.id.blackGPawn)
        val blackHPawn = rootView.findViewById<ImageView>(R.id.blackHPawn)
        val whiteRook1 = rootView.findViewById<ImageView>(R.id.whiteRook1)
        val whiteKnight1 = rootView.findViewById<ImageView>(R.id.whiteKnight1)
        val whiteBishop1 = rootView.findViewById<ImageView>(R.id.whiteBishop1)
        val whiteQueen = rootView.findViewById<ImageView>(R.id.whiteQueen)
        val whiteKing = rootView.findViewById<ImageView>(R.id.whiteKing)
        val whiteBishop2 = rootView.findViewById<ImageView>(R.id.whiteBishop2)
        val whiteKnight2 = rootView.findViewById<ImageView>(R.id.whiteKnight2)
        val whiteRook2 = rootView.findViewById<ImageView>(R.id.whiteRook2)
        val whiteAPawn = rootView.findViewById<ImageView>(R.id.whiteAPawn)
        val whiteBPawn = rootView.findViewById<ImageView>(R.id.whiteBPawn)
        val whiteCPawn = rootView.findViewById<ImageView>(R.id.whiteCPawn)
        val whiteDPawn = rootView.findViewById<ImageView>(R.id.whiteDPawn)
        val whiteEPawn = rootView.findViewById<ImageView>(R.id.whiteEPawn)
        val whiteFPawn = rootView.findViewById<ImageView>(R.id.whiteFPawn)
        val whiteGPawn = rootView.findViewById<ImageView>(R.id.whiteGPawn)
        val whiteHPawn = rootView.findViewById<ImageView>(R.id.whiteHPawn)




        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Game.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameVisible().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}