package com.siriusbasegames.cosmicfps

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.siriusbasegames.cosmicfps.managers.CosmicSceneManager
import com.siriusbasegames.cosmicfps.render.TexturesDrawer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync


class CosmicFPS : KtxGame<KtxScreen>() {
    private val gameStateHandler = GameStateHandler()

    override fun create() {
        KtxAsync.initiate()

        addScreen(GameScreen(gameStateHandler))
        setScreen<GameScreen>()
    }
}

class GameScreen(private val gameStateHandler: GameStateHandler) : KtxScreen {
    private var time = 0f

    private val texturesDrawer = TexturesDrawer()

    private val sceneManager: CosmicSceneManager

    private val updater: Updater

    init {
        updater = Updater(gameStateHandler)

        updater.placeCameraAtStart()

        sceneManager = CosmicSceneManager(updater.debugCamera)
    }

    override fun render(delta: Float) {
        val deltaTime = Gdx.graphics.deltaTime
        time += deltaTime

        updateFPSCamera(time)
        clearScreen()

        if (!updater.update()) {
            dispose()
            Gdx.app.exit()
            return
        }

        draw(deltaTime)
    }

    private fun updateFPSCamera(time: Float) {
        if (gameStateHandler.cameraToDraw == CameraToDraw.FPS) {
            texturesDrawer.updateProjectionMatrix(updater.firstPersonCamera.combined)
            updater.firstPersonCamera.update()
        } else if (gameStateHandler.cameraToDraw == CameraToDraw.SCENE) {
            sceneManager.updateCamera(time)
        }
    }

    private fun clearScreen() {
        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
    }

    private fun draw(deltaTime: Float) {
        if (gameStateHandler.cameraToDraw == CameraToDraw.FPS) {
            texturesDrawer.render()
        } else if (gameStateHandler.cameraToDraw == CameraToDraw.SCENE) {
            sceneManager.updateAndRender(deltaTime)
        }
    }

    override fun resize(width: Int, height: Int) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        sceneManager.updateViewport(width.toFloat(), height.toFloat())
    }

    override fun dispose() {
        texturesDrawer.dispose()
        sceneManager.dispose()
    }
}
