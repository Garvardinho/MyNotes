package com.garvardinho.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    companion object {
        fun getRealmInstance(): Realm {
            return Realm.getDefaultInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder().allowWritesOnUiThread(true).build()
        )
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.notes, CreateNewNoteFragment())
                .setTransition(TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_main_menu, menu)

        if (menu != null) {
            val menuAdd: MenuItem = menu.findItem(R.id.menu_add)
            val menuSave: MenuItem = menu.findItem(R.id.menu_save)

            menuAdd.isVisible = true
            menuSave.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }
}