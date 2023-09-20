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
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        val buttonLogin = rootView.findViewById<Button>(R.id.login)
        val buttonRegister = rootView.findViewById<Button>(R.id.logOut)
        val buttonGoBack = rootView.findViewById<Button>(R.id.buttonGoBackFromLogin)
        val emailEditText = rootView.findViewById<EditText>(R.id.email)
        val passwordEditText = rootView.findViewById<EditText>(R.id.password)
        val fragmentManager = activity?.supportFragmentManager
        val handler = Handler(Looper.getMainLooper())
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        buttonLogin.startAnimation(animationFadeIn)
        buttonRegister.startAnimation(animationFadeIn)
        buttonGoBack.startAnimation(animationFadeIn)
        emailEditText.startAnimation(animationFadeIn)
        passwordEditText.startAnimation(animationFadeIn)

        val auth = FirebaseAuth.getInstance()
        buttonLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        handler.postDelayed({
                            buttonLogin.startAnimation(animationFadeOut)
                            buttonRegister.startAnimation(animationFadeOut)
                            buttonGoBack.startAnimation(animationFadeOut)
                            emailEditText.startAnimation(animationFadeOut)
                            passwordEditText.startAnimation(animationFadeOut)
                        }, 250)
                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, ProfileFragment())?.addToBackStack(null)
                            ?.commit()
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }



        buttonRegister.setOnClickListener{
            buttonLogin.startAnimation(animationFadeOut)
            buttonRegister.startAnimation(animationFadeOut)
            buttonGoBack.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, RegisterFragment())?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }
        buttonGoBack.setOnClickListener{
            buttonLogin.startAnimation(animationFadeOut)
            buttonRegister.startAnimation(animationFadeOut)
            buttonGoBack.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)
                    ?.commit()

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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}