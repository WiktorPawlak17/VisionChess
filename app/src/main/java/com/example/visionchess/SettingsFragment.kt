package com.example.visionchess

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.io.File
import java.util.Locale


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("SameParameterValue")
class SettingsFragment : Fragment() {

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
        val sayCheck = rootView.findViewById<Switch>(R.id.sayCheck)
        val sayOpponentPlayed = rootView.findViewById<Switch>(R.id.sayOpponentPlayed)
        val languagesText = rootView.findViewById<TextView>(R.id.languagesText)
        val languageSpinner = rootView.findViewById<Spinner>(R.id.languagesSpinner)
        val handler = Handler(Looper.getMainLooper())
        var firstRunOfRunnable = true
        handler.postDelayed({
            inflater.inflate(R.layout.fragment_settings, container, false)
        }, 250)
        val runnable = object : Runnable {
            override fun run() {
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
                if(sayCheck.isChecked){
                    sayCheck.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
                }
                else{
                    sayCheck.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
                }
                if(sayOpponentPlayed.isChecked){
                    sayOpponentPlayed.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
                }
                else{
                    sayOpponentPlayed.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
                }
                if(firstRunOfRunnable){
                    handler.postDelayed(this, 1)
                    firstRunOfRunnable = false
                }
                handler.postDelayed(this, 100)
            }
        }
        runnable.run()
        val goBackButton = rootView.findViewById<Button>(R.id.buttonGoBackFromSettings)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val fragmentManager = activity?.supportFragmentManager


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
        sayCheck.setOnClickListener{
            if(sayCheck.isChecked){
                sayCheck.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
            }
            else{
                sayCheck.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
            }
        }
        sayOpponentPlayed.setOnClickListener{
            if(sayOpponentPlayed.isChecked){
                sayOpponentPlayed.thumbDrawable = resources.getDrawable(R.drawable.switchon, null)
            }
            else{
                sayOpponentPlayed.thumbDrawable = resources.getDrawable(R.drawable.switchoff, null)
            }
        }
        pawnToSquare.startAnimation(animationFadeIn)
        pawnTakes.startAnimation(animationFadeIn)
        pawnPromotion.startAnimation(animationFadeIn)
        sayCheck.startAnimation(animationFadeIn)
        sayOpponentPlayed.startAnimation(animationFadeIn)
        goBackButton.startAnimation(animationFadeIn)
        languagesText.startAnimation(animationFadeIn)
        languageSpinner.startAnimation(animationFadeIn)
        val fileName = "settings.json"
        val settings = context?.let { readSettingsFromFile(it,fileName) }
        sayCheck.isChecked = settings?.sayCheck!!
        sayOpponentPlayed.isChecked = settings.sayOpponentPlayed
        pawnPromotion.isChecked = settings.sayPromotion
        pawnTakes.isChecked = settings.sayTakes
        pawnToSquare.isChecked = settings.sayPawn
        val position = when(settings.language){
            "English" -> 0
            "Polish" -> 1
            else -> 0
        }
        languageSpinner.setSelection(position)
        languageSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                pos: Int,
                id: Long,
            ) {
                when (parent.getItemAtPosition(pos).toString()){
                    "English" -> setLocale("en")
                    "Polish" -> setLocale("pl")
                }
                pawnToSquare.text = getString(R.string.saying_pawn_when_moving_pawn)
                pawnTakes.text = getString(R.string.saying_takes_when_taking_a_piece)
                sayCheck.text = getString(R.string.say_check_when_king_is_in_check)
                sayOpponentPlayed.text = getString(R.string.say_opponent_played)
                languagesText.text = getString(R.string.language)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }

        }

        goBackButton.setOnClickListener{
            goBackButton.startAnimation(animationFadeOut)
            pawnToSquare.startAnimation(animationFadeOut)
            pawnTakes.startAnimation(animationFadeOut)
            pawnPromotion.startAnimation(animationFadeOut)
            sayCheck.startAnimation(animationFadeOut)
            sayOpponentPlayed.startAnimation(animationFadeOut)
            val settingsToSave = JSONObject()
            settingsToSave.put("firstLaunch", false)
            settingsToSave.put("sayPawn", pawnToSquare.isChecked)
            settingsToSave.put("sayTakes", pawnTakes.isChecked)
            settingsToSave.put("sayPromotion", pawnPromotion.isChecked)
            settingsToSave.put("sayCheck", sayCheck.isChecked)
            settingsToSave.put("sayOpponentPlayed", sayOpponentPlayed.isChecked)
            settingsToSave.put("language", languageSpinner.selectedItem.toString())
            val jsonObject = JSONObject()
            jsonObject.put("Settings", settingsToSave)
            val file = File(context?.filesDir, fileName)
            file.delete()
            try {
                file.bufferedWriter().use { writer ->
                    writer.write(jsonObject.toString())

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)?.commit()




        }






        // Inflate the layout for this fragment
        return rootView
    }

    @Suppress("DEPRECATION")
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun readSettingsFromFile(context: Context, fileName: String): Settings? {
        try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                return null
            }

            val jsonString = file.readText()
            val jsonObject = JSONObject(jsonString)
            val settingsJson = jsonObject.getJSONObject("Settings")

            return Settings(
                firstLaunch = settingsJson.getBoolean("firstLaunch"),
                sayPawn = settingsJson.getBoolean("sayPawn"),
                sayTakes = settingsJson.getBoolean("sayTakes"),
                sayPromotion = settingsJson.getBoolean("sayPromotion"),
                sayCheck = settingsJson.getBoolean("sayCheck"),
                sayOpponentPlayed = settingsJson.getBoolean("sayOpponentPlayed"),
                language = settingsJson.getString("language")
            )
        } catch (e: Exception) {
            e.printStackTrace() // Print the error stack trace for debugging
            return null
        }
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