package com.example.moneyboxgame.di

import android.content.Context
import com.example.data.gameState.data.GameStateStore
import com.example.data.gameState.data.GameStateStoreImpl
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class DataModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideGameStateStore(): GameStateStore = GameStateStoreImpl(context)
}