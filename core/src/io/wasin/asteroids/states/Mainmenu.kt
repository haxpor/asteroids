package io.wasin.asteroids.states

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import io.wasin.asteroids.entities.MenuItem
import io.wasin.asteroids.handlers.BBInput
import io.wasin.asteroids.handlers.GameStateManager

/**
 * Created by haxpor on 7/12/17.
 */
class Mainmenu(gsm: GameStateManager): GameState(gsm), MenuItem.Clickable {

    private val titleFont: BitmapFont
    private val font: BitmapFont
    private val fontRed: BitmapFont

    private val title: String = "Asteroids"
    private val titleGlyph: GlyphLayout
    private var itemTitles: Array<String>
    private val menuItems: Array<MenuItem>
    private var currentSelectedMenuItem: Int = 0

    init {
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))

        // title font
        titleFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
            it.size = 56
        })

        // font
        font = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
            it.size = 20
        })

        // font red
        fontRed = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
            it.size = 20
            it.color = Color.RED
        })

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
            i -> MenuItem(hudCam.viewportWidth/2f, hudCam.viewportHeight/2f-50f*i, itemTitles[i], font, fontRed)
                .also { it.listener = this }
        })
    }

    override fun handleInput(dt: Float) {
        if (BBInput.isPressed(BBInput.BUTTON_UP)) {
            currentSelectedMenuItem--
            if (currentSelectedMenuItem < 0f) {
                currentSelectedMenuItem = itemTitles.size + currentSelectedMenuItem
            }
        }
        if (BBInput.isPressed(BBInput.BUTTON_DOWN)) {
            currentSelectedMenuItem = (currentSelectedMenuItem+1) % itemTitles.size
        }

        if (BBInput.isPressed(BBInput.BUTTON_ENTER)) {
            select()
        }
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
        menuItems.forEachIndexed { i,
                                   it -> it.apply {
                // if current selected menu item matches with menu item then render it with red color
                it.highlight = i == currentSelectedMenuItem
                it.render(sb)
            }
        }
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {
        // update position of all menu items as viewport changed
        menuItems.forEachIndexed { i, menuItem ->  menuItem.setPosition(hudCam.viewportWidth/2f, hudCam.viewportHeight/2f-50f*i) }
    }

    private fun select() {
        when(currentSelectedMenuItem) {
            0 -> gsm.setState(Play(gsm))
            1 -> println("go to high score state")
            2 -> Gdx.app.exit()
        }
    }

    override fun onClick(item: MenuItem) {
        when (item.text) {
            "Play" -> {
                currentSelectedMenuItem = 0
                gsm.setState(Play(gsm))
            }
            "High Scores" -> {
                currentSelectedMenuItem = 1
                println("go to high scrore state")
            }
            "Quit" -> {
                currentSelectedMenuItem = 2
                Gdx.app.exit()
            }
        }
    }
}