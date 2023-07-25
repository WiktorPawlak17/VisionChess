package com.example.visionchess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_screen, container, false)
        val menucirclewithbuttons = rootView.findViewById<ImageView>(R.id.menucirclewithbuttons)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_quick)
        menucirclewithbuttons.startAnimation(animationFadeIn)

        /*
        //This is here for me to have the code for the future
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        handler.postDelayed({
            val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
            menucirclewithbuttons.startAnimation(animationFadeIn)
        }, 0)
         */
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        handler.removeCallbacksAndMessages(null)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
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