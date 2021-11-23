package com.garvardinho.mynotes.ui

import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.garvardinho.mynotes.MainActivity
import com.garvardinho.mynotes.R
import com.garvardinho.mynotes.data.Note
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import io.realm.kotlin.where

class EditNoteFragment : Fragment() {
    private lateinit var note: Note
    private lateinit var backgroundRealmThread: Realm
    private lateinit var noteTitleTextInputLayout: TextInputLayout
    private lateinit var noteTitleTextInput: TextInputEditText
    private lateinit var datePicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundRealmThread = MainActivity.getRealmInstance()
        note = arguments?.getParcelable(getString(R.string.current_note))!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_create_or_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noteTitleTextInputLayout = requireActivity().findViewById(R.id.note_title_input_layout)
        noteTitleTextInput = requireActivity().findViewById(R.id.note_title)
        datePicker = requireActivity().findViewById(R.id.note_date)
        noteTitleTextInput.setOnClickListener {
            noteTitleTextInputLayout.error = null
        }
        noteTitleTextInput.setText(note.title)
        datePicker.updateDate(note.year, note.month, note.day)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuObject.setMenuItemsVisibility(MenuObject.EditNoteFragmentTag)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                if (!titleIsNull()) {
                    try {
                        updateNoteInRealm(note)
                    } catch (e: RealmPrimaryKeyConstraintException) {
                        noteTitleTextInputLayout.error = "Такое название уже имеется"
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun titleIsNull(): Boolean {
        if (noteTitleTextInput.text.isNullOrEmpty()) {
            noteTitleTextInputLayout.error = "Введите название заметки"
            return true
        }
        return false
    }

    private fun updateNoteInRealm(note: Note) {
        val noteInRealm: Note = backgroundRealmThread.where<Note>()
            .equalTo("title", note.title)
            .findFirst()!!

        val noteToInsert = createNewNoteBasedOn(note)

        backgroundRealmThread.executeTransaction { transactionRealm ->
            noteInRealm.deleteFromRealm()
            transactionRealm.insert(noteToInsert)
        }

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.notes, NotesFragment())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun createNewNoteBasedOn(note: Note): Note {
        val noteTitle: String = noteTitleTextInput.text.toString()
        val noteYear: Int = datePicker.year
        val noteMonth: Int = datePicker.month
        val noteDay: Int = datePicker.dayOfMonth

        return Note(noteTitle, note.noteBody, noteYear, noteMonth, noteDay)
    }
}