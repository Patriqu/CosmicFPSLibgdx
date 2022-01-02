package com.siriusbasegames.cosmicfps.render

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use

class TexturesDrawer {
    private val batch = SpriteBatch()

    private val image = Texture("logo.png".toInternalFile(), true)
        .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) }

    fun render() {
        drawTextures()
    }

    fun updateProjectionMatrix(matrix: Matrix4) {
        batch.projectionMatrix = matrix
    }

    private fun drawTextures() {
        batch.use {
            it.draw(image, -5f, -5f, 10f, 10f)
        }
    }

    fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}