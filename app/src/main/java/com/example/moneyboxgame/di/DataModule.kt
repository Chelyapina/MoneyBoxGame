package com.example.moneyboxgame.di

import android.content.Context
import com.example.data.GameStateStore
import com.example.data.GameStateStoreImpl
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class DataModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideGameStateStore(): GameStateStore = GameStateStoreImpl(context)
}