package io.wasin.asteroids.states

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.wasin.asteroids.data.PlayerScore
import io.wasin.asteroids.entities.TouchButton
import io.wasin.asteroids.handlers.BBInput
import io.wasin.asteroids.handlers.GameStateManager
import io.wasin.asteroids.handlers.Settings

/**
 * Created by haxpor on 7/13/17.
 */
class GameOver(score: Long, gsm: GameStateManager): GameState(gsm) {

    private var titleFont: BitmapFont
    private var titleGlyph: GlyphLayout

    private var score: Long = score
    private var regardedAsOneOfHighScores: Boolean = false
    private var singleCharTextFontWidth: Int = -1
    private var singleCharTextFontHeight: Int = -1
    private var currentNameInputCharacterIndex: Int = 0

    // if input score can be regarded as one of high scores
    // then these will be initialized
    private var subtitleFont: BitmapFont? = null
    private var inputNameFont: BitmapFont? = null
    private var subtitleGlyph: GlyphLayout? = null
    private var inputNameGlyph: GlyphLayout? = null
    private var inputName: StringBuilder? = null

    // only for Android, and iOS
    private var sr: ShapeRenderer? = null
    private var smallUIFont: BitmapFont? = null
    private var up: TouchButton? = null
    private var down: TouchButton? = null
    private var switch: TouchButton? = null
    private var ok: TouchButton? = null

