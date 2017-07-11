package io.wasin.asteroids.states

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import io.wasin.asteroids.entities.MenuItem
import io.wasin.asteroids.handlers.GameStateManager

/**
 * Created by haxpor on 7/12/17.
 */
class Mainmenu(gsm: GameStateManager): GameState(gsm), MenuItem.Clickable {

    private val titleFont: BitmapFont
    private val font: BitmapFont

    private val title: String = "Asteroids"
    private val titleGlyph: GlyphLayout
    private var itemTitles: Array<String>
    private val menuItems: Array<MenuItem>

    init {
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))
        val fontParams = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParams.size = 56
        // title font
        titleFont = fontGen.generateFont(fontParams)

        // font
        fontParams.size = 20
        font = fontGen.generateFont(fontParams)
        // dispose font generator
        fontGen.dispose()

        // create glyph of title text
        titleGlyph = GlyphLayout(titleFont, title)

        // populate item's titles according to the platform it's running on
        // as we cannot do Gdx.app.exit() on iOS (should be avoided anyway), and we will avoid on Android as well
        // thus only do it on Desktop
        if (Gdx.app.type == Application.ApplicationType.iOS ||
                Gdx.app.type == Application.ApplicationType.Android) {
            itemTitles = arrayOf("Play", "High Scores")
        }
        else {
            itemTitles = arrayOf("Play", "High Scores", "Quit")
        }

        // create menuitems
        menuItems = Array(itemTitles.size, {
            i -> MenuItem(hudCam.viewportWidth/2f, hudCam.viewportHeight/2f-50f*i, itemTitles[i], font)
                .also { it.listener = this }
        })
    }

    override fun handleInput(dt: Float) {

    }

    override fun update(dt: Float) {
        handleInput(dt)

        menuItems.forEach { it.update(dt, cam, camViewport) }
    }

    override fun render() {
        sb.projectionMatrix = hudCam.combined
        hudViewport.apply(true)

        // clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()
        titleFont.draw(sb, titleGlyph,
                hudCam.viewportWidth/2f - titleGlyph.width/2f,
                hudCam.viewportHeight/2f + titleGlyph.height + 70f)
        menuItems.forEach { it.render(sb) }
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {
        // update position of all menu items as viewport changed
        menuItems.forEachIndexed { i, menuItem ->  menuItem.setPosition(hudCam.viewportWidth/2f, hudCam.viewportHeight/2f-50f*i) }
    }

    override fun onClick(item: MenuItem) {
        // we don't do quit on mobile platform
        if (Gdx.app.type == Application.ApplicationType.iOS ||
                Gdx.app.type == Application.ApplicationType.Android) {
            when (item.text) {
                "Play" -> gsm.setState(Play(gsm))
                "High Scores" -> println("go to high scrore state")
            }
        }
        else {
            when (item.text) {
                "Play" -> gsm.setState(Play(gsm))
                "High Scores" -> println("go to high scrore state")
                "Quit" -> Gdx.app.exit()    // should be used only for desktop
            }
        }
    }
}