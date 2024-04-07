package com.advancedsolutionsdevelopers.cryptomonitor.core

interface ICache<DATA> {
    fun put(data: DATA)
    fun get(): DATA
    fun drop()
    fun reduce(reducer: (state: DATA) -> DATA) {
        put(reducer(get()))
    }
}