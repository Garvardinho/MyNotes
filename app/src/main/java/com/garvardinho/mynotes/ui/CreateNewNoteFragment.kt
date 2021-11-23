package com.garvardinho.mynotes.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.garvardinho.mynotes.MainActivity
import com.garvardinho.mynotes.data.Note
import com.garvardinho.mynotes.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException

class CreateNewNoteFragment : Fragment() {

    private lateinit var backgroundRealmThread: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundRealmThread = MainActivity.getRealmInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_create_or_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        val noteTitleTextInputLayout: TextInputLayout =
            requireActivity().findViewById(R.id.note_title_input_layout)
        val noteTitleTextInput: TextInputEditText = requireActivity().findViewById(R.id.note_title)

        toolbar.title = getString(R.string.create_note_title)
        noteTitleTextInput.setOnClickListener {
            noteTitleTextInputLayout.error = null
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuObject.setMenuItemsVisibility(MenuObject.CreateNewNoteFragmentTag)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                if (!titleIsNull()) {
                    val note: Note = createNewNote()
                    try {
                        createNoteInRealm(note)
                    } catch (e: RealmPrimaryKeyConstraintException) {
                        val noteTitleTextInputLayout: TextInputLayout =
                            requireActivity().findViewById(R.id.note_title_input_layout)
                        noteTitleTextInputLayout.error = "Такое название уже имеется"
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun titleIsNull(): Boolean {
        val noteTitleTextInputLayout: TextInputLayout =
            requireActivity().findViewById(R.id.note_title_input_layout)
        val noteTitleTextInput: TextInputEditText = requireActivity().findViewById(R.id.note_title)

        if (noteTitleTextInput.text.isNullOrEmpty()) {
            noteTitleTextInputLayout.error = "Введите название заметки"
            return true
        }

        return false
    }

    private fun createNoteInRealm(note: Note) {
        backgroundRealmThread.executeTransaction { transactionRealm ->
            transactionRealm.insert(note)
        }
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.notes, NotesBodyFragment.newInstance(getString(R.string.current_note), note))
            .addToBackStack(null)
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun createNewNote(): Note {
        val noteTitleTextInput: TextInputEditText = requireActivity().findViewById(R.id.note_title)
        val noteDatePicker: DatePicker = requireActivity().findViewById(R.id.note_date)
        val noteTitle: String = noteTitleTextInput.text.toString()
        val noteYear: Int = noteDatePicker.year
        val noteMonth: Int = noteDatePicker.month
        val noteDay: Int = noteDatePicker.dayOfMonth

        return Note(noteTitle, null, noteYear, noteMonth, noteDay)
    }
}