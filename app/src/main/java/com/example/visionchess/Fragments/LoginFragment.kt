package com.example.visionchess.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.visionchess.R
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {


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
                        fragmentManager?.beginTransaction()?.replace(
                            R.id.fragmentContainerView,
                            ProfileFragment()
                        )?.addToBackStack(null)
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
                fragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentContainerView,
                    RegisterFragment()
                )?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }
        buttonGoBack.setOnClickListener{
            buttonLogin.startAnimation(animationFadeOut)
            buttonRegister.startAnimation(animationFadeOut)
            buttonGoBack.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentContainerView,
                    HomeScreenFragment()
                )?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }
        // Inflate the layout for this fragment
        return rootView
    }



}