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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TutorialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TutorialFragment : Fragment() {
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TutorialFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TutorialFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}