package com.example.visionchess

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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.security.MessageDigest


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("NAME_SHADOWING")
class RegisterFragment : Fragment() {
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
                //class Person(val email:String ,val nickname: String, val name:String,val surname:String,val ratings: MutableList<Int>,
                //             val games: MutableList<ChessGame>,val wins: Int, val losses: Int, val draws: Int, val lastGame: ChessGame,
                //             val lastGameTime:String, val lastGameType:String){
                val newUser = Person(email, username, hashedPassword, "", mutableListOf(), mutableListOf(),
                    0, 0, 0, null, "0", "none")

                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            Toast.makeText(context, "User created successfully",
                                Toast.LENGTH_SHORT).show()
                            currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "Email sent.")
                                    }
                                }
                            val database = FirebaseDatabase.getInstance()
                            val databaseReference = database.reference.child("users")
                            databaseReference.child("users").setValue(newUser)
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
                            databaseReference.child(username).setValue(newUser)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun sha256(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}