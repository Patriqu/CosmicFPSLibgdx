package com.siriusbasegames.cosmicfps

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.siriusbasegames.cosmicfps.input.DebugInputProcessor
import com.siriusbasegames.cosmicfps.input.FPSInputProcessor
import com.siriusbasegames.cosmicfps.input.GameInputProcessor

class Updater(private val gameStateHandler: GameStateHandler) {
    val firstPersonCamera = PerspectiveCamera(90f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    val debugCamera: PerspectiveCamera = PerspectiveCamera(90f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    private var fpsInputProcessor = FPSInputProcessor(firstPersonCamera, gameStateHandler)
    private var gameInputProcessor = GameInputProcessor(gameStateHandler)
    private var debugInputProcessor = DebugInputProcessor(debugCamera, gameStateHandler)

    private var inputMultiplexer: InputMultiplexer = InputMultiplexer()

    init {
        inputMultiplexer.addProcessor(fpsInputProcessor)
        inputMultiplexer.addProcessor(gameInputProcessor)
        inputMultiplexer.addProcessor(debugInputProcessor)

        input.inputProcessor = inputMultiplexer
        input.isCursorCatched = true
    }

    fun update(): Boolean {
        if (gameStateHandler.state == GameState.EXIT_GAME) {
            return false
        }

        fpsInputProcessor.handleMovement()
        debugInputProcessor.handleMovement()

        return true
    }

    fun placeCameraAtStart() {
        firstPersonCamera.position.set(0f, 8f, 100f)
        firstPersonCamera.lookAt(Vector3.Zero)
        firstPersonCamera.near = 0.1f
        firstPersonCamera.far = 300f
        firstPersonCamera.update()
    }
}