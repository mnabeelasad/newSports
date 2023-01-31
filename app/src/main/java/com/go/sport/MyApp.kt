package com.go.sport

import android.app.Application
import com.facebook.FacebookSdk
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        EmojiManager.install(IosEmojiProvider())
        Places.initialize(applicationContext, applicationContext.getString(R.string.api_key));
        FirebaseApp.initializeApp(this)
        FacebookSdk.sdkInitialize(this)
    }
}