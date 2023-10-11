package com.example.visionchess


import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView


class TutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val fragmentManager = activity?.supportFragmentManager
        val handler = Handler(Looper.getMainLooper())
        val rootView = inflater.inflate(R.layout.fragment_tutorial, container, false)
        val tutorialVideoView = rootView.findViewById<VideoView>(R.id.videoViewTutorialPlayer)
        val goBackButton = rootView.findViewById<Button>(R.id.buttonGoBackFromTutorial)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val mediaController = MediaController(this.context)
        val tv = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.sample_video)
        tutorialVideoView.startAnimation(animationFadeIn)
        goBackButton.startAnimation(animationFadeIn)
        handler.postDelayed({
            inflater.inflate(R.layout.fragment_tutorial, container, false)
            tutorialVideoView.setMediaController(mediaController)
            tutorialVideoView.setVideoURI(tv)
            tutorialVideoView.start()
        }, 250)
        goBackButton.setOnClickListener{
            goBackButton.startAnimation(animationFadeOut)
            tutorialVideoView.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)?.commit()
            }, 250)
        }
        tutorialVideoView.setOnCompletionListener {
            tutorialVideoView.startAnimation(animationFadeOut)
            goBackButton.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)?.commit()
            }, 250)
        }
        // Inflate the layout for this fragment
        return rootView
    }

    companion object {

    }
}