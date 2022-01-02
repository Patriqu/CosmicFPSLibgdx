@file:JvmName("Lwjgl3Launcher")

package com.siriusbasegames.cosmicfps.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.siriusbasegames.cosmicfps.CosmicFPS

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(CosmicFPS(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Cosmic FPS Libgdx")
        setWindowedMode(1024, 768)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
