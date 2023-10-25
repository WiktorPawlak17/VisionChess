package com.example.visionchess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomAdapterReceived(private val receivedList: ArrayList<String>, val listener: OnYesListener) : RecyclerView.Adapter<CustomAdapterReceived.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameOfFriend)
        val buttonYes: Button = itemView.findViewById(R.id.button_yes)
        val buttonNo :Button = itemView.findViewById(R.id.button_no)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_rec_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val receivedItemName = receivedList[position]
        holder.nameTextView.text = receivedItemName
        val firebaseDatabase = FirebaseDatabase.getInstance("https://visionchess-928e0-default-rtdb.europe-west1.firebasedatabase.app/")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val currentUserId = currentUser?.uid
        val databaseReference = firebaseDatabase.reference

        holder.buttonYes.setOnClickListener{

            val currentDatabaseReference = databaseReference.child("users/$currentUserId/friends")
            currentDatabaseReference.push().setValue(receivedItemName)
            val receivedDatabaseReference = databaseReference.child("users/$currentUserId/friendRequestsReceived")
            val query = databaseReference.child("users").orderByChild("nickname").equalTo(receivedItemName)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (user in snapshot.children) {
                        val userId = user.key

                        val currentUserNicknamePath = databaseReference.child("users/${currentUser?.uid}/nickname")
                        currentUserNicknamePath.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val currentUserNickname = snapshot.value.toString()
                                    databaseReference.child("users/$userId/friends")
                                        .push().setValue(currentUserNickname)
                                    val query2 =
                                        databaseReference.child("users/$userId/friendRequestsSent")
                                            .orderByValue().equalTo(currentUserNickname)

                                    query2.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            for (ds in snapshot.children) {
                                                val key = ds.key
                                                val sentDatabaseReference =
                                                    databaseReference.child("users/$userId/friendRequestsSent")
                                                sentDatabaseReference.child(key!!).removeValue()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            //Log.w("TAG", "loadPost:onCancelled", error.toException())
                                        }
                                    })
                                    val query3 = databaseReference.child("users/$currentUserId/friendRequestsReceived")
                                        .orderByValue().equalTo(receivedItemName)
                                    query3.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            for (ds in snapshot.children) {
                                                val key = ds.key
                                                receivedDatabaseReference.child(key!!).removeValue()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            //Log.w("TAG", "loadPost:onCancelled", error.toException())
                                        }
                                    })


                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
            receivedList.removeAt(position)
            listener.yesClicked()
            notifyDataSetChanged()
        }
        holder.buttonNo.setOnClickListener{
            val receivedDatabaseReference = databaseReference.child("$currentUserId/friendRequestsReceived")
            receivedDatabaseReference.child(receivedItemName).removeValue()
            val query = databaseReference.orderByChild("nickname").equalTo(receivedItemName)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val key = ds.key
                        val sentDatabaseReference = databaseReference.child("$key/friendRequestsSent")
                        sentDatabaseReference.child(receivedItemName).removeValue()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    //Log.w("TAG", "loadPost:onCancelled", error.toException())
                }
            })
            receivedList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return receivedList.size
    }
}
interface OnYesListener{
    fun yesClicked()
}