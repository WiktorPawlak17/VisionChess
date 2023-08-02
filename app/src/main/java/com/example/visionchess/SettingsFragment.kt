package com.example.visionchess

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Switch



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
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

    @SuppressLint("UseSwitchCompatOrMaterialCode", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        val pawnToSquare = rootView.findViewById<Switch>(R.id.sayPawn)
        val pawnTakes = rootView.findViewById<Switch>(R.id.sayTakes)
        val pawnPromotion = rootView.findViewById<Switch>(R.id.sayPromotion)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            inflater.inflate(R.layout.fragment_settings, container, false)
        }, 250)
        val goBackButton = rootView.findViewById<Button>(R.id.buttonGoBackFromSettings)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val fragmentManager = activity?.supportFragmentManager
        if(pawnPromotion.isChecked){
            pawnPromotion.text = getString(R.string.pawn_to_e8_promote_to_a_queen_or_other_piece)
            pawnPromotion.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
        }
        else{
            pawnPromotion.text = getString(R.string.e8_promote_to_a_queen_or_other_piece)
            pawnPromotion.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
        }
        if(pawnTakes.isChecked){
            pawnTakes.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
        }
        else{
            pawnTakes.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
        }
        if(pawnToSquare.isChecked){
            pawnToSquare.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
        }
        else{
            pawnToSquare.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
        }
        pawnToSquare.setOnClickListener{
            if(pawnToSquare.isChecked){
                pawnPromotion.text = getString(R.string.pawn_to_e8_promote_to_a_queen_or_other_piece)
                pawnToSquare.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
            }
            else{
                pawnPromotion.text = getString(R.string.e8_promote_to_a_queen_or_other_piece)
                pawnToSquare.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
            }
        }
        pawnTakes.setOnClickListener{
            if(pawnTakes.isChecked){
                pawnTakes.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
            }
            else{
                pawnTakes.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
            }
        }
        pawnPromotion.setOnClickListener{
            if(pawnPromotion.isChecked){
                pawnPromotion.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
            }
            else{
                pawnPromotion.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
            }
        }
        pawnToSquare.startAnimation(animationFadeIn)
        pawnTakes.startAnimation(animationFadeIn)
        pawnPromotion.startAnimation(animationFadeIn)
        goBackButton.setOnClickListener{
            goBackButton.startAnimation(animationFadeOut)
            pawnToSquare.startAnimation(animationFadeOut)
            pawnTakes.startAnimation(animationFadeOut)
            pawnPromotion.startAnimation(animationFadeOut)
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)?.commit()
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
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}