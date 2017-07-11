package io.wasin.asteroids.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.wasin.asteroids.Game
import io.wasin.asteroids.handlers.GameStateManager

/**
 * Created by haxpor on 5/14/17.
 */
abstract class GameState(gsm: GameStateManager) {
    protected var gsm: GameStateManager = gsm

    lateinit protected var camViewport: Viewport
    lateinit protected var hudViewport: Viewport

    // for convenient in reference and use in derived class
    protected val game: Game = gsm.game
    protected val sb: SpriteBatch = gsm.game.sb
    lateinit protected var cam: OrthographicCamera
    lateinit protected var hudCam: OrthographicCamera

    init {
        setupCamera(Game.V_WIDTH, Game.V_HEIGHT)
        setupViewport(cam, hudCam, Game.V_WIDTH, Game.V_HEIGHT)
        // always update viewport
        camViewport.update(Gdx.app.graphics.width, Gdx.app.graphics.height)
        hudViewport.update(Gdx.app.graphics.width, Gdx.app.graphics.height, true)
    }

    abstract fun handleInput(dt: Float)
    abstract fun update(dt: Float)
    abstract fun render()
    abstract fun dispose()
    abstract fun resize_user(width: Int, height: Int)

    fun resize(width: Int, height: Int) {
        camViewport.update(width, height)
        hudViewport.update(width, height, true)

        resize_user(width, height)
    }

    /**
     * Set up cam, and hudCam.
     * If needed to create a different type of camera and viewport, then override and implement it in
     * GameState class.
     */
    open protected fun setupCamera(viewportWidth: Float, viewportHeight: Float) {
        // set up cam
        cam = OrthographicCamera()
        cam.setToOrtho(false, viewportWidth, viewportHeight)

        // set up hud-cam
        hudCam = OrthographicCamera()
        hudCam.setToOrtho(false, viewportWidth, viewportHeight)
    }

    /**
     * Set up camViewport, and hudViewport.
     * If needed to create a different type of viewport, then override and implement it in GameState class.
     */
    open protected fun setupViewport(cam: OrthographicCamera, hudCam: OrthographicCamera, viewportWidth: Float, viewportHeight: Float) {
        camViewport = ExtendViewport(viewportWidth, viewportHeight, cam)
        hudViewport = ExtendViewport(viewportWidth, viewportHeight, hudCam)

        // apply to use cam viewport
        camViewport.apply()
    }
}