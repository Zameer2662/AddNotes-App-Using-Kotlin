package com.example.addnotes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: MutableList<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var database: DatabaseReference
    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        val fabAddNote: FloatingActionButton = findViewById(R.id.fabAddNote)
        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)

        // Initialize note list and adapter
        noteList = mutableListOf()
        noteAdapter = NoteAdapter(noteList, this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = noteAdapter

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("notes")

        // Handle FAB click to add a new note
        fabAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
        }

        // Fetch notes from the database
        fetchNotes()

        // Handle search button click
        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString().trim()
            searchNotes(query)
        }
    }

    // Fetch notes from Firebase database
    private fun fetchNotes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        noteList.add(note)
                    }
                }
                noteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load notes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Search notes based on the query
    private fun searchNotes(query: String) {
        val filteredList = noteList.filter {
            it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
        noteAdapter.updateList(filteredList)
    }

    // Handle item click in the RecyclerView
    override fun onItemClick(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java).apply {
            putExtra("noteId", note.id)
            putExtra("title", note.title)
            putExtra("description", note.description)
        }
        startActivity(intent)
    }
}
