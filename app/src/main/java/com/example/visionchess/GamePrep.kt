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
import android.widget.Spinner
import android.widget.TextView


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GamePrep.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamePrep : Fragment() {

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
        val rootView = inflater.inflate(R.layout.fragment_game_prep, container, false)
        val whichButtonClicked = arguments
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val timeFormatTextView = rootView.findViewById<TextView>(R.id.timeFormatTextView)
        val timeFormatSpinner = rootView.findViewById<Spinner>(R.id.spinner2)
        val howManyPeeksTextView = rootView.findViewById<TextView>(R.id.howManyPeeksTextView)
        val howManyPeeksSpinner = rootView.findViewById<Spinner>(R.id.spinner3)
        val goBackButton = rootView.findViewById<Button>(R.id.buttonGoBackFromPrep)
        val letsPlayButton = rootView.findViewById<Button>(R.id.buttonLetsPlayFinally)
        timeFormatTextView.startAnimation(animationFadeIn)
        timeFormatSpinner.startAnimation(animationFadeIn)
        goBackButton.startAnimation(animationFadeIn)
        letsPlayButton.startAnimation(animationFadeIn)
        val fragmentManager = activity?.supportFragmentManager
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val handler = Handler(Looper.getMainLooper())
        val currentGameMode = Bundle()
        val nextFragment = Game()
        nextFragment.arguments = currentGameMode
        letsPlayButton.setOnClickListener{
            val timeFormat = timeFormatSpinner.selectedItem.toString()
            val howManyPeeks = howManyPeeksSpinner.selectedItem.toString()
            currentGameMode.putString("timeFormat", timeFormat)
            currentGameMode.putString("howManyPeeks", howManyPeeks)
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)
                ?.commit()
        }
        if(whichButtonClicked != null) {
            val buttonClicked = whichButtonClicked.getString("buttonClicked")
            if(buttonClicked == "CasualGame" || buttonClicked == "HotSeat"|| buttonClicked == "BlindfoldVsNonBlindfold"){
                howManyPeeksTextView.visibility = View.VISIBLE
                howManyPeeksSpinner.visibility = View.VISIBLE
                howManyPeeksTextView.startAnimation(animationFadeIn)
                howManyPeeksSpinner.startAnimation(animationFadeIn)
            }else {
                howManyPeeksTextView.visibility = View.INVISIBLE
                howManyPeeksSpinner.visibility = View.INVISIBLE
            }
            handler.postDelayed({
                inflater.inflate(R.layout.fragment_play, container, false)
            }, 250)

            goBackButton.setOnClickListener{
                goBackButton.startAnimation(animationFadeOut)
                if(buttonClicked == "CasualGame" || buttonClicked == "HotSeat"|| buttonClicked == "BlindfoldVsNonBlindfold") {
                    howManyPeeksTextView.startAnimation(animationFadeOut)
                    howManyPeeksSpinner.startAnimation(animationFadeOut)
                }
                timeFormatTextView.startAnimation(animationFadeOut)
                timeFormatSpinner.startAnimation(animationFadeOut)
                letsPlayButton.startAnimation(animationFadeOut)
                handler.postDelayed({
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, PlayFragment())?.addToBackStack(null)
                        ?.commit()
                                    }, 250)

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
         * @return A new instance of fragment GamePrep.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GamePrep().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}