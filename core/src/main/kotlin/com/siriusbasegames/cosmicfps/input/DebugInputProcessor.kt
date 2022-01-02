package com.siriusbasegames.cosmicfps.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.siriusbasegames.cosmicfps.CameraToDraw
import com.siriusbasegames.cosmicfps.GameState
import com.siriusbasegames.cosmicfps.GameStateHandler

class DebugInputProcessor(private val debugCamera: PerspectiveCamera, private val gameStateHandler: GameStateHandler) : InputProcessor {
    private var screenX: Int = Gdx.graphics.width/2
    private var screenY: Int = Gdx.graphics.height/2

    private val rotationUpperLimitY = Gdx.graphics.height * 0.75
    private val rotationLowerLimitY = Gdx.graphics.height * 0.25

    private val sensitivityX: Float = 0.5F
    private val sensitivityY: Float = 0.5F

    private var forward = false
    private var backward = false
    private var strafeLeft = false
    private var strafeRight = false
    private var up = false
    private var down = false

    override fun keyDown(keycode: Int): Boolean {
        debugKeys()

        return false
    }

    private fun debugKeys() {
        /*if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            //gridController.enterTheBreakpoint()
        }*/
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            gameStateHandler.state = GameState.LEVEL_COMPLETE
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            gameStateHandler.cameraToDraw = CameraToDraw.FPS
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            gameStateHandler.cameraToDraw = CameraToDraw.SCENE
        }

        if (gameStateHandler.cameraToDraw == CameraToDraw.SCENE) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                up = true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.C)) {
                down = true
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                forward = true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                backward = true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                strafeLeft = true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                strafeRight = true
            }
        }
    }

    fun handleMovement() {
        if (gameStateHandler.cameraToDraw == CameraToDraw.SCENE) {
            if (forward) {
                debugCamera.translate(0f, 0f, -0.2f)
            }
            if (backward) {
                debugCamera.translate(0f, 0f, 0.2f)
            }

            if (strafeLeft) {
                debugCamera.translate(-0.2f, 0f, 0f)
            }
            if (strafeRight) {
                debugCamera.translate(0.2f, 0f, 0f)
            }

            if (up) {
                debugCamera.translate(0f, -0.2f, 0f)
            }
            if (down) {
                debugCamera.translate(0f, 0.2f, 0f)
            }
        }
        //debugCamera.update()
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        if (gameStateHandler.cameraToDraw == CameraToDraw.SCENE) {
            if (screenX > Gdx.graphics.width/2) {
                debugCamera.rotate(((this.screenX - screenX) * sensitivityX), 0f, 1f, 0f)
            } else if (screenX <= Gdx.graphics.width/2) {
                debugCamera.rotate(-((this.screenX - screenX) * sensitivityX), 0f, -1f, 0f)
            }

            this.screenX = screenX

            if (screenY <= rotationUpperLimitY && screenY >= rotationLowerLimitY) {
                if (screenY > this.screenY/2) {
                    debugCamera.rotate(((this.screenY - screenY) * sensitivityY), 1f, 0f, 0f)
                } else if (screenY <= this.screenY/2) {
                    debugCamera.rotate(-((this.screenY - screenY) * sensitivityY), -1f, 0f, 0f)
                }

                this.screenY = screenY
            } else if (screenY < 0) {
                this.screenY = 0
            } else if (screenY > rotationUpperLimitY){
                this.screenY = rotationUpperLimitY.toInt()
            }

            println("[DEBUG] screenX: " + this.screenX + " screenY: " + this.screenY)

            return true
        }

        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}