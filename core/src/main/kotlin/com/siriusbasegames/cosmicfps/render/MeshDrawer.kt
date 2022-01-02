package com.siriusbasegames.cosmicfps.render

import com.badlogic.gdx.graphics.g3d.ModelBatch
import ktx.graphics.use

class MeshDrawer {
    private var batch: ModelBatch

    init {

    }

    fun render() {
        drawMesh()
    }

    private fun drawMesh() {
        batch.use {
            it.draw(image, -5f, -5f, 10f, 10f)
        }
    }
}