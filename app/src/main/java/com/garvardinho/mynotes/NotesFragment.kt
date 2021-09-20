package com.garvardinho.mynotes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE

class NotesFragment : Fragment() {

    private var isLandscape: Boolean = false
    private var currentNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentNote = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(getString(R.string.current_note))
        } else {
            Note(
                resources.getStringArray(R.array.titles)[0],
                resources.getStringArray(R.array.note_bodies)[0],
                resources.getIntArray(R.array.years)[0],
                resources.getIntArray(R.array.months)[0],
                resources.getIntArray(R.array.days)[0])
        }

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            showNotesBody(currentNote)
        }

        initList(view)
    }

    private fun showNotesBody(note: Note?) {
        when (isLandscape) {
            true -> showLandNotesBody(note)
            false -> showPortNotesBody(note)
        }
    }

    private fun showLandNotesBody(note: Note?) {
        val detail: NotesBodyFragment = NotesBodyFragment.newInstance(getString(R.string.current_note), note)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction
            .replace(R.id.note_body, detail)
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun initList(view: View) {
        val layoutView: LinearLayout = view as LinearLayout
        val noteTitles: Array<String> = resources.getStringArray(R.array.titles)

        for (i in noteTitles.indices) {
            val noteTitle: String = noteTitles[i]
            val textView = TextView(context)
            textView.text = noteTitle
            textView.textSize = 30.toFloat()
            layoutView.addView(textView)
            textView.setOnClickListener {
                currentNote = Note(
                    resources.getStringArray(R.array.titles)[i],
                    resources.getStringArray(R.array.note_bodies)[i],
                    resources.getIntArray(R.array.years)[i],
                    resources.getIntArray(R.array.months)[i],
                    resources.getIntArray(R.array.days)[i])
                showNotesBody(currentNote)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(getString(R.string.current_note), currentNote)
        super.onSaveInstanceState(outState)
    }

    private fun showPortNotesBody(note: Note?) {
        val intent = Intent(activity, NotesBodyActivity::class.java)
        intent.putExtra(getString(R.string.current_note), note)
        startActivity(intent)
    }
}