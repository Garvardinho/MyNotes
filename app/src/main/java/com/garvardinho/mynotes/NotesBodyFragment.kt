package com.garvardinho.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText

class NotesBodyFragment : Fragment() {

    private var currentNote: Note? = null

    companion object {
        fun newInstance(tag: String, currentNote: Note?) : NotesBodyFragment {
            val instance = NotesBodyFragment()
            val args = Bundle()

            args.putParcelable(tag, currentNote)
            instance.arguments = args

            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            currentNote = (arguments as Bundle).getParcelable(getString(R.string.current_note))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_notes_body, container, false)
        val noteBody: TextInputEditText = view.findViewById(R.id.note_body)

        noteBody.setText(currentNote?.noteBody ?: "")
        return view
    }

}