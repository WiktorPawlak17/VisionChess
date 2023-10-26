package com.example.visionchess.Fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.visionchess.Person
import com.example.visionchess.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.security.MessageDigest


class RegisterFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val handler = Handler(Looper.getMainLooper())
        val rootView = inflater.inflate(R.layout.fragment_register, container, false)
        val buttonGoBack = rootView.findViewById<Button>(R.id.buttonGoBackFromRegister)
        val fragmentManager = activity?.supportFragmentManager
        val emailEditText = rootView.findViewById<EditText>(R.id.email)
        val usernameEditText = rootView.findViewById<EditText>(R.id.username)
        val passwordEditText = rootView.findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = rootView.findViewById<EditText>(R.id.passwordAgain)
        val buttonRegister = rootView.findViewById<Button>(R.id.logOut)
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        buttonGoBack.startAnimation(animationFadeIn)
        buttonRegister.startAnimation(animationFadeIn)
        emailEditText.startAnimation(animationFadeIn)
        usernameEditText.startAnimation(animationFadeIn)
        passwordEditText.startAnimation(animationFadeIn)
        confirmPasswordEditText.startAnimation(animationFadeIn)
        buttonRegister.setOnClickListener{
            val email = emailEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            if (password == confirmPassword) {
                val hashedPassword = sha256(password)
                val newUser = Person(email, username, hashedPassword, mutableListOf(1000,1000), mutableListOf(),
                    mutableListOf(), mutableListOf(), mutableListOf(), "none",
                    0, 0, 0, null, "0", "none")

                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //val currentUser = auth.currentUser
                            Toast.makeText(context, "User created successfully",
                                Toast.LENGTH_SHORT).show()
                            //currentUser?.sendEmailVerification()
                            //    ?.addOnCompleteListener { task ->
                            //        if (task.isSuccessful) {
                            //            Log.d(TAG, "Email sent.")
                            //        }
                            //    }
                            val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
                            val databaseReference = database.reference
                            val currentUser = auth.currentUser
                            if (currentUser != null) {
                                databaseReference.child("users").child(currentUser.uid).setValue(newUser)
                                    .addOnSuccessListener {
                                        // Data was successfully written
                                        Toast.makeText(
                                            context, "Data successfully added to the database.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener { e ->
                                        // There was an error writing to the database
                                        Toast.makeText(
                                            context, "Error adding data to the database: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                            buttonGoBack.startAnimation(animationFadeOut)
                            buttonRegister.startAnimation(animationFadeOut)
                            emailEditText.startAnimation(animationFadeOut)
                            usernameEditText.startAnimation(animationFadeOut)
                            passwordEditText.startAnimation(animationFadeOut)
                            confirmPasswordEditText.startAnimation(animationFadeOut)
                            handler.postDelayed({
                                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.addToBackStack(null)
                                    ?.commit()

                            }, 250)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed, Database crashed or something.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }

            }
        }





        buttonGoBack.setOnClickListener{
            buttonGoBack.startAnimation(animationFadeOut)
            buttonRegister.startAnimation(animationFadeOut)
            emailEditText.startAnimation(animationFadeOut)
            usernameEditText.startAnimation(animationFadeOut)
            passwordEditText.startAnimation(animationFadeOut)
            confirmPasswordEditText.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }



        // Inflate the layout for this fragment
        return rootView
    }

    companion object {

            }

    private fun sha256(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}