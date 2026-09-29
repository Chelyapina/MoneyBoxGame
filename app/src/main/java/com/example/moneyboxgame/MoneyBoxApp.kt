package com.example.moneyboxgame

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.moneyboxgame.di.AppComponent
import com.example.moneyboxgame.di.DaggerAppComponent
import com.example.moneyboxgame.di.DataModule

class MoneyBoxApp : Application(), ImageLoaderFactory {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .dataModule(DataModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
}