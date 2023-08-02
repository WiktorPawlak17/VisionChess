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
import android.widget.ImageView
import android.widget.TextView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreenFragment : Fragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//      super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // This is the code the stuff fade in
        ////////////////////////////////////////////////////////////////////////////////////////////////
        val rootView = inflater.inflate(R.layout.fragment_home_screen, container, false)

        val menucirclewithbuttons = rootView.findViewById<ImageView>(R.id.menucirclewithbuttons)
        val playTextView = rootView.findViewById<TextView>(R.id.play_textview)
        val trainingTextView = rootView.findViewById<TextView>(R.id.training_textview)
        val historyTextView = rootView.findViewById<TextView>(R.id.history_textview)
        val settingsTextView = rootView.findViewById<TextView>(R.id.settings_textview)
        val tutorialTextView = rootView.findViewById<TextView>(R.id.tutorial_textview)
        val friendsTextView = rootView.findViewById<TextView>(R.id.friends_textview)
        val profileTextView = rootView.findViewById<TextView>(R.id.profile_textview)
        if(isFirstLaunch){
            val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_quick)
            isFirstLaunch = false
            menucirclewithbuttons.startAnimation(animationFadeIn)
            playTextView.startAnimation(animationFadeIn)
            trainingTextView.startAnimation(animationFadeIn)
            historyTextView.startAnimation(animationFadeIn)
            settingsTextView.startAnimation(animationFadeIn)
            tutorialTextView.startAnimation(animationFadeIn)
            friendsTextView.startAnimation(animationFadeIn)
            profileTextView.startAnimation(animationFadeIn)
        } else {
            val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
            menucirclewithbuttons.startAnimation(animationFadeIn)
            playTextView.startAnimation(animationFadeIn)
            trainingTextView.startAnimation(animationFadeIn)
            historyTextView.startAnimation(animationFadeIn)
            settingsTextView.startAnimation(animationFadeIn)
            tutorialTextView.startAnimation(animationFadeIn)
            friendsTextView.startAnimation(animationFadeIn)
            profileTextView.startAnimation(animationFadeIn)
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // This is the code that makes the buttons fade out and then go to the next fragment
        ////////////////////////////////////////////////////////////////////////////////////////////////
        val playButton = rootView.findViewById<Button>(R.id.play_Button)
        val trainingButton = rootView.findViewById<Button>(R.id.training_Button)
        val historyButton = rootView.findViewById<Button>(R.id.history_Button)
        val settingsButton = rootView.findViewById<Button>(R.id.settings_Button)
        val tutorialButton = rootView.findViewById<Button>(R.id.tutorial_Button)
        val friendsButton = rootView.findViewById<Button>(R.id.friends_Button)
        val profileButton = rootView.findViewById<Button>(R.id.profile_Button)
        val fragmentManager = activity?.supportFragmentManager
        val handler = Handler(Looper.getMainLooper())
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        playButton.setOnClickListener{
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, PlayFragment())?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }
        trainingButton.setOnClickListener {
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, TrainingFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)

        }
        historyButton.setOnClickListener{
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HistoryFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        settingsButton.setOnClickListener{
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, SettingsFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        tutorialButton.setOnClickListener{
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, TutorialFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        friendsButton.setOnClickListener{
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, FriendsFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }
        profileButton.setOnClickListener{
            menucirclewithbuttons.startAnimation(animationFadeOut)
            playTextView.startAnimation(animationFadeOut)
            trainingTextView.startAnimation(animationFadeOut)
            historyTextView.startAnimation(animationFadeOut)
            settingsTextView.startAnimation(animationFadeOut)
            tutorialTextView.startAnimation(animationFadeOut)
            friendsTextView.startAnimation(animationFadeOut)
            profileTextView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, ProfileFragment())?.addToBackStack(null)
                    ?.commit()
            }, 250)
        }

        return rootView
    }


    override fun onDestroy() {
        super.onDestroy()
        val handler = Handler(Looper.getMainLooper())
        handler.removeCallbacksAndMessages(null)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *


         * @return A new instance of fragment HomeScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
        private var isFirstLaunch = true
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}