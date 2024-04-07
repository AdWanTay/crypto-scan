package com.advancedsolutionsdevelopers.cryptomonitor.core.converter

public interface OneWayConverter<F, T> {
    public fun convert(from: F): T
}
