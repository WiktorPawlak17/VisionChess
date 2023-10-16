package com.example.visionchess

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var avatar: ImageView
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
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val buttonGoBack = rootView.findViewById<Button>(R.id.buttonGoBackFromProfile)
        avatar = rootView.findViewById(R.id.avatar)
        val gameName = rootView.findViewById<TextView>(R.id.gameName)
        val blitzRating = rootView.findViewById<TextView>(R.id.blitzRating)
        val rapidRating = rootView.findViewById<TextView>(R.id.rapidRating)
        val logOut = rootView.findViewById<Button>(R.id.logOut)
        val fragmentManager = activity?.supportFragmentManager
        val animationFadeOut = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out_very_quick)
        val animationFadeIn = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in_very_quick)
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        /////////////////////////////////////////////
        //Random stuff added itself???
        /////////////////////////////////////////////
        val user = auth.currentUser
        val uid = user?.uid
        val userReference = database.getReference("users/$uid")
        val usernamePath = userReference.child("nickname")
        val blitzRatingReferencePath = userReference.child("ratings/0")
        val rapidRatingReferencePath = userReference.child("ratings/1")


        usernamePath.addValueEventListener(object:ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                gameName.text = getString(R.string.nickname)+ " : "+ snapshot.value.toString()
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                gameName.text = "Error"
            }

        })
        blitzRatingReferencePath.addValueEventListener(object:ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                blitzRating.text = getString(R.string.blitz)+ " : "+ snapshot.value.toString()
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                blitzRating.text = "Error"
            }

        })
        rapidRatingReferencePath.addValueEventListener(object:ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                rapidRating.text = getString(R.string.rapid)+ " : " + snapshot.value.toString()
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                rapidRating.text = "Error"
            }

        })
        val avatarReferencePath = userReference.child("picture")
        avatarReferencePath.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val picture = snapshot.value?.toString() ?: ""

                if (picture != "none") {

                    //This works but depreciated
                  //  Picasso.get().load(picture).into(avatar)
                    try {

                        val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
                        val storageRef = storage.reference
                        val avatarRef = storageRef.child("images/$uid")
                        val localFile = File.createTempFile("images", "jpg")
                        avatarRef.getFile(localFile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                            avatar.setImageBitmap(bitmap)
                        }



                        //val inputStream = context?.contentResolver?.openInputStream(uri)
                        //val bitmap = BitmapFactory.decodeStream(inputStream)
                        //avatar.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()


                    }

                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }

        })



        buttonGoBack.startAnimation(animationFadeIn)
        gameName.startAnimation(animationFadeIn)
        blitzRating.startAnimation(animationFadeIn)
        rapidRating.startAnimation(animationFadeIn)
        logOut.startAnimation(animationFadeIn)
        avatar.startAnimation(animationFadeIn)

        avatar.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)

        }


        buttonGoBack.setOnClickListener{
            buttonGoBack.startAnimation(animationFadeOut)
            gameName.startAnimation(animationFadeOut)
            blitzRating.startAnimation(animationFadeOut)
            rapidRating.startAnimation(animationFadeOut)
            logOut.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, HomeScreenFragment())?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }


        logOut.setOnClickListener {
            auth.signOut()
            buttonGoBack.startAnimation(animationFadeOut)
            gameName.startAnimation(animationFadeOut)
            blitzRating.startAnimation(animationFadeOut)
            rapidRating.startAnimation(animationFadeOut)
            logOut.startAnimation(animationFadeOut)
            handler.postDelayed({
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.addToBackStack(null)
                    ?.commit()

            }, 250)
        }
        // Inflate the layout for this fragment
        return rootView
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the selected image URI here
            val selectedImageUri = data.data
            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid
            val storage = FirebaseStorage.getInstance("gs://visionchess-928e0.appspot.com")
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/$uid")
            val uploadTask = avatarRef.putFile(selectedImageUri!!)
            uploadTask.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            //avatarReference.setValue(selectedImageUri.toString())
            val inputStream = context?.contentResolver?.openInputStream(selectedImageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            avatar.setImageBitmap(bitmap)

        }
    }


    companion object {

    }
}