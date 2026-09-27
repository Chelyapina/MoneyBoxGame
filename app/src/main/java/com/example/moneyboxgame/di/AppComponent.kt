package com.example.moneyboxgame.di

import com.example.moneyboxgame.MoneyBoxApp
import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {
    fun inject(app: MoneyBoxApp)
    fun viewModelFactory(): AppViewModelFactory
}