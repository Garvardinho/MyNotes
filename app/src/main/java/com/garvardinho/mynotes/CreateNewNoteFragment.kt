package com.garvardinho.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.android.material.button.MaterialButton
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
        return inflater.inflate(R.layout.fragment_create_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val createNewNoteButton: MaterialButton =
            view.findViewById(R.id.create_new_note_button)
        val noteTitleTextInputLayout: TextInputLayout = requireActivity().findViewById(R.id.note_title_input_layout)
        val noteTitleTextInput: TextInputEditText = requireActivity().findViewById(R.id.note_title)
        noteTitleTextInput.setOnClickListener {
            noteTitleTextInputLayout.error = null
        }

        createNewNoteButton.setOnClickListener {
            val note: Note = createNewNote()

            if (noteTitleTextInput.text.isNullOrEmpty()) {
                noteTitleTextInputLayout.error = "Введите название заметки"
                return@setOnClickListener
            }

            try {
                backgroundRealmThread.executeTransaction { transactionRealm ->
                    transactionRealm.insert(note)
                }
                requireActivity().supportFragmentManager.popBackStack()
            } catch (e: RealmPrimaryKeyConstraintException) {
                noteTitleTextInputLayout.error = "Такое название уже имеется"
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createNewNote(): Note {
        val noteTitleTextInput: TextInputEditText = requireActivity().findViewById(R.id.note_title)
        val noteDatePicker: DatePicker = requireActivity().findViewById(R.id.note_date)
        val noteTitle: String = noteTitleTextInput.text.toString()
        val noteYear: Int = noteDatePicker.year
        val noteMonth: Int = noteDatePicker.month
        val noteDay: Int = noteDatePicker.dayOfMonth

        return Note(noteTitle,null, noteYear, noteMonth, noteDay)
    }
}