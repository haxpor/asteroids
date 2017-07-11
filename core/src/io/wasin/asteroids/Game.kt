package io.wasin.asteroids

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.wasin.asteroids.handlers.BBInput
import io.wasin.asteroids.handlers.BBInputProcessor
import io.wasin.asteroids.handlers.Content
import io.wasin.asteroids.handlers.GameStateManager
import io.wasin.asteroids.handlers.PlayerSaveFileManager
import io.wasin.asteroids.handlers.Settings
import io.wasin.asteroids.states.Mainmenu
import io.wasin.asteroids.states.Play

class Game : ApplicationAdapter() {

    lateinit var sb: SpriteBatch
        private set
    lateinit var gsm: GameStateManager
        private set
    lateinit var playerSaveFileManager: PlayerSaveFileManager
        private set

    companion object {
        const val TITLE = "Asteroids"
        const val V_WIDTH = 640f
        const val V_HEIGHT = 480f

        var res: Content = Content()
            private set
    }

    override fun create() {

        Gdx.input.inputProcessor = BBInputProcessor()

        sb = SpriteBatch()

        gsm = GameStateManager(this)

        // create player's savefile manager with pre-set of savefile's path
        playerSaveFileManager = PlayerSaveFileManager(Settings.PLAYER_SAVEFILE_RELATIVE_PATH)

        // load any resource here...
        res.loadSound("sounds/explode.ogg", "explode")
        res.loadSound("sounds/extralife.ogg", "extralife")
        res.loadSound("sounds/largesaucer.ogg", "largesaucer")
        res.loadSound("sounds/pulsehigh.ogg", "pulsehigh")
        res.loadSound("sounds/pulselow.ogg", "pulselow")
        res.loadSound("sounds/saucershoot.ogg", "saucershoot")
        res.loadSound("sounds/shoot.ogg", "shoot")
        res.loadSound("sounds/smallsaucer.ogg", "smallsaucer")
        res.loadSound("sounds/thruster.ogg", "thruster")


        // only this time to check for controller
        // if user plug in controller after this then they have to restart the game
        setupFirstActiveController()

        // go to main menu state
        gsm.setState(Mainmenu(gsm))
    }

    private fun setupFirstActiveController() {
        if (Controllers.getControllers().count() > 0) {
            val bbInputProcessor = Gdx.input.inputProcessor as BBInputProcessor
            val controller = Controllers.getControllers().first()
            controller.addListener(bbInputProcessor)
            bbInputProcessor.setController1(controller)
        }
    }

    override fun render() {
        Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.framesPerSecond)
        gsm.update(Gdx.graphics.deltaTime)
        gsm.render()
        BBInput.update()
    }

    override fun dispose() {
    }

    override fun resize(width: Int, height: Int) {
        gsm.resize(width, height)
    }
}
