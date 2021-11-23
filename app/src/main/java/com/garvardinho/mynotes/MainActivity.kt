package com.garvardinho.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.garvardinho.mynotes.ui.CreateNewNoteFragment
import com.garvardinho.mynotes.ui.MenuObject
import com.google.android.material.navigation.NavigationView
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
        initDrawer(toolbar)
    }

    private fun initDrawer(toolbar: Toolbar) {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id: Int = menuItem.itemId
            if (navigateFragment(id)) {
                drawer.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            }
            return@setNavigationItemSelectedListener false
        }
    }

    private fun navigateFragment(id: Int): Boolean {
        return when (id) {
            R.id.menu_add -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.notes, CreateNewNoteFragment())
                    .setTransition(TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()

                true
            }
            else -> false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (navigateFragment(item.itemId))
            return true

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_main_menu, menu)
        if (menu != null) {
            MenuObject.menuAdd = menu.findItem(R.id.menu_add)
            MenuObject.menuDelete = menu.findItem(R.id.menu_delete)
            MenuObject.menuEdit = menu.findItem(R.id.menu_edit)
            MenuObject.menuSave = menu.findItem(R.id.menu_save)
            MenuObject.setMenuItemsVisibility(MenuObject.MainTag)
        }
        return super.onCreateOptionsMenu(menu)
    }
}