package com.garvardinho.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import io.realm.Realm

class CreateNewNoteActivity : AppCompatActivity() {

    private lateinit var backgroundRealmThread: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_note)

        backgroundRealmThread = RealmInstance.getInstance(this)

        val createNewNoteButton: MaterialButton = findViewById(R.id.create_new_note_button)
        createNewNoteButton.setOnClickListener {
            val note: Note = createNewNote()
            backgroundRealmThread.executeTransaction { transactionRealm ->
                transactionRealm.insert(note)
            }
            finish()
        }
    }

    private fun createNewNote(): Note {
        val noteTitleTextInput: TextInputEditText = findViewById(R.id.note_title)
        val noteDatePicker: DatePicker = findViewById(R.id.note_date)
        val noteTitle: String = noteTitleTextInput.text.toString()
        val noteYear: Int = noteDatePicker.year
        val noteMonth: Int = noteDatePicker.month
        val noteDay: Int = noteDatePicker.dayOfMonth

        return Note(noteTitle,null, noteYear, noteMonth, noteDay)
    }
}