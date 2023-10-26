package com.example.visionchess.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.visionchess.R

/**
 * A simple [Fragment] subclass.
 * Use the [PlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayFragment : Fragment() {



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
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //Arguments through the bundle to the next fragment
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        val whichButtonClicked = Bundle()
        val nextFragment = GamePrepFragment()
        nextFragment.arguments = whichButtonClicked
        buttonLastPlayed.setOnClickListener{
        whichButtonClicked.putString("buttonClicked","LastPlayed")
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)?.commit()
            }, 250)
        }
        buttonRankedGame.setOnClickListener{
            whichButtonClicked.putString("buttonClicked","RankedGame")
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)?.commit()
            }, 250)
        }
        buttonCasualGame.setOnClickListener{
            whichButtonClicked.putString("buttonClicked","CasualGame")
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)?.commit()
            }, 250)
        }
        buttonHotSeat.setOnClickListener{
            whichButtonClicked.putString("buttonClicked","HotSeat")
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)?.commit()
            }, 250)
        }
        buttonNonBlindfoldVsBlindfold.setOnClickListener{
            whichButtonClicked.putString("buttonClicked","NonBlindfoldVsBlindfold")
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)?.commit()
            }, 250)
        }
        buttonBlindfoldVsNonBlindfold.setOnClickListener{
            whichButtonClicked.putString("buttonClicked","BlindfoldVsNonBlindfold")
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, nextFragment)?.addToBackStack(null)?.commit()
            }, 250)
        }


        goBackButton.setOnClickListener {
            goBackButton.startAnimation(animationFadeOut)
            buttonLastPlayed.startAnimation(animationFadeOut)
            buttonRankedGame.startAnimation(animationFadeOut)
            buttonCasualGame.startAnimation(animationFadeOut)
            buttonHotSeat.startAnimation(animationFadeOut)
            buttonNonBlindfoldVsBlindfold.startAnimation(animationFadeOut)
            buttonBlindfoldVsNonBlindfold.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentContainerView,
                    HomeScreenFragment()
                )?.addToBackStack(null)?.commit()
            }, 250)


        }
        return rootView
    }



    companion object {

    }
}