package com.siriusbasegames.cosmicfps.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader
import net.mgsx.gltf.loaders.gltf.GLTFLoader
import net.mgsx.gltf.scene3d.scene.SceneAsset

class CosmicAssetManager : AssetManager() {
    private val assetsDirectory = "assets/"
    private val modelsDirectory = assetsDirectory + "models/"

    init {
        setLoader(SceneAsset::class.java, ".gltf", GLTFAssetLoader())
    }

    fun loadGlftModel(filename: String): SceneAsset {
        load(modelsDirectory + filename, SceneAsset::class.java)

        return get(modelsDirectory + filename, SceneAsset::class.java)
    }

    fun loadGlftModelWithoutManager(filename: String): SceneAsset {
        return GLTFLoader().load(Gdx.files.internal(modelsDirectory + "scene.gltf"))
    }
}