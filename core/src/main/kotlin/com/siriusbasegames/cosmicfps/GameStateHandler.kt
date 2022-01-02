package com.siriusbasegames.cosmicfps

class GameStateHandler {
    var state = GameState.STARTED

    var cameraToDraw: CameraToDraw = CameraToDraw.FPS

    private var currentLevel = 1
    private var maxLevels = 3

    fun currentLevel(): Int {
        return currentLevel
    }

    fun nextLevel() {
        if (currentLevel <= 0) {
            currentLevel = 1
        } else if (currentLevel < maxLevels) {
            ++currentLevel
        } else {
            currentLevel = 1
        }
    }

    fun setLevel(level: Int) {
        if (level > maxLevels) {
            currentLevel = maxLevels
        } else {
            currentLevel = level
        }
    }

    fun runGame() {
        state = GameState.RUNNING
    }

    fun setWinState() {
        state = if (currentLevel == maxLevels) {
            GameState.WIN
        } else {
            GameState.LEVEL_COMPLETE
        }
    }

    fun resetGame() {
        currentLevel = 1
        state = GameState.RUNNING
    }
}

enum class GameState {
    STARTED,
    RUNNING,
    LOSE,
    LEVEL_COMPLETE,
    NEXT_LEVEL,
    WIN,
    RESET,
    EXIT_GAME
}