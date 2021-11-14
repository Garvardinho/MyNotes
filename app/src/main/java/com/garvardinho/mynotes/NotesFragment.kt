package com.garvardinho.mynotes

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class NotesFragment : Fragment() {

    private var isLandscape: Boolean = false
    private var currentNote: Note? = null
    private lateinit var noteTitles: LinearLayout
    private lateinit var backgroundThreadRealm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundThreadRealm = MainActivity.getRealmInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteTitles = view.findViewById(R.id.note_titles)
        currentNote = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(getString(R.string.current_note))
        } else {
            if (backgroundThreadRealm.where<Note>().count() == 0L) null
            else backgroundThreadRealm.where<Note>().findFirst()
        }

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            showNotesBody(currentNote)
        }
    }

    override fun onResume() {
        super.onResume()
        initList()
    }

    private fun showNotesBody(note: Note?) {
        when (isLandscape) {
            true -> showLandNotesBody(note)
            false -> showPortNotesBody(note)
        }
    }

    private fun initList() {
        val noteList: RealmResults<Note> = backgroundThreadRealm.where<Note>().findAll()

        noteTitles.removeAllViews()
        for (note in noteList) {
            val textView = TextView(requireContext())
            textView.text = note.title
            textView.textSize = 30.toFloat()
            noteTitles.addView(textView)
            textView.setOnClickListener {
                currentNote = note
                showNotesBody(note)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(getString(R.string.current_note), currentNote)
        super.onSaveInstanceState(outState)
    }

    private fun showLandNotesBody(note: Note?) {
        val detail: NotesBodyFragment =
            NotesBodyFragment.newInstance(getString(R.string.current_note), note)

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.note_body, detail)
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    private fun showPortNotesBody(note: Note?) {
        val detail: NotesBodyFragment =
            NotesBodyFragment.newInstance(getString(R.string.current_note), note)

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.notes, detail)
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}