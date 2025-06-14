package com.example.addnotes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button

    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonSave = findViewById(R.id.buttonSave)
        buttonDelete = findViewById(R.id.buttonDelete)

        // Retrieve note details from intent extras
        noteId = intent.getStringExtra("noteId")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        // If note exists, populate fields with existing data
        if (title != null && description != null) {
            editTextTitle.setText(title)
            editTextDescription.setText(description)
        }

        // Set up the save button
        buttonSave.setOnClickListener {
            val newTitle = editTextTitle.text.toString()
            val newDescription = editTextDescription.text.toString()
            if (newTitle.isEmpty() || newDescription.isEmpty()) {
                Toast.makeText(this, "Title and Note cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                saveOrUpdateNote(newTitle, newDescription)
            }
        }

        // Set up the delete button
        buttonDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun saveOrUpdateNote(title: String, description: String) {
        val note = Note(
            id = noteId ?: FirebaseDatabase.getInstance().getReference("notes").push().key ?: "",
            title = title,
            description = description
        )

        if (noteId != null) {
            // Update existing note
            FirebaseDatabase.getInstance().getReference("notes").child(noteId!!).setValue(note)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Save new note
            FirebaseDatabase.getInstance().getReference("notes").child(note.id).setValue(note)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        finish()
    }

    private fun deleteNote() {
        noteId?.let {
            FirebaseDatabase.getInstance().getReference("notes").child(it).removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
                    }
                }
            finish()
        } ?: Toast.makeText(this, "No note to delete", Toast.LENGTH_SHORT).show()
    }
}
