package com.siriusbasegames.cosmicfps.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.Timer
import com.siriusbasegames.cosmicfps.CameraToDraw
import com.siriusbasegames.cosmicfps.GameState
import com.siriusbasegames.cosmicfps.GameStateHandler

class FPSInputProcessor(private val fpsCamera: Camera, private val gameStateHandler:
GameStateHandler
) : InputProcessor {

    private var screenX: Int = Gdx.graphics.width/2
    private var screenY: Int = Gdx.graphics.height/2

    private val rotationUpperLimitX = Gdx.graphics.width * 0.75
    private val rotationLowerLimitX = Gdx.graphics.width * 0.25

    private val rotationUpperLimitY = Gdx.graphics.height * 0.75
    private val rotationLowerLimitY = Gdx.graphics.height * 0.25

    private val sensitivityX: Float = 0.5F
    private val sensitivityY: Float = 0.5F

    private var walkForward = false
    private var walkBackward = false
    private var strafeLeft = false
    private var strafeRight = false

    private var jumpSteps = 8
    private var actualJumpStep = 1
    private val jumpStepsDelay = 0.01f
    private val jumpStepDistance = 0.4f
    private var jumpin = false
    private lateinit var jumpCountdownTask: Timer.Task

    private val crouchValue = 1.5f

    init {
        Gdx.input.setCursorPosition(screenX, Gdx.graphics.height - screenY)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            val actualState = gameStateHandler.state
            if (actualState == GameState.LEVEL_COMPLETE) {
                gameStateHandler.state = GameState.NEXT_LEVEL
            } else if (actualState == GameState.LOSE || actualState == GameState.WIN) {
                gameStateHandler.state = GameState.RESET
            }

            return true
        }

        if (gameStateHandler.cameraToDraw == CameraToDraw.FPS) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (!jumpin) {
                    jumpCountdownTask = startJump()
                    jumpin = true
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.C)) {
                fpsCamera.translate(0f, -crouchValue, 0f)
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                walkForward = true
                return true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                walkBackward = true
                return true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                strafeLeft = true
                return true
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                strafeRight = true
                return true
            }
        }

        return false
    }

    private fun startJump(): Timer.Task {
        return Timer.schedule(object : Timer.Task() {
            override fun run() {
                if (actualJumpStep > jumpSteps*2) {
                    jumpin = false
                    actualJumpStep = 1
                    cancel()
                } else if (actualJumpStep++ <= jumpSteps) {
                    fpsCamera.translate(0f, jumpStepDistance, 0f)
                } else if (actualJumpStep > jumpSteps) {
                    fpsCamera.translate(0f, -jumpStepDistance, 0f)
                }
            }
        }, 0f, jumpStepsDelay)
    }

    fun handleMovement() {
        if (walkForward) {
            fpsCamera.translate(0f, 0f, -0.2f)
            println("W")
        }
        if (walkBackward) {
            fpsCamera.translate(0f, 0f, 0.2f)
            println("S")
        }

        if (strafeLeft) {
            fpsCamera.translate(-0.2f, 0f, 0f)
            println("A")
        }
        if (strafeRight) {
            fpsCamera.translate(0.2f, 0f, 0f)
            println("D")
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        if (gameStateHandler.cameraToDraw == CameraToDraw.FPS) {
            if (keycode == Input.Keys.C) {
                fpsCamera.translate(0f, crouchValue, 0f)
                return true
            }

            //if (walkForward) {
            if (keycode == Input.Keys.W) {
                walkForward = false
                return true
            }
            //if (walkBackward) {
            if (keycode == Input.Keys.S) {
                walkBackward = false
                return true
            }
            //if (strafeLeft) {
            if (keycode == Input.Keys.A) {
                strafeLeft = false
                return true
            }
            //if (strafeRight) {
            if (keycode == Input.Keys.D) {
                strafeRight = false
                return true
            }
        }

        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        /*if (Gdx.input.isTouched(0)) {
            val actualState = gameStateHandler.getState()
            if (actualState == GameState.RUNNING) {
                Gdx.app.log("Mouse", "LMB x=$screenX, y=$screenY")

                val touchPosition = Vector3()
                touchPosition[screenX.toFloat(), screenY.toFloat()] = 0f

                val worldPosition = fpsCamera.unproject(touchPosition)
                val x = worldPosition.x
                val y = worldPosition.y

                //gridController.revealCard(x, y, fpsCamera)
            } else if (actualState == GameState.LEVEL_COMPLETE) {
                gameStateHandler.setState(GameState.NEXT_LEVEL)
            } else if (actualState == GameState.LOSE || actualState == GameState.WIN) {
                gameStateHandler.setState(GameState.RESET)
            }
        }*/

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        if (gameStateHandler.cameraToDraw == CameraToDraw.FPS) {
            //if (screenX < Gdx.graphics.width/2 && screenX > 0) {
            //if (screenX <= rotationUpperLimitX && screenX >= rotationLowerLimitX) {
            //if (screenX > this.screenX) {
            if (screenX > Gdx.graphics.width/2) {
                fpsCamera.rotate(((this.screenX - screenX) * sensitivityX), 0f, 1f, 0f)
                //fpsCamera.rotate(((Gdx.graphics.width/2 - this.screenX - screenX) * sensitivityX), 0f, 1f, 0f)
                //} else if (screenX < this.screenX) {
            } else if (screenX <= Gdx.graphics.width/2) {
                fpsCamera.rotate(-((this.screenX - screenX) * sensitivityX), 0f, -1f, 0f)
                //fpsCamera.rotate(-((Gdx.graphics.width/2 - this.screenX - screenX) * sensitivityX), 0f, -1f, 0f)
            }

            this.screenX = screenX
            /* } else if (screenX < 0) {
                 this.screenX = 0
             } else if (screenX > rotationUpperLimitX) {
                 //this.screenX = Gdx.graphics.width/2
                 this.screenX = rotationUpperLimitX.toInt()
             }*/

            //if (screenY <= Gdx.graphics.height/2 && screenY >= 0) {
            if (screenY <= rotationUpperLimitY && screenY >= rotationLowerLimitY) {
                if (screenY > this.screenY/2) {
                    fpsCamera.rotate(((this.screenY - screenY) * sensitivityY), 1f, 0f, 0f)
                } else if (screenY <= this.screenY/2) {
                    fpsCamera.rotate(-((this.screenY - screenY) * sensitivityY), -1f, 0f, 0f)
                }

                this.screenY = screenY
            } else if (screenY < 0) {
                this.screenY = 0
            } else if (screenY > rotationUpperLimitY){
                //this.screenY = Gdx.graphics.height/2
                this.screenY = rotationUpperLimitY.toInt()
            }

            //println("screenY: $screenY")
            println("screenX: " + this.screenX + " screenY: " + this.screenY)

            return true
        }

        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}