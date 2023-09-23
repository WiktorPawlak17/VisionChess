package com.example.visionchess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterReceived(private val receivedList: List<String>) : RecyclerView.Adapter<CustomAdapterReceived.ViewHolder>() {

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
        holder.buttonYes.setOnClickListener{
        TODO()
        }
        holder.buttonNo.setOnClickListener{
        TODO()
        }
    }

    override fun getItemCount(): Int {
        return receivedList.size
    }
}