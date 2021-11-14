package com.garvardinho.mynotes.ui

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.garvardinho.mynotes.Note
import com.garvardinho.mynotes.R
import io.realm.RealmResults

class NoteAdapter(private val notes: RealmResults<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.text = notes[position]!!.title
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView as TextView

        init {
            noteTitle.setOnClickListener { v ->
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(v, bindingAdapterPosition)
                }
            }
        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }
}