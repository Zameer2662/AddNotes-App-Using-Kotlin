package com.example.addnotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var noteList: List<Note>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Interface to handle item clicks
    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Inflate the item layout for each note
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Bind the note data to the ViewHolder
        val note = noteList[position]
        holder.bind(note)
        // Set click listener for the item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(note)
        }
    }

    override fun getItemCount(): Int = noteList.size

    // Inner class to hold the views for each note item
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)

        // Bind the note data to the TextViews
        fun bind(note: Note) {
            titleTextView.text = note.title
            descriptionTextView.text = note.description
        }
    }

    // Update the list of notes and notify the adapter
    fun updateList(newList: List<Note>) {
        noteList = newList
        notifyDataSetChanged()
    }
}

