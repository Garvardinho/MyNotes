package com.garvardinho.mynotes.ui

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.garvardinho.mynotes.R
import com.garvardinho.mynotes.data.CardsSource
import com.garvardinho.mynotes.data.Note

class NoteAdapter(private val notes: CardsSource) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (notes.getCardData(position) != null)
            holder.setData(notes.getCardData(position)!!)
    }

    override fun getItemCount(): Int {
        return notes.getSize()
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick(v: View, position: Int)
        fun onLongClick(v: View, position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteTitle: TextView = itemView.findViewById(R.id.note_card_title)
        private val noteBodyPreview: TextView? = itemView.findViewById(R.id.note_body_preview)

        init {
            itemView.setOnClickListener { v ->
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(v, bindingAdapterPosition)
                }
            }

            itemView.setOnLongClickListener { v ->
                if (itemClickListener != null) {
                    itemClickListener!!.onLongClick(v, bindingAdapterPosition)
                }
                return@setOnLongClickListener true
            }
        }

        fun setData(note: Note) {
            noteTitle.text = note.title
            if (note.noteBody != null && note.noteBody!!.length > 20) {
                val noteBodyPreviewText: String = note.noteBody?.substring(0, 20) + "..."
                noteBodyPreview?.text = noteBodyPreviewText
            }
            else {
                noteBodyPreview?.text = note.noteBody
            }
        }
    }
}