package com.garvardinho.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}