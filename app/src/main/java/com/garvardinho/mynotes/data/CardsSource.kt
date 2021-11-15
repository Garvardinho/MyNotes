package com.garvardinho.mynotes.data

interface CardsSource {
    fun getCardData(position: Int) : Note?
    fun getSize() : Int
}