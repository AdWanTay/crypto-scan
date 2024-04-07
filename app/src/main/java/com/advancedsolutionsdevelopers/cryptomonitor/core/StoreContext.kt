package com.advancedsolutionsdevelopers.cryptomonitor.core

public class StoreContext<STATE : Any>(
    public val reduce: suspend ((STATE) -> STATE) -> Unit
)
