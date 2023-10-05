package com.example.visionchess

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import java.io.File
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("SameParameterValue", "DEPRECATION")
class HomeScreenFragment : Fragment() {
      //  var something = find
//    override fun onCreate(savedInstanceState: Bundle?) {
//      super.onCreate(savedInstanceState)
//    }
      private lateinit var playTextView: TextView
    private val recordAudioPermission = Manifest.permission.RECORD_AUDIO
    private val requestRecordAudioPermission = 200
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

       // val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser



        ////////////////////////////////////////////////////////////////////////////////////////////////
        // This is the code the stuff fade in
        ////////////////////////////////////////////////////////////////////////////////////////////////
        val rootView = inflater.inflate(R.layout.fragment_home_screen, container, false)
        val menucirclewithbuttons = rootView.findViewById<ImageView>(R.id.menucirclewithbuttons)
         playTextView = rootView.findViewById(R.id.play_textview)
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

        val hasMicrophonePermission = context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.RECORD_AUDIO
            )
        } == PackageManager.PERMISSION_GRANTED

        if(!hasMicrophonePermission){
            requestMicrophonePermission()
        }

        // Check if the app has microphone permission


        ////////////////////////////////////////////////////////////////////////////////////////////////
        // This is the code that makes the settings file (read and write)
        ////////////////////////////////////////////////////////////////////////////////////////////////
        val fileName = "settings.json"
        val settings = context?.let { readSettingsFromFile(it,fileName) }
        if (settings == null) {
            val defaultSettings = JSONObject()
            defaultSettings.put("firstLaunch", true)
            defaultSettings.put("sayPawn", true)
            defaultSettings.put("sayTakes", true)
            defaultSettings.put("sayPromotion", true)
            defaultSettings.put("sayCheck", true)
            defaultSettings.put("sayOpponentPlayed", true)
            defaultSettings.put("language", "English")
            val jsonObject = JSONObject()
            jsonObject.put("Settings", defaultSettings)
            val file = File(context?.filesDir, fileName)
            try {
                file.bufferedWriter().use { writer ->
                    writer.write(jsonObject.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
            settingsTextView.setBackgroundColor(resources.getColor(R.color.red, null))
        } else {
            settingsTextView.setBackgroundColor(resources.getColor(R.color.transparent, null))
            when(settings.language) {
                "English" -> setLocale("en")
                "Polish" -> setLocale("pl")
            }

            playTextView.text = resources.getString(R.string.Play)
            trainingTextView.text = resources.getString(R.string.Training)
            historyTextView.text = resources.getString(R.string.History)
            settingsTextView.text = resources.getString(R.string.Settings)
            tutorialTextView.text = resources.getString(R.string.Tutorial)
            friendsTextView.text = resources.getString(R.string.Friends)
            profileTextView.text = resources.getString(R.string.Profile)
        }
            if(currentUser == null) {
                profileTextView.setBackgroundColor(resources.getColor(R.color.red, null))
            }
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

            if(currentUser != null) {
                handler.postDelayed({
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, ProfileFragment())?.addToBackStack(null)
                        ?.commit()
                }, 250)
            }else {
                handler.postDelayed({
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.addToBackStack(null)
                        ?.commit()
                }, 250)
            }

        }

        return rootView
    }
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
    private fun requestMicrophonePermission() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(recordAudioPermission),
            requestRecordAudioPermission
        )
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == requestRecordAudioPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
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