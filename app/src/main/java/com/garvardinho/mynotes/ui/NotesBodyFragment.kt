package com.garvardinho.mynotes.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.garvardinho.mynotes.MainActivity
import com.garvardinho.mynotes.data.Note
import com.garvardinho.mynotes.R
import com.google.android.material.textfield.TextInputEditText
import io.realm.Realm
import io.realm.kotlin.where

class NotesBodyFragment : Fragment() {

    private var currentNote: Note? = null

    companion object {
        fun newInstance(tag: String, currentNote: Note?): NotesBodyFragment {
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
        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.toolbar)

        if (container?.id == R.id.notes &&
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().supportFragmentManager.popBackStack()
        }

        toolbar.title = currentNote?.title
        noteBody.setText(currentNote?.noteBody ?: "")
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MenuObject.setMenuItemsVisibility(MenuObject.LandscapeMainTag)
        } else {
            MenuObject.setMenuItemsVisibility(MenuObject.NotesBodyFragmentTag)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                val backgroundRealmThread: Realm = MainActivity.getRealmInstance()
                backgroundRealmThread.executeTransaction {
                    val noteBody: TextInputEditText = requireActivity().findViewById(R.id.note_body)
                    val currentNoteInDatabase =
                        backgroundRealmThread.where<Note>()
                            .equalTo("title", currentNote?.title)
                            .findFirst()!!
                    currentNoteInDatabase.noteBody = noteBody.text.toString()
                    currentNote?.noteBody = noteBody.text.toString()
                }

                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.notes, NotesFragment())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}