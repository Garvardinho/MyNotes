package com.garvardinho.mynotes.ui

import android.view.MenuItem

object MenuObject {
    const val MainTag: String = "MAIN"
    const val LandscapeMainTag: String = "LANDSCAPE_MAIN"
    const val LongTappedTag: String = "LONG_TAPPED"
    const val NotesBodyFragmentTag: String = "NOTES_BODY"
    const val CreateNewNoteFragmentTag: String = "CREATE_NEW_NOTE"
    const val EditNoteFragmentTag: String = "EDIT_NOTE"
    lateinit var menuAdd: MenuItem
    lateinit var menuDelete: MenuItem
    lateinit var menuEdit: MenuItem
    lateinit var menuSave: MenuItem

    fun setMenuItemsVisibility(tag: String) {
        when(tag) {
            MainTag -> {
                menuAdd.isVisible = true
                menuDelete.isVisible = false
                menuEdit.isVisible = false
                menuSave.isVisible = false
            }

            LandscapeMainTag -> {
                menuAdd.isVisible = true
                menuDelete.isVisible = false
                menuEdit.isVisible = false
                menuSave.isVisible = true
            }

            LongTappedTag -> {
                menuAdd.isVisible = false
                menuDelete.isVisible = true
                menuEdit.isVisible = true
                menuSave.isVisible = false
            }

            NotesBodyFragmentTag,
            CreateNewNoteFragmentTag,
            EditNoteFragmentTag -> {
                menuAdd.isVisible = false
                menuDelete.isVisible = false
                menuEdit.isVisible = false
                menuSave.isVisible = true
            }

        }
    }
}