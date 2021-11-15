package com.garvardinho.mynotes.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.garvardinho.mynotes.MainActivity
import com.garvardinho.mynotes.data.Note
import com.garvardinho.mynotes.R
import com.garvardinho.mynotes.data.CardsSource
import com.garvardinho.mynotes.data.CardsSourceAdapter
import io.realm.Realm
import io.realm.kotlin.where

class NotesFragment : Fragment() {

    private var isLandscape: Boolean = false
    private var currentNote: Note? = null
    private lateinit var backgroundThreadRealm: Realm
    private lateinit var recyclerView: RecyclerView
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundThreadRealm = MainActivity.getRealmInstance()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_notes, container, false)
        recyclerView = view.findViewById(R.id.note_titles)
        initList()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        initMenu(true)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun showNotesBody(note: Note?) {
        when (isLandscape) {
            true -> showLandNotesBody(note)
            false -> showPortNotesBody(note)
        }
    }

    private fun initList() {
        val data: CardsSource = CardsSourceAdapter(backgroundThreadRealm)
        val layoutManager = LinearLayoutManager(context)
        val adapter = NoteAdapter(data)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object: NoteAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                currentNote = data.getCardData(position)
                showNotesBody(currentNote)
            }

            override fun onLongClick(v: View, position: Int) {
                currentNote = data.getCardData(position)
                initMenu(false)
            }
        })
    }

    private fun initMenu(standard: Boolean) {
        val menuAdd: MenuItem = menu.findItem(R.id.menu_add)
        val menuEdit: MenuItem = menu.findItem(R.id.menu_edit)
        val menuDelete: MenuItem = menu.findItem(R.id.menu_delete)

        menuAdd.isVisible = standard
        menuEdit.isVisible = !standard
        menuDelete.isVisible = !standard
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                val noteToDelete = backgroundThreadRealm
                    .where<Note>()
                    .equalTo("title", currentNote?.title)
                    .findFirst()

                backgroundThreadRealm.executeTransaction {
                    noteToDelete?.deleteFromRealm()
                }

                initList()
                initMenu(true)
            }

            R.id.menu_edit -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.notes, EditNoteFragment(currentNote!!))
                    .setTransition(TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
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