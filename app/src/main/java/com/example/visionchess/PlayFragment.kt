package com.example.visionchess

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_play, container, false)
        val buttonLastPlayed = rootView.findViewById<Button>(R.id.buttonLastPlayed)
        val buttonRankedGame = rootView.findViewById<Button>(R.id.buttonRankedGame)
        val buttonCasualGame = rootView.findViewById<Button>(R.id.buttonCasualGame)
        val buttonHotSeat = rootView.findViewById<Button>(R.id.buttonHotSeatGame)
        val buttonNonBlindfoldVsBlindfold = rootView.findViewById<Button>(R.id.buttonNonBlindfoldVsBlindfold)
        val buttonBlindfoldVsNonBlindfold = rootView.findViewById<Button>(R.id.buttonBlindfoldVsNonBlindfold)
        val goBackButton = rootView.findViewById<Button>(R.id.buttonGoBack)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        buttonLastPlayed.startAnimation(animationFadeIn)
        buttonRankedGame.startAnimation(animationFadeIn)
        buttonCasualGame.startAnimation(animationFadeIn)
        buttonHotSeat.startAnimation(animationFadeIn)
        buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeIn)
        buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeIn)
        goBackButton.startAnimation(animationFadeIn)
        // Inflate the layout for this fragment
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            inflater.inflate(R.layout.fragment_play, container, false)
        }, 250)
        val fragmentManager = activity?.supportFragmentManager
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        goBackButton.setOnClickListener {
            goBackButton.startAnimation(animationFadeOut)
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)?.commit()
            }, 250)

        }
        return rootView
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}