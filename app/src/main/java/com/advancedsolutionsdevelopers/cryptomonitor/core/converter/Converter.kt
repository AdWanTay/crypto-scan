package com.advancedsolutionsdevelopers.cryptomonitor.core.converter

public interface Converter<F, T> : OneWayConverter<F, T> {
    public fun reverse(from: T): F
}
