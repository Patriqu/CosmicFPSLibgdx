package com.siriusbasegames.cosmicfps.android

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.siriusbasegames.cosmicfps.CosmicFPS

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(CosmicFPS(), AndroidApplicationConfiguration().apply {
            // Configure your application here.
        })
    }
}
