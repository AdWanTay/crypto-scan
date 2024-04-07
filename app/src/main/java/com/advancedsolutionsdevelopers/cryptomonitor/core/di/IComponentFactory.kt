package com.advancedsolutionsdevelopers.cryptomonitor.core.di

public interface IComponentFactory<out T> {
    public fun create(): T
}
