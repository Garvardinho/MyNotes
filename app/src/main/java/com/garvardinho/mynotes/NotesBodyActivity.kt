package com.garvardinho.mynotes

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NotesBodyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_body)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
            return
        }

        if (savedInstanceState == null) {
            val details = NotesBodyFragment()
            details.arguments = intent.extras
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, details)
                .commit()
        }
    }
}