package com.garvardinho.mynotes.data

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class CardsSourceAdapter(backgroundRealmThread: Realm) : CardsSource {

    private var dataSource: RealmResults<Note> = backgroundRealmThread.where<Note>().findAll()

    override fun getCardData(position: Int) : Note? {
        return dataSource[position]
    }

    override fun getSize() : Int {
        return dataSource.size
    }
}