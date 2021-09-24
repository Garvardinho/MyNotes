package com.garvardinho.mynotes

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

object RealmInstance {

    fun getInstance(context: Context) : Realm {
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build()

        return Realm.getInstance(config)
    }
}