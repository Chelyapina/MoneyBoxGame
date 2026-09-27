package com.example.moneyboxgame

import android.app.Application
import com.example.moneyboxgame.di.AppComponent
import com.example.moneyboxgame.di.DaggerAppComponent
import com.example.moneyboxgame.di.DataModule

class MoneyBoxApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataModule(DataModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}