package com.siriusbasegames.cosmicfps.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import ktx.assets.disposeSafely
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx
import net.mgsx.gltf.scene3d.scene.Scene
import net.mgsx.gltf.scene3d.scene.SceneAsset
import net.mgsx.gltf.scene3d.scene.SceneManager
import net.mgsx.gltf.scene3d.scene.SceneSkybox
import net.mgsx.gltf.scene3d.utils.IBLBuilder

class CosmicSceneManager(debugCamera: PerspectiveCamera) : SceneManager() {
    private val sceneCamera: PerspectiveCamera = debugCamera

    private var scene: Scene

    private var sceneAsset: SceneAsset
    private val sceneName = "scene.glft"

    private lateinit var light: DirectionalLightEx

    private lateinit var skybox: SceneSkybox

    private lateinit var environmentCubemap: Cubemap
    private lateinit var diffuseCubemap: Cubemap
    private lateinit var specularCubemap: Cubemap
    private lateinit var brdfLUT: Texture

    private var assetManager = CosmicAssetManager()

    init {
        setupCamera()

        sceneAsset = assetManager.loadGlftModelWithoutManager(sceneName)

        scene = Scene(sceneAsset.scene)

        addScene(scene, true)

        val modelPosition = Vector3(sceneCamera.position.x, sceneCamera.position.y, sceneCamera.position.z - 20)
        scene.modelInstance.transform.set(modelPosition, Quaternion())

        setupDirectionalLight()
        setupImageBasedLighting()
        setupPBREnvironment()
        setupSkybox()
    }

    private fun setupCamera() {
        val d = .02f
        sceneCamera.near = d / 1000f
        sceneCamera.far = d * 4

        sceneCamera.up.set(Vector3.Y)
        sceneCamera.lookAt(Vector3.Zero)

        setCamera(sceneCamera)
    }

    private fun setupDirectionalLight() {
        light = DirectionalLightEx()
        light.direction.set(1f, -3f, 1f).nor()
        light.color.set(Color.WHITE)

        environment.add(light)
    }

    private fun setupImageBasedLighting() {
        val iblBuilder = IBLBuilder.createOutdoor(light)

        environmentCubemap = iblBuilder.buildEnvMap(1024)
        diffuseCubemap = iblBuilder.buildIrradianceMap(256)
        specularCubemap = iblBuilder.buildRadianceMap(10)

        iblBuilder.dispose()
    }

    private fun setupPBREnvironment() {
        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"))

        setAmbientLight(1f)
        environment.set(PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT))
        environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap))
        environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap))
    }

    private fun setupSkybox() {
        skybox = SceneSkybox(environmentCubemap)
        setSkyBox(skybox)
    }

    fun updateCamera(time: Float) {
        //camera.position.setFromSpherical(MathUtils.PI/4, time * .3f).scl(.02f);
        /*camera.up.set(Vector3.Y)
        camera.lookAt(Vector3.Zero)*/
        camera.update()
    }

    fun updateAndRender(deltaTime: Float) {
        update(deltaTime)
        render()
    }

    override fun dispose() {
        sceneAsset.disposeSafely()
        environmentCubemap.disposeSafely()
        diffuseCubemap.disposeSafely()
        specularCubemap.disposeSafely()
        brdfLUT.disposeSafely()
        skybox.disposeSafely()
    }
}