    init {
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))
        titleFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
            it.size = 40
        })

        // create fonts
        // - title font
        titleGlyph = GlyphLayout(titleFont, "Game Over")

        // sync save file, then get highscores data
        val highScores = game.playerSaveFileManager.sync(Settings.TOTAL_HIGH_SCORES_RECORD).highScores
        // check if input score can beat current high-score, thus we can save it
        if (score > highScores.last().score) {
            regardedAsOneOfHighScores = true
        }

        // check if running on mobile platform
        val isAndroidOriOS = Gdx.app.type == Application.ApplicationType.iOS || Gdx.app.type == Application.ApplicationType.Android

        // create shape renderer is conditions are met
        if (isAndroidOriOS || regardedAsOneOfHighScores) {
            sr = ShapeRenderer()
        }

        // initialize variables if needed
        if (regardedAsOneOfHighScores) {

            subtitleFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
                it.size = 35
            })
            inputNameFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
                it.size = 35
                it.kerning = false  // we want monospace-type of font here to render each character
                                    // of input name
            })

            // get a single character width from font text
            GlyphLayout(inputNameFont, "A").also {
                singleCharTextFontWidth = it.width.toInt()
                singleCharTextFontHeight = it.height.toInt()
            }

            subtitleGlyph = GlyphLayout(subtitleFont, "New High Score: $score")
            inputName = StringBuilder("AAA")
            inputNameGlyph = GlyphLayout(subtitleFont, inputName)

            // only for Android and iOS
            if (isAndroidOriOS) {

                // touch buttons
                val radius = 35f
                val yPos = hudCam.viewportHeight/2f + 70f - titleGlyph.height/2f - 30f -
                        subtitleGlyph!!.height/2f - inputNameGlyph!!.height/2f - 35f - radius*2f - 25f

                up = TouchButton(hudCam.viewportWidth/2f - radius*2f - 5f, yPos,
                        radius, "+", inputNameFont!!, hudViewport)
                down = TouchButton(hudCam.viewportWidth/2f + radius*2f + 5f, yPos,
                        radius, "-", inputNameFont!!, hudViewport)
                switch = TouchButton(hudCam.viewportWidth/2f, yPos,
                        radius, "Sw", inputNameFont!!, hudViewport)
            }
        }

        // ok button for Android, and iOS
        if (isAndroidOriOS) {
            smallUIFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
                it.size = 20
            })

            val okRadius = 25f
            ok = TouchButton(hudCam.viewportWidth - okRadius - 10f,
                    hudCam.viewportHeight - okRadius - 10f, okRadius,
                    "ok", smallUIFont!!, hudViewport)
        }

        fontGen.dispose()
    }

    override fun handleInput(dt: Float) {
        val controller = BBInput.controller1()

        if (regardedAsOneOfHighScores) {
            // safe to use !! here as we checked it via flag

            // select which character to operate on
            if (BBInput.isButtonPressed(BBInput.ButtonKey.LEFT) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.DPAD_LEFT)) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.L_ANALOG_LEFT))) {
                currentNameInputCharacterIndex = (3 + currentNameInputCharacterIndex - 1) % 3
            }
            if (BBInput.isButtonPressed(BBInput.ButtonKey.RIGHT) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.DPAD_RIGHT)) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.L_ANALOG_RIGHT)) ||
                    (switch != null && switch!!.isPressed())) {
                currentNameInputCharacterIndex = (currentNameInputCharacterIndex + 1) % 3
            }

            // select characters for selected operating character
            // possible character in decimal as in ascii table is 65-90 which is A-Z which is 26 characters in total
            if (BBInput.isButtonPressed(BBInput.ButtonKey.UP) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.DPAD_UP)) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.L_ANALOG_UP)) ||
                    (up != null && up!!.isPressed())) {
                inputName!![currentNameInputCharacterIndex] = (((inputName!![currentNameInputCharacterIndex].toInt() - 65 + 1) % 26) + 65).toChar()
                // update glyph
                inputNameGlyph!!.setText(inputNameFont, inputName)
            }
            if (BBInput.isButtonPressed(BBInput.ButtonKey.DOWN) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.DPAD_DOWN)) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.L_ANALOG_DOWN)) ||
                    (down != null && down!!.isPressed())) {
                inputName!![currentNameInputCharacterIndex] = (((inputName!![currentNameInputCharacterIndex].toInt() - 65 - 1 + 26) % 26) + 65).toChar()
                // update glyph
                inputNameGlyph!!.setText(inputNameFont, inputName)
            }

            // submit
            if (BBInput.isButtonPressed(BBInput.ButtonKey.ENTER) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.A)) ||
                    (ok != null && ok!!.isPressed())) {
                // update a new score, and write to file immediately
                game.playerSaveFileManager.updateWithNewPlayerScore(PlayerScore(inputName.toString(), score), true)
                // go back to mainmenu
                gsm.setState(Mainmenu(gsm))
            }
        }
        else {
            if (BBInput.isButtonPressed(BBInput.ButtonKey.ENTER) ||
                    BBInput.isMousePressed(BBInput.MouseKey.LEFT) ||
                    (controller != null && BBInput.isControllerPressed(0, BBInput.ControllerKey.A)) ||
                    (ok != null && ok!!.isPressed())) {
                // go back to mainmenu
                gsm.setState(Mainmenu(gsm))
            }
        }
    }

    override fun update(dt: Float) {
        handleInput(dt)
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
                hudCam.viewportHeight/2f + 70f + titleGlyph.height/2f)

        if (regardedAsOneOfHighScores) {
            // now it's safe to use !! as we check via flag already
            subtitleFont!!.draw(sb, subtitleGlyph!!,
                    hudCam.viewportWidth/2f - subtitleGlyph!!.width/2f,
                    hudCam.viewportHeight/2f + 70f - titleGlyph.height/2f - 30f - subtitleGlyph!!.height/2f)
            inputNameFont!!.draw(sb, inputNameGlyph!!,
                    hudCam.viewportWidth/2f - inputNameGlyph!!.width/2f,
                    hudCam.viewportHeight/2f + 70f - titleGlyph.height/2f - 30f -
                            subtitleGlyph!!.height/2f - inputNameGlyph!!.height/2f - 35f)
            sb.end()

            // touch buttons
            sr?.let {
                val _sr = it

                up?.let { it.render(sb, _sr) }
                down?.let { it.render(sb, _sr) }
                switch?.let { it.render(sb, _sr) }
            }
        }
        else {
            sb.end()
        }

        // draw line underlying character to input name
        if (regardedAsOneOfHighScores) {
            sr!!.projectionMatrix = hudCam.combined

            // calculate position for line
            var xPos = hudCam.viewportWidth/2f - inputNameGlyph!!.width/2f + currentNameInputCharacterIndex*singleCharTextFontWidth
            val yPos = hudCam.viewportHeight/2f + 70f - titleGlyph.height/2f - 30f -
                    subtitleGlyph!!.height/2f - inputNameGlyph!!.height/2f - 35f - singleCharTextFontHeight - 4f

            // safe to use !! as we checked via flag already
            sr!!.begin(ShapeRenderer.ShapeType.Line)
            sr!!.line(xPos, yPos, xPos + singleCharTextFontWidth, yPos)
            sr!!.end()
        }

        // render ok button
        ok?.let {
            val _ok = it
            sr?.let {
                _ok.render(sb, it)
            }
        }
    }

    override fun resize_user(width: Int, height: Int) {

    }

    override fun dispose() {
        titleFont.dispose()
        sr?.dispose()
        subtitleFont?.dispose()
        smallUIFont?.dispose()
        inputNameFont?.dispose()
    }
